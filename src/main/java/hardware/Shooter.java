package hardware;

import wrappers.*;
import interfaces.*;
import utils.*;

public class Shooter {

    PIDMotor topWheel;
    PIDMotor bottomWheel;

    MotorGroup feeders;    

    DoubleSolenoid blocker;

    public static boolean readyToFire = false;

    private double feedRate = -0.5;

    // in decimal percent
    private double acceptableSpeedError = 0.05;

    public Shooter(PIDMotor topWheel, PIDMotor bottomWheel, MotorGroup feeders, DoubleSolenoid blocker) {

        this.topWheel = topWheel;
        this.bottomWheel = bottomWheel;

        this.feeders = feeders;

        this.blocker = blocker;

    }

    private double[] distanceToSpeeds(double distance) {

        // PLACEHOLDER MATH 
        double topWheelSpeed = 0;
        double bottomWheelSpeed = 0;
        
        return new double[]{topWheelSpeed, bottomWheelSpeed};

    }

    // sets the shooter speeds based on distance
    public void setShooterSpeeds(double distance) {

        double[] speeds = distanceToSpeeds(distance);

        topWheel.setSpeed(speeds[0]);
        bottomWheel.setSpeed(speeds[1]);

        readyToFire = Calc.isBetween(topWheel.getSpeed(), speeds[0] * (1.0 - acceptableSpeedError), speeds[0] * (1 + acceptableSpeedError)) &&
                     (Calc.isBetween(bottomWheel.getSpeed(), speeds[1] * (1.0 - acceptableSpeedError), speeds[1] * (1 + acceptableSpeedError)));
        
    }

    public void setRawSpeeds(double topSpeed, double bottomSpeed) {

        topWheel.setSpeed(topSpeed);
        bottomWheel.setSpeed(-bottomSpeed);

    }

    public void setBlocker(boolean on) {

        blocker.activateChannel(on);

    }

    public void stop() {
        
        topWheel.setSpeed(0);
        bottomWheel.setSpeed(0);

        feeders.setSpeed(0);

        blocker.activateChannel(false);

        readyToFire = false;

    }

    public void setFeeders(boolean on) {
        
        blocker.activateChannel(on);

        feeders.setSpeed((on) ? feedRate : 0);

    }

}