package com.rexlai.snake;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class SnakeActivity extends AppCompatActivity implements View.OnClickListener, GameStatusListener {

    private SnakeView snakeView;

    private int score = 0;

    private long gameSpeed = 900L;

    private TextView difficultyTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_snake);
        initActionBar();
        initSnakeView();
        initButtons();
        initDifficulty();
    }

    private void initDifficulty() {
        difficultyTextView = (TextView) findViewById(R.id.level);
        difficultyTextView.setText(String.format(getString(R.string.difficulty), 1));
        difficultyTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (snakeView.getGameStatus() == GameStatus.PLAYING || snakeView.getGameStatus() == GameStatus.PLAYING) {
                    return;
                }
                showDiffcultyDialog();
            }
        });
    }

    private void showDiffcultyDialog() {

        final String[] diffcultyArray = getResources().getStringArray(R.array.level);
        new AlertDialog.Builder(this).setCancelable(false)
                .setItems(diffcultyArray, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        setDifficulty(diffcultyArray[which]);
                        dialog.cancel();
                    }
                })
                .show();
    }

    private void setDifficulty(String difficultyLevel) {
        difficultyTextView.setText(String.format(getString(R.string.difficulty), difficultyLevel));
        long level = Long.valueOf(difficultyLevel);
        gameSpeed = 1000 - (100 * level);
    }

    private void initButtons() {
        findViewById(R.id.button_down).setOnClickListener(this);
        findViewById(R.id.button_up).setOnClickListener(this);
        findViewById(R.id.button_right).setOnClickListener(this);
        findViewById(R.id.button_left).setOnClickListener(this);
        findViewById(R.id.button_game_control).setOnClickListener(this);

    }

    private void initActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
    }

    private void initSnakeView() {
        snakeView = (SnakeView) findViewById(R.id.snake);
        snakeView.setGameStatusListener(this);
        resetGame();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_down:
                snakeView.changeDirection(Direction.DOWN);
                break;
            case R.id.button_up:
                snakeView.changeDirection(Direction.UP);
                break;
            case R.id.button_right:
                snakeView.changeDirection(Direction.RIGHT);
                break;
            case R.id.button_left:
                snakeView.changeDirection(Direction.LEFT);
                break;
            case R.id.button_game_control:
                controlGame();
                break;
        }
    }

    /**
     * Use to control game status.
     */
    private void controlGame() {
        switch (snakeView.getGameStatus()) {
            case READY:
                ((Button) findViewById(R.id.button_game_control)).setText(getText(R.string.button_pause));
                snakeView.changeStatus(GameStatus.PLAYING);
                break;
            case PAUSE:
                ((Button) findViewById(R.id.button_game_control)).setText(getText(R.string.button_pause));
                snakeView.changeStatus(GameStatus.PLAYING);
                break;
            case PLAYING:
                ((Button) findViewById(R.id.button_game_control)).setText(getText(R.string.button_resume));
                snakeView.changeStatus(GameStatus.PAUSE);
                break;
            case OVER:
                ((Button) findViewById(R.id.button_game_control)).setText(getText(R.string.start));
                snakeView.changeStatus(GameStatus.READY);
                snakeView.initNewGame(gameSpeed);
                break;
            case NEW:
                ((Button) findViewById(R.id.button_game_control)).setText(getText(R.string.start));
                snakeView.changeStatus(GameStatus.READY);
                snakeView.initNewGame(gameSpeed);
                break;
        }
    }

    @Override
    public void scored() {
        score += (1000 - gameSpeed) / 100;
        ((TextView) findViewById(R.id.score)).setText(String.format(getString(R.string.scored), score));
    }

    @Override
    public void gameOver() {
        showScoreDialog();
    }

    /**
     * Show score after game over.
     */
    private void showScoreDialog() {
        new AlertDialog.Builder(this).setCancelable(false)
                .setTitle(R.string.game_over)
                .setMessage(String.format(getString(R.string.scored), score))
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        resetGame();
                        dialog.cancel();
                    }
                }).show();
    }

    /**
     * When game over, reset score and button.
     */
    private void resetGame() {
        score = 0;
        ((Button) findViewById(R.id.button_game_control)).setText(getText(R.string.button_new_game));
        ((TextView) findViewById(R.id.score)).setText(String.format(getString(R.string.scored), score));
    }
}
