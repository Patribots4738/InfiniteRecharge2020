package hardware;

import utils.Calc;
import wrappers.*;

public class Drive {

    MotorGroup leftMotors;
    MotorGroup rightMotors;

    public Drive(MotorGroup leftMotors, MotorGroup rightMotors) {

        this.leftMotors = leftMotors;
        this.rightMotors = rightMotors;

        leftMotors.setSpeed(0);
        rightMotors.setSpeed(0);

    }

    public void linearArcade(double throttle, double turning) {

        double leftMotorInput = (throttle + turning);
        double rightMotorInput = -(throttle - turning);

        leftMotors.setSpeed(leftMotorInput);
        rightMotors.setSpeed(rightMotorInput);

    }

    public void parabolicArcade(double throttle, double turning) {

        double leftMotorInput = (throttle + turning) * Math.abs((throttle + turning));
        double rightMotorInput = (throttle - turning) * Math.abs((throttle - turning));

        leftMotors.setSpeed(leftMotorInput);
        rightMotors.setSpeed(-rightMotorInput);

    }

    public void linearTank(double leftStick, double rightStick) {

        double leftMotorInput = leftStick;
        double rightMotorInput = -rightStick;

        leftMotors.setSpeed(leftMotorInput);
        rightMotors.setSpeed(rightMotorInput);

    }

    public void parabolicTank(double leftStick, double rightStick) {

        double leftMotorInput = leftStick * Math.abs(leftStick);
        double rightMotorInput = -(rightStick * Math.abs(rightStick));

        leftMotors.setSpeed(leftMotorInput);
        rightMotors.setSpeed(rightMotorInput);

    }

    public void curvature(double throttle, double turning) {

        if(throttle == 0) {

            linearArcade(throttle, turning);
            
        } else {

            double speedDifference = Math.atan(turning * Math.PI) * throttle;

            double leftMotorInput = throttle - speedDifference;
            double rightMotorInput = throttle + speedDifference;

            leftMotorInput = Calc.bananaCurve(leftMotorInput);
            rightMotorInput = -Calc.bananaCurve(rightMotorInput);

            leftMotors.setSpeed(leftMotorInput);
            rightMotors.setSpeed(rightMotorInput);

        }

    }

}