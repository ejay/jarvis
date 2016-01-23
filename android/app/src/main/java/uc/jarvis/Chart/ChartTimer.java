package uc.jarvis.Chart;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.LineData;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimerTask;

import uc.jarvis.SleepData;

/**
 * Created by ejay on 23/01/16.
 */


public class ChartTimer extends TimerTask {

    @Override
    public void run() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        final String time = sdf.format(calendar.getTime());

//        SleepData.getInstance().addEntry(time, movementCounter.get());
//        System.out.println("Entry added: " + time + ", " + movementCounter);
//        lineChart.setData(
//                new LineData(
//                        SleepData.getInstance().getLabelsAsArrayList(),
//                        SleepData.getInstance().getDataSet()
//                )
//        );
//        lineChart.getLineData().getDataSetByIndex(0).setDrawFilled(true);
//        lineChart.getLineData().getDataSetByIndex(0).setFillAlpha(127);
//        lineChart.getAxisLeft().setAxisMaxValue(UPDATE_INTERVAL_ACCEL/50);
//        lineChart.getAxisLeft().setAxisMinValue(0.0f);
//        lineChart.getAxisRight().setAxisMaxValue(UPDATE_INTERVAL_ACCEL/50);
//        lineChart.getAxisRight().setAxisMinValue(0.0f);
//        lineChart.getAxisLeft().setDrawGridLines(false);
//        lineChart.getAxisRight().setDrawGridLines(false);
//        lineChart.getXAxis().setDrawGridLines(false);
//        lineChart.getLineData().getDataSetByIndex(0).setDrawCircles(false);
//        lineChart.getLineData().setDrawValues(false);
    }

//    protected void createChart(){
//        timerTask = new ChartTimer();
//
//        lineData = new LineData(
//                SleepData.getInstance().getLabelsAsArrayList(),
//                SleepData.getInstance().getDataSet()
//        );
//
//        lineChart = new LineChart(getApplicationContext());
//        lineChart.setData(lineData);
////        lineChart.setVisibleXRangeMaximum(120);
//        lineChart.setVisibleYRangeMaximum(50, YAxis.AxisDependency.LEFT);
//        lineChart.setMaxVisibleValueCount(10);
//
//
//    }
}