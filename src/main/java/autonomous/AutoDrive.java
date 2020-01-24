package autonomous;

import wrappers.*;
import utils.Constants;

import java.util.ArrayList;

public class AutoDrive {

	PIDMotorGroup leftMotors;
	PIDMotorGroup rightMotors;

	// details how close we can get to the specified end position of a command
	// before calling it complete and moving to the next one
	private double acceptableError;

	// the positions the left and right motorgroups should
	// be at when the currently running command has been completed,
	// these values will change often during the operation of the class.
	// the order is left motor, right motor for index 0 and 1
	private double[] completePositions;

	// true if it's running commands, false if it's not
	private boolean running;

	private ArrayList<Command> commandQueue;

	public AutoDrive(PIDMotorGroup leftMotors, PIDMotorGroup rightMotors) {

		this.leftMotors = leftMotors;
		this.rightMotors = rightMotors;

		acceptableError = 1.0;

		running = false;

		completePositions = new double[]{
			leftMotors.getPosition(),
			rightMotors.getPosition()
		};

	}

	// resets the command queue and stops execution of currently running commands
	public void reset() {

		commandQueue = new ArrayList<Command>();
		completePositions[0] = 0;
		completePositions[1] = 0;
		leftMotors.resetEncoder();
		rightMotors.resetEncoder();
		running = false;

	}

	// appends an array of commands to the end of the commandQueue arraylist
	public void addCommands(Command... commands) {
		
		for (int i = 0; i < commands.length; i++) {

			commandQueue.add(commands[i]); 
			
		}

	}

	// returns a boolean detailing if the command at the supplied index could be removed
	private boolean removeCommand(int index) {

		if (index < commandQueue.size()){

			commandQueue.remove(index);

			return true;

		} else {

			return false;

		}

	}

	// this command will be called once to start executing a command
	public void executeCommand(Command command) {

		Command.CommandType commandType = command.getType();
		double value = command.getValue();
		double speed = command.getSpeed();

		// the left motor will always move in the same direction regardless of moving or rotating the robot
		completePositions[0] += value;
		
		if(commandType == Command.CommandType.MOVE) {
			
			completePositions[1] -= value;

			leftMotors.setPosition(completePositions[0], -speed, speed);
			rightMotors.setPosition(completePositions[1], -speed, speed);

		} else { // the command is a rotate command
			// sets the right motor to rotate opposite of the left
			completePositions[1] += value;

			leftMotors.setPosition(completePositions[0], -speed, speed);
			rightMotors.setPosition(completePositions[1], -speed, speed);

		}

	}
 
	// this command will be called continously in robot
	public void executeQueue() {

		// checks if there aren't any commands to save cpu cycles
		if (commandQueue.size() == 0) {

			running = false;
			return;

		} else if (!running) {

			// starts executing commands if this is being called while commands aren't executing
			executeCommand(commandQueue.get(0));
			running = true;
			return;

		}

		double leftWheelPosition = leftMotors.getPosition();
		double rightWheelPosition = rightMotors.getPosition();

		double leftError = Math.abs(completePositions[0] - leftWheelPosition);
		double rightError = Math.abs(completePositions[1] - rightWheelPosition);

		if (Constants.LOGGING) {

			System.out.println("left wheel has moved: " + leftWheelPosition);
			System.out.println("right wheel has moved: " + rightWheelPosition);
			System.out.println();
			System.out.println("leftError is: " + leftError);
			System.out.println("rightError is: " + rightError);

		}

		if(leftError <= acceptableError && rightError <= acceptableError) {

			removeCommand(0);
			executeCommand(commandQueue.get(0));

			running = true;
			return;

		}

	}

} 