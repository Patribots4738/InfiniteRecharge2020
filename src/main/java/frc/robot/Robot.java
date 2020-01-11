package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;

import autonomous.*;
import wrappers.*;

public class Robot extends TimedRobot {

  PIDMotorGroup leftMotors;
  PIDMotorGroup rightMotors;

  AutoDrive autonomous;

  SparkMax testMotor;

  @Override
  public void robotInit() {

    testMotor = new SparkMax(3);
    testMotor.setP(1);
    testMotor.setI(0);
    testMotor.setD(0);

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
  public void teleopInit() {}

  @Override
  public void teleopPeriodic() { 

    testMotor.setPosition(10.0, -0.25, 0.25);

    System.out.println("position is: " + testMotor.getPosition());    

  }
  
  @Override
  public void testPeriodic() {}
  
}