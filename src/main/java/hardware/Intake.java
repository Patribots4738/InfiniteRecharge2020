package hardware;

import interfaces.*;

public class Intake {

	Motor sucker;

	public Intake(Motor sucker) {

		this.sucker = sucker;

	}

	public void setSuck(double suckRate) {

		sucker.setSpeed(suckRate);

	}

}