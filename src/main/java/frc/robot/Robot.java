package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;

import autonomous.*;
import wrappers.*;

public class Robot extends TimedRobot {

  PIDMotorGroup leftMotors;
  PIDMotorGroup rightMotors;

  AutoDrive autonomous;

  @Override
  public void robotInit() {

    leftMotors = new PIDMotorGroup(new SparkMax(3));
    rightMotors = new PIDMotorGroup(new SparkMax(1));

    rightMotors.setP(1);
    rightMotors.setI(0);
    rightMotors.setD(0);

    leftMotors.setP(1);
    leftMotors.setI(0);
    leftMotors.setD(0);

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
  public void teleopInit() {}

  @Override
  public void teleopPeriodic() {}
  
  @Override
  public void testPeriodic() {}
  
}