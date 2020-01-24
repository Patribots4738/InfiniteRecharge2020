package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;

import wrappers.*;
import autonomous.*;

public class Robot extends TimedRobot {
  
  PIDMotorGroup leftMotors;
  PIDMotorGroup rightMotors;

  AutoDrive autonomous;  

	@Override
	public void robotInit() {
    
    leftMotors = new PIDMotorGroup(new SparkMax(2), new SparkMax(1));
    rightMotors = new PIDMotorGroup(new SparkMax(5), new SparkMax(4));

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

    autonomous.reset();

    autonomous.addCommands(
      new Command(Command.CommandType.ROTATE, -0.25, 0.1),
      new Command(Command.CommandType.MOVE, 10, 0.1),
      new Command(Command.CommandType.ROTATE, -0.25, 0.1),
      new Command(Command.CommandType.MOVE, 10, 0.1),
      new Command(Command.CommandType.ROTATE, -0.25, 0.1),
      new Command(Command.CommandType.MOVE, 10, 0.1),
      new Command(Command.CommandType.ROTATE, -0.25, 0.1),
      new Command(Command.CommandType.MOVE, 10, 0.1)
    );

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