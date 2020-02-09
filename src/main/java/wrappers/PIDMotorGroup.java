package wrappers;

import interfaces.*;

public class PIDMotorGroup extends MotorGroup implements PIDMotor {

    PIDMotor[] PIDMotors;

    public PIDMotorGroup(PIDMotor... motors) {

        super(motors);
        this.PIDMotors = motors;

    }

    public PIDMotor[] getPidMotors() {

        return this.PIDMotors;

    }

    public void setP(double p) {

        for(int i = 0; i < motors.length; i++) {

            PIDMotors[i].setP(p);

        }

    }

    public void setI(double i) {

        for(int j = 0; j < motors.length; j++) {

            PIDMotors[j].setI(i);

        }

    }

    public void setD(double d) {

        for(int i = 0; i < motors.length; i++) {

            PIDMotors[i].setD(d);

        }

    }    

    public void setPID(double p, double i, double d) {

        for(int j = 0; j < motors.length; j++) {

            PIDMotors[j].setPID(p, i, d);

        }

    }

    public void setPosition(double rotations, double minSpeed, double maxSpeed) {

        for(int i = 0; i < motors.length; i++) {

            PIDMotors[i].setPosition(rotations, minSpeed, maxSpeed);

        }
        
    }

    public double getPosition() {

        double positionSum = 0.0;

        for(int i = 0; i < motors.length; i++) {

            positionSum += PIDMotors[i].getPosition();

        }

        return positionSum / motors.length;

    }

    public double getSpeed() {

        double speedSum = 0.0;

        for(int i = 0; i < motors.length; i++) {

            speedSum += PIDMotors[i].getSpeed();

        }

        return speedSum / motors.length;

    }

    public void resetEncoder() {

        for(int i = 0; i < motors.length; i++) {

            PIDMotors[i].resetEncoder();

        }

    }

}