package frc.robot;

import javax.xml.namespace.QName;

import edu.wpi.first.wpilibj.TimedRobot;
import wrappers.*;
import utils.*;
import interfaces.*;
//import sun.management.Sensor;
import hardware.*;

public class Robot extends TimedRobot {
  
  UltraSonic ultra;

  XboxController driver;

  Drive drive;

  //ColorSensor colorSensor;

  MotorGroup leftMotors;
  MotorGroup rightMotors;

  @Override
  public void robotInit() {

    ultra = new UltraSonic(0);

    driver = new XboxController(0);

    //colorSensor = new ColorSensor();
  
    //leftMotors = new MotorGroup(new SparkMax(3));
    //rightMotors = new MotorGroup(new SparkMax(1));

    //drive = new Drive(leftMotors, rightMotors);

  }

  @Override
  public void robotPeriodic() {



  }

  @Override
  public void autonomousInit() {
  
    

  }

  @Override
  public void autonomousPeriodic() {
   


  }

  @Override
  public void teleopPeriodic() {

    //if(driver.getButton(3)){

    //drive.curvature(driver.getAxis(1), driver.getAxis(4));

    //}

      //double test = 1.1;

      //int tester = (int) test;

      //double decimal = test - tester;

  
    if(driver.getToggle(0)){

      System.out.println(ultra.getDistance());

    }
    

    //System.out.println("toggle: " + driver.getToggle(0));
    //System.out.println("wasPressed: " + driver.getButtonUp(0));

    /*
    if(driver.getToggle(0)){

      System.out.println(colorSensor.determineColor());

    }
    */
  }
  
  @Override
  public void testPeriodic() {
  
  
  }
  
}
