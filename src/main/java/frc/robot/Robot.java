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

    public static boolean emergencyManual = true;

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
        
        //smashBoard.set("EMERGENCY", emergencyManual);

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
        //auto.addPath(new AutoPath("home/lvuser/deploy/autopaths/Default.json"));
/*
        String smashBoardPath;

        try {

            smashBoardPath = smashBoard.get("autoPath").toString();

        } catch (Exception e) {

            // if the smashBoard doesn't return anything, just do nothing
            smashBoardPath = "Blank";

        }

        path = new AutoPath("home/lvuser/deploy/autopaths/" + smashBoardPath + ".json");
*/
        shooterControl.stop();

    } 

    @Override
    public void autonomousPeriodic() {

        //smashBoard.set("enabled", true);
        if (auto.queueIsEmpty()) {

            if(shootTimer.isRunning()) {
/*
                leftMotors.setPID(0.5, 0, 0);
                rightMotors.setPID(0.5, 0, 0);

                shooterControl.aim();

                if(ShooterController.aligned) {
*/
                    shooterControl.manualFire();
/*
                }
*/
            } else {

                //leftMotors.setPID(2, 0, 0);
                //rightMotors.setPID(2, 0, 0);

                if(firstTime) {
                    
                    auto.addPath(new AutoPath("/home/lvuser/deploy/autopaths/Default.json"));
                    
                    //auto.addPath(path);

                    firstTime = false;

                    //auto.jumpstart();
                    

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

        //smashBoard.set("enabled", false);

    }
    
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

        boolean trainingWheels = driver.getToggle(XboxController.Buttons.Y);

        boolean inverted = driver.getToggle(XboxController.Buttons.L);
        double multiplier = ((inverted) ? -1.0 : 1.0);

        double maxSpeed = 1.0;
/*
        if(trainingWheels) {

            maxSpeed = 0.6;

        }
*/
        multiplier *= maxSpeed;

        //smashBoard.set("reversed", inverted);

        shifted = !driver.getToggle(XboxController.Buttons.R);

        if(trainingWheels) {

            drive.bananaArcade(-driver.getAxis(XboxController.Axes.LeftY) * multiplier, driver.getAxis(XboxController.Axes.RightX));
            
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

        // set the rumbles to 0 to disable them, unless they're later reset
        // also resets them to 0 if they aren't being told to do stuff later
        operator.setRumble(true, 0.0);
        operator.setRumble(false, 0.0);
        driver.setRumble(true, 0.0);
        driver.setRumble(false, 0.0);

        //smashBoard.set("enabled", true);

        //boolean aiming = driver.getButton(XboxController.Buttons.A);

        emergencyManual();
/*
        if(operator.getButtonDown(XboxController.Buttons.Y) && driver.getButton(XboxController.Buttons.X) && driver.getButton(XboxController.Buttons.Y)) {

            emergencyManual = !emergencyManual;

        }

        if(emergencyManual) {

            emergencyManual();
            return;

        }

        if(!aiming) {

            drive();

            operate();


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

                }

            } else {

                shooterControl.stop();

            }

        }
*/
        //shooterControl.eval();

        //smashBoard.set("firing", Robot.emergencyManual);
        //smashBoard.set("readyToFire", ShooterController.aligned);
        //smashBoard.set("aligned", !(limelight.getHorizontalAngle() == 0.0));

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