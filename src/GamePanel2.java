import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Random;

public class GamePanel2 extends JPanel implements KeyListener {

    private final int boardWidth;
    private final int boardHeight;
    private final int cellSize = 25;

    private final CardLayout layout;
    private final JPanel cardPanel;
    private final GameOverPanel gameOverPanel;

    private Cell snakeHead1;
    private Cell snakeHead2;

    private final ArrayList<Cell> snakeBody1;
    private final ArrayList<Cell> snakeBody2;

    private final Cell food1;
    private final Cell food2;

    private final Random random;
    private final Timer timer;
    private int movingX1;
    private int movingY1;
    private int movingX2;
    private int movingY2;

    private boolean gameOver = false;

    private final SoundManager soundManager2;


    public GamePanel2(int boardWidth, int boardHeight, CardLayout layout, JPanel cardPanel, GameOverPanel gameOverPanel) {
        this.boardWidth = boardWidth;
        this.boardHeight = boardHeight;
        this.layout = layout;
        this.cardPanel = cardPanel;
        this.gameOverPanel = gameOverPanel;

        soundManager2 = new SoundManager();

        setPreferredSize(new Dimension(this.boardWidth, this.boardHeight));
        setBackground(Color.LIGHT_GRAY);
        addKeyListener(this);
        setFocusable(true);
        requestFocusInWindow();

        snakeHead1 = new Cell(5, 5);
        snakeBody1 = new ArrayList<>();

        snakeHead2 = new Cell(15, 15);
        snakeBody2 = new ArrayList<>();

        food1 = new Cell(10, 10);

        food2 = new Cell(12, 12);

        random = new Random();
        placeFood(food1, snakeBody1);
        placeFood(food2, snakeBody2);


        movingX1 = 0;
        movingY1 = 0;
        movingX2 = 0;
        movingY2 = 0;

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
        gr.fillRect(food1.getX() * cellSize, food1.getY() * cellSize, cellSize, cellSize);
        gr.setColor(new Color(128, 0, 0));
        gr.fillRect(food2.getX() * cellSize, food2.getY() * cellSize, cellSize, cellSize);


        // snake head 1
        gr.setColor(Color.GREEN);
        gr.fillRect(snakeHead1.getX() * cellSize, snakeHead1.getY() * cellSize, cellSize, cellSize);

        // snake body 1

        gr.setColor(new Color(157, 193, 131));
        for (Cell cell : snakeBody1) {
            gr.fillRect(cell.getX() * cellSize, cell.getY() * cellSize, cellSize, cellSize);
        }

        // snake head 2
        gr.setColor(new Color(63, 122, 77));
        gr.fillRect(snakeHead2.getX() * cellSize, snakeHead2.getY() * cellSize, cellSize, cellSize);

        // snake body 2
        gr.setColor(new Color(80, 200, 120));
        for (Cell cell : snakeBody2) {
            gr.fillRect(cell.getX() * cellSize, cell.getY() * cellSize, cellSize, cellSize);
        }


        // Score

        gr.setFont(new Font("Ariel", Font.PLAIN, 24));
        if (!gameOver) {
            gr.setColor(Color.GREEN);
            gr.drawString("Score player 1:  " + snakeBody1.size(), cellSize - 16, cellSize);
            gr.setColor(new Color(63, 122, 77));
            gr.drawString("Score player 2:  " + snakeBody2.size(), cellSize + 260, cellSize);
        }
    }

    public void placeFood(Cell food, ArrayList<Cell> snakeBody) {
        boolean valid = false; // we assume that the placement will not be good
        int newX = 0;
        int newY = 0;
        while (!valid) // until it's good
        {
            newX = random.nextInt(boardWidth / cellSize);
            newY = random.nextInt(boardHeight / cellSize);

            valid = true; // we assume that it's good

            for (Cell cell : snakeBody) {
                if (collision(new Cell(newX, newY), cell)) {
                    valid = false; // they collided, so it's not good
                    break;
                }
            }
        }
        food.setX(newX);
        food.setY(newY);
    }

    public boolean collision(Cell cell1, Cell cell2) {
        return cell1.getX() == cell2.getX() && cell1.getY() == cell2.getY();
    }

    public void move() {

        if (collision(snakeHead1, food1)) {
            soundManager2.playSound("sound/AppleCrunch.wav", false);
            snakeBody1.add(new Cell(food1.getX(), food1.getY()));
            placeFood(food1, snakeBody1);
        }
        if (collision(snakeHead2, food2)) {
            soundManager2.playSound("sound/AppleCrunch.wav", false);
            snakeBody2.add(new Cell(food2.getX(), food2.getY()));
            placeFood(food2, snakeBody2);
        }

        // snake body 1
        for (int i = snakeBody1.size() - 1; i >= 0; i--) {
            Cell cell = snakeBody1.get(i);
            if (i == 0) {
                cell.setX(snakeHead1.getX());
                cell.setY(snakeHead1.getY());
            } else {
                Cell prevCell = snakeBody1.get(i - 1);
                cell.setX(prevCell.getX());
                cell.setY(prevCell.getY());
            }
        }

        for (int i = snakeBody2.size() - 1; i >= 0; i--) {
            Cell cell = snakeBody2.get(i);
            if (i == 0) {
                cell.setX(snakeHead2.getX());
                cell.setY(snakeHead2.getY());
            } else {
                Cell prevCell = snakeBody2.get(i - 1);
                cell.setX(prevCell.getX());
                cell.setY(prevCell.getY());
            }
        }
        int newX = snakeHead1.getX() + movingX1;
        int newY = snakeHead1.getY() + movingY1;

        int newX2 = snakeHead2.getX() + movingX2;
        int newY2 = snakeHead2.getY() + movingY2;


        if (newX < 0 || newY < 0 || newX > (boardWidth / cellSize) || newY > (boardHeight / cellSize)) {
            gameOver = true;
            gameOverPanel.setScoreLabel("<html>Score player 1: " + snakeBody1.size() + "<br>Score player 2: " + snakeBody2.size() + "</html>");
            layout.show(cardPanel, "GameOver");
        }

        if (newX2 < 0 || newY2 < 0 || newX2 > (boardWidth / cellSize) || newY2 > (boardHeight / cellSize)) {
            gameOver = true;
            print();

        }

        for (Cell snake : snakeBody1) {
            if (collision(snake, snakeHead2)) {
                gameOver = true;
                print();
                break;
            }
        }

        for (Cell snake : snakeBody2) { // if the head of player 1's snake collides with player 2's snake
            if (collision(snake, snakeHead1)) {
                gameOver = true;
                print();
                break;
            }
        }

        if (collision(snakeHead1, snakeHead2)) { // if they collide with each other
            gameOver = true;
            print();
        }


        snakeHead1.setX(newX);
        snakeHead1.setY(newY);

        snakeHead2.setX(newX2);
        snakeHead2.setY(newY2);

    }

    public void print() {
        gameOverPanel.setScoreLabel("<html>Score player 1: " + snakeBody1.size() + "<br>Score player 2: " + snakeBody2.size() + "</html>");
        layout.show(cardPanel, "GameOver");
    }


    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_UP && movingY1 != -1) {
            movingX1 = 0;
            movingY1 = -1;
        } else if (e.getKeyCode() == KeyEvent.VK_DOWN && movingY1 != -1) {
            movingX1 = 0;
            movingY1 = 1;
        } else if (e.getKeyCode() == KeyEvent.VK_LEFT && movingX1 != 1) {
            movingX1 = -1;
            movingY1 = 0;
        } else if (e.getKeyCode() == KeyEvent.VK_RIGHT && movingX1 != -1) {
            movingX1 = 1;
            movingY1 = 0;
        } else if (e.getKeyCode() == KeyEvent.VK_W && movingY2 != 1) {
            movingX2 = 0;
            movingY2 = -1;
        } else if (e.getKeyCode() == KeyEvent.VK_S && movingY2 != -1) {
            movingX2 = 0;
            movingY2 = 1;
        } else if (e.getKeyCode() == KeyEvent.VK_A && movingX2 != 1) {
            movingX2 = -1;
            movingY2 = 0;
        } else if (e.getKeyCode() == KeyEvent.VK_D && movingX2 != -1) {
            movingX2 = 1;
            movingY2 = 0;
        }

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    public void reset() {
        snakeHead1 = new Cell(5, 5);
        snakeHead2 = new Cell(15, 15);
        snakeBody1.clear();
        snakeBody2.clear();
        int newX = random.nextInt(boardWidth / cellSize);
        int newY = random.nextInt(boardHeight / cellSize);
        food1.setX(newX);
        food1.setY(newY);

        newX = random.nextInt(boardWidth / cellSize);
        newY = random.nextInt(boardHeight / cellSize);
        food2.setX(newX);
        food2.setY(newY);

        movingX1 = 0;
        movingY1 = 0;
        movingX2 = 0;
        movingY2 = 0;


        gameOver = false;
        timer.restart();
    }
}
