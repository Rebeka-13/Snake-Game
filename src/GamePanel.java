import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Random;

public class GamePanel extends JPanel implements KeyListener {
    private final int boardWidth;
    private final int boardHeight;
    private final int cellSize = 25;

    private final CardLayout layout;
    private final JPanel cardPanel;
    private final GameOverPanel gameOverPanel;

    private Cell snakeHead;
    private final ArrayList<Cell> snakeBody;
    private final Cell food;
    private final Random random;
    private final Timer timer;
    private int movingX;
    private int movingY;

    boolean gameOver = false;

    private final SoundManager soundManager1;

    private int highScore;
    private final HighScoreManager highScoreManager;

    public GamePanel(int boardWidth, int boardHeight, CardLayout layout, JPanel cardPanel, GameOverPanel gameOverPanel) {
        this.boardWidth = boardWidth;
        this.boardHeight = boardHeight;
        this.layout = layout;
        this.cardPanel = cardPanel;
        this.gameOverPanel = gameOverPanel;

        highScoreManager = new HighScoreManager();
        this.highScore = highScoreManager.getHighScore();

        soundManager1 = new SoundManager();

        setPreferredSize(new Dimension(this.boardWidth, this.boardHeight));
        setBackground(Color.LIGHT_GRAY);
        addKeyListener(this);
        setFocusable(true);
        requestFocusInWindow();


        snakeHead = new Cell(5, 5);
        snakeBody = new ArrayList<>();

        food = new Cell(10, 10);
        random = new Random();
        int newX = random.nextInt(boardWidth / cellSize);
        int newY = random.nextInt(boardHeight / cellSize);
        food.setX(newX);
        food.setY(newY);

        movingX = 0; // if 0, then it hasn't moved on the X axis; if -1, then to the left; if 1, then to the right
        movingY = 0; // If 1 -> down, if -1 -> up

        timer = new Timer(100, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                move();
                repaint();
                if (gameOver) {
                    timer.stop();
                }
            }
        });
        timer.start();

    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics gr) {
        // food
        gr.setColor(Color.RED);
        gr.fillRect(food.getX() * cellSize, food.getY() * cellSize, cellSize, cellSize);

        // snake head
        gr.setColor(new Color(0, 78, 56));
        gr.fillRect(snakeHead.getX() * cellSize, snakeHead.getY() * cellSize, cellSize, cellSize);


        // snake body
        gr.setColor(Color.GREEN);
        snakeBody.forEach(cell -> gr.fillRect(cell.getX() * cellSize, cell.getY() * cellSize, cellSize, cellSize));

        // Score
        gr.setFont(new Font("Ariel", Font.PLAIN, 24));
        if (!gameOver) {
            gr.drawString("Score: " + snakeBody.size(), cellSize - 16, cellSize);
            gr.drawString("High score: " + highScore, boardWidth - 150, 30);
        }
    }

    public void placeFood() {
        boolean valid = false; // we assume that the placement will not be good
        int newX = 0;
        int newY = 0;
        while (!valid) // until it's good
        {
            newX = random.nextInt(boardWidth / cellSize);
            newY = random.nextInt(boardHeight / cellSize);

            int finalNewX = newX;
            int finalNewY = newY;
            valid = snakeBody.stream().noneMatch(cell -> collision(new Cell(finalNewX, finalNewY), cell)); // we check if we place the food down, then not to place it on the snake
        }
        food.setX(newX);
        food.setY(newY);
    }

    public boolean collision(Cell cell1, Cell cell2) {
        return cell1.getX() == cell2.getX() && cell1.getY() == cell2.getY();
    }

    public void move() {
        // if they collide, the snake's head is on the food
        if (collision(snakeHead, food)) {
            soundManager1.playSound("sound/AppleCrunch.wav", false);
            snakeBody.add(new Cell(food.getX(), food.getY()));
            placeFood();

        }

        // snake body
        for (int i = snakeBody.size() - 1; i >= 0; i--) {
            Cell cell = snakeBody.get(i);
            if (i == 0) {
                cell.setX(snakeHead.getX());
                cell.setY(snakeHead.getY());
            } else {
                Cell prevCell = snakeBody.get(i - 1);
                cell.setX(prevCell.getX());
                cell.setY(prevCell.getY());
            }
        }
        int newX = snakeHead.getX() + movingX;
        int newY = snakeHead.getY() + movingY;

        if (newX < 0 || newY < 0 || newX > (boardWidth / cellSize) || newY > (boardHeight / cellSize)) {
            gameOver = true;
            if (snakeBody.size() > highScore) {
                highScore = snakeBody.size();
                highScoreManager.setHighScore(highScore);
            }
            gameOverPanel.setScoreLabel("Score: " + snakeBody.size());
            layout.show(cardPanel, "GameOver");
            return;
        }

        snakeHead.setX(newX);
        snakeHead.setY(newY);
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_UP && movingY != 1) {
            movingX = 0;
            movingY = -1;
        } else if (e.getKeyCode() == KeyEvent.VK_DOWN && movingY != -1) {
            movingX = 0;
            movingY = 1;
        } else if (e.getKeyCode() == KeyEvent.VK_LEFT && movingX != 1) {
            movingX = -1;
            movingY = 0;
        } else if (e.getKeyCode() == KeyEvent.VK_RIGHT && movingX != -1) {
            movingX = 1;
            movingY = 0;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    public void reset() {
        snakeHead = new Cell(5, 5);
        snakeBody.clear();
        int newX = random.nextInt(boardWidth / cellSize);
        int newY = random.nextInt(boardHeight / cellSize);
        food.setX(newX);
        food.setY(newY);
        movingX = 0;
        movingY = 0;
        gameOver = false;
        timer.restart();
    }

}
