package MinesweeperLogic;

public class FieldEmpty extends Field implements FieldInterface {

    FieldEmpty(int fieldId) {
        this.fieldcharacter = '_';
        setId(fieldId);
    }


    @Override
    public int getFieldImgType(boolean endGame) {
        if (this.isShown() && this.isFlagSet() && endGame) {
            // If its shown and a flag is set, that means that you have lost
            //and all the fields are being shown
            return 12;
        } else if (this.isShown()) {
            // If its shown, return the img for empty
            return 0;
        } else if (this.isFlagSet()) {
            return 11;
        } else {
            // If the field is not shown and the flag is not set
            // Return the std. img for a field
            return 10;
        }
    }
}
