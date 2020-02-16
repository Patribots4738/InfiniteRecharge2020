package wrappers;

import edu.wpi.first.wpilibj.GenericHID.RumbleType;

public class XboxController extends Gamepad {

	/*Zach here hopefully making your life a little easier
	so there are enums for all the different axes and buttons on xboxControllers
	but sometimes you need to use their numbers, so here's a key for you:
	Joysticks:
	0: left x axis
	1: left y axis
	2: left trigger (only 0 - 1)
	3: right trigger (only 0 -1)
	4: right x axis
	5: right y axis

	Buttons:
	0: A
	1: B
	2: X
	3: Y
	4: left bumper
	5: right bumper
	6: the one with two boxes
	7: the one with three lines
	8: left joystick
	9: right joystick
	*/

	public XboxController(int port) {

		super(port);

	}

	public Vector getLeftStick() {

		return new Vector(this.getAxis(0), -this.getAxis(1));//this is inverted because for some reason full foward is -1

	}

	public Vector getRightStick() {

		return new Vector(this.getAxis(4), -this.getAxis(5));//this is inverted because for some reason full foward is -1

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

	public void setRumble(RumbleType type, double rumble) {

		this.setRumble(type, rumble);

	}

	public enum Buttons {

		A, B, X, Y, L, R, Select, Start, LJ, RJ

	}
	
	public enum Axes {

		LeftX, LeftY, LeftTrigger, RightTrigger, RightX, RightY;

	}

}