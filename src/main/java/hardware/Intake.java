package hardware;

import interfaces.*;

public class Intake {

    PIDMotor angleController;
    Motor sucker;

    private final double gearRatio = 24.0 / 16.0;
    
    private final double suckSpeed = 0.35;

    private double[] PID = {1, 0, 0};

    public Intake(Motor sucker, PIDMotor angleController) {

        this.sucker = sucker;
        this.angleController = angleController;

        this.angleController.resetEncoder();
        this.angleController.setPID(PID[0], PID[1], PID[2]);

    }

    public void setSuck(boolean sucking) {

        if (sucking) {

            sucker.setSpeed(suckSpeed);

        } else {

            sucker.setSpeed(0);

        }

    }

    public void setDown(boolean down) {

        if (down) {

            angleController.setPosition(0, -0.5, 0.5);

        } else {

            angleController.setPosition(0.25 * gearRatio, -0.5, 0.5);

        }

    }

}