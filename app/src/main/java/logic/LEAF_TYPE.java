package logic;

import android.util.SparseArray;

import java.util.EnumSet;

/**
 * Created by Tamir on 24/02/2018.
 * An enum representing a leaf type (what it holds).
 */

public enum LEAF_TYPE {
    EMPTY(1),
    RED_FROG(2),
    GREEN_FROG(3);

    private final int code;

    private static final SparseArray<LEAF_TYPE> LOOKUP
            = new SparseArray<>();

    LEAF_TYPE(int index) {
        this.code = index;
    }

    public int getCode() {
        return code;
    }

    static {
        for(LEAF_TYPE s : EnumSet.allOf(LEAF_TYPE.class))
            LOOKUP.put(s.getCode(), s);
    }
}
