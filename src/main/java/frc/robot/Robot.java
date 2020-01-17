package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;

import wrappers.*;

public class Robot extends TimedRobot {

  SparkMax topShooter;
  SparkMax bottomShooter;

  /*
  PIDMotorGroup leftMotors;
  PIDMotorGroup rightMotors;

  AutoDrive autonomous;
  */

	@Override
	public void robotInit() {

    topShooter = new SparkMax(2);
    bottomShooter = new SparkMax(1);

    //topShooter.setOutputRange(0.0, 0.25);
    topShooter.setSpeedVariance(0.05);
    topShooter.setP(0.0001);
    topShooter.setI(0);
    topShooter.setD(0);

    //bottomShooter.setOutputRange(0, 0);
    bottomShooter.setSpeedVariance(0.05);
    bottomShooter.setP(0.0001);
    bottomShooter.setI(0);
    bottomShooter.setD(0);

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

    double topShooterSpeed = 0.5;
    double bottomShooterSpeed = -0.5;

    double[] weightCorrection = new double[]{
      ((37.7669895 * Math.pow(31.0431483, topShooterSpeed))/5700) + (100/5700),
      ((37.7669895 * Math.pow(31.0431483, bottomShooterSpeed))/5700) + (100/5700)
    };
    //[0.1,0.15,0.2,0.25,0.3,0.35];
    //[52.8573303,63.5714111,74.2857055,90.7139892,107.143578,123.571777];
    // Equation is really cool look at this here bro ---> 37.7669895 * 31.0431483^weightCorrection
 
    topShooter.setSpeed(topShooterSpeed);
    bottomShooter.setSpeed(bottomShooterSpeed);

    //System.out.println(topShooter.getSpeed() * 5700);
    System.out.println(bottomShooter.getSpeed() * -5700);    

  }
  
  @Override
  public void testPeriodic() {}
  
}