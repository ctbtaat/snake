package com.rexlai.snake;

/**
 * Created by rexlai on 2016/5/18.
 */
public enum GameStatus {

    READY("ready", 0), PAUSE("pause", 1), PLAYING("playing", 2), OVER("over", 3), NEW("new", 4);

    GameStatus(String status, int i) {

    }
}
