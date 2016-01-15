package uc.jarvis;

import android.app.Service;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import java.util.Calendar;


public class AccelerometerService extends Service implements SensorEventListener {

    private long lastUpdateTime = System.currentTimeMillis();
    private int update_interval = 5;


    private float lastX = 0;
    private float lastY = 0;
    private float lastZ = 0;

    private static final float gravity = 9.80665F;
    private static final float movement_threshold = 0.04f;

    private SensorManager sm;
    private IBinder mBinder = new AccelerometerBinder();


    @Override
    public IBinder onBind(Intent arg0) {
        return mBinder;
    }

    @Override
    public void onCreate(){
        sm = (SensorManager) getSystemService(SENSOR_SERVICE);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            long currentTime = System.currentTimeMillis();

            if ((currentTime - lastUpdateTime) >= update_interval) {
                lastUpdateTime = currentTime;

                if (movementThreshold(event.values)) {
                    Log.i("UPDATE", "Accelerometer sensor value X: " + event.values[0]);
                    PostSensorData(event);
                }
            }
        }
    }

    @Override
    public void onDestroy() {
        sm.unregisterListener(this);
    }

    private void PostSensorData(SensorEvent event){
        String postString = String.format("timestamp=%s&sensorId=%s&sensorType=%s&value=%s",
                Long.toString(Calendar.getInstance().getTime().getTime()),
                "005",
                "Accelerometer",
                Float.toString(event.values[0]),
                Float.toString(event.values[1]),
                Float.toString(event.values[2]));

//        new PostSensorDataTask().execute(postString);
    }

    private boolean movementThreshold(float[] values){
        if( (Math.abs(values[0]) -lastX) > movement_threshold ||
                (Math.abs(values[1]) -lastY) > movement_threshold ||
                (Math.abs(values[2]) -lastZ) > movement_threshold){
            return true;
        }
        return false;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    public class AccelerometerBinder extends Binder {
        AccelerometerService getService() {
            return AccelerometerService.this;
        }
    }
}
