package frc.robot;

import javax.xml.namespace.QName;

import edu.wpi.first.wpilibj.TimedRobot;
import wrappers.*;
import utils.*;
import interfaces.*;
import hardware.*;
import autonomous.*;

public class Robot extends TimedRobot {

  XboxController driver;

  Drive drive;

  PIDMotorGroup leftMotors;
  PIDMotorGroup rightMotors;

  AutoDrive autonomous;

  @Override
  public void robotInit() {

    driver = new XboxController(0);
  
    leftMotors = new PIDMotorGroup(new SparkMax(3));
    rightMotors = new PIDMotorGroup(new SparkMax(1));

    drive = new Drive(leftMotors, rightMotors);

    autonomous = new AutoDrive(leftMotors, rightMotors);

  }

  @Override
  public void robotPeriodic() {}

  @Override
  public void autonomousInit() {

    autonomous.addCommand(new Command(Command.CommandType.MOVE, 12, 0.2));

  }

  @Override
  public void autonomousPeriodic() {

    autonomous.executeQueue();

  }

  @Override
  public void teleopPeriodic() {}
  
  @Override
  public void testPeriodic() {}
  
}
