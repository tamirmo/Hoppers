package bluetooth;

import logic.DIFFICULTY;

/**
 * Created by Tamir on 26/02/2018.
 * A class for constants sent via the bluetooth channel.
 */

public class ConnectionCodes {
    // Indicating a challenge game accepted
    final static byte ACCEPT = 4;
    final static byte FINISHED = 5;
    final static byte EXIT = 6;

    // The number to add to the difficulty code to send the other player
    private final static byte LEVEL_OFFSET = 10;

    static byte DifficultyToByteCode(DIFFICULTY difficulty){
        return (byte)(difficulty.getCode());
    }

    static DIFFICULTY ByteToDifficulty(byte difficulty){
        return DIFFICULTY.getTypeByCode(difficulty);
    }

    static byte LevelToByteCode(int level){
        return (byte)(level + LEVEL_OFFSET);
    }

    static int ByteToLevel(byte level){
        return level - LEVEL_OFFSET;
    }
}
