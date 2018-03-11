package logic;

/**
 * Created by Tamir on 24/02/2018.
 * Representing a cell in the game board.
 */

public class Leaf {
    private LEAF_TYPE type;

    // Indicating if this leaf was chosen by the user
    private boolean isSelected;

    // Indicating if the leaf can be hopped to from the selected leaf
    private boolean isValidHop;

    public LEAF_TYPE getType() {
        return type;
    }

    void setType(LEAF_TYPE type) {
        this.type = type;
    }

    public boolean isSelected() {
        return isSelected;
    }

    void setSelected(boolean selected) {
        isSelected = selected;
    }

    public boolean isValidHop() {
        return isValidHop;
    }

    void setValidHop(boolean validHop) {
        isValidHop = validHop;
    }
}
