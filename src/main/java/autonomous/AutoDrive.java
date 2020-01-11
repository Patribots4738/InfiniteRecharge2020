package autonomous;

import wrappers.*;
import utils.Constants;

import java.util.ArrayList;

public class AutoDrive {

    ArrayList<Command> commandQueue;

    private PIDMotorGroup leftMotors;
    private PIDMotorGroup rightMotors; 

    public AutoDrive(PIDMotorGroup leftMotors, PIDMotorGroup rightMotors, Command... commands) {

        this.leftMotors = leftMotors;
        this.rightMotors = rightMotors;

        for (int i = 0; i < commands.length; i++) {

            commandQueue.add(commands[i]);

        }

    }

    public void create(Command.CommandType type, double value, double speed) {

        commandQueue.add(
            new Command(type, value, speed, leftMotors, rightMotors)
        );

    }

    // this command will be called continously in robot
    public boolean executeQueue() {

        if (commandQueue.size() == 0) {
            // there are no more commands in the queue
            return false;

        }
        // runs the command and stores whether it is done or not
        boolean done = commandQueue.get(0).execute();

        if (done) {

            commandQueue.remove(0);

        }
        // there is still commands in the queue
        return true;

    }

    public int queueLength() {

        return commandQueue.size();

    }
    
} 