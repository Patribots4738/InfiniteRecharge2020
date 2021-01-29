package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;

import autonomous.*;
import hardware.*;
import interfaces.*;
import networking.*;
import utils.*;
import wrappers.*;

public class Robot extends TimedRobot {

	public static boolean soloControls = true;

	DriverCamera cam;

	public static boolean shifted;

	boolean firstTime;

	public static boolean emergencyManual = false;

	double shootTime;

	Countdown shootTimer;

	//NTTable smashBoard;

	Compressor compressor;

	DoubleSolenoid gearShifter;

	PIDMotorGroup leftMotors;
	PIDMotorGroup rightMotors;

	Drive drive;

	XboxController driver;
	XboxController operator;

	Shooter shooter;

	Intake intake;

	Conveyor conveyor;

	Limelight limelight;

	ShooterController shooterControl;

	Limitswitch topSwitch;

	Elevator elevator;
	
	AutoPath path;
	AutoDrive auto;

	@Override
	public void robotInit() {
		// here begin all the constructors

		cam = new DriverCamera(0);

		Timer.init();

		shifted = true;

		firstTime = true;

		shootTime = 13;

		shootTimer = new Countdown(shootTime);

		//smashBoard = new NTTable("/SmartDashboard");

		compressor = new Compressor();

		gearShifter = new DoubleSolenoid(7,6);

		leftMotors = new PIDMotorGroup(new Falcon(1), new Falcon(2));
		rightMotors = new PIDMotorGroup(new Falcon(3), new Falcon(4));

		drive = new Drive(leftMotors, rightMotors);

		driver = new XboxController(0);
		operator = new XboxController(1);               

		PIDMotor topShooterWheel = new Falcon(6);
		PIDMotor bottomShooterWheel = new Falcon(5);

		DoubleSolenoid shooterBlocker = new DoubleSolenoid(2,3);

		MotorGroup shooterFeeders = new MotorGroup(new Talon(10), new Talon(9));

		shooter = new Shooter(topShooterWheel, bottomShooterWheel, shooterFeeders, shooterBlocker);

		Motor intakeSucker = new Talon(8);

		intake = new Intake(intakeSucker);

		Motor conveyorDriver = new Talon(7);

		conveyor = new Conveyor(conveyorDriver);

		limelight = new Limelight();

		shooterControl = new ShooterController(conveyor, shooter, limelight, drive);

		PIDMotor leftElevator = new Falcon(12);
		PIDMotor rightElevator = new Falcon(11);

		DoubleSolenoid elevatorLock = new DoubleSolenoid(1,0);

		topSwitch = new Limitswitch(0);

		elevator = new Elevator(leftElevator, rightElevator, elevatorLock);

		auto = new AutoDrive(leftMotors, rightMotors);

		// the constructors end here, now everything gets configured

		compressor.setState(true);

		gearShifter.activateChannel(shifted);

		// drive motors have their PID configured in teleop and autonomous
		// init, as they need to be different between the two modes

		topShooterWheel.setPID(1.7, 0.15, 0.15);
		bottomShooterWheel.setPID(0.7, 0.15, 0.15);

		auto.reset();

	}

	@Override
	public void robotPeriodic() {

		gearShifter.activateChannel(shifted);

		compressor.setState(true);

	}

	@Override
	public void autonomousInit() {

		firstTime = true;
		
		shifted = true;

		gearShifter.activateChannel(shifted);

		shootTimer.reset();

		// config motors for positional control
		leftMotors.setPID(2, 0, 0);
		rightMotors.setPID(2, 0, 0);

		auto.reset();

		auto.addPath(new AutoPath("home/lvuser/deploy/autopaths/Default.json"));

		shooterControl.stop();

	} 

	@Override
	public void autonomousPeriodic() {

		if (auto.queueIsEmpty()) {

			if(shootTimer.isRunning()) {

				leftMotors.setPID(0.5, 0, 0);
				rightMotors.setPID(0.5, 0, 0);

				shooterControl.aim();

				if(ShooterController.aligned) {

					shooterControl.fire();

				}

			} else {

				leftMotors.setPID(2, 0, 0);
				rightMotors.setPID(2, 0, 0);

				if(firstTime) {
										
					auto.addPath(path);

					firstTime = false;

					auto.jumpstart();

					shooterControl.stop();

				}

			}

		} else {

			auto.executeQueue();

		}

	}

	// NO TOUCH
	@Override 
	public void disabledInit() {

		firstTime = true;

	}
	
	// VERY EXTRA NO TOUCH
	@Override
	public void disabledPeriodic() {}
	
	@Override
	public void teleopInit() {

		// config motors for velocity control
		leftMotors.setPID(0.5, 0, 0);
		rightMotors.setPID(0.5, 0, 0);

		leftMotors.resetEncoder();
		rightMotors.resetEncoder();

		shooterControl.stop();

	}

	public void drive() {

		boolean trainingWheels = true;

		boolean inverted = driver.getToggle(XboxController.Buttons.L);
		double multiplier = ((inverted) ? -1.0 : 1.0);

		double maxSpeed = 1.0;

		if(trainingWheels) {

			maxSpeed = 0.6;

		}

		multiplier *= maxSpeed;

		shifted = !driver.getToggle(XboxController.Buttons.R);

		if(trainingWheels) {

			drive.trainingWheels(-driver.getAxis(XboxController.Axes.LeftY) * multiplier, driver.getAxis(XboxController.Axes.RightX));
			
		} else {

			drive.curvature(-driver.getAxis(XboxController.Axes.LeftY) * multiplier, driver.getAxis(XboxController.Axes.RightX));

		} 

	}

	public void operate() {

		double intakeMultiplier = 0.37;
		double conveyorMultiplier = 0.275;
		double elevatorMultiplier = 0.5;

		double elevatorInput = -operator.getAxis(XboxController.Axes.LeftY);

		if(!topSwitch.getState() && elevatorInput > 0) {

			elevatorInput = 0;

		}

		elevator.setElevator(elevatorInput * elevatorMultiplier);

		elevator.setLock(operator.getToggle(XboxController.Buttons.Start));

		if(operator.getAxis(XboxController.Axes.RightTrigger) < 0.2) {

			intake.setSuck(operator.getAxis(XboxController.Axes.LeftTrigger) * intakeMultiplier);
			conveyor.setSpeed(-operator.getAxis(XboxController.Axes.LeftTrigger) * conveyorMultiplier);
			
		} else {

			intake.setSuck(-operator.getAxis(XboxController.Axes.RightTrigger) * intakeMultiplier);
			conveyor.setSpeed(operator.getAxis(XboxController.Axes.RightTrigger) * conveyorMultiplier);

		}

	}

	public void soloOperate() {
		
		double intakeMultiplier = 0.37;
		double conveyorMultiplier = 0.275;

		if(driver.getAxis(XboxController.Axes.RightTrigger) < 0.2) {

			intake.setSuck(driver.getAxis(XboxController.Axes.LeftTrigger) * intakeMultiplier);
			conveyor.setSpeed(-driver.getAxis(XboxController.Axes.LeftTrigger) * conveyorMultiplier);
			
		} else {

			intake.setSuck(-driver.getAxis(XboxController.Axes.RightTrigger) * intakeMultiplier);
			conveyor.setSpeed(driver.getAxis(XboxController.Axes.RightTrigger) * conveyorMultiplier);

		}

	}

	public void emergencyManual() {

		boolean aiming = driver.getButton(XboxController.Buttons.A);

		if(aiming) {

			shooterControl.aim();

		} else {

			drive();

		}

		operate();

		if(operator.getButton(XboxController.Buttons.A)) {

			shooterControl.fire();

		} else {

			shooter.stop();

		}

	}

	@Override
	public void teleopPeriodic() {

		// here begins the code for controlling the full robot

		boolean aiming = driver.getButton(XboxController.Buttons.A);

		if(emergencyManual) {

			emergencyManual();
			return;

		}

		if(!aiming) {

			drive();

			if (soloControls) {

				soloOperate();

			} else {

				operate();

			}            

		} else {

			shifted = true;

			// the angle between the limelight and the target is never exactly 0 unless it can't see the target
			if(limelight.getHorizontalAngle() == 0.0) {

				// and if the limelight cant see the target then it shouldn't do anything
				shooterControl.stop();

			} else {

				shooterControl.aim();

			}

			if(ShooterController.aligned) {

				if(operator.getButton(XboxController.Buttons.A)) {

					shooterControl.fire();

				} else {

					// if the operator isn't trying to fire the shooter should be off
					shooterControl.stop();

				} 

			} else {

				// if the robot isn't aligned the shooter should be off
				shooterControl.stop();

			}

		}

		shooterControl.eval();

	}

	@Override
	public void testInit() {}

	@Override
	public void testPeriodic() {

		boolean feeding = operator.getToggle(XboxController.Buttons.Y);

		if(feeding) {

			shooter.setRawSpeeds(0.58, 0.36);
			shooter.eval(0);
			shooter.setFeeders(Shooter.readyToFire);

		} else {

			shooter.stop();

		}

		drive();
		operate();

		if(feeding && operator.getAxis(XboxController.Axes.RightTrigger) > 0.2) {

			conveyor.setConveyor(true);

		}

	}

}