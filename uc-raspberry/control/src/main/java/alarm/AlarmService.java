package alarm;

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
