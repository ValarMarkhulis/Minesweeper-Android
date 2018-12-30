package MinesweeperLogic;

public class Field {
    char fieldcharacter = '?';
    boolean picked = false;
    private boolean flagSet = false;
    private boolean isShown = false;
    private int fieldId = 0;

    public char getValue() {
        //TODO: Remove the '!' infront of the "isShown" var
        if (!isShown) {
            return fieldcharacter;
        } else if ((flagSet)) {
            return '\u2691';
        } else {
            return '\u25A0';
        }
    }

    public void setValue(char character) {
        fieldcharacter = character;
    }

    public void setShown() {
        isShown = true;
    }


    public boolean isShown() {
        return isShown;
    }

    public boolean isFlagSet() {
        return flagSet;
    }

    public void setFlagSet(boolean flagSet) {
        this.flagSet = flagSet;
    }

    public boolean isPicked() {
        return picked;
    }

    public void setPicked(boolean picked) {
        this.picked = picked;
    }

    public int getId() {
        return fieldId;
    }

    public void setId(int id) {
        fieldId = id;
    }
}
