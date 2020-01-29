package utils;

import java.util.ArrayList;

// this class is used to take the average of a constantly changing set of values
public class MovingAverage {

    double[] values;

    public MovingAverage(int size) {

        this.values = new double[size];

    }

    public void addValue(double newVal) {

        for(int i = 0; i < values.length; i++) {

            values[i + 1] = values[i];

        }

        values[0] = newVal;

    }

    public double getAverage() {

        double sum = 0;

        for (int i = 0; i < values.length; i++) {

            sum += values[i];

        }
    
        return sum / values.length;
        
    }

}