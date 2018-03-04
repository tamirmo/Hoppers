package storage;

import android.provider.BaseColumns;

import static storage.SQLConstants.*;

/**
 * Created by Tamir on 25/02/2018.
 * Defining the High scores table schema
 */

final class LevelsDBContract {
    // make the constructor private to prevent instantiating this class
    private LevelsDBContract () {}

    /* Inner class that defines the table contents */
    static class Level implements BaseColumns {

        // make the constructor private to prevent instantiating this class
        private Level() {}

        static final String TABLE_NAME = "Level";
        static final String COLUMN_NAME_HIGH_SCORE_HOURS = "highScoreHours";
        static final String COLUMN_NAME_HIGH_SCORE_MINUTES = "highScoreMinutes";
        static final String COLUMN_NAME_HIGH_SCORE_SECONDS = "highScoreSeconds";
        static final String COLUMN_NAME_DIFFICULTY = "difficulty";
        static final String COLUMN_NAME_RED_FROG_LOCATION = "redFrogLocation";
        static final String COLUMN_NAME_GREEN_FROGS_LOCATIONS = "greenFrogs";
        static final String COLUMN_NAME_SOLUTION = "solution";
        static final String COLUMN_NAME_IS_SOLUTION_VIEWED = "isSolutionViewed";

        static final String SQL_CREATE_LEVELS =
                CREATE_TABLE + Level.TABLE_NAME +
                        " (" +
                        Level._ID + INTEGER + PRIMARY_KEY + COMMA +
                        Level.COLUMN_NAME_DIFFICULTY + INTEGER + COMMA +
                        Level.COLUMN_NAME_HIGH_SCORE_HOURS + INTEGER + COMMA +
                        Level.COLUMN_NAME_HIGH_SCORE_MINUTES + INTEGER + COMMA +
                        Level.COLUMN_NAME_HIGH_SCORE_SECONDS + INTEGER + COMMA +
                        Level.COLUMN_NAME_RED_FROG_LOCATION + INTEGER + COMMA +
                        Level.COLUMN_NAME_GREEN_FROGS_LOCATIONS + TEXT + COMMA +
                        Level.COLUMN_NAME_IS_SOLUTION_VIEWED + INTEGER + COMMA +
                        Level.COLUMN_NAME_SOLUTION + TEXT +
                        ")";

        static final String SQL_DELETE_LEVELS =
                DROP_TABLE + Level.TABLE_NAME;

    }
}

