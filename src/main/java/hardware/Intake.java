package hardware;

import interfaces.*;
import wrappers.*;
import utils.*;

public class Intake {

	Motor sucker;

	Limelight ballFinder;

	Drive drive;

	Conveyor conveyor;

	MovingAverage movingAverage;

	public int ballsCollected;

	private boolean aboveBallPercent;

	private double log;

	private double maxTurning = 0.2;

	private double acceptableAngleError = .5;

	private double minTurning = 0.05;

	private double converter = 1.0 / 15;

	private double closeBallPercent = 2.4;

    public static boolean aligned = false;

	PIDLoop aimLoop;

	public Intake(Motor sucker, Limelight ballFinder, Drive drive, Conveyor conveyor) {

		aboveBallPercent = false;

		movingAverage = new MovingAverage(20);

		this.sucker = sucker;

		this.ballFinder = ballFinder;

		this.drive = drive;

		this.conveyor = conveyor;

		log = 0;

		ballsCollected = 0;

		aimLoop = new PIDLoop(.95, .15, .075, 1, 25);

	}

	public void setSuck(double suckRate) {

		sucker.setSpeed(suckRate);

	}

	public void seekBall() {

		//System.out.println("Horizontal Angle: " + ballFinder.getHorizontalAngle());
		//System.out.println("Vertical Angle: " + ballFinder.getVerticalAngle());

		log = ballFinder.getTargetAreaPercent();
		System.out.println(log);

		movingAverage.addValue(log);

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

		if(ballFinder.getTargetAreaPercent() == 0) {

			throttle = 0;
			turning = 0.2;	

		}

		drive.bananaArcade(throttle, turning);

		setSuck(-0.75);
		conveyor.setConveyor(true);

		System.out.println("Log Average Bigger: " + (movingAverage.getAverage() > closeBallPercent));
		System.out.println("AboveBallPercent: " + aboveBallPercent);
		System.out.println("Log: " + log + "     CloseBallPercent: " + closeBallPercent);
		
		if (movingAverage.getAverage() > closeBallPercent) {

			aboveBallPercent = true;

		}

		if (aboveBallPercent && movingAverage.getAverage() < closeBallPercent) {

			ballsCollected += 1;
			aboveBallPercent = false;

		}

	}

}