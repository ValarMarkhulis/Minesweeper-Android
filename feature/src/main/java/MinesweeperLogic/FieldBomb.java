package MinesweeperLogic;

public class FieldBomb extends Field implements FieldInterface {

    FieldBomb(int fieldId) {
        this.fieldcharacter = '*';
        setId(fieldId);
    }


    @Override
    public int getFieldImgType(boolean endGame) {

        if (this.isShown() && picked && endGame) {
            // If its shown, has the "picked" var set and endGame is true,
            // that means that you have lost by choosing a bomb
            // and all the fields are being shown
            return 13;
        } else if (this.isFlagSet() && endGame) {
            // If the flag is set and its endGame
            return 11;
        } else if (this.isShown() && endGame) {
            // If its shown, return the img for bomb
            return 9;
        } else if (this.isFlagSet()) {
            return 11;
        } else {
            // If the field is not shown and the flag is not set
            // Return the std. img for a field
            return 10;
        }
    }
}
