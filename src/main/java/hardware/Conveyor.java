package hardware;

import edu.wpi.first.wpilibj.Solenoid;
import interfaces.*;


public class Conveyor {

    Motor rightFeeder;
    Motor leftFeeder;
    Motor conveyor;

    Solenoid ballBlocker;

    private final double feederSpeed = 0.5;
    private final double conveyorSpeed = 0.25;

    public Conveyor(Motor rightFeeder, Motor leftFeeder, Motor conveyor, Solenoid ballBlocker) {

        this.rightFeeder = rightFeeder;
        this.leftFeeder = leftFeeder;
        this.conveyor = conveyor;

        this.ballBlocker = ballBlocker;

    }

    public void setConveyor(boolean up) {

        if (up) {

            conveyor.setSpeed(conveyorSpeed);

        } else {

            conveyor.setSpeed(-conveyorSpeed);

        }

    }

    public void stopConveyor() {
        
        conveyor.setSpeed(0);

    }

    public void setFeeding(boolean feeding) {

        if (feeding) {

            leftFeeder.setSpeed(feederSpeed);
            rightFeeder.setSpeed(-feederSpeed);

        } else {

            leftFeeder.setSpeed(0);
            rightFeeder.setSpeed(0);

        }

    }
        
}