package uc.jarvis.DataProcessor;

/**
 * Created by ejay on 19/01/16.
 */

import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;

/**
 *
 */
public class DataProcessingReceiver extends WakefulBroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent){
        Intent service = new Intent(context, DataProcessingService.class);
        Log.i("DataProcessingReceiver", "Starting service @ " + SystemClock.elapsedRealtime());
        startWakefulService(context, service);
    }

}
