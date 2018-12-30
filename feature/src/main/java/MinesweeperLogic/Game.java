package MinesweeperLogic;

import java.util.ArrayList;
import java.util.Random;


public class Game {

    public final ArrayList<FieldInterface> gameBoard;
    private final FieldINumber testNr;
    private final FieldEmpty testEm;
    private final FieldBomb testBo;
    private final int boombsOnStart = 24;
    private final boolean debug = false;
    public int flagsSet = 0;
    public int bombWhichKilledYou = -1;
    private int Boombsleft = 24;
    private int hiddenFieldsleft = 160;

    public Game() {


        //Get input
        //java.util.Scanner tastatur = new java.util.Scanner(System.in);

        gameBoard = new ArrayList<>();

        //TODO: Generate random fields
        //generate();
        //gameBoard.get(12).setFlagSet(true); Boombsleft--;
        //gameBoard.get(37).setFlagSet(true);
        //drawAsciiBoard();
        testNr = new FieldINumber(1, -1);
        testEm = new FieldEmpty(-1);
        testBo = new FieldBomb(-1);

        /*
        while(Boombsleft != 0){
            System.out.println("\n\n");
            System.out.println("Antal bomber: "+Boombsleft);

            System.out.println("Write 1 for flag and 0 for guess");
            int flagOrNot = tastatur.nextInt();
            System.out.println("Which column: 1-10");
            int x = tastatur.nextInt();
            tastatur.nextLine();
            System.out.println("Which row: 1-16");
            int y = tastatur.nextInt();
            tastatur.nextLine();

            int number = (x  + (y-1) * 10)-1;
            System.out.println(""+number);


        */
    }

    public int toggleFlag(int fieldID) {
        //Check if the user tries to toggle a flag on a shown empty or number field
        //If true, dont do anything else do rest of the func
        if ((gameBoard.get(fieldID).getClass().equals(testNr.getClass()) || gameBoard.get(fieldID).getClass().equals(testEm.getClass())) && gameBoard.get(fieldID).isShown()) {
            return 1;
        }

        boolean flagstatus = !gameBoard.get(fieldID).isFlagSet();
        gameBoard.get(fieldID).setFlagSet(flagstatus);

        //Increment/decrement the amount of flags set by the player
        if (flagstatus) {
            flagsSet++;
        } else {
            flagsSet--;
        }

        if (flagstatus && gameBoard.get(fieldID).getClass().equals(testBo.getClass())) {
            Boombsleft--;
        } else if (!flagstatus && gameBoard.get(fieldID).getClass().equals(testBo.getClass())) {
            Boombsleft++;
        }
        //if(debug) System.out.println(Boombsleft);
        //if(debug) System.out.println(hiddenFieldsleft);
        if (debug) System.out.println("FlagSet: " + flagsSet);
        if (Boombsleft == 0 && hiddenFieldsleft == boombsOnStart) {
            return 0;
        } else {
            return 1;
        }
    }

    public int guess(int fieldID) {
        try {
            if (gameBoard.get(fieldID).isFlagSet()) {
                System.out.println("You cant guess on a field with a flag on it!");
            } else if (gameBoard.get(fieldID).getClass().equals(testNr.getClass())) {
                try {
                    if (gameBoard.get(fieldID).isShown()) {
                        int status = visitShownField(fieldID);
                        if (status == -1) return -1;
                    } else {
                        if (debug) {
                            System.out.println("It was a Number was CALLED!");
                        }
                        visitField(fieldID, direction.NON);
                    }
                } catch (Exception ex) {

                }

            } else if (gameBoard.get(fieldID).getClass().equals(testEm.getClass())) {
                try {
                    if (gameBoard.get(fieldID).isShown()) {
                        System.out.println("You can't press a empty field!");
                    } else {
                        if (debug) {
                            System.out.println("It was a Empty");
                        }
                        visitField(fieldID, direction.NON);
                    }

                } catch (Exception ex) {

                }

            } else if (gameBoard.get(fieldID).getClass().equals(testBo.getClass())) {
                gameBoard.get(fieldID).setShown();
                hiddenFieldsleft--;
                if (debug) {
                    System.err.println("It was a Bomb");
                }
                bombWhichKilledYou = fieldID;
                return -1;
            }

        } catch (Exception ex) {

        }
        if (debug) {
            System.out.println(Boombsleft);
        }
        if (debug) {
            System.out.println(hiddenFieldsleft);
        }
        if (Boombsleft == 0 && hiddenFieldsleft == boombsOnStart) {
            return 0;
        } else {
            return 1;
        }

    }

    /**
     * This function will be called if the player presses a number which is shown. The function
     * will then visit all surrounding fields and shown them if they are number or empty, but
     * the player will lose if one of the fields is a bomb.
     *
     * @param number Is the field number you are trying to visit
     */
    private int visitShownField(int number) {
        int status;

        //Visit RIGHT
        if ((number + 1) % 10 != 0 && (number + 1) != 0) {
            status = testField(number + 1);
            if (status == -1) return -1;
        }

        //Visit LEFT
        if ((number - 1) % 10 != 9) {
            status = testField(number + -1);
            if (status == -1) return -1;
        }

        //Visit UP
        boolean debug2 = false;
        if (debug2) System.out.println("Visit up blev kaldt");
        status = testField(number - 10);
        if (status == -1) return -1;

        //Visit DOWN
        if (debug2) System.out.println("Visit downn blev kaldt");
        status = testField(number + 10);
        if (status == -1) return -1;

        //Visit UP RIGHT
        if (debug2) System.out.println("Visit UP RIGHT blev kaldt");
        if ((number - 9) % 10 != 0) {
            status = testField(number - 9);
            if (status == -1) return -1;
        }

        //Visit UP LEFT
        if (debug2) System.out.println("Visit UP LEFT blev kaldt");
        if ((number - 11) % 10 != 9) {
            status = testField(number - 11);
            if (status == -1) return -1;
        }

        //Visit DOWN RIGHT
        if (debug2) System.out.println("Visit DOWN RIGHT blev kaldt");
        if ((number + 11) % 10 != 0) {
            status = testField(number + 11);
            if (status == -1) return -1;
        }

        //Visit DOWN LEFT
        if (debug2) System.out.println("Visit DOWN LEFT blev kaldt");
        if ((number + 9) % 10 != 9) {
            status = testField(number + 9);
            if (status == -1) return -1;
        }

        return 0;

    }

    private int testField(int number) {
        try {
            //If the flag has been set skip it
            if (gameBoard.get(number).isFlagSet()) {
                return 0;
            } else if (gameBoard.get(number).getClass().equals(testBo.getClass())) {
                if (!gameBoard.get(number).isShown()) hiddenFieldsleft--;
                gameBoard.get(number).setShown();
                if (debug) {
                    System.err.println("It was a Bomb");
                }
                bombWhichKilledYou = number;
                return -1;
                //System.exit(-1);
            } else if (gameBoard.get(number).getClass().equals(testEm.getClass())) {
                visitField(number, direction.NON);
            } else if (gameBoard.get(number).getClass().equals(testNr.getClass())) {
                if (debug) {
                    System.out.println("A number field has been reveald!");
                }
                if (!gameBoard.get(number).isShown()) hiddenFieldsleft--;
                gameBoard.get(number).setShown();
            }
        } catch (Exception ex) {

        }
        return 0;
    }

    private void visitField(int number, direction direction) {
        //Check if its already been shown or a flag has been set
        if (gameBoard.get(number).isFlagSet() ||
                gameBoard.get(number).isShown()) {
            return;
        }


        //RIGHT
        //Testing if the number just went from one row to row++
        if (direction == Game.direction.RIGHT) {
            if (gameBoard.get(number).getClass().equals(testBo.getClass()) ||
                    number % 10 == 0 && number != 0) {
                return;
            }
        } else if (direction == Game.direction.LEFT) {
            //LEFT
            //Testing if the number just went from one row to row--
            if (gameBoard.get(number).getClass().equals(testBo.getClass()) ||
                    number % 10 == 9) {
                return;
            }
        } else if (direction == Game.direction.UP) {
            //UP
            //Testing if the number just went from one row to row--
            if (gameBoard.get(number).getClass().equals(testBo.getClass())) {
                return;
            }
        } else if (direction == Game.direction.DOWN) {
            //DOWN
            //Testing if the number just went from one row to row++
            if (gameBoard.get(number).getClass().equals(testBo.getClass())) {
                return;
            }
        } else if (direction == Game.direction.RIGHTUP) {
            //RIGHT UP
            if (gameBoard.get(number).getClass().equals(testBo.getClass()) ||
                    number % 10 == 0) {
                return;
            }
        } else if (direction == Game.direction.LEFTUP) {
            //LEFT UP
            if (gameBoard.get(number).getClass().equals(testBo.getClass()) ||
                    number % 10 == 9) {
                return;
            }
        } else if (direction == Game.direction.RIGHTDOWN) {
            //LEFT UP
            if (gameBoard.get(number).getClass().equals(testBo.getClass()) ||
                    number % 10 == 0) {
                return;
            }
        } else if (direction == Game.direction.LEFTDOWN) {
            //LEFT UP
            if (gameBoard.get(number).getClass().equals(testBo.getClass()) ||
                    number % 10 == 9) {
                return;
            }
        }

        //They all need to check for these
        if (gameBoard.get(number).getClass().equals(testNr.getClass())) {
            if (!gameBoard.get(number).isShown()) hiddenFieldsleft--;
            //If the field is a number, show it and return
            gameBoard.get(number).setShown();
            return;
        } else if (gameBoard.get(number).getClass().equals(testEm.getClass())) {
            if (!gameBoard.get(number).isShown()) hiddenFieldsleft--;
            gameBoard.get(number).setShown();
        }

        //Go right
        if (direction == Game.direction.LEFT) {
        } else {
            try {
                visitField(number + 1, Game.direction.RIGHT);
            } catch (Exception ex) {

            }
        }


        //Go left
        if (direction == Game.direction.RIGHT) {
        } else {
            try {
                visitField(number - 1, Game.direction.LEFT);
            } catch (Exception ex) {

            }
        }

        //Go up
        if (direction == Game.direction.DOWN) {
        } else {
            try {
                visitField(number - 10, Game.direction.UP);
            } catch (Exception ex) {

            }
        }

        if (direction == Game.direction.UP) {
        } else {
            //Go down
            try {
                visitField(number + 10, Game.direction.DOWN);
            } catch (Exception ex) {

            }
        }

        //Go up and right
        if (direction == Game.direction.LEFTDOWN) {
        } else {
            try {
                visitField((number - 10) + 1, Game.direction.RIGHTUP);
            } catch (Exception ex) {

            }
        }

        //Go up and left
        if (direction == Game.direction.RIGHTDOWN) {
        } else {

            try {
                visitField((number - 10) - 1, Game.direction.LEFTUP);
            } catch (Exception ex) {

            }
        }

        //Go down and right
        if (direction == Game.direction.LEFTUP) {
        } else {

            try {
                visitField((number + 10) + 1, Game.direction.RIGHTDOWN);
            } catch (Exception ex) {

            }
        }

        //Go down and left
        if (direction == Game.direction.RIGHTUP) {
        } else {

            try {
                visitField((number + 10) - 1, Game.direction.LEFTDOWN);
            } catch (Exception ex) {

            }
        }
    }

    public void drawAsciiBoard() {
        FieldINumber testNr = new FieldINumber(1, -1);
        FieldEmpty testEm = new FieldEmpty(-1);
        FieldBomb testBo = new FieldBomb(-1);
        int testId = 0;
        for (FieldInterface gameBoard : gameBoard) {
            if (testId % 10 == 0 && testId != 0) {
                System.out.println(" |" + (testId - 1));
            }
            if (gameBoard.getClass().equals(testNr.getClass())) {
                System.out.print(" " + gameBoard.getValue());
                //System.out.println("Det matchede number");
            } else if (gameBoard.getClass().equals(testEm.getClass())) {
                System.out.print(" " + gameBoard.getValue());
                //.out.println("Det matchede Empty");
            } else if (gameBoard.getClass().equals(testBo.getClass())) {
                System.out.print(" " + gameBoard.getValue());
                //System.out.println("Det matchede Bomb");
            }
            testId++;
        }
        System.out.println(" |" + (testId - 1));
    }

    private void generate() {
        int id = 0;
        //Række 0
        gameBoard.add(new FieldEmpty(id));
        id++;
        for (int i = 0; i < 3; i++) {
            gameBoard.add(new FieldINumber(1, id));
            id++;
        }
        for (int i = 0; i < 2; i++) {
            gameBoard.add(new FieldEmpty(id));
            id++;
        }
        gameBoard.add(new FieldINumber(1, id));
        id++;
        gameBoard.add(new FieldBomb(id));
        id++;
        gameBoard.add(new FieldINumber(1, id));
        id++;
        gameBoard.add(new FieldEmpty(id));
        id++;
        // Række 1
        gameBoard.add(new FieldEmpty(id));
        id++;
        gameBoard.add(new FieldINumber(1, id));
        id++;
        gameBoard.add(new FieldBomb(id));
        id++;
        gameBoard.add(new FieldINumber(1, id));
        id++;
        for (int i = 0; i < 2; i++) {
            gameBoard.add(new FieldEmpty(id));
            id++;
        }
        for (int i = 0; i < 3; i++) {
            gameBoard.add(new FieldINumber(1, id));
            id++;
        }
        gameBoard.add(new FieldEmpty(id));
        id++;
        // Række 2
        gameBoard.add(new FieldEmpty(id));
        id++;
        gameBoard.add(new FieldINumber(1, id));
        id++;
        for (int i = 0; i < 2; i++) {
            gameBoard.add(new FieldINumber(2, id));
            id++;
        }
        gameBoard.add(new FieldINumber(1, id));
        id++;
        gameBoard.add(new FieldEmpty(id));
        id++;
        for (int i = 0; i < 3; i++) {
            gameBoard.add(new FieldINumber(1, id));
            id++;
        }
        gameBoard.add(new FieldEmpty(id));
        id++;
        // Række 3
        for (int i = 0; i < 2; i++) {
            gameBoard.add(new FieldEmpty(id));
            id++;
        }
        gameBoard.add(new FieldINumber(1, id));
        id++;
        gameBoard.add(new FieldBomb(id));
        id++;
        gameBoard.add(new FieldINumber(1, id));
        id++;
        gameBoard.add(new FieldEmpty(id));
        id++;
        gameBoard.add(new FieldINumber(1, id));
        id++;
        gameBoard.add(new FieldBomb(id));
        id++;
        gameBoard.add(new FieldINumber(1, id));
        id++;
        gameBoard.add(new FieldEmpty(id));
        id++;
        // Række 4
        gameBoard.add(new FieldINumber(1, id));
        id++;
        gameBoard.add(new FieldINumber(1, id));
        id++;
        gameBoard.add(new FieldINumber(3, id));
        id++;
        gameBoard.add(new FieldINumber(2, id));
        id++;
        gameBoard.add(new FieldINumber(2, id));
        id++;
        gameBoard.add(new FieldEmpty(id));
        id++;
        for (int i = 0; i < 3; i++) {
            gameBoard.add(new FieldINumber(1, id));
            id++;
        }
        gameBoard.add(new FieldEmpty(id));
        id++;
        // Række 5
        gameBoard.add(new FieldINumber(1, id));
        id++;
        gameBoard.add(new FieldBomb(id));
        id++;
        gameBoard.add(new FieldINumber(2, id));
        id++;
        gameBoard.add(new FieldBomb(id));
        id++;
        gameBoard.add(new FieldINumber(2, id));
        id++;
        for (int i = 0; i < 5; i++) {
            gameBoard.add(new FieldINumber(1, id));
            id++;
        }
        // Række 6
        gameBoard.add(new FieldINumber(2, id));
        id++;
        gameBoard.add(new FieldINumber(2, id));
        id++;
        gameBoard.add(new FieldINumber(3, id));
        id++;
        gameBoard.add(new FieldINumber(2, id));
        id++;
        gameBoard.add(new FieldINumber(3, id));
        id++;
        gameBoard.add(new FieldBomb(id));
        id++;
        gameBoard.add(new FieldINumber(1, id));
        id++;
        gameBoard.add(new FieldINumber(1, id));
        id++;
        gameBoard.add(new FieldBomb(id));
        id++;
        gameBoard.add(new FieldINumber(2, id));
        id++;
        // Række 7
        gameBoard.add(new FieldINumber(1, id));
        id++;
        gameBoard.add(new FieldBomb(id));
        id++;
        gameBoard.add(new FieldINumber(1, id));
        id++;
        gameBoard.add(new FieldINumber(2, id));
        id++;
        gameBoard.add(new FieldBomb(id));
        id++;
        gameBoard.add(new FieldINumber(4, id));
        id++;
        gameBoard.add(new FieldINumber(2, id));
        id++;
        gameBoard.add(new FieldINumber(2, id));
        id++;
        gameBoard.add(new FieldINumber(3, id));
        id++;
        gameBoard.add(new FieldBomb(id));
        id++;
        // Række 8
        gameBoard.add(new FieldINumber(1, id));
        id++;
        gameBoard.add(new FieldINumber(1, id));
        id++;
        gameBoard.add(new FieldINumber(1, id));
        id++;
        gameBoard.add(new FieldINumber(2, id));
        id++;
        gameBoard.add(new FieldBomb(id));
        id++;
        gameBoard.add(new FieldINumber(3, id));
        id++;
        gameBoard.add(new FieldBomb(id));
        id++;
        gameBoard.add(new FieldINumber(1, id));
        id++;
        gameBoard.add(new FieldINumber(2, id));
        id++;
        gameBoard.add(new FieldBomb(id));
        id++;
        // Række 9
        gameBoard.add(new FieldEmpty(id));
        id++;
        gameBoard.add(new FieldEmpty(id));
        id++;
        gameBoard.add(new FieldEmpty(id));
        id++;
        gameBoard.add(new FieldINumber(1, id));
        id++;
        gameBoard.add(new FieldINumber(1, id));
        id++;
        gameBoard.add(new FieldINumber(3, id));
        id++;
        gameBoard.add(new FieldINumber(2, id));
        id++;
        gameBoard.add(new FieldINumber(2, id));
        id++;
        gameBoard.add(new FieldINumber(1, id));
        id++;
        gameBoard.add(new FieldINumber(1, id));
        id++;
        // Række 10
        gameBoard.add(new FieldEmpty(id));
        id++;
        gameBoard.add(new FieldEmpty(id));
        id++;
        gameBoard.add(new FieldEmpty(id));
        id++;
        gameBoard.add(new FieldEmpty(id));
        id++;
        gameBoard.add(new FieldEmpty(id));
        id++;
        gameBoard.add(new FieldINumber(2, id));
        id++;
        gameBoard.add(new FieldBomb(id));
        id++;
        gameBoard.add(new FieldINumber(2, id));
        id++;
        gameBoard.add(new FieldEmpty(id));
        id++;
        gameBoard.add(new FieldEmpty(id));
        id++;
        // Række 11
        gameBoard.add(new FieldINumber(2, id));
        id++;
        gameBoard.add(new FieldINumber(3, id));
        id++;
        gameBoard.add(new FieldINumber(2, id));
        id++;
        gameBoard.add(new FieldINumber(1, id));
        id++;
        gameBoard.add(new FieldEmpty(id));
        id++;
        gameBoard.add(new FieldINumber(2, id));
        id++;
        gameBoard.add(new FieldBomb(id));
        id++;
        gameBoard.add(new FieldINumber(2, id));
        id++;
        gameBoard.add(new FieldEmpty(id));
        id++;
        gameBoard.add(new FieldEmpty(id));
        id++;
        // Række 12
        gameBoard.add(new FieldBomb(id));
        id++;
        gameBoard.add(new FieldBomb(id));
        id++;
        gameBoard.add(new FieldBomb(id));
        id++;
        gameBoard.add(new FieldINumber(1, id));
        id++;
        gameBoard.add(new FieldEmpty(id));
        id++;
        gameBoard.add(new FieldINumber(2, id));
        id++;
        gameBoard.add(new FieldINumber(2, id));
        id++;
        gameBoard.add(new FieldINumber(2, id));
        id++;
        gameBoard.add(new FieldINumber(1, id));
        id++;
        gameBoard.add(new FieldINumber(1, id));
        id++;
        // Række 13
        gameBoard.add(new FieldBomb(id));
        id++;
        gameBoard.add(new FieldINumber(4, id));
        id++;
        gameBoard.add(new FieldINumber(2, id));
        id++;
        gameBoard.add(new FieldINumber(2, id));
        id++;
        gameBoard.add(new FieldINumber(1, id));
        id++;
        gameBoard.add(new FieldINumber(3, id));
        id++;
        gameBoard.add(new FieldBomb(id));
        id++;
        gameBoard.add(new FieldINumber(2, id));
        id++;
        gameBoard.add(new FieldINumber(1, id));
        id++;
        gameBoard.add(new FieldBomb(id));
        id++;
        // Række 14
        gameBoard.add(new FieldINumber(1, id));
        id++;
        gameBoard.add(new FieldINumber(1, id));
        id++;
        gameBoard.add(new FieldEmpty(id));
        id++;
        gameBoard.add(new FieldINumber(1, id));
        id++;
        gameBoard.add(new FieldBomb(id));
        id++;
        gameBoard.add(new FieldINumber(3, id));
        id++;
        gameBoard.add(new FieldBomb(id));
        id++;
        gameBoard.add(new FieldINumber(2, id));
        id++;
        gameBoard.add(new FieldINumber(1, id));
        id++;
        gameBoard.add(new FieldINumber(1, id));
        id++;
        // Række 15
        gameBoard.add(new FieldEmpty(id));
        id++;
        gameBoard.add(new FieldEmpty(id));
        id++;
        gameBoard.add(new FieldEmpty(id));
        id++;
        gameBoard.add(new FieldINumber(1, id));
        id++;
        gameBoard.add(new FieldINumber(1, id));
        id++;
        gameBoard.add(new FieldINumber(2, id));
        id++;
        gameBoard.add(new FieldINumber(1, id));
        id++;
        gameBoard.add(new FieldINumber(1, id));
        id++;
        gameBoard.add(new FieldEmpty(id));
        id++;
        gameBoard.add(new FieldEmpty(id));
    }

    private void generate2() {
        int id = 0;
        //Række 0
        for (int i = 0; i < 4; i++) {
            gameBoard.add(new FieldEmpty(id));
            id++;
        }
        for (int i = 0; i < 3; i++) {
            gameBoard.add(new FieldINumber(1, id));
            id++;
        }
        for (int i = 0; i < 3; i++) {
            gameBoard.add(new FieldEmpty(id));
            id++;
        }

        // Række 1
        for (int i = 0; i < 4; i++) {
            gameBoard.add(new FieldEmpty(id));
            id++;
        }
        gameBoard.add(new FieldINumber(1, id));
        id++;
        gameBoard.add(new FieldBomb(id));
        id++;
        gameBoard.add(new FieldINumber(1, id));
        id++;
        for (int i = 0; i < 3; i++) {
            gameBoard.add(new FieldEmpty(id));
            id++;
        }

        // Række 2
        for (int i = 0; i < 4; i++) {
            gameBoard.add(new FieldEmpty(id));
            id++;
        }
        gameBoard.add(new FieldINumber(1, id));
        id++;
        gameBoard.add(new FieldINumber(2, id));
        id++;
        gameBoard.add(new FieldINumber(2, id));
        id++;
        for (int i = 0; i < 3; i++) {
            gameBoard.add(new FieldINumber(1, id));
            id++;
        }

        // Række 3
        for (int i = 0; i < 5; i++) {
            gameBoard.add(new FieldEmpty(id));
            id++;
        }
        gameBoard.add(new FieldINumber(1, id));
        id++;
        gameBoard.add(new FieldBomb(id));
        id++;
        gameBoard.add(new FieldINumber(1, id));
        id++;
        gameBoard.add(new FieldINumber(1, id));
        id++;
        gameBoard.add(new FieldBomb(id));
        id++;

        // Række 4
        for (int i = 0; i < 3; i++) {
            gameBoard.add(new FieldEmpty(id));
            id++;
        }
        gameBoard.add(new FieldINumber(1, id));
        id++;
        gameBoard.add(new FieldINumber(1, id));
        id++;
        gameBoard.add(new FieldINumber(2, id));
        id++;
        for (int i = 0; i < 4; i++) {
            gameBoard.add(new FieldINumber(1, id));
            id++;
        }

        // Række 5
        gameBoard.add(new FieldINumber(1, id));
        id++;
        gameBoard.add(new FieldINumber(1, id));
        id++;
        gameBoard.add(new FieldINumber(1, id));
        id++;
        gameBoard.add(new FieldINumber(2, id));
        id++;
        gameBoard.add(new FieldBomb(id));
        id++;
        gameBoard.add(new FieldINumber(2, id));
        id++;
        for (int i = 0; i < 4; i++) {
            gameBoard.add(new FieldEmpty(id));
            id++;
        }

        // Række 6
        gameBoard.add(new FieldINumber(2, id));
        id++;
        gameBoard.add(new FieldBomb(id));
        id++;
        gameBoard.add(new FieldINumber(2, id));
        id++;
        gameBoard.add(new FieldINumber(3, id));
        id++;
        gameBoard.add(new FieldBomb(id));
        id++;
        gameBoard.add(new FieldINumber(2, id));
        id++;
        gameBoard.add(new FieldEmpty(id));
        id++;
        gameBoard.add(new FieldEmpty(id));
        id++;
        gameBoard.add(new FieldINumber(1, id));
        id++;
        gameBoard.add(new FieldINumber(1, id));
        id++;

        // Række 7
        gameBoard.add(new FieldBomb(id));
        id++;
        gameBoard.add(new FieldINumber(4, id));
        id++;
        gameBoard.add(new FieldBomb(id));
        id++;
        gameBoard.add(new FieldINumber(3, id));
        id++;
        gameBoard.add(new FieldINumber(2, id));
        id++;
        gameBoard.add(new FieldINumber(2, id));
        id++;
        gameBoard.add(new FieldEmpty(id));
        id++;
        gameBoard.add(new FieldEmpty(id));
        id++;
        gameBoard.add(new FieldINumber(2, id));
        id++;
        gameBoard.add(new FieldBomb(id));
        id++;

        // Række 8
        gameBoard.add(new FieldBomb(id));
        id++;
        gameBoard.add(new FieldINumber(4, id));
        id++;
        gameBoard.add(new FieldINumber(2, id));
        id++;
        gameBoard.add(new FieldINumber(3, id));
        id++;
        gameBoard.add(new FieldBomb(id));
        id++;
        gameBoard.add(new FieldINumber(2, id));
        id++;
        gameBoard.add(new FieldINumber(1, id));
        id++;
        gameBoard.add(new FieldINumber(1, id));
        id++;
        gameBoard.add(new FieldINumber(2, id));
        id++;
        gameBoard.add(new FieldBomb(id));
        id++;

        // Række 9
        gameBoard.add(new FieldINumber(1, id));
        id++;
        gameBoard.add(new FieldINumber(2, id));
        id++;
        gameBoard.add(new FieldBomb(id));
        id++;
        gameBoard.add(new FieldINumber(3, id));
        id++;
        gameBoard.add(new FieldINumber(2, id));
        id++;
        gameBoard.add(new FieldINumber(3, id));
        id++;
        gameBoard.add(new FieldBomb(id));
        id++;
        for (int i = 0; i < 3; i++) {
            gameBoard.add(new FieldINumber(1, id));
            id++;
        }

        // Række 10
        gameBoard.add(new FieldEmpty(id));
        id++;
        gameBoard.add(new FieldINumber(1, id));
        id++;
        gameBoard.add(new FieldINumber(1, id));
        id++;
        gameBoard.add(new FieldINumber(2, id));
        id++;
        gameBoard.add(new FieldBomb(id));
        id++;
        gameBoard.add(new FieldINumber(3, id));
        id++;
        gameBoard.add(new FieldINumber(2, id));
        id++;
        for (int i = 0; i < 3; i++) {
            gameBoard.add(new FieldINumber(1, id));
            id++;
        }

        // Række 11
        for (int i = 0; i < 3; i++) {
            gameBoard.add(new FieldEmpty(id));
            id++;
        }
        gameBoard.add(new FieldINumber(1, id));
        id++;
        gameBoard.add(new FieldINumber(3, id));
        id++;
        gameBoard.add(new FieldBomb(id));
        id++;
        gameBoard.add(new FieldINumber(3, id));
        id++;
        gameBoard.add(new FieldINumber(1, id));
        id++;
        gameBoard.add(new FieldINumber(2, id));
        id++;
        gameBoard.add(new FieldBomb(id));
        id++;

        // Række 12
        gameBoard.add(new FieldINumber(1, id));
        id++;
        gameBoard.add(new FieldINumber(1, id));
        id++;
        gameBoard.add(new FieldEmpty(id));
        id++;
        gameBoard.add(new FieldEmpty(id));
        id++;
        gameBoard.add(new FieldINumber(2, id));
        id++;
        gameBoard.add(new FieldBomb(id));
        id++;
        gameBoard.add(new FieldINumber(3, id));
        id++;
        gameBoard.add(new FieldBomb(id));
        id++;
        gameBoard.add(new FieldINumber(2, id));
        id++;
        gameBoard.add(new FieldINumber(1, id));
        id++;

        // Række 13
        gameBoard.add(new FieldBomb(id));
        id++;
        gameBoard.add(new FieldINumber(2, id));
        id++;
        for (int i = 0; i < 4; i++) {
            gameBoard.add(new FieldINumber(1, id));
            id++;
        }
        for (int i = 0; i < 3; i++) {
            gameBoard.add(new FieldINumber(2, id));
            id++;
        }
        gameBoard.add(new FieldINumber(1, id));
        id++;

        // Række 14
        gameBoard.add(new FieldINumber(2, id));
        id++;
        gameBoard.add(new FieldINumber(3, id));
        id++;
        gameBoard.add(new FieldBomb(id));
        id++;
        gameBoard.add(new FieldINumber(1, id));
        id++;
        gameBoard.add(new FieldEmpty(id));
        id++;
        gameBoard.add(new FieldEmpty(id));
        id++;
        gameBoard.add(new FieldINumber(1, id));
        id++;
        gameBoard.add(new FieldINumber(2, id));
        id++;
        gameBoard.add(new FieldBomb(id));
        id++;
        gameBoard.add(new FieldINumber(1, id));
        id++;

        // Række 15
        gameBoard.add(new FieldINumber(1, id));
        id++;
        gameBoard.add(new FieldBomb(id));
        id++;
        gameBoard.add(new FieldINumber(2, id));
        id++;
        gameBoard.add(new FieldINumber(1, id));
        id++;
        gameBoard.add(new FieldEmpty(id));
        id++;
        gameBoard.add(new FieldEmpty(id));
        id++;
        gameBoard.add(new FieldINumber(1, id));
        id++;
        gameBoard.add(new FieldBomb(id));
        id++;
        gameBoard.add(new FieldINumber(2, id));
        id++;
        gameBoard.add(new FieldINumber(1, id));
        //id++;

    }

    public void generateRandomMap(int chosenField) {
        System.out.println("Generating the map from the seed: " + chosenField);

        //Fill the arrays with zeros
        int[][] Array = new int[16][10];
        for (int i = 0; i < 16; i++) {
            for (int j = 0; j < 10; j++) {
                Array[i][j] = 0;
            }
        }
        //Puts a special character into the chosenField so it cant be a number or a bomb
        //Array[chosenField / 10][chosenField % 10] = -2;

        Random rand = new Random();

        ArrayList<Integer> used_Numbers_list = new ArrayList<>();
        //Put in the chosen number, so it doesnt get overridden
        used_Numbers_list.add(chosenField);

        for (int i = 1; i < 25; i++) {
            int n = rand.nextInt(159);
            //System.out.println("n er: "+n);
            if (used_Numbers_list.contains(n)) {
                i--;
            } else {
                used_Numbers_list.add(n);
            }
        }


        //System.out.println("LIsten: "+used_Numbers_list.toString());
        //Remove the chosenfield from the list
        used_Numbers_list.remove(0);
        //System.out.println("LIsten: "+used_Numbers_list.toString());

        //Put bombs on the positions from the used_numbers_list
        for (int number : used_Numbers_list) {
            Array[number / 10][number % 10] = -1;
        }

        if (debug) {
            for (int i = 0; i < 16; i++) {
                for (int j = 0; j < 10; j++) {
                    if (j == 0 && i != 0) {
                        System.out.println(" |");
                    }
                    System.out.print("\t" + Array[i][j]);
                }
            }
            System.out.println("\t|\n\n");
        }


        //TODO: Run through the array and look at all the adjedent fields and incremment the number in the cell if there is a bomb.
        for (int i = 0; i < 16; i++) {
            for (int j = 0; j < 10; j++) {
                int currentField = Array[i][j];
                if (currentField == -1) {
                    //These are bomb fields

                } else {
                    //These are number fields and the pressed field
                    //All the nr fields needs to be visisted and if there is a bomb, increment
                    // the number which is located in the fields position in the array
                    try {
                        //LEFT UP
                        if (Array[i - 1][j - 1] == -1) {
                            Array[i][j] = ++currentField;
                        }
                    } catch (Exception ex) {

                    }
                    try {
                        //UP
                        if (Array[i][j - 1] == -1) {
                            Array[i][j] = ++currentField;
                        }
                    } catch (Exception ex) {

                    }
                    try {
                        //RIGHT UP
                        if (Array[i + 1][j - 1] == -1) {
                            Array[i][j] = ++currentField;
                        }
                    } catch (Exception ex) {

                    }
                    try {
                        //LEFT
                        if (Array[i - 1][j] == -1) {
                            Array[i][j] = ++currentField;
                        }
                    } catch (Exception ex) {

                    }
                    try {
                        //RIGHT
                        if (Array[i + 1][j] == -1) {
                            Array[i][j] = ++currentField;
                        }
                    } catch (Exception ex) {

                    }
                    try {
                        //LEFT DOWN
                        if (Array[i - 1][j + 1] == -1) {
                            Array[i][j] = ++currentField;
                        }
                    } catch (Exception ex) {

                    }
                    try {
                        //DOWN
                        if (Array[i][j + 1] == -1) {
                            Array[i][j] = ++currentField;
                        }
                    } catch (Exception ex) {

                    }
                    try {
                        //DOWN RIGHT
                        if (Array[i + 1][j + 1] == -1) {
                            Array[i][j] = ++currentField;
                        }
                    } catch (Exception ex) {

                    }

                }
            }
        }
        if (debug) {
            System.out.print("After the numbers have been set\n");
            for (int i = 0; i < 16; i++) {
                for (int j = 0; j < 10; j++) {
                    if (j == 0 && i != 0) {
                        System.out.println(" |");
                    }
                    System.out.print("\t" + Array[i][j]);
                }
            }
            System.out.println("\t|\n\n");
        }


        //TODO: Put the fields into the gameBoard arraylist
        int id = 0;
        for (int i = 0; i < 16; i++) {
            for (int j = 0; j < 10; j++) {
                int currentField = Array[i][j];
                if (currentField == -1) {
                    //This is the bomb field
                    gameBoard.add(new FieldBomb(++id));
                } else if (currentField == 0) {
                    //This is the empty fields
                    gameBoard.add(new FieldEmpty(++id));
                } else {
                    //This is all the number fields
                    gameBoard.add(new FieldINumber(currentField, id));
                }
            }
        }
    }

    public void showAllFields(int fieldID) {
        gameBoard.get(fieldID).setPicked(true);
        for (FieldInterface gB : gameBoard) {
            gB.setShown();
        }
    }


    public enum direction {RIGHT, LEFT, DOWN, UP, NON, RIGHTUP, RIGHTDOWN, LEFTUP, LEFTDOWN}

    public int getBoombsleft() {
        return Boombsleft;
    }
}

