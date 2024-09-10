package com.company;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

public class Main extends JPanel implements KeyListener {

    public static int width = 400;
    public static int height = 400;
    public static final int CELL_SIZE = 20;
    public static int row = height / CELL_SIZE;
    public static int col = width / CELL_SIZE;
    private Snake snake;
    private Fruit fruit;
    private Timer t;
    private int speed;
    private static String direction;
    private boolean allowKeyPress;
    private int score;
    private int highest_score;
    private int fruitEatenCount = 0;
    String desktop = System.getProperty("user.home") + "/Desktop/";
    String myScoreFile = desktop + "highScore.txt";

    public Main() {
        read_highest_score();
        reset();
        addKeyListener(this);
    }

    private void setTimer() {
        t = new Timer();
        t.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                repaint();
            }
        }, 0, speed);
    }

    // 遊戲重來要做的一些設置
    private void reset() {
        // 初始化速度、分數
        score = 0;
        speed = 100;
        if (snake != null) {
            snake.getSnakeBody().clear();
        }
        allowKeyPress = true;
        direction = "Right";
        snake = new Snake();
        fruit = new Fruit();
        setTimer();
    }

    private void increaseSpeed() {
        speed -= 10; // 每次減少10毫秒來增加速度
        if (speed < 20) {
            speed = 20; // 設置一個最低速度限制
        }
        // 重新設置 Timer 以更新速度
        t.cancel();
        setTimer();
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(width, height);
    }

    @Override
    public void paintComponent(Graphics g) {
        // check if snake eat itself
        ArrayList<Node> snake_body = snake.getSnakeBody();
        Node Head = snake_body.get(0);
        for (int i = 1; i < snake_body.size(); i++) {
            if (Head.x == snake_body.get(i).x && Head.y == snake_body.get(i).y) {
                allowKeyPress = false;
                t.cancel();
                t.purge();
                write_score();
                int response = JOptionPane.showOptionDialog(this, "Game Over!! Your score is : " + score + "\nThe highest :" + highest_score + "\nWould you like to continue ??", "Game over", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE, null, null, JOptionPane.YES_OPTION);
                switch (response) {
                    case JOptionPane.CLOSED_OPTION:
                        System.exit(0);
                        break;
                    case JOptionPane.NO_OPTION:
                        System.exit(0);
                        break;
                    case JOptionPane.YES_OPTION:
                        reset();
                        return;
                }
            }
        }

        // draw a black background
        g.fillRect(0, 0, width, height);
        fruit.drawFruit(g);
        snake.drawSnake(g);

        // remove the tail and put it in head
        int snakeX = snake.getSnakeBody().get(0).x;
        int snakeY = snake.getSnakeBody().get(0).y;

        //轉彎，判斷方向
        switch (direction) {
            case "Right" -> snakeX += CELL_SIZE;
            case "Left" -> snakeX -= CELL_SIZE;
            case "Up" -> snakeY -= CELL_SIZE;
            case "Down" -> snakeY += CELL_SIZE;
        }

        Node newHead = new Node(snakeX, snakeY);

        // Check if the snake eats fruit
        if (snake.getSnakeBody().get(0).x == fruit.getX() && snake.getSnakeBody().get(0).y == fruit.getY()) {
            System.out.println("eating fruit !");
            // 吃掉果實
            fruit.setNewLocations(snake);
            fruit.drawFruit(g);
            score++;
            fruitEatenCount++;
            // 吃五個果實加速一次
            if (fruitEatenCount % 5 == 0) {
                increaseSpeed();
                System.out.println("You have eaten " + fruitEatenCount + " goddamn!!");
                System.out.println("Now speed is " + speed);
            }
        } else {
            snake.getSnakeBody().remove(snake.getSnakeBody().size() - 1);
        }
        snake.getSnakeBody().add(0, newHead);

        allowKeyPress = true;
        requestFocusInWindow();
    }


    public static void main(String[] args) {
        JFrame window = new JFrame("com.comany.Snake Game");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setContentPane(new Main());
        window.pack();
        window.setLocationRelativeTo(null);
        window.setVisible(true);
        window.setResizable(false);
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (allowKeyPress) {
            if (e.getKeyCode() == 37 && !direction.equals("Right")) {
                direction = "Left";
            } else if (e.getKeyCode() == 38 && !direction.equals("Down")) {
                direction = "Up";
            } else if (e.getKeyCode() == 39 && !direction.equals("Left")) {
                direction = "Right";
            } else if (e.getKeyCode() == 40 && !direction.equals("Up")) {
                direction = "Down";
            }
        }
        allowKeyPress = false;
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    private void read_highest_score() {
        try {
            File myObj = new File(myScoreFile);
            Scanner myReader = new Scanner(myObj);
            highest_score = myReader.nextInt();
            myReader.close();
        } catch (FileNotFoundException e) {
            highest_score = 0;
            try {
                File myObj = new File(myScoreFile);
                if (myObj.createNewFile()) {
                    System.out.println("File created : " + myObj.getName());
                }
                FileWriter myWriter = new FileWriter(myObj.getName());
                myWriter.write("" + 0);
            } catch (IOException ex) {
                System.out.println("An I/O error occurred");
                ex.printStackTrace();
            }
        }
    }

    private void write_score() {
        try {
            FileWriter myWriter = new FileWriter(myScoreFile);
            if (score > highest_score) {
                myWriter.write("" + score);
                highest_score = score;
            } else {
                myWriter.write("" + highest_score);
            }
            myWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
