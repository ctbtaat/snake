package com.rexlai.snake;

import android.content.Context;
import android.content.res.Resources;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;

import java.util.Random;

/**
 * Created by rexlai on 2016/5/17.
 */
public class SnakeView extends TileView implements GameController {

    private static final int SNAKE_BODY = 1;
    private static final int SNAKE_HEAD = 2;
    private static final int WALL = 3;

    private Snake snake;
    private Coordinate snakeFood;
    private Random rnd = new Random();
    private GameRefreshHandler refreshHandler = new GameRefreshHandler();
    private GameStatus status = GameStatus.NEW;
    private GameStatusListener listener;
    private long gameSpeed;

    private class GameRefreshHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {
            SnakeView.this.update();
            SnakeView.this.invalidate();
        }

        public void sleep(long delayMillis) {
            this.removeMessages(0);
            sendMessageDelayed(obtainMessage(0), delayMillis);
        }
    }

    ;

    /**
     * Constructs a SnakeView based on inflation from XML
     *
     * @param context
     * @param attrs
     */
    public SnakeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initSnakeView();
    }

    public SnakeView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initSnakeView();
    }

    /**
     * Initial the snake view, head, body and wall.
     */
    private void initSnakeView() {
        setFocusable(true);
        Resources r = this.getContext().getResources();
        resetTiles(4);
        loadTile(SNAKE_BODY, r.getDrawable(R.drawable.body));
        loadTile(SNAKE_HEAD, r.getDrawable(R.drawable.head));
        loadTile(WALL, r.getDrawable(R.drawable.wall));

    }

    /**
     * Initial new snake, food, gameSpeed determine refresh rate.
     * @param gameSpeed
     */
    public void initNewGame(long gameSpeed) {
        this.gameSpeed = gameSpeed;
        initSnake();
        addRandomFood();
        update();
        layoutScreen();
    }

    /**
     * Initial snake start position, and it will be inside wall
     */
    private void initSnake() {
        int x = 1 + rnd.nextInt(xTileCount - 2);
        int y = 1 + rnd.nextInt(yTileCount - 2);
        snake = new Snake();
        if (x + snake.getSnake().length() >= xTileCount) {
            x = xTileCount - snake.getSnake().length() - 1;
        }
        snake.setSnakeHead(new Coordinate(x, y));
    }

    /**
     * Random position to add food, need check the snake position and don't add on snake.
     */
    private void addRandomFood() {
        Coordinate newFood;
        boolean overlapping = false;
        do {
            int x = 1 + rnd.nextInt(xTileCount - 2);
            int y = 1 + rnd.nextInt(yTileCount - 2);
            newFood = new Coordinate(x, y);
            for (Coordinate coordinate : snake.getSnakeCoordinate()) {
                if (coordinate.equals(newFood)) {
                    overlapping = true;
                }
            }
        }
        while (overlapping);
        snakeFood = newFood;
        updateFood();
    }

    /**
     * Update the snake location when game status is playing,
     * the speed determine snake refresh rate.
     */
    private void update() {
        if (status == GameStatus.PLAYING) {
            layoutScreen();
            snake.move();
        }
        refreshHandler.sleep(gameSpeed);
    }

    /**
     * Clean all item, and redraw wall, snake, and food.
     */
    private void layoutScreen() {
        clearTiles();
        updateWalls();
        updateSnake();
        updateFood();
    }

    /**
     * Draw the walls.
     */
    private void updateWalls() {
        for (int x = 0; x < xTileCount; x++) {
            setTile(WALL, x, 0);
            setTile(WALL, x, yTileCount - 1);
        }
        for (int y = 1; y < yTileCount - 1; y++) {
            setTile(WALL, 0, y);
            setTile(WALL, xTileCount - 1, y);
        }
    }

    /**
     * Draw the food.
     */
    private void updateFood() {
        setTile(SNAKE_BODY, snakeFood.getX(), snakeFood.getY());
    }

    /**
     * In this function, first, it will determine snake eats food or not,
     * second, it will determine the snake out of boundary,
     * and layout the snake.
     */
    private void updateSnake() {
        if (snake.getSnakeHead().equals(snakeFood)) {
            snake.eatFood();
            listener.scored();
            addRandomFood();
        } else {
            int snakeHeadX = snake.getSnakeHead().getX();
            int snakeHeadY = snake.getSnakeHead().getY();
            if (snakeHeadX <= 0 || snakeHeadY <= 0) {
                changeStatus(GameStatus.OVER);
                listener.gameOver();
            } else if (snakeHeadX + 1 >= xTileCount || snakeHeadY + 1 >= yTileCount) {
                changeStatus(GameStatus.OVER);
                listener.gameOver();
            }
        }

        snake.layoutSnack(new SnakeGenerator() {
            @Override
            public void layoutSnakeHead(Coordinate coordinate) {
                setTile(SNAKE_HEAD, coordinate.getX(), coordinate.getY());
            }

            @Override
            public void layoutSnakeBody(Coordinate coordinate) {
                setTile(SNAKE_BODY, coordinate.getX(), coordinate.getY());
            }
        });
    }

    public void setGameStatusListener(GameStatusListener listener) {
        this.listener = listener;
    }

    @Override
    public void changeDirection(Direction direction) {
        if (status == GameStatus.PLAYING && direction != null && snake != null) {
            snake.moveDirection(direction);
        }
    }

    @Override
    public GameStatus getGameStatus() {
        return status;
    }

    @Override
    public void changeStatus(GameStatus status) {
        this.status = status;
    }

}

