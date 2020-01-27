package utils;

import java.util.ArrayList;

public class MovingAverage {

    ArrayList<Double> values;

    int maxSize;

    public MovingAverage(int size) {

        values = new ArrayList<Double>();

        maxSize = size;

    }

    public void addValue(double newVal) {

        values.add(0, newVal);

        while(values.size() > maxSize) {

            values.remove(values.size() - 1);

        }  

    }

    public double getAverage() {

        double sum = 0;

        for(int i =0; i < values.size(); i++) {

            sum += values.get(i);

        }

        return sum / values.size();

    }

}