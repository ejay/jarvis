package uc.jarvis.DataProcessor;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

import uc.jarvis.AccelerometerData;

public class ParcelSensorHistory implements Parcelable {

//    private List<ParcleTopic> list;
//    private ArrayList<HoldListTopic> listh=new ArrayList<HoldListTopic>();

    private String name;
    private ArrayList<AccelerometerData> itemList = new ArrayList<>();


//    public ArrayList<HoldListTopic> GetListTopic(){}

    public Pa

    protected ParcelSensorHistory(Parcel in) {
    }

    public static final Creator<ParcelSensorHistory> CREATOR = new Creator<ParcelSensorHistory>() {
        @Override
        public ParcelSensorHistory createFromParcel(Parcel in) {
            return new ParcelSensorHistory(in);
        }

        @Override
        public ParcelSensorHistory[] newArray(int size) {
            return new ParcelSensorHistory[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
    }
}
