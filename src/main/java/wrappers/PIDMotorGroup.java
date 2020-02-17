package wrappers;

import interfaces.*;

public class PIDMotorGroup extends MotorGroup implements PIDMotor {

    PIDMotor[] pidMotors;

    public PIDMotorGroup(PIDMotor... motors) {

        super(motors);
        this.pidMotors = motors;

    }

    public PIDMotor[] getpidMotors() {

        return this.pidMotors;

    }

    public void setP(double p) {

        for (PIDMotor motor : pidMotors) {

            motor.setP(p);

        }

    }

    public void setI(double i) {

        for (PIDMotor motor : pidMotors) {

            motor.setI(i);

        }

    }

    public void setD(double d) {

        for (PIDMotor motor : pidMotors) {

            motor.setD(d);

        }

    }    

    public void setFF(double ff) {

        for (PIDMotor motor : pidMotors) {

            motor.setFF(ff);

        }

    }

    public void setPID(double p, double i, double d) {

        for (PIDMotor motor : pidMotors) {

            motor.setPID(p, i, d);

        }

    }

    public void setPosition(double rotations, double minSpeed, double maxSpeed) {

        for (PIDMotor motor : pidMotors) {

            motor.setPosition(rotations, minSpeed, maxSpeed);

        }
        
    }

    public double getPosition() {

        double positionSum = 0.0;

        for (PIDMotor motor : pidMotors) {

            positionSum += motor.getPosition();

        }

        return positionSum / motors.length;

    }

    public double getSpeed() {

        double speedSum = 0.0;

        for (PIDMotor motor : pidMotors) {

            speedSum += motor.getSpeed();

        }

        return speedSum / motors.length;

    }

    public void resetEncoder() {

        for (PIDMotor motor : pidMotors) {

            motor.resetEncoder();

        }

    }

}