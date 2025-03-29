import java.io.*;

public class HighScoreManager {

    public int getHighScore() {
        try (BufferedReader reader = new BufferedReader(new FileReader("src/HighScore.txt"))) {
            String line = reader.readLine();
            if (line != null) {
                return Integer.parseInt(line);
            }
        } catch (IOException | NumberFormatException e) {
            throw new RuntimeException(e);
        }
        return 0; // if we don't have a high score yet
    }

    public void setHighScore(int newHighScore) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("src/HighScore.txt"));
            writer.write(String.valueOf(newHighScore));
            writer.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
