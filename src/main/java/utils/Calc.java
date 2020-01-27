package utils;

public class Calc {

    // returns the y coordinate of @param x on the banana curve (graph on desmos: https://www.desmos.com/calculator/xxgjmyatz5)
    public static double bananaCurve(double x) {

        return Math.signum(x) * Math.pow(Math.abs(x), 1 + (Math.abs(x)));

    }

    public static boolean isBetween(double value, double min, double max) {

        return (value < max) && (value > min);

    }

    public static double degreesToRadians(double degrees) {

        return (degrees / 180.0) * Math.PI;

    } 

    // converts rotations of the robot to rotations of the drive motors
    public static double robotRotationsToDrive(double inches) {

        double driveMotorRotations = (((inches)
                                     * Constants.ROBOT_WHEEL_CIRCLE_CIRCUMFRENCE)
                                     / Constants.DRIVE_WHEEL_CIRCUMFRENCE)
                                     / Constants.DRIVE_GEAR_RATIO;

        return driveMotorRotations;

    }
    
    // converts linear distance to rotations of the drive motors
    public static double inchesToDrive(double robotRotations) {

        double driveMotorRotations = ((robotRotations)
                                     / Constants.DRIVE_WHEEL_CIRCUMFRENCE)
                                     / Constants.DRIVE_GEAR_RATIO;

        return driveMotorRotations;
        
    }

}