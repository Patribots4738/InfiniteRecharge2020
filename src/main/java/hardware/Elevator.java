package hardware;

import interfaces.PIDMotor;
import wrappers.*;

public class Elevator {

    PIDMotor rightLifter;
    PIDMotor leftLifter;

    Limitswitch bottomSwitch;
    Limitswitch topSwitch;

    DoubleSolenoid lock;

    public Elevator(PIDMotor leftLifter, PIDMotor rightLifter, DoubleSolenoid lock, Limitswitch bottomSwitch, Limitswitch topSwitch) {
        
        this.rightLifter = rightLifter;
        this.leftLifter = leftLifter;

        this.lock = lock;

        this.bottomSwitch = bottomSwitch;
        this.topSwitch = topSwitch;

        rightLifter.setSpeed(0.0);
        leftLifter.setSpeed(0.0);
        
    }

    public void setElevator(double speed) {

        // these two if statements are to prevent stupidity
        if(!topSwitch.getState() && speed > 0) {

            speed = 0;

        }

        if(!bottomSwitch.getState() && speed < 0) {

            speed = 0;

        }

        rightLifter.setPercent(speed);
        leftLifter.setPercent(-speed);

    }

    public void setLock(boolean isLocked) {

        lock.activateChannel(isLocked);

    }

}