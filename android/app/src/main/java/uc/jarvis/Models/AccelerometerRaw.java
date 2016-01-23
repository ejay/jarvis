package uc.jarvis.Models;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

/**
 * Model for database usage contains raw data of accelerometer data
 */
public class AccelerometerRaw{
    public long timestamp;
    public double x;
    public double y;
    public double z;


    /*
     * time smoothing constant for low-pass filter
     * 0 ≤ alpha ≤ 1 ; a smaller value basically means more smoothing
     * See: http://en.wikipedia.org/wiki/Low-pass_filter#Discrete-time_realization
     */
    static final float ALPHA = 0.15f;

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
    public void setY(long y){ this.y = y; }
    public void setZ(long z){
        this.z = z;
    }

    /**
     * Lowpass filter for raw accelerometer sensor data
     * @see http://en.wikipedia.org/wiki/Low-pass_filter#Algorithmic_implementation
     * @see http://developer.android.com/reference/android/hardware/SensorEvent.html#values
     * @param newValues values being filtered
     * @param oldValues last set of smoothed values
     * @return
     */
    protected float[] lowPass(float[] newValues, float[] oldValues){
        if ( oldValues == null ) return newValues;

        for ( int i=0; i<newValues.length; i++ ) {
            oldValues[i] = oldValues[i] + ALPHA * (newValues[i] - oldValues[i]);
        }
        return oldValues;
    }

    public String toString(){
        return "t="+timestamp+", x="+x+", y="+y+", z="+z;
    }
    public String toCSV(){
        return timestamp+","+x+","+y+","+z;
    }
}
