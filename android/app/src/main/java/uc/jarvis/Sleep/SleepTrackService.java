package uc.jarvis.Sleep;


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Binder;
import android.os.IBinder;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;
import java.net.InetAddress;
import org.sleeper.propclasses.com_manager.clComManager;
import org.sleeper.propclasses.dataprocessor_manager.clDataProcessor;

import uc.jarvis.DataProcessor.SleepClassifierDataProcessor;
import uc.jarvis.PostSensorDataTask;

public class SleepTrackService extends Service implements clDataProcessor.clSleepStageClassifier.ISleepStateListener {
    private clDataProcessor DataProcessor;
    private Context AttachedContext = null;
    private WakeLock wakelock = null;
    private AlarmManager alarmManager = null;
    private WifiManager wifiManager = null;
    private PendingIntent alarmIntent = null;
    private SleepTrackService.WifiReceiver wifiReceiver = null;
    private SleepTrackService.AlarmReceiver alarmReceiver = null;
    private boolean isRunning = false;
    public static final String APP_ACTION_ALARM_TRIGGERED = "APP_ACTION_ALARM_TRIGGERED";
    private long monitorDuration = 30000000; // runs for 8,3 hours

    clDataProcessor.clSleepStageClassifier sleepStageClassifier=null ;

    public SleepTrackService(){
        clDataProcessor sleepClassifierDataProcessor = new SleepClassifierDataProcessor(this);
        this.AttachedContext = getApplicationContext();
        this.initializeApp();
    }

    public SleepTrackService(Context context, clDataProcessor _DataProcessor) {
        this.DataProcessor = _DataProcessor;
        this.AttachedContext = context;
        this.initializeApp();
    }

    private void initializeApp() {
        PowerManager powerManager = (PowerManager)this.AttachedContext.getSystemService("power");
        this.wakelock = powerManager.newWakeLock(1, "MyWakelockTag");
        this.alarmManager = (AlarmManager)this.AttachedContext.getSystemService("alarm");
        this.wifiManager = (WifiManager)this.AttachedContext.getSystemService("wifi");
    }

    public void startSleepMode(long ringTimeMillis) {
        if(!this.isRunning) {
            if(!this.wakelock.isHeld()) {
                this.wakelock.acquire();
            }

            this.alarmReceiver = new SleepTrackService.AlarmReceiver();
            IntentFilter filter = new IntentFilter("APP_ACTION_ALARM_TRIGGERED");
            this.AttachedContext.registerReceiver(this.alarmReceiver, filter);
            Intent intent = new Intent("APP_ACTION_ALARM_TRIGGERED");
            this.alarmIntent = PendingIntent.getBroadcast(this.AttachedContext, 0, intent, 268435456);
            this.alarmManager.set(0, System.currentTimeMillis() + ringTimeMillis, this.alarmIntent);
            this.DataProcessor.measureStart();
            this.isRunning = true;
        }

    }

    public void stopSleepMode() {
        if(this.isRunning) {
            this.DataProcessor.measureStop();
            this.AttachedContext.unregisterReceiver(this.alarmReceiver);
            if(this.wakelock.isHeld()) {
                this.wakelock.release();
            }

            this.alarmManager.cancel(this.alarmIntent);
            this.isRunning = false;
        }

    }

    public void onResume() {
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.net.wifi.STATE_CHANGE");
        this.wifiReceiver = new SleepTrackService.WifiReceiver();
        this.AttachedContext.registerReceiver(this.wifiReceiver, filter);
    }

    public void onPause() {
        this.AttachedContext.unregisterReceiver(this.wifiReceiver);
    }

    public void onDestroy() {
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    public int onStartCommand(Intent intent, int flags, int startId){
        this.startSleepMode(monitorDuration);
        Log.i("SleepTrackService ", "Starting sleep tracking for: "+ monitorDuration +"ms");
        return Service.START_STICKY;
    }

    public clDataProcessor getDataProcessor() {
        return this.DataProcessor;
    }

    /** @deprecated */
    @Deprecated
    private boolean isConnectedViaWifi() {
        ConnectivityManager connectivityManager = (ConnectivityManager)this.AttachedContext.getSystemService("connectivity");
        NetworkInfo mWifi = connectivityManager.getNetworkInfo(1);
        return mWifi.isConnected();
    }

//    @Override
//    public void run() {
//        android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_BACKGROUND);
//        this.startSleepMode(monitorDuration);
//    }

    public void setTime(long monitorDuration){
        this.monitorDuration = monitorDuration;
    }

    @Override
    public void onSleepStateRetrievedEvent(int sleepState, Double[] doubles) {
        Log.i("UPDATE", "Light sensor value: " + sleepState);

        String postString = String.format("key=%s&value=%s",
                "CurrentSleepCycleUser_raw",
                sleepState);

        new PostSensorDataTask().execute(postString);

        switch(sleepState){
            case clDataProcessor.clSleepStageClassifier.AWAKE :
                Log.i("SleepState: ", "AWAKE") ;
                Toast.makeText(AttachedContext, "AWAKE", Toast.LENGTH_SHORT).show() ;
                break ;
            case clDataProcessor.clSleepStageClassifier.DEEP:
                Log.i("SleepState: ","SLEEP") ;
                Toast.makeText(AttachedContext,"SLEEP",Toast.LENGTH_SHORT).show() ;
                break ;
            case clDataProcessor.clSleepStageClassifier.REM:
                Log.i("SleepState: ","REM") ;
                Toast.makeText(AttachedContext,"REM",Toast.LENGTH_SHORT).show() ;
                break ;
        }

        Log.i("SleepState: ",""+sleepState) ;
    }

    private class WifiReceiver extends BroadcastReceiver {
        public WifiReceiver() {
        }

        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals("android.net.wifi.STATE_CHANGE")) {
                this.SetIp();
            }

            if(intent.getAction().equals(SleepTrackService.this.wifiManager.NETWORK_STATE_CHANGED_ACTION)){
                NetworkInfo netInfo = intent.getParcelableExtra (WifiManager.EXTRA_NETWORK_INFO);
                if (ConnectivityManager.TYPE_WIFI == netInfo.getType ()) {
                    if(netInfo.isConnected()){
                        WifiInfo info = wifiManager.getConnectionInfo ();
                        String ssid = info.getBSSID();

                        String postString = String.format("key=%s&value=%s",
                                "WifiFingerprint_raw",
                                ssid);

                        Log.i("NEW WIFI",postString);

                        new PostSensorDataTask().execute(postString);
                    }

                }
            }

        }

        private void SetIp() {
            SleepTrackService.this.wifiManager = (WifiManager)SleepTrackService.this.AttachedContext.getSystemService("wifi");
            WifiInfo wifiInfo = SleepTrackService.this.wifiManager.getConnectionInfo();
            int ipAddress = wifiInfo.getIpAddress();
            byte firstIpGroup = (byte)ipAddress;
            byte secondIpGroup = (byte)(ipAddress >> 8);
            byte thirdIpGroup = (byte)(ipAddress >> 16);
            byte fourthIpGroup = 1;
            byte[] ipAddr = new byte[]{firstIpGroup, secondIpGroup, thirdIpGroup, fourthIpGroup};

            try {
                clComManager.setIpAddr(InetAddress.getByAddress(ipAddr));
            } catch (Exception var9) {
                Log.e(this.toString(), var9.getMessage());
            }

        }
    }

    private class AlarmReceiver extends BroadcastReceiver {
        public AlarmReceiver() {
        }

        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals("APP_ACTION_ALARM_TRIGGERED")) {
                SleepTrackService.this.stopSleepMode();
                Toast.makeText(context, "Alarm Rang!!", 0).show();
            }

        }
    }

    public class SleepTrackBinder extends Binder {
        public SleepTrackService getService(){
            return SleepTrackService.this;
        }
    }
}