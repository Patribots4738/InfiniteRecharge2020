package wrappers;

import lowleveltools.DigitalPort;

public class Limitswitch {

    TogglableButton switchButton;

    DigitalPort DIOPort;

    public Limitswitch(int port) {

        switchButton = new TogglableButton();

        DIOPort = new DigitalPort(port);

    }

    // get the raw value from the digital input/output port
    public boolean getState() {

        return DIOPort.getState();

    }

    // these next three functions just access the base toggleableButton
    // features with the state of the swtich as the boolean
    public boolean getToggle() {

        return switchButton.toggle(getState());

    }

    public boolean getPressed() {

        return switchButton.wasPressed(getState());

    }

    public boolean getReleased() {

        return switchButton.wasReleased(getState());

    }

} 