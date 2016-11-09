package model;

/**
 * Created by YannL on 09/11/2016.
 */
public class GameServer extends Game {
    private String wordToFind;
    private int maxFailedAttempts;

    public GameServer(String wordToFind, int nbAttempts) {
        super();
        this.wordToFind = wordToFind;
        this.maxFailedAttempts = nbAttempts;
    }

    public String getWordToFind() {
        return wordToFind;
    }

    public int getMaxFailedAttempts() {
        return maxFailedAttempts;
    }
}
