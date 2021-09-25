package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;

import autonomous.*;
import hardware.*;
import interfaces.*;
import networking.*;
import utils.*;
import wrappers.*;

public class Robot extends TimedRobot {

	boolean singleDriver = false;

	boolean joystick = false;

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

	PIDMotor leftElevator;
	PIDMotor rightElevator;
	
	AutoPath path;
	AutoDrive auto;

	AutoSeeker seeker;

	Gamepad driveStick;

	@Override
	public void robotInit() {

		driveStick = new Gamepad(2);

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

		leftElevator = new Falcon(12);
		rightElevator = new Falcon(11);

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

		boolean trainingWheels = false;

		boolean inverted = driver.getToggle(XboxController.Buttons.R);
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

	public void operate(XboxController controller) {

		double intakeMultiplier = 0.75;
		double conveyorMultiplier = 0.275;

		if(controller.getAxis(XboxController.Axes.RightTrigger) < 0.2) {

			intake.setSuck(controller.getAxis(XboxController.Axes.LeftTrigger) * intakeMultiplier);
			conveyor.setSpeed(-controller.getAxis(XboxController.Axes.LeftTrigger) * conveyorMultiplier);
			
		} else {

			intake.setSuck(-controller.getAxis(XboxController.Axes.RightTrigger) * intakeMultiplier);
			conveyor.setSpeed(controller.getAxis(XboxController.Axes.RightTrigger) * conveyorMultiplier);

		}

		
		boolean elevatorLock = controller.getToggle(XboxController.Buttons.Select);

		boolean eleUp = controller.getDPad(Gamepad.Directions.N);
		boolean eleDown = controller.getDPad(Gamepad.Directions.S);

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
		

	}

	// method for aligning and firing the shooter
	public void autoShoot(boolean fireInput) {

		shifted = true;

		if(fireInput) {

			// the area of the target is never exactly 0 unless it can't see the target
			if(shooterCam.getTargetAreaPercent() <= 0.01) {

				// if we're trying to fire and can't see the target, we're trying to baby shot

				double topSpeed = 0.18;
				double bottomSpeed = 0.13;

				shooter.setRawSpeeds(topSpeed, bottomSpeed);

				shooter.rawEval(topSpeed, bottomSpeed);

				shooter.setFeeders(Shooter.readyToFire);

				conveyor.setConveyor(Shooter.readyToFire);

			} else {

				// if we can see the target, then aim and fire
				shooterControl.aim();

				if(ShooterController.aligned) {

					shooterControl.fire();

				}

			}

		} else {

			// if we aren't trying to fire, we should be stopped
			shooterControl.stop();

		}

		shooterControl.eval();

	}

	public void hyperDrive() {

		double throttle = Math.max(driver.getAxis(XboxController.Axes.RightTrigger), driver.getAxis(XboxController.Axes.LeftTrigger));

		double multiplier = ((throttle > 0.08) ? 0.6 : 0.7);

		//double inverted = ((driver.getDPad(Gamepad.Directions.W)) ? 1.0 : -1.0);

		double inverted = ((driver.getAxis(XboxController.Axes.LeftTrigger) > driver.getAxis(XboxController.Axes.RightTrigger)) ? 1.0 : -1.0);

		drive.curvature(throttle * inverted * multiplier, driver.getAxis(XboxController.Axes.RightX));

	}

	public void joyDrive() {

		if(driveStick.getButton(2)) {

			autoShoot(driveStick.getButton(0));

			return;

		} else {

			shooterControl.stop();

		}

		double throttle = 0;
		double turning = 0;

		if(driveStick.getPOV(Gamepad.Directions.N)) {

			throttle = 0.4;

		}

		if(driveStick.getPOV(Gamepad.Directions.S)) {

			throttle = -0.4;

		}

		if(driveStick.getButton(1)) {

			turning = (driveStick.getAxis(2) * 0.3);

		}

		drive.trainingWheels(throttle, turning);

		if (driveStick.getButton(3)) {

			intake.setSuck(-0.5);
			conveyor.setSpeed(0.4);

		} else if (driveStick.getButton(5)) {

			intake.setSuck(0.5);
			conveyor.setSpeed(-0.4);

		} else {

			intake.setSuck(0);
			conveyor.setSpeed(0);

		}

	}

	public void emergencyManual() {

		boolean aiming = driver.getButton(XboxController.Buttons.A);

		if(aiming) {

			shooterControl.aim();

		} else {

			drive();

			operate(operator);

		}

		if(operator.getButton(XboxController.Buttons.A)) {

			shooterControl.fire();

		} else {

			shooter.stop();

		}

	}

	@Override
	public void teleopPeriodic() {

		// initial 3 exception cases

		// exception case for joystick driving
		if(joystick) {

			hyperDrive();
			
			return;

		}

		// excpetion case for emergency manual mode
		if(emergencyManual) {

			emergencyManual();
			return;

		}

		// exception case for sinlge operator/driver mode
		if(singleDriver) {

			if(!driver.getButton(XboxController.Buttons.A)) {

				drive();
				operate(driver);

			} else {

				autoShoot(driver.getButton(XboxController.Buttons.X));

			}

			return;

		}

		// after this is normal operation

		// B for brake, as in stop the robot
		boolean brake = driver.getButton(XboxController.Buttons.B);

		if(brake) {

			rightMotors.safeStop(0.25);
			leftMotors.safeStop(0.25);

			return;

		}
		
		boolean aiming = driver.getButton(XboxController.Buttons.A);		

		if(aiming) {

			autoShoot(operator.getButton(XboxController.Buttons.A));			

		} else {

			shooterControl.stop();
			
			drive();

			operate(operator);

		}

		System.out.println("Left Elevator Amperage: " + leftElevator.getAmperage());
		System.out.println("Right Elevator Amperage: " + rightElevator.getAmperage());

	}

	double topSpeed = 0.18;
	double bottomSpeed = 0.13;

	@Override
	public void testInit() {

		// config motors for velocity control
		leftMotors.setPID(0.5, 0, 0);
		rightMotors.setPID(0.5, 0, 0);

	}
	
	@Override
	public void testPeriodic() {
		
		System.out.println("corrected Distance: " + shooterControl.correctLimelightDistanceError(shooterCam.getDistance()));
		System.out.println(".");
		System.out.println("raw Distance: " + shooterCam.getDistance());
		System.out.println(".");

		double interval = 0.005;
		
		if (driver.getButtonDown(XboxController.Buttons.X)) {
			this.topSpeed += interval;
		} 
		if (driver.getButtonDown(XboxController.Buttons.Y)) {
			this.topSpeed -= interval;
		}
		if (driver.getButtonDown(XboxController.Buttons.A)) {
			this.bottomSpeed += interval;
		}
		if (driver.getButtonDown(XboxController.Buttons.B)) {
			this.bottomSpeed -= interval;
		}
		
		System.out.println("Top: " + topSpeed + "\nBottom: " + bottomSpeed);
		System.out.println(".");

		shooter.setFeeders(driver.getAxis(XboxController.Axes.RightTrigger) > 0.7);
		conveyor.setConveyor(driver.getAxis(XboxController.Axes.RightTrigger) > 0.7);
		
		shooter.setRawSpeeds(topSpeed, bottomSpeed);

		shooter.rawEval(topSpeed, bottomSpeed);

		shooter.setBlocker(Shooter.readyToFire);

		/*
		smashBoard.set("angleFromTarget", "" + shooterCam.getHorizontalAngle());
		smashBoard.set("distFromTarget", "" + shooterCam.getDistance());
		smashBoard.set("aligned", "" + (Math.abs(shooterCam.getHorizontalAngle()) <= 0.5));

		boolean seeking = driver.getButton(XboxController.Buttons.Y); //because "Y" not?!!

		if(!seeking) {

			drive();
			soloOperate();

		} else {

			seeker.runSeeker();

		}
		*/
	
	}

}