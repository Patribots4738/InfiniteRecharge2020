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

}