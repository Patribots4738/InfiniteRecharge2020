package hardware;

import interfaces.*;
import utils.*;

public class Conveyor {

    Motor conveyor;

    double runTime;
    double maxTime = 0.2;

    private double conveyorSpeed = 0.5;

    public Conveyor(Motor conveyor) {

        this.conveyor = conveyor;

        runTime = 0;

    }

    // set conveyor based on direct input
    public void setSpeed(double speed) {

        conveyor.setSpeed(speed);

    }
    
    // set conveyor to preset speed based on boolean
    public void setConveyor(boolean on) {

        double speed = (on) ? conveyorSpeed : 0;

        conveyor.setSpeed(speed);

    }

    public boolean increment() {

        runTime += Constants.LOOP_TIME;

        return runTime < maxTime;

    }

    public void resetTime() {

        runTime = 0;

    }
        
}