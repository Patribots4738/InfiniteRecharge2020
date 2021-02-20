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

	// 1st 3 values are for finding balls, final one is for the final rotation before going to the end zone
	private int[][] blindTurningDirections = { 
					/* path A red */          {1, 1, -1, 1},
					/* path A blue */		  {1, 1, 1, -1},
					/* path B red */		  {1, 1, 1, -1},
					/* path B blue */         {1, -1, -1, 1}
											 };

	private double[] finalRotationAngles = { 
				/* path A red */            0.25,
				/* path A blue */		    0.0,
				/* path B red */		    0.4,
				/* path B blue */           0.375
										   };

	private double maxTurning = 0.2;

	private double minTurning = 0.05;

	private double converter = 1.0 / 15;

	private double closeBallPercent = 2.1;

    private double acceptableError = 0.25;
    
    private boolean allBallsCollected = false;

	private boolean startFinalRotation = false;
	
	private boolean pathFound = false;
	
	private int pathNum;

	private double forwardSpeed = 0.65;

	private double rotateSpeed = 0.45;

    public AutoSeeker(Intake intake, Conveyor conveyor, Limelight ballFinder, Drive drive, PIDMotorGroup leftMotors, PIDMotorGroup rightMotors) {
       
        this.intake = intake;

        this.conveyor = conveyor;

		this.ballFinder = ballFinder;

		this.drive = drive;

		this.leftMotors = leftMotors;
		this.rightMotors = rightMotors;

        ballPercentLog = new MovingAverage(10);

        completePositions = new double[2];
        completePositions[0] = leftMotors.getPosition();
        completePositions[1] = rightMotors.getPosition();

		aimLoop = new PIDLoop(.95, .15, .075, 1, 25);

    }

    public void reset() {

		ballsCollected = 0;
		startFinalRotation = false;
		allBallsCollected = false;
		pathFound = false;

	}

	public int getBallsCollected() {

		return ballsCollected;

	}

	public int getPathNum() {

		/*
		path 0 is red A
		path 1 is blue A
		path 2 red B
		path 3 blue B
		*/

		boolean isPathA;		
		boolean isPathRed = ballFinder.getTargetAreaPercent() > 1.0;

		if (isPathRed) {

			isPathA = Math.abs(ballFinder.getDirectVerticalAngle()) > 4.5 ? true : false;

		} else {

			isPathA = ballFinder.getDirectVerticalAngle() < 14.65 ? true : false;

		}

		if (isPathA) {

			return isPathRed ? 0 : 1;

		} else {

			return isPathRed ? 2 : 3;

		}

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
		System.out.println("PathNum: " + pathNum);

		// set the intake and conveyor on to collect balls
		intake.setSuck(-0.75);
		conveyor.setConveyor(true);

		ballPercentLog.addValue(ballFinder.getTargetAreaPercent());

		double angle = ballFinder.getHorizontalAngle();

		double turning = -(aimLoop.getCommand(0, angle) * converter); 

		double throttle = forwardSpeed;

		if(Math.abs(turning) < minTurning) {

			turning = minTurning * Math.signum(turning);

		}

		if(Math.abs(turning) > maxTurning) {

			turning = maxTurning * Math.signum(turning);

        }
        
        countBalls();

        // if we can't find any balls, turn in the right direction until we find one
		if(ballFinder.getTargetAreaPercent() < 0.0735 && ballsCollected < 3) {

			// System.out.println("TURNING");
			throttle = 0;
			turning = rotateSpeed * blindTurningDirections[pathNum][ballsCollected];

		}

		// force it to turn for that one time in path 1 when two balls are super close and it's hard to tell which one it is
		if (pathNum == 1 && ballFinder.getTargetAreaPercent() < 0.1 && ballsCollected < 1) {

			throttle = 0.1;
			turning = 0.1775;

		}

		if(pathNum == 0 && ballsCollected == 0) {

			throttle = 0.4;

		}

        drive.bananaArcade(throttle, turning);

		/*
		System.out.println("Log Average Bigger: " + (ballPercentLog.getAverage() > closeBallPercent));
		System.out.println("AboveBallPercent: " + aboveBallPercent);
		System.out.println("Log: " + ballFinder.getTargetAreaPercent() + "     CloseBallPercent: " + closeBallPercent);
		*/

    }
    
    public void runSeeker() {

		// detect which path we're on based on starting information
		if(!pathFound) {

			pathNum = getPathNum();

			pathFound = true;

		}

        // if we haven't collected all the balls, go find them
        if(!allBallsCollected) {

            seekBalls();

        } else {

            // once we've collected all the balls, do the final turn
            if(!startFinalRotation) {

                // config motors for positional control
		        leftMotors.setPID(2, 0, 0);
		        rightMotors.setPID(2, 0, 0);

                rotate(finalRotationAngles[pathNum] * blindTurningDirections[pathNum][3], rotateSpeed);

                startFinalRotation = true;

            }

            // once we've finished our final rotation, get moving
            if(finishedFinalRotation()) {

                // config motors for velocity control
		        leftMotors.setPID(0.5, 0, 0);
		        rightMotors.setPID(0.5, 0, 0);

                drive.bananaArcade(forwardSpeed, 0);

            }

        }

    }

}