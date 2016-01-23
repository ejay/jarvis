package uc.jarvis.Sleep;

import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;

/**
 * Created by ejay on 23/01/16.
 */
public class SleepTrackBroadcastReceiver extends WakefulBroadcastReceiver{
    @Override
    public void onReceive(Context context, Intent intent){
        Intent service = new Intent(context, SleepTrackService.class);
        Log.i("SleepTrackBroadcast", "Starting service @ " + SystemClock.elapsedRealtime());
        startWakefulService(context, service);
    }

}
