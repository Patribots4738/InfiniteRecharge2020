package utils;

public class Nonstants {

    // this class is for storing non-constant values (nonstants), that need to be the
    // same everywhere and be accessed in multiple places, but cant just be sent between them

    // this is wether the robot has shifted from the 4.4 gear ratio to the 12.86 gear ratio
    static boolean shifted;

    // place default values here
    public static void init() {

        shifted = true;

    }

    public static void setShifted(boolean inputShifted) {

        shifted = inputShifted;

    }

    public static boolean getShifted() {

        return shifted;

    }

}