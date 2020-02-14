package lowleveltools;

import java.util.InputMismatchException;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.AnalogOutput;
import utils.MovingAverage;

public class AnalogPort{

    // Raw analog port class for reading a writing voltage to an analog port on the rio

    AnalogInput input;
    AnalogOutput output;
    MovingAverage inputAvg;

    public AnalogPort(int port) {

        input = new AnalogInput(port);
        output = new AnalogOutput(port);

        inputAvg = new MovingAverage(10);

    }

    // 0 to 5, in volts
    public void setVoltage(double volts) {

        output.setVoltage(volts);

    }

    public double getVoltage() {

        inputAvg.addValue(input.getVoltage());

        return inputAvg.getAverage();

    }

    public double getRawVoltage() {

        return input.getVoltage();

    }

}