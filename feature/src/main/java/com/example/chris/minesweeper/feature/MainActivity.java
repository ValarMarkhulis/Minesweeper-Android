package com.example.chris.minesweeper.feature;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button btn_play;
    Button btn_highscore;
    Button btn_exit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Setting up the buttons
        btn_play = findViewById(R.id.button_play);
        btn_play.setOnClickListener(this);
        btn_highscore = findViewById(R.id.button_highscore);
        btn_highscore.setOnClickListener(this);
        btn_exit = findViewById(R.id.button_exit);
        btn_exit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.button_play) {
            Intent Act_SpilActivity = new Intent(getApplicationContext(), GameActivity.class);
            startActivity(Act_SpilActivity);
        } else if (v.getId() == R.id.button_highscore) {
            final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
            Long highscore_time = prefs.getLong("highscore", -1);
            String displayMsg = "Your Fastest time: ";
            SimpleDateFormat sdf = new SimpleDateFormat("mm:ss");
            String time = sdf.format(new Date(highscore_time));

            if (highscore_time == -1) {
                displayMsg += "NaN";
            } else
                displayMsg += "" + time;

            Snackbar mySnackbar = Snackbar.make(findViewById(R.id.mainActivity),
                    displayMsg, Snackbar.LENGTH_SHORT);
            mySnackbar.show();
        } else if (v.getId() == R.id.button_exit) {
            Toast.makeText(getApplicationContext(), "You have pressed the exit button", Toast.LENGTH_SHORT).show();
            finish();
            System.exit(0);
        }
    }
}
