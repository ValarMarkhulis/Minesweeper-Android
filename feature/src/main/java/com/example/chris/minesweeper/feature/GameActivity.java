package com.example.chris.minesweeper.feature;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import MinesweeperLogic.Game;

public class GameActivity extends AppCompatActivity implements View.OnClickListener, View.OnLongClickListener {
    int amounts = 0;
    private final int N_ROWS = 16;
    private final int N_COLS = 10;
    int amount_of_fields = 160;
    boolean game_started = false;
    Game game;
    boolean endGame = false;
    ImageButton[] buttons;

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

        if (savedInstanceState == null) {

            game = new Game();
            //GUI gui = new GUI(game, view_game);
        }

    }

    @Override
    public void onClick(View v) {
        if (endGame) {
            return;
        }
        //Buttons are found by the Tag which is given to it in the for loop where the buttons[] is initiliazed.
        //Toast.makeText(getApplicationContext(), "You pressed on: " + String.valueOf(v.getTag()), Toast.LENGTH_SHORT).show();
        int number = 0;

        try {
            number = Integer.parseInt(String.valueOf(v.getTag()));
        } catch (Exception ex) {
            System.err.println("ERROR getting the tag!");
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
            try {
                game.generateRandomMap(number);
                game.drawAsciiBoard();
            } catch (Exception ex) {
                System.err.println("ERROR!");
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
                //Slut spil
            } else if (status == 0) {
                //Slut spil
            }
        } catch (Exception ex) {
            System.err.println("ERROR!");
            ex.printStackTrace();
            return;
        }


        Resources res = getResources();
        //buttons[0].setBackgroundResource(R.drawable.b13);

        for (int i = 0; i < N_ROWS; i++) {
            for (int j = 0; j < N_COLS; j++) {
                int cell = game.gameBoard.get(10 * i + j).getFieldImgType(endGame);

                if (cell == 10) {
                    break;
                }
                boolean debug = false;
                if (debug) {
                    int test = 10 * i + j;
                    System.out.println("Nr. " + test + " is a " + cell);
                }
                //buttons[10*i+j].setBackgroundResource(R.drawable.b2);

                switch (cell) {
                    case 0:
                        buttons[10 * i + j].setBackgroundResource(R.drawable.b10);
                        break;
                    case 1:
                        buttons[10 * i + j].setBackgroundResource(R.drawable.b1);
                        break;
                    case 2:
                        buttons[10 * i + j].setBackgroundResource(R.drawable.b2);
                        break;
                    case 3:
                        buttons[10 * i + j].setBackgroundResource(R.drawable.b3);
                        break;
                    case 4:
                        buttons[10 * i + j].setBackgroundResource(R.drawable.b4);
                        break;
                    case 5:
                        buttons[10 * i + j].setBackgroundResource(R.drawable.b5);
                        break;
                    case 6:
                        buttons[10 * i + j].setBackgroundResource(R.drawable.b6);
                        break;
                    case 7:
                        buttons[10 * i + j].setBackgroundResource(R.drawable.b7);
                        break;
                    case 8:
                        buttons[10 * i + j].setBackgroundResource(R.drawable.b8);
                        break;
                    case 9:
                        buttons[10 * i + j].setBackgroundResource(R.drawable.b9);
                        break;
                    case 10:
                        buttons[10 * i + j].setBackgroundResource(R.drawable.b0);
                        break;
                    case 11:
                        buttons[10 * i + j].setBackgroundResource(R.drawable.b11);
                        break;
                    case 12:
                        buttons[10 * i + j].setBackgroundResource(R.drawable.b12);
                        break;
                    case 13:
                        buttons[10 * i + j].setBackgroundResource(R.drawable.b13);
                        break;
                }
            }
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
                Toast.makeText(getApplicationContext(), "You made a long press on button: " + fieldID, Toast.LENGTH_SHORT).show();
                game.toggleFlag(fieldID);
                buttons[fieldID].setBackgroundResource(R.drawable.b11);
            } else {
                Toast.makeText(getApplicationContext(), "A long press on " + fieldID + "was registered," +
                        " but not accepted", Toast.LENGTH_SHORT).show();
            }
        }

        return false;
    }
}
