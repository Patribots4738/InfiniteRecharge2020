package frc.robot;

import autonomous.*;
import autonomous.Command.CommandType;
import edu.wpi.first.wpilibj.TimedRobot;

import wrappers.*;
import networktables.*;
import hardware.*;

public class Robot extends TimedRobot {

  NTInterface ntInterface;

  Limelight limelight;

  XboxController xbox;

  Drive drive;
  AutoDrive auto;

  PIDMotorGroup leftMotors;
  PIDMotorGroup rightMotors;

	@Override
	public void robotInit() {

    ntInterface = new NTInterface();

    //limelight = new Limelight(ntInterface.getTable("limelight"));

    xbox = new XboxController(0);

    leftMotors = new PIDMotorGroup(new SparkMax(2), new SparkMax(1));
    rightMotors = new PIDMotorGroup(new SparkMax(5), new SparkMax(4));

    leftMotors.setPID(0.1, 0 ,0);
    rightMotors.setPID(0.1, 0, 0);

    drive = new Drive(leftMotors, rightMotors);
    auto = new AutoDrive(leftMotors, rightMotors);
    
	}

	@Override
  public void robotPeriodic() {}

	@Override
	public void autonomousInit() {

    double speed = 0.25;

    auto.reset();

    auto.addCommands(new Command(CommandType.ROTATE, 0.05, speed),
                     new Command(CommandType.MOVE, 10.0, speed),

                     new Command(CommandType.ROTATE, 0.05, speed),
                     new Command(CommandType.MOVE, 10.0, speed),

                     new Command(CommandType.ROTATE, 0.05, speed),
                     new Command(CommandType.MOVE, 10.0, speed),

                     new Command(CommandType.ROTATE, 0.05, speed),
                     new Command(CommandType.MOVE, 10.0, speed),

                     new Command(CommandType.ROTATE, 0.05, speed),
                     new Command(CommandType.MOVE, 10.0, speed));

  }

	@Override
  public void autonomousPeriodic() {

    auto.executeQueue();

  }
  
	@Override
	public void teleopInit() {

    leftMotors.resetEncoder();
    rightMotors.resetEncoder();

  }

  @Override
  public void teleopPeriodic() {

    /*
    double limelightHorizontalAngle = limelight.getHorizontalAngle();
    
    double distance = limelight.getDistance();

    System.out.println("Distance: " + distance);
    System.out.println("Vertical Angle: " + limelight.getVerticalAngle());
    System.out.println("Horizontal Angle: " + limelightHorizontalAngle);

    if(xbox.getButton(XboxController.Buttons.A)) {

      limelight.setLED(Limelight.LEDMode.ON);

      double maxSpeed = 0.4;

      double speedMultiplier = 1.0 / 90.0;

      double distanceMultiplier = 1.0 / 200.0;

      double stopDistance = 150.0;

      double distanceError = distance - stopDistance;

      double minCommand = 0.02 * maxSpeed;

      double leftCommand = (speedMultiplier * limelightHorizontalAngle) + minCommand;
      double rightCommand = (speedMultiplier * limelightHorizontalAngle) + minCommand;

      if(xbox.getButton(XboxController.Buttons.B)) {

        if(limelight.getDistance() > stopDistance)  {

          leftCommand -= distanceMultiplier * distanceError;
          rightCommand += distanceMultiplier * distanceError;

        }

      }

      if(Math.abs(leftCommand) > maxSpeed) {

        leftCommand = maxSpeed * (Math.abs(leftCommand) / leftCommand);

      } 

      if(Math.abs(rightCommand) > maxSpeed) {

        rightCommand = maxSpeed * (Math.abs(rightCommand) / rightCommand);

      }
    
        leftMotors.setSpeed(leftCommand);
        rightMotors.setSpeed(rightCommand);

    } else {

      limelight.setLED(Limelight.LEDMode.OFF);

      drive.curvature(xbox.getAxis(1), xbox.getAxis(4));

    }
    */

    double throttle = -xbox.getAxis(1);

    double speedCap = 0.55;

    if(xbox.getToggle(XboxController.Buttons.Y)) {

      throttle *= -1.0;

    }
 
    if (xbox.getToggle(XboxController.Buttons.A)) {

      if(Math.abs(throttle) > speedCap) {

        throttle = speedCap * (Math.abs(throttle) / throttle);

      }

    }

    double throttleSign = (throttle < 0) ? -1 : 1;

    drive.curvature(throttle, xbox.getAxis(4) * throttleSign);

  }
  
  @Override
  public void testPeriodic() {}
  
}