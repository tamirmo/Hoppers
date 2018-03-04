package logic;

import android.util.SparseArray;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Tamir on 24/02/2018.
 * An enum representing the game's difficulties.
 */

public enum DIFFICULTY {
    BEGINNER(0),
    INTERMEDIATE(1),
    ADVANCED(2),
    EXPERT(3);

    private final int code;

    // The count of enum options
    public static final int SIZE = DIFFICULTY.values().length;

    private static final SparseArray<DIFFICULTY> LOOKUP
            = new SparseArray<>();

    DIFFICULTY(int index) {
        this.code = index;
    }

    public int getCode() {
        return code;
    }

    static {
        for(DIFFICULTY s : EnumSet.allOf(DIFFICULTY.class))
            LOOKUP.put(s.getCode(), s);
    }

    public static DIFFICULTY getTypeByCode(int code) {
        return LOOKUP.get(code);
    }

    /**
     * This method checks if the given code is a valid difficulty code
     * @param code the code to check
     * @return True if the given code is valid (in range), False if not
     */
    public static boolean isValidCode(int code) {
        return code <= SIZE;
    }
}
