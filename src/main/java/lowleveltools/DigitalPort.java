package lowleveltools;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DigitalOutput;

public class DigitalPort {

	DigitalInput input;
	DigitalOutput output;

	public DigitalPort(int port) {

		input = new DigitalInput(port);
		output = new DigitalOutput(port);

	}

	public void setState(boolean on) {

		output.set(on);

	}

	public boolean getState() {

		return input.get();

	}

}   