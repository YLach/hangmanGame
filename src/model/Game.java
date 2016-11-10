package model;

/**
 * Created by YannL on 09/11/2016.
 */
public class Game {
    public final static String NEW_GAME = "newgame";
    public final static String GAME_OVER = "gameover";
    public final static String GAME_WIN = "congratulation";

    protected int score;
    protected StringBuilder currentWorToGuess; //Current view of the word to guess
    protected int nbFailedAttempts;
    protected boolean isFinished;

    public Game() {
        this.score = 0;
        this.nbFailedAttempts = 0;
    }

    public void newGame() {
        isFinished = false;
        this.nbFailedAttempts = 0;
        //this.currentWorToGuess = new StringBuilder(new String(new char[length]).replace("\0", "-"));
    }


    public void modifyCurrentViewOfWord(String currentView) {
        if (!isFinished)
            currentWorToGuess = new StringBuilder(currentView);
    }

    public int getScore() {
        return score;
    }

    public StringBuilder getCurrentViewOfWord() {
        return currentWorToGuess;
    }

    public int getNbFailedAttempts() {
        return nbFailedAttempts;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void setNbFailedAttempts(int nbFailedAttempts) {
        this.nbFailedAttempts = nbFailedAttempts;
    }

    public void setStatusToFinished()
    {
        this.isFinished = true;
    }
}
