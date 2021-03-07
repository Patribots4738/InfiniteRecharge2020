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

	// each index in this array is another foot of distance from the target, starting at 10ft away, ending at 35ft away
	// these will be used to determine the speeds the shooter wheels need to be at when the robot is firing
	// order is topSpeed, bottomSpeed
	private double[][] shooterSpeeds = { {}, {}, {}, {}, {}, //10-15ft
										 {}, {}, {}, {}, {}, //16-20ft
										 {}, {}, {}, {}, {}, // 21-25ft
										 {}, {}, {}, {}, {}, // 26-30ft
										 {}, {}, {}, {}, {}}; // 31-35ft

	public Shooter(PIDMotor topWheel, PIDMotor bottomWheel, MotorGroup feeders, DoubleSolenoid blocker) {

		this.topWheel = topWheel;
		this.bottomWheel = bottomWheel;

		this.feeders = feeders;

		this.blocker = blocker;

	}

	private double[] distanceToSpeeds(double distance) {

		double feet = distance/12.0;

		// exception case for when the robot is at maximum range
		if(feet >= 35.0) {

			return shooterSpeeds[24];

		}

		// exception case for when the robot is at minimum range
		if(feet < 10) {

			return shooterSpeeds[0];

		}

		double arrayPosition = (feet - 10.0) / 5.0;

		int lowerIndex = (int)arrayPosition;
		int upperIndex = lowerIndex + 1;

		double percentBetweenPoints = arrayPosition - (double)lowerIndex;

		double topSpeed = shooterSpeeds[lowerIndex][0] + (shooterSpeeds[upperIndex][0] - shooterSpeeds[lowerIndex][0]) * percentBetweenPoints;
		double bottomSpeed = shooterSpeeds[lowerIndex][1] + (shooterSpeeds[upperIndex][1] - shooterSpeeds[lowerIndex][1]) * percentBetweenPoints;

		return new double[] {topSpeed, bottomSpeed};

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