package uc.jarvis;

import android.app.Activity;
import android.app.KeyguardManager;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.Vibrator;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class AccelerometerActivity extends AppCompatActivity implements SensorEventListener {

    private SensorManager sm;
    private Sensor accelerometer;
    private Sensor light;

    private ChartTimer timerTask;

    private TextView acceleration;
    private TextView currentX, currentY, currentZ;

//    private Line

    private float deltaX = 0;
    private float deltaY = 0;
    private float deltaZ = 0;
    private float lastX = 0;
    private float lastY = 0;
    private float lastZ = 0;

    private static final float gravity = 9.80665F;
    private static final float movement_threshold = 0.4f;
    private static final int update_interval = 5; //5ms interval;

    private long lastUpdateTime = System.currentTimeMillis();

    private int movementCounter = 0;

    private LineChart lineChart;
    private LineData lineData;
    private Timer timer;

    public Vibrator v;
    private float vibrateThreshold = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accelerometer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // initialize chart
        createChart();
        timerTask = new ChartTimer();

        // disable screen lock
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);

        // dim screen
        WindowManager.LayoutParams WMLP = getWindow().getAttributes();
        WMLP.screenBrightness = 0.15F;
        getWindow().setAttributes(WMLP);

        // initalize sensing
        sm = (SensorManager) getSystemService(SENSOR_SERVICE);
        accelerometer = sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        // initialize views
//        currentX = (TextView) findViewById(R.id.currentX);
//        currentY = (TextView) findViewById(R.id.currentY);
//        currentZ = (TextView) findViewById(R.id.currentZ);

//        sm.registerListener(mListener, accelerometer, 1 *1000000 /* 1 second */, 10 *1000000 /* 10 seconds */);
        sm.registerListener(mListener, accelerometer, update_interval, update_interval);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    protected void createChart(){

        lineData = new LineData(
                SleepData.getInstance().getLabelsAsArrayList(),
                SleepData.getInstance().getDataSet()
        );

        lineChart = new LineChart(getApplicationContext());
        lineChart.setData(lineData);
//        lineChart.setVisibleXRangeMaximum(120);
        lineChart.setVisibleYRangeMaximum(50, YAxis.AxisDependency.LEFT);
        lineChart.setMaxVisibleValueCount(10);


    }

    protected void onResume(){
        super.onResume();
        sm.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    protected void onPause(){
        super.onPause();
        sm.unregisterListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_accelerometer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void displayValues(){
        currentX.setText(Float.toString(deltaX));
//        currentY.setText(Float.toString(deltaY));
//        currentZ.setText(Float.toString(deltaZ));
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
//
//        long currentTime = System.currentTimeMillis();
//
//        if ((currentTime - lastUpdateTime) >= update_interval) {
//
//            lastUpdateTime = currentTime;
//
//            if (movementThreshold(event.values)) {
//
//
//
//                if (movementThreshold(event.values)) {
////                    Log.i("UPDATE", "Accelerometer sensor value: " + event.values);
//                    // post sensordata to raspberry
//                    // should probably only do states of the phone
//                    // e.g. sleep awake rem-sleep or something
//                    PostSensorData(event);
//                    movementCounter++;
//                    Log.i("UPDATE", "Movement total "+ movementCounter);
//
//                }
//            }
//        }
//
//        // store last values
//        lastX = event.values[0];
//        lastY = event.values[1];
//        lastZ = event.values[2];
    }

    /**
     * Detect if accelerometer movement is above predefined threshold
     * @param values
     * @return
     */
    private boolean movementThreshold(float[] values){
        if( Math.abs(values[0] - lastX) > movement_threshold ||
            Math.abs(values[1] - lastY) > movement_threshold ||
            Math.abs(values[2] - lastZ) > movement_threshold ){
            return true;
        }

        return false;
    }

    private void PostSensorData(SensorEvent event){
        String postString = String.format("timestamp=%s&sensorId=%s&sensorType=%s&value=%s",
                Long.toString(Calendar.getInstance().getTime().getTime()),
                "005",
                "Accelerometer",
                Float.toString(event.values[0]) + ',' +
                        Float.toString(event.values[1]) + ',' +
                        Float.toString(event.values[2]));

        Log.i("UPDATE", postString);
//        new PostSensorDataTask().execute(postString);
    }

    private void test(SensorEvent event){
        // extract accelerometer values
        deltaX = Math.abs(lastX - event.values[0]);
        deltaY = Math.abs(lastY - event.values[1]);
        deltaZ = Math.abs(lastZ - event.values[2]);

        // absolute acceleration in m/s^2
//        float absX = deltaX * gravity;
//        float absY = deltaY * gravity;
//        float absZ = (deltaZ + 1.0f) * gravity;

        displayValues(); // display accelerometer values
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }



    class ChartTimer extends TimerTask {

        @Override
        public void run() {
            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
            final String time = sdf.format(calendar.getTime());

            SleepData.getInstance().addEntry(time, movementCounter);
            System.out.println("Entry added: " + time + ", " + movementCounter);
            lineChart.setData(
                    new LineData(
                            SleepData.getInstance().getLabelsAsArrayList(),
                            SleepData.getInstance().getDataSet()
                    )
            );
            lineChart.getLineData().getDataSetByIndex(0).setDrawFilled(true);
            lineChart.getLineData().getDataSetByIndex(0).setFillAlpha(127);
            lineChart.getAxisLeft().setAxisMaxValue(update_interval/50);
            lineChart.getAxisLeft().setAxisMinValue(0.0f);
            lineChart.getAxisRight().setAxisMaxValue(update_interval/50);
            lineChart.getAxisRight().setAxisMinValue(0.0f);
            lineChart.getAxisLeft().setDrawGridLines(false);
            lineChart.getAxisRight().setDrawGridLines(false);
            lineChart.getXAxis().setDrawGridLines(false);
            lineChart.getLineData().getDataSetByIndex(0).setDrawCircles(false);
            lineChart.getLineData().setDrawValues(false);


        }
    }

    private final SensorEventListener mListener = new SensorEventListener() {

        @Override
        public void onSensorChanged(SensorEvent event) {

            long currentTime = System.currentTimeMillis();

            if ((currentTime - lastUpdateTime) >= update_interval) {

                lastUpdateTime = currentTime;

                if (movementThreshold(event.values)) {



                    if (movementThreshold(event.values)) {
//                    Log.i("UPDATE", "Accelerometer sensor value: " + event.values);
                        // post sensordata to raspberry
                        // should probably only do states of the phone
                        // e.g. sleep awake rem-sleep or something
                        PostSensorData(event);
                        movementCounter++;
                        Log.i("UPDATE", "Movement total "+ movementCounter);

                    }
                }
            }

            // store last values
            lastX = event.values[0];
            lastY = event.values[1];
            lastZ = event.values[2];
        }

        @Override
        public void onAccuracyChanged(Sensor s, int accuracy) {

        }
    };
}
