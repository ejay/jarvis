package uc.jarvis;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.util.Calendar;

public class LightSensorActivity extends AppCompatActivity implements SensorEventListener{
    private SensorManager sensorManager;
    private Sensor light;
    private double lastLightValue;

    private static final int UPDATE_INTERVAL_LIGHT = 5*100; //5ms interval;
    private static final int LIGHT_THRESHOLD = 100;
    private HandlerThread mSensorThread;
    private Handler mSensorHandler;

    @Override
    public final void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Get an instance of the sensor service, and use that to get an instance of
        // a particular sensor.
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        light = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);

        mSensorThread = new HandlerThread("Sensor processing thread", Thread.MAX_PRIORITY);
        mSensorThread.start();
        mSensorHandler = new Handler(mSensorThread.getLooper());
        sensorManager.registerListener(mListener, light, UPDATE_INTERVAL_LIGHT, UPDATE_INTERVAL_LIGHT, mSensorHandler);
    }

    @Override
    protected void onResume() {
        // Register a listener for the sensor.
        super.onResume();
        sensorManager.registerListener(this, light, SensorManager.SENSOR_DELAY_NORMAL);
    }


    @Override
    public void onSensorChanged(SensorEvent event) {
//        Log.i("UPDATE", "Light sensor value: " + event.values[0]);
//
//        String postString = String.format("timestamp=%s&sensorId=%s&sensorType=%s&value=%s",
//                Long.toString(Calendar.getInstance().getTime().getTime()),
//                "001",
//                "Light",
//                Float.toString(event.values[0]));

//        new PostSensorDataTask().execute(postString);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }


    private final SensorEventListener mListener = new SensorEventListener() {

        @Override
        public void onSensorChanged(SensorEvent event) {
            int sensorType = event.sensor.getType();

            switch (sensorType){
                case Sensor.TYPE_LIGHT:
                    processLightEvent(event);
            }
        }

        protected void processLightEvent(SensorEvent event){
            if (Math.abs(lastLightValue - event.values[0]) >= LIGHT_THRESHOLD) {
                Log.i("UPDATE", "Light sensor value: " + event.values[0]);

                String postString = String.format("key=%s&value=%s",
                        "RoomLight_raw",
                        Float.toString(event.values[0]));

                lastLightValue = event.values[0];

                new PostSensorDataTask().execute(postString);
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    };
}
