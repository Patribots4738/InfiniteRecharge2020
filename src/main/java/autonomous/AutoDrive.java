package autonomous;

import wrappers.*;
import utils.Constants;

import java.util.ArrayList;

public class AutoDrive {

    PIDMotorGroup leftMotors;
    PIDMotorGroup rightMotors;

    // the positions the left and right motorgroups should
    // be at when the currently running command has been
    // completed, these values will change often during the operation of the class
    // order is left, right
    double[] completePositions;

    ArrayList<Command> commandQueue;

    public AutoDrive(PIDMotorGroup leftMotors, PIDMotorGroup rightMotors) {

        this.leftMotors = leftMotors;
        this.rightMotors = rightMotors;

        completePositions = new double[2];
        completePositions[0] = leftMotors.getPosition();
        completePositions[1] = rightMotors.getPosition();

    }

    public void addCommand(Command command) {

        commandQueue.add(command);

    }

    // for internal use only
    private void removeCommand(int index) {

        if(index < commandQueue.size()){

            commandQueue.remove(index);

        }

    }

    // this command will be called once to start executing a command
    public void executeCommand(Command command) {

        Command.CommandType commandType = command.getType();

        if(commandType == Command.CommandType.MOVE) {

            // converts inches of linear distance to rotations of the drive wheels
            double convertedValue = command.getValue() / Constants.DRIVE_WHEEL_CIRCUMFRENCE;

            completePositions[0] += convertedValue;
            completePositions[1] -= convertedValue;

            leftMotors.setPosition(completePositions[0], command.getSpeed());
            rightMotors.setPosition(completePositions[1], -command.getSpeed());

        }

        if(commandType == Command.CommandType.ROTATE) {

            // converts percent rotations of the robot to percent rotations of the drive wheels
            double convertedValue = (command.getValue() * Constants.ROBOT_WHEEL_CIRCLE_CIRCUMFRENCE) / Constants.DRIVE_WHEEL_CIRCUMFRENCE;

            completePositions[0] += convertedValue;
            completePositions[1] += convertedValue;

            leftMotors.setPosition(completePositions[0], command.getSpeed());
            rightMotors.setPosition(completePositions[1], command.getSpeed());

        }

    }

    // this command will be called continously in robot
    public void executeQueue() {

        if((leftMotors.getPosition() == completePositions[0]) && (rightMotors.getPosition() == completePositions[1])) {

            if(!(commandQueue.size() == 0)) {

                removeCommand(0);

            } else {

                return;

            }

            executeCommand(commandQueue.get(0));

        }

    }
    
} 