package hardware;

import frc.robot.Robot;
import wrappers.Limelight;

public class ShooterController {

    Shooter shooter;

    Conveyor conveyor;

    Limelight limelight;

    Drive drive;

    // these may need some tuning as things change

    // except these next two, leave these alone
    private double maxSpeed = 0.315;

    private double acceptableAngleError = 0.65;

    private double minSpeed = 0.15 * maxSpeed;

    private double converter = 1.0 / 15;

    public static boolean aligned = false;

    private double shortOffset = 3.55;

    private double longOffset = 1.68;

    public ShooterController(Conveyor conveyor, Shooter shooter, Limelight limelight, Drive drive) {

        this.shooter = shooter;

        this.conveyor = conveyor;

        this.limelight = limelight;

        this.drive = drive;

    }

    // stops the shooter
    public void stop() {

        shooter.stop();

        conveyor.setSpeed(0);

        shooter.eval(0);

    }

    // checks if the robot is aligned and if the shooter is spun up, then updates internal variables accordingly
    public void eval() {

        double offset = (limelight.getDistance() > 200) ? (longOffset) : (shortOffset);

        double angle = limelight.getHorizontalAngle() - offset;

        aligned = Math.abs(angle) <= acceptableAngleError; 
        
        if(Robot.emergencyManual) {

            shooter.eval(0);

        }

        shooter.eval(limelight.getDistance());

    }

    // this spins up the shooter and sets the conveyor and feeders based on wether the shooter is up to speed
    public void fire() {

        if(Robot.emergencyManual) {

            shooter.setRawSpeeds(0.58, 0.36);

        } else {

            shooter.setShooterSpeeds(limelight.getDistance());

        }

        eval();

        System.out.println("shooter: " + Shooter.readyToFire);

        conveyor.setConveyor(Shooter.readyToFire);

        shooter.setFeeders(Shooter.readyToFire);

    }

    public void manualFire() {

        shooter.setRawSpeeds(0.58, 0.36);

        shooter.eval(0);

        conveyor.setConveyor(Shooter.readyToFire);

        shooter.setFeeders(Shooter.readyToFire);

    }

    // this aligns the robot with the vision target found by the limelight
    public void aim() {

        double offset = (limelight.getDistance() > 200) ? (longOffset) : (shortOffset);

        double angle = limelight.getHorizontalAngle() - offset;

        aligned = Math.abs(angle) <= acceptableAngleError;

        double speed = ((angle * converter) * maxSpeed);

        speed += (minSpeed * Math.signum(angle));

        if(speed > maxSpeed) {

            speed = maxSpeed;

        }
/*
        System.out.println(aligned);
        System.out.println(speed);
        System.out.println(limelight.getHorizontalAngle());
*/
        drive.bananaTank(speed, -speed);

    }

}