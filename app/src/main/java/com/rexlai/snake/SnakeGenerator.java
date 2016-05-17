package com.rexlai.snake;

/**
 * Created by rexlai on 2016/5/17.
 */
public interface SnakeGenerator {

    void layoutSnakeHead(Coordinate coordinate);

    void layoutSnakeBody(Coordinate coordinate);
}
