package wrappers;

public class TogglableButton {

	private boolean lastButtonState;
	private boolean toggleState;

	public TogglableButton () {
		lastButtonState = false;
		toggleState = false;
	}

	// returns true the instant the button is pressed, and false otherwise
	public boolean wasPressed(boolean currentButtonState) {

		// checks if the button is currently down and the last loop the button was not down
		if (currentButtonState == true && lastButtonState != true) {

			lastButtonState = currentButtonState;

			return true;

		}

		return false;

	}

	// returns true the instant the button is released, and false otherwise
	public boolean wasReleased(boolean currentButtonState) {

		// checks if the button is currently up and the last loop the button was not up
		if (currentButtonState == false && lastButtonState != false) {

			lastButtonState = currentButtonState;

			return true;

		}

		return false;

	}

	public boolean toggle(boolean currentButtonState) {

		if (wasPressed(currentButtonState)) {

			toggleState = !toggleState;

		}

		return toggleState;

	}
	
}