package logic;

/**
 * Created by Tamir on 24/02/2018.
 * Representing one turn - one frog hop from one leaf to another
 */

public class Hop {

    private final static String HOP_SEPARATOR = ",";

    public enum HOP_TYPE{LEFT,
        RIGHT,
        DOWN,
        UP,
        LEFT_UP,
        LEFT_DOWN,
        RIGHT_UP,
        RIGHT_DOWN}

    private LeafCoordinate frogOriginalLeaf;
    private LeafCoordinate frogHoppedLeaf;
    private LeafCoordinate eatenFrogLeaf;
    private HOP_TYPE hopType;

    Hop(LeafCoordinate frogOriginalLeaf, LeafCoordinate frogHoppedLeaf, LeafCoordinate eatenFrogLeaf){
        this.frogOriginalLeaf = frogOriginalLeaf;
        this.frogHoppedLeaf = frogHoppedLeaf;
        this.eatenFrogLeaf = eatenFrogLeaf;

        evaluateHopType();
    }

    Hop(String hopAsString){
        String[] coordinates = hopAsString.split(HOP_SEPARATOR);

        frogOriginalLeaf = new LeafCoordinate(coordinates[0]);
        frogHoppedLeaf = new LeafCoordinate(coordinates[1]);
        eatenFrogLeaf = new LeafCoordinate(coordinates[2]);

        evaluateHopType();
    }

    private void evaluateHopType(){
        // If the rows are the same it is left or right hop:
        if(frogOriginalLeaf.getRow() ==  frogHoppedLeaf.getRow()){
            if(frogOriginalLeaf.getColumn() <  frogHoppedLeaf.getColumn()){
                hopType = HOP_TYPE.RIGHT;
            }
            else {
                hopType = HOP_TYPE.LEFT;
            }
        }
        // If the cols are the same it is up or down hop:
        else if(frogOriginalLeaf.getColumn() ==  frogHoppedLeaf.getColumn()){

            if(frogOriginalLeaf.getRow() <  frogHoppedLeaf.getRow()){
                hopType = HOP_TYPE.DOWN;
            }
            else {
                hopType = HOP_TYPE.UP;
            }
        }
        // If not the same then we have a diagonal hop:
        else{
            if(frogOriginalLeaf.getRow() <  frogHoppedLeaf.getRow() &&
                    frogOriginalLeaf.getColumn() <  frogHoppedLeaf.getColumn()){
                hopType = HOP_TYPE.RIGHT_DOWN;
            }
            else if(frogOriginalLeaf.getRow() <  frogHoppedLeaf.getRow() &&
                    frogOriginalLeaf.getColumn() >  frogHoppedLeaf.getColumn()){
                hopType = HOP_TYPE.LEFT_DOWN;
            }
            else if(frogOriginalLeaf.getColumn() <  frogHoppedLeaf.getColumn()){
                hopType = HOP_TYPE.RIGHT_UP;
            }
            else{
                hopType = HOP_TYPE.LEFT_UP;
            }
        }
    }

    public LeafCoordinate getFrogOriginalLeaf() {
        return frogOriginalLeaf;
    }

    public LeafCoordinate getFrogHoppedLeaf() {
        return frogHoppedLeaf;
    }

    public LeafCoordinate getEatenFrogLeaf() {
        return eatenFrogLeaf;
    }

    public HOP_TYPE getHopType() {
        return hopType;
    }

    @Override
    public String toString() {
        return frogOriginalLeaf.toString() + HOP_SEPARATOR +
                frogHoppedLeaf.toString() + HOP_SEPARATOR +
                eatenFrogLeaf.toString();
    }
}
