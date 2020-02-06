package frc.robot;

import autonomous.*;
import autonomous.Command.CommandType;
import edu.wpi.first.wpilibj.TimedRobot;

import wrappers.*;
import networking.*;
import hardware.*;

public class Robot extends TimedRobot {

  DriverCamera cam;

	@Override
	public void robotInit() {

    cam = new DriverCamera();

	}

	@Override
  public void robotPeriodic() {

    cam.retryConstructor();
    
  }

	@Override
	public void autonomousInit() {}

	@Override
  public void autonomousPeriodic() {}
  
	@Override
	public void teleopInit() {}

  @Override
  public void teleopPeriodic() {}
  
  @Override
  public void testPeriodic() {}
  
}