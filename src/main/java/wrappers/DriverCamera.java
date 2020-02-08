package wrappers;

import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.cameraserver.CameraServer;

public class DriverCamera {

    UsbCamera cam;

    boolean constructed = false;

    public DriverCamera() {

        try{

            cam = CameraServer.getInstance().startAutomaticCapture();
            cam.setResolution(240, 160);
            cam.setFPS(30);
            cam.setExposureManual(50);

            constructed = true;

        } catch (Exception exception) {

            System.err.println("Camera Not Found");

        }

    }

    public void retryConstructor() {

        if(constructed) {

            return;

        }

        try{

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