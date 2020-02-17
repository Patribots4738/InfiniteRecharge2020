package hardware;

import wrappers.Limelight;

public class ShooterController {

    Shooter shooter;

    Conveyor conveyor;

    Limelight limelight;

    Drive drive;

    // all of these doubles are placeholders pending testing
    private double maxSpeed = 0.4;

    private double minSpeed = 0.02 * maxSpeed;

    private double converter = 1.0 / ((Shooter.readyToFire) ? 100.0 : 500.0);

    private double acceptableAngleError = 0.5;

    public static boolean aligned = false;

    public ShooterController(Conveyor conveyor, Shooter shooter, Limelight limelight, Drive drive) {

        this.shooter = shooter;

        this.conveyor = conveyor;

        this.limelight = limelight;

        this.drive = drive;

    }

    // stops the shooter and turns off the limelight's LED
    public void stop() {

        shooter.stop();

        limelight.setLED(Limelight.LEDMode.OFF);

    }

    // this spins up the shooter and sets the conveyor and feeders based on wether the shooter is up to speed
    public void fire() {

        shooter.setShooterSpeeds(limelight.getDistance());

        conveyor.setConveyor(Shooter.readyToFire);

        shooter.setFeeders(Shooter.readyToFire);

    }

    // this aligns the robot with the vision target found by the limelight
    public void aim() {

        limelight.setLED(Limelight.LEDMode.ON);

        double angle = limelight.getHorizontalAngle();

        aligned = Math.abs(angle) <= acceptableAngleError;

        double speed = ((angle * converter) * maxSpeed) + minSpeed;

        if(speed > maxSpeed) {

            speed = maxSpeed;

        }

        drive.bananaTank(speed, speed);

    }

}