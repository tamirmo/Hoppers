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

    public boolean isRemoteOpponent() {
        return isRemoteOpponent;
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
                true);

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
                12,
                11,
                true);

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
        solution.add(new Hop(new LeafCoordinate(1, 1), new LeafCoordinate(3, 0), new LeafCoordinate(2, 1)));

        Level level = new Level(DIFFICULTY.ADVANCED,
                21,
                LeafCoordinate.getCellIndex(3, 0),
                greenFrogsLocations,
                solution,
                0,
                0,
                0,
                false);

        levelsDbHelper.addLevel(level.toLevelRecord());

        greenFrogsLocations = new int[8];
        greenFrogsLocations[0] = LeafCoordinate.getCellIndex(0, 0);
        greenFrogsLocations[1] = LeafCoordinate.getCellIndex(0, 1);
        greenFrogsLocations[2] = LeafCoordinate.getCellIndex(1, 0);
        greenFrogsLocations[3] = LeafCoordinate.getCellIndex(1, 1);
        greenFrogsLocations[4] = LeafCoordinate.getCellIndex(2, 0);
        greenFrogsLocations[5] = LeafCoordinate.getCellIndex(2, 1);
        greenFrogsLocations[6] = LeafCoordinate.getCellIndex(3, 1);
        greenFrogsLocations[7] = LeafCoordinate.getCellIndex(4, 2);

        solution = new ArrayList<>();
        solution.add(new Hop(new LeafCoordinate(0, 0), new LeafCoordinate(0, 2), new LeafCoordinate(0, 1)));
        solution.add(new Hop(new LeafCoordinate(2, 0), new LeafCoordinate(2, 2), new LeafCoordinate(2, 1)));
        solution.add(new Hop(new LeafCoordinate(2, 2), new LeafCoordinate(0, 1), new LeafCoordinate(1, 1)));
        solution.add(new Hop(new LeafCoordinate(0, 2), new LeafCoordinate(0, 0), new LeafCoordinate(0, 1)));
        solution.add(new Hop(new LeafCoordinate(0, 0), new LeafCoordinate(2, 1), new LeafCoordinate(1, 0)));
        solution.add(new Hop(new LeafCoordinate(3, 0), new LeafCoordinate(1, 1), new LeafCoordinate(2, 1)));
        solution.add(new Hop(new LeafCoordinate(4, 2), new LeafCoordinate(2, 1), new LeafCoordinate(3, 1)));
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
        greenFrogsLocations[1] = LeafCoordinate.getCellIndex(0, 1);
        greenFrogsLocations[2] = LeafCoordinate.getCellIndex( 1, 0);
        greenFrogsLocations[3] = LeafCoordinate.getCellIndex(1, 1);
        greenFrogsLocations[4] = LeafCoordinate.getCellIndex(2, 1);
        greenFrogsLocations[5] = LeafCoordinate.getCellIndex(3, 1);
        greenFrogsLocations[6] = LeafCoordinate.getCellIndex(4, 0);
        greenFrogsLocations[7] = LeafCoordinate.getCellIndex(4, 1);
        greenFrogsLocations[8] = LeafCoordinate.getCellIndex(4, 2);

        List<Hop> solution = new ArrayList<>();
        solution.add(new Hop(new LeafCoordinate(2, 0), new LeafCoordinate(2, 2), new LeafCoordinate(2, 1)));
        solution.add(new Hop(new LeafCoordinate(0, 0), new LeafCoordinate(0, 2), new LeafCoordinate(0, 1)));
        solution.add(new Hop(new LeafCoordinate(0, 2), new LeafCoordinate(2, 1), new LeafCoordinate(1, 1)));
        solution.add(new Hop(new LeafCoordinate(2, 2), new LeafCoordinate(2, 0), new LeafCoordinate(2, 1)));
        solution.add(new Hop(new LeafCoordinate(4, 2), new LeafCoordinate(2, 1), new LeafCoordinate(3, 1)));
        solution.add(new Hop(new LeafCoordinate(1, 0), new LeafCoordinate(3, 1), new LeafCoordinate(2, 1)));
        solution.add(new Hop(new LeafCoordinate(4, 0), new LeafCoordinate(4, 2), new LeafCoordinate(4, 1)));
        solution.add(new Hop(new LeafCoordinate(4, 2), new LeafCoordinate(2, 1), new LeafCoordinate(3, 1)));
        solution.add(new Hop(new LeafCoordinate(2, 0), new LeafCoordinate(2, 2), new LeafCoordinate(2, 1)));


        Level level = new Level(DIFFICULTY.EXPERT,
                31,
                LeafCoordinate.getCellIndex(2, 0),
                greenFrogsLocations,
                solution,
                0,
                0,
                0,
                false);

        levelsDbHelper.addLevel(level.toLevelRecord());

        greenFrogsLocations = new int[9];
        greenFrogsLocations[0] = LeafCoordinate.getCellIndex(0, 0);
        greenFrogsLocations[1] = LeafCoordinate.getCellIndex(2, 0);
        greenFrogsLocations[2] = LeafCoordinate.getCellIndex(4, 0);
        greenFrogsLocations[3] = LeafCoordinate.getCellIndex(1, 0);
        greenFrogsLocations[4] = LeafCoordinate.getCellIndex(3, 0);
        greenFrogsLocations[5] = LeafCoordinate.getCellIndex(1, 1);
        greenFrogsLocations[6] = LeafCoordinate.getCellIndex(3, 1);
        greenFrogsLocations[7] = LeafCoordinate.getCellIndex(0, 2);
        greenFrogsLocations[8] = LeafCoordinate.getCellIndex(2, 2);

        solution = new ArrayList<>();
        solution.add(new Hop(new LeafCoordinate(2, 1), new LeafCoordinate(4, 2), new LeafCoordinate(3, 1)));
        solution.add(new Hop(new LeafCoordinate(4, 0), new LeafCoordinate(2, 1), new LeafCoordinate(3, 0)));
        solution.add(new Hop(new LeafCoordinate(1, 0), new LeafCoordinate(3, 1), new LeafCoordinate(2, 1)));
        solution.add(new Hop(new LeafCoordinate(0, 2), new LeafCoordinate(2, 1), new LeafCoordinate(1, 1)));
        solution.add(new Hop(new LeafCoordinate(3, 1), new LeafCoordinate(1, 0), new LeafCoordinate(2, 1)));
        solution.add(new Hop(new LeafCoordinate(0, 0), new LeafCoordinate(2, 1), new LeafCoordinate(1, 0)));
        solution.add(new Hop(new LeafCoordinate(4, 2), new LeafCoordinate(0, 2), new LeafCoordinate(2, 2)));
        solution.add(new Hop(new LeafCoordinate(2, 0), new LeafCoordinate(2, 2), new LeafCoordinate(2, 1)));
        solution.add(new Hop(new LeafCoordinate(0, 2), new LeafCoordinate(4, 2), new LeafCoordinate(2, 2)));

        level = new Level(DIFFICULTY.EXPERT,
                32,
                LeafCoordinate.getCellIndex(2, 1),
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

        levelsDbHelper.dropDb();

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
        currLevel = levels.get(levelId - 1);
        seconds = minutes = hours = 0;
        this.isRemoteOpponent = isRemoteOpponent;
        hops.clear();

        swamp.setLevel(currLevel);

        startTimer();
    }

    /**
     * This method prepares the start of showing the solution for the user
     */
    public void startShowSolution(){
        swamp.setLevel(currLevel);
        solutionIterator = currLevel.getSolution().listIterator();

        // Updating the database of the solution viewed
        new Thread(new Runnable() {
            @Override
            public void run() {
                levelsDbHelper.updateSolutionViewedById(currLevel.getId(), true);
            }
        }).start();

        // Updating the local level
        currLevel.setSolutionViewed(true);
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

    public Leaf getLeaf(int index){
        Leaf leaf = null;

        if(swamp != null){
            leaf = swamp.getLeaf(new LeafCoordinate(index));
        }

        return leaf;
    }

    public void setSelectedLeaf(int index){
        if(swamp != null){
            swamp.selectLeaf(new LeafCoordinate(index));
        }
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
        // Starting only if not already running
        if(playTimeTimer == null) {
            // Starting a timer for each second to increase game time duration:
            playTimeTimer = new Timer(false);
            playTimeTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    increaseTime();
                }
            }, 1000, 1000);
        }
    }

    private void stopTimer(){
        if(playTimeTimer != null){
            playTimeTimer.cancel();
            playTimeTimer = null;
        }
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
        stopTimer();
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

            LeafCoordinate eatenFrogCord;

            eatenFrogCord = getEatenFrogLeaf(swamp.getSelectedFrogCord(), destinationLeafCord);

            hop = new Hop(swamp.getSelectedFrogCord(), destinationLeafCord, eatenFrogCord);

            // Removing the eaten frog from the game and moving the hopped frog
            swamp.makeHop(hop);

            turnResult.setHop(hop);
            turnResult.setResult(TURN_RESULT.HOP);
            hops.add(hop);
        }
        else {
            turnResult.setResult(TURN_RESULT.INVALID_HOP);
        }

        if(swamp.isOnlyRedFrog()){
            turnResult.setResult(TURN_RESULT.GAME_WON);

            // Game is over, stopping timer
            stopTimer();

            if(isGameDurationRecord()){
                // Saving the record (is a separated thread [not UI thread] )
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        levelsDbHelper.updateHighScoreById(currLevel.getId(), hours, minutes, seconds);
                    }
                }).start();

                // Updating the local level:
                currLevel.setRecordHours(hours);
                currLevel.setRecordMinutes(minutes);
                currLevel.setRecordSeconds(seconds);
            }
        }

        return turnResult;
    }

    /**
     * Calculating the eaten frog in the hop that a frog that it's coordinates are given to the given destFrogIndex.
     * @param originalLeaf The original coordinates of the frog hopping
     * @param destinationLeafCord The index of the leaf hopping to
     * @return LeafCoordinate The coordinates of the eaten frog
     */
    private LeafCoordinate getEatenFrogLeaf(LeafCoordinate originalLeaf, LeafCoordinate destinationLeafCord){
        int originalFrogRow = originalLeaf.getRow();
        int originalFrogCol = originalLeaf.getColumn();
        int destFrogIndex = LeafCoordinate.getCellIndex(destinationLeafCord.getRow(), destinationLeafCord.getColumn());

        LeafCoordinate eatenFrog = null;

        // Tf the frog is a row with 3 columns
        if(originalFrogRow % 2 == 0){
            eatenFrog = getEatenFrogLeafEvenRow(destFrogIndex, originalFrogRow, originalFrogCol);
        }else{
            eatenFrog = getEatenFrogLeafOddRow(destFrogIndex, originalFrogRow, originalFrogCol);
        }

        return eatenFrog;
    }

    /**
     * Calculating the eaten frog in the hop that a frog that it's coordinates are given to the given destFrogIndex.
     * (This method is for a leaf that has 3 frogs in it's row [even row number])
     * @param destFrogIndex The index of the leaf hopping to
     * @param originalFrogRow The original row of the frog hopping
     * @param originalFrogCol The original column of the frog hopping
     * @return LeafCoordinate The coordinates of the eaten frog
     */
    private LeafCoordinate getEatenFrogLeafEvenRow(int destFrogIndex,
                                                   int originalFrogRow,
                                                   int originalFrogCol){
        LeafCoordinate eatenFrog = null;

        // Handling the "straight" hop
        // (not available for frogs in a row with two leaves):

        // Hop down
        if(destFrogIndex == LeafCoordinate.getCellIndex(originalFrogRow + 4, originalFrogCol)){
            eatenFrog = new LeafCoordinate(originalFrogRow + 2, originalFrogCol);
        }

        // Hop up
        if(destFrogIndex == LeafCoordinate.getCellIndex(originalFrogRow - 4, originalFrogCol)){
            eatenFrog = new LeafCoordinate(originalFrogRow - 2, originalFrogCol);
        }

        // Hop right
        if(destFrogIndex == LeafCoordinate.getCellIndex(originalFrogRow, originalFrogCol + 2)){
            eatenFrog = new LeafCoordinate(originalFrogRow, originalFrogCol + 1);
        }

        // Hop left
        if(destFrogIndex == LeafCoordinate.getCellIndex(originalFrogRow, originalFrogCol - 2)){
            eatenFrog = new LeafCoordinate(originalFrogRow, originalFrogCol - 1);
        }

        // Handling the "diagonal" hop:

        // Diagonal down left
        if(destFrogIndex == LeafCoordinate.getCellIndex(originalFrogRow + 2, originalFrogCol - 1)){
            eatenFrog = new LeafCoordinate(originalFrogRow + 1, originalFrogCol - 1);
        }

        // Diagonal down right
        if(destFrogIndex == LeafCoordinate.getCellIndex(originalFrogRow + 2, originalFrogCol + 1)){
            eatenFrog = new LeafCoordinate(originalFrogRow + 1, originalFrogCol);
        }

        // Diagonal up left
        if(destFrogIndex == LeafCoordinate.getCellIndex(originalFrogRow - 2, originalFrogCol - 1)){
            eatenFrog = new LeafCoordinate(originalFrogRow - 1, originalFrogCol - 1);
        }

        // Diagonal up right
        if(destFrogIndex == LeafCoordinate.getCellIndex(originalFrogRow - 2, originalFrogCol + 1)){
            eatenFrog = new LeafCoordinate(originalFrogRow - 1, originalFrogCol);
        }

        return eatenFrog;
    }

    /**
     * Calculating the eaten frog in the hop that a frog that it's coordinates are given to the given destFrogIndex.
     * (This method is for a leaf that has 2 frogs in it's row [odd row number])
     * @param destFrogIndex The index of the leaf hopping to
     * @param originalFrogRow The original row of the frog hopping
     * @param originalFrogCol The original column of the frog hopping
     * @return LeafCoordinate The coordinates of the eaten frog
     */
    private LeafCoordinate getEatenFrogLeafOddRow(int destFrogIndex,
                                                   int originalFrogRow,
                                                   int originalFrogCol){
        LeafCoordinate eatenFrog = null;

        // Handling the "diagonal" hop:

        // Diagonal down left
        if(destFrogIndex == LeafCoordinate.getCellIndex(originalFrogRow + 2, originalFrogCol - 1)){
            eatenFrog = new LeafCoordinate(originalFrogRow + 1, originalFrogCol);
        }

        // Diagonal down right
        if(destFrogIndex == LeafCoordinate.getCellIndex(originalFrogRow + 2, originalFrogCol + 1)){
            eatenFrog = new LeafCoordinate(originalFrogRow + 1, originalFrogCol + 1);
        }

        // Diagonal up left
        if(destFrogIndex == LeafCoordinate.getCellIndex(originalFrogRow - 2, originalFrogCol - 1)){
            eatenFrog = new LeafCoordinate(originalFrogRow - 1, originalFrogCol);
        }

        // Diagonal up right
        if(destFrogIndex == LeafCoordinate.getCellIndex(originalFrogRow - 2, originalFrogCol + 1)){
            eatenFrog = new LeafCoordinate(originalFrogRow - 1, originalFrogCol + 1);
        }

        return eatenFrog;
    }

    private boolean isGameDurationRecord(){
        boolean isRecord = false;

        if(hours > currLevel.getRecordHours() ||
                // If the hours are the same, checking minutes
                (hours == currLevel.getRecordHours() &&
                        (minutes > currLevel.getRecordMinutes() ||
                                // If minutes are the same, checking the seconds
                                (minutes == currLevel.getRecordMinutes() && seconds > currLevel.getRecordSeconds())) )){
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

            // The last selected is not relevant anymore
            swamp.clearSelectedLeaf();

            wasHoppedBack = true;
        }

        return wasHoppedBack;
    }

    /**
     * Creates a list of all levels in the given difficulty
     * @param difficulty The difficulty to get levels of
     * @return A list of levels in the given difficulty
     */
    public List<Level> getLevelsByDifficulty(DIFFICULTY difficulty){

        List<Level> difficultyLevels = new ArrayList<>();
        for (Level level : levels){
            if (level.getDifficulty() == difficulty){
                difficultyLevels.add(level);
            }
        }

        return difficultyLevels;
    }

    public int getRandomLevel(DIFFICULTY difficulty){
        List<Level> chosenDifficultyLevels = getLevelsByDifficulty(difficulty);

        // Choosing a random level:

        int levelListIndex = randInt(chosenDifficultyLevels.size()-1);
        Level chosenLevel = chosenDifficultyLevels.get(levelListIndex);

        return chosenLevel.getId();
    }

    /**
     * Returns a random number between 0 and max, inclusive.
     *
     * @param max Maximum value.
     * @return Integer between 0 and max, inclusive.
     * @see java.util.Random#nextInt(int)
     */
    private int randInt(int max) {

        // nextInt is normally exclusive of the top value,
        // so add 1 to make it inclusive
        return rand.nextInt((max) + 1);
    }
}
