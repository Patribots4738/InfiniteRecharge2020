package wrappers;

import interfaces.*;

public class PIDMotorGroup extends MotorGroup {

    PIDMotor[] PIDMotors;

    public PIDMotorGroup(PIDMotor... motors) {
        super(motors);
        this.PIDMotors = motors;
    }

    public void setP(double p) {

        for(int j = 0; j < motors.length; j++) {
            PIDMotors[j].setP(p);
        }

    }

    public void setI(double i) {

        for(int j = 0; j < motors.length; j++) {
            PIDMotors[j].setI(i);
        }

    }

    public void setD(double d) {

        for(int j = 0; j < motors.length; j++) {
            PIDMotors[j].setD(d);
        }

    }    

    public void setPosition(double rotations, double minSpeed, double maxSpeed) {

        for(int j = 0; j < motors.length; j++) {

            PIDMotors[j].setPosition(rotations, minSpeed, maxSpeed);

        }
        
    }

    public double getPosition() {

        double positionSum = 0.0;

        for(int j = 0; j < motors.length; j++) {

            positionSum += PIDMotors[j].getPosition();

        }

        return positionSum / motors.length;

    }

    public double getSpeed() {

        double speedSum = 0.0;

        for(int j = 0; j < motors.length; j++) {

            speedSum += PIDMotors[j].getSpeed();

        }

        return speedSum / motors.length;

    }

    public void setSpeedVariance(double speedVariance) {

        for(int j = 0; j < motors.length; j++) {

            PIDMotors[j].setSpeedVariance(speedVariance);

        }
        
    }

    public void resetEncoder() {

        for(int j = 0; j < motors.length; j++) {

            PIDMotors[j].resetEncoder();

        }

    }

}