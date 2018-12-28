package MinesweeperUI;

import android.view.View;

import MinesweeperLogic.Game;

public class GUI {

    private final Game game;
    // private final Board_Fake BF;

    public GUI(Game game, View view_game) {
        this.game = game;
        //BF = new Board_Fake(view_game);
    }

}
