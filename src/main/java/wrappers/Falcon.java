package wrappers;

import interfaces.*;
import utils.*;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.TalonFX;

public class Falcon implements PIDMotor{

    TalonFX motor;

    PIDLoop PIDLoop;

    public Falcon(int canID) {

        motor = new TalonFX(canID);

        //this is a black box, dont touch, get Zach (unless I'm gone, then google, I'm sorry for the horrors that await you)
        motor.configSelectedFeedbackSensor(FeedbackDevice.IntegratedSensor);
        motor.configAllowableClosedloopError(0, 0, 20);
        motor.config_kF(0, 0, 20);
        motor.configNominalOutputForward(0, 20);
        motor.configNominalOutputReverse(0, 20);
        motor.configPeakOutputForward(1, 20);
        motor.configPeakOutputReverse(-1, 20);
        motor.setSensorPhase(false);

        PIDLoop = new PIDLoop(0, 0, 0);

    }

    public void setP(double P) {

        motor.config_kP(0, P, 20);
        PIDLoop.setP(P);

    }

    public void setI(double I) {

        motor.config_kI(0, I, 20);
        PIDLoop.setI(I);

    }

    public void setD(double D) {

        motor.config_kD(0, D);
        PIDLoop.setD(D);

    }

    public void setFF(double FF) {

        motor.config_kF(0, FF, 20);
        PIDLoop.setFF(FF);

    }

    public void setPID(double P, double I, double D) {

        setP(P);
        setI(I);
        setD(D);

    }

    public void setSpeed(double speed) {

        double currentCommand = PIDLoop.getCommand(speed, getSpeed());

        motor.set(ControlMode.PercentOutput, currentCommand);

    }

    public double getSpeed() {

        // the 22000.0 is to convert between the weird encoder base units and percent output
        return motor.getSelectedSensorVelocity() / 22000.0;

    }

    public void setPosition(double rotations, double minSpeed, double maxSpeed) {

        motor.configPeakOutputForward(maxSpeed);
        motor.configPeakOutputReverse(minSpeed);

        motor.set(ControlMode.Position, rotations * Constants.FALCON_CLICKS);

    }

    public double getPosition() {

        return motor.getSelectedSensorPosition() / ((double) Constants.FALCON_CLICKS);

    }

    public void resetEncoder() {

        motor.setSelectedSensorPosition(0);

    }

}