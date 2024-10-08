package com.company;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class Fruit {

    private int x;
    private int y;
    private ImageIcon img;


    public Fruit() {
        img = new ImageIcon(getClass().getResource("orange.png"));
//        img = new ImageIcon("orange.png");
        this.x = (int) (Math.floor(Math.random() * Main.col) * Main.CELL_SIZE);
        this.y = (int) (Math.floor(Math.random() * Main.row) * Main.CELL_SIZE);
    }


    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void drawFruit(Graphics g) {
        img.paintIcon(null, g, this.x, this.y);
//        g.setColor(Color.YELLOW);
//        g.fillOval(x, y, com.company.Main.CELL_SIZE, com.company.Main.CELL_SIZE);
    }

    public void setNewLocations(Snake s) {
        int new_x;
        int new_y;
        boolean overlapping;
        //水果不能跟蛇重疊
        do {
            new_x = (int) (Math.floor(Math.random() * Main.col) * Main.CELL_SIZE);
            new_y = (int) (Math.floor(Math.random() * Main.col) * Main.CELL_SIZE);
            overlapping = check_overlap(new_x, new_y, s);
        } while (overlapping);
        this.x = new_x;
        this.y = new_y;
    }

    private boolean check_overlap(int x, int y, Snake s) {
        ArrayList<Node> snake_body = s.getSnakeBody();
        for (Node node : snake_body) {
            if (x == node.x && y == node.y) {
                return true;
            }
        }
        return false;
    }
}

