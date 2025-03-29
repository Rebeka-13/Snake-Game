import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class CardLayoutJFrame extends JFrame {

    private boolean isOnePlayer = true;
    private final SoundManager soundManager;

    public CardLayoutJFrame() throws IOException {

        soundManager = new SoundManager();

        CardLayout layout = new CardLayout();
        JPanel cardPanel = new JPanel();
        cardPanel.setLayout(layout);
        add(cardPanel);


        // menu view
        BackgroundImageManager menuPanel = new BackgroundImageManager("img/SnakeImage.png");
        JPanel players = new JPanel();
        menuPanel.setLayout(new GridLayout(2, 1));
        JLabel menuLabel = new JLabel("Snake Game", SwingConstants.CENTER); // aligns the text to the center
        menuLabel.setForeground(new Color(75, 83, 32));

        JButton b1 = new JButton("1 Player");
        JButton b2 = new JButton("2 Players");
        b1.setPreferredSize(new Dimension(160, 100));
        b2.setPreferredSize(new Dimension(160, 100));

        b1.setForeground(Color.WHITE);
        b1.setBackground(new Color(0, 78, 56));
        b2.setForeground(Color.WHITE);
        b2.setBackground(new Color(0, 78, 56));


        menuLabel.setFont(new Font("Book Antiqua", Font.BOLD, 80));
        menuPanel.add(menuLabel);
        players.setLayout(new FlowLayout(FlowLayout.CENTER));
        players.setBackground(new Color(208, 240, 192));
        players.add(b1);
        players.add(b2);
        menuPanel.add(players);

        cardPanel.add(menuPanel, "Menu");


        // game over panel
        GameOverPanel gameOverPanel = new GameOverPanel();
        cardPanel.add(gameOverPanel, "GameOver");


        int boardWith = 625;

        // Game view 1 Player
        GamePanel game = new GamePanel(boardWith, boardWith, layout, cardPanel, gameOverPanel);
        cardPanel.add(game, "Game1");


        // Game view 2 Players
        GamePanel2 game2 = new GamePanel2(boardWith, boardWith, layout, cardPanel, gameOverPanel);
        cardPanel.add(game2, "Game2");


        gameOverPanel.getMainMenu().addActionListener(e -> {
            layout.show(cardPanel, "Menu");
            soundManager.stopSound();
            game.reset();
            game2.reset();

        });
        gameOverPanel.getRetryButton().addActionListener(e -> {
            if (isOnePlayer) {
                game.reset();
                layout.show(cardPanel, "Game1");
                game.requestFocusInWindow();
            } else {
                game2.reset();
                layout.show(cardPanel, "Game2");
                game2.requestFocusInWindow();
            }
        });


        b1.addActionListener(e -> {
            isOnePlayer = true;
            soundManager.playSound("sound/SnakeSound.wav", true);

            layout.show(cardPanel, "Game1");
            game.requestFocusInWindow();
        });
        b2.addActionListener(e -> {
            isOnePlayer = false;
            soundManager.playSound("sound/SnakeSound.wav", true);
            layout.show(cardPanel, "Game2");
            game2.requestFocusInWindow();
        });

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setBounds(100, 100, 670, 690);
        setVisible(true);
    }

    public static void main(String[] args) throws IOException {
        new CardLayoutJFrame();
    }
}

