package utils;

public final class Constants {

    // Numeric values for colors are in the RGB color system
    private static final double[][] COLOR_AVERAGES = {

        {144.05, 255.0, 76.2}, // Yellow
        {249.76, 204.99, 92.36}, // Red
        {80.62, 255.0, 120.94}, // Green
        {75.03, 242.8, 253.3} // Blue
        
    };

    // Numeric values for colors are in the RGB color system
    private static final double[][] COLOR_STANDARD_DEVIATIONS = {

        {3.66, 2.0, 19.54}, // Yellow
        {17.29, 27.46, 21.65}, // Red
        {5.37, 2.0, 3.41}, // Green
        {7.86, 8.04, 5.24} // Blue

    };

    // the amount of clicks in a rotation in a Talon SRX encoder
    public static final int TALON_CLICKS = 4096;

    public static final double DRIVE_WHEEL_DIAMETER = 3.98;

    public static final double DRIVE_WHEEL_CIRCUMFRENCE = Math.PI * DRIVE_WHEEL_DIAMETER;

    public static final double DRIVE_GEAR_RATIO = 7.441;

    // this distance between the centers of the wheels on opposite sides of the robot, in inches
    public static final double ROBOT_WHEEL_SPACING = 25.75;

    // the circumfrence of the circle formed by rotating the robot a full rotation in place
    public static final double ROBOT_WHEEL_CIRCLE_CIRCUMFRENCE = Math.PI * ROBOT_WHEEL_SPACING;

    public static final String[] COLORS = {"Yellow","Red","Green","Blue"};

    public enum Color{

        Yellow(COLOR_AVERAGES[0], COLOR_STANDARD_DEVIATIONS[0]), 
        Red(COLOR_AVERAGES[1], COLOR_STANDARD_DEVIATIONS[1]), 
        Green(COLOR_AVERAGES[2], COLOR_STANDARD_DEVIATIONS[2]), 
        Blue(COLOR_AVERAGES[3], COLOR_STANDARD_DEVIATIONS[3]);

        public final double RMAX;
        public final double RMIN;

        public final double GMAX;
        public final double GMIN;

        public final double BMAX;
        public final double BMIN;
    
        Color(double averages[], double[] standardDeviations){
    
            RMAX = averages[0] + (2 * standardDeviations[0]);
            RMIN = averages[0] - (2 * standardDeviations[0]);

            GMAX = averages[1] + (2 * standardDeviations[1]);
            GMIN = averages[1] - (2 * standardDeviations[1]);

            BMAX = averages[2] + (2 * standardDeviations[2]);
            BMIN = averages[2] - (2 * standardDeviations[2]);
    
        }
    
    } 

}