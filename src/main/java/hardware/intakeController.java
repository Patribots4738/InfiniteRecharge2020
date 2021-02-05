package hardware;

import interfaces.*;
import wrappers.*;
import utils.*;

public class IntakeController {

    Conveyor conveyor;
    Intake intake;

    public IntakeController(Conveyor conveyor, Intake intake) {

        this.conveyor = conveyor;
        this.intake = intake;

    }

    public void setSuck(double suckRate) {

        intake.setSuck(suckRate);

    }

}