package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;

import autonomous.*;
import hardware.*;
import interfaces.*;
import networking.*;
import utils.*;
import wrappers.*;

public class Robot extends TimedRobot {

    public static boolean shifted;

    boolean firstTime;

    double shootTime;

    Countdown shootTimer;

    NTTable smashBoard;

    DriverCamera forwardCam;
    DriverCamera reverseCam;

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

    Elevator elevator;
    
    AutoPath path;
    AutoDrive auto;

    @Override
    public void robotInit() {
        // here begin all the constructors

        shifted = true;

        firstTime = true;

        Timer.init();

        shootTime = 8.5;

        shootTimer = new Countdown(shootTime);

        smashBoard = new NTTable("/SmartDashboard");

        try {
            
            forwardCam = new DriverCamera(1);

        } catch (Exception e) {

            System.err.println("forward camera failed to start!");

        }

        try {

            reverseCam = new DriverCamera(0);

        } catch (Exception e) {

            System.err.println("reverse camera failed to start!");

        }

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

        Limitswitch topSwitch = new Limitswitch(0);
        Limitswitch bottomSwitch = new Limitswitch(1);

        DoubleSolenoid elevatorLock = new DoubleSolenoid(1,0);

        elevator = new Elevator(leftElevator, rightElevator, elevatorLock, topSwitch, bottomSwitch);

        auto = new AutoDrive(leftMotors, rightMotors);

        // the constructors end here, now everything gets configured

        compressor.setState(true);

        gearShifter.activateChannel(shifted);

        // drive motors have their PID configured in teleop and autonomous
        // init, as th   ey need to be different between the two modes

        topShooterWheel.setPID(1.7, 0.15, 0.15);
        bottomShooterWheel.setPID(1.7, 0.15, 0.15);

        auto.reset();

    }

    @Override
    public void robotPeriodic() {

        //forwardCam.retryConnection();
        //reverseCam.retryConnection();

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

        // add universal default path
        auto.addPath(new AutoPath("home/lvuser/deploy/autopaths/Default.json"));

        String smashBoardPath = smashBoard.get("autoPath").toString();

        // 9 is the smallest possible size of a valid string for the path,
        // all valid paths are 9 or more, and all nonvalid ones are less
        if(smashBoardPath.length() > 8) {
            System.out.println("ADDED PATH");
            path = new AutoPath("home/lvuser/deploy/autopaths/" + smashBoardPath  + ".json");

        } else {
            System.out.println("DEFAULT ADDED");
            // if the smashBoard path is invalid, just do the default path again
            path = new AutoPath("home/lvuser/deploy/autopaths/Default.json");

        }

        shooterControl.stop();

    } 

    @Override
    public void autonomousPeriodic() {

        smashBoard.set("enabled", true);

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

        driver.setRumble(true, 0.0);
        driver.setRumble(false, 0.0);
        operator.setRumble(true, 0.0);
        operator.setRumble(false, 0.0);

    }
    
    // VERY EXTRA NO TOUCH
    @Override
    public void disabledPeriodic() {

        smashBoard.set("enabled", false);

    }
    
    @Override
    public void teleopInit() {

        // config motors for velocity control
        leftMotors.setPID(0.5, 0, 0);
        rightMotors.setPID(0.5, 0, 0);

        leftMotors.resetEncoder();
        rightMotors.resetEncoder();

    }

    public void drive() {

        boolean trainingWheels = false;

        boolean inverted = driver.getToggle(XboxController.Buttons.L);
        double multiplier = ((inverted) ? -1.0 : 1.0);

        double maxSpeed = 1.0;

        if(trainingWheels) {

            maxSpeed = 0.6;

        }

        multiplier *= maxSpeed;

        smashBoard.set("reversed", inverted);

        shifted = !driver.getToggle(XboxController.Buttons.R);

        if(trainingWheels) {

            drive.bananaArcade(-driver.getAxis(XboxController.Axes.LeftY) * multiplier, driver.getAxis(XboxController.Axes.RightX));
            return;
            
        }

        drive.curvature(-driver.getAxis(XboxController.Axes.LeftY) * multiplier, driver.getAxis(XboxController.Axes.RightX));

    }

    public void operate() {

        double intakeMultiplier = 0.35;
        double conveyorMultiplier = 0.8;
        double elevatorMultiplier = 0.5;

        elevator.setElevator(-operator.getAxis(XboxController.Axes.LeftY) * elevatorMultiplier);

        elevator.setLock(operator.getToggle(XboxController.Buttons.Start));

        if(operator.getAxis(XboxController.Axes.RightTrigger) < 0.2) {

            intake.setSuck(operator.getAxis(XboxController.Axes.LeftTrigger) * intakeMultiplier);
            conveyor.setSpeed(-operator.getAxis(XboxController.Axes.LeftTrigger) * conveyorMultiplier);
            
        } else {

            intake.setSuck(-operator.getAxis(XboxController.Axes.RightTrigger) * intakeMultiplier);
            conveyor.setSpeed(operator.getAxis(XboxController.Axes.RightTrigger) * conveyorMultiplier);

        }

    }

    @Override
    public void teleopPeriodic() {

        // here begins the code for controlling the full robot

        // set the rumbles to 0 to disable them, unless they're later reset
        // also resets them to 0 if they aren't being told to do stuff later
        operator.setRumble(true, 0.0);
        operator.setRumble(false, 0.0);
        driver.setRumble(true, 0.0);
        driver.setRumble(false, 0.0);

        smashBoard.set("enabled", true);

        boolean aiming = driver.getButton(XboxController.Buttons.A);

        if(!aiming) {

            drive();

            operate();

            shooter.stop();

        } else {

            shifted = true;

            // the angle between the limelight and the target is never exactly 0 unless it can't see the target
            if(limelight.getHorizontalAngle() == 0.0) {

                // buzz driver controller if they try to line up without the limelight finding a target,
                driver.setRumble(true, 0.5);
                driver.setRumble(false, 0.5);

            } else {

                shooterControl.aim();

            }

            if(ShooterController.aligned) {

                // start buzzing the operator controller if they're ready to fire
                operator.setRumble(true, 0.5);
                operator.setRumble(false, 0.5);

                if(operator.getButton(XboxController.Buttons.A)) {

                    shooterControl.fire();

                } else {

                    shooterControl.stop();

                }

            }

        }

        shooterControl.eval();

        smashBoard.set("firing", Shooter.readyToFire);
        smashBoard.set("readyToFire", ShooterController.aligned);
        smashBoard.set("aligned", !(limelight.getHorizontalAngle() == 0.0));

    }

    @Override
    public void testInit() {}

    @Override
    public void testPeriodic() {}

}