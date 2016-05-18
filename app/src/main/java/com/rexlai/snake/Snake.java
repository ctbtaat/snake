package com.rexlai.snake;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rexlai on 2016/5/17.
 */
public class Snake {

    private String snake = "LRRR";

    private boolean eat = false;

    private Coordinate snakeHead;

    private List<Coordinate> snakeCoordinate;

    public Snake() {
        snakeCoordinate = new ArrayList<>();
        snakeHead = new Coordinate(0, 0);
    }

    public String getCurrentDirection() {
        return "" + snake.charAt(0);
    }

    public void moveDirection(Direction direction) {
        switch (direction) {
            case UP:
                moveUp();
                break;
            case DOWN:
                moveDown();
                break;
            case RIGHT:
                moveRight();
                break;
            case LEFT:
                moveLeft();
                break;
        }
    }

    private void moveUp() {
        if (!"D".equals(getCurrentDirection())) {
            snake = "U" + snake.substring(1);
        }
    }

    private void moveDown() {
        if (!"U".equals(getCurrentDirection())) {
            snake = "D" + snake.substring(1);
        }
    }

    private void moveLeft() {
        if (!"R".equals(getCurrentDirection())) {
            snake = "L" + snake.substring(1);
        }
    }

    private void moveRight() {
        if (!"L".equals(getCurrentDirection())) {
            snake = "R" + snake.substring(1);
        }
    }

    public void move() {
        String tail;
        if (!eat) {
            tail = snake.substring(1, snake.length() - 1);
        } else {
            tail = snake.substring(1, snake.length());
            eat = false;
        }
        String head = getCurrentDirection();
        String neck = "D";
        if ("D".equals(head)) {
            neck = "U";
            snakeHead.setY(snakeHead.getY() + 1);
        } else if ("U".equals(head)) {
            neck = "D";
            snakeHead.setY(snakeHead.getY() - 1);
        } else if ("R".equals(head)) {
            neck = "L";
            snakeHead.setX(snakeHead.getX() + 1);
        } else if ("L".equals(head)) {
            neck = "R";
            snakeHead.setX(snakeHead.getX() - 1);
        }
        snake = head + neck + tail;
    }

    public void layoutSnack(SnakeGenerator snakeGenerator) {
        snakeCoordinate = new ArrayList<>();
        snakeCoordinate.add(0, snakeHead);
        snakeGenerator.layoutSnakeHead(snakeHead);
        int x = snakeHead.getX();
        int y = snakeHead.getY();
        for (int i = 1; i < snake.length(); i++) {
            char c = snake.charAt(i);
            switch (c) {
                case 'U':
                    y--;
                    break;
                case 'D':
                    y++;
                    break;
                case 'R':
                    x++;
                    break;
                case 'L':
                    x--;
                    break;
            }
            Coordinate body = new Coordinate(x, y);
            snakeCoordinate.add(i, body);
            snakeGenerator.layoutSnakeBody(body);
        }
    }

    public void eatFood() {
        eat = true;
    }

    public void setSnakeHead(Coordinate snakeHead) {
        this.snakeHead = snakeHead;
    }

    public Coordinate getSnakeHead() {
        return snakeHead;
    }

    public String getSnake() {
        return snake;
    }

    public List<Coordinate> getSnakeCoordinate() {
        return snakeCoordinate;
    }
}
