package uc.jarvis.DataProcessor;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import org.sleeper.propclasses.dataprocessor_manager.clDataProcessor;
import org.sleeper.propclasses.dataprocessor_manager.clStatManager;

import uc.jarvis.PostSensorDataTask;


public class SleepClassifierDataProcessor extends clDataProcessor implements clDataProcessor.clSleepStageClassifier.ISleepStateListener{

    clDataProcessor.clSleepStageClassifier sleepStageClassifier=null ;

    public SleepClassifierDataProcessor(Context context) {
        super(context);

        sleepStageClassifier=getSleepStageClassifier() ;
        sleepStageClassifier.registerSleepStateListener(this);
    }

    @Override
    public void measureStart() {
        super.measureStart();
    }

    @Override
    public void measureStop() {
        super.measureStop();
    }

    @Override
    public clDatabaseManager getDatabase() {return super.getDatabase();}

    @Override
    public clStatManager createStatManager(int i) {
        return null;
    }

    @Override
    public void onSleepStateRetrievedEvent(int sleepState, Double[] doubles) {

        Log.i("UPDATE", "Light sensor value: " + sleepState);

        String postString = String.format("key=%s&value=%s",
                "CurrentSleepCycleUser_raw",
                sleepState);

        new PostSensorDataTask().execute(postString);

        switch(sleepState){
            case clSleepStageClassifier.AWAKE :
                Log.i("SleepState: ", "AWAKE") ;
                Toast.makeText(AttachedContext, "AWAKE", Toast.LENGTH_SHORT).show() ;
                break ;
            case clSleepStageClassifier.DEEP:
                Log.i("SleepState: ","DEEP") ;
                Toast.makeText(AttachedContext,"DEEP",Toast.LENGTH_SHORT).show() ;
                break ;
            case clSleepStageClassifier.REM:
                Log.i("SleepState: ","REM") ;
                Toast.makeText(AttachedContext,"REM",Toast.LENGTH_SHORT).show() ;
                break ;
        }

        Log.i("SleepState: ",""+sleepState) ;
    }
}