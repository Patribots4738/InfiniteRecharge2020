package utils;

import frc.robot.Robot;

public class Calc {

    // returns the y coordinate of @param x on the banana curve (graph on desmos: https://www.desmos.com/calculator/xxgjmyatz5)
    // input and output should be within -1 and 1 (inclusive)
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
    public static double robotRotationsToDrive(double robotRotations) {

        return inchesToDrive((robotRotations) * Constants.ROBOT_WHEEL_CIRCLE_CIRCUMFRENCE);

    }
    
    // converts linear distance to rotations of the drive motors
    public static double inchesToDrive(double inches) {

        return ((inches) / Constants.DRIVE_WHEEL_CIRCUMFRENCE) / ((Robot.shifted) ? (Constants.DRIVE_GEAR_RATIO2) : (Constants.DRIVE_GEAR_RATIO));
        
    }

    // caps a value between the two specified inputs and outputs
    public static double cap(double value, double min, double max) {

        if(value > max) {

            return max;

        }

        if(value < min) {

            return min;

        }

        return value;

    }

}