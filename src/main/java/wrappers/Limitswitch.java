package wrappers;

import edu.wpi.first.wpilibj.DigitalInput;

public class Limitswitch {

    DigitalInput limitswitch;
    TogglableButton switchButton;

    public Limitswitch(int port) {

        limitswitch = new DigitalInput(port);

        switchButton = new TogglableButton();

    }

    // returns the raw on/off from the limit switch
    public boolean getState() {

        return limitswitch.get();

    }

    // uses the togglableButton class to return true when the button is pressed
    public boolean wasPressed() {

        return switchButton.wasPressed(getState());

    }

    // uses the togglableButton class to return true when the button is released
    public boolean wasReleased() {

        return switchButton.wasReleased(getState());

    }

    // uses the togglableButton class to get the togglestate
    public boolean getToggle() {

        return switchButton.toggle(getState());

    }

} 