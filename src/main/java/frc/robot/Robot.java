package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;

import autonomous.*;
import hardware.*;
import interfaces.*;
import networking.*;
import utils.*;
import wrappers.*;
import wrappers.XboxController.Buttons;

public class Robot extends TimedRobot {

	boolean singleDriver = false;

	public static boolean emergencyManual = false;

	DriverCamera cam;

	public static boolean shifted;

	boolean firstTime;

	double shootTime;

	Countdown shootTimer;

	NTTable smashBoard;

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

	Limelight shooterCam;
	Limelight ballFinder;

	ShooterController shooterControl;

	Limitswitch topSwitch;

	Elevator elevator;
	
	AutoPath path;
	AutoDrive auto;

	AutoSeeker seeker;

	@Override
	public void robotInit() {
		// here begin all the constructors

		cam = new DriverCamera(0);

		Timer.init();

		shifted = true;

		firstTime = true;

		shootTime = 13;

		shooterCam = new Limelight("limelight-shooter");
		ballFinder = new Limelight("limelight-balls");

		shootTimer = new Countdown(shootTime);

		smashBoard = new NTTable("/SmartDashboard");

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

		Motor conveyorDriver = new Talon(7);

		conveyor = new Conveyor(conveyorDriver);

		intake = new Intake(intakeSucker);

		shooterControl = new ShooterController(conveyor, shooter, shooterCam, drive);

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

		seeker = new AutoSeeker(intake, conveyor, ballFinder, drive, leftMotors, rightMotors);

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
		/*
		shifted = true;

		gearShifter.activateChannel(shifted);

		shootTimer.reset();

		// config motors for positional control
		leftMotors.setPID(2, 0, 0);
		rightMotors.setPID(2, 0, 0);

		auto.reset();

		auto.addPath(new AutoPath("home/lvuser/deploy/autopaths/Default.json"));

		shooterControl.stop();
		*/

		seeker.reset();

	} 

	@Override
	public void autonomousPeriodic() {

		seeker.runSeeker();

	/*
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
	*/
	}

	// NO TOUCH
	@Override 
	public void disabledInit() {

		firstTime = true;
		
		leftMotors.setPID(0.5, 0, 0);
		rightMotors.setPID(0.5, 0, 0);

		leftMotors.resetEncoder();
		rightMotors.resetEncoder();

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

			//maxSpeed = (double)smashBoard.get("trainingSpeed");
			maxSpeed = 0.65;

		}

		multiplier *= maxSpeed;

		//shifted = !driver.getToggle(XboxController.Buttons.R);

		if(trainingWheels) {

			drive.trainingWheels(-driver.getAxis(XboxController.Axes.LeftY) * multiplier, driver.getAxis(XboxController.Axes.RightX));
			
		} else {

			drive.curvature(-driver.getAxis(XboxController.Axes.LeftY) * multiplier, driver.getAxis(XboxController.Axes.RightX));

		} 

	}

	public void operate() {

		double intakeMultiplier = 0.75;
		double conveyorMultiplier = 0.275;

		/*
		boolean elevatorLock = operator.getToggle(XboxController.Buttons.Select);

		boolean eleUp = operator.getDPad(Gamepad.Directions.N);
		boolean eleDown = operator.getDPad(Gamepad.Directions.S);

		if(eleUp) {

			elevator.setElevatorUp();

		} else if(eleDown) {

			elevator.setElevatorDown();

		} else {

			elevator.stop();

		}

		if(!topSwitch.getState() && eleUp) {

			elevator.stop();

		}

		elevator.setLock(elevatorLock);
		*/

		if(operator.getAxis(XboxController.Axes.RightTrigger) < 0.2) {

			intake.setSuck(operator.getAxis(XboxController.Axes.LeftTrigger) * intakeMultiplier);
			conveyor.setSpeed(-operator.getAxis(XboxController.Axes.LeftTrigger) * conveyorMultiplier);
			
		} else {

			intake.setSuck(-operator.getAxis(XboxController.Axes.RightTrigger) * intakeMultiplier);
			conveyor.setSpeed(operator.getAxis(XboxController.Axes.RightTrigger) * conveyorMultiplier);

		}

	}

	// method for aligning and firing the shooter
	public void autoShoot(boolean fireInput) {

		shifted = true;

		// the angle between the shooterCam and the target is never exactly 0 unless it can't see the target
		if(shooterCam.getHorizontalAngle() == 0.0) {

			// and if the shooterCam cant see the target then it shouldn't do anything
			shooterControl.stop();

		} else {

			shooterControl.aim();

		}

		if(ShooterController.aligned) {

			if(fireInput) {

				shooterControl.fire();

			} else {

				// if the operator isn't trying to fire the shooter should be off
				shooterControl.stop();

			} 

		} else {

			// if the robot isn't aligned the shooter should be off
			shooterControl.stop();

		}

		shooterControl.eval();

	}

	// for use when only one driver is around
	public void soloOperate() {
		
		double intakeMultiplier = 0.75;
		double conveyorMultiplier = 0.275;

		/*
		boolean elevatorLock = driver.getToggle(XboxController.Buttons.Select);

		boolean eleUp = driver.getDPad(Gamepad.Directions.N);
		boolean eleDown = driver.getDPad(Gamepad.Directions.S);

		if(eleUp) {

			elevator.setElevatorUp();

		} else if(eleDown) {

			elevator.setElevatorDown();

		} else {

			elevator.stop();

		}

		if(!topSwitch.getState() && eleUp) {

			elevator.stop();

		}

		elevator.setLock(elevatorLock);
		*/

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

		// B for brake, as in stop the robot
		boolean brake = driver.getButton(XboxController.Buttons.B);

		if(brake) {

			rightMotors.safeStop(0.25);
			leftMotors.safeStop(0.25);

			return;

		}

		// here begins the code for controlling the full robot
		boolean aiming = driver.getButton(XboxController.Buttons.A);
		
		//System.out.println("Distance: " + shooterCam.getDistance());

		// if emergency manual mode, run only the emergency manual code, then return
		if(emergencyManual) {

			emergencyManual();
			return;

		}

		if(singleDriver) {

			if(!aiming) {

				drive();
				soloOperate();

			} else {

				autoShoot(driver.getButton(XboxController.Buttons.X));

			}

			return;

		}

		// standard operation from here on out
		if(!aiming) {

			shooterControl.stop();
			
			drive();

			operate();

		} else {

			autoShoot(operator.getButton(XboxController.Buttons.A));

		}

	}
	
	double topSpeed = 0.5;
	double bottomSpeed = 0.5;

	@Override
	public void testInit() {

		// config motors for velocity control
		leftMotors.setPID(0.5, 0, 0);
		rightMotors.setPID(0.5, 0, 0);

	}

	
	@Override
	public void testPeriodic() {

		System.out.println(shooterCam.getDistance());

		if (driver.getButtonDown(XboxController.Buttons.X)) {
			this.topSpeed += 0.025;
		} 
		if (driver.getButtonDown(XboxController.Buttons.Y)) {
			this.topSpeed -= 0.025;
		}
		if (driver.getButtonDown(XboxController.Buttons.A)) {
			this.bottomSpeed += 0.025;
		}
		if (driver.getButtonDown(XboxController.Buttons.B)) {
			this.bottomSpeed -= 0.025;
		}
		
		System.out.println("Top: " + topSpeed + "\nBottom: " + bottomSpeed);

		shooter.setFeeders(driver.getAxis(XboxController.Axes.RightTrigger) > 0.7);
		conveyor.setConveyor(driver.getAxis(XboxController.Axes.RightTrigger) > 0.7);
		
		shooter.setRawSpeeds(topSpeed, bottomSpeed);

		/*smashBoard.set("angleFromTarget", "" + shooterCam.getHorizontalAngle());
		smashBoard.set("distFromTarget", "" + shooterCam.getDistance());
		smashBoard.set("aligned", "" + (Math.abs(shooterCam.getHorizontalAngle()) <= 0.5));

		boolean seeking = driver.getButton(XboxController.Buttons.Y); //because "Y" not?!!

		if(!seeking) {

			drive();
			soloOperate();

		} else {

			seeker.runSeeker();

		}*/
	
	}

}