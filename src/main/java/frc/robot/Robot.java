package frc.robot;

import javax.xml.namespace.QName;

import edu.wpi.first.wpilibj.TimedRobot;
import wrappers.*;
import utils.*;
import interfaces.*;
//import sun.management.Sensor;
import hardware.*;

public class Robot extends TimedRobot {

  XboxController driver;

  Drive drive;

  PIDMotorGroup leftMotors;
  PIDMotorGroup rightMotors;

  @Override
  public void robotInit() {

    driver = new XboxController(0);
  
    leftMotors = new PIDMotorGroup(new SparkMax(3));
    rightMotors = new PIDMotorGroup(new SparkMax(1));

    drive = new Drive(leftMotors, rightMotors);

  }

  @Override
  public void robotPeriodic() {}

  @Override
  public void autonomousInit() {}

  @Override
  public void autonomousPeriodic() {}

  @Override
  public void teleopPeriodic() {
    
    leftMotors.setDistance(24);
    rightMotors.setDistance(48);

  }
  
  @Override
  public void testPeriodic() {}
  
}
