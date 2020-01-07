package wrappers;

public class UltraSonic{

    edu.wpi.first.wpilibj.AnalogInput PWMInput;

    public UltraSonic(int port) {

        PWMInput = new edu.wpi.first.wpilibj.AnalogInput(port);

    }

    public double getRaw() {

        return PWMInput.getAverageVoltage();

    }

    public String getDistance() {

        double sonicVolts = PWMInput.getAverageVoltage();
        sonicVolts = 0.825 + (1.43 * sonicVolts) + (1.63 * sonicVolts * sonicVolts) - (0.28 * sonicVolts * sonicVolts * sonicVolts);
        //0.716 + 2.1 * (sonicVolts) + 1.25 * (sonicVolts * sonicVolts) - 0.224 * (sonicVolts * sonicVolts * sonicVolts);
        int feet = (int) sonicVolts;
        double decimal = sonicVolts - feet;
        decimal = decimal * 12;
        int inches = (int) decimal;

        String feetString = Integer.toString(feet);
        String inchesString = Integer.toString(inches);

        return "feet: " + feetString + ", inches: " + inchesString;

    }

}