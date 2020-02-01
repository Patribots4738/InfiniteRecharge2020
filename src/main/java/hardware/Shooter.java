package hardware;

import interfaces.*;

public class Shooter {

    private PIDMotor topWheel;
    private PIDMotor bottomWheel;

    public Shooter(PIDMotor topWheel, PIDMotor bottomWheel) {

        this.topWheel = topWheel;
        this.bottomWheel = bottomWheel;

    }

    private double[] distanceToSpeeds(double distance) {

        // PLACEHOLDER MATH 
        double topWheelSpeed = 0;
        double bottomWheelSpeed = 0;
        
        return new double[]{topWheelSpeed, bottomWheelSpeed};

    }

    public void setSpeeds(double distance) {

        double[] speeds = distanceToSpeeds(distance);

        topWheel.setSpeed(speeds[0]);
        bottomWheel.setSpeed(speeds[1]);

    }

    public void stop() {

        topWheel.setSpeed(0);
        bottomWheel.setSpeed(0);

    }

}