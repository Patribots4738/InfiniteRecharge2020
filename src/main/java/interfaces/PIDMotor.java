package interfaces;

public interface PIDMotor extends Motor{

    //@param p/i/d: internal PID control strength values
    public void setP(double p);
    public void setI(double i);
    public void setD(double d);
    
    public void setPosition(double rotations, double minSpeed, double maxSpeed);
    public double getPosition();
    public double getSpeed();
    public void resetEncoder();

}