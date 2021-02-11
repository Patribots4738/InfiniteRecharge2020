package hardware;

import interfaces.*;
import wrappers.*;
import utils.*;

public class Intake {

	PIDMotorGroup leftMotors;
	PIDMotorGroup rightMotors;

	Motor sucker;

	Limelight ballFinder;

	Drive drive;

	Conveyor conveyor;

	MovingAverage ballPercentLog;

	public int ballsCollected;

	//-1 is left, +1 is right
	private int[] pathATurningVals = {1, -1, -1, 1};

	private double[] completePositions;

	private boolean aboveBallPercent;

	private boolean firstTime;

	private double maxTurning = 0.2;

	private double minTurning = 0.05;

	private double converter = 1.0 / 15;

	private double closeBallPercent = 2.4;

	private double acceptableError = 0.25;

	PIDLoop aimLoop;

	public Intake(Motor sucker, Limelight ballFinder, Drive drive, Conveyor conveyor, PIDMotorGroup leftMotors, PIDMotorGroup rightMotors) {

		aboveBallPercent = false;

		ballPercentLog = new MovingAverage(20);

		this.sucker = sucker;

		this.ballFinder = ballFinder;

		this.drive = drive;

		this.conveyor = conveyor;

		this.leftMotors = leftMotors;
		this.rightMotors = rightMotors;

		ballsCollected = 0;

		firstTime = true;

		completePositions = new double[] {

			leftMotors.getPosition(),
			rightMotors.getPosition()

		};

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

	public boolean isPathRed() {


		return !(ballFinder.getTargetAreaPercent() == 0);

	}

	public void rotate(double degrees, double speed) {

		double leftWheelPosition = leftMotors.getPosition();
		double rightWheelPosition = rightMotors.getPosition();

		double leftError = Math.abs(completePositions[0] - leftWheelPosition);
		double rightError = Math.abs(completePositions[1] - rightWheelPosition);

		if(leftError <= acceptableError && rightError <= acceptableError) {

			degrees = Calc.robotRotationsToDrive(degrees);

			completePositions[0] += degrees;
			completePositions[1] += degrees;

			leftMotors.setPosition(completePositions[0], -speed, speed);
			rightMotors.setPosition(completePositions[1], -speed, speed);

		} 
		
		return;

	}

	public void rotateReset() {

		completePositions[0] = 0.0;
		completePositions[1] = 0.0;

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

		if(ballFinder.getTargetAreaPercent() == 0 && ballsCollected < 3) {

			throttle = 0;
			turning = 0.2 * pathATurningVals[ballsCollected];
			System.out.println("BLIDNLY TURNING");	

		}

		if(ballsCollected == 3) {

			//throttle = 0.186;
			//turning = 0.253 * pathATurningVals[ballsCollected];

			if (firstTime) {

				rotateReset();
				firstTime = false;

			}

			rotate(0.9, 0.2);

		}

		System.out.println("Turning Speed: " + turning);
		drive.bananaArcade(throttle, turning);

		/*
		System.out.println("Log Average Bigger: " + (ballPercentLog.getAverage() > closeBallPercent));
		System.out.println("AboveBallPercent: " + aboveBallPercent);
		System.out.println("Log: " + ballFinder.getTargetAreaPercent() + "     CloseBallPercent: " + closeBallPercent);
		*/

	}

}