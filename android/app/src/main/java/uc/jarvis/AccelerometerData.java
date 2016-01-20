package uc.jarvis;

public class AccelerometerData {

    private long timestamp;
    private double x;
    private double y;
    private double z;

    public AccelerometerData(long timestamp, double x, double y, double z){
        this.timestamp = timestamp;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public long getTimestamp(){
        return timestamp;
    }

    public double getX(){
        return x;
    }
    public double getY(){
        return y;
    }
    public double getZ(){
        return z;
    }

    public void setTimestamp(long timestamp){
        this.timestamp = timestamp;
    }

    public void setX(long x){
        this.x = x;
    }
    public void setY(long y){
        this.y = y;
    }
    public void setZ(long z){
        this.z = z;
    }

    public String toString(){
        return "t="+timestamp+", x="+x+", y="+y+", z="+z;
    }

    public String toCSV(){
        return timestamp+","+x+","+y+","+z;
    }
}
