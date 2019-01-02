package com.example.chris.minesweeper.feature;

import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

import MinesweeperLogic.Game;
import MinesweeperUI.Dialog;

public class GameActivity extends AppCompatActivity implements View.OnClickListener, View.OnLongClickListener, Dialog.onButtonClick, SharedPreferences.OnSharedPreferenceChangeListener {
    private static final String TAG = "GameActivity";
    private final int N_ROWS = 16;
    private final int N_COLS = 10;
    int amount_of_fields = 160;
    boolean game_started = false;
    Game game;
    boolean endGame = false;
    ImageButton[] buttons;

    TextView flagsetTextview;
    TextView timeTextview;
    CountDownTimer cdt;
    Button gameboard_reset_button;
    Button gameboard_menu_button;
    private long end_time = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        View view_game = findViewById(R.id.game);

        gameboard_reset_button = findViewById(R.id.gameboard_reset_button);
        gameboard_reset_button.setOnClickListener(this);
        gameboard_menu_button = findViewById(R.id.gameboard_menu_button);
        gameboard_menu_button.setOnClickListener(this);

        //The buttons are kept in a ImageButton array
        buttons = new ImageButton[160];

        Resources res = getResources();
        //Iterate through and find the buttons, set OnLong and Onclick listeners
        // and at last, give it a tag which is its field #
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


        fullscreenAndhideActionbar();

        if (savedInstanceState == null) {
            game = new Game();
            flagsetTextview = findViewById(R.id.flagSet);
            flagsetTextview.setText("Flags set: " + game.flagsSet);
            timeTextview = findViewById(R.id.time);
        }

    }

    @Override
    public void onClick(View v) {
        boolean firstTime = false;

        //Ingame reset button
        if (v.getId() == R.id.gameboard_reset_button) {
            try {
                cdt.cancel();
            } catch (Exception ex) {
                ex.printStackTrace();
            }


            //Reset time
            end_time = 0;

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

            //Reset the flag and time textview
            flagsetTextview.setText("Flags set: 0");
            timeTextview.setText("Time: 00:00");
            return;
        } else if (v.getId() == R.id.gameboard_menu_button) {
            //Ingame menu button
            finish();
        }

        //Ignore button presses after the game is finished. This can happen, if the player presses
        //outside the "winner/looser" dialog which makes the two options "retry" or "go to menu"
        // disappear and the player now has to press "backwards" on the phone to go to the menu.
        if (endGame) {
            return;
        }

        //Buttons are found by the Tag which is given to it in the for loop where the buttons[] is initiliazed.
        //Toast.makeText(getApplicationContext(), "You pressed on: " + String.valueOf(v.getTag()), Toast.LENGTH_SHORT).show();
        int number = 0;

        try {
            //Try to look at the tag of the button which has been pressed. This sometimes throws
            //a "no tag can be found"/null pointer reference exception?
            number = Integer.parseInt(String.valueOf(v.getTag()));
        } catch (Exception ex) {
            //If the catch is run, the user have to press the button again and a new onClick()
            // will be called... This for some reason works?
            System.err.println("ERROR 0 getting the tag!");
            ex.printStackTrace();
            return;
        }

        // If the tag returns a invalid number
        if (number >= amount_of_fields || number < 0) {
            System.err.println("GetTag returned a value less than 0 or bigger than 160. No input" +
                    "was given to the game logic");
            return;
        }

        //When the game has not started yet/ the first click of the game
        if (!game_started) {
            //Sometimes the number var will be set to a null reference, and not be catched
            // in the first try catch.. So this needs to be here.
            try {
                game.generateRandomMap(number);
                game.drawAsciiBoard();
            } catch (Exception ex) {
                System.err.println("ERROR 1: The input to the randomMap generator was wrong!");
                ex.printStackTrace();
                return;
            }
            //From here on, the games has started
            game_started = true;
            //Its the first time, so a flag for starting the timer should be started
            firstTime = true;
        }


        //Do this every time
        int status = 0; // Holds the return value from the game logic .guess(int) method
        try {
            status = game.guess(number);
            if (status == -1) {
                //You found a bomb.. and it exploded!
                game.showAllFields(game.bombWhichKilledYou);
                endGame = true;
                openDialogWin(false); // Open a Dialog, where the player has lost the game
            } else if (status == 0) {
                // All the bombs have been flaged
                // and it is only the 24 bombs left which is still hidden.
                endGame = true;
                openDialogWin(true);
            }
        } catch (Exception ex) {
            System.err.println("ERROR 2!");
            ex.printStackTrace();
            return;
        }

        //Iterate through all the fields and update their image
        for (int i = 0; i < N_ROWS; i++) {
            for (int j = 0; j < N_COLS; j++) {
                //Find the field and get which FieldImgType it should be displayed as
                int cell = game.gameBoard.get(10 * i + j).getFieldImgType(endGame);

                changeImg(cell, 10 * i + j); // Change the field's image
            }
        }


        if (firstTime) {
            //Start a countDownTimer, that will update the GUI Time: text field
            cdt = new CountDownTimer(3600000, 1000) {
                Date date = new Date(0);

                //Every second
                public void onTick(long millisUntilFinished) {
                    SimpleDateFormat sdf = new SimpleDateFormat("mm:ss");
                    date.setTime(end_time += 1000);
                    timeTextview.setText("Time: " + sdf.format(date));
                }

                public void onFinish() {
                    timeTextview.setText("Time: 59:59");
                }


            }.start();
        }

    }

    private void openDialogWin(boolean Win) {

        cdt.cancel();

        if( end_time > 3600000){
            //If the time is over 1 hour, the highscore "time" will overflow, so heres a fix
            // set the time to 59 min and 99 seconds
            end_time = 3599000;
            Toast.makeText(getApplicationContext(), "It took you over an HOUR! Nice try :P", Toast.LENGTH_SHORT).show();
        }
        SimpleDateFormat sdf = new SimpleDateFormat("mm:ss");
        String time = sdf.format(new Date(end_time));

        //System.out.println("Din tid er : "+time+" sdf giver "+sdf.format(new Date(186000)));

        //System.out.println(new Date(end_time).compareTo(new Date(179000)));


        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);

        SharedPreferences.Editor editor = prefs.edit();
        Long highscore_time = prefs.getLong("highscore", -1);
        boolean new_highscore_flag = false;

        if (highscore_time == -1 && Win) {
            System.out.println("There were no previous highscore! " +
                    "The time " + time + " is the fastest time!");
            editor.putLong("highscore", end_time);
            editor.apply();
            highscore_time = end_time;
            new_highscore_flag = true;
        } else if (Win) {
            //There was a previous best score compare the two scores
            //Compare time vs the highscore time
            int new_highscore = new Date(end_time).compareTo(new Date(highscore_time));
            // -1: end_time < highscore_time
            // 0 : end_time == highscore_time
            // 1 : end_time > highscore_time

            if (new_highscore < 0) {
                //Update the highscore
                editor.putLong("highscore", end_time);
                editor.apply();
                highscore_time = end_time;
                new_highscore_flag = true;
            } else {
                //Do nothing
            }
        }

        Bundle bundle = new Bundle();

        if (Win) {
            //Win
            bundle.putString("first", "Best time: " + sdf.format(new Date(highscore_time)));
            bundle.putString("second", "Your time: " + sdf.format(new Date(end_time)));
            if (new_highscore_flag) {
                bundle.putString("title", "You won, and beat the highscore!");
            } else {
                bundle.putString("title", "You won!");
            }

        } else {
            //Loose
            bundle.putString("first", game.getBoombsleft() + " bombs left");
            bundle.putString("second", "Your time: " + sdf.format(new Date(end_time)));
            bundle.putString("third", "Best time: " + sdf.format(new Date(highscore_time)));
            if (game.getBoombsleft() < 6) {
                bundle.putString("title", "You lost, but it was close!");
            } else {
                bundle.putString("title", "You lost!");
            }

        }

        Dialog dialog = new Dialog();
        dialog.setArguments(bundle);
        dialog.show(getSupportFragmentManager(), "dialog");


    }

    /**
     * @param imgNr   Which image should the fieldID be set too
     * @param fieldID The field which should have its image changed
     */
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
        //You can only set flags if the game has started
        if (endGame) {
            return false;
        } else if (v.getId() == R.id.gameboard_reset_button || v.getId() == R.id.gameboard_menu_button) {
            return false;
        }


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

    /**
     * This function handles the button press from the Dialog
     *
     * @param pressed Is it the retry button which have been pressed?
     */
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

            //Reset the flag and time textview
            flagsetTextview.setText("Flags set: 0");
            timeTextview.setText("Time: 00:00");

            //Reset time
            end_time = 0;

            fullscreenAndhideActionbar();
        } else {
            // Go to menu
            finish();
        }
        //Remove the thrid line which should only be displayed when the player has lost the game
        try {
            TextView dialog_thirdLine = findViewById(R.id.dialog_thirdLine);
            dialog_thirdLine.setVisibility(View.INVISIBLE);
        } catch (Exception ex) {

        }

    }

    private void fullscreenAndhideActionbar() {
        //Set fullscreen
        this.getWindow().getDecorView();
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN |
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION |
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

        //Remove the Topbar/actionbar
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
    }


    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

    }

}
