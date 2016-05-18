package com.rexlai.snake;

/**
 * Created by rexlai on 2016/5/18.
 */
public interface GameController {

    void changeDirection(Direction direction);

    GameStatus getGameStatus();

    void changeStatus(GameStatus status);

}
