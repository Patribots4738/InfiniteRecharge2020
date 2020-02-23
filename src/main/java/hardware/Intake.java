package hardware;

import interfaces.*;

public class Intake {

    PIDMotor angleController;
    
    Motor sucker;

    private final double gearRatio = 24.0 / 16.0;

    public Intake(Motor sucker, PIDMotor angleController) {

        this.sucker = sucker;
        this.angleController = angleController;

        this.angleController.resetEncoder();
        this.angleController.setPID(0, 0, 0);

    }

    public void setSuck(double suckRate) {

        sucker.setSpeed(suckRate);

    }

    public void setDown(boolean down) {

        if (down) {

            angleController.setPosition(0.25 * gearRatio, -0.5, 0.5);

        } else {

            angleController.setPosition(0, -0.5, 0.5);

        }

    }

}