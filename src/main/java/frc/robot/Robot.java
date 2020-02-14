package frc.robot;

import edu.wpi.first.wpilibj.Compressor;
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
  
  XboxController xbox;

  AutoPath path;
  AutoDrive auto;

	@Override
	public void robotInit() {

    Nonstants.init();

    compressor = new Compressor();

    leftMotors = new PIDMotorGroup(new Falcon(1), new Falcon(2));
    rightMotors = new PIDMotorGroup(new Falcon(3), new Falcon(4));

    gearShifter = new DoubleSolenoid(2,3);

    drive = new Drive(leftMotors, rightMotors);

    xbox = new XboxController(0);

    path = new AutoPath("/home/lvuser/deploy/autopaths/Test.json");
    
    auto = new AutoDrive(leftMotors, rightMotors);

    auto.reset();

    Nonstants.setShifted(true);

    compressor.setClosedLoopControl(true);

    gearShifter.activateChannel(Nonstants.getShifted());

	}

	@Override
  public void robotPeriodic() {}

	@Override
	public void autonomousInit() {

    Nonstants.setShifted(true);

    gearShifter.activateChannel(Nonstants.getShifted());

    // config motors for positional control
    leftMotors.setPID(1, 0, 0.1);
    rightMotors.setPID(1, 0, 0.1);

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

    boolean inverted = xbox.getToggle(XboxController.Buttons.Y);
    double multiplier = (inverted) ? -1.0 : 1.0;

    Nonstants.setShifted(!xbox.getToggle(XboxController.Buttons.A));
    
    gearShifter.activateChannel(Nonstants.getShifted());

    drive.curvature(-xbox.getAxis(XboxController.Axes.LeftY) * multiplier, xbox.getAxis(XboxController.Axes.RightX));

    System.out.println("Fast: " + !Nonstants.getShifted() + " Inverted: " + inverted);

  }
  
  @Override
  public void testInit(){}
  
  @Override
  public void testPeriodic() {}
  
}