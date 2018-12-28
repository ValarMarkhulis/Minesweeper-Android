package com.example.chris.minesweeper.feature;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button btn_spil;
    Button btn_highscore;
    Button btn_exit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_spil = findViewById(R.id.button_spil);
        btn_spil.setOnClickListener(this);
        btn_highscore = findViewById(R.id.button_highscore);
        btn_highscore.setOnClickListener(this);
        btn_exit = findViewById(R.id.button_exit);
        btn_exit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.button_spil) {
            Intent Act_SpilActivity = new Intent(getApplicationContext(), GameActivity.class);
            startActivity(Act_SpilActivity);
        } else if (v.getId() == R.id.button_highscore) {
            Toast.makeText(getApplicationContext(), "You have pressed on the highscore button", Toast.LENGTH_SHORT).show();
        } else if (v.getId() == R.id.button_exit) {
            Toast.makeText(getApplicationContext(), "You have pressed the exit button", Toast.LENGTH_SHORT).show();
        }
    }
}
