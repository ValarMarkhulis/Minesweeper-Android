package com.example.chris.minesweeper.feature;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import MinesweeperLogic.Game;
import MinesweeperUI.Dialog;

//for highscore
// implement SharedPreferences.OnSharedPreferenceChangeListener

public class GameActivity extends AppCompatActivity implements View.OnClickListener, View.OnLongClickListener, Dialog.onButtonClick {
    private static final String TAG = "GameActivity";
    int amounts = 0;
    private final int N_ROWS = 16;
    private final int N_COLS = 10;
    int amount_of_fields = 160;
    boolean game_started = false;
    Game game;
    boolean endGame = false;
    ImageButton[] buttons;
    Button retry;

    int flagSet = 0;
    TextView flagsetTextview;
    Long start_time = Long.valueOf(0);
    Long end_time = Long.valueOf(0);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        View view_game = findViewById(R.id.game);
        buttons = new ImageButton[160];

        Resources res = getResources();
        for (int i = 0; i < 160; i++) {
            String number = "" + i;
            if (i < 10) {
                number = "0" + i;
            }
            String field = "field" + number;
            //Find all buttons and put them into the buttons[] and setup onClickListeners for each of them
            buttons[i] = findViewById(res.getIdentifier(field, "id", getPackageName()));
            buttons[i].setOnLongClickListener(this);
            buttons[i].setOnClickListener(this);

            //Give each button a tag, which is used in the onClick method
            findViewById(res.getIdentifier(field, "id", getPackageName())).setTag(i);
        }


        //Fullscreen
        this.getWindow().getDecorView();
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN |
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION |
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        if (savedInstanceState == null) {
            game = new Game();
            flagsetTextview = findViewById(R.id.flagSet);
            flagsetTextview.setText("Flags set: " + game.flagsSet);
        }

    }

    @Override
    public void onClick(View v) {

        //openDialogWin(true);


        if (endGame) {
            return;
        }
        //Buttons are found by the Tag which is given to it in the for loop where the buttons[] is initiliazed.
        //Toast.makeText(getApplicationContext(), "You pressed on: " + String.valueOf(v.getTag()), Toast.LENGTH_SHORT).show();
        int number = 0;

        try {
            number = Integer.parseInt(String.valueOf(v.getTag()));
        } catch (Exception ex) {
            System.err.println("ERROR 0 getting the tag!");
            ex.printStackTrace();
            return;
        }
        if (number >= amount_of_fields || number < 0) {
            number = 0;
            System.err.println("GetTag returned a value less than 0 or bigger than 160." +
                    " 0 was given to the random map generation");
        }

        if (!game_started) {
            //When the game has not started yet/ the first click of the game
            start_time = System.currentTimeMillis();

            try {
                game.generateRandomMap(number);
                game.drawAsciiBoard();
            } catch (Exception ex) {
                System.err.println("ERROR 1!");
                ex.printStackTrace();
                return;
            }
            game_started = true;
        }

        //Do this every time
        int status = 0;
        try {
            status = game.guess(number);
            if (status == -1) {
                //TODO: Show all fields!
                game.showAllFields(game.bombWhichKilledYou);
                endGame = true;
                openDialogWin(false);
                //Slut spil
            } else if (status == 0) {
                //Slut spil
                endGame = true;
                openDialogWin(true);
            }
        } catch (Exception ex) {
            System.err.println("ERROR 2!");
            ex.printStackTrace();
            return;
        }


        Resources res = getResources();
        //buttons[0].setBackgroundResource(R.drawable.b13);

        for (int i = 0; i < N_ROWS; i++) {
            for (int j = 0; j < N_COLS; j++) {
                int cell = game.gameBoard.get(10 * i + j).getFieldImgType(endGame);

                boolean debug = false;
                if (debug) {
                    int test = 10 * i + j;
                    System.out.println("Nr. " + test + " is a " + cell);
                }
                //buttons[10*i+j].setBackgroundResource(R.drawable.b2);
                changeImg(cell, 10 * i + j);
            }
        }
    }

    private void openDialogWin(boolean Win) {

        /*
        end_time = System.currentTimeMillis() - start_time;
        if( end_time > 3600000){
            //If the time is over 1 hour, the highscore "time" will overflow, so heres a fix
            // set the time to 59 min and 99 seconds
            end_time =3639000L;
            Toast.makeText(getApplicationContext(), "It took you over an HOUR! Nice try :P", Toast.LENGTH_SHORT).show();
        }
        SimpleDateFormat sdf = new SimpleDateFormat("mm:ss");
        String time = sdf.format(end_time);

        System.out.println("sdf giver"+sdf.format(3639000L));


        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        /*
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("highscore", "2:00");
        editor.apply();


        int highscore_time_min = prefs.getInt("highscore_min",-1);
        int highscore_time_sek = prefs.getInt("highscore_sek",-1);

        if(highscore_time_min == -1 || highscore_time_sek == -1){
            SharedPreferences.Editor editor = prefs.edit();
            highscore_time_min = Integer.parseInt(time.substring(0,2));
            highscore_time_sek = Integer.parseInt(time.substring(3,5));
            editor.putInt("highscore_min", highscore_time_min);
            editor.putInt("highscore_sek", highscore_time_sek);
            editor.apply();
        }

        //Compare time vs the highscore
        int time_time_min = Integer.parseInt(time.substring(0,2));
        int time_time_sek = Integer.parseInt(time.substring(3,5));
        */

        //String highscore_time = "1:00";

        Bundle bundle = new Bundle();
        int highscore_time_min = 0;
        int highscore_time_sek = 0;
        int time_time_min = 0;
        int time_time_sek = 0;

        if (Win) {
            //Win
            bundle.putString("first", "Best time: " + highscore_time_min + ":" + highscore_time_sek);
            bundle.putString("second", "Your time: " + time_time_min + ":" + time_time_sek);
            bundle.putString("title", "You won!");
        } else {
            //Loose
            bundle.putString("first", game.getBoombsleft() + " bombs left");
            bundle.putString("second", "Your time: " + time_time_min + ":" + time_time_sek);
            bundle.putString("third", "Best time: " + highscore_time_min + ":" + highscore_time_sek);
            bundle.putString("title", "You lost!");
        }

        Dialog dialog = new Dialog();
        dialog.setArguments(bundle);
        dialog.show(getSupportFragmentManager(), "dialog");

    }

    private void changeImg(int imgNr, int fieldID) {
        switch (imgNr) {
            case 0:
                buttons[fieldID].setBackgroundResource(R.drawable.b10);
                break;
            case 1:
                buttons[fieldID].setBackgroundResource(R.drawable.b1);
                break;
            case 2:
                buttons[fieldID].setBackgroundResource(R.drawable.b2);
                break;
            case 3:
                buttons[fieldID].setBackgroundResource(R.drawable.b3);
                break;
            case 4:
                buttons[fieldID].setBackgroundResource(R.drawable.b4);
                break;
            case 5:
                buttons[fieldID].setBackgroundResource(R.drawable.b5);
                break;
            case 6:
                buttons[fieldID].setBackgroundResource(R.drawable.b6);
                break;
            case 7:
                buttons[fieldID].setBackgroundResource(R.drawable.b7);
                break;
            case 8:
                buttons[fieldID].setBackgroundResource(R.drawable.b8);
                break;
            case 9:
                buttons[fieldID].setBackgroundResource(R.drawable.b9);
                break;
            case 10:
                buttons[fieldID].setBackgroundResource(R.drawable.b0);
                break;
            case 11:
                buttons[fieldID].setBackgroundResource(R.drawable.b11);
                break;
            case 12:
                buttons[fieldID].setBackgroundResource(R.drawable.b12);
                break;
            case 13:
                buttons[fieldID].setBackgroundResource(R.drawable.b13);
                break;
        }

    }


    @Override
    public boolean onLongClick(View v) {

        if (endGame) {
            return false;
        }
        //You can only set flags if the game has started

        if (game_started) {
            int fieldID = Integer.parseInt(String.valueOf(v.getTag()));
            //Toggle flag
            if (game.gameBoard.size() > fieldID) {
                //Toast.makeText(getApplicationContext(), "You made a long press on button: " + fieldID, Toast.LENGTH_SHORT).show();
                int status = game.toggleFlag(fieldID);

                int cell = game.gameBoard.get(fieldID).getFieldImgType(endGame);
                changeImg(cell, fieldID);

                if (status == 0) {
                    //Spil vundet
                    //stop spillet
                    endGame = true;
                    openDialogWin(true);
                }
            } else {
                Toast.makeText(getApplicationContext(), "A long press on " + fieldID + "was registered," +
                        " but not accepted", Toast.LENGTH_SHORT).show();
            }
        }

        //update the flag set textview
        flagsetTextview.setText("Flags set: " + game.flagsSet);

        //do not process the signal anymore (prevents processing of a "single click" right after)
        return true;
    }

    @Override
    public void retryButton(boolean pressed) {
        Log.d(TAG, "retryButton returned: " + pressed);

        if (pressed) {
            //Restart game
            endGame = false;
            game_started = false;

            //Update all the fields
            for (int i = 0; i < N_ROWS; i++) {
                for (int j = 0; j < N_COLS; j++) {
                    //Set all tiles to hidden
                    changeImg(10, 10 * i + j);
                }
            }
            //Make a new instance of the game
            game = new Game();

            //Reset the flag set textview
            flagsetTextview.setText("Flags set: 0");

        } else {
            // Go to menu
            finish();
        }
        try {
            TextView dialog_thirdLine = findViewById(R.id.dialog_thirdLine);
            dialog_thirdLine.setVisibility(View.INVISIBLE);
        } catch (Exception ex) {

        }

    }

    /*
    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

    }
    */
}
