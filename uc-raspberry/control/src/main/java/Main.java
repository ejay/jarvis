import javafx.embed.swing.JFXPanel;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.nio.file.Paths;

/**
 * Created by bas on 12-12-15.
 */
public class Main {
    private static MediaPlayer mediaPlayer;

    public static void main(String[] args){
        playSong();
    }

    private static void playSong(){
         String bip = "control/bip.mp3";
         JFXPanel fxPanel = new JFXPanel();
         Media hit = new Media(Paths.get(bip).toUri().toString());
         mediaPlayer = new MediaPlayer(hit);
         mediaPlayer.play();
    }
}

