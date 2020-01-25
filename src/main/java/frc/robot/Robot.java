package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;

import wrappers.*;
import networktables.*;
import autonomous.*;

public class Robot extends TimedRobot {

  NTInterface ntInterface;

  NTTable test;

	@Override
	public void robotInit() {

    ntInterface = new NTInterface();

    test = ntInterface.getTable("Test");

    test.setBoolean("test", true);
    
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

    System.out.println(test.getBoolean("test"));

  }
  
  @Override
  public void testPeriodic() {}
  
}