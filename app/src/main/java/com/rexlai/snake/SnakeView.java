package com.rexlai.snake;

import android.content.Context;
import android.content.res.Resources;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.TextView;

import java.util.Random;

/**
 * Created by rexlai on 2016/5/17.
 */
public class SnakeView extends TileView {

    private static final String TAG = "SnakeView";

    /**
     * Current mode of application: READY to run, RUNNING, or you have already
     * lost. static final ints are used instead of an enum for performance
     * reasons.
     */
    private int mMode = READY;
    public static final int PAUSE = 0;
    public static final int READY = 1;
    public static final int RUNNING = 2;
    public static final int LOSE = 3;

    /**
     * Labels for the drawables that will be loaded into the TileView class
     */
    private static final int SNAKE_HEAD = 1;
    private static final int SNAKE_BODY = 2;
    private static final int WALL = 3;

    /**
     * mScore: used to track the number of apples captured mMoveDelay: number of
     * milliseconds between snake movements. This will decrease as apples are
     * captured.
     */
    private long mScore = 0;
    // TODO private long mMoveDelay = 600;
    /**
     * mLastMove: tracks the absolute time when the snake last moved, and is
     * used to determine if a move should be made based on mMoveDelay.
     */
    // TODO private long mLastMove;

    /**
     * mStatusText: text shows to the user in some run states
     */
    private TextView mStatusText;

    /**
     * mSnakeTrail: a list of Coordinates that make up the snake's body
     * snakeFoodList: the secret location of the juicy apples the snake craves.
     */
    private Coordinate snakeFood;

    /**
     * Everyone needs a little randomness in their life
     */
    private static final Random rnd = new Random();

    /**
     * Create a simple handler that we can use to cause animation to happen. We
     * set ourselves as a target and we can use the sleep() function to cause an
     * update/invalidate to occur at a later date.
     */
    private RefreshHandler refreshHandler = new RefreshHandler();

    class RefreshHandler extends Handler {

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

    private void initSnakeView() {
        setFocusable(true);

        Resources r = this.getContext().getResources();

        resetTiles(4);
        loadTile(SNAKE_BODY, r.getDrawable(R.drawable.body));
        loadTile(SNAKE_HEAD, r.getDrawable(R.drawable.head));
        loadTile(WALL, r.getDrawable(R.drawable.wall));

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        initNewGame();
        update();
        return super.onTouchEvent(event);
    }

    private void initNewGame() {
        initSnake();
        addRandomFood();
        mScore = 0;
    }

    private Snake snake;

    private void initSnake() {
        int x = 1 + rnd.nextInt(xTileCount - 2);
        int y = 1 + rnd.nextInt(yTileCount - 2);
        snake = new Snake();
        if (x + snake.getSnake().length() >= xTileCount) {
            x = xTileCount - snake.getSnake().length();
        }
        snake.setSnakeHead(new Coordinate(x, y));
    }


    /**
     * Sets the TextView that will be used to give information (such as "Game
     * Over" to the user.
     *
     * @param newView
     */
    public void setTextView(TextView newView) {
        mStatusText = newView;
    }

    /**
     * Updates the current mode of the application (RUNNING or PAUSED or the
     * like) as well as sets the visibility of textview for notification
     *
     * @param newMode
     */
    public void setMode(int newMode) {
        int oldMode = mMode;
        mMode = newMode;

        if (newMode == RUNNING & oldMode != RUNNING) {
//            mStatusText.setVisibility(View.INVISIBLE);
            update();
            return;
        }

        Resources res = getContext().getResources();
        CharSequence str = "";
//        if (newMode == PAUSE) {
//            str = res.getText(R.string.mode_pause);
//        }
//        if (newMode == READY) {
//            str = res.getText(R.string.mode_ready);
//        }
//        if (newMode == LOSE) {
//            str = res.getString(R.string.mode_lose_prefix) + mScore + res.getString(R.string.mode_lose_suffix);
//        }

//        mStatusText.setText(str);
//        mStatusText.setVisibility(View.VISIBLE);
    }

    /**
     * Selects a random location within the garden that is not currently covered
     * by the snake. Currently _could_ go into an infinite loop if the snake
     * currently fills the garden, but we'll leave discovery of this prize to a
     * truly excellent snake-player.
     */
    private void addRandomFood() {
        Coordinate newFood = null;
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
     * Handles the basic update loop, checking to see if we are in the running
     * state, determining if a move should be made, updating the snake's
     * location.
     */
    public void update() {
//        if (mMode == RUNNING) {
			/*
			 * TODO long now = System.currentTimeMillis();
			 *
			 * if (now - mLastMove > mMoveDelay) { clearTiles(); updateWalls();
			 * updateSnake(); updateApples(); mLastMove = now; }
			 */
        clearTiles();
        updateWalls();
        updateSnake();
        updateFood();
//        }
        snake.move();
        refreshHandler.sleep(Snake.speed);
    }

    /**
     * Draws some walls.
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
     * Draws some apples.
     */
    private void updateFood() {
        setTile(SNAKE_BODY, snakeFood.getX(), snakeFood.getX());
    }

    /**
     * Figure out which way the snake is going, see if he's run into anything
     * (the walls, himself, or an apple). If he's not going to die, we then add
     * to the front and subtract from the rear in order to simulate motion. If
     * we want to grow him, we don't subtract from the rear.
     */
    private void updateSnake() {
        if (snake.getSnakeHead().equals(snakeFood)) {
            snake.eatFood();
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
}

