import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class BackgroundImageManager extends JPanel {
    private Image backgroungImage;

    public BackgroundImageManager(String filename) throws IOException {
        try {
            backgroungImage = ImageIO.read(new File(filename));
        } catch (IOException e) {
            System.out.println("Image IO Error");
            backgroungImage = null;
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroungImage != null) {
            g.drawImage(backgroungImage, 0, 0, getWidth(), getHeight() - 275, this);
        }
    }
}
