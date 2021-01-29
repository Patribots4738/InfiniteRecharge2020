package hardware;

import wrappers.*;
import frc.robot.Robot;
import interfaces.*;
import utils.*;

public class Shooter {

	PIDMotor topWheel;
	PIDMotor bottomWheel;

	MotorGroup feeders;    

	DoubleSolenoid blocker;

	public static boolean readyToFire = false;

	private double feedRate = -0.5;

	// in decimal percent
	private double acceptableSpeedError = 0.05;

	public Shooter(PIDMotor topWheel, PIDMotor bottomWheel, MotorGroup feeders, DoubleSolenoid blocker) {

		this.topWheel = topWheel;
		this.bottomWheel = bottomWheel;

		this.feeders = feeders;

		this.blocker = blocker;

	}

	private double[] distanceToSpeeds(double distance) {

		double topWheelSpeed = getTopSpeed(distance);
		double bottomWheelSpeed = getBottomSpeed(distance);

		if(distance > 200) {

			topWheelSpeed = 0.185 + (distance / 60000.0);
			bottomWheelSpeed = 0.695 + (distance / 50000.0);

		}

		if(Robot.emergencyManual) {

			topWheelSpeed = 0.58;
			bottomWheelSpeed = 0.36;

		}
		
		return new double[]{topWheelSpeed, bottomWheelSpeed};

	}

	private double getTopSpeed(double distance) {

		double const1 = (-2.15 * (Math.pow(10.0, -9.0)));
		double const2 = (1.99 * (Math.pow(10.0, -6.0)));
		double const3 = (-6.428 * (Math.pow(10.0, -4.0)));
		double const4 = (0.0835);
		double const5 = (-3.02);

		double degree4 = (Math.pow(distance, 4)) * const1;
		double degree3 = (Math.pow(distance, 3)) * const2;
		double degree2 = (Math.pow(distance, 2)) * const3;
		double degree1 = distance * const4;

		return degree4 + degree3 + degree2 + degree1 + const5;

	}

	private double getBottomSpeed(double distance) {

		double const1 = (3.04 * (Math.pow(10.0, -9.0)));
		double const2 = (-2.92 * (Math.pow(10.0, -6.0)));
		double const3 = (1 * (Math.pow(10.0, -3.0)));
		double const4 = (-0.143);
		double const5 = (7.58);

		double degree4 = (Math.pow(distance, 4)) * const1;
		double degree3 = (Math.pow(distance, 3)) * const2;
		double degree2 = (Math.pow(distance, 2)) * const3;
		double degree1 = distance * const4;

		return degree4 + degree3 + degree2 + degree1 + const5;

	}

	// sets the shooter speeds based on distance
	public void setShooterSpeeds(double distance) {

		double[] speeds = distanceToSpeeds(distance);

		topWheel.setSpeed(speeds[0]);
		bottomWheel.setSpeed(-speeds[1]);

		eval(distance);
		
	}

	public void eval(double distance) {

		double[] speeds = distanceToSpeeds(distance);

		boolean topReady = Calc.isBetween(topWheel.getSpeed(), speeds[0] - acceptableSpeedError, speeds[0] + acceptableSpeedError);
		boolean bottomReady = Calc.isBetween(Math.abs(bottomWheel.getSpeed()), speeds[1] - acceptableSpeedError, speeds[1] + acceptableSpeedError);

		readyToFire = topReady && bottomReady;
/*
		System.out.println("top: " + topReady);
		System.out.println("bottom: " + bottomReady);
*/
	}

	public void setRawSpeeds(double topSpeed, double bottomSpeed) {

		topWheel.setSpeed(topSpeed);
		bottomWheel.setSpeed(-bottomSpeed);

	}

	public void setBlocker(boolean on) {

		blocker.activateChannel(on);

	}

	public void stop() {
		
		topWheel.setPercent(0);
		bottomWheel.setPercent(0);

		feeders.setSpeed(0);

		blocker.activateChannel(false);

		readyToFire = false;

	}

	public void setFeeders(boolean on) {
		
		blocker.activateChannel(on);

		feeders.setSpeed((on) ? feedRate : 0);

	}

}