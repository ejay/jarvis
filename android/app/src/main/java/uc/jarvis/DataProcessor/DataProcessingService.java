package uc.jarvis.DataProcessor;


import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import uc.jarvis.AccelerometerData;
import uc.jarvis.PostSensorDataTask;
import uc.jarvis.ProcessedSensorDataObject;

public class DataProcessingService extends IntentService {

    public DataProcessingService() {
        super("DataProcessingService");
    }

    @Override
    public void onCreate(){
        super.onCreate();
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        Bundle bundle = intent.getExtras();
//        ArrayList sensorHistory = bundle.getParcelableArrayList("sensorHistory");
        ArrayList sensorHistory = bundle.getParcelableArrayList("sensorHistory");

//        Log.i("DataProcessingService", "Completed service @ " + sensorHistory.toString());
        // process data
//        ProcessedSensorDataObject pdo = new ProcessedSensorDataObject();
//        pdo = processedData(sensorHistory, pdo);
//        PostSensorData(pdo);
        // clear history
//        sensorHistory = new ArrayList();

        Log.i("DataProcessingService", "Completed service @ " + SystemClock.elapsedRealtime());
        DataProcessingReceiver.completeWakefulIntent(intent);
    }


    private void PostSensorData(ProcessedSensorDataObject pdo){

        Gson gson = new Gson();
        String json = gson.toJson(pdo);
        Log.i("PostSensorData", json);

        new PostSensorDataTask().execute(json);
    }

    /**
     * calculate average + min & max
     * average, minimum, maximum and rms for each 5 minute window for each axis
     * @param sensorHistory
     * @param pdo
     * @return
     */
    private ProcessedSensorDataObject processedData(List<AccelerometerData> sensorHistory, ProcessedSensorDataObject pdo){

//        if(sensorHistory == null || sensorHistory.isEmpty()){
//            return pdo;
//        }

        double sumX, sumY, sumZ;
        sumX = sumY = sumZ = 0.0;
        double minX, minY, minZ;
        minX = minY = minZ = 0.0;
        double maxX, maxY, maxZ;
        maxX = maxY = maxZ = 0.0;
        double msX, msY, msZ;
        msX = msY = msZ = 0.0;


        for (AccelerometerData ah : sensorHistory){
            // avg
            sumX += ah.getX();
            sumY += ah.getY();
            sumZ += ah.getZ();

            // min
            if(ah.getX() < minX) minX = ah.getX();
            if(ah.getX() < minY) minY = ah.getY();
            if(ah.getX() < minZ) minZ = ah.getZ();
            //  max
            if(ah.getX() > maxX) maxX = ah.getX();
            if(ah.getX() > maxY) maxY = ah.getY();
            if(ah.getX() > maxZ) maxZ = ah.getZ();

            // rms
            msX += ah.getX() * ah.getX();
            msY += ah.getY() * ah.getY();
            msZ += ah.getZ() * ah.getZ();
        }

        pdo.setAvgX(sumX / sensorHistory.size());
        pdo.setAvgY(sumY / sensorHistory.size());
        pdo.setAvgZ(sumZ / sensorHistory.size());

        pdo.setMinX(minX);
        pdo.setMinY(minY);
        pdo.setMinZ(minZ);

        pdo.setMaxX(maxZ);
        pdo.setMaxY(maxY);
        pdo.setMaxZ(maxZ);

        msX /= sensorHistory.size();
        msY /= sensorHistory.size();
        msZ /= sensorHistory.size();

        pdo.setRmsX(Math.sqrt(msX));
        pdo.setRmsY(Math.sqrt(msY));
        pdo.setRmsZ(Math.sqrt(msZ));

        return pdo;
    }
}