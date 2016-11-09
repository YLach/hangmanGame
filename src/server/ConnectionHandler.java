package server;

import model.Game;
import model.GameServer;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * Created by YannL on 09/11/2016.
 */
public class ConnectionHandler implements Runnable {

    private Socket clientSocket;

    public ConnectionHandler(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    /**
     * TODO
     */
    @Override
    public void run() {
        try (BufferedInputStream in = new BufferedInputStream(clientSocket.getInputStream());
        BufferedOutputStream out = new BufferedOutputStream(clientSocket.getOutputStream())) {

            while(true) {
                System.out.println("ICI");
                byte[] msg = new byte[4096];
                int bytesRead = 0;
                int n;

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

                byte[] answer;
                if (bytesRead == 1) {
                    // Letter proposed
                    // TODO
                    StringBuilder sb = new StringBuilder();
                    sb.append(fromClient);
                    sb.append(' ');
                    sb.append(10);
                    answer = sb.toString().getBytes();
                } else if (fromClient.equals(Game.NEW_GAME)) {
                    // New game
                    answer = "-------- 10".getBytes();
                } else {
                    // Word proposition
                    // TODO
                    answer = "-------- 10".getBytes();
                }


                for (int i = 0; i < answer.length; i++) {
                    out.write(answer[i]);
                }
                out.flush();
            }
        } catch (IOException e) {
            System.err.println("Could not get I/O for client socket " + clientSocket.getInetAddress() + " : " + e);
        }
    }


}
