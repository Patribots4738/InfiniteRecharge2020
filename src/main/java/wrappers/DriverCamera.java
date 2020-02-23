package wrappers;

import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.cameraserver.CameraServer;

public class DriverCamera {

    UsbCamera cam;

    boolean constructed = false;

    public DriverCamera() {

        try {

            cam = CameraServer.getInstance().startAutomaticCapture();
            cam.setResolution(240, 160);
            cam.setFPS(30);
            cam.setExposureManual(50);

            constructed = true;

        } catch (Exception e) {

            System.err.println("Camera Not Found");

        }

    }

    public void retryConnection() {

        try {

            cam.setExposureManual(51);

        } catch(Exception e) {

            constructed = false;

        }

        if(constructed) {

            return;

        }

        try {

            cam = CameraServer.getInstance().startAutomaticCapture();
            cam.setResolution(240, 160);
            cam.setFPS(30);
            cam.setExposureManual(50);

            constructed = true;

        } catch (Exception exception) {

            System.err.println("Camera Not Found");

        }

    }

}