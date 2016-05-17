package com.rexlai.snake;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class SnakeActivity extends AppCompatActivity implements View.OnClickListener{
    View contentView;
    private SnakeView snakeView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_snake);
        contentView = findViewById(R.id.main_layout);
        initScreen();
        initButtons();
    }

    private void initButtons() {
        findViewById(R.id.button_down).setOnClickListener(this);
        findViewById(R.id.button_up).setOnClickListener(this);
        findViewById(R.id.button_right).setOnClickListener(this);
        findViewById(R.id.button_left).setOnClickListener(this);
        findViewById(R.id.button_game_control).setOnClickListener(this);

    }

    private void initScreen() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        contentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        snakeView = (SnakeView) findViewById(R.id.snake);
        snakeView.setMode(SnakeView.READY);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_down:
                break;
            case R.id.button_up:
                break;
            case R.id.button_right:
                break;
            case R.id.button_left:
                break;
            case R.id.button_game_control:
                break;
        }
    }
}
