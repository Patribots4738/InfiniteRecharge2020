package hardware;

import interfaces.PIDMotor;
import wrappers.*;

public class Elevator {

    PIDMotor rightLifter;
    PIDMotor leftLifter;

    DoubleSolenoid lock;

    public Elevator(PIDMotor leftLifter, PIDMotor rightLifter, DoubleSolenoid lock) {
        
        this.rightLifter = rightLifter;
        this.leftLifter = leftLifter;

        this.lock = lock;

        rightLifter.setSpeed(0.0);
        leftLifter.setSpeed(0.0);
        
    }

    public void setElevator(double speed) {

        rightLifter.setSpeed(speed);
        leftLifter.setSpeed(-speed);

    }

    public void setLock(boolean isLocked) {

        lock.activateChannel(isLocked);

    }

}