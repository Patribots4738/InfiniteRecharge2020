package autonomous;

import utils.Constants;
import wrappers.PIDMotorGroup;

public class Command {

    public enum CommandType {
        MOVE, ROTATE;
    }

    private CommandType type;
    
    // Inches if moving, percent of a full revolution if rotating
    private double value;    
    // decimal percent of the max motor speed
    private double speed;

    private boolean completed;

    private boolean initiated;


    private PIDMotorGroup leftMotors;
    private PIDMotorGroup rightMotors;

    public Command(CommandType type, double value, double speed, PIDMotorGroup leftMotors, PIDMotorGroup rightMotors) {

        this.type = type;
        this.value = value;
        this.speed = speed;

        this.completed = false;
        this.initiated = false;

        this.leftMotors = leftMotors;
        this.rightMotors = rightMotors;

    }

    private void init() {

        initiated = true;

    }

    public boolean execute() {

        if (!initiated) {

            init();

        } else {

            if (type == CommandType.MOVE) {

                leftMotors.setPosition((value / Constants.DRIVE_WHEEL_CIRCUMFRENCE) + leftMotors.getPosition(), speed);
                rightMotors.setPosition((-value / Constants.DRIVE_WHEEL_CIRCUMFRENCE) + rightMotors.getPosition(), -speed);

            } else if (type == CommandType.ROTATE) {

                // converts percent rotations of the robot to percent rotations of the drive wheels
                double conversion = (value * Constants.ROBOT_WHEEL_CIRCLE_CIRCUMFRENCE) / Constants.DRIVE_WHEEL_CIRCUMFRENCE;

                leftMotors.setPosition(leftMotors.getPosition() + conversion, speed);
                rightMotors.setPosition(rightMotors.getPosition() + conversion, speed);

            }

        }

    }

}