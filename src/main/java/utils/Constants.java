package utils;

public final class Constants {

    // the order of these is the same as the order of the enums, and the 3 values are R,G,B
    public static final double[][] COLOR_AVERAGES = {{144.05, 255.0, 76.2}, {249.76, 204.99, 92.36}, {80.62, 255.0, 120.94}, {75.03, 242.8, 253.3}};
    public static final double[][] COLOR_STANDARD_DEVIATIONS = {{3.66, 2.0, 19.54}, {17.29, 27.46, 21.65}, {5.37, 2.0, 3.41}, {7.86, 8.04, 5.24}};

    public enum Color{

        Yellow(COLOR_AVERAGES[0], COLOR_STANDARD_DEVIATIONS[0]), Red(COLOR_AVERAGES[1], COLOR_STANDARD_DEVIATIONS[1]), Green(COLOR_AVERAGES[2], COLOR_STANDARD_DEVIATIONS[2]), Blue(COLOR_AVERAGES[3], COLOR_STANDARD_DEVIATIONS[3]);
    
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