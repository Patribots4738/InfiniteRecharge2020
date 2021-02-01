package hardware;

import interfaces.PIDMotor;
import wrappers.*;

public class Elevator {

	PIDMotor rightLifter;
	PIDMotor leftLifter;

	DoubleSolenoid lock;

	double defaultSpeed = 0.4;

	public Elevator(PIDMotor leftLifter, PIDMotor rightLifter, DoubleSolenoid lock) {
		
		this.rightLifter = rightLifter;
		this.leftLifter = leftLifter;

		this.lock = lock;

		rightLifter.setSpeed(0.0);
		leftLifter.setSpeed(0.0);
		
	}

	public void setElevator(double speed) {

		rightLifter.setPercent(speed);
		leftLifter.setPercent(-speed);

	}

	public void setElevatorUp() {

		rightLifter.setPercent(defaultSpeed);
		leftLifter.setPercent(-defaultSpeed);

	}

	public void setElevatorDown() {

		rightLifter.setPercent(-defaultSpeed);
		leftLifter.setPercent(defaultSpeed);

	}

	public void stop() {

		rightLifter.setPercent(0);
		leftLifter.setPercent(0);

	}

	public void setLock(boolean isLocked) {

		lock.activateChannel(isLocked);

	}

}