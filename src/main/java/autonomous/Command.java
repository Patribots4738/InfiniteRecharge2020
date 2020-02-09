package autonomous;

import utils.Calc;

public class Command {

    public enum CommandType {
        MOVE, ROTATE;
    }

    private CommandType type;
    
    // Input for constructor is in inches for moving, decimal percent rotations if rotating
    // however, it is internally stored in rotations of the drive motor's output shaft
    private double value;

    // decimal percent of the max motor speed
    private double speed;

    public Command(CommandType type, double value, double speed) {

        this.type = type;
        this.value = ((type == CommandType.ROTATE) ? Calc.robotRotationsToDrive(value) : Calc.inchesToDrive(value)); 
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