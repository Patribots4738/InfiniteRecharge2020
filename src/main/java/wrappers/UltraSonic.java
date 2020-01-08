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
        sonicVolts = (sonicVolts/0.00977) * .394 / 12;//0.477 + (2.36 * sonicVolts) + (1.05 * sonicVolts * sonicVolts) - (0.19 * sonicVolts * sonicVolts * sonicVolts);
        int feet = (int) sonicVolts;
        double decimal = sonicVolts - feet;
        decimal = decimal * 12;
        int inches = (int) decimal;

        String feetString = Integer.toString(feet);
        String inchesString = Integer.toString(inches);

        return "feet: " + feetString + ", inches: " + inchesString;

    }

}