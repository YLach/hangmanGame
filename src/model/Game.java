package model;

/**
 * Created by YannL on 09/11/2016.
 */
public class Game {
    public final static String NEW_GAME = "=newgame=";
    public final static String GAME_OVER = "=gameover=";
    public final static String GAME_WIN = "=congratulation=";

    private int score;
    private StringBuilder wordToGuess;
    private int nbFailedAttempts;

    public Game() {
        this.score = 0;
        this.nbFailedAttempts = 0;
        this.wordToGuess = new StringBuilder();
    }

    public void newGame(int length) {
        this.nbFailedAttempts = 0;
        this.wordToGuess = new StringBuilder(new String(new char[length]).replace("\0", "-"));
    }


    public void modifyWordToGuess(String currentView) {
        //if (currentView.length() == wordToGuess.length())
        wordToGuess = new StringBuilder(currentView);
    }



    public int getScore() {
        return score;
    }

    public StringBuilder getWordToGuess() {
        return wordToGuess;
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
}
