package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;

import autonomous.*;
import hardware.*;
import interfaces.*;
import networking.*;
import utils.*;
import wrappers.*;

public class Robot extends TimedRobot {

    public static boolean shifted = true;
    
    // time since autonomous firing began (in seconds)
    double shooterCount = 0.0;

    // time autonomous firing is supposed to last (in seconds)
    double shootTime = 4.0;

    boolean firstTime;

    NTTable smashBoard;

    DriverCamera cam;

    DoubleSolenoid gearShifter;

    PIDMotorGroup leftMotors;
    PIDMotorGroup rightMotors;

    Drive drive;

    XboxController driver;
    XboxController operator;

    Shooter shooter;

    Intake intake;

    Conveyor conveyor;

    ShooterController shooterControl;

    Elevator elevator;
    
    AutoPath path;
    AutoDrive auto;

    @Override
    public void robotInit() {
        // here begin all the constructors
/*
        smashBoard = new NTTable("/SmartDashboard");

        cam = new DriverCamera();

        Compressor compressor = new Compressor();

        gearShifter = new DoubleSolenoid(2,3);

        leftMotors = new PIDMotorGroup(new Falcon(1), new Falcon(2));
        rightMotors = new PIDMotorGroup(new Falcon(3), new Falcon(4));

        drive = new Drive(leftMotors, rightMotors);

        driver = new XboxController(0);

        operator = new XboxController(1);               

        PIDMotor topShooter = new Falcon(5);
        PIDMotor bottomShooter = new Falcon(6);

        // placeholder port
        SingleSolenoid shooterBlocker = new SingleSolenoid(0);

        // placeholder motors
        MotorGroup shooterFeeders = new MotorGroup();

        shooter = new Shooter(topShooter, bottomShooter, shooterFeeders, shooterBlocker);

        // placeholder CAN ID
        PIDMotor intakeController = new Falcon(0);

        // placeholder CAN ID
        Motor intakeSucker = new Victor(0);

        intake = new Intake(intakeSucker, intakeController);

        // placeholder CAN ID
        Motor conveyorDriver = new Victor(0);

        conveyor = new Conveyor(conveyorDriver);

        Limelight limelight = new Limelight();

        shooterControl = new ShooterController(conveyor, shooter, limelight, drive);

        // placeholder CAN IDs
        PIDMotor leftElevator = new Falcon(0);
        PIDMotor rightElevator = new Falcon(0);

        // placeholder port
        SingleSolenoid elevatorLock = new SingleSolenoid(1);

        elevator = new Elevator(leftElevator, rightElevator, elevatorLock);

        auto = new AutoDrive(leftMotors, rightMotors);
*/
        // the constructors end here, now everything gets configured
/*
        compressor.setState(true);

        gearShifter.activateChannel(shifted);

        // drive motors have their PID configured in telop and autonomous
        // init, as they need to be different between the two modes

        topShooter.setPID(2.1, 0.15, 0.15);
        bottomShooter.setPID(2.1, 0.15, 0.15);

        // placeholder PID values
        intakeController.setPID(0, 0, 0);

        // placeholder PID values
        leftElevator.setPID(0, 0, 0);
        rightElevator.setPID(0, 0, 0);

        auto.reset();
*/
    }

    @Override
    public void robotPeriodic() {

        //cam.retryConstructor();

    }

    @Override
    public void autonomousInit() {
/*
        firstTime = true;
        
        shifted = true;

        gearShifter.activateChannel(shifted);

        // config motors for positional control
        leftMotors.setPID(2, 0, 0);
        rightMotors.setPID(2, 0, 0);

        auto.reset();

        path = new AutoPath(smashBoard.get("selectedPath").toString());

        auto.addPath(new AutoPath("home/lvuser/deploy/autopaths/Default.json"));
*/
    } 

    @Override
    public void autonomousPeriodic() {
/*
        if (auto.queueIsEmpty()) {

            if(shooterCount < shootTime) {

                shooterCount += 0.02;

                shooterControl.aim();

                if(ShooterController.aligned) {

                    shooterControl.fire();

                }

            } else {

                if(firstTime) {

                    auto.addPath(new AutoPath("/home/lvuser/deploy/autopaths/Blank.json"));

                    auto.addPath(path);

                    firstTime = false;

                }

            }

        } else {

            auto.executeQueue();

        }
*/
    }

    // NO TOUCH
    @Override 
    public void disabledInit() {}

    // VERY NO TOUCH
    @Override
    public void disabledPeriodic() {}
    
    @Override
    public void teleopInit() {
/*
        topSpeed = 0.0;
        bottomSpeed = 0.0;
        increment = 0.01;

        firstTime = true;

        // config motors for velocity control
        leftMotors.setPID(0.5, 0, 0);
        rightMotors.setPID(0.5, 0, 0);

        leftMotors.resetEncoder();
        rightMotors.resetEncoder();
*/
    }

    @Override
    public void teleopPeriodic() {
        // here begins the code for controlling the full robot
/*
        boolean inverted = driver.getToggle(XboxController.Buttons.L);
        double multiplier = (inverted) ? -1.0 : 1.0;

        shifted = !driver.getToggle(XboxController.Buttons.R);

        gearShifter.activateChannel(shifted);
        
        drive.curvature(-driver.getAxis(XboxController.Axes.LeftY) * multiplier, driver.getAxis(XboxController.Axes.RightX));


        boolean aiming = driver.getButton(XboxController.Buttons.A);

        elevator.setElevator(operator.getAxis(XboxController.Axes.LeftY));
        elevator.setLock(operator.getToggle(XboxController.Buttons.Select));

        if(!aiming) {

            drive.curvature(-driver.getAxis(XboxController.Axes.LeftY) * multiplier, driver.getAxis(XboxController.Axes.RightX));

            intake.setDown(operator.getButton(XboxController.Buttons.R));

            if(operator.getAxis(XboxController.Axes.RightTrigger) < 0.2) {

                intake.setSuck(-operator.getAxis(XboxController.Axes.LeftTrigger));
                conveyor.setConveyor(-operator.getAxis(XboxController.Axes.LeftTrigger));

            } else {

                intake.setSuck(operator.getAxis(XboxController.Axes.RightTrigger));
                conveyor.setConveyor(operator.getAxis(XboxController.Axes.RightTrigger));

            }

            shooterControl.stop();

        } else {

            shooterControl.aim();

            if(ShooterController.aligned) {

                if(operator.getButton(XboxController.Buttons.A)) {

                    shooterControl.fire();

                }

            }

        }
*/
    }
    
    @Override
    public void testInit(){}
    // temporary values for testing shooter, will not be present for final version
/*
    double topSpeed = 0.0;
    double bottomSpeed = 0.0;
    double increment = 0.01;
*/
    @Override
    public void testPeriodic() {
// here begins the code for testing the shooter
/*
        boolean currentShooter = driver.getToggle(XboxController.Buttons.X);

        if(driver.getButtonDown(XboxController.Buttons.A)) {

            increment *= 2.0;

        }

        if(driver.getButtonDown(XboxController.Buttons.B)) {

            increment *= 0.5;

        }

        if(driver.getPOV(Gamepad.Directions.N)) {

            if(currentShooter) {

                bottomSpeed += increment;

            } else {

                topSpeed += increment;

            }

        }

        if(driver.getPOV(Gamepad.Directions.S)) {

            if(currentShooter) {

                bottomSpeed -= increment;

            } else {

                topSpeed -= increment;

            }

        }

        if(driver.getToggle(XboxController.Buttons.Y)) {

            increment = 0.01;
            topSpeed = 0.0;
            bottomSpeed = 0.0;

        }

        if(topSpeed > 1.0 || bottomSpeed > 1.0) {

            topSpeed = 1.0;
            bottomSpeed = 1.0;

        }

        if(topSpeed < 0.0 || bottomSpeed < 0.0) {

            topSpeed = 0.0;
            bottomSpeed = 0.0;

        }

        topShooter.setSpeed(topSpeed);
        bottomShooter.setSpeed(-bottomSpeed);

        System.out.println("Current increment: " + increment + "\n");
        System.out.println("Current Top Speed: " + topShooter.getSpeed());
        System.out.println("Commanded Top Speed: " + topSpeed + "\n");
        System.out.println("Current Bottom Speed: " + bottomShooter.getSpeed());
        System.out.println("Commanded Bottom Speed: " + -bottomSpeed);
*/
    }
    
}