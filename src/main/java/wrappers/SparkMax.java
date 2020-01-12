package wrappers;

import interfaces.*;
import com.revrobotics.CANEncoder;
import com.revrobotics.CANPIDController;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel;
import com.revrobotics.ControlType;

public class SparkMax implements PIDMotor {

    CANSparkMax motor;
    CANEncoder encoder;
    CANPIDController pidController;

    private double speedVariance; 

    //@param canID: CAN ID of the motor
    public SparkMax(int canID) {

        motor = new CANSparkMax(canID, CANSparkMaxLowLevel.MotorType.kBrushless);
        encoder = new CANEncoder(motor);
        pidController = new CANPIDController(motor);

        speedVariance = 0.0;

        encoder.setPosition(0);

        pidController.setP(1.5);
        pidController.setI(0.2);
        pidController.setD(0);
        pidController.setIZone(0);
        pidController.setFF(0);

    }

    public void setP(double p) {
        pidController.setP(p);
    }

    public void setI(double i) {
        pidController.setI(i);
    }

    public void setD(double d) {
        pidController.setD(d);
    }

    // @param percentVariance: decimal percent (0 - 1) that the PID controller can vary from the input speed
    public void setSpeedVariance(double percentVariance) {
        speedVariance = percentVariance;
    }

    public void setSpeed(double speed) {

        pidController.setOutputRange(
            speed * (1 - speedVariance),
            speed * (1 + speedVariance)
        );

        pidController.setReference(speed, ControlType.kVelocity);

    }

    public void setPosition(double rotations, double minSpeed, double maxSpeed) {

        pidController.setOutputRange(minSpeed, maxSpeed);

        pidController.setReference(rotations, ControlType.kPosition);

    }

    public double getPosition() {

       return encoder.getPosition();

    }

    public void resetEncoder() {

        encoder.setPosition(0);

    }

}