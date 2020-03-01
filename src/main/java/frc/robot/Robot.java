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

    Countdown shootTimer;

    boolean firstTime;

    NTTable smashBoard;

    DriverCamera cam;

    Compressor compressor;

    DoubleSolenoid gearShifter;

    PIDMotorGroup leftMotors;
    PIDMotorGroup rightMotors;

    Drive drive;

    XboxController driver;
    XboxController operator;

    // once testing is done, make these no longer global variables
    PIDMotor topShooterWheel;
    PIDMotor bottomShooterWheel;

    Shooter shooter;

    Intake intake;

    Conveyor conveyor;

    Limelight limelight;

    // once testing is done, make these no longer global variables
    MotorGroup shooterFeeders;

    ShooterController shooterControl;

    Elevator elevator;
    
    AutoPath path;
    AutoDrive auto;

    @Override
    public void robotInit() {
        // here begin all the constructors

        Timer.init();

        smashBoard = new NTTable("/SmartDashboard");

        cam = new DriverCamera();

        compressor = new Compressor();

        gearShifter = new DoubleSolenoid(7,6);

        leftMotors = new PIDMotorGroup(new Falcon(1), new Falcon(2));
        rightMotors = new PIDMotorGroup(new Falcon(3), new Falcon(4));

        drive = new Drive(leftMotors, rightMotors);

        driver = new XboxController(0);
        operator = new XboxController(1);               

        // once testing is done, make these no longer global variables
        topShooterWheel = new Falcon(6);
        bottomShooterWheel = new Falcon(5);

        DoubleSolenoid shooterBlocker = new DoubleSolenoid(2,3);

        // once testing is done, make this no longer a global variable
        shooterFeeders = new MotorGroup(new Talon(10), new Talon(9));

        shooter = new Shooter(topShooterWheel, bottomShooterWheel, shooterFeeders, shooterBlocker);

        PIDMotor intakeController = new Falcon(11);

        Motor intakeSucker = new Talon(8);

        intake = new Intake(intakeSucker, intakeController);

        Motor conveyorDriver = new Talon(7);

        conveyor = new Conveyor(conveyorDriver);

        limelight = new Limelight();

        shooterControl = new ShooterController(conveyor, shooter, limelight, drive);
/*
        // PLACEHOLDER CAN IDs
        PIDMotor leftElevator = new Falcon(0);
        PIDMotor rightElevator = new Falcon(0);

        // PLACEHOLDER port
        SingleSolenoid elevatorLock = new SingleSolenoid(1);

        elevator = new Elevator(leftElevator, rightElevator, elevatorLock);
*/
        auto = new AutoDrive(leftMotors, rightMotors);

        // the constructors end here, now everything gets configured

        compressor.setState(true);

        gearShifter.activateChannel(shifted);

        // drive motors have their PID configured in teleop and autonomous
        // init, as they need to be different between the two modes

        topShooterWheel.setPID(1.7, 0.15, 0.15);
        bottomShooterWheel.setPID(1.7, 0.15, 0.15);

        // PLACEHOLDER PID values
        intakeController.setPID(1, 0, 0);
/*
        // PLACEHOLDER PID values
        leftElevator.setPID(0, 0, 0);
        rightElevator.setPID(0, 0, 0);
*/
        auto.reset();

    }

    @Override
    public void robotPeriodic() {

        //cam.retryConnection();

        gearShifter.activateChannel(shifted);

        compressor.setState(true);

    }

    @Override
    public void autonomousInit() {

        firstTime = true;
        
        shifted = true;

        gearShifter.activateChannel(shifted);

        shootTimer = new Countdown(10.0);

        // config motors for positional control
        leftMotors.setPID(2, 0, 0);
        rightMotors.setPID(2, 0, 0);

        auto.reset();

        path = new AutoPath("home/lvuser/deploy/autopaths/Default.json");//smashBoard.get("selectedPath").toString());

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

                    auto.addPath(new AutoPath("/home/lvuser/deploy/autopaths/Blank.json"));

                    auto.addPath(path);

                    firstTime = false;

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

        driver.setRumble(true, 0.0);
        driver.setRumble(false, 0.0);

        operator.setRumble(true, 0.0);
        operator.setRumble(false, 0.0);

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

    }

    public void drive() {

        boolean trainingWheels = true;

        boolean inverted = driver.getToggle(XboxController.Buttons.L);
        double multiplier = ((inverted) ? -1.0 : 1.0);

        if(trainingWheels) {

            multiplier *= 0.2;

        }

        shifted = !driver.getToggle(XboxController.Buttons.R);

        if(trainingWheels) {

            drive.bananaArcade(-driver.getAxis(XboxController.Axes.LeftY) * multiplier, driver.getAxis(XboxController.Axes.RightX));

        } else {

            drive.curvature(-driver.getAxis(XboxController.Axes.LeftY) * multiplier, driver.getAxis(XboxController.Axes.RightX));

        }
    }

    public void operate() {

        double intakeMultiplier = 0.3;
        double conveyorMultiplier = 0.8;

        //elevator.setElevator(operator.getAxis(XboxController.Axes.LeftY));
        //elevator.setLock(operator.getToggle(XboxController.Buttons.Select));

        intake.setDown(operator.getToggle(XboxController.Buttons.R));

        if(operator.getAxis(XboxController.Axes.RightTrigger) < 0.2) {

            intake.setSuck(operator.getAxis(XboxController.Axes.LeftTrigger) * intakeMultiplier);
            //conveyor.setSpeed(-operator.getAxis(XboxController.Axes.LeftTrigger) * conveyorMultiplier);
            conveyor.setSpeed(0);

        } else {

            intake.setSuck(-operator.getAxis(XboxController.Axes.RightTrigger) * intakeMultiplier);
            conveyor.setSpeed(operator.getAxis(XboxController.Axes.RightTrigger) * conveyorMultiplier);

        }

    }

    @Override
    public void teleopPeriodic() {
        // here begins the code for controlling the full robot

        boolean aiming = driver.getButton(XboxController.Buttons.A);

        if(!aiming) {

            operator.setRumble(true, 0.0);
            operator.setRumble(false, 0.0);
            driver.setRumble(true, 0.0);
            driver.setRumble(false, 0.0);

            drive();

            operate();

            shooter.stop();

        } else {

            shifted = true;

            // buzz driver controller if they try to line up without the limelight finding a target,
            // and stop the buzzing and start lining up if the limelight finds a target
/*          if(!limelight.targetFound()) {

                driver.setRumble(true, 0.2);
                driver.setRumble(false, 0.2);

            } else {

                driver.setRumble(true, 0.0);
                driver.setRumble(false, 0.0);
*/
                shooterControl.aim();

           // }            

            if(ShooterController.aligned) {

                operator.setRumble(true, 0.5);
                operator.setRumble(false, 0.5);

                if(operator.getButton(XboxController.Buttons.A)) {

                    shooterControl.fire();

                } else {

                    shooterControl.stop();

                    conveyor.setConveyor(operator.getButton(XboxController.Buttons.B));

                }

            }

        }

    }

    // everything from here down is temporary for testing

    @Override
    public void testInit(){

        topSpeed = 0.0;
        bottomSpeed = 0.0;
        increment = 0.01;

        teleopInit();

    }

    // temporary values for testing shooter, will not be present for final version
    double topSpeed = 0.0;
    double bottomSpeed = 0.0;
    double increment = 0.01;

    public void testShooter() {
        
        // here begins the code for testing the shooter
        compressor.setState(true);

        boolean feeding = operator.getToggle(XboxController.Buttons.Y);
        
        shooter.setBlocker(feeding);
        
        shooter.setFeeders(feeding);

        conveyor.setConveyor(feeding);

        intake.setSuck((feeding) ? -0.2 : 0);
        
        boolean currentShooter = operator.getToggle(XboxController.Buttons.X);

        boolean sameSpeed = operator.getToggle(XboxController.Buttons.Start);
        
        if(operator.getButtonDown(XboxController.Buttons.RJ)) {
        
            increment *= 2.0;
        
        }
        
        if(operator.getButtonDown(XboxController.Buttons.LJ)) {
        
            increment *= 0.5;
        
        }
        
        if(operator.getPOV(Gamepad.Directions.N) || operator.getButtonDown(XboxController.Buttons.A)) {
        
            if(currentShooter) {
        
                bottomSpeed += increment;
        
            } else {
        
                topSpeed += increment;
        
            }
        
        }
        
        if(operator.getPOV(Gamepad.Directions.S) || operator.getButtonDown(XboxController.Buttons.B)) {
        
            if(currentShooter) {
        
                bottomSpeed -= increment;
        
            } else {
        
                topSpeed -= increment;
        
            }
        
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
        
        shooter.setRawSpeeds(topSpeed, ((sameSpeed) ? topSpeed : bottomSpeed));
        
        System.out.println("Current increment: " + increment);
        System.out.println("Current wheel: " + ((currentShooter) ? "bottom" : "top") + "\n");
        System.out.println("Current Top Speed: " + topShooterWheel.getSpeed());
        System.out.println("Commanded Top Speed: " + topSpeed + "\n");
        System.out.println("Current Bottom Speed: " + bottomShooterWheel.getSpeed());
        System.out.println("Commanded Bottom Speed: " + -bottomSpeed);
        
    }

    @Override
    public void testPeriodic() {

        boolean aiming = driver.getButton(XboxController.Buttons.A);
        
        drive();
        operate();
        testShooter();

        
        if(aiming) {

            System.out.println(limelight.getDistance());
            shooterControl.aim();
            System.out.println(ShooterController.aligned);

        }
        
    }

}