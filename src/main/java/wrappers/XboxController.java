package wrappers;

public class XboxController extends Gamepad {

	public enum Buttons {

		A, B, X, Y, L, R, Select, Start, LJ, RJ

	}

	public enum Axes {

		LeftX, LeftY, LeftTrigger, RightTrigger, RightX, RightY;

	}

	public XboxController(int port) {

		super(port);

	}

	public double getRightTrigger() {

		return this.getAxis(2);
		
	}

	public double getLeftTrigger() {

		return this.getAxis(3);

	}

	public double getAxis(Axes axis) {

		return this.getAxis(axis.ordinal());

	}

	public boolean getButton(Buttons button) {

		return this.getButton(button.ordinal());

	}

	public boolean getToggle(Buttons button) {

		return this.getToggle(button.ordinal());

	}

	public boolean getButtonDown(Buttons button) {

		return this.getButtonDown(button.ordinal());

	}

	public boolean getButtonUp(Buttons button) {

		return this.getButtonUp(button.ordinal());

	}

	public boolean getDPad(Directions direction) {

		return this.getPOV(direction);

	}

	public void setRumble(boolean isLeftRumble, double rumble) {

		super.setRumble(isLeftRumble, rumble);

	}

}