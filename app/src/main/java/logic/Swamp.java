package logic;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tamir on 24/02/2018.
 * A class representing the game board.
 * The game board consists of leafs and frogs.
 */

public class Swamp {
    private Leaf[][] leaves;
    private LeafCoordinate selectedFrogCord;

    public Leaf[][] getLeaves() {
        return leaves;
    }

    public void setLeaves(Leaf[][] leaves) {
        this.leaves = leaves;
    }

    public LeafCoordinate getSelectedFrogCord() {
        return selectedFrogCord;
    }

    public void setSelectedFrogCord(LeafCoordinate selectedFrogCord) {
        this.selectedFrogCord = selectedFrogCord;
    }

    public Swamp(){
        leaves = new Leaf[5][];

        // Initializing the leaves table:
        // (even rows have 3 leaves, uneven rows have 2 leaves)
        leaves[0] = new Leaf[3];
        leaves[1] = new Leaf[2];
        leaves[2] = new Leaf[3];
        leaves[3] = new Leaf[2];
        leaves[3] = new Leaf[3];
    }

    public void setLevel(Level level){

        // Emptying all leaves
        for(int i = 0 ; i < leaves.length; i ++){
            for(int j = 0; j < leaves[i].length; j ++){
                leaves[i][j].setType(LEAF_TYPE.EMPTY);
                leaves[i][j].setValidHop(false);
                leaves[i][j].setSelected(false);
            }
        }

        // Setting the green frogs
        int[] greenFrogsLocations = level.getGreenFrogsLocations();
        for(int i = 0; i < level.getGreenFrogsLocations().length; i ++){
            getLeaf(new LeafCoordinate(greenFrogsLocations[i])).setType(LEAF_TYPE.GREEN_FROG);
        }

        // Setting red frog
        getLeaf(new LeafCoordinate(level.getRedFrogLocation())).setType(LEAF_TYPE.RED_FROG);
    }

    public Leaf getLeaf(int row, int column){
        Leaf leaf = null;

        // Checking if the indexes represent valid leaves
        if(leaves.length < row &&
                leaves[row].length < column){
            // Getting the leaf in the desired space
            leaf = leaves[row][column];
        }

        return leaf;
    }

    public Leaf getLeaf(LeafCoordinate coordinate){
        Leaf leaf = null;

        // Checking if the indexes represent valid leaves
        if(leaves.length < coordinate.getRow() &&
                leaves[coordinate.getRow()].length < coordinate.getColumn()){
            // Getting the leaf in the desired space
            leaf = leaves[coordinate.getRow()][coordinate.getColumn()];
        }

        return leaf;
    }


    public void makeHop(Hop hop){
        getLeaf(hop.getEatenFrogLeaf()).setType(LEAF_TYPE.EMPTY);
        // Getting the hopped frog and clearing it's original leaf
        LEAF_TYPE frog = getLeaf(hop.getFrogOriginalLeaf()).getType();
        getLeaf(hop.getFrogOriginalLeaf()).setType(LEAF_TYPE.EMPTY);

        // Setting the destination leaf with the hopped frog
        getLeaf(hop.getFrogHoppedLeaf()).setType(frog);
    }

    /**
     * Reverting the given hop.
     * Called when stepping backwards from a solution.
     * @param hop The hop to revert
     */
    public void revertHop(Hop hop){
        // Getting the green frog back
        getLeaf(hop.getEatenFrogLeaf()).setType(LEAF_TYPE.GREEN_FROG);
        // Getting the hopped frog and clearing it's current leaf
        LEAF_TYPE frog = getLeaf(hop.getFrogHoppedLeaf()).getType();
        getLeaf(hop.getFrogHoppedLeaf()).setType(LEAF_TYPE.EMPTY);

        // Setting the original leaf with the hopped frog
        getLeaf(hop.getFrogOriginalLeaf()).setType(frog);
    }

    public void selectLeaf(LeafCoordinate coordinate){
        Leaf leaf = getLeaf(coordinate);
        if(leaf != null){
            if(leaf.getType() == LEAF_TYPE.EMPTY){
                // TODO: Wrong choice
            }else{
                // Clearing last selected:

                for(int i = 0 ; i < leaves.length; i ++){
                    for(int j = 0; j < leaves[i].length; j ++){
                        leaves[i][j].setSelected(false);
                        leaves[i][j].setValidHop(false);
                    }
                }

                setValidHops(coordinate);

                leaf.setSelected(true);
                selectedFrogCord = coordinate;
            }
        }
        else{
            // TODO: Wrong choice
        }
    }

    private void setValidHops(LeafCoordinate selectedFrogLeaf){

        int selectedFrogRow = selectedFrogLeaf.getRow();
        int selectedFrogColumn = selectedFrogLeaf.getColumn();

        // Tf the frog is a row with 3 columns
        if(selectedFrogLeaf.getColumn() % 2 == 0){
            // Handling the "straight" hop
            // (not available for frogs in a row with two leaves):

            if(isValidHop(new LeafCoordinate(selectedFrogRow + 4, selectedFrogColumn),
                    new LeafCoordinate(selectedFrogRow - 2, selectedFrogColumn))){

                getLeaf(new LeafCoordinate(selectedFrogRow + 4, selectedFrogColumn)).setValidHop(true);
            }

            if(isValidHop(new LeafCoordinate(selectedFrogRow - 4, selectedFrogColumn),
                    new LeafCoordinate(selectedFrogRow + 2, selectedFrogColumn))){
                getLeaf(new LeafCoordinate(selectedFrogRow - 4, selectedFrogColumn)).setValidHop(true);
            }
        }

        // Handling the "diagonal" hop:

        // Diagonal down left
        if(isValidHop(new LeafCoordinate(selectedFrogRow + 2, selectedFrogColumn - 1),
                new LeafCoordinate(selectedFrogRow + 1, selectedFrogColumn - 1))){
            getLeaf(new LeafCoordinate(selectedFrogRow + 2, selectedFrogColumn - 1)).setValidHop(true);
        }

        // Diagonal up right
        if(isValidHop(new LeafCoordinate(selectedFrogRow + 2, selectedFrogColumn + 1),
                new LeafCoordinate(selectedFrogRow + 1, selectedFrogColumn + 1))){
            getLeaf(new LeafCoordinate(selectedFrogRow + 2, selectedFrogColumn + 1)).setValidHop(true);
        }

        // Diagonal down left
        if(isValidHop(new LeafCoordinate(selectedFrogRow - 2, selectedFrogColumn - 1),
                new LeafCoordinate(selectedFrogRow - 1, selectedFrogColumn - 1))){
            getLeaf(new LeafCoordinate(selectedFrogRow - 2, selectedFrogColumn - 1)).setValidHop(true);
        }

        // Diagonal down right
        if(isValidHop(new LeafCoordinate(selectedFrogRow - 2, selectedFrogColumn + 1),
                new LeafCoordinate(selectedFrogRow - 1, selectedFrogColumn + 1))){
            getLeaf(new LeafCoordinate(selectedFrogRow - 2, selectedFrogColumn + 1)).setValidHop(true);
        }

    }

    /**
     * Checking if the given destination is an empty leaf and the eatenFrogLeaf contains a green frog
     * @param destinationLeaf The coordinates of the leaf hopping to
     * @param eatenFrogLeaf The coordinates of the leaf holding the hopped upon frog (eaten)
     * @return True if given destination is an empty leaf and the eatenFrogLeaf contains a green frog,
     * False otherwise
     */
    private boolean isValidHop(LeafCoordinate destinationLeaf, LeafCoordinate eatenFrogLeaf){
        boolean isValidHop = false;

        if(getLeaf(destinationLeaf) != null &&
                getLeaf(destinationLeaf).getType() == LEAF_TYPE.EMPTY &&
                getLeaf(eatenFrogLeaf) != null &&
                getLeaf(eatenFrogLeaf).getType() == LEAF_TYPE.GREEN_FROG){
            isValidHop = true;
        }

        return isValidHop;
    }

    /**
     * Indicating if the swamp contains only one red frog and no green ones
     * @return True if only red frog, False otherwise
     */
    public boolean isOnlyRedFrog(){
        boolean isOnlyRedFrog = true;

        for(int i = 0 ; i < leaves.length; i ++){
            for(int j = 0; j < leaves[i].length; j ++){
                if(leaves[i][j].getType() == LEAF_TYPE.GREEN_FROG){
                    isOnlyRedFrog = false;
                }
            }
        }

        return  isOnlyRedFrog;
    }
}