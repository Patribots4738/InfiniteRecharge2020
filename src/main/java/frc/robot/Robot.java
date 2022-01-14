package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;

import autonomous.*;
import autonomous.Command.CommandType;
import hardware.*;
import interfaces.*;
import networking.*;
import utils.*;
import wrappers.*;

public class Robot extends TimedRobot {

	boolean singleDriver = false;

	boolean joystick = false;

	public static boolean emergencyManual = false;
	public static boolean superEmergencyManual = false;

	//DriverCamera cam;

	public static boolean shifted;

	boolean firstTime;

	double shootTime;
	double intakeTime;

	Countdown shootTimer;
	Countdown intakeTimer;

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

	//Limitswitch topSwitch;

	Elevator elevator;

	PIDMotor leftElevator;
	PIDMotor rightElevator;
	
	AutoPath path;
	AutoDrive auto;

	AutoSeeker seeker;

	Gamepad driveStick;

	FalconMusic falconMusic;

	PIDLoop aimLoop;

	ExternalCPUInterface cpu;

	boolean firstIntaking;
	boolean intaking;

	Limitswitch autoSwitch;

	@Override
	public void robotInit() {

		driveStick = new Gamepad(2);

		// here begin all the constructors

		//cam = new DriverCamera(0);

		Timer.init();

		shifted = true;

		firstTime = true;

		shootTime = 8;
		intakeTime = 3.833422;

		shooterCam = new Limelight("limelight-shooter");
		ballFinder = new Limelight("limelight-balls");

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

		//topSwitch = new Limitswitch(0);

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

		autoSwitch = new Limitswitch(5);

		auto.reset();

		falconMusic = new FalconMusic(new Falcon[]{});

	}

	@Override
	public void robotPeriodic() {

		gearShifter.activateChannel(shifted);

		//System.out.println("Angle: " + Calc.getAngleFromSplineDestination(60, 24));

		compressor.setState(true);

	}

	@Override
	public void autonomousInit() {

		auto.reset();

		auto.addCommands(new Command(CommandType.SPLINE, 60, 24, 0.2));
		//auto.addCommands(new Command(CommandType.MOVE, 90, 0.2));

/*
		if (!autoSwitch.getState()) {

			shootTime = 4.5;
	
		} else {

			//auto.addPath(new AutoPath("home/lvuser/deploy/autopaths/Default.json"));
			intakeTime = 0;
			auto.addCommands(new Command(CommandType.MOVE, 27, 0.2));

		}
*/
		shootTimer = new Countdown(shootTime);
		intakeTimer = new Countdown(intakeTime);

		firstTime = true;
		firstIntaking = true;
		intaking = true;
		
		//shifted = true;

		//gearShifter.activateChannel(shifted);

		shootTimer.reset();
		intakeTimer.reset();

		// config motors for positional control
		leftMotors.setPID(0.5, 0, 0);
		rightMotors.setPID(0.5, 0, 0);

		shooterControl.stop();

		//seeker.reset();

		aimLoop = new PIDLoop(.95, .15, .075, 1, 25);

	} 

	@Override
	public void autonomousPeriodic() {
		auto.executeQueue();

/*
		//seeker.runSeeker();

		System.out.println("State: " + autoSwitch.getState());
		System.out.println("Intaking: " + intaking);
		System.out.println("First Intaking: " + firstIntaking);
		System.out.println("Intake Time Remaining: " + intakeTimer.timeRemaining());
		System.out.println("Shooter Time Remaining: " + shootTimer.timeRemaining());
		System.out.println("Shoot Time: " + shootTime);
		System.out.println("Target Area Percent: " + ballFinder.getTargetAreaPercent());
		System.out.println("Command Queue Length: " + auto.getQueueLength());
		System.out.println("Shooter Timer Running: " + shootTimer.isRunning());
		System.out.println("Intake Timer Running: " + intakeTimer.isRunning());

		double throttle = 0.5;
		double maxTurning = 0.1;
		double minTurning = 0.05;
		double converter = 1.0 / 15;

		if (auto.queueIsEmpty()) {

			if(shootTimer.isRunning()) {

				System.out.println("SHOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOTING");
				
				intakeTimer.reset();

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
										
					//auto.addPath(path);

					firstTime = false;

					auto.jumpstart();

					shooterControl.stop();

				} else {

					if (firstIntaking) {

						System.out.println("REEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEESET");
						intakeTimer.reset();
	
					}

					if (!intakeTimer.isRunning()) {
	
						intaking = false;
						shootTimer = new Countdown(15);
						shootTimer.reset();
						System.out.println("STOOOOOOOOOOOOOOOOOOOOOOP INTAKING");
	
					}
	
					if (intaking) {

						System.out.println("INTAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAKING");
	
						if (ballFinder.getTargetAreaPercent() > 1.5 && firstIntaking) {
	
							firstIntaking = false;
							throttle = 0.2;
							intakeTimer.reset();
	
						}

						if (!firstIntaking) {

							throttle = 0.2;

						}
	
						// the following code makes the robot back up and suck balls
	
						// set the intake and conveyor on to collect balls
						intake.setSuck(-0.75);
						conveyor.setConveyor(true);
	
						double angle = ballFinder.getHorizontalAngle();
						double turning = -(aimLoop.getCommand(0, angle) * converter);

						System.out.println("Angle: " + angle + "; Turning: " + turning);
	
						// if turning is less than minTurning, it sets it to minTurning
						if(Math.abs(turning) < minTurning) {
	
							turning = minTurning * Math.signum(turning);
	
						}
	
						// if turning is greater than maxTurning, it sets it to maxTurning
						if(Math.abs(turning) > maxTurning) {
	
							turning = maxTurning * Math.signum(turning);
	
						}
	
						drive.bananaArcade(throttle, turning);
	
					}

				}

			}

		} else {

			System.out.println("RUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUNING COMMMANDS");

			auto.executeQueue();

		}
	*/
	}

	// NO TOUCH
	@Override 
	public void disabledInit() {

		firstTime = true;

		falconMusic.stop();
		
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

		leftElevator.resetEncoder();

		shooterControl.stop();

		driver.setupButtons();
		operator.setupButtons();

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

		double intakeMultiplier = 0.55;
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

		// if the elevator is pushing downwards onto the metal bar, then through experimentation both motors will be at
		// approximatly 50 amps, so this will make it go up just a bit until it is no longer stressing the motors
		//if(leftElevator.getAmperage() > 35) {

			//elevator.setElevatorUp();
			//leftElevator.resetEncoder();

		//} else {

			if (eleDown && leftElevator.getPosition() > -21) {

				eleDown = false;
	
			}

			if (eleUp && leftElevator.getPosition() < -125) {

				eleUp = false;

			}

			if (!eleUp && !eleDown) {

				elevator.stop();

			}

			if(eleUp) {

				elevator.setElevatorUp();
	
			} else if(eleDown) {
	
				elevator.setElevatorDown();
	
			} else {
	
				elevator.stop();
	
			}
	/*
			if(!topSwitch.getState() && eleUp) {
	
				elevator.stop();
	
			}
	*/
			elevator.setLock(elevatorLock);

		//}

	}

	// method for aligning and firing the shooter
	public void autoShoot(boolean fireInput) {

		shifted = true;

		// if we can see the target, then aim and fire
		shooterControl.aim();

		if(fireInput) {
/*
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
*/
				

				if(ShooterController.aligned) {

					shooterControl.fire();

				//}

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

	public void superEmergencyManual() {

		boolean aiming = driver.getButton(XboxController.Buttons.A);

		if(aiming) {



		} else {

			drive();

			operate(operator);

		}

		if(operator.getButton(XboxController.Buttons.A)) {

			shooterControl.emergencyFire();

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

		if(superEmergencyManual) {

			superEmergencyManual();
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

	}

	double topSpeed = 0.18;
	double bottomSpeed = 0.13; 

	int song;
	String[] songs = {"cottoneyedjoe.chrp", "imperialmarch.chrp", "mainstarwarstheme.chrp"};
	boolean playing = false;
	boolean switchSong = false;

	IMU imu;

	@Override
	public void testInit() {

		song = 0;
		playing = false;
		switchSong = false;

		falconMusic.addFalcons(new Falcon[]{new Falcon(12), new Falcon(11), new Falcon(6), new Falcon(5)});
		falconMusic.loadSong(songs[song]);
		
		// config motors for velocity control
		leftMotors.setPID(0.5, 0, 0);
		rightMotors.setPID(0.5, 0, 0);

		driver.setupButtons();
		operator.setupButtons();

		imu = new IMU();

	}
	
	@Override
	public void testPeriodic() {

		drive();

/*
		playing = driver.getToggle(XboxController.Buttons.A);
		switchSong = driver.getButtonDown(XboxController.Buttons.X);

		System.out.println("Playing: " + playing);
		System.out.println("Song num: " + song);

		if (playing) {

			if (switchSong) {

				if (song == (songs.length - 1)) {
	
					song = 0;
	
				} else {
	
					song++;
	
				}

				falconMusic.loadSong(songs[song]);
	
			}
	
			falconMusic.play();

		} else {

			falconMusic.stop();

		}
*/
		/*
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
*/
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