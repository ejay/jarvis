package control;

import javafx.embed.swing.JFXPanel;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.nio.file.Paths;

/**
 * Created by bas on 12-12-15.
 */
public class Main {
    public static void main(String[] args){
        playSong();
    }

    private void playSong(){
        String bip = "bip.mp3";
        JFXPanel fxPanel = new JFXPanel();
        Media hit = new Media(Paths.get(bip).toUri().toString());
        MediaPlayer mediaPlayer = new MediaPlayer(hit);
        mediaPlayer.play();
    }
}
