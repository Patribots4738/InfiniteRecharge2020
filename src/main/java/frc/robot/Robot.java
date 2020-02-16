package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;

import autonomous.*;
import hardware.*;
import interfaces.*;
import networking.*;
import utils.*;
import wrappers.*;

public class Robot extends TimedRobot {

  Compressor compressor;

  PIDMotorGroup leftMotors;
  PIDMotorGroup rightMotors;
  
  DoubleSolenoid gearShifter;

  Drive drive;
  
  XboxController driver;

  AutoPath path;
  AutoDrive auto;


  Shooter shooter;

  Conveyor conveyor;

  Limelight limelight;

  Intake intake;

  Elevator elevator;

  ShooterController shooterControl;

  XboxController operator;


	@Override
	public void robotInit() {

    Nonstants.init();

    compressor = new Compressor();

    leftMotors = new PIDMotorGroup(new Falcon(1), new Falcon(2));
    rightMotors = new PIDMotorGroup(new Falcon(3), new Falcon(4));

    gearShifter = new DoubleSolenoid(2,3);

    drive = new Drive(leftMotors, rightMotors);

    driver = new XboxController(0);

    path = new AutoPath("/home/lvuser/deploy/autopaths/Test.json");
    
    auto = new AutoDrive(leftMotors, rightMotors);

    compressor.setState(true);

    auto.reset();

    Nonstants.setShifted(true);

    gearShifter.activateChannel(Nonstants.getShifted());

	}

	@Override
  public void robotPeriodic() {}

	@Override
	public void autonomousInit() {

    Nonstants.setShifted(true);

    gearShifter.activateChannel(Nonstants.getShifted());

    // config motors for positional control
    leftMotors.setPID(2, 0, 0);
    rightMotors.setPID(2, 0, 0);

    auto.reset();

    auto.addPath(path);

  } 

	@Override
  public void autonomousPeriodic() {

    auto.executeQueue();

  }

  // NO TOUCH
  @Override 
  public void disabledInit() {}

  // VERY NO TOUCH
  @Override
  public void disabledPeriodic() {}

	@Override
	public void teleopInit() {

    // config motors for velocity control
    leftMotors.setPID(0.5, 0, 0);
    rightMotors.setPID(0.5, 0, 0);

    leftMotors.resetEncoder();
    rightMotors.resetEncoder();

  }

  @Override
  public void teleopPeriodic() {

    boolean inverted = driver.getToggle(XboxController.Buttons.L);
    double multiplier = (inverted) ? -1.0 : 1.0;

    Nonstants.setShifted(!driver.getToggle(XboxController.Buttons.R));

    gearShifter.activateChannel(Nonstants.getShifted());
    
    drive.curvature(-driver.getAxis(XboxController.Axes.LeftY) * multiplier, driver.getAxis(XboxController.Axes.RightX));



    // skeleton of code for using the shooter
    /*
    boolean aiming = driver.getButton(XboxController.Buttons.A);

    elevator.setElevator(operator.getAxis(XboxController.Axes.LeftY));
    elevator.setLock(operator.getToggle(XboxController.Buttons.Select));

    if(!aiming) {

      drive.curvature(-driver.getAxis(XboxController.Axes.LeftY) * multiplier, driver.getAxis(XboxController.Axes.RightX));

      intake.setDown(operator.getButton(XboxController.Buttons.R));

      if(operator.getAxis(XboxController.Axes.RightTrigger) < 0.2) {

        intake.setSuck(-operator.getAxis(XboxController.Axes.LeftTrigger));
        conveyor.setConveyor(-operator.getAxis(XboxController.Axes.LeftTrigger));

      } else {

        intake.setSuck(operator.getAxis(XboxController.Axes.RightTrigger));
        conveyor.setConveyor(operator.getAxis(XboxController.Axes.RightTrigger));

      }

      shooterControl.stop();

    } else {

      shooterControl.aim();

      if(Nonstants.getAligned()) {

        if(operator.getButton(XboxController.Buttons.A)) {

          shooterControl.fire();

        }

      }

    }
    */
    
  }
  
  @Override
  public void testInit(){}
  
  @Override
  public void testPeriodic() {}
  
}