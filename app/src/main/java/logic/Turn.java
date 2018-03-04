package logic;

/**
 * Created by Tamir on 24/02/2018.
 * Representing a game turn consist of a hop and a result.
 */

public class Turn {
    private GameManager.TURN_RESULT result;
    private Hop hop;

    /*public Turn(GameManager.TURN_RESULT result, Hop hop){
        this.result = result;
        this.hop = hop;
    }*/

    public Hop getHop() {
        return hop;
    }

    public void setHop(Hop hop) {
        this.hop = hop;
    }

    public GameManager.TURN_RESULT getResult() {
        return result;
    }

    public void setResult(GameManager.TURN_RESULT result) {
        this.result = result;
    }
}
