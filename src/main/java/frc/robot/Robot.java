package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;

import autonomous.*;
import hardware.*;
import interfaces.*;
import networking.*;
import utils.*;
import wrappers.*;

public class Robot extends TimedRobot {

  AutoPath path;

  PIDMotorGroup leftMotors;
  PIDMotorGroup rightMotors;

  DoubleSolenoid leftShifter;
  DoubleSolenoid rightShifter;

  Drive drive;
  AutoDrive auto;

  XboxController xbox;

	@Override
	public void robotInit() {

    path = new AutoPath("/home/lvuser/deploy/autopaths/Defualt.json");

    leftShifter = new DoubleSolenoid(0,1);
    rightShifter = new DoubleSolenoid(2,3);

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

  // NO TOUCH
  @Override 
  public void disabledInit() {}

  // VERY NO TOUCH
  @Override
  public void disabledPeriodic() {}
  
	@Override
	public void teleopInit() {

    leftShifter.activateChannel(true);
    rightShifter.activateChannel(true);

  }

  @Override
  public void teleopPeriodic() {

    boolean toggle = xbox.getToggle(XboxController.Buttons.A);

    rightShifter.activateChannel(toggle);
    leftShifter.activateChannel(toggle);


    drive.bananaArcade(-xbox.getAxis(XboxController.Axes.LeftY), xbox.getAxis(XboxController.Axes.RightX));

  }
  
  @Override
  public void testInit(){}
  
  @Override
  public void testPeriodic() {}
  
}