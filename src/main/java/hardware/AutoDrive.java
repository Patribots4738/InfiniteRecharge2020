package hardware;
import interfaces.*;
import wrappers.*;

import java.util.ArrayList;

public class AutoDrive {

    PIDMotorGroup leftMotors;
    PIDMotorGroup rightMotors;

    ArrayList<Command> commandQueue;

    public AutoDrive(PIDMotorGroup leftMotors, PIDMotorGroup rightMotors) {

        this.leftMotors = leftMotors;
        this.rightMotors = rightMotors;

    }

    public void execute() {

        Command currentCommand = commandQueue.get(0);

        if (currentCommand.isDone()) {
            
        }

    }
    
}