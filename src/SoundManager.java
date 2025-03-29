import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class SoundManager {
    private Clip clip;

    public void playSound(String filename, boolean backMusic) {
        try {
            stopSound();
            AudioInputStream audio = AudioSystem.getAudioInputStream(new File(filename));
            clip = AudioSystem.getClip();
            clip.open(audio);
            if (backMusic) {
                clip.loop(Clip.LOOP_CONTINUOUSLY);
            } else {
                clip.start();
            }

        } catch (UnsupportedAudioFileException | LineUnavailableException | IOException e) {
            System.out.println("IO error");
        }
    }

    public void stopSound() {
        if (clip != null && clip.isRunning()) {
            clip.stop();
            clip.close();
        }
    }
}



