package uc.jarvis;


import android.graphics.Color;

import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineDataSet;

import org.apache.commons.collections4.queue.CircularFifoQueue;

import java.util.ArrayList;

public class SleepData {
    private static SleepData instance;

    private CircularFifoQueue<Entry> dataEntries;
    private CircularFifoQueue<String> labels;

    private String title;
    private int bufferIndex = 0;
    private int bufferSize = 10;
    private int numberOfEntries = 0;


    private SleepData() {
        title = "Sleep Chart";
        dataEntries = new CircularFifoQueue<>(bufferSize);
        labels = new CircularFifoQueue<>(bufferSize);
        for (int i = 0; i < bufferSize; i++) {
            dataEntries.add(new Entry(0f, i));
            labels.add("--:--:--");
        }
    }

    public LineDataSet getDataSet() {
        LineDataSet dataSet = new LineDataSet(getDataEntriesAsArrayList(), title);
        dataSet.setDrawCircles(true);
        dataSet.setLineWidth(2.0f);
        dataSet.setColor(Color.BLACK);
        dataSet.setDrawFilled(true);
        dataSet.setFillColor(Color.rgb(0,255,127));
        dataSet.setDrawCubic(true);
        dataSet.setFillAlpha(Color.alpha(127));
        return dataSet;
    }

    public ArrayList<Entry> getDataEntriesAsArrayList() {
        return new ArrayList<>(dataEntries);
    }

    public ArrayList<String> getLabelsAsArrayList() {
        return new ArrayList<>(labels);
    }

    public void addEntry(String time, float data) {
        labels.add(time);
        dataEntries.add(new Entry(data, numberOfEntries++));
        updateYValues();
    }

    private void updateYValues() {
        for (int i = 0; i < dataEntries.size(); i++) {
            dataEntries.get(i).setXIndex(i);
        }
    }

    public static synchronized SleepData getInstance() {
        if (instance == null) {
            instance = new SleepData();
        }
        return instance;
    }



}
