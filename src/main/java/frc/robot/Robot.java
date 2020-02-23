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

    PIDMotor topShooterWheel;
    PIDMotor bottomShooterWheel;

    Shooter shooter;

    Intake intake;

    Conveyor conveyor;

    Limelight limelight;

    MotorGroup shooterFeeders;

    ShooterController shooterControl;

    Elevator elevator;
    
    AutoPath path;
    AutoDrive auto;

    @Override
    public void robotInit() {
        // here begin all the constructors

        smashBoard = new NTTable("/SmartDashboard");
/*
        cam = new DriverCamera();

        Compressor compressor = new Compressor();

        gearShifter = new DoubleSolenoid(2,3);

        leftMotors = new PIDMotorGroup(new Falcon(1), new Falcon(2));
        rightMotors = new PIDMotorGroup(new Falcon(3), new Falcon(4));

        drive = new Drive(leftMotors, rightMotors);
*/
        driver = new XboxController(0);
/*
        operator = new XboxController(1);               
*/
        topShooterWheel = new Falcon(6);
        bottomShooterWheel = new Falcon(5);
/*
        // placeholder port
        SingleSolenoid shooterBlocker = new SingleSolenoid(0);
*/
        shooterFeeders = new MotorGroup(new Talon(10), new Talon(9));
/*
        shooter = new Shooter(topShooterWheel, bottomShooterWheel, shooterFeeders, shooterBlocker);

        // placeholder CAN ID
        PIDMotor intakeController = new Falcon(0);

        // placeholder CAN ID
        Motor intakeSucker = new Victor(0);

        intake = new Intake(intakeSucker, intakeController);

        // placeholder CAN ID
        Motor conveyorDriver = new Victor(0);

        conveyor = new Conveyor(conveyorDriver);

        limelight = new Limelight();

        shooterControl = new ShooterController(conveyor, shooter, limelight, drive);

        // placeholder CAN IDs
        PIDMotor leftElevator = new Falcon(0);
        PIDMotor rightElevator = new Falcon(0);

        // placeholder port
        SingleSolenoid elevatorLock = new SingleSolenoid(1);

        elevator = new Elevator(leftElevator, rightElevator, elevatorLock);

        auto = new AutoDrive(leftMotors, rightMotors);

        // the constructors end here, now everything gets configured

        compressor.setState(true);

        gearShifter.activateChannel(shifted);

        // drive motors have their PID configured in telop and autonomous
        // init, as they need to be different between the two modes
*/
        topShooterWheel.setPID(1.7, 0.15, 0.15);
        bottomShooterWheel.setPID(1.7, 0.15, 0.15);
/*
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

        //cam.retryConnection();

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
    public void disabledInit() {
/*
        driver.setRumble(true, 0.0);
        driver.setRumble(false, 0.0);

        operator.setRumble(true, 0.0);
        operator.setRumble(false, 0.0);
*/
    }
    // VERY EXTRA NO TOUCH
    @Override
    public void disabledPeriodic() {}
    
    @Override
    public void teleopInit() {
/*
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

            operator.setRumble(true, 0.0);
            operator.setRumble(false, 0.0);
            driver.setRumble(true, 0.0);
            driver.setRumble(false, 0.0);

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

            // buzz driver controller if they try to line up without the limelight finding a target,
            // and stop the buzzing and start lining up if the limelight finds a target
            if(!limelight.targetFound()) {

                driver.setRumble(true, 0.2);
                driver.setRumble(false, 0.2);

            } else {

                driver.setRumble(true, 0.0);
                driver.setRumble(false, 0.0);

                shooterControl.aim();

            }            

            if(ShooterController.aligned) {

                operator.setRumble(true, 0.5);
                operator.setRumble(false, 0.5);

                if(operator.getButton(XboxController.Buttons.A)) {

                    shooterControl.fire();

                }

            }

        }
*/
    }
    
    @Override
    public void testInit(){

        topSpeed = 0.0;
        bottomSpeed = 0.0;
        increment = 0.01;

    }
    // temporary values for testing shooter, will not be present for final version

    double topSpeed = 0.0;
    double bottomSpeed = 0.0;
    double increment = 0.01;

    boolean test = true;

    @Override
    public void testPeriodic() {

        test = !test;

        smashBoard.set("test", test);

        double feedRate = 0.5;

        // here begins the code for testing the shooter

        shooterFeeders.setSpeed((driver.getToggle(XboxController.Buttons.RJ)) ? -feedRate : 0.0);

        boolean currentShooter = driver.getToggle(XboxController.Buttons.X);

        if(driver.getButtonDown(XboxController.Buttons.R)) {

            increment *= 2.0;

        }

        if(driver.getButtonDown(XboxController.Buttons.L)) {

            increment *= 0.5;

        }

        if(driver.getPOV(Gamepad.Directions.N) || driver.getButtonDown(XboxController.Buttons.A)) {

            if(currentShooter) {

                bottomSpeed += increment;

            } else {

                topSpeed += increment;

            }

        }

        if(driver.getPOV(Gamepad.Directions.S) || driver.getButtonDown(XboxController.Buttons.B)) {

            if(currentShooter) {

                bottomSpeed -= increment;

            } else {

                topSpeed -= increment;

            }

        }

        if(driver.getButton(XboxController.Buttons.Y)) {

            increment = 0.01;
            topSpeed = 0.0;
            bottomSpeed = 0.0;

        }

        if(topSpeed > 1.0) {

            topSpeed = 1.0;

        }

        if(bottomSpeed > 1.0) {

            bottomSpeed = 1.0;

        }

        if(topSpeed < 0.0) {

            topSpeed = 0.0;

        }

        if(bottomSpeed < 0.0) {

            bottomSpeed = 0.0;

        }

        topShooterWheel.setSpeed(topSpeed);
        bottomShooterWheel.setSpeed(-bottomSpeed);

        System.out.println("Current increment: " + increment);
        System.out.println("Current wheel: " + ((currentShooter) ? "bottom" : "top") + "\n");
        System.out.println("Current Top Speed: " + topShooterWheel.getSpeed());
        System.out.println("Commanded Top Speed: " + topSpeed + "\n");
        System.out.println("Current Bottom Speed: " + bottomShooterWheel.getSpeed());
        System.out.println("Commanded Bottom Speed: " + -bottomSpeed);
        
    }
    
}