package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;

import wrappers.*;
import networktables.*;
import autonomous.*;

public class Robot extends TimedRobot {

  NTInterface ntInterface;

  NTTable SmashboardTest;

	@Override
	public void robotInit() {

    ntInterface = new NTInterface();

    SmashboardTest = ntInterface.getTable("/SmartDashboard");
    
	}

	@Override
	public void robotPeriodic() {}

	@Override
	public void autonomousInit() {}

	@Override
  public void autonomousPeriodic() {}
  
	@Override
	public void teleopInit() {}

  @Override
  public void teleopPeriodic() {

    System.out.println(SmashboardTest.getKeys().length);

  }
  
  @Override
  public void testPeriodic() {}
  
}