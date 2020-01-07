package wrappers;

import interfaces.*;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;

public class Talon implements PIDMotor{

    private TalonSRX motor;

    public Talon(int canID){

        motor = new TalonSRX(canID);

        //this is a black box, dont touch, get Zach (unless I'm gone, then google, I'm sorry for the horrors that await you)
        motor.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, 100);
        motor.configAllowableClosedloopError(0, 0, 20);
        motor.config_kF(0, 0, 20);
        motor.configNominalOutputForward(0, 20);
        motor.configNominalOutputReverse(0, 20);
        motor.configPeakOutputForward(1, 20);
        motor.configPeakOutputReverse(-1, 20);
        motor.setSensorPhase(true);

    }

    public void setSensorPhase(boolean phase){
        motor.setSensorPhase(phase);
    }

    public void setP(double P) {
        motor.config_kP(0, P, 20);
    }

    public void setI(double I) {
        motor.config_kI(0, I, 20);
    }

    public void setD(double D) {
        motor.config_kD(0, D, 20);
    }

    public void setSpeed(double speed){
        motor.set(ControlMode.PercentOutput, speed);
    }

}