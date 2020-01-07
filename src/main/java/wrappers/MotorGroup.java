package wrappers;

import interfaces.*;

public class MotorGroup {

    Motor[] motors;

    public MotorGroup(Motor... motors) {

        this.motors = motors;

    }

    public void setSpeed(double speed) {

        for(int j = 0; j < motors.length; j++) {
            motors[j].setSpeed(speed);
        }

    }

}