package CodewithPawan;

// Import necessary libraries
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.LinkedList;

// Main class for the Snake Game
public class SnakeGame extends JPanel implements KeyListener, ActionListener {

    // Constants for game settings
    private static final int WIDTH = 600; // Width of the game window
    private static final int HEIGHT = 400; // Height of the game window
    private static final int UNIT_SIZE = 20; // Size of each grid unit
    private static final int INITIAL_SNAKE_LENGTH = 2; // Initial length of the snake
    private static final int DELAY = 100; // Delay for the game timer (speed)

    // Game variables
    private final LinkedList<Point> snake; // Linked list to store snake body segments
    private int direction; // Direction of movement: 1-right, 2-left, 3-up, 4-down
    private Point food; // Position of the food
    private boolean running; // Game running state
    private final Timer timer; // Timer to control game updates
    private boolean gameOver; // Game over state

    // Constructor to initialize the game
    public SnakeGame() {
        // Set panel properties
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setBackground(Color.BLACK);
        setFocusable(true);
        addKeyListener(this);

        // Initialize variables
        snake = new LinkedList<>();
        direction = 1; // Start moving to the right
        running = false;
        gameOver = false;

        // Initialize snake and spawn food
        initializeSnake();
        spawnFood();

        // Set up timer
        timer = new Timer(DELAY, this);
    }

    // Initialize snake with predefined length
    private void initializeSnake() {
        snake.clear(); // Clear any existing snake data
        for (int i = 0; i < INITIAL_SNAKE_LENGTH; i++) {
            snake.add(new Point(WIDTH / 2 - i * UNIT_SIZE, HEIGHT / 2)); // Add segments at the center
        }
    }

    // Spawn food at a random location
    private void spawnFood() {
        int x = (int) (Math.random() * (WIDTH / UNIT_SIZE)) * UNIT_SIZE;
        int y = (int) (Math.random() * (HEIGHT / UNIT_SIZE)) * UNIT_SIZE;
        food = new Point(x, y);
    }

    // Move the snake in the current direction
    private void move() {
        Point head = snake.getFirst(); // Get the head of the snake
        Point newHead = new Point(head.x, head.y); // Create a new head position

        // Update head position based on direction
        switch (direction) {
            case 1: // Right
                newHead.x += UNIT_SIZE;
                break;
            case 2: // Left
                newHead.x -= UNIT_SIZE;
                break;
            case 3: // Up
                newHead.y -= UNIT_SIZE;
                break;
            case 4: // Down
                newHead.y += UNIT_SIZE;
                break;
        }

        // Add new head to the snake
        snake.addFirst(newHead);

        // Check if food is eaten
        if (newHead.equals(food)) {
            spawnFood(); // Spawn new food
        } else {
            snake.removeLast(); // Remove tail if no food eaten
        }

        checkCollision(); // Check for collisions
    }

    // Check for collisions with walls or self
    private void checkCollision() {
        Point head = snake.getFirst();
        // Check wall collision
        if (head.x < 0 || head.x >= WIDTH || head.y < 0 || head.y >= HEIGHT) {
            gameOver = true; // End game if wall is hit
        }
        // Check self collision
        for (int i = 1; i < snake.size(); i++) {
            if (head.equals(snake.get(i))) { // Compare head with body segments
                gameOver = true; // End game if snake runs into itself
                break;
            }
        }
    }

    // Draw the game components
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (gameOver) {
            // Display game over message
            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.BOLD, 30));
            String message = "Game Over! Score: " + (snake.size() - INITIAL_SNAKE_LENGTH);
            int messageWidth = g.getFontMetrics().stringWidth(message);
            g.drawString(message, (WIDTH - messageWidth) / 2, HEIGHT / 2);

            // Display restart instructions
            g.setFont(new Font("Arial", Font.PLAIN, 20));
            String playAgainMessage = "Press SPACE to Play Again";
            int playAgainWidth = g.getFontMetrics().stringWidth(playAgainMessage);
            g.drawString(playAgainMessage, (WIDTH - playAgainWidth) / 2, HEIGHT / 2 + 50);

            String exitMessage = "Press ESC to Exit";
            int exitWidth = g.getFontMetrics().stringWidth(exitMessage);
            g.drawString(exitMessage, (WIDTH - exitWidth) / 2, HEIGHT / 2 + 80);
        } else {
            // Draw snake
            g.setColor(Color.GREEN);
            for (Point point : snake) {
                g.fillRect(point.x, point.y, UNIT_SIZE, UNIT_SIZE);
            }
            // Draw food
            g.setColor(Color.RED);
            g.fillRect(food.x, food.y, UNIT_SIZE, UNIT_SIZE);
        }
    }

    // Handle key presses
    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_SPACE) { // Restart game on SPACE key
            if (gameOver) {
                initializeSnake();
                spawnFood();
                gameOver = false;
            } else if (!running) {
                running = true;
                timer.start();
            }
        } else if (key == KeyEvent.VK_ESCAPE) { // Exit game on ESC key
            System.exit(0);
        }
        if (running && !gameOver) {
            // Change direction
            switch (key) {
                case KeyEvent.VK_RIGHT:
                    if (direction != 2)
                        direction = 1;
                    break;
                case KeyEvent.VK_LEFT:
                    if (direction != 1)
                        direction = 2;
                    break;
                case KeyEvent.VK_UP:
                    if (direction != 4)
                        direction = 3;
                    break;
                case KeyEvent.VK_DOWN:
                    if (direction != 3)
                        direction = 4;
                    break;
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (running && !gameOver) {
            move(); // Move the snake
            repaint(); // Refresh screen
        }
    }

    // Main method to start the game
    public static void main(String[] args) {
        JFrame frame = new JFrame("Snake Game");
        SnakeGame game = new SnakeGame();
        frame.add(game);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    // Unused methods for KeyListener interface
    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }
}
