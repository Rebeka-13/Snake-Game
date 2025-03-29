import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class GameOverPanel extends JPanel {

    private final JLabel scoreLabel;
    private final JButton mainMenu;
    private final JButton retryButton;

    public GameOverPanel() throws IOException {

        setLayout(new GridLayout(3, 1));
        JLabel gameOverLabel = new JLabel("Game Over", SwingConstants.CENTER);
        gameOverLabel.setFont(new Font("Arial", Font.BOLD, 60));
        add(gameOverLabel);

        setBackground(new Color(152, 251, 152));

        scoreLabel = new JLabel("", SwingConstants.CENTER);
        scoreLabel.setFont(new Font("Arial", Font.ITALIC, 30));
        add(scoreLabel);

        JPanel buttons = new JPanel();
        buttons.setLayout(new FlowLayout());
        buttons.setBackground(new Color(63, 122, 77));

        mainMenu = new JButton("Back to Menu");
        retryButton = new JButton();
        BufferedImage retryImage = ImageIO.read(new File("img/Retry.png"));
        ImageIcon retryIcon = new ImageIcon(retryImage);
        retryButton.setIcon(retryIcon);
        retryButton.setText("");
        retryButton.setPreferredSize(new Dimension(180, 190));
        mainMenu.setPreferredSize(new Dimension(180, 190));

        mainMenu.setBackground(new Color(169, 186, 157));
        retryButton.setBackground(Color.WHITE);

        buttons.add(mainMenu);
        buttons.add(retryButton);

        add(buttons);

    }

    public void setScoreLabel(String scoreText) {
        scoreLabel.setText(scoreText);
    }

    public JButton getMainMenu() {
        return mainMenu;
    }

    public JButton getRetryButton() {
        return retryButton;
    }

}
