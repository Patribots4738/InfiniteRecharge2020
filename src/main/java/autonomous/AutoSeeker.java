package autonomous;

import wrappers.*;
import utils.*;
import hardware.*;

public class AutoSeeker {

    Intake intake;

    Conveyor conveyor;

    Limelight ballFinder;

    Drive drive;

    PIDMotorGroup leftMotors;
	PIDMotorGroup rightMotors;

	MovingAverage ballPercentLog;
    
    private boolean aboveBallPercent = false;

    public int ballsCollected = 0;

    PIDLoop aimLoop;

	private double[] completePositions;
	
	private int pathNum = 0;

	// 1st 3 values are for finding balls, final one is for the final rotation before going to the end zone
	private int[][] blindTurningDirections = { 
					/* path A red */         {1, 1, -1, 1},
					/* path A blue */		 {1, -1, 1, -1},
					/* path B red */		 {1, 1, -1, 1},
					/* path B blue */        {1, -1, 1, -1}
											 };

	private double maxTurning = 0.2;

	private double minTurning = 0.05;

	private double converter = 1.0 / 15;

	private double closeBallPercent = 2.4;

    private double acceptableError = 0.25;
    
    private boolean allBallsCollected = false;

    private boolean startFinalRotation = false;

    public AutoSeeker(Intake intake, Conveyor conveyor, Limelight ballFinder, Drive drive, PIDMotorGroup leftMotors, PIDMotorGroup rightMotors) {
       
        this.intake = intake;

        this.conveyor = conveyor;

		this.ballFinder = ballFinder;

		this.drive = drive;

		this.leftMotors = leftMotors;
        this.rightMotors = rightMotors;

        ballPercentLog = new MovingAverage(20);

        completePositions = new double[2];
        completePositions[0] = leftMotors.getPosition();
        completePositions[1] = rightMotors.getPosition();

		aimLoop = new PIDLoop(.95, .15, .075, 1, 25);

    }

    public void resetBallCount() {

		ballsCollected = 0;
		startFinalRotation = false;
		allBallsCollected = false;

	}

	public void detectPath() {

		// not finsihed

	}

	public int getBallsCollected() {

		return ballsCollected;

	}

	public boolean isPathRed() {

		return !((ballFinder.getTargetAreaPercent() == 0) || (ballFinder.getTargetAreaPercent() < 1.0));

	}

	public void rotate(double rotations, double speed) {

        resetRotate();

        rotations = Calc.robotRotationsToDrive(rotations);

		completePositions[0] += rotations;
		completePositions[1] += rotations;

		leftMotors.setPosition(completePositions[0], -speed, speed);
        rightMotors.setPosition(completePositions[1], -speed, speed); 
		
    }
    
    public boolean finishedFinalRotation() {

        double leftWheelPosition = leftMotors.getPosition();
		double rightWheelPosition = rightMotors.getPosition();

		double leftError = Math.abs(completePositions[0] - leftWheelPosition);
		double rightError = Math.abs(completePositions[1] - rightWheelPosition);

        // check that motors have reached the intended positions
		if((leftError <= acceptableError && rightError <= acceptableError)) {

            return true;

        } 
        
        return false;

    }

	public void resetRotate() {

		completePositions[0] = leftMotors.getPosition();
		completePositions[1] = rightMotors.getPosition();

    }
    
    public void countBalls() {

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
        
        // check that we have collected all the balls
        allBallsCollected = (ballsCollected == 3) && !allBallsCollected;

    }

	public void seekBalls() {

		System.out.println("Horizontal Angle: " + ballFinder.getHorizontalAngle());
		System.out.println("Vertical Angle: " + ballFinder.getVerticalAngle());
		System.out.println("Target Area: " + ballFinder.getTargetAreaPercent());
		System.out.println("Balls: " + ballsCollected);

		// set the intake and conveyor on to collect balls
		intake.setSuck(-0.75);
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
        
        countBalls();

        // if we can't find any balls, turn in the right direction until we find one
		if(ballFinder.getTargetAreaPercent() < 0.08 && ballsCollected < 3) {

			throttle = 0;
			turning = 0.2 * blindTurningDirections[pathNum][ballsCollected];

		}

        drive.bananaArcade(throttle, turning);

		/*
		System.out.println("Log Average Bigger: " + (ballPercentLog.getAverage() > closeBallPercent));
		System.out.println("AboveBallPercent: " + aboveBallPercent);
		System.out.println("Log: " + ballFinder.getTargetAreaPercent() + "     CloseBallPercent: " + closeBallPercent);
		*/

    }
    
    public void runSeeker() {

        // if we haven't collected all the balls, go find them
        if(!allBallsCollected) {

            seekBalls();

        } else {

            // once we've collected all the balls, do the final turn
            if(!startFinalRotation) {

                // config motors for positional control
		        leftMotors.setPID(2, 0, 0);
		        rightMotors.setPID(2, 0, 0);

				// make array of correct final rotation values, first one is 0.25, 2nd is 0.1, find others
                rotate(0.1 * blindTurningDirections[pathNum][3], 0.2);

                startFinalRotation = true;

            }

            // once we've finished our final rotation, get moving
            if(finishedFinalRotation()) {

                // config motors for velocity control
		        leftMotors.setPID(0.5, 0, 0);
		        rightMotors.setPID(0.5, 0, 0);

                drive.bananaArcade(0.2, 0);

            }

        }

    }

}