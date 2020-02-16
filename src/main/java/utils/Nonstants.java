package utils;

public class Nonstants {

    // this class is for storing non-constant values (nonstants), that need to be the
    // same everywhere and be accessed in multiple places, but cant just be sent between them

    // this is wether the robot has shifted from the 12.86 gear ratio to the 4.4 gear ratio
    static boolean shifted;

    // if the robot is lined up with the vision target
    static boolean aligned;

    // if the shooter's wheels are spun to the correct speed
    static boolean readyToFire;

    // place default values here
    public static void init() {

        shifted = true;

        aligned = false;

        readyToFire = false;

    }

    public static void setShifted(boolean isShifted) {

        shifted = isShifted;

    }

    public static void setAligned(boolean isAligned) {

        aligned = isAligned;

    } 

    public static void setReadyToFire(boolean isReadyToFire) {

        readyToFire = isReadyToFire; 

    }

    public static boolean getShifted() {

        return shifted;

    }

    public static boolean getAligned() {

        return aligned;

    }

    public static boolean getIsReadyToFire() {

        return readyToFire;

    }

}