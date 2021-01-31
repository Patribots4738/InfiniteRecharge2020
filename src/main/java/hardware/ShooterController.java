package hardware;

import utils.Constants;
import utils.PIDLoop;
import wrappers.Limelight;

public class ShooterController {

	Shooter shooter;

	Conveyor conveyor;

	Limelight limelight;

	Drive drive;

	// swap out current alignment math with PIDLoop class

	// these may need some tuning as things change
	private double maxSpeed = 0.12;  //0.15 worked

	private double acceptableAngleError = .5;

	private double minSpeed = 0.03;

	private double converter = 1.0 / 15;

    public static boolean aligned = false;
    
    private int loop;

	private double shortOffset = 3.55;//3.55;

	private double longOffset = 1.68;

	PIDLoop aimLoop;

	public ShooterController(Conveyor conveyor, Shooter shooter, Limelight limelight, Drive drive) {

		this.shooter = shooter;

		this.conveyor = conveyor;

		this.limelight = limelight;

		this.drive = drive;
		  //0.5,0.5,0 PID worked ok
		//aimLoop = new PIDLoop(.95, .15, .075, 1, 25); GOOD PID
		
        aimLoop = new PIDLoop(.95, .15, .075, 1, 25);
        
        loop = 0;
	}

	// stops the shooter
	public void stop() {

		shooter.stop();

		conveyor.setSpeed(0);

		shooter.eval(0);

	}

	// checks if the robot is aligned and if the shooter is spun up, then updates internal variables accordingly
	public void eval() {

		double offset = (limelight.getDistance() > 200) ? (longOffset) : (shortOffset);

		double angle = limelight.getHorizontalAngle() - offset;

		aligned = Math.abs(angle) <= acceptableAngleError; 
	 
		shooter.eval(limelight.getDistance());

	}

	// this spins up the shooter and sets the conveyor and feeders based on wether the shooter is up to speed
	public void fire() {

		shooter.setShooterSpeeds(limelight.getDistance());

		eval();

		conveyor.setConveyor(Shooter.readyToFire);

		shooter.setFeeders(Shooter.readyToFire);

		//System.out.println("shooter: " + Shooter.readyToFire);

	}

	// this aligns the robot with the vision target found by the limelight
	public void aim() {

		double offset = (limelight.getDistance() > 200) ? (longOffset) : (shortOffset);

		double angle = (Math.abs(limelight.getHorizontalAngle()) - offset) * Math.signum(limelight.getHorizontalAngle());

		aligned = Math.abs(angle) <= acceptableAngleError;

		double speed = -(aimLoop.getCommand(0, angle) * converter); 

		if(Math.abs(speed) < minSpeed) {

			speed = minSpeed * Math.signum(speed);

		}

		if(Math.abs(speed) > maxSpeed) {

			speed = maxSpeed * Math.signum(speed);

		}

        drive.bananaTank(speed, -speed);

        loop++;
        
        String str = loop * Constants.LOOP_TIME + "," + angle;

        //System.out.println(str);
/*
		System.out.println(angle);
		System.out.println(aligned);
		System.out.println(speed);
		System.out.println(limelight.getHorizontalAngle());
*/

	}

}