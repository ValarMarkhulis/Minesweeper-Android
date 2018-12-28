package com.example.chris.minesweeper.feature;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import MinesweeperLogic.Game;
import MinesweeperUI.GUI;

public class GameActivity extends AppCompatActivity implements View.OnClickListener {
    int amounts = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        View view_game = findViewById(R.id.game);
        ImageButton[] buttons = new ImageButton[160];
        buttons[0] = findViewById(R.id.field00);
        buttons[1] = findViewById(R.id.field01);
        buttons[2] = findViewById(R.id.field02);
        buttons[3] = findViewById(R.id.field03);

        try {
            for (ImageButton button : buttons) {
                button.setOnClickListener(this);
            }
        } catch (Exception ex) {

        }


        //Fullscreen
        this.getWindow().getDecorView();
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN |
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION |
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

        if (savedInstanceState == null) {
            Game game = new Game();
            GUI gui = new GUI(game, view_game);
        }

    }

    @Override
    public void onClick(View v) {
        //Find the button
        int id = v.getId() - 0x7f07003d;

        Toast.makeText(getApplicationContext(), findViewById(R.id.field00).getId() + "Is the field 0, and You have pressed on button " + id, Toast.LENGTH_SHORT).show();
    }
}
