package wrappers;

public class Timer {

    static edu.wpi.first.wpilibj.Timer Timer;

    public static void init() {

        Timer = new edu.wpi.first.wpilibj.Timer();

        Timer.start();

    }

    // returns current time since robot code began, in seconds;
    public static double getTime() {

        return Timer.get();

    }

}