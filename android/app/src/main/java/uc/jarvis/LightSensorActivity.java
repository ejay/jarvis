package uc.jarvis;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.util.Calendar;

public class LightSensorActivity extends AppCompatActivity implements SensorEventListener{
    private SensorManager sensorManager;
    private Sensor light;

    @Override
    public final void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Get an instance of the sensor service, and use that to get an instance of
        // a particular sensor.
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        light = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
    }

    @Override
    protected void onResume() {
        // Register a listener for the sensor.
        super.onResume();
        sensorManager.registerListener(this, light, SensorManager.SENSOR_DELAY_NORMAL);
    }


    @Override
    public void onSensorChanged(SensorEvent event) {
        Log.i("UPDATE", "Light sensor value: " + event.values[0]);

        String postString = String.format("timestamp=%s&sensorId=%s&sensorType=%s&value=%s",
                Long.toString(Calendar.getInstance().getTime().getTime()),
                "001",
                "Light",
                Float.toString(event.values[0]));

        new PostSensorDataTask().execute(postString);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
