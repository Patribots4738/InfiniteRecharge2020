package wrappers;

import networking.*;
import utils.*;

public class Limelight {

    // the different modes the LEDs can operate in
    public enum LEDMode {
        DEFAULT, OFF, BLINK, ON;
    }

    // the different modes the camera can operate in
    public enum CAMMode {
        VISION, CAMERA_STREAM;
    }

    NTTable limelightTable;
    
    MovingAverage horizAngleAvg;
    MovingAverage vertAngleAvg;

    public Limelight(NTTable limelightTable) {

        this.limelightTable = limelightTable;

        setLED(LEDMode.DEFAULT);

        setCamera(CAMMode.VISION);

        // creates average value samples to pad the values from changing too drastically.
        horizAngleAvg = new MovingAverage(10);
        vertAngleAvg = new MovingAverage(10);

    }

    // sets the camera mode from the enums at the top of the file
    public void setCamera(CAMMode mode) {

        limelightTable.set("camMode", mode.ordinal());

    }

    // sets the led mode from the enums at the top of the file
    public void setLED(LEDMode mode) {

        limelightTable.set("ledMode", mode.ordinal());

    }

    // returns the NetworkTables instance 
    public NTTable getNetworkTable() {

        return limelightTable;

    }

    public boolean targetFound() {

        // whether the limelight has any valid targets
        int found = Integer.valueOf((String)limelightTable.get("tv"));

        // returns if the value is true or false
        return (found == 1) ? true : false;

    }

    // returns how much of the image the camera can actually see
    public double targetAreaPercent() {

        return Double.valueOf((Double)limelightTable.get("ta"));

    }

    // returns the horizontal offset from the crosshair
    public double getHorizontalAngle() {

        double angle = Double.valueOf((Double)limelightTable.get("tx"));

        horizAngleAvg.addValue(angle);
        
        return horizAngleAvg.getAverage();

    }
    
    // returns the vertical offset from the crosshair.
    public double getVerticalAngle() {

        double angle = Double.valueOf((Double)limelightTable.get("ty"));

        vertAngleAvg.addValue(angle);

        return vertAngleAvg.getAverage();

    }

    // returns the distance from the target to the robot in inches.
    public double getDistance() {

        double distance = (Constants.TARGET_HEIGHT - Constants.LIMELIGHT_HEIGHT)
                          / Math.tan(Constants.LIMELIGHT_MOUNTING_ANGLE + Calc.degreesToRadians(getVerticalAngle()));

        return distance;

    }

} 