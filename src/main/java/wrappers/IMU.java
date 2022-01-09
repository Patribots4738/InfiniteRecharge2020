package wrappers;

import com.analog.adis16470.frc.*;
import com.analog.adis16470.frc.ADIS16470_IMU.IMUAxis;

public class IMU {

    ADIS16470_IMU imu;

    double rotX = 0.0;
    double rotY = 0.0;
    double rotZ = 0.0;

    /**
     * Axis can be found on a diagram on the front
     * of the roborio
     * 
     * At the current state of this library
     */
    public IMU() {

        imu = new ADIS16470_IMU();

    }

    /**
     * @return Rotational degrees (-1.0 to 1.0) around X axis
     */
    public double getXRotation() {

        //return imu.getAccelInstantY();

        rotX = Math.abs(imu.getAccelInstantY());

        //imu.reset();

        return imu.getYFilteredAccelAngle();

    }

    /**
     * @return Rotational degrees (-1.0 to 1.0) around Y axis
     */
    public double getYRotation() {

        //return imu.getAccelInstantX();

        //imu.setYawAxis(IMUAxis.kY);

        return imu.getXFilteredAccelAngle();

    }
/*
    /**
     * @return Degrees (-1.0 to 1.0) from Z to XY plane of IMU
     
    public double getAngleFromZPlane() {

        return imu.getAccelInstantZ();

    }
*/
    public double getZRotation() {

        //return imu.getAngle();

        //imu.setYawAxis(IMUAxis.kZ);

        return imu.getAngle();

    }

    /**
     * @return acceleration of rotation on X axis
     */
    public double getXRotationalAcceleration() {

        return imu.getGyroInstantX();

    }

    /**
     * @return acceleration of rotation on Y axis
     */
    public double getYRotationalAcceleration() {

        return imu.getGyroInstantY();

    }

    /**
     * @return acceleration of rotation on Z axis
     */
    public double getZRotationalAcceleration() {

        return imu.getGyroInstantZ();

    }

}