package frc.robot;

import autonomous.*;
import edu.wpi.first.wpilibj.TimedRobot;

import wrappers.*;
import networking.*;
import hardware.*;

public class Robot extends TimedRobot {

  AutoPath path;

  PIDMotorGroup leftMotors;
  PIDMotorGroup rightMotors;

  Drive drive;
  AutoDrive auto;

  XboxController xbox;

	@Override
	public void robotInit() {

    path = new AutoPath("/home/lvuser/deploy/autopaths/square.json");

    leftMotors = new PIDMotorGroup(new Falcon(1), new Falcon(2));
    rightMotors = new PIDMotorGroup(new Falcon(3), new Falcon(4));

    drive = new Drive(leftMotors, rightMotors);
    auto = new AutoDrive(leftMotors, rightMotors);

    xbox = new XboxController(0);

	}

	@Override
  public void robotPeriodic() {}

	@Override
	public void autonomousInit() {

    auto.addPath(path);

  }

	@Override
  public void autonomousPeriodic() {

    auto.executeQueue();

  }
  
	@Override
	public void teleopInit() {}

  @Override
  public void teleopPeriodic() {

    drive.bananaArcade(-xbox.getAxis(XboxController.Axes.LeftY), xbox.getAxis(XboxController.Axes.RightX));

  }
  
  @Override
  public void testPeriodic() {}
  
}