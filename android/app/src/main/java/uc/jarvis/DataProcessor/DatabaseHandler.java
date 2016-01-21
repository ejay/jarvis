package uc.jarvis.DataProcessor;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import uc.jarvis.AccelerometerData;
import uc.jarvis.Models.AccelerometerRaw;
import uc.jarvis.Models.ProcessedFeature;

/**
 * Database handler for sensor data
 *
 * these command are to retrieve database from phone and debug the data
 ./adb shell run-as <app package name> chmod 666 /data/data/<app package name>/databases/<database file name>
 ./adb shell cp /data/data/<app package name>/databases/<database file name> /sdcard/
 ./adb pull /sdcard/<database file name>
 */
public class DatabaseHandler extends SQLiteOpenHelper {

    private static DatabaseHandler sInstance;
    private static final String TAG = "DatabaseHandler";

    // Database Info
    private static final String DATABASE_NAME = "sensorData";
    private static final int DATABASE_VERSION = 1;

    // Table Names
    private static final String TABLE_RAW = "accelerometer_raw";
    private static final String TABLE_FEATURES = "features";

//    public static final String COLUMN_ID = "_id";
//    public static final String COLUMN_PRODUCTNAME = "productname";
//    public static final String COLUMN_QUANTITY = "quantity";


    public DatabaseHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        onUpgrade(this.getWritableDatabase(), 0, 0);
    }

    /**
     * Constructor should be private to prevent direct instantiation.
     * Make a call to the static method "getInstance()" instead.
     */
    private DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        onUpgrade(this.getWritableDatabase(), 0, 0);
    }

    public static synchronized DatabaseHandler getInstance(Context context) {
        // Use the application context, which will ensure that you
        // don't accidentally leak an Activity's context.
        // See this article for more information: http://bit.ly/6LRzfx
        if (sInstance == null) {
            sInstance = new DatabaseHandler(context.getApplicationContext());
        }
        return sInstance;
    }


    @Override
    public void onConfigure(SQLiteDatabase db){
        super.onConfigure(db);
//        db.setForeignKeyConstraintsEnabled(true);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // create database and table
//        db=openOrCreateDatabase("SensorData", Context.MODE_PRIVATE, null);
        db.execSQL("DROP TABLE IF EXISTS"+ TABLE_RAW);
        db.execSQL("DROP TABLE IF EXISTS" + TABLE_FEATURES);

        String CREATE_RAW_TABLE = "CREATE TABLE IF NOT EXISTS "+ TABLE_RAW +
                "(" +
                "id INTEGER primary key AUTOINCREMENT," +
                "timestamp LONG," +
                "x DOUBLE," +
                "y DOUBLE," +
                "z DOUBLE" +
                ");";

        String CREATE_FEATURES_TABLE = "CREATE TABLE IF NOT EXISTS "+ TABLE_FEATURES+
                "(" +
                "id INTEGER primary key AUTOINCREMENT," +
                " timestamp LONG," +
                " avgX DOUBLE," +
                " avgY DOUBLE," +
                " avgZ DOUBLE," +
                " minX DOUBLE," +
                " minY DOUBLE," +
                " minZ DOUBLE," +
                " maxX DOUBLE," +
                " maxY DOUBLE," +
                " maxZ DOUBLE," +
                " rmsX DOUBLE," +
                " rmsY DOUBLE," +
                " rmsZ DOUBLE);";

        db.execSQL(CREATE_RAW_TABLE);
        db.execSQL(CREATE_FEATURES_TABLE);
    }


    public void addRawData(AccelerometerData ad){
        SQLiteDatabase db = getWritableDatabase();

        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put("timestamp", ad.getTimestamp());
            values.put("x", ad.getX());
            values.put("y", ad.getY());
            values.put("z", ad.getZ());

            db.insertOrThrow(TABLE_RAW, null, values);
            db.setTransactionSuccessful();
        }catch (Exception e){
            Log.d(TAG, "Error while trying to add raw data to database");
        } finally {
            db.endTransaction();
        }
    }

    public List<AccelerometerRaw> getRawData(){
        List<AccelerometerRaw> acHistory = new ArrayList<>();

        String RAW_DATA_QUERY ="SELECT * FROM "+TABLE_RAW;

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(RAW_DATA_QUERY, null);

        try{
            if(cursor.moveToFirst()){
                do{
                    AccelerometerRaw acRaw = new AccelerometerRaw();
                    acRaw.timestamp = cursor.getLong(cursor.getColumnIndex("timestamp"));
                    acRaw.x = cursor.getDouble(cursor.getColumnIndex("x"));
                    acRaw.y = cursor.getDouble(cursor.getColumnIndex("y"));
                    acRaw.z = cursor.getDouble(cursor.getColumnIndex("z"));

                    acHistory.add(acRaw);
                } while (cursor.moveToNext());
            }
        }catch (Exception e){
            Log.d(TAG, "Error while trying to get raw data from database");
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }

        Log.d(TAG, "Got raw data from database"+ acHistory.size());
        return acHistory;
    }

    public void addFeature(ProcessedFeature feature){
        SQLiteDatabase db = getWritableDatabase();

        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put("timestamp", feature.timestamp);
            values.put("avgX", feature.avgX);
            values.put("avgY", feature.avgY);
            values.put("avgZ", feature.avgZ);

            values.put("minX", feature.minX);
            values.put("minY", feature.minY);
            values.put("minZ", feature.minZ);

            values.put("maxX", feature.maxX);
            values.put("maxY", feature.maxY);
            values.put("maxZ", feature.maxZ);

            values.put("rmsX", feature.rmsX);
            values.put("rmsY", feature.rmsY);
            values.put("rmsZ", feature.rmsZ);

            db.insertOrThrow(TABLE_FEATURES, null, values);
            db.setTransactionSuccessful();
        }catch (Exception e){
            Log.d(TAG, "Error while trying to add feature data to database");
        } finally {
            db.endTransaction();
        }
    }

    public ProcessedFeature lastFeature(){

        ProcessedFeature feature = new ProcessedFeature();
        String LAST_FEATURE_QUERY ="SELECT * FROM "+TABLE_RAW+" ORDER BY timestamp LIMIT 1";

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(LAST_FEATURE_QUERY, null);

        try{
            if(cursor.moveToFirst()){
                feature.timestamp = cursor.getLong(cursor.getColumnIndex("timestamp"));
                feature.avgX = cursor.getDouble(cursor.getColumnIndex("avgX"));
                feature.avgY = cursor.getDouble(cursor.getColumnIndex("avgY"));
                feature.avgZ = cursor.getDouble(cursor.getColumnIndex("avgZ"));
                feature.minX = cursor.getDouble(cursor.getColumnIndex("minX"));
                feature.minY = cursor.getDouble(cursor.getColumnIndex("minY"));
                feature.minZ = cursor.getDouble(cursor.getColumnIndex("minZ"));
                feature.maxX = cursor.getDouble(cursor.getColumnIndex("maxX"));
                feature.maxY = cursor.getDouble(cursor.getColumnIndex("maxY"));
                feature.maxZ = cursor.getDouble(cursor.getColumnIndex("maxZ"));
                feature.rmsX = cursor.getDouble(cursor.getColumnIndex("rmsX"));
                feature.rmsY = cursor.getDouble(cursor.getColumnIndex("rmsY"));
                feature.rmsZ = cursor.getDouble(cursor.getColumnIndex("rmsZ"));
            }
        }catch (Exception e){
            Log.d(TAG, "Error while trying to get raw data from database");
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }

        return feature;
    }


    public void clearRawData(){
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        try{
            db.delete(TABLE_RAW, null, null);
        } catch (Exception e) {
            Log.d(TAG, "Error while trying to delete raw data");
        } finally {
            db.endTransaction();
        }
    }



    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if(oldVersion != newVersion){
            db.execSQL("DROP TABLE IF EXISTS"+ TABLE_RAW);
            db.execSQL("DROP TABLE IF EXISTS"+ TABLE_FEATURES);
        }
    }

}
