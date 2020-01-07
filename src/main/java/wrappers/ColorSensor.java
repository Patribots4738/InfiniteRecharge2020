package wrappers;

import edu.wpi.first.wpilibj.I2C;
import com.revrobotics.ColorSensorV3;
import edu.wpi.first.wpilibj.util.Color;
import utils.*;

import com.revrobotics.ColorSensorV3.RawColor;

public class ColorSensor{

    I2C.Port port;

    ColorSensorV3 colorSensor;

    public ColorSensor () {

        port = I2C.Port.kOnboard;

        colorSensor = new ColorSensorV3(port);

    }

    public Color getColor() {
        
        return colorSensor.getColor();
        
    }

    public ColorSensorV3.RawColor getRawColor(){

        return colorSensor.getRawColor();

    }

    public double[] getRGBIR(){

        RawColor color = colorSensor.getRawColor();

        double max = Math.max(color.ir, Math.max(Math.max(color.red, color.blue), color.green));

        double r = ((color.red/max) * 255.0);
        double g = ((color.green/max) * 255.0);
        double b = ((color.blue/max) * 255.0);
        double ir =((color.ir/max) * 255.0);

        double[] rgbIR = {r, g, b, ir};

        return rgbIR;

    }

    public String determineColor() {

        double[] rgbIR = getRGBIR();

        if(isColor(rgbIR, "Yellow") == true) {
            //System.out.println("yellow");
            return "Yellow";

        } else if(isColor(rgbIR, "Red") == true) {
            //System.out.println("red");
            return "Red";

        } else if(isColor(rgbIR, "Green") == true) {
            //System.out.println("green");
            return "Green";

        } else if(isColor(rgbIR, "Blue") == true) {
            //System.out.println("blue");
            return "Blue";

        } else {

            return "N/A";

        }

    }

    public boolean isColor(double[] rgb, String color) {

            if(color.equals("Yellow")) {

                return isYellow(rgb);

            } else if(color.equals("Red")) {

                return isRed(rgb);

            } else if(color.equals("Green")) {

                return isGreen(rgb);

            } else if(color.equals("Blue")) {

                return isBlue(rgb);

            } else {

                return false;

            }

    }

    public boolean isYellow(double[] rgb) {

        if(rgb[0] > Constants.Color.Yellow.RMIN && rgb[0] < Constants.Color.Yellow.RMAX) {
            
            if(rgb[1] > Constants.Color.Yellow.GMIN && rgb[1] < Constants.Color.Yellow.GMAX) {
                
                if(rgb[2] > Constants.Color.Yellow.BMIN && rgb[2] < Constants.Color.Yellow.BMAX) {
                    
                    return true;

                }

            }

        }

        return false;

    } 

    public boolean isRed(double[] rgb) {

        if(rgb[0] > Constants.Color.Red.RMIN && rgb[0] < Constants.Color.Red.RMAX) {
            
            if(rgb[1] > Constants.Color.Red.GMIN && rgb[1] < Constants.Color.Red.GMAX) {

                if(rgb[2] > Constants.Color.Red.BMIN && rgb[2] < Constants.Color.Red.BMAX) {

                    return true;

                }

            }

        }

        return false;

    }

    public boolean isGreen(double[] rgb) {

        if(rgb[0] > Constants.Color.Green.RMIN && rgb[0] < Constants.Color.Green.RMAX) {
            
            if(rgb[1] > Constants.Color.Green.GMIN && rgb[1] < Constants.Color.Green.GMAX) {
                
                if(rgb[2] > Constants.Color.Green.BMIN && rgb[2] < Constants.Color.Green.BMAX) {
                    
                    return true;

                }

            }

        }

        return false;

    }

    public boolean isBlue(double[] rgb) {

        if(rgb[0] > Constants.Color.Blue.RMIN && rgb[0] < Constants.Color.Blue.RMAX) {

            if(rgb[1] > Constants.Color.Blue.GMIN && rgb[1] < Constants.Color.Blue.GMAX) {

                if(rgb[2] > Constants.Color.Blue.BMIN && rgb[2] < Constants.Color.Blue.BMAX) {

                    return true;

                }

            }

        }

        return false;

    }

}