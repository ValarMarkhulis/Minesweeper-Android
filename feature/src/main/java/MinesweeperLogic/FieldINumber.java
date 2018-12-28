package MinesweeperLogic;

public class FieldINumber extends Field implements FieldInterface {

    private final int fieldNumber;

    FieldINumber(int fieldNumber, int fieldId) {
        this.fieldNumber = fieldNumber;

        this.fieldcharacter = (char) (fieldNumber + '0');
        setId(fieldId);
    }


    @Override
    public int getFieldImgType(boolean endGame) {

        if (this.isShown() && this.isFlagSet() && endGame) {
            // If its shown and a flag is set, that means that you have lost
            // and all the fields are being shown
            return 12;
        } else if (this.isShown()) {
            // If its shown, return the numeric number
            return fieldNumber;
        } else if (this.isFlagSet()) {
            return 11;
        } else {
            // If the field is not shown and the flag is not set
            // Return the std. img for a field
            return 10;
        }
    }
}
