package logic;

import java.util.ArrayList;
import java.util.List;

import storage.LevelRecord;

/**
 * Created by Tamir on 24/02/2018.
 * Representing a level in the game with it's difficulty, id and starting state.
 */

public class Level {
    private final static String SOLUTION_HOPS_SEPARATOR = "|";
    private final static String GREEN_FROGS_SEPARATOR = "%";

    private DIFFICULTY difficulty;
    private int id;

    // The locating of all frog at the initial state of this level
    private int redFrogLocation;
    private int[] greenFrogsLocations;

    private List<Hop> solution;

    // The record time the user solved the level
    // (null if not yet solved)
    private int recordSeconds;
    private int recordMinutes;
    private int recordHours;

    // Indicating if the user has viewed this level's solution
    private boolean isSolutionViewed;

    public DIFFICULTY getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(DIFFICULTY difficulty) {
        this.difficulty = difficulty;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getRedFrogLocation() {
        return redFrogLocation;
    }

    public void setRedFrogLocation(int redFrogLocation) {
        this.redFrogLocation = redFrogLocation;
    }

    public int[] getGreenFrogsLocations() {
        return greenFrogsLocations;
    }

    public void setGreenFrogsLocations(int[] greenFrogsLocations) {
        this.greenFrogsLocations = greenFrogsLocations;
    }

    public List<Hop> getSolution() {
        return solution;
    }

    public void setSolution(List<Hop> solution) {
        this.solution = solution;
    }

    public boolean isSolutionViewed() {
        return isSolutionViewed;
    }

    public void setSolutionViewed(boolean solutionViewed) {
        isSolutionViewed = solutionViewed;
    }

    public int getRecordSeconds() {
        return recordSeconds;
    }

    public void setRecordSeconds(int recordSeconds) {
        this.recordSeconds = recordSeconds;
    }

    public int getRecordMinutes() {
        return recordMinutes;
    }

    public void setRecordMinutes(int recordMinutes) {
        this.recordMinutes = recordMinutes;
    }

    public int getRecordHours() {
        return recordHours;
    }

    public void setRecordHours(int recordHours) {
        this.recordHours = recordHours;
    }

    public boolean isSolved(){
        return this.recordHours != 0 || this.recordMinutes != 0 || this.recordSeconds != 0;
    }

    public Level(DIFFICULTY difficulty,
            int id,

            // The locating of all frog at the initial state of this level
            int redFrogLocation,
            int[] greenFrogsLocations,

            List<Hop> solution,

            // The record time the user solved the level
            // (null if not yet solved)
            int recordSeconds,
            int recordMinutes,
            int recordHours,

            // Indicating if the user has viewed this level's solution
            boolean isSolutionViewed){
        this.difficulty = difficulty;
        this.redFrogLocation = redFrogLocation;
        this.id = id;
        this.greenFrogsLocations = greenFrogsLocations;
        this.solution = solution;
        this.recordHours = recordHours;
        this.recordMinutes = recordMinutes;
        this.recordSeconds = recordSeconds;
        this.isSolutionViewed = isSolutionViewed;
    }

    /**
     * Converting a record from the DB to a Level instance
     * (filling the indexes of the green frogs, the solution...)
     * @param levelRecord The record from the DB
     */
    public Level(LevelRecord levelRecord){
        this.difficulty = DIFFICULTY.getTypeByCode(levelRecord.getDifficulty());
        this.id = levelRecord.getId();
        this.redFrogLocation = levelRecord.getRedFrogLocation();

        if(levelRecord.getGreenFrogs() != null) {
            // Splitting the locations
            String[] frogsLocations = levelRecord.getGreenFrogs().split(GREEN_FROGS_SEPARATOR);

            // Adding each location to the array:
            this.greenFrogsLocations = new int[frogsLocations.length];
            for(int i = 0; i < frogsLocations.length; i ++){
                this.greenFrogsLocations[i] = Integer.parseInt(frogsLocations[i]);
            }
        }

        if(levelRecord.getSolution() != null){
            // Splitting the Hops
            String[] hops = levelRecord.getSolution().split("\\" + SOLUTION_HOPS_SEPARATOR);

            solution = new ArrayList<>();

            // Adding each hop to the list:
            for (String hop : hops) {
                solution.add(new Hop(hop));
            }
        }

        this.isSolutionViewed = levelRecord.getIsSolutionViewed() == 1;

        this.recordSeconds = levelRecord.getHighScoreSeconds();
        this.recordMinutes = levelRecord.getHighScoreMinutes();
        this.recordHours = levelRecord.getHighScoreHours();
    }

    /**
     * Converting this instance to LevelRecord object that can be stored in DB
     * @return LevelRecord instance representing this level
     */
    public LevelRecord toLevelRecord(){
        String greenFrogs = getGreenFrogsString();
        String solution = getSolutionString();
        int isSolutionViewed = this.isSolutionViewed ? 1: 0;

        return new LevelRecord(this.id,
                this.recordHours,
                this.recordMinutes,
                this.recordSeconds,
                this.difficulty.getCode(),
                this.redFrogLocation,
                greenFrogs,
                solution,
                isSolutionViewed);
    }

    public String getGreenFrogsString(){
        StringBuilder greenFrogsLocations = new StringBuilder();

        if(this.greenFrogsLocations != null &&
                this.greenFrogsLocations.length > 0) {
            for (int currFrogLocation : this.greenFrogsLocations) {
                greenFrogsLocations.append(currFrogLocation);
                greenFrogsLocations.append(GREEN_FROGS_SEPARATOR);
            }
        }

        return greenFrogsLocations.toString();
    }

    public String getSolutionString(){
        StringBuilder greenFrogsLocations = new StringBuilder();

        if(this.solution != null &&
                this.solution.size() > 0) {
            for (Hop currHop : this.solution) {
                greenFrogsLocations.append(currHop.toString());
                greenFrogsLocations.append(SOLUTION_HOPS_SEPARATOR);
            }

            // Removing last separator
            greenFrogsLocations.deleteCharAt(greenFrogsLocations.length() - 1);
        }

        return greenFrogsLocations.toString();
    }

    public String getRecordString(){
        return String.format("%02d:%02d:%02d", this.recordHours, this.recordMinutes, this.recordSeconds);
    }
}
