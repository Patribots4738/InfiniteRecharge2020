package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;

import wrappers.*;

public class Robot extends TimedRobot {

  Falcon top;
  Falcon bottom;

  /*
  PIDMotorGroup leftMotors;
  PIDMotorGroup rightMotors;

  AutoDrive autonomous;
  */

	@Override
	public void robotInit() {
    
    top = new Falcon(4);
    bottom = new Falcon(2);

    top.setPID(1.2, 0.15, 0.15);
    bottom.setPID(1.2, 0.15, 0.15);

    top.setFF(1.31147971);
    bottom.setFF(1.06429);

    /*
    testFalcon.setP(1.2);
    testFalcon.setI(0);
    testFalcon.setD(0.15);
    */

    /*
    leftMotors = new PIDMotorGroup(new SparkMax(2), new SparkMax(1));
    rightMotors = new PIDMotorGroup(new SparkMax(5), new SparkMax(4));

		rightMotors.setP(1);
		rightMotors.setI(0);
		rightMotors.setD(0);

		leftMotors.setP(1);
		leftMotors.setI(0);
		leftMotors.setD(0);

    autonomous = new AutoDrive(leftMotors, rightMotors);
    */

	}

	@Override
	public void robotPeriodic() {}

	@Override
	public void autonomousInit() {

    //autonomous.reset();

    //autonomous.addCommand(new Command(Command.CommandType.MOVE, 72, 0.07));

	}

	@Override
	public void autonomousPeriodic() {

    //autonomous.executeQueue();

	}

	@Override
	public void teleopInit() {}

  @Override
  public void teleopPeriodic() {

    // For 10 feet: topshooter = 100% and bottom = 15%, angle = 60 degrees
    // For 44 feet: topshooter = 30% and bottom = 54%, angle = 60 degrees

    // 35 feet: 21, 45 (top, bottom) ff: 1.18447971, 1.10429
    // 30 feet: 13, 49 (top, bottom)  ff: 1.31147971, 1.06429
    // 25 feet: SAME AS 30 WHAT WHY PLEASE TELL ME
    // 20 feet: 13, 43.5 (top, bottom) ff: 1.31147971, 1.06429
    // 15 feet: 13, 47 (top, bottom) ff: 1.31147971, 1.06429
    // 10 feet: 90, 13 (top, bottom) ff: 1.11147971, 1.16429
    // 5 feet: NOT POSSIBLE DO NOT MAKE SPEED ABOVE 90 IT MIGHT DESTROY THE BALL
    
    top.setSpeed(0.13);
    bottom.setSpeed(-0.375);

    System.out.println("top: " + top.getSpeed() * 100.0);
    System.out.println("bottom: " + bottom.getSpeed() * 100.0);

    /*
    testFalcon.setPosition(15, -1, 1);

    System.out.println(testFalcon.getPosition());
    */

  }
  
  @Override
  public void testPeriodic() {}
  
}