package lowleveltools;

import edu.wpi.first.wpilibj.PWM;

public class PWMPort {

	// NOT TESTED, NEEDS WORK

	PWM PWMPort;

	public PWMPort(int port) {

		PWMPort = new PWM(port);

	}

	public int getPort() {

		return PWMPort.getChannel();

	}

	// input is decimal percent power, from -1 to 1
	public void setPower(double percentPower) {

		int signal = (int)((percentPower * 127.5) + 127.5);

		this.setRaw(signal);

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