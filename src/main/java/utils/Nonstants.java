package utils;

public class Nonstants {

    // this class is for storing non-constant values (nonstants), that need to be the
    // same everywhere and be accessed in multiple places, but cant just be sent between them   

    // this is wether the robot has shifted from the 12.86 gear ratio to the 4.4 gear ratio
    public static boolean shifted;

    // if the robot is lined up with the vision target
    public static boolean aligned;

    // if the shooter's wheels are spun to the correct speed
    public static boolean readyToFire;

    // place default values here
    public static void init() {

        shifted = true;

        aligned = false;

        readyToFire = false;

    }

}