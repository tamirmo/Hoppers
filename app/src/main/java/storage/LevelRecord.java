package storage;

/**
 * Created by Tamir on 25/02/2018.
 * Representing one record of a level from the database.
 */

public class LevelRecord {
    private int id;
    private int highScoreHours;
    private int highScoreMinutes;
    private int highScoreSeconds;
    private int difficulty;
    private int redFrogLocation;
    private String greenFrogs;
    private String solution;
    private int isSolutionViewed;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getHighScoreHours() {
        return highScoreHours;
    }

    public void setHighScoreHours(int highScoreHours) {
        this.highScoreHours = highScoreHours;
    }

    public int getHighScoreMinutes() {
        return highScoreMinutes;
    }

    public void setHighScoreMinutes(int highScoreMinutes) {
        this.highScoreMinutes = highScoreMinutes;
    }

    public int getHighScoreSeconds() {
        return highScoreSeconds;
    }

    public void setHighScoreSeconds(int highScoreSeconds) {
        this.highScoreSeconds = highScoreSeconds;
    }

    public int getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
    }

    public int getRedFrogLocation() {
        return redFrogLocation;
    }

    public void setRedFrogLocation(int redFrogLocation) {
        this.redFrogLocation = redFrogLocation;
    }

    public String getGreenFrogs() {
        return greenFrogs;
    }

    public void setGreenFrogs(String greenFrogs) {
        this.greenFrogs = greenFrogs;
    }

    public String getSolution() {
        return solution;
    }

    public void setSolution(String solution) {
        this.solution = solution;
    }

    public int getIsSolutionViewed() {
        return isSolutionViewed;
    }

    public void setIsSolutionViewed(int isSolutionViewed) {
        this.isSolutionViewed = isSolutionViewed;
    }

    public LevelRecord(int id,
                int highScoreHours,
                int highScoreMinutes,
                int highScoreSeconds,
                int difficulty,
                int redFrogLocation,
                String greenFrogs,
                String solution,
                int isSolutionViewed){
        this.id = id;
        this.highScoreHours = highScoreHours;
        this.highScoreMinutes = highScoreMinutes;
        this.highScoreSeconds = highScoreSeconds;
        this.difficulty = difficulty;
        this.redFrogLocation = redFrogLocation;
        this.greenFrogs = greenFrogs;
        this.solution = solution;
        this.isSolutionViewed = isSolutionViewed;
    }

}
