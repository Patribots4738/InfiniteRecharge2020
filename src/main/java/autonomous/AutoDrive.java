package autonomous;

import wrappers.*;
import utils.Constants;

import java.util.ArrayList;

import autonomous.Command.CommandType;

public class AutoDrive {

    PIDMotorGroup leftMotors;
    PIDMotorGroup rightMotors;

    double acceptableError;

    // the positions the left and right motorgroups should
    // be at when the currently running command has been
    // completed, these values will change often during the operation of the class
    // order is left, right
    double[] completePositions;

    boolean isFirstCommand;

    ArrayList<Command> commandQueue;

    public AutoDrive(PIDMotorGroup leftMotors, PIDMotorGroup rightMotors) {

        this.leftMotors = leftMotors;
        this.rightMotors = rightMotors;

        commandQueue = new ArrayList<Command>();
        // this adds a filler command to be removed at 
        // the beginning when the robot has yet to be moved
        addCommand(new Command(CommandType.MOVE, 0, 0));

        acceptableError = 1.0;

        completePositions = new double[2];
        completePositions[0] = 0.0;
        completePositions[1] = 0.0;

        isFirstCommand = true;

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

    public void reset() {

        commandQueue = new ArrayList<Command>();
        addCommand(new Command(CommandType.MOVE, 0, 0));

        completePositions[0] = 0;
        completePositions[1] = 0;

        leftMotors.resetEncoder();
        rightMotors.resetEncoder();

    }

    // this command will be called once to start executing a command
    public void executeCommand(Command command) {

        Command.CommandType commandType = command.getType();

        if(commandType == Command.CommandType.MOVE) {

            // converts inches of linear distance to rotations of the drive wheels
            double convertedValue = (command.getValue() / Constants.DRIVE_WHEEL_CIRCUMFRENCE) / Constants.DRIVE_GEAR_RATIO * 0.666;

            completePositions[0] += convertedValue;
            completePositions[1] -= convertedValue;

            leftMotors.setPosition(completePositions[0], -command.getSpeed(), command.getSpeed());
            rightMotors.setPosition(completePositions[1], -command.getSpeed(), command.getSpeed());

        }

        if(commandType == Command.CommandType.ROTATE) {

            // converts percent rotations of the robot to percent rotations of the drive wheels
            double convertedValue = ((command.getValue() * Constants.ROBOT_WHEEL_CIRCLE_CIRCUMFRENCE) / Constants.DRIVE_WHEEL_CIRCUMFRENCE) / Constants.DRIVE_GEAR_RATIO;

            completePositions[0] += convertedValue;
            completePositions[1] += convertedValue;

            leftMotors.setPosition(completePositions[0], -command.getSpeed(), command.getSpeed());
            rightMotors.setPosition(completePositions[1], -command.getSpeed(), command.getSpeed());

        }

    }

    // this command will be called continously in robot
    public void executeQueue() {

        double leftWheelPosition = leftMotors.getPosition()  / Constants.DRIVE_GEAR_RATIO;
        double rightWheelPosition = rightMotors.getPosition() / Constants.DRIVE_GEAR_RATIO;

        System.out.println("left wheel has moved: " + leftWheelPosition);
        System.out.println("right wheel has moved: " + rightWheelPosition);

        double leftError = Math.abs(completePositions[0] - leftWheelPosition);
        double rightError = Math.abs(completePositions[1] - rightWheelPosition);

        System.out.println("leftError is: " + leftError);
        System.out.println("rightError is: " + rightError);

        if(leftError <= acceptableError && rightError <= acceptableError) {

            if(!(commandQueue.size() == 0)) {

                System.out.println("removed completed command");

                removeCommand(0);

            } 

            if(commandQueue.size() == 0) {

                System.out.println("queue is empty");

                return;

            }

            executeCommand(commandQueue.get(0));
            System.out.println("Running command");

        }

    }
    
} 