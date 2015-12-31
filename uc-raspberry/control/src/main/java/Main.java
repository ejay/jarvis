import javafx.embed.swing.JFXPanel;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.nio.file.Paths;

/**
 * Created by bas on 12-12-15.
 */
public class Main {
    private static MediaPlayer mediaPlayer;

    public static void main(String[] args) throws InterruptedException {
//        playSong();

//        Storage storage = new Storage("localhost");
//
//        while(true){
//            String value = storage.get("light");
//            System.out.println("light value = " + value);
//            Thread.sleep(3*1000);
//        }

        NSocket.connect("192.168.178.69", 5001);
        System.out.println(NSocket.write("{\"action\":\"identify\"}"));
        System.out.println(NSocket.write("{\"action\":\"request values\"}"));

        String line = NSocket.getLine();

        System.out.println("line = " + line);


        System.out.println(NSocket.write("{\n" +
                "\"action\": \"send\"\n" +
                "\"code\": {\n" +
                "\"protocol\": [\"kaku_switch\"],\n" +
                "\"id\": 17432370,\n" +
                "\"unit\": 0,\n" +
                "\"off\": 1\n" +
                "}\n" +
                "}"));



        NSocket.close();

    }

    private static void playSong(){
         String bip = "control/bip.mp3";
         JFXPanel fxPanel = new JFXPanel();
         Media hit = new Media(Paths.get(bip).toUri().toString());
         mediaPlayer = new MediaPlayer(hit);
         mediaPlayer.play();
    }
}

