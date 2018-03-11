package logic;

/**
 * Created by Tamir on 24/02/2018.
 * Representing a game turn consist of a hop and a result.
 */

public class Turn {
    private GameManager.TURN_RESULT result;
    private Hop hop;

    public Hop getHop() {
        return hop;
    }

    void setHop(Hop hop) {
        this.hop = hop;
    }

    public GameManager.TURN_RESULT getResult() {
        return result;
    }

    void setResult(GameManager.TURN_RESULT result) {
        this.result = result;
    }
}
