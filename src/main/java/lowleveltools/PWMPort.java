package lowleveltools;

import edu.wpi.first.wpilibj.PWM;

public class PWMPort {

    PWM PWMPort;

    public PWMPort(int port) {

        PWMPort = new PWM(port);

    }

    public int getPort() {

        return PWMPort.getChannel();

    }

    // input is decimal percent power, from 0 to 1
    public void setPower(double percentPower) {

        int pwmOutput = (int)(percentPower * 255.0);

        this.setRaw(pwmOutput);

    }

    // returns decimal percent power, from 0 to 1
    public double getPower() {

        int pwmInput = this.getRaw();

        return ((double)pwmInput / 255.0);

    }

    // get raw output from the PWM port, 0 to 255
    public int getRaw() {

        return PWMPort.getRaw();

    }

    // set raw output to the PWM port, 0 to 255
    public void setRaw(int rawInput) {

        if(rawInput > 255) {

            rawInput = 255;

        }

        if(rawInput < 0) {

            rawInput = 0;

        }

        PWMPort.setRaw(rawInput);

    }

}