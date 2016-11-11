package model;


public class Game {
    public final static String NEW_GAME = "newgame";
    public final static String GAME_OVER = "gameover";
    public final static String GAME_WIN = "congratulation";

    protected int score;
    protected StringBuilder currentWorToGuess; //Current view of the word to guess
    protected int nbFailedAttempts;
    protected boolean isFinished;

    public Game() {
    }

    public void newGame() {
        isFinished = false;
    }

    // Getters
    public int getScore() {
        return score;
    }

    public StringBuilder getCurrentViewOfWord() {
        return currentWorToGuess;
    }

    public int getNbFailedAttempts() {
        return nbFailedAttempts;
    }

    // Setters
    public void modifyCurrentViewOfWord(String currentView) {
        if (!isFinished)
            currentWorToGuess = new StringBuilder(currentView);
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
