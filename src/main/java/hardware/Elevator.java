package hardware;

import wrappers.*;

public class Elevator {

    Falcon rightLifter;
    Falcon leftLifter;

    SingleSolenoid lock;

    public Elevator(Falcon rightLifter, Falcon leftLifter, SingleSolenoid lock) {
        
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

        lock.set(isLocked);

    }

}