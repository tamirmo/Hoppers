package storage;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import storage.LevelsDBContract.Level;

/**
 * Created by Tamir on 25/02/2018.
 */

public class LevelsDbHelper extends SQLiteOpenHelper {
    // Indicating some operation did not succeed
    public static final int OPERATION_UNSUCCESSFUL = -1;

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "Levels.db";

    public LevelsDbHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(LevelsDBContract.Level.SQL_CREATE_LEVELS);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(LevelsDBContract.Level.SQL_DELETE_LEVELS);
        onCreate(db);
    }

    public int addLevel(LevelRecord recordToInsert) {
        int newRowId = OPERATION_UNSUCCESSFUL;
        try {
            // Gets the data repository in write mode
            SQLiteDatabase db = getWritableDatabase();

            // Create a new map of values, where column names are the keys
            ContentValues values = new ContentValues();
            values.put(Level.COLUMN_NAME_DIFFICULTY, recordToInsert.getDifficulty());
            values.put(Level.COLUMN_NAME_GREEN_FROGS_LOCATIONS, recordToInsert.getGreenFrogs());
            values.put(Level.COLUMN_NAME_HIGH_SCORE_HOURS, recordToInsert.getHighScoreHours());
            values.put(Level.COLUMN_NAME_HIGH_SCORE_MINUTES, recordToInsert.getHighScoreMinutes());
            values.put(Level.COLUMN_NAME_HIGH_SCORE_SECONDS, recordToInsert.getHighScoreSeconds());
            values.put(Level.COLUMN_NAME_IS_SOLUTION_VIEWED, recordToInsert.getIsSolutionViewed());
            values.put(Level.COLUMN_NAME_RED_FROG_LOCATION, recordToInsert.getRedFrogLocation());

            // Insert the new row, returning the primary key value of the new row
            newRowId = (int)db.insert(Level.TABLE_NAME, null, values);
        }
        catch(Exception ex){
            ex.printStackTrace();

            // When something went wrong returning INVALID_ROW_ID
            return newRowId;
        }

        return newRowId;
    }

    public LevelRecord[] getLevels() {
        SQLiteDatabase db = getReadableDatabase();

        // null to indicate something went wrong:
        LevelRecord[] levels = null;
        Cursor cursor = null;

        try {
            // How you want the results sorted in the resulting Cursor
            String sortOrder =
                    Level._ID + SQLConstants.DESC;

            cursor = db.query(
                    Level.TABLE_NAME,                     // The table to query
                    null,                            // The columns to return (all)
                    null,                                // The columns for the WHERE clause (none)
                    null,                            // The values for the WHERE clause (none)
                    null,                                     // don't group the rows
                    null,                                     // don't filter by row groups
                    sortOrder                                 // The sort order
            );
            if (cursor.getCount() > 0) {
                int currLevelIndex = 0;
                levels = new LevelRecord[cursor.getCount()];
                while (cursor.moveToNext()) {
                    levels[currLevelIndex++] = createLevelRecordFromCursor(cursor);
                }
            }
            else {
                // There are no scores (differentiating from an error)
                levels = new LevelRecord[0];
            }
        }
        catch (Exception ex) {
            ex.printStackTrace();
            return levels;
        }
        finally {
            if(cursor != null){
                cursor.close();
            }
        }

        return levels;
    }

    public synchronized long updateHighScoreById(long levelId,
                                                     int hours,
                                                     int minutes,
                                                     int seconds){
        long recordsUpdated = OPERATION_UNSUCCESSFUL;

        try {
            SQLiteDatabase db = getWritableDatabase();

            ContentValues cv = new ContentValues();

            cv.put(Level.COLUMN_NAME_HIGH_SCORE_HOURS, hours);
            cv.put(Level.COLUMN_NAME_HIGH_SCORE_MINUTES, minutes);
            cv.put(Level.COLUMN_NAME_HIGH_SCORE_SECONDS, seconds);

            recordsUpdated = db.update(Level.TABLE_NAME, cv, Level._ID + SQLConstants.EQUALS + levelId, null);
        }
        catch (Exception ex) {
            ex.printStackTrace();
            return recordsUpdated;
        }

        return recordsUpdated;
    }

    private LevelRecord createLevelRecordFromCursor(Cursor cursor){
        LevelRecord record;

        int id = cursor.getInt(
                cursor.getColumnIndexOrThrow(Level._ID));
        int highScoreHours = cursor.getInt(
                cursor.getColumnIndexOrThrow(Level.COLUMN_NAME_HIGH_SCORE_HOURS));
        int highScoreMinutes = cursor.getInt(
                cursor.getColumnIndexOrThrow(Level.COLUMN_NAME_HIGH_SCORE_MINUTES));
        int highScoreSeconds = cursor.getInt(
                cursor.getColumnIndexOrThrow(Level.COLUMN_NAME_HIGH_SCORE_SECONDS));
        int difficulty = cursor.getInt(
                cursor.getColumnIndexOrThrow(Level.COLUMN_NAME_DIFFICULTY));
        int redFrogLocation = cursor.getInt(
                cursor.getColumnIndexOrThrow(Level.COLUMN_NAME_RED_FROG_LOCATION));
        String greenFrogs = cursor.getString(
                cursor.getColumnIndexOrThrow(Level.COLUMN_NAME_GREEN_FROGS_LOCATIONS));
        String solution = cursor.getString(
                cursor.getColumnIndexOrThrow(Level.COLUMN_NAME_SOLUTION));
        int isSolutionViewed = cursor.getInt(
                cursor.getColumnIndexOrThrow(Level.COLUMN_NAME_IS_SOLUTION_VIEWED));

        record = new LevelRecord(id,
                                highScoreHours,
                                highScoreMinutes,
                                highScoreSeconds,
                                difficulty,
                                redFrogLocation,
                                greenFrogs,
                                solution,
                                isSolutionViewed);

        return record;
    }

}
