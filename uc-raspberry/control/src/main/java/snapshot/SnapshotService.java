package snapshot;

import storage.Storage;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.stream.Collectors;

public class SnapshotService {
    private static String[] keys = {
            "BedPhoneLight_raw",
            "BedPhoneLight",
            "RoomLight_raw",
            "RoomLight",
            "AlarmIsSet",
            "UserIsAtHome",
            "TimeOfTodaysFirstCalendarEvent",
            "TodoListIsEmpty",
            "PlannedWakeUpTime",
            "LightsAreOn",
            "Accelerometer",
            "WifiFingerprint",
            "UserHasWokenUp",
            "CurrentSleepCycleUser"
    };

    public static void main(String[] args){

        DateTimeFormatter simpleDateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'hh:mm:ss");
        LocalDateTime localDateTime = LocalDateTime.now();
        String filename = "Snapshots-" + localDateTime.format(simpleDateFormat) + ".txt";

        String header = Arrays.stream(keys).collect(Collectors.joining(", "));
        System.out.println("header = " + header);

        try {
            PrintWriter printWriter = new PrintWriter(new File(filename));
            printWriter.println(header);
            printWriter.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        while(true){
            try {
                PrintWriter printWriter = new PrintWriter(new FileOutputStream(new File(filename), true));
                String values = "";

                Storage storage = new Storage("localhost");
                for(String s : keys){
                    values += storage.get(s)+ ", ";
                }

                values = values.substring(0, values.length() - 2);
                printWriter.println(values);
                printWriter.close();

                Thread.sleep(2000);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }
}
