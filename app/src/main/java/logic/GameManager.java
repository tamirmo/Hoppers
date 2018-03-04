package logic;

import android.content.Context;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Random;
import java.util.Stack;
import java.util.Timer;
import java.util.TimerTask;

import storage.LevelRecord;
import storage.LevelsDbHelper;

/**
 * Created by Tamir on 24/02/2018.
 */

public class GameManager {

    private static GameManager instance;

    public enum TURN_RESULT {HOP, INVALID_HOP, GAME_WON}

    private Swamp swamp;
    private Level currLevel;
    private Stack<Hop> hops;
    private List<Level> levels;

    // Time played:
    private int seconds;
    private int minutes;
    private int hours;

    private Timer playTimeTimer;
    private IOnGameDurationChanged gameDurationChangedListener;
    private LevelsDbHelper levelsDbHelper;
    // Indicating if the current game is with an opponent
    private boolean isRemoteOpponent;
    private Random rand;

    // An iterator for moving forward and backward in the current level solution
    private ListIterator<Hop> solutionIterator;

    public static GameManager getInstance(){
        if(instance == null){
            instance = new GameManager();
        }
        return instance;
    }

    public Level getCurrLevel(){
        return currLevel;
    }

    public void setGameDurationChangedListener(IOnGameDurationChanged listener){
        this.gameDurationChangedListener = listener;
    }

    public List<Level> getLevels(){
        return levels;
    }

    private void initializeBeginner(){
        int[] greenFrogsLocations = new int[3];
        greenFrogsLocations[0] = LeafCoordinate.getCellIndex(0, 1);
        greenFrogsLocations[1] = LeafCoordinate.getCellIndex(2, 2);
        greenFrogsLocations[2] = LeafCoordinate.getCellIndex(4, 1);

        List<Hop> solution = new ArrayList<>();
        solution.add(new Hop(new LeafCoordinate(0, 0), new LeafCoordinate(0, 2), new LeafCoordinate(0, 1)));
        solution.add(new Hop(new LeafCoordinate(0, 2), new LeafCoordinate(4, 2), new LeafCoordinate(2, 2)));
        solution.add(new Hop(new LeafCoordinate(4, 2), new LeafCoordinate(4, 0), new LeafCoordinate(4, 1)));

        Level level = new Level(DIFFICULTY.BEGINNER,
                1,
                LeafCoordinate.getCellIndex(0, 0),
                greenFrogsLocations,
                solution,
                0,
                0,
                0,
                false);

        levelsDbHelper.addLevel(level.toLevelRecord());

        greenFrogsLocations[0] = LeafCoordinate.getCellIndex(0, 0);
        greenFrogsLocations[1] = LeafCoordinate.getCellIndex(1, 0);
        greenFrogsLocations[2] = LeafCoordinate.getCellIndex(2, 1);

        solution.clear();
        solution.add(new Hop(new LeafCoordinate(3, 0), new LeafCoordinate(1, 1), new LeafCoordinate(2, 1)));
        solution.add(new Hop(new LeafCoordinate(0, 0), new LeafCoordinate(2, 1), new LeafCoordinate(1, 0)));
        solution.add(new Hop(new LeafCoordinate(1, 1), new LeafCoordinate(3, 0), new LeafCoordinate(2, 1)));

        level = new Level(DIFFICULTY.BEGINNER,
                2,
                LeafCoordinate.getCellIndex(3, 0),
                greenFrogsLocations,
                solution,
                0,
                0,
                0,
                false);

        levelsDbHelper.addLevel(level.toLevelRecord());

        greenFrogsLocations = new int[4];

        greenFrogsLocations[0] = LeafCoordinate.getCellIndex(0, 0);
        greenFrogsLocations[1] = LeafCoordinate.getCellIndex(1, 0);
        greenFrogsLocations[2] = LeafCoordinate.getCellIndex(1, 1);
        greenFrogsLocations[3] = LeafCoordinate.getCellIndex(0, 2);

        solution.clear();
        solution.add(new Hop(new LeafCoordinate(0, 0), new LeafCoordinate(2, 1), new LeafCoordinate(1, 0)));
        solution.add(new Hop(new LeafCoordinate(2, 0), new LeafCoordinate(2, 2), new LeafCoordinate(2, 1)));
        solution.add(new Hop(new LeafCoordinate(0, 2), new LeafCoordinate(2, 1), new LeafCoordinate(1, 1)));
        solution.add(new Hop(new LeafCoordinate(2, 2), new LeafCoordinate(2, 0), new LeafCoordinate(2, 1)));

        level = new Level(DIFFICULTY.BEGINNER,
                3,
                LeafCoordinate.getCellIndex(2, 0),
                greenFrogsLocations,
                solution,
                0,
                0,
                0,
                false);

        levelsDbHelper.addLevel(level.toLevelRecord());

        greenFrogsLocations = new int[4];

        greenFrogsLocations[0] = LeafCoordinate.getCellIndex(0, 0);
        greenFrogsLocations[1] = LeafCoordinate.getCellIndex(1, 0);
        greenFrogsLocations[2] = LeafCoordinate.getCellIndex(2, 0);
        greenFrogsLocations[3] = LeafCoordinate.getCellIndex(1, 1);

        solution.clear();
        solution.add(new Hop(new LeafCoordinate(2, 1), new LeafCoordinate(0, 2), new LeafCoordinate(1, 1)));
        solution.add(new Hop(new LeafCoordinate(0, 0), new LeafCoordinate(2, 1), new LeafCoordinate(1, 0)));
        solution.add(new Hop(new LeafCoordinate(2, 0), new LeafCoordinate(2, 2), new LeafCoordinate(2, 1)));
        solution.add(new Hop(new LeafCoordinate(0, 2), new LeafCoordinate(4, 2), new LeafCoordinate(2, 2)));

        level = new Level(DIFFICULTY.BEGINNER,
                4,
                LeafCoordinate.getCellIndex(2, 1),
                greenFrogsLocations,
                solution,
                0,
                0,
                0,
                false);

        levelsDbHelper.addLevel(level.toLevelRecord());

        greenFrogsLocations = new int[4];

        greenFrogsLocations[0] = LeafCoordinate.getCellIndex(0, 0);
        greenFrogsLocations[1] = LeafCoordinate.getCellIndex(2, 0);
        greenFrogsLocations[2] = LeafCoordinate.getCellIndex(3, 0);
        greenFrogsLocations[3] = LeafCoordinate.getCellIndex(2, 1);

        solution.clear();
        solution.add(new Hop(new LeafCoordinate(0, 0), new LeafCoordinate(4, 0), new LeafCoordinate(2, 0)));
        solution.add(new Hop(new LeafCoordinate(2, 2), new LeafCoordinate(2, 0), new LeafCoordinate(2, 1)));
        solution.add(new Hop(new LeafCoordinate(4, 0), new LeafCoordinate(2, 1), new LeafCoordinate(3, 0)));
        solution.add(new Hop(new LeafCoordinate(2, 0), new LeafCoordinate(2, 2), new LeafCoordinate(2, 1)));

        level = new Level(DIFFICULTY.BEGINNER,
                5,
                LeafCoordinate.getCellIndex(2, 2),
                greenFrogsLocations,
                solution,
                0,
                0,
                0,
                false);

        levelsDbHelper.addLevel(level.toLevelRecord());

        greenFrogsLocations = new int[4];

        greenFrogsLocations[0] = LeafCoordinate.getCellIndex(2, 1);
        greenFrogsLocations[1] = LeafCoordinate.getCellIndex(2, 2);
        greenFrogsLocations[2] = LeafCoordinate.getCellIndex(3, 0);
        greenFrogsLocations[3] = LeafCoordinate.getCellIndex(4, 1);

        solution.clear();
        solution.add(new Hop(new LeafCoordinate(4, 1), new LeafCoordinate(2, 0), new LeafCoordinate(3, 0)));
        solution.add(new Hop(new LeafCoordinate(0, 0), new LeafCoordinate(4, 0), new LeafCoordinate(2, 0)));
        solution.add(new Hop(new LeafCoordinate(2, 2), new LeafCoordinate(2, 0), new LeafCoordinate(2, 1)));
        solution.add(new Hop(new LeafCoordinate(4, 0), new LeafCoordinate(0, 0), new LeafCoordinate(2, 0)));

        level = new Level(DIFFICULTY.BEGINNER,
                6,
                LeafCoordinate.getCellIndex(0, 0),
                greenFrogsLocations,
                solution,
                0,
                0,
                0,
                false);

        levelsDbHelper.addLevel(level.toLevelRecord());

        greenFrogsLocations = new int[5];

        greenFrogsLocations[0] = LeafCoordinate.getCellIndex(0, 1);
        greenFrogsLocations[1] = LeafCoordinate.getCellIndex(1, 0);
        greenFrogsLocations[2] = LeafCoordinate.getCellIndex(2, 0);
        greenFrogsLocations[3] = LeafCoordinate.getCellIndex(4, 0);
        greenFrogsLocations[4] = LeafCoordinate.getCellIndex(4, 1);

        solution.clear();
        solution.add(new Hop(new LeafCoordinate(0, 0), new LeafCoordinate(0, 2), new LeafCoordinate(0, 1)));
        solution.add(new Hop(new LeafCoordinate(4, 0), new LeafCoordinate(0, 0), new LeafCoordinate(2, 0)));
        solution.add(new Hop(new LeafCoordinate(0, 0), new LeafCoordinate(2, 1), new LeafCoordinate(1, 0)));
        solution.add(new Hop(new LeafCoordinate(4, 1), new LeafCoordinate(0, 1), new LeafCoordinate(2, 1)));
        solution.add(new Hop(new LeafCoordinate(0, 2), new LeafCoordinate(0, 0), new LeafCoordinate(0, 1)));

        level = new Level(DIFFICULTY.BEGINNER,
                7,
                LeafCoordinate.getCellIndex(0, 0),
                greenFrogsLocations,
                solution,
                0,
                0,
                0,
                false);

        levelsDbHelper.addLevel(level.toLevelRecord());
    }

    private void initializeIntermediate(){
        int[] greenFrogsLocations = new int[5];
        greenFrogsLocations[0] = LeafCoordinate.getCellIndex(0, 0);
        greenFrogsLocations[1] = LeafCoordinate.getCellIndex(1, 0);
        greenFrogsLocations[2] = LeafCoordinate.getCellIndex(3, 0);
        greenFrogsLocations[3] = LeafCoordinate.getCellIndex(4, 1);
        greenFrogsLocations[4] = LeafCoordinate.getCellIndex(1, 1);

        List<Hop> solution = new ArrayList<>();
        solution.add(new Hop(new LeafCoordinate(4, 1), new LeafCoordinate(2, 0), new LeafCoordinate(3, 0)));
        solution.add(new Hop(new LeafCoordinate(2, 0), new LeafCoordinate(0, 1), new LeafCoordinate(1, 0)));
        solution.add(new Hop(new LeafCoordinate(0, 0), new LeafCoordinate(0, 2), new LeafCoordinate(0, 1)));
        solution.add(new Hop(new LeafCoordinate(0, 2), new LeafCoordinate(2, 1), new LeafCoordinate(1, 1)));
        solution.add(new Hop(new LeafCoordinate(3, 1), new LeafCoordinate(1, 0), new LeafCoordinate(2, 1)));

        Level level = new Level(DIFFICULTY.INTERMEDIATE,
                12,
                LeafCoordinate.getCellIndex(3, 1),
                greenFrogsLocations,
                solution,
                0,
                0,
                0,
                false);

        levelsDbHelper.addLevel(level.toLevelRecord());

        greenFrogsLocations = new int[6];
        greenFrogsLocations[0] = LeafCoordinate.getCellIndex(0, 0);
        greenFrogsLocations[1] = LeafCoordinate.getCellIndex(0, 1);
        greenFrogsLocations[2] = LeafCoordinate.getCellIndex(1, 1);
        greenFrogsLocations[3] = LeafCoordinate.getCellIndex(2, 0);
        greenFrogsLocations[4] = LeafCoordinate.getCellIndex(2, 2);
        greenFrogsLocations[5] = LeafCoordinate.getCellIndex(3, 0);

        solution = new ArrayList<>();
        solution.add(new Hop(new LeafCoordinate(0, 0), new LeafCoordinate(0, 2), new LeafCoordinate(0, 1)));
        solution.add(new Hop(new LeafCoordinate(2, 2), new LeafCoordinate(0, 1), new LeafCoordinate(1, 1)));
        solution.add(new Hop(new LeafCoordinate(0, 2), new LeafCoordinate(0, 0), new LeafCoordinate(0, 1)));
        solution.add(new Hop(new LeafCoordinate(0, 0), new LeafCoordinate(4, 0), new LeafCoordinate(2, 0)));
        solution.add(new Hop(new LeafCoordinate(4, 0), new LeafCoordinate(2, 1), new LeafCoordinate(3, 0)));
        solution.add(new Hop(new LeafCoordinate(4, 1), new LeafCoordinate(0, 1), new LeafCoordinate(2, 1)));

        level = new Level(DIFFICULTY.INTERMEDIATE,
                13,
                LeafCoordinate.getCellIndex(4, 1),
                greenFrogsLocations,
                solution,
                0,
                0,
                0,
                false);

        levelsDbHelper.addLevel(level.toLevelRecord());
    }

    private void initializeAdvanced(){
        int[] greenFrogsLocations = new int[7];
        greenFrogsLocations[0] = LeafCoordinate.getCellIndex(0, 0);
        greenFrogsLocations[1] = LeafCoordinate.getCellIndex(1, 0);
        greenFrogsLocations[2] = LeafCoordinate.getCellIndex(2, 0);
        greenFrogsLocations[3] = LeafCoordinate.getCellIndex(2, 1);
        greenFrogsLocations[4] = LeafCoordinate.getCellIndex(3, 1);
        greenFrogsLocations[5] = LeafCoordinate.getCellIndex(4, 1);
        greenFrogsLocations[6] = LeafCoordinate.getCellIndex(4, 2);

        List<Hop> solution = new ArrayList<>();
        solution.add(new Hop(new LeafCoordinate(3, 0), new LeafCoordinate(1, 1), new LeafCoordinate(2, 1)));
        solution.add(new Hop(new LeafCoordinate(0, 0), new LeafCoordinate(2, 1), new LeafCoordinate(1, 0)));
        solution.add(new Hop(new LeafCoordinate(3, 1), new LeafCoordinate(1, 0), new LeafCoordinate(2, 1)));
        solution.add(new Hop(new LeafCoordinate(4, 2), new LeafCoordinate(4, 0), new LeafCoordinate(4, 1)));
        solution.add(new Hop(new LeafCoordinate(4, 0), new LeafCoordinate(0, 0), new LeafCoordinate(2, 0)));
        solution.add(new Hop(new LeafCoordinate(0, 0), new LeafCoordinate(2, 1), new LeafCoordinate(1, 0)));

        Level level = new Level(DIFFICULTY.INTERMEDIATE,
                12,
                LeafCoordinate.getCellIndex(3, 1),
                greenFrogsLocations,
                solution,
                0,
                0,
                0,
                false);

        levelsDbHelper.addLevel(level.toLevelRecord());

        greenFrogsLocations = new int[6];
        greenFrogsLocations[0] = LeafCoordinate.getCellIndex(0, 0);
        greenFrogsLocations[1] = LeafCoordinate.getCellIndex(0, 1);
        greenFrogsLocations[2] = LeafCoordinate.getCellIndex(1, 1);
        greenFrogsLocations[3] = LeafCoordinate.getCellIndex(2, 0);
        greenFrogsLocations[4] = LeafCoordinate.getCellIndex(2, 2);
        greenFrogsLocations[5] = LeafCoordinate.getCellIndex(3, 0);

        solution = new ArrayList<>();
        solution.add(new Hop(new LeafCoordinate(0, 0), new LeafCoordinate(0, 2), new LeafCoordinate(0, 1)));
        solution.add(new Hop(new LeafCoordinate(2, 2), new LeafCoordinate(0, 1), new LeafCoordinate(1, 1)));
        solution.add(new Hop(new LeafCoordinate(0, 2), new LeafCoordinate(0, 0), new LeafCoordinate(0, 1)));
        solution.add(new Hop(new LeafCoordinate(0, 0), new LeafCoordinate(4, 0), new LeafCoordinate(2, 0)));
        solution.add(new Hop(new LeafCoordinate(4, 0), new LeafCoordinate(2, 1), new LeafCoordinate(3, 0)));
        solution.add(new Hop(new LeafCoordinate(4, 1), new LeafCoordinate(0, 1), new LeafCoordinate(2, 1)));
        solution.add(new Hop(new LeafCoordinate(1, 1), new LeafCoordinate(3, 0), new LeafCoordinate(2, 1)));

        level = new Level(DIFFICULTY.ADVANCED,
                22,
                LeafCoordinate.getCellIndex(3, 0),
                greenFrogsLocations,
                solution,
                0,
                0,
                0,
                false);

        levelsDbHelper.addLevel(level.toLevelRecord());
    }

    private void initializeExpert(){
        int[] greenFrogsLocations = new int[9];
        greenFrogsLocations[0] = LeafCoordinate.getCellIndex(0, 0);
        greenFrogsLocations[1] = LeafCoordinate.getCellIndex(2, 0);
        greenFrogsLocations[2] = LeafCoordinate.getCellIndex(4, 0);
        greenFrogsLocations[3] = LeafCoordinate.getCellIndex(1, 0);
        greenFrogsLocations[4] = LeafCoordinate.getCellIndex(3, 0);
        greenFrogsLocations[5] = LeafCoordinate.getCellIndex(1, 1);
        greenFrogsLocations[6] = LeafCoordinate.getCellIndex(3, 1);
        greenFrogsLocations[7] = LeafCoordinate.getCellIndex(0, 2);
        greenFrogsLocations[8] = LeafCoordinate.getCellIndex(2, 2);

        List<Hop> solution = new ArrayList<>();
        solution.add(new Hop(new LeafCoordinate(2, 1), new LeafCoordinate(4, 2), new LeafCoordinate(3, 1)));
        solution.add(new Hop(new LeafCoordinate(4, 0), new LeafCoordinate(2, 1), new LeafCoordinate(3, 0)));
        solution.add(new Hop(new LeafCoordinate(1, 0), new LeafCoordinate(3, 1), new LeafCoordinate(2, 1)));
        solution.add(new Hop(new LeafCoordinate(0, 2), new LeafCoordinate(2, 1), new LeafCoordinate(1, 1)));
        solution.add(new Hop(new LeafCoordinate(3, 1), new LeafCoordinate(1, 0), new LeafCoordinate(2, 1)));
        solution.add(new Hop(new LeafCoordinate(0, 0), new LeafCoordinate(2, 1), new LeafCoordinate(1, 0)));
        solution.add(new Hop(new LeafCoordinate(4, 2), new LeafCoordinate(0, 2), new LeafCoordinate(2, 2)));
        solution.add(new Hop(new LeafCoordinate(2, 0), new LeafCoordinate(2, 2), new LeafCoordinate(2, 1)));
        solution.add(new Hop(new LeafCoordinate(0, 2), new LeafCoordinate(4, 2), new LeafCoordinate(2, 2)));

        Level level = new Level(DIFFICULTY.EXPERT,
                33,
                LeafCoordinate.getCellIndex(2, 1),
                greenFrogsLocations,
                solution,
                0,
                0,
                0,
                false);

        levelsDbHelper.addLevel(level.toLevelRecord());

        greenFrogsLocations = new int[6];
        greenFrogsLocations[0] = LeafCoordinate.getCellIndex(0, 0);
        greenFrogsLocations[1] = LeafCoordinate.getCellIndex(0, 1);
        greenFrogsLocations[2] = LeafCoordinate.getCellIndex(1, 1);
        greenFrogsLocations[3] = LeafCoordinate.getCellIndex(2, 0);
        greenFrogsLocations[4] = LeafCoordinate.getCellIndex(2, 2);
        greenFrogsLocations[5] = LeafCoordinate.getCellIndex(3, 0);

        solution = new ArrayList<>();
        solution.add(new Hop(new LeafCoordinate(0, 0), new LeafCoordinate(0, 2), new LeafCoordinate(0, 1)));
        solution.add(new Hop(new LeafCoordinate(2, 2), new LeafCoordinate(0, 1), new LeafCoordinate(1, 1)));
        solution.add(new Hop(new LeafCoordinate(0, 2), new LeafCoordinate(0, 0), new LeafCoordinate(0, 1)));
        solution.add(new Hop(new LeafCoordinate(0, 0), new LeafCoordinate(4, 0), new LeafCoordinate(2, 0)));
        solution.add(new Hop(new LeafCoordinate(4, 0), new LeafCoordinate(2, 1), new LeafCoordinate(3, 0)));
        solution.add(new Hop(new LeafCoordinate(4, 1), new LeafCoordinate(0, 1), new LeafCoordinate(2, 1)));
        solution.add(new Hop(new LeafCoordinate(1, 1), new LeafCoordinate(3, 0), new LeafCoordinate(2, 1)));

        level = new Level(DIFFICULTY.ADVANCED,
                13,
                LeafCoordinate.getCellIndex(3, 0),
                greenFrogsLocations,
                solution,
                0,
                0,
                0,
                false);

        levelsDbHelper.addLevel(level.toLevelRecord());
    }

    public void initializeManager(Context context){
        levelsDbHelper = new LevelsDbHelper(context);

        rand = new Random();
        swamp = new Swamp();
        hops = new Stack<>();

        initializeBeginner();
        initializeIntermediate();
        initializeAdvanced();
        initializeExpert();

        // Pulling all levels from the DB
        LevelRecord[] levelRecords = levelsDbHelper.getLevels();

        // Converting the levels from DB to Level objects:
        levels = new LinkedList<>();
        for(LevelRecord record: levelRecords){
            levels.add(new Level(record));
        }
    }

    public void startGame(int levelId, boolean isRemoteOpponent){
        currLevel = levels.get(levelId);
        seconds = minutes = hours = 0;
        this.isRemoteOpponent = isRemoteOpponent;

        swamp.setLevel(currLevel);

        startTimer();
    }

    /**
     * This method prepares the start of showing the solution for the user
     */
    public void startShowSolution(){
        swamp.setLevel(currLevel);
        solutionIterator = currLevel.getSolution().listIterator();
    }

    public Hop nextSolutionStep(){
        // Default indicating the solution is over
        Hop nextHop = null;

        if(solutionIterator.hasNext()) {
            nextHop = solutionIterator.next();
            swamp.makeHop(nextHop);
        }

        return nextHop;
    }

    public Hop prevSolutionStep(){
        // Default indicating there is no previous solution
        Hop prevHop = null;

        if(solutionIterator.hasPrevious()) {
            prevHop = solutionIterator.previous();
            swamp.revertHop(prevHop);
        }

        return prevHop;
    }

    /**
     * Make the given hop on the swamp
     * (called in show solution process)
     * @param hop Hop, The hop to perform
     */
    public void hop(Hop hop){
        swamp.makeHop(hop);
    }

    private void startTimer(){
        // Starting a timer for each second to increase game time duration:
        playTimeTimer = new Timer(false);
        playTimeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                increaseTime();
            }
        }, 1000);
    }

    private void increaseTime(){
        if(++seconds > 60){
            seconds = 0;
            if(++minutes > 60){
                minutes = 0;
                ++hours;
            }
        }

        if(gameDurationChangedListener != null){
            String duration = getGameDuration();
            gameDurationChangedListener.onGameDurationChanged(duration);
        }
    }

    public String getGameDuration(){
        return String.format("%02d:%02d:%02d", this.hours, this.minutes, seconds );
    }

    public void pauseGame(){
        if(playTimeTimer != null){
            playTimeTimer.cancel();
        }
    }

    public void resumeGame(){
        startTimer();
    }

    /**
     * Performing a hop of the frog from the selected leaf to the given
     * (called when the user chooses to hop)
     * @param destinationLeafCord The coordinate of the leaf to hop to
     * @return The coordinate of the from eaten
     */
    public Turn hop(LeafCoordinate destinationLeafCord){
        Turn turnResult = new Turn();
        Leaf destLeaf = swamp.getLeaf(destinationLeafCord);
        Hop hop;

        if(destLeaf != null && destLeaf.isValidHop()){

            // Searching for the frog eaten:

            LeafCoordinate eatenFrogCord = new LeafCoordinate();

            int dstRow = destinationLeafCord.getRow();
            int dstCol = destinationLeafCord.getColumn();
            int origRow = swamp.getSelectedFrogCord().getRow();
            int origCol = swamp.getSelectedFrogCord().getColumn();

            // if the eating frog hopped left
            if(dstCol  < origCol){
                // The location of the eaten frog is next to it
                eatenFrogCord.setColumn(dstCol + 1);
                //dstCol + 1;
            }
            // if the eating frog hopped right
            else if(dstCol  > origCol){
                // The location of the eaten frog is next to it
                eatenFrogCord.setColumn(dstCol - 1);
                // The location of the eaten frog is ont leaf before it
                //destinationLeafCord.getColumn() - 1;
            }
            // The eaten frog and the eating frog are at the same column
            else{
                eatenFrogCord.setColumn(dstCol);
            }

            // if the eating frog hopped down
            if(dstRow  < origRow){
                // The location of the eaten frog is above to it
                eatenFrogCord.setRow(dstRow + 1);
                // dstRow + 1;
            }
            // if the eating frog hopped up
            else if(dstRow  > origRow){
                // The location of the eaten frog is ont leaf below it
                eatenFrogCord.setRow(dstRow - 1);
                //dstRow - 1
            }
            // The eaten frog and the eating frog are at the same row
            else{
                eatenFrogCord.setRow(dstRow);
            }

            // Removing the eaten frog from the game
            swamp.getLeaf(eatenFrogCord).setType(LEAF_TYPE.EMPTY);

            hop = new Hop(swamp.getSelectedFrogCord(), destinationLeafCord, eatenFrogCord);

            turnResult.setHop(hop);
            turnResult.setResult(TURN_RESULT.HOP);
            hops.add(hop);

            // If the eating frog stayed in the same row
            /*if(dstRow == origRow){
                // if the eating frog hopped left
                if(dstCol  < origCol){
                    // The location of the eaten frog is next to it
                    //dstCol + 1;
                }
                // if the eating frog hopped right
                else{
                    // The location of the eaten frog is ont leaf before it
                    //destinationLeafCord.getColumn() - 1;
                }
            }
            // The eating frog moved a row
            else {
                if(dstCol == origCol){
                    // if the eating frog hopped down
                    if(dstRow  < origRow){
                        // The location of the eaten frog is above to it
                        // dstRow + 1;
                    }
                    // if the eating frog hopped right
                    else{
                        // The location of the eaten frog is ont leaf below it
                        //dstRow - 1;
                    }
                }
                // A diagonal hop
                else{

                }
            }*/
        }
        else {
            turnResult.setResult(TURN_RESULT.INVALID_HOP);
        }

        if(swamp.isOnlyRedFrog()){
            turnResult.setResult(TURN_RESULT.GAME_WON);
            if(isGameDurationRecord()){
                // Saving the record (is a separated thread [not UI thread] )
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        levelsDbHelper.updateHighScoreById(currLevel.getId(), hours, minutes, seconds);
                    }
                }).start();
            }
        }

        return turnResult;
    }

    private boolean isGameDurationRecord(){
        boolean isRecord = false;

        if(hours < currLevel.getRecordHours() ||
                // If the hours are the same, checking minutes
                (hours == currLevel.getRecordHours() &&
                        (minutes < currLevel.getRecordMinutes() ||
                                // If minutes are the same, checking the seconds
                                (minutes == currLevel.getRecordMinutes() && seconds < currLevel.getRecordSeconds())) )){
            isRecord = true;
        }

        return isRecord;
    }

    /**
     * Returning the swamp to the state before the last hop
     * @return True if there was a hop to return before, False if not
     */
    public boolean undoLastStep(){
        boolean wasHoppedBack = false;

        if(hops != null && hops.size() > 0){
            Hop lastHop = hops.pop();

            // Returning the eaten frog to the game
            swamp.getLeaf(lastHop.getEatenFrogLeaf()).setType(LEAF_TYPE.GREEN_FROG);

            // Returning the eaten frog to the game
            LEAF_TYPE eatingFrog = swamp.getLeaf(lastHop.getFrogHoppedLeaf()).getType();
            // Returning it to the original place:
            swamp.getLeaf(lastHop.getFrogOriginalLeaf()).setType(eatingFrog);
            swamp.getLeaf(lastHop.getFrogHoppedLeaf()).setType(LEAF_TYPE.EMPTY);

            wasHoppedBack = true;
        }

        return wasHoppedBack;
    }

    public int getRandomLevel(DIFFICULTY difficulty){

        // Creating a list of all levels in the given difficulty:

        List<Level> chosenDifficultyLevels = new ArrayList<>();
        for (Level level : levels){
            if (level.getDifficulty() == difficulty){
                chosenDifficultyLevels.add(level);
            }
        }

        // Choosing a random level:

        int levelListIndex = randInt(chosenDifficultyLevels.size()-1);
        Level chosenLevel = chosenDifficultyLevels.get(levelListIndex);

        return chosenLevel.getId();
    }

    /**
     * Returns a random number between 0 and max, inclusive.
     * The difference between 0 and max can be at most
     * <code>Integer.MAX_VALUE - 1</code>.
     *
     * @param max Maximum value.  Must be greater than min.
     * @return Integer between 0 and max, inclusive.
     * @see java.util.Random#nextInt(int)
     */
    private int randInt(int max) {

        // nextInt is normally exclusive of the top value,
        // so add 1 to make it inclusive
        return rand.nextInt((max) + 1);
    }
}
