package autonomous;

public class Command {

    public enum CommandType {
        MOVE, ROTATE;
    }

    private CommandType type;
    
    // Inches if moving, percent of a full revolution if rotating
    private double value; 

    private boolean completed;

    private boolean initiated;

    private double speed;

    public Command(CommandType type, double value, double speed) {

        this.type = type;
        this.value = value;
        this.speed = speed;

        this.completed = false;
        this.initiated = false;

    }

    public void init() {

        if (initiated) {

            return;

        }

        initiated = true;

    }

    public boolean isDone() {

        return completed;

    }

    public void finish() {

        if(completed){

            return;

        }

        completed = true;

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