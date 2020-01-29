package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;

import wrappers.*;
import networktables.*;
import hardware.*;

public class Robot extends TimedRobot {

  NTInterface ntInterface;

  Limelight limelight;

  XboxController xbox;

  Drive drive;

  PIDMotorGroup leftMotors;
  PIDMotorGroup rightMotors;

	@Override
	public void robotInit() {

    ntInterface = new NTInterface();

    limelight = new Limelight(ntInterface.getTable("limelight"));

    xbox = new XboxController(0);

    leftMotors = new PIDMotorGroup(new SparkMax(2), new SparkMax(1));
    rightMotors = new PIDMotorGroup(new SparkMax(5), new SparkMax(4));

    drive = new Drive(leftMotors, rightMotors);
    
	}

	@Override
	public void robotPeriodic() {}

	@Override
	public void autonomousInit() {}

	@Override
  public void autonomousPeriodic() {}
  
	@Override
	public void teleopInit() {}

  @Override
  public void teleopPeriodic() {

    double limelightHorizontalAngle = limelight.getHorizontalAngle();

    System.out.println("Distance: " + limelight.getDistance());
    System.out.println("Vertical Angle: " + limelight.getVerticalAngle());
    System.out.println("Horizontal Angle: " + limelightHorizontalAngle);

    if(xbox.getToggle(XboxController.Buttons.A)) {

      double maxSpeed = 0.4;

      double speedMultiplier = 1.0 / 40.0;

      double minCommand = 0.15 * maxSpeed;

      double leftCommand = (speedMultiplier * limelightHorizontalAngle) + minCommand;
      double rightCommand = (speedMultiplier * limelightHorizontalAngle) + minCommand;

      if(Math.abs(leftCommand) > maxSpeed) {

        leftCommand = maxSpeed * (Math.abs(leftCommand) / leftCommand);

      } 

      if(Math.abs(rightCommand) > maxSpeed) {

        rightCommand = maxSpeed * (Math.abs(rightCommand) / rightCommand);

      }
    
        leftMotors.setSpeed(leftCommand);
        rightMotors.setSpeed(rightCommand);

    } else {

      drive.curvature(-xbox.getAxis(1), xbox.getAxis(4));

    }

  }
  
  @Override
  public void testPeriodic() {}
  
}