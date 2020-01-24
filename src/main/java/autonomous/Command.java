package autonomous;

import utils.Constants;

public class Command {

    public enum CommandType {
        MOVE, ROTATE;
    }

    private CommandType type;
    
    // Inches if moving, percent of a full revolution if rotating
    private double value;    

    // decimal percent of the max motor speed
    private double speed;

    public Command(CommandType type, double value, double speed) {

        this.type = type;
        this.value = (type == CommandType.ROTATE) ?
            // converts inches of linear distance to rotations of the drive wheels
            (((value) * Constants.ROBOT_WHEEL_CIRCLE_CIRCUMFRENCE) / Constants.DRIVE_WHEEL_CIRCUMFRENCE) / Constants.DRIVE_GEAR_RATIO : 
            // converts percent rotations of the robot to percent rotations of the drive wheel
            ((value) / Constants.DRIVE_WHEEL_CIRCUMFRENCE) / Constants.DRIVE_GEAR_RATIO; 

        this.speed = speed;

    }

    public CommandType getType() {

        return type;

    }
    
    public double getValue() {

        return value;

    }

    public double getSpeed() {

        return speed;

    }

}