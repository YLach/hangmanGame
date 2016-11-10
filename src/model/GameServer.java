package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class GameServer extends Game {
    private String wordToFind;
    private int maxFailedAttempts;
    private List<String> dictionnary;
    private Random r;

    public GameServer(List<String> words, int nbAttempts) {
        super();
        this.maxFailedAttempts = nbAttempts;
        this.dictionnary = words;
        r = new Random(); // TODO : need a seed ?
        newGame();
    }

    public void newGame() {
        pickNewWord();
        System.out.println("WORD TO FIND : " + wordToFind);
        //super.newGame(wordToFind.length());
        currentWorToGuess = new StringBuilder(new String(new char[wordToFind.length()]).replace("\0", "-"));
        super.newGame();
    }

    private void pickNewWord() {
        if (dictionnary != null) {
            int index = r.nextInt(dictionnary.size() - 1);
            wordToFind = dictionnary.get(index);
        }
    }

    public boolean containLetter(char l) {
        boolean isContained = false;
        int index = getWordToFind().indexOf(l);
        StringBuilder sb = getCurrentViewOfWord();
        while (index >= 0) {
            sb.setCharAt(index, l);
            index = getWordToFind().indexOf(l, index+1);

            if (!isContained)
                isContained = true;
        }
        return isContained;
    }


    public void incrementFailedAttempts() {
        if (!isFinished)
            nbFailedAttempts++;
    }

    public void decrementScore() {
        if (!isFinished)
            score--;
    }

    public void incrementScore() {
        if (!isFinished)
            score++;
    }

    public String getWordToFind() {
        return wordToFind;
    }

    public int getMaxFailedAttempts() {
        return maxFailedAttempts;
    }
}
