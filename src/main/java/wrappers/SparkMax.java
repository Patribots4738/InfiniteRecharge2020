package wrappers;

import interfaces.*;
import utils.*;

import com.revrobotics.CANEncoder;
import com.revrobotics.CANPIDController;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel;
import com.revrobotics.ControlType;

public class SparkMax implements PIDMotor {

	CANSparkMax motor;
	CANEncoder encoder;
	CANPIDController pidController;

	PIDLoop PIDLoop;

	// @param canID: CAN ID of the motor
	public SparkMax(int canID) {

		motor = new CANSparkMax(canID, CANSparkMaxLowLevel.MotorType.kBrushless);
		encoder = new CANEncoder(motor);
		pidController = new CANPIDController(motor);

		encoder.setPosition(0);

		pidController.setP(0);
		pidController.setI(0);
		pidController.setD(0);

		PIDLoop = new PIDLoop(0, 0, 0);
		
	}

	public void setP(double P) {

		PIDLoop.setP(P);
		pidController.setP(P);

	}

	public void setI(double I) {

		PIDLoop.setI(I);
		pidController.setI(I);

	}

	public void setD(double D) {

		PIDLoop.setD(D);
		pidController.setD(D);

	}

	public void setFF(double FF) {

		PIDLoop.setFF(FF);
		pidController.setFF(FF);

	}

	public void setIzone(int Izone) {

		PIDLoop.setIzone(Izone);
		pidController.setIZone(Izone);

	}

	public void setPID(double P, double I, double D) {

		setP(P);
		setI(I);
		setD(D);

	}

	public void setSpeed(double speed) {

		double currentCommand = PIDLoop.getCommand(speed, getSpeed());

		this.setPercent(currentCommand);

	}

	public void setPercent(double percent) {

		pidController.setReference(percent, ControlType.kDutyCycle);

	}

	public void setPosition(double rotations, double minSpeed, double maxSpeed) {

		pidController.setOutputRange(minSpeed, maxSpeed);

		pidController.setReference(rotations, ControlType.kPosition);

	}

	public double getPosition() {

		return encoder.getPosition();

	}

	public double getSpeed() {

		// 5700 is the theoretical max RPM of the neo
		return encoder.getVelocity() / 5700.0;

	}

	public void resetEncoder() {

		encoder.setPosition(0);

	}

}