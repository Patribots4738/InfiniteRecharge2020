package hardware;

import networktables.*;
import utils.*;

public class Limelight {

    public enum LEDMode {

        DEFAULT, OFF, BLINK, ON;

    }

    public enum CAMMode {

        VISION, CAMERA_STREAM;

    }

    NTTable limelightTable;

    MovingAverage distanceAvg;
    MovingAverage horizAngleAvg;
    MovingAverage vertAngleAvg;

    public Limelight(NTTable limelightTable) {

        this.limelightTable = limelightTable;

        setLED(LEDMode.DEFAULT);

        setCamera(CAMMode.VISION);

        distanceAvg = new MovingAverage(10);
        horizAngleAvg = new MovingAverage(10);
        vertAngleAvg = new MovingAverage(10);

    }

    public void setCamera(CAMMode mode) {

        limelightTable.setDouble("camMode", mode.ordinal());

    }

    public void setLED(LEDMode mode) {

        limelightTable.setDouble("ledMode", mode.ordinal());

    }

    public NTTable getNetworkTable() {

        return limelightTable;

    }

    public boolean targetFound() {

        double found = limelightTable.getDouble("tv");

        return (found == 1) ? true : false;

    }

    public double targetAreaPercent() {

        return limelightTable.getDouble("ta");

    }

    public double getHorizontalAngle() {

        double angle = limelightTable.getDouble("tx");

        horizAngleAvg.addValue(angle);
        
        return horizAngleAvg.getAverage();

    }

    public double getVerticalAngle() {

        double angle = limelightTable.getDouble("ty");

        vertAngleAvg.addValue(angle);

        return vertAngleAvg.getAverage();

    }

    public double getDistance() {

        double distance = (Constants.TARGET_HEIGHT - Constants.LIMELIGHT_HEIGHT)
                          / Math.tan(Calc.degreesToRadians(Constants.LIMELIGHT_MOUNTING_ANGLE) + Calc.degreesToRadians(getVerticalAngle()));

        distanceAvg.addValue(distance);

        return distanceAvg.getAverage();

    }

} 