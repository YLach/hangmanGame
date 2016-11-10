package server;

import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;
import model.Game;
import model.GameServer;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.List;


public class ConnectionHandler implements Runnable {

    private Socket clientSocket;
    private GameServer game;

    public ConnectionHandler(Socket clientSocket, List<String> words) {
        this.clientSocket = clientSocket;
        this.game = new GameServer(words, 10);
    }

    /**
     * TODO
     */
    @Override
    public void run() {
        // Open the streams
        BufferedInputStream in;
        BufferedOutputStream out;
        try {
            in = new BufferedInputStream(clientSocket.getInputStream());
            out = new BufferedOutputStream(clientSocket.getOutputStream());
        } catch (IOException e) {
            System.err.println("Could not get I/O for client socket " + clientSocket.getInetAddress() + " : " + e);
            return;
        }

        while(!clientSocket.isClosed()) {
            // Read incoming messages
            byte[] msg = new byte[4096];
            int bytesRead = 0;
            int n;

            try {
                while ((n = in.read(msg, bytesRead, 256)) != -1) {
                    bytesRead += n;
                    if (bytesRead == 4096) {
                        break;
                    }
                    if (in.available() == 0) {
                        break;
                    }
                }

                String fromClient = new String(msg, 0, bytesRead);
                //System.out.println("From client : " + fromClient);

                byte[] answer;
                StringBuilder sb = new StringBuilder();
                if (bytesRead == 1) {
                    // Letter proposed
                    if (!game.containLetter(fromClient.charAt(0)))
                        game.incrementFailedAttempts();

                    if (game.getNbFailedAttempts() == game.getMaxFailedAttempts()) {
                        // Client loses
                        game.decrementScore();
                        game.setStatusToFinished();
                        sb.append(Game.GAME_OVER);
                        sb.append('+');
                        sb.append(game.getScore());
                    } else {
                        if (game.getWordToFind().equals(game.getCurrentViewOfWord().toString())) {
                            // Client wins
                            game.incrementScore();
                            game.setStatusToFinished();
                            sb.append(Game.GAME_WIN);
                            sb.append('+');
                            sb.append(game.getCurrentViewOfWord());
                            sb.append('+');
                            sb.append(game.getScore());
                        } else {
                            sb.append(game.getCurrentViewOfWord());
                            sb.append('+');
                            sb.append(game.getNbFailedAttempts());
                        }
                    }
                } else if (fromClient.equals(Game.NEW_GAME)) {
                    // New game
                    game.newGame();
                    sb.append(game.getCurrentViewOfWord());
                    sb.append('+');
                    sb.append(game.getNbFailedAttempts());
                } else {
                    // Word proposition
                    if (game.getWordToFind().equals(fromClient)) {
                        // Client wins
                        game.modifyCurrentViewOfWord(fromClient);
                        game.incrementScore();
                        game.setStatusToFinished();
                        sb.append(Game.GAME_WIN);
                        sb.append('+');
                        sb.append(fromClient);
                        sb.append('+');
                        sb.append(game.getScore());
                    } else {
                        // Client failed
                        game.incrementFailedAttempts();
                        if (game.getNbFailedAttempts() == game.getMaxFailedAttempts()) {
                            // Client loses
                            game.decrementScore();
                            game.setStatusToFinished();
                            sb.append(Game.GAME_OVER);
                            sb.append('+');
                            sb.append(game.getScore());
                        } else {
                            sb.append(game.getCurrentViewOfWord());
                            sb.append('+');
                            sb.append(game.getNbFailedAttempts());
                        }
                    }
                }
                answer = sb.toString().getBytes();

                // Send the answer back
                for (int i = 0; i < answer.length; i++) {
                    out.write(answer[i]);
                }
                out.flush();
            } catch (IOException e) {
                // Socket closed by the client
                return;
            }
        }

    }


}
