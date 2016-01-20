package uc.jarvis;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

public class AccelerometerData implements Parcelable{

    private long timestamp;
    private double x;
    private double y;
    private double z;

    /*
     * time smoothing constant for low-pass filter
     * 0 ≤ alpha ≤ 1 ; a smaller value basically means more smoothing
     * See: http://en.wikipedia.org/wiki/Low-pass_filter#Discrete-time_realization
     */
    static final float ALPHA = 0.15f;

    public AccelerometerData(Parcel source){
        Log.v("AccelerometerData", "ParcelData(Parcel source): time to put back parcel data");
        timestamp = source.readLong();
        x = source.readDouble();
        y = source.readDouble();
        z = source.readDouble();
    }

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        Log.v("AccelerometerData", "writeToParcel..." + flags);

        dest.writeLong(timestamp);
        dest.writeDouble(x);
        dest.writeDouble(y);
        dest.writeDouble(z);
    }

    /**
     * It will be required during un-marshaling data stored in a Parcel
     * @author prasanta
     */
    public class AcDataCreator implements Parcelable.Creator<AccelerometerData> {
        public AccelerometerData createFromParcel(Parcel source) {
            return new AccelerometerData(source);
        }
        public AccelerometerData[] newArray(int size) {
            return new AccelerometerData[size];
        }
    }
}
