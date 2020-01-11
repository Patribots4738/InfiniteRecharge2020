package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;

import autonomous.*;
import wrappers.*;

public class Robot extends TimedRobot {

  PIDMotorGroup leftMotors;
  PIDMotorGroup rightMotors;

  AutoDrive autonomous;

  PIDMotorGroup testGroup;

  @Override
  public void robotInit() {

    testGroup = new PIDMotorGroup(new SparkMax(3));

    /*
    leftMotors = new PIDMotorGroup(new SparkMax(3));
    rightMotors = new PIDMotorGroup(new SparkMax(1));

    autonomous = new AutoDrive(leftMotors, rightMotors);
    */

  }

  @Override
  public void robotPeriodic() {}

  @Override
  public void autonomousInit() {

    //autonomous.addCommand(new Command(Command.CommandType.MOVE, 12, 0.2));

  }

  @Override
  public void autonomousPeriodic() {

    //autonomous.executeQueue();

  }

  @Override
  public void teleopPeriodic(){ 

    testGroup.setPosition(1.0, 0.1);

  }
  
  @Override
  public void testPeriodic() {}
  
}