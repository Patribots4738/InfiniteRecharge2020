package hardware;

import interfaces.*;
import wrappers.*;
import utils.*;

public class Intake {

	Motor sucker;

	Limelight ballFinder;

	Drive drive;

	Conveyor conveyor;

	private double maxTurning = 0.2;

	private double acceptableAngleError = .5;

	private double minTurning = 0.05;

	private double converter = 1.0 / 15;

    public static boolean aligned = false;

	PIDLoop aimLoop;

	public Intake(Motor sucker, Limelight ballFinder, Drive drive, Conveyor conveyor) {

		this.sucker = sucker;

		this.ballFinder = ballFinder;

		this.drive = drive;

		this.conveyor = conveyor;

		aimLoop = new PIDLoop(.95, .15, .075, 1, 25);

	}

	public void setSuck(double suckRate) {

		sucker.setSpeed(suckRate);

	}

	public void seekBall() {

		System.out.println("Horizontal Angle: " + ballFinder.getHorizontalAngle());
		System.out.println("Vertical Angle: " + ballFinder.getVerticalAngle());

		double angle = ballFinder.getHorizontalAngle();

		aligned = Math.abs(angle) <= acceptableAngleError;

		double turning = -(aimLoop.getCommand(0, angle) * converter); 

		double throttle = 0.2;

		if(Math.abs(turning) < minTurning) {

			turning = minTurning * Math.signum(turning);

		}

		if(Math.abs(turning) > maxTurning) {

			turning = maxTurning * Math.signum(turning);

		}

		drive.bananaArcade(throttle, turning);
		
		if(ballFinder.getTargetAreaPercent() > 1.0) {

			setSuck(-0.75);
			conveyor.setConveyor(true);

		} else {

			conveyor.setConveyor(false);

		}

	}

}