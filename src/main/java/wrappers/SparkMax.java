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
    static double[] errorArray = new double[100];
    
    private double speedVariance;

    // @param canID: CAN ID of the motor
    public SparkMax(int canID) {

        motor = new CANSparkMax(canID, CANSparkMaxLowLevel.MotorType.kBrushless);
        encoder = new CANEncoder(motor);
        pidController = new CANPIDController(motor);

        speedVariance = 0.0;

        encoder.setPosition(0);
        /*
         * pidController.setP(1.5); pidController.setI(0.2); pidController.setD(0);
         * pidController.setIZone(0); pidController.setFF(0);
         */
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

    // @param percentVariance: decimal percent (0 - 1) that the PID controller can
    // vary from the input speed
    public void setSpeedVariance(double percentVariance) {
        speedVariance = percentVariance;
    }

    public void setOutputRange(double minSpeed, double maxSpeed) {

        pidController.setOutputRange(minSpeed, maxSpeed);

    }

    public void setSpeed(double speed) {

        pidController.setOutputRange(speed * (1 - speedVariance), speed * (1 + speedVariance));

        pidController.setReference(speed, ControlType.kVelocity);

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

    // default feedForward is 1; if it is not 1 the motor will not move
    // p must be greater than or equal to 0, no negetives
    // PID 
    public static double sparkMaxPID(double desiredCommand, double feedForward, double P, double I, double D,
            SparkMax sparkMax) {

        double error = desiredCommand - sparkMax.getSpeed();
        double sum = 0;
        double E_P = 0;
        double E_I = 0;
        double E_D = 0;

        for (int i = errorArray.length - 1; i >= 0; i--) {

            if (i == 0) {

                errorArray[0] = error;

            } else {

                errorArray[i] = errorArray[i - 1];
               
            }

        }

        //System.out.println(Arrays.toString(errorArray));

        for (int i = 0; i < errorArray.length; i++) {

            sum += errorArray[i];

        }

        E_P = error;
        // 0.02 is in seconds; 0.02 is delta time
        // E_I is the integral (area) of the error
        E_I = sum * 0.02;

        // 0.02 is in seconds; 0.02 is delta time
        // E_D is the derivative (slope) of the error in the last 0.02 seconds
        E_D = (errorArray[0] - errorArray[1]) / 0.02;

        // correctedCommand = (desiredCommand) + (delta of desiredCommand)
        double correctedCommand = (feedForward * desiredCommand) + (P * E_P) + (I * E_I) + (D * E_D);
        //System.out.println("P: " + E_P + "\tI: " + E_I + "\tD: " + E_D);
        return correctedCommand;

    }
}