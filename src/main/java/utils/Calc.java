package utils;

public class Calc {

    // returns the y coordinate of @param x on the banana curve (graph on desmos: https://www.desmos.com/calculator/xxgjmyatz5)
    public static double bananaCurve(double x) {

        return Math.signum(x) * Math.pow(Math.abs(x), 1 + (Math.abs(x)));

    }

    public static boolean isBetween(double value, double min, double max) {

        return (value < max) && (value > min);

    }

}