import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class SnakeGame extends JFrame {

    SnakeGame() {
        ImageIcon icon = new ImageIcon("sheriff.png");
        this.setIconImage(icon.getImage());

        this.add(new gamePanel());
        this.setTitle("Snake");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.pack();
        this.setVisible(true);
        this.setLocationRelativeTo(null);
    }

    public static void main(String[] args) {
        new SnakeGame();
    }

    public static class gamePanel extends JPanel implements ActionListener {

        static final int SCREEN_WIDTH = 1000;
        static final int SCREEN_HEIGHT = 800;
        static final int SQUARE = 40;
        static final int GAME_UNITS = (SCREEN_WIDTH * SCREEN_HEIGHT) / SQUARE;
        static final int DELAY = 75;
        final int[] x = new int[GAME_UNITS];
        final int[] y = new int[GAME_UNITS];
        int bodyParts = 6;
        int robbersEaten;
        int robberX;
        int robberY;
        char direction = 'R';
        boolean running = false;
        Timer timer;
        Random random;
        BufferedImage backgroundImage;
        BufferedImage snakeHeadImage;
        BufferedImage appleImage;

        gamePanel() {
            random = new Random();
            this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
            this.setFocusable(true);
            this.addKeyListener(new MyKeyAdapter());

            try {
                backgroundImage = ImageIO.read(new File("desert.png"));
                snakeHeadImage = ImageIO.read(new File("snake_head.png"));
                appleImage = ImageIO.read(new File("robber.png"));
            } catch (IOException e) {
                e.printStackTrace();
            }
            startGame();
        }

        public void startGame() {
            newApple();
            running = true;
            timer = new Timer(DELAY, this);
            timer.start();
        }

        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            draw(g);
        }

        public void draw(Graphics g) {
            if (backgroundImage != null) {
                g.drawImage(backgroundImage, 0, 0, SCREEN_WIDTH, SCREEN_HEIGHT, this);
            }

            if (running) {
                for (int i = 0; i < SCREEN_WIDTH / SQUARE; i++) {
                    g.drawLine(i * SQUARE, 0, i * SQUARE, SCREEN_HEIGHT);
                }
                for (int i = 0; i < SCREEN_HEIGHT / SQUARE; i++) {
                    g.drawLine(0, i * SQUARE, SCREEN_WIDTH, i * SQUARE);
                }

                if (appleImage != null) {
                    g.drawImage(appleImage, robberX, robberY, SQUARE, SQUARE, this);
                }

                for (int i = 0; i < bodyParts; i++) {
                    if (i == 0) {
                        if (snakeHeadImage != null) {
                            g.drawImage(snakeHeadImage, x[i], y[i], SQUARE, SQUARE, this);
                        }
                    } else {
                        g.setColor(new Color(29, 39, 90));
                        g.fillRect(x[i], y[i], SQUARE, SQUARE);
                    }
                }

                g.setColor(Color.red);
                g.setFont(new Font("Serif", Font.BOLD, 40));
                FontMetrics metrics = getFontMetrics(g.getFont());
                g.drawString("Score: " + robbersEaten, (SCREEN_WIDTH - metrics.stringWidth("Score: " + robbersEaten)) / 2,
                        g.getFont().getSize());
            } else {
                gameOver(g);
            }
        }

        public void newApple() {
            robberX = random.nextInt((int) (SCREEN_WIDTH / SQUARE)) * SQUARE;
            robberY = random.nextInt((int) (SCREEN_HEIGHT / SQUARE)) * SQUARE;
        }

        public void move() {
            for (int i = bodyParts; i > 0; i--) {
                x[i] = x[i - 1];
                y[i] = y[i - 1];
            }

            switch (direction) {
                case 'U':
                    y[0] = y[0] - SQUARE;
                    break;
                case 'D':
                    y[0] = y[0] + SQUARE;
                    break;
                case 'L':
                    x[0] = x[0] - SQUARE;
                    break;
                case 'R':
                    x[0] = x[0] + SQUARE;
                    break;
            }
        }

        public void checkApple() {
            if ((x[0] == robberX) && (y[0] == robberY)) {
                bodyParts++;
                robbersEaten++;
                newApple();
            }
        }

        public void checkCollisions() {
            for (int i = bodyParts; i > 0; i--) {
                if ((x[0] == x[i]) && y[0] == y[i]) {
                    running = false;
                }
            }
            if (x[0] < 0 || x[0] > SCREEN_WIDTH || y[0] < 0 || y[0] > SCREEN_HEIGHT) {
                running = false;
            }
            if (!running) {
                timer.stop();
            }
        }

        public void gameOver(Graphics g) {
            g.setColor(Color.red);
            g.setFont(new Font("Serif", Font.BOLD, 40));
            FontMetrics metrics1 = getFontMetrics(g.getFont());
            g.drawString("Score: " + robbersEaten, (SCREEN_WIDTH - metrics1.stringWidth("Score: " + robbersEaten)) / 2,g.getFont().getSize());

            g.setColor(Color.black);
            g.setFont(new Font("Capri", Font.BOLD, 75));
            FontMetrics metrics2 = getFontMetrics(g.getFont());
            g.drawString("Game Over", (SCREEN_WIDTH - metrics2.stringWidth("Game Over")) / 2, SCREEN_HEIGHT / 2);

            JButton exitButton = new JButton("Replay");
            exitButton.setBounds((SCREEN_WIDTH / 2) - 100, SCREEN_HEIGHT / 2 + 100, 200, 50);
            exitButton.setFont(new Font("", Font.BOLD, 30));

            exitButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    robbersEaten = 0;
                    new SnakeGame();
                }
            });

            this.setLayout(null);
            this.add(exitButton);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (running) {
                move();
                checkApple();
                checkCollisions();
            }
            repaint();
        }

        public class MyKeyAdapter extends KeyAdapter {
            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_LEFT:
                        if (direction != 'R') {
                            direction = 'L';
                        }
                        break;
                    case KeyEvent.VK_RIGHT:
                        if (direction != 'L') {
                            direction = 'R';
                        }
                        break;
                    case KeyEvent.VK_UP:
                        if (direction != 'D') {
                            direction = 'U';
                        }
                        break;
                    case KeyEvent.VK_DOWN:
                        if (direction != 'U') {
                            direction = 'D';
                        }
                        break;
                }
            }
        }
    }
}
