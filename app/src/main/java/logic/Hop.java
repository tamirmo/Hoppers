package logic;

/**
 * Created by Tamir on 24/02/2018.
 * Representing one turn - one frog hop from one leaf to another
 */

public class Hop {

    private final static String HOP_SEPARATOR = ",";

    private LeafCoordinate frogOriginalLeaf;
    private LeafCoordinate frogHoppedLeaf;
    private LeafCoordinate eatenFrogLeaf;

    public Hop(LeafCoordinate frogOriginalLeaf, LeafCoordinate frogHoppedLeaf, LeafCoordinate eatenFrogLeaf){
        this.frogOriginalLeaf = frogOriginalLeaf;
        this.frogHoppedLeaf = frogHoppedLeaf;
        this.eatenFrogLeaf = eatenFrogLeaf;
    }

    public Hop(String hopAsString){
        String[] coordinates = hopAsString.split(HOP_SEPARATOR);

        frogOriginalLeaf = new LeafCoordinate(coordinates[0]);
        frogHoppedLeaf = new LeafCoordinate(coordinates[1]);
        eatenFrogLeaf = new LeafCoordinate(coordinates[2]);
    }

    public LeafCoordinate getFrogOriginalLeaf() {
        return frogOriginalLeaf;
    }

    public void setFrogOriginalLeaf(LeafCoordinate frogOriginalLeaf) {
        this.frogOriginalLeaf = frogOriginalLeaf;
    }

    public LeafCoordinate getFrogHoppedLeaf() {
        return frogHoppedLeaf;
    }

    public void setFrogHoppedLeaf(LeafCoordinate frogHoppedLeaf) {
        this.frogHoppedLeaf = frogHoppedLeaf;
    }

    public LeafCoordinate getEatenFrogLeaf() {
        return eatenFrogLeaf;
    }

    public void setEatenFrogLeaf(LeafCoordinate eatenFrogLeaf) {
        this.eatenFrogLeaf = eatenFrogLeaf;
    }

    @Override
    public String toString() {
        return frogOriginalLeaf.toString() + HOP_SEPARATOR +
                frogHoppedLeaf.toString() + HOP_SEPARATOR +
                eatenFrogLeaf.toString();
    }
}
