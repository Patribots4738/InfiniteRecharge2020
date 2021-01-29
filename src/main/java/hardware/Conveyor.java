package hardware;

import interfaces.*;

public class Conveyor {

	Motor conveyor;

	private double conveyorSpeed = 0.4;

	public Conveyor(Motor conveyor) {

		this.conveyor = conveyor;

	}

	// set conveyor based on direct input
	public void setSpeed(double speed) {

		conveyor.setSpeed(speed);

	}
	
	// set conveyor to preset speed based on boolean
	public void setConveyor(boolean on) {

		double speed = (on) ? conveyorSpeed : 0;

		conveyor.setSpeed(speed);

	}
		
}