package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;

import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj.Timer;

import autonomous.*;
import hardware.*;
import interfaces.*;
import lowleveltools.*;
import networking.*;
import utils.*;
import wrappers.*;

public class Robot extends TimedRobot {

    PWMPort servo;

    public static boolean shifted = true;

    // time since autonomous firing began (in seconds)
    double shooterCount = 0.0;

    // time autonomous firing is supposed to last (in seconds) PLACEHOLDER
    double shootTime = 4.0;

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

    ColorSensor colorSensor;

    boolean seenBall;

    @Override
    public void robotInit() {
        // here begin all the constructors

        seenBall = false;

        colorSensor = new ColorSensor();

        smashBoard = new NTTable("/SmartDashboard");

        cam = new DriverCamera();

        compressor = new Compressor();

        // PLACEHOLDER ports
        gearShifter = new DoubleSolenoid(7,6);

        leftMotors = new PIDMotorGroup(new Falcon(1), new Falcon(2));
        rightMotors = new PIDMotorGroup(new Falcon(3), new Falcon(4));

        drive = new Drive(leftMotors, rightMotors);

        driver = new XboxController(0);
        operator = new XboxController(1);               

        topShooterWheel = new Falcon(6);
        bottomShooterWheel = new Falcon(5);

        DoubleSolenoid shooterBlocker = new DoubleSolenoid(2,3);

        shooterFeeders = new MotorGroup(new Talon(10), new Talon(9));

        shooter = new Shooter(topShooterWheel, bottomShooterWheel, shooterFeeders, shooterBlocker);

        // PLACEHOLDER CAN ID
        PIDMotor intakeController = new Falcon(11);

        Motor intakeSucker = new Talon(8);

        intake = new Intake(intakeSucker, intakeController);

        Motor conveyorDriver = new Talon(7);

        conveyor = new Conveyor(conveyorDriver);

        //limelight = new Limelight();

        //shooterControl = new ShooterController(conveyor, shooter, limelight, drive);
/*
        // PLACEHOLDER CAN IDs
        PIDMotor leftElevator = new Falcon(0);
        PIDMotor rightElevator = new Falcon(0);

        // PLACEHOLDER port
        SingleSolenoid elevatorLock = new SingleSolenoid(1);

        elevator = new Elevator(leftElevator, rightElevator, elevatorLock);

        auto = new AutoDrive(leftMotors, rightMotors);
*/
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

        auto.reset();
*/
    }

    @Override
    public void robotPeriodic() {

        //cam.retryConnection();

        compressor.setState(true);

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

    @Override
    public void teleopPeriodic() {
        // here begins the code for controlling the full robot
        boolean inverted = driver.getToggle(XboxController.Buttons.L);
        double multiplier = (inverted) ? -1.0 : 1.0;

        shifted = !driver.getToggle(XboxController.Buttons.R);

        gearShifter.activateChannel(shifted);

        double intakeMultiplier = 0.5;
/*
        boolean aiming = driver.getButton(XboxController.Buttons.A);

        elevator.setElevator(operator.getAxis(XboxController.Axes.LeftY));
        elevator.setLock(operator.getToggle(XboxController.Buttons.Select));

        if(!aiming) {

            operator.setRumble(true, 0.0);
            operator.setRumble(false, 0.0);
            driver.setRumble(true, 0.0);
            driver.setRumble(false, 0.0);
*/
            drive.curvature(-driver.getAxis(XboxController.Axes.LeftY) * multiplier, driver.getAxis(XboxController.Axes.RightX));

            intake.setDown(operator.getToggle(XboxController.Buttons.R));

            if(operator.getAxis(XboxController.Axes.RightTrigger) < 0.2) {

                intake.setSuck(operator.getAxis(XboxController.Axes.LeftTrigger) * intakeMultiplier);

            } else {

                intake.setSuck(-operator.getAxis(XboxController.Axes.RightTrigger) * intakeMultiplier);

            }

            if(operator.getButtonDown(XboxController.Buttons.LJ)) {

                conveyor.resetTime();

            }

            conveyor.setConveyor(conveyor.increment());

/* DELETE THIS  
*/
compressor.setState(true);

boolean feeding = operator.getToggle(XboxController.Buttons.RJ);

shooter.setBlocker(feeding);

shooter.setFeeders(feeding);

boolean currentShooter = operator.getToggle(XboxController.Buttons.X);

if(operator.getButton(XboxController.Buttons.B)) {

    conveyor.setSpeed(-0.35);

}

if(operator.getButton(XboxController.Buttons.Y)) {

    conveyor.setSpeed(0.35);

}

if(operator.getButtonDown(XboxController.Buttons.R)) {

    increment *= 2.0;

}

if(operator.getButtonDown(XboxController.Buttons.L)) {

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

if(operator.getButton(XboxController.Buttons.Y)) {

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
/*
if(colorSensor.getRGBIR()[3] < 2.6) {

    System.out.println("ball");
    conveyor.setSpeed(0.5);

}

if(seenBall = true && colorSensor.getRGBIR()[3] < 2.6) {

    seenBall = false;
    conveyor.setSpeed(0.5);

}
*/
shooter.setRawSpeeds(topSpeed, topSpeed);
/*
System.out.println("Current increment: " + increment);
System.out.println("Current wheel: " + ((currentShooter) ? "bottom" : "top") + "\n");
System.out.println("Current Top Speed: " + topShooterWheel.getSpeed());
System.out.println("Commanded Top Speed: " + topSpeed + "\n");
System.out.println("Current Bottom Speed: " + bottomShooterWheel.getSpeed());
System.out.println("Commanded Bottom Speed: " + -bottomSpeed);
   */         
/*
*/

/*
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

    int servoPos = 0;

    boolean test = true;

    @Override
    public void testPeriodic() {

        // here begins the code for testing the shooter

        boolean feeding = driver.getToggle(XboxController.Buttons.RJ);

        intake.setDown(driver.getToggle(XboxController.Buttons.LJ));

        shooter.setFeeders(feeding);

        conveyor.setConveyor(feeding);

        intake.setSuck((feeding) ? 0.5 : 0.0);

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

        shooter.setRawSpeeds(topSpeed, bottomSpeed);

        System.out.println("Current increment: " + increment);
        System.out.println("Current wheel: " + ((currentShooter) ? "bottom" : "top") + "\n");
        System.out.println("Current Top Speed: " + topShooterWheel.getSpeed());
        System.out.println("Commanded Top Speed: " + topSpeed + "\n");
        System.out.println("Current Bottom Speed: " + bottomShooterWheel.getSpeed());
        System.out.println("Commanded Bottom Speed: " + -bottomSpeed);

    }
  
}