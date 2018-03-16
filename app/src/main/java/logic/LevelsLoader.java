package logic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import storage.LevelRecord;
import storage.LevelsDbHelper;

/**
 * Created by Tamir on 3/12/2018.
 * Loading the levels from the database or inserting if not exist yet.
 */

class LevelsLoader {
    private List<Level> levels;
    private LevelsDbHelper levelsDbHelper;

    LevelsLoader(LevelsDbHelper levelsDbHelper){
        this.levelsDbHelper = levelsDbHelper;
    }

    List<Level> getLevels(){
        // Getting the levels from the database
        LevelRecord[] dbLevels = levelsDbHelper.getLevels();
        levels = new LinkedList<>();

        if(dbLevels != null && dbLevels.length > 0){
            // Converting the levels from DB to Level objects:
            for(LevelRecord record: dbLevels){
                levels.add(new Level(record));
            }
        }
        // The db was not yet filled with levels
        else{
            // Creating the levels:

            // Creating the maps helping with levels creations
            initHopsMap();
            initLeavesCordMap();

            initializeBeginner();
            initializeIntermediate();
            initializeAdvanced();
            initializeExpert();

            // Adding each level to the DB
            for (Level level : levels) {
                levelsDbHelper.addLevel(level.toLevelRecord());
            }
        }

        return levels;
    }

    private void initializeBeginner(){
        levels.add(new LevelDetails(DIFFICULTY.BEGINNER,
                0,
                "B,H,L",
                "A",
                "A-C,C-M,M-K").toLevel());

        levels.add(new LevelDetails(DIFFICULTY.BEGINNER,
                1,
                "A,D,G",
                "I",
                "I-E,A-G,E-I").toLevel());

        levels.add(new LevelDetails(DIFFICULTY.BEGINNER,
                2,
                "A,C,D,E",
                "F",
                "A-G,F-H,C-G,H-F").toLevel());

        levels.add(new LevelDetails(DIFFICULTY.BEGINNER,
                3,
                "A,D,E,F",
                "G",
                "G-C,A-G,F-H,C-M").toLevel());

        levels.add(new LevelDetails(DIFFICULTY.BEGINNER,
                4,
                "A,F,G,I",
                "H",
                "A-K,H-F,K-G,F-H").toLevel());

        levels.add(new LevelDetails(DIFFICULTY.BEGINNER,
                5,
                "G,H,I,L",
                "A",
                "L-F,A-K,H-F,K-A").toLevel());

        levels.add(new LevelDetails(DIFFICULTY.BEGINNER,
                6,
                "B,D,F,K,L",
                "A",
                "A-C,K-A,A-G,L-B,C-A").toLevel());

        levels.add(new LevelDetails(DIFFICULTY.BEGINNER,
                7,
                "A,D,F,I,H",
                "G",
                "A-K,G-A,K-G,H-F,A-K").toLevel());

        levels.add(new LevelDetails(DIFFICULTY.BEGINNER,
                8,
                "A,D,E,F,J",
                "G",
                "G-C,A-G,F-H,C-M,M-G").toLevel());

        levels.add(new LevelDetails(DIFFICULTY.BEGINNER,
                9,
                "A,D,F,I,H,J",
                "E",
                "A-K,H-L,L-F,K-A,A-G,E-I").toLevel());

        levels.add(new LevelDetails(DIFFICULTY.BEGINNER,
                10,
                "A,D,F,G,I,M",
                "B",
                "F-H,A-G,I-E,M-C,C-G,B-L").toLevel());
    }

    private void initializeIntermediate(){

        levels.add(new LevelDetails(DIFFICULTY.INTERMEDIATE,
                11,
                "A,D,E,I,L",
                "J",
                "L-F,F-B,A-C,C-G,J-D").toLevel());

        levels.add(new LevelDetails(DIFFICULTY.INTERMEDIATE,
                12,
                "A,B,E,F,H,I",
                "L",
                "A-C,H-B,C-A,A-K,K-G,L-B").toLevel());

        levels.add(new LevelDetails(DIFFICULTY.INTERMEDIATE,
                13,
                "A,B,D,I,K,L",
                "F",
                "K-G,A-C,L-B,C-A,A-G,F-H").toLevel());

        levels.add(new LevelDetails(DIFFICULTY.INTERMEDIATE,
                14,
                "A,B,D,F,J",
                "K",
                "A-G,K-A,J-D,B-F,A-K").toLevel());

        levels.add(new LevelDetails(DIFFICULTY.INTERMEDIATE,
                15,
                "A,D,E,G,H,I,L",
                "J",
                "G-C,A-G,J-D,C-M,M-K,K-G,D-J").toLevel());

        levels.add(new LevelDetails(DIFFICULTY.INTERMEDIATE,
                16,
                "B,E,F,G,H,I,J,L",
                "D",
                "G-C,C-M,L-H,M-C,C-A,A-K,K-G,D-J").toLevel());

        levels.add(new LevelDetails(DIFFICULTY.INTERMEDIATE,
                17,
                "A,B,D,E,F,G,H,I,L",
                "J",
                "A-C,G-K,K-A,A-G,E-I,C-M,M-K,K-G,J-D").toLevel());

        levels.add(new LevelDetails(DIFFICULTY.INTERMEDIATE,
                18,
                "A,B,D,E,F,J",
                "I",
                "A-C,F-B,B-H,C-M,M-G,I-E").toLevel());

        levels.add(new LevelDetails(DIFFICULTY.INTERMEDIATE,
                19,
                "A,B,E,F,I,J,L",
                "D",
                "A-C,L-H,H-B,C-A,A-K,K-G,D-J").toLevel());

        levels.add(new LevelDetails(DIFFICULTY.INTERMEDIATE,
                20,
                "B,D,F,G,I,J,L",
                "E",
                "G-M,M-K,K-A,B-F,A-K,K-G,E-I").toLevel());
    }

    private void initializeAdvanced(){

        levels.add(new LevelDetails(DIFFICULTY.ADVANCED,
                21,
                "A,D,F,G,J,L,M",
                "I",
                "I-E,A-G,J-D,M-K,K-A,A-G,E-I").toLevel());

        levels.add(new LevelDetails(DIFFICULTY.ADVANCED,
                22,
                "A,B,D,E,F,G,J,M",
                "I",
                "A-C,F-H,H-B,C-A,A-G,I-E,M-G,E-I").toLevel());

        levels.add(new LevelDetails(DIFFICULTY.ADVANCED,
                23,
                "A,B,D,E,F,H,I,M",
                "L",
                "A-K,K-G,E-I,M-C,C-A,A-G,L-F,F-H").toLevel());

        levels.add(new LevelDetails(DIFFICULTY.ADVANCED,
                24,
                "A,C,D,E,G,I,J,K,M",
                "F",
                "F-H,A-G,J-D,K-G,D-J,C-G,H-F,M-G,F-H").toLevel());

        levels.add(new LevelDetails(DIFFICULTY.ADVANCED,
                25,
                "A,B,D,E,F,H,I,L",
                "J",
                "A-C,H-B,C-A,A-K,L-F,K-A,A-G,J-D").toLevel());

        levels.add(new LevelDetails(DIFFICULTY.ADVANCED,
                26,
                "A,B,D,E,F,G,I",
                "J",
                "A-C,G-K,K-A,A-G,J-D,C-G,D-J").toLevel());

        levels.add(new LevelDetails(DIFFICULTY.ADVANCED,
                27,
                "A,D,E,F,H,I,K",
                "B",
                "F-L,K-M,M-C,C-G,B-L,A-G,L-B").toLevel());

        levels.add(new LevelDetails(DIFFICULTY.ADVANCED,
                28,
                "A,D,E,F,I,J,K",
                "G",
                "G-C,A-G,F-H,K-G,H-L,L-B,C-A").toLevel());

        levels.add(new LevelDetails(DIFFICULTY.ADVANCED,
                29,
                "A,B,C,D,E,F,I,K",
                "J",
                "A-G,J-D,K-G,E-I,C-A,A-K,K-G,D-J").toLevel());

        levels.add(new LevelDetails(DIFFICULTY.ADVANCED,
                30,
                "C,D,E,F,J,L,M",
                "A",
                "A-K,M-G,D-J,C-G,L-H,H-F,K-A").toLevel());
    }

    private void initializeExpert(){
        levels.add(new LevelDetails(DIFFICULTY.EXPERT,
                31,
                "A,B,D,E,G,J,K,L,M",
                "F",
                "F-H,A-C,C-G,H-F,M-G,D-J,K-M,M-G,F-H").toLevel());

        levels.add(new LevelDetails(DIFFICULTY.EXPERT,
                32,
                "A,C,D,E,F,H,I,J,K",
                "G",
                "G-M,K-G,D-J,C-G,J-D,A-G,M-C,F-H,C-M").toLevel());

        levels.add(new LevelDetails(DIFFICULTY.EXPERT,
                33,
                "A,D,F,E,J,L,M",
                "G",
                "A-K,G-A,M-G,E-I,K-G,L-B,A-C").toLevel());

        levels.add(new LevelDetails(DIFFICULTY.EXPERT,
                34,
                "A,D,E,F,H,I,L,M",
                "G",
                "A-K,G-A,K-G,E-I,M-K,K-G,H-F,A-K").toLevel());

        levels.add(new LevelDetails(DIFFICULTY.EXPERT,
                35,
                "A,B,D,E,F,G,H,J,L,M",
                "I",
                "A-C,H-B,I-E,C-A,A-G,J-D,M-K,K-A,A-G,E-I").toLevel());

        levels.add(new LevelDetails(DIFFICULTY.EXPERT,
                36,
                "A,C,D,E,F,H,I,L",
                "G",
                "A-K,K-M,G-K,C-G,D-J,M-G,H-F,K-A").toLevel());

        levels.add(new LevelDetails(DIFFICULTY.EXPERT,
                37,
                "A,B,C,D,E,F,I,J,K",
                "G",
                "G-M,A-G,F-H,K-G,B-L,C-G,L-B,M-C,C-A").toLevel());

        levels.add(new LevelDetails(DIFFICULTY.EXPERT,
                38,
                "B,D,F,G,H,J,K,M",
                "A",
                "B-L,M-G,D-J,K-M,M-G,A-K,H-F,K-A").toLevel());

        levels.add(new LevelDetails(DIFFICULTY.EXPERT,
                39,
                "A,B,C,D,E,F,H,I,K,L",
                "M",
                "K-G,D-J,M-K,C-M,M-G,F-H,A-C,C-G,H-F,K-A").toLevel());

        levels.add(new LevelDetails(DIFFICULTY.EXPERT,
                40,
                "A,B,C,D,E,G,I,J,K,L,M",
                "F",
                "F-H,A-G,J-D,K-G,E-I,C-A,A-G,H-F,M-K,K-G,F-H").toLevel());
    }

    // Each letter represents a coordinate of a leaf:
    private static LeafCoordinate A = new LeafCoordinate(0, 0);
    private static LeafCoordinate B = new LeafCoordinate(0, 1);
    private static LeafCoordinate C = new LeafCoordinate(0, 2);
    private static LeafCoordinate D = new LeafCoordinate(1, 0);
    private static LeafCoordinate E = new LeafCoordinate(1, 1);
    private static LeafCoordinate F = new LeafCoordinate(2, 0);
    private static LeafCoordinate G = new LeafCoordinate(2, 1);
    private static LeafCoordinate H = new LeafCoordinate(2, 2);
    private static LeafCoordinate I = new LeafCoordinate(3, 0);
    private static LeafCoordinate J = new LeafCoordinate(3, 1);
    private static LeafCoordinate K = new LeafCoordinate(4, 0);
    private static LeafCoordinate L = new LeafCoordinate(4, 1);
    private static LeafCoordinate M = new LeafCoordinate(4, 2);

    // Creating a hop for each hop possible to use when reading solutions
    private static Hop A_C = new Hop(A, C, B);
    private static Hop A_G = new Hop(A, G, D);
    private static Hop A_K = new Hop(A, K, F);
    private static Hop B_F = new Hop(B, F, D);
    private static Hop B_H = new Hop(B, H, E);
    private static Hop B_L = new Hop(B, L, G);
    private static Hop C_A = new Hop(C, A, B);
    private static Hop C_M = new Hop(C, M, H);
    private static Hop C_G = new Hop(C, G, E);
    private static Hop D_J = new Hop(D, J, G);
    private static Hop E_I = new Hop(E, I, G);
    private static Hop F_H = new Hop(F, H, G);
    private static Hop F_L = new Hop(F, L, I);
    private static Hop F_B = new Hop(F, B, D);
    private static Hop G_M = new Hop(G, M, J);
    private static Hop G_K = new Hop(G, K, I);
    private static Hop G_C = new Hop(G, C, E);
    private static Hop G_A = new Hop(G, A, D);
    private static Hop H_L = new Hop(H, L, J);
    private static Hop H_B = new Hop(H, B, E);
    private static Hop H_F = new Hop(H, F, G);
    private static Hop I_E = new Hop(I, E, G);
    private static Hop J_D = new Hop(J, D, G);
    private static Hop K_A = new Hop(K, A, F);
    private static Hop K_M = new Hop(K, M, L);
    private static Hop K_G = new Hop(K, G, I);
    private static Hop L_F = new Hop(L, F, I);
    private static Hop L_H = new Hop(L, H, J);
    private static Hop L_B = new Hop(L, B, G);
    private static Hop M_K = new Hop(M, K, L);
    private static Hop M_G = new Hop(M, G, J);
    private static Hop M_C = new Hop(M, C, H);

    private Map<String, Hop> hopsMap = new HashMap<>();
    private Map<String, LeafCoordinate> leavesCordsMap = new HashMap<>();

    private void initHopsMap(){
        hopsMap.put("A-C", A_C);
        hopsMap.put("A-G", A_G);
        hopsMap.put("A-K", A_K);
        hopsMap.put("A-F", A_K);
        hopsMap.put("B-F", B_F);
        hopsMap.put("B-H", B_H);
        hopsMap.put("B-L", B_L);
        hopsMap.put("C-A", C_A);
        hopsMap.put("C-M", C_M);
        hopsMap.put("C-G", C_G);
        hopsMap.put("D-J", D_J);
        hopsMap.put("E-I", E_I);
        hopsMap.put("F-H", F_H);
        hopsMap.put("F-L", F_L);
        hopsMap.put("F-B", F_B);
        hopsMap.put("G-M", G_M);
        hopsMap.put("G-K", G_K);
        hopsMap.put("G-C", G_C);
        hopsMap.put("G-A", G_A);
        hopsMap.put("H-L", H_L);
        hopsMap.put("H-B", H_B);
        hopsMap.put("H-F", H_F);
        hopsMap.put("I-E", I_E);
        hopsMap.put("J-D", J_D);
        hopsMap.put("K-A", K_A);
        hopsMap.put("K-M", K_M);
        hopsMap.put("K-G", K_G);
        hopsMap.put("L-F", L_F);
        hopsMap.put("L-H", L_H);
        hopsMap.put("L-B", L_B);
        hopsMap.put("M-K", M_K);
        hopsMap.put("M-G", M_G);
        hopsMap.put("M-C", M_C);
    }

    private void initLeavesCordMap(){
        leavesCordsMap.put("A", A);
        leavesCordsMap.put("B", B);
        leavesCordsMap.put("C", C);
        leavesCordsMap.put("D", D);
        leavesCordsMap.put("E", E);
        leavesCordsMap.put("F", F);
        leavesCordsMap.put("G", G);
        leavesCordsMap.put("H", H);
        leavesCordsMap.put("I", I);
        leavesCordsMap.put("J", J);
        leavesCordsMap.put("K", K);
        leavesCordsMap.put("L", L);
        leavesCordsMap.put("M", M);
    }

    private class LevelDetails{

        String greenFrogsLocations;
        String redFrogLocation;
        String solution;
        DIFFICULTY difficulty;
        int id;

        LevelDetails(DIFFICULTY difficulty,
                int id,
                String greenFrogsLocations,
                String redFrogLocation,
                String solution){
            this.greenFrogsLocations = greenFrogsLocations;
            this.redFrogLocation = redFrogLocation;
            this.solution = solution;
            this.difficulty = difficulty;
            this.id = id;
        }

        Level toLevel(){

            ArrayList<Hop> solutionList = new ArrayList<>();

            // Creating the solution according to the string given (each step is separated by ','):
            String[] splitSol = solution.split(",");
            for(String solStep : splitSol){
                solutionList.add(hopsMap.get(solStep));
            }

            // Creating the green frog locations according to the string given (each frog is separated by ','):
            String[] splitGreenFrogs = greenFrogsLocations.split(",");
            int[] greenFrogs = new int[splitGreenFrogs.length];

            for(int i = 0; i < splitGreenFrogs.length; i ++){
                greenFrogs[i] = leavesCordsMap.get(splitGreenFrogs[i]).getCellIndex();
            }

            // Creating a level with the details gotten
            return new Level(difficulty, id,
                    leavesCordsMap.get(redFrogLocation).getCellIndex(),
                    greenFrogs,
                    solutionList,
                    0,
                    0,
                    0,
                    false);
        }
    }
}
