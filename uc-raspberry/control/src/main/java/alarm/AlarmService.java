package alarm;

import light.NSocket;
import storage.Storage;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class AlarmService {
    private static long oneSidedRangeInMinutes = 20;

    public static void main(String[] args){

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("hh:mm:ss");
        LocalDateTime alarmTime;

        while(true){
            Storage storage = new Storage("localhost");
            String alarmTimeString = storage.get("PlannedWakeUpTime");
            LocalDateTime currentTime = LocalDateTime.now();
            alarmTime = LocalDateTime.parse(alarmTimeString, dateTimeFormatter);
            boolean userIsAwake = Boolean.parseBoolean(storage.get("UserHasWokenUp"));

            if(currentTime.isAfter(alarmTime.minusMinutes(oneSidedRangeInMinutes))){
                // TODO Turn on light
                NSocket.connect("192.168.178.69", 57286);
                NSocket.write("{\"action\": \"send\", \"code\": {\"protocol\": [\"kaku_switch\"],\"id\": 17432370,\"unit\": 0,\"off\": 1}}");
            }

            if(currentTime.isAfter(alarmTime.plusMinutes(oneSidedRangeInMinutes)) && !userIsAwake){
                // After the acceptable range, sound alarm regardless of sleep cycle.
                //TODO sound alarm.
            }

            String sleepCycle = storage.get("CurrentSleepCycleUser");

            long minutesBetween = ChronoUnit.MINUTES.between(currentTime, alarmTime);
            if(minutesBetween < oneSidedRangeInMinutes && sleepCycle == "awake" && !userIsAwake){
                // TODO Sound the alarm.
            }

            try {
                Thread.sleep(60 * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
