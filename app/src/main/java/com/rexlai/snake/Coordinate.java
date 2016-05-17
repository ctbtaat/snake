package com.rexlai.snake;

/**
 * Created by rexlai on 2016/5/17.
 */
public class Coordinate {

    private int x;

    private int y;

    public Coordinate(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    @Override
    public boolean equals(Object o) {
        return  (this.x == ((Coordinate)o).getX()) && (this.y == ((Coordinate)o).getY());
    }
}
