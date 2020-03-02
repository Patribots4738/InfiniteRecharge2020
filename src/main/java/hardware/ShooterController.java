package hardware;

import wrappers.Limelight;

public class ShooterController {

    Shooter shooter;

    Conveyor conveyor;

    Limelight limelight;

    Drive drive;

    // all of these doubles are placeholders pending testing
    private double maxSpeed = 0.3;

    private double minSpeed = 0.22 * maxSpeed;

    private double converter = 1.0 / 15.0;

    private double acceptableAngleError = 0.65;

    public static boolean aligned = false;

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

    }

    // this spins up the shooter and sets the conveyor and feeders based on wether the shooter is up to speed
    public void fire() {

        shooter.setShooterSpeeds(limelight.getDistance());

        conveyor.setConveyor(Shooter.readyToFire);

        shooter.setFeeders(Shooter.readyToFire);

    }

    // this aligns the robot with the vision target found by the limelight
    public void aim() {

        double offset = (limelight.getDistance() > 200) ? (1.68) : (3.55);

        double angle = limelight.getHorizontalAngle() - offset;

        aligned = Math.abs(angle) <= acceptableAngleError;

        double speed = ((angle * converter) * maxSpeed);

        speed += (minSpeed * Math.signum(angle));

        if(speed > maxSpeed) {

            speed = maxSpeed;

        }

        drive.bananaTank(speed, -speed);

    }

}