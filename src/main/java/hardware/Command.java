package hardware;

import wrappers.*;

public class Command {

    public enum CommandType {
        MOVE, ROTATE;
    }

    private CommandType type;
    
    // Inches if moving, percent of a full revolution if rotating
    private double value; 

    private boolean completed;

    private boolean initiated;

    public Command(CommandType type, double value) {

        this.type = type;
        this.value = value;

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



    }



}