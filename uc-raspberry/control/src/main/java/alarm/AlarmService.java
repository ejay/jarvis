package alarm;

import javafx.embed.swing.JFXPanel;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import light.NSocket;
import storage.Storage;

import java.nio.file.Paths;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class AlarmService {
    private static long oneSidedRangeInMinutes = 10;

    public static void main(String[] args){

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        LocalTime alarmTime;

        NSocket.close();
        NSocket.connect("192.168.178.69", 57286);
        NSocket.write("{\"action\":\"identify\"}");

        while(true){
            Storage storage = new Storage("localhost");
            storage.store("PlannedWakeUpTime", "06:30:00");
            storage.store("TimeOfTodaysFirstCalendarEvent", "07:45:00");
            String alarmTimeString = storage.get("PlannedWakeUpTime");
            LocalTime currentTime = LocalTime.now();
            alarmTime = LocalTime.parse(alarmTimeString, dateTimeFormatter);
            boolean userIsAwake = Boolean.parseBoolean(storage.get("UserHasWokenUp"));

            if(currentTime.isAfter(alarmTime.minusMinutes(oneSidedRangeInMinutes))){
                // Turn on light
                System.out.println("Turning on light");
                NSocket.write("{\"action\": \"send\", \"code\": {\"protocol\": [\"kaku_switch\"],\"id\": 17432370,\"unit\": 0,\"on\": 1}}");

            }

userIsAwake = false;
            if(currentTime.isAfter(alarmTime.plusMinutes(oneSidedRangeInMinutes)) && !userIsAwake){
                // After the acceptable range, sound alarm regardless of sleep cycle.
                //TODO sound alarm.
                soundAlarm();
            }

            String sleepCycle = storage.get("CurrentSleepCycleUser");

            long minutesBetween = ChronoUnit.MINUTES.between(currentTime, alarmTime);
            if(minutesBetween < oneSidedRangeInMinutes && sleepCycle.equals("awake") && !userIsAwake){
                // TODO Sound the alarm.
                try {
                    Thread.sleep(60 * 1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                soundAlarm();
            }



            try {
                Thread.sleep(60 * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }

    }

    private static MediaPlayer mediaPlayer;
    public static void soundAlarm(){
        System.out.println("Sounding alarm!");
        String bip = "control/bip.mp3";

         JFXPanel fxPanel = new JFXPanel();
         Media hit = new Media(Paths.get(bip).toUri().toString());
         mediaPlayer = new MediaPlayer(hit);
         mediaPlayer.play();
    }
}
