package wrappers;

import edu.wpi.first.wpilibj.Joystick;

public class Gamepad {

	Joystick joystick;
	int port;
	public TogglableButton buttons[];

	public Gamepad(int port) {
		this.port = port;
		joystick = new Joystick(port);

		int buttonCount = joystick.getButtonCount();
		if (buttonCount < 10) {
			buttonCount = 10;
		}

		buttons = new TogglableButton[buttonCount];
		for (int i = 0; i < buttons.length; i++) {
			buttons[i] = new TogglableButton();
		}
	}

	/**
	 * @param button Button to be toggled.
	 * @return Returns opposite state from when button was previously toggled.
	 */
	public boolean getToggle(int button) {
		buttons[button].wasReleased(joystick.getRawButton(button + 1));
		return buttons[button].toggle(joystick.getRawButton(button + 1));
	}

	/**
	 * @param button Button being pressed down.
	 * @return Returns true on the button press.
	 */
	public boolean getButtonDown(int button) {
		return buttons[button].wasPressed(joystick.getRawButton(button + 1));
	}

	/**
	 * @param button Button being released.
	 * @return Returns true on the button's release.
	 */
	public boolean getButtonUp(int button) {
		return buttons[button].wasReleased(joystick.getRawButton(button + 1));
	}

	/**
	 * @param direction The direction the d-pad is being pressed.
	 * @return If direction being pressed is direction it returns true, otherwise
	 *         false.
	 */
	public boolean getPOV(Directions direction) {
		return (joystick.getPOV() == direction.ordinal() * 45);
	}

	public double getAxis(int axis) {

		return joystick.getRawAxis(axis);
	}

	public boolean getButton(int button) {
		return joystick.getRawButton(button + 1);
	}

	public enum Directions {
		N, NE, E, SE, S, SW, W, NW
	}
}