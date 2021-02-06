package hardware;

import interfaces.*;
import wrappers.*;
import utils.*;

public class Intake {

	Motor sucker;

	Limelight ballFinder;

	Drive drive;

	Conveyor conveyor;

	MovingAverage ballPercentLog;

	public int ballsCollected;

	private boolean aboveBallPercent;

	private double maxTurning = 0.2;

	private double minTurning = 0.05;

	private double converter = 1.0 / 15;

	private double closeBallPercent = 2.4;

	PIDLoop aimLoop;

	public Intake(Motor sucker, Limelight ballFinder, Drive drive, Conveyor conveyor) {

		aboveBallPercent = false;

		ballPercentLog = new MovingAverage(20);

		this.sucker = sucker;

		this.ballFinder = ballFinder;

		this.drive = drive;

		this.conveyor = conveyor;

		ballsCollected = 0;

		aimLoop = new PIDLoop(.95, .15, .075, 1, 25);

	}

	public void setSuck(double suckRate) {

		sucker.setSpeed(suckRate);

	}

	public void resetBallCount() {

		ballsCollected = 0;

	}

	public int getBallsCollected() {

		return ballsCollected;

	}

	public void seekBall() {

		//System.out.println("Horizontal Angle: " + ballFinder.getHorizontalAngle());
		//System.out.println("Vertical Angle: " + ballFinder.getVerticalAngle());

		// set the intake and conveyor on to collect balls
		setSuck(-0.75);
		conveyor.setConveyor(true);

		ballPercentLog.addValue(ballFinder.getTargetAreaPercent());

		double angle = ballFinder.getHorizontalAngle();

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

		/*
		System.out.println("Log Average Bigger: " + (ballPercentLog.getAverage() > closeBallPercent));
		System.out.println("AboveBallPercent: " + aboveBallPercent);
		System.out.println("Log: " + ballFinder.getTargetAreaPercent() + "     CloseBallPercent: " + closeBallPercent);
		*/
		
		// check and record that the area taken up by the ball has reached the amount it should be when the ball is close (like inside the intake close)
		if (ballPercentLog.getAverage() > closeBallPercent) {

			aboveBallPercent = true;

		}

		// check that the ball has disapperead from veiw after getting inside the intake (meaning it's been collected)
		// so increase the number of balls collected, and reset the checker for if the ball gets close
		if (aboveBallPercent && ballPercentLog.getAverage() < closeBallPercent) {

			ballsCollected += 1;
			aboveBallPercent = false;

		}

	}

}