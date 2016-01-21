package uc.jarvis;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.LineData;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicInteger;

import uc.jarvis.DataProcessor.DataProcessingReceiver;
import uc.jarvis.DataProcessor.DatabaseHandler;

public class AccelerometerActivity extends AppCompatActivity implements SensorEventListener {

    private SensorManager sm;
    private Sensor accelerometer;

    private ArrayList<AccelerometerData> sensorHistory;

    private ChartTimer timerTask;

    private TextView acceleration;
    private TextView currentX, currentY, currentZ;
    private LineChart lineChart;
    private LineData lineData;

//    private Line

    private float deltaX = 0;
    private float deltaY = 0;
    private float deltaZ = 0;
    private float lastX = 0;
    private float lastY = 0;
    private float lastZ = 0;

    private static final float gravity = 9.80665F;
    private static final float movement_threshold = 0.4f;
    private static final int UPDATE_INTERVAL = 5; //5ms interval;
    private long lastUpdateTime = System.currentTimeMillis();

    private final AtomicInteger movementCounter = new AtomicInteger();

    private HandlerThread mSensorThread;
    private Handler mSensorHandler;

    DatabaseHandler dbHandler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        super.onCreate();
        setContentView(R.layout.activity_accelerometer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // initalize sensing
        sm = (SensorManager) getSystemService(SENSOR_SERVICE);
        accelerometer = sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorHistory = new ArrayList();
        initViews();

        dbHandler = DatabaseHandler.getInstance(getBaseContext());

        // background dataprocessing every 5 minutes
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        Intent dataProcessing = new Intent(AccelerometerActivity.this, DataProcessingReceiver.class);
//        dataProcessing.putExtra("sensorHistory", sensorHistory);
        dataProcessing.putParcelableArrayListExtra("sensorHistory", sensorHistory);
        PendingIntent dataProcessingIntent  = PendingIntent.getBroadcast(AccelerometerActivity.this, 0, dataProcessing, 0);
        alarmManager.setInexactRepeating(AlarmManager.RTC, 5 * 1000, 5 * 1000, dataProcessingIntent);
//        alarmManager.set(AlarmManager.RTC, 1, dataProcessingIntent);
//        alarmManager.set(AlarmManager.RTC, 60*1000*5, 60*1000*5, dataProcessingIntent);
//        alarmManager.setInexactRepeating(AlarmManager.RTC, 60*1000*5, 60*1000*5, dataProcessingIntent);


        mSensorThread = new HandlerThread("Sensor processing thread", Thread.MAX_PRIORITY);
        mSensorThread.start();
        mSensorHandler = new Handler(mSensorThread.getLooper());
        sm.registerListener(mListener, accelerometer, UPDATE_INTERVAL, UPDATE_INTERVAL, mSensorHandler);



//        // create database and table
//        db=openOrCreateDatabase("SensorData", Context.MODE_PRIVATE, null);
//        db.execSQL("CREATE TABLE IF NOT EXISTS AccelerometerData(timestamp LONG,name VARCHAR,marks VARCHAR);");
//        db.execSQL("CREATE TABLE IF NOT EXISTS ProcessedFeature(_id INTEGER primary key AUTOINCREMENT, avgX DOUBLE, avgY DOUBLE, avgZ DOUBLE, minX DOUBLE, minY DOUBLE, minZ DOUBLE, maxX DOUBLE, maxY DOUBLE, maxZ DOUBLE, rmsX DOUBLE, rmsY DOUBLE, rmsZ DOUBLE);");

//        tablo2="CREATE TABLE ProcessedFeature (_id INTEGER primary key AUTOINCREMENT,ortX REAL,ortY REAL,ortZ REAL,stdX REAL,stdY REAL,stdZ REAL,maxX REAL,maxY REAL,maxZ REAL,aadX REAL,aadY REAL,aadZ REAL,averageResultantAcc REAL,binX1 INTEGER,binX2 INTEGER,binX3 INTEGER,binX4 INTEGER,binX5 INTEGER,binX6 INTEGER,binX7 INTEGER,binX8 INTEGER,binX9 INTEGER,binX10 INTEGER,binY1 INTEGER,binY2 INTEGER,binY3 INTEGER,binY4 INTEGER,binY5 INTEGER,binY6 INTEGER,binY7 INTEGER,binY8 INTEGER,binY9 INTEGER,binY10 INTEGER,binZ1 INTEGER,binZ2 INTEGER,binZ3 INTEGER,binZ4 INTEGER,binZ5 INTEGER,binZ6 INTEGER,binZ7 INTEGER,binZ8 INTEGER,binZ9 INTEGER,binZ10 INTEGER);";


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        //        IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
//        Intent batteryStatus = context.registerReceiver(null, ifilter);
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
        currentY.setText(Float.toString(deltaY));
        currentZ.setText(Float.toString(deltaZ));
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
       displayValues();
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

//    private void PostSensorData(SensorEvent event){
//        String postString = String.format("timestamp=%s&sensorId=%s&sensorType=%s&value=%s",
//                Long.toString(Calendar.getInstance().getTime().getTime()),
//                "005",
//                "Accelerometer",
//                Float.toString(event.values[0]) + ',' +
//                        Float.toString(event.values[1]) + ',' +
//                        Float.toString(event.values[2]));
//
//        Log.i("UPDATE", postString);
////        new PostSensorDataTask().execute(postString);
//    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }


    public void initViews(){
        // initialize views
        currentX = (TextView) findViewById(R.id.currentX);
        currentY = (TextView) findViewById(R.id.currentY);
        currentZ = (TextView) findViewById(R.id.currentZ);
    }


    class ChartTimer extends TimerTask {

        @Override
        public void run() {
            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
            final String time = sdf.format(calendar.getTime());

            SleepData.getInstance().addEntry(time, movementCounter.get());
            System.out.println("Entry added: " + time + ", " + movementCounter);
            lineChart.setData(
                    new LineData(
                            SleepData.getInstance().getLabelsAsArrayList(),
                            SleepData.getInstance().getDataSet()
                    )
            );
            lineChart.getLineData().getDataSetByIndex(0).setDrawFilled(true);
            lineChart.getLineData().getDataSetByIndex(0).setFillAlpha(127);
            lineChart.getAxisLeft().setAxisMaxValue(UPDATE_INTERVAL/50);
            lineChart.getAxisLeft().setAxisMinValue(0.0f);
            lineChart.getAxisRight().setAxisMaxValue(UPDATE_INTERVAL/50);
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

            int sensorType = event.sensor.getType();

            switch (sensorType){
                case Sensor.TYPE_ACCELEROMETER:
                    processAccelerometerEvent(event);
                    break;
            }
        }

        protected void processAccelerometerEvent(SensorEvent event){
            long currentTime = System.currentTimeMillis();

            if ((currentTime - lastUpdateTime) >= UPDATE_INTERVAL) {
                lastUpdateTime = currentTime;

                if (movementThreshold(event.values)) {
                    if (movementThreshold(event.values)) {
//                    Log.i("UPDATE", "Accelerometer sensor value: " + event.values);
                        // post sensordata to raspberry
                        // should probably only do states of the phone
                        // e.g. sleep awake rem-sleep or something
//                        writeDataToCSV(event);

                        // store sensordata
                        long timestamp = System.currentTimeMillis();
                        double x = event.values[0];
                        double y = event.values[1];
                        double z = event.values[2];
                        AccelerometerData data = new AccelerometerData(timestamp,x,y,z);

                        dbHandler.addRawData(data);

                        sensorHistory.add(data);
//                        PostSensorData(event);
                        movementCounter.incrementAndGet();
                        Log.i("UPDATE", "Movement total "+ movementCounter);
                    }
                }
            }

            // store last values
            lastX = event.values[0];
            lastY = event.values[1];
            lastZ = event.values[2];
        }

        protected void writeDataToCSV(SensorEvent event){
            // store data to csv
//            Calendar calendar = Calendar.getInstance();
//            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
//            final String time = sdf.format(calendar.getTime());
//            try {
//                writer.write(time+","+event.values[0]+","+event.values[1]+","+event.values[2]+"\n");
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
        }

        @Override
        public void onAccuracyChanged(Sensor s, int accuracy) {}

        protected void dimScreen(){
            // disable screen lock
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);

            // dim screen
            WindowManager.LayoutParams WMLP = getWindow().getAttributes();
            WMLP.screenBrightness = 0.15F;
            getWindow().setAttributes(WMLP);
        }

        protected void createChart(){
            timerTask = new ChartTimer();

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
    };

}
