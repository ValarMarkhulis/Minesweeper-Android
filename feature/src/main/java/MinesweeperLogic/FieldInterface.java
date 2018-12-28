package MinesweeperLogic;

public interface FieldInterface {

    int getId();

    void setId(int id);

    char getValue();

    void setValue(char character);

    int getFieldImgType(boolean endGame);

    void setShown();

    boolean isShown();

    boolean isFlagSet();

    void setFlagSet(boolean flagSet);

    boolean isPicked();

    void setPicked(boolean picked);

}
