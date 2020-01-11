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

    public Command(CommandType type, double value, double speed) {

        this.type = type;
        this.value = value;
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