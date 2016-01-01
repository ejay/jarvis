import javafx.embed.swing.JFXPanel;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import storage.Storage;

import java.nio.file.Paths;

/**
 * Created by bas on 12-12-15.
 */
public class Main {
    private static MediaPlayer mediaPlayer;

    public static void main(String[] args) throws InterruptedException {
//        playSong();

        Storage storage = new Storage("localhost");
        String line;
        int count = 0;

        NSocket.connect("192.168.178.69", 57286);
        System.out.println(NSocket.write("{\"action\":\"identify\"}"));

        while(count < 240){
            String value = storage.get("light");
            System.out.println("light value = " + value);

            if(Double.parseDouble(value) > 100.0){
                NSocket.write("{\"action\": \"send\", \"code\": {\"protocol\": [\"kaku_switch\"],\"id\": 17432370,\"unit\": 0,\"off\": 1}}");
            }else{
                NSocket.write("{\"action\": \"send\", \"code\": {\"protocol\": [\"kaku_switch\"],\"id\": 17432370,\"unit\": 0,\"on\": 1}}");
            }

            Thread.sleep(2000);

            count++;
        }

        NSocket.close();

//        System.out.println(NSocket.write("{\"action\":\"identify\"}"));
//        System.out.println(NSocket.read(1024));
//        System.out.println(line = NSocket.getLine());
//        System.out.println("line = " + line);
//        System.out.println(NSocket.write("{\"action\":\"request values\"}"));
//        System.out.println(NSocket.read(1024));
//        System.out.println(line = NSocket.getLine());
//        System.out.println("line = " + line);

//        System.out.println(NSocket.write("HEART"));
//        System.out.println(NSocket.read(1024));
//        System.out.println(line = NSocket.getLine());
//        System.out.println("line = " + line);


//        System.out.println(NSocket.write("{\"action\": \"send\", \"code\": {\"protocol\": [\"kaku_switch\"],\"id\": 17432370,\"unit\": 0,\"off\": 1}}"));

//        System.out.println(NSocket.read(1024));
//        System.out.println(line = NSocket.getLine());
//        System.out.println("line = " + line);

//        Thread.sleep(1000);


    }

    private static void playSong(){
         String bip = "control/bip.mp3";
         JFXPanel fxPanel = new JFXPanel();
         Media hit = new Media(Paths.get(bip).toUri().toString());
         mediaPlayer = new MediaPlayer(hit);
         mediaPlayer.play();
    }
}

