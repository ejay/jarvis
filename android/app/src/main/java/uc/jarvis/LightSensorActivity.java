package uc.jarvis;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.goebl.david.Webb;

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

        Webb webb = Webb.create();
        webb.get("http://localhost/sensor-data")
                .param("timestamp", Long.toString(Calendar.getInstance().getTime().getTime()))
                .param("sensorId", "00001")
                .param("sensorType", "Light")
                .param("value", Float.toString(event.values[0]))
                .ensureSuccess()
                .asJsonObject()
                .getBody();
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
