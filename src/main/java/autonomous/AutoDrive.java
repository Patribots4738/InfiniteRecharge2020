package autonomous;
import wrappers.*;

import java.util.ArrayList;

import utils.Constants;

public class AutoDrive {

    ArrayList<Command> commandQueue;

    public AutoDrive(PIDMotorGroup leftMotors, PIDMotorGroup rightMotors) {

        this.leftMotors = leftMotors;
        this.rightMotors = rightMotors;

        completePositions = new double[2];
        completePositions[0] = leftMotors.getPosition();
        completePositions[1] = rightMotors.getPosition();

        currentPositions = new double[2];
        currentPositions[0] = leftMotors.getPosition();
        currentPositions[1] = rightMotors.getPosition();

    }

    public void addCommand(Command command) {

        commandQueue.add(command);

    }

    public void insertCommand(Command command, int index) {

        commandQueue.add(index, command);

    }

    public void removeCommand(int index) {

        if(index < commandQueue.size()){

            commandQueue.remove(index);

        }

    }

    // this command will be called once to start executing a command
    public void executeCommand(Command command) {

        Command.CommandType commandType = command.getType();

        if(commandType == Command.CommandType.MOVE) {

            completePositions[0] += command.getValue();
            completePositions[1] -= command.getValue();

            leftMotors.setPosition(leftMotors.getPosition() + command.getValue(), command.getSpeed());
            rightMotors.setPosition(-(rightMotors.getPosition() + command.getValue()), -command.getSpeed());

        }

        if(commandType == Command.CommandType.ROTATE) {

            completePositions[0] += command.getValue();
            completePositions[1] += command.getValue();

            // converts percent rotations of the robot to percent rotations of the drive wheels
            double conversion = (command.getValue() * Constants.ROBOT_WHEEL_CIRCLE_CIRCUMFRENCE) / Constants.DRIVE_WHEEL_CIRCUMFRENCE;

            leftMotors.setPosition(leftMotors.getPosition() + conversion, command.getSpeed());
            rightMotors.setPosition(rightMotors.getPosition() + conversion, command.getSpeed());

        }

    }

    // this command will be called continously in robot
    public void executeQueue() {

        currentPositions[0] = leftMotors.getPosition();
        currentPositions[1] = rightMotors.getPosition();

        if((currentPositions[0] == completePositions[0]) && currentPositions[1] == completePositions[1]) {

            if(!(commandQueue.size() == 0)) {

                removeCommand(0);

            }

            if(commandQueue.size() == 0) {

                return;

            }

            executeCommand(commandQueue.get(0));

        }

    }
    
} 