package autonomous;

import utils.Constants;
import wrappers.PIDMotorGroup;

public class Command {

    public enum CommandType {
        MOVE, ROTATE;
    }
    // index of zero represents the final value the left motor should arrive at once the command is finished
    // index of one represents the same thing for the right motor
    double[] finalValues = new double[2];
    // either MOVE or ROTATE. These determine the command the robot will try to do.
    private CommandType type;
    // inches if command type is MOVE, percent of a full revolution if command type is ROTATE
    private double value;    
    // decimal percent of the max motor speed
    private double speed;
    // value determines if the init method has been called before. True means that it has
    private boolean initiated;

    private PIDMotorGroup leftMotors;
    private PIDMotorGroup rightMotors;

    public Command(CommandType type, double value, double speed, PIDMotorGroup leftMotors, PIDMotorGroup rightMotors) {

        this.type = type;
        this.value = value;
        this.speed = speed;
        // value starts as false, meaning that init hasn't been called 
        this.initiated = false;
        this.leftMotors = leftMotors;
        this.rightMotors = rightMotors;

    }

    private void init() {

        initiated = true;

        if (type == CommandType.MOVE) {

            finalValues[0] = (value / Constants.DRIVE_WHEEL_CIRCUMFRENCE) + leftMotors.getPosition();
            finalValues[1] = (-value / Constants.DRIVE_WHEEL_CIRCUMFRENCE) + rightMotors.getPosition();

            leftMotors.setPosition(finalValues[0], speed);
            rightMotors.setPosition(finalValues[1], -speed);
    
        } else if (type == CommandType.ROTATE) {

            double conversion = (value * Constants.ROBOT_WHEEL_CIRCLE_CIRCUMFRENCE) / Constants.DRIVE_WHEEL_CIRCUMFRENCE;

            finalValues[0] = leftMotors.getPosition() + conversion;
            finalValues[1] = rightMotors.getPosition() + conversion;

            leftMotors.setPosition(finalValues[0], speed);
            rightMotors.setPosition(finalValues[1], speed);

        }

    }
    
    // Starts running the command and returns true when the command is done. It will return false if the command isn't complete
    public boolean execute() {

        if (!initiated) {

            init();

        }
        
        if (leftMotors.getPosition() >= finalValues[0] && rightMotors.getPosition() >= finalValues[1]) {

            return true;

        } else {

            return false;

        }

    }

}