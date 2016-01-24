package uc.jarvis.Activities;

import android.app.PendingIntent;
import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
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

import org.sleeper.propclasses.dataprocessor_manager.clDataProcessor;

import java.util.concurrent.atomic.AtomicInteger;

import uc.jarvis.AccelerometerData;
import uc.jarvis.CalendarActivity;
import uc.jarvis.DataProcessor.DatabaseHandler;
import uc.jarvis.DataProcessor.SleepClassifierDataProcessor;
import uc.jarvis.PostSensorDataTask;
import uc.jarvis.R;
import uc.jarvis.Sleep.SleepTrackService;
import uc.jarvis.Sleep.SleepTrackBroadcastReceiver;

public class AccelerometerActivity extends AppCompatActivity implements SensorEventListener {

    private SensorManager sm;
    private Sensor accelerometer;
    private Sensor light;
    private WifiManager wifiManager = null;

    private TextView currentX, currentY, currentZ;

    private float deltaX = 0;
    private float deltaY = 0;
    private float deltaZ = 0;
    private float lastX = 0;
    private float lastY = 0;
    private float lastZ = 0;

    private static final float gravity = 9.80665F;

    private static final int UPDATE_INTERVAL_ACCEL = 50; //50ms interval;
    private static final int UPDATE_INTERVAL_LIGHT = 5*100; //5ms interval;
    private long lastUpdateTime_ACCEL = System.currentTimeMillis();
    private long lastUpdateTime_LIGHT = System.currentTimeMillis();

    // Thresholds for changes in sensor values for updating latest value
    private static final int LIGHT_THRESHOLD = 10;
    private static final float MOVEMENT_THRESHOLD = 0.4f;

    private final AtomicInteger movementCounter = new AtomicInteger();

    private HandlerThread mSensorThread;
    private Handler mSensorHandler;
    private SleepTrackService sleepApp; // sleep classifier
    private Service m_service;

    DatabaseHandler dbHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        super.onCreate();
        setContentView(R.layout.activity_accelerometer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initViews();

        // initalize sensing
        sm = (SensorManager) getSystemService(SENSOR_SERVICE);
        wifiManager = (WifiManager) getSystemService(WIFI_SERVICE);

        // initialize database used for raw sensor data
//        dbHandler = DatabaseHandler.getInstance(getApplicationContext());

        initializeSensorListeners();
//        initSleepTracking();

        clDataProcessor sleepClassifierDataProcessor = new SleepClassifierDataProcessor(this);
        sleepApp = new SleepTrackService(this,sleepClassifierDataProcessor);
        sleepApp.startSleepMode(35000000); // start for 8,3+ hours


//        this.registerReceiver()
//        Intent sleepTracking = new Intent(this, SleepTrackBroadcastReceiver.class);
//        startService(sleepTracking);
//        bindService(sleepTracking, m_serviceConnection, BIND_AUTO_CREATE);
//        PendingIntent sleepTrackingIntent = PendingIntent.getBroadcast(AccelerometerActivity.this, 0, sleepTracking, 0);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startCalendar();
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        //        IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
//        Intent batteryStatus = context.registerReceiver(null, ifilter);
    }

    private ServiceConnection m_serviceConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className, IBinder service) {
            m_service = ((SleepTrackService.SleepTrackBinder)service).getService();
        }

        public void onServiceDisconnected(ComponentName className) {
            m_service = null;
        }
    };

    protected void startCalendar(){
        Intent intent = new Intent(this,CalendarActivity.class);
        startActivity(intent);
    }

    protected void initializeSensorListeners(){

        accelerometer = sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        light = sm.getDefaultSensor(Sensor.TYPE_LIGHT);

        mSensorThread = new HandlerThread("Sensor processing thread", Thread.MAX_PRIORITY);
        mSensorThread.start();
        mSensorHandler = new Handler(mSensorThread.getLooper());
//        sm.registerListener(mListener, accelerometer, UPDATE_INTERVAL_ACCEL, UPDATE_INTERVAL_ACCEL, mSensorHandler);
        sm.registerListener(mListener, light, UPDATE_INTERVAL_LIGHT, UPDATE_INTERVAL_LIGHT, mSensorHandler);
    }

    /**
     * Sleep data classifier
     */
    protected void initSleepTracking(){
//        clDataProcessor sleepClassifierDataProcessor = new SleepClassifierDataProcessor(this);
//        sleepApp = new SleepTrackService(this, sleepClassifierDataProcessor);
//        sleepApp.setTime(600000);
//        sleepApp.run();
//        Intent i = new Intent(this, SleepTrackService.class);
//        startService(i);

//        sleepApp = new SleepTrackService(this,sleepClassifierDataProcessor);
//        sleepApp.startSleepMode(60000*10); // start for 10 hours
    }


    protected void onResume(){
        super.onResume();

        sm.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        sm.registerListener(this, light, SensorManager.SENSOR_DELAY_NORMAL);

        Intent sleepTracking = new Intent(this, SleepTrackBroadcastReceiver.class);
        startService(sleepTracking);
        PendingIntent sleepTrackingIntent = PendingIntent.getBroadcast(AccelerometerActivity.this, 0, sleepTracking, 0);

//        sleepApp.startSleepMode(60000);
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
    /**
     * Currently only used for light sensor
     */
    public void onSensorChanged(SensorEvent event) {
    }

    /**
     * Detect if accelerometer movement is above predefined threshold
     * @param values
     * @return
     */
    private boolean movementThreshold(float[] values){
        if( Math.abs(values[0] - lastX) > MOVEMENT_THRESHOLD ||
            Math.abs(values[1] - lastY) > MOVEMENT_THRESHOLD ||
            Math.abs(values[2] - lastZ) > MOVEMENT_THRESHOLD ){
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


    private final SensorEventListener mListener = new SensorEventListener() {

        @Override
        public void onSensorChanged(SensorEvent event) {

            int sensorType = event.sensor.getType();

            switch (sensorType){
                case Sensor.TYPE_ACCELEROMETER:
                    processAccelerometerEvent(event);
                    break;
                case Sensor.TYPE_LIGHT:
                    processLightEvent(event);
            }
        }

        protected void processAccelerometerEvent(SensorEvent event){

            long currentTime = System.currentTimeMillis();

            if ((currentTime - lastUpdateTime_ACCEL) >= UPDATE_INTERVAL_ACCEL) {
                lastUpdateTime_ACCEL = currentTime;

                if (movementThreshold(event.values)) {
                    if (movementThreshold(event.values)) {
                        // store sensordata
                        long timestamp = System.currentTimeMillis();
                        double x = event.values[0];
                        double y = event.values[1];
                        double z = event.values[2];
                        AccelerometerData data = new AccelerometerData(timestamp,x,y,z);

//                        dbHandler.addRawData(data);

//                        movementCounter.incrementAndGet();
//                        Log.i("UPDATE", "Amount of movement recorded: "+ dbHandler.getRawData().size());
                    }
                }
            }

            // store last values
            lastX = event.values[0];
            lastY = event.values[1];
            lastZ = event.values[2];
        }

        private double lastLightValue = 0;

        protected void processLightEvent(SensorEvent event){

            long currentTime = System.currentTimeMillis();

//            if ((currentTime - lastUpdateTime_LIGHT) >= UPDATE_INTERVAL_LIGHT) {
            if (Math.abs(lastLightValue - event.values[0]) >= LIGHT_THRESHOLD) {
                lastUpdateTime_LIGHT = currentTime;
                Log.i("UPDATE", "Light sensor value: " + event.values[0]);

                String postString = String.format("key=%s&value=%s",
                        "BedPhoneLight_raw",
                        Float.toString(event.values[0]));

                lastLightValue = event.values[0];

                new PostSensorDataTask().execute(postString);
            }
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
    };

}
