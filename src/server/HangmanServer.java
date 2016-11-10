/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author YannL
 */
public class HangmanServer {
    // Help message to use the server
    static final String USAGE = "java HangmanServer [port]";

    // File containing
    static final String DICTIONARY_FILE = "res/words.txt";

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // Port number of the ServerSocket
        int port = 8080;

        // Parse input arguments
        if (args.length > 0) {
            // Display help if asked
            if (args[0].equalsIgnoreCase("-h") || args[0].equalsIgnoreCase("-help")) {
                System.out.println(USAGE);
                System.exit(1);
            }

            try {
                port = Integer.parseInt(args[0]);
            } catch (NumberFormatException e) {
                System.err.println(USAGE);
                System.exit(1);
            }
        }

        // Create dictionary of words
        List<String> dictionary = new ArrayList<>();
        try {
            FileReader fr = new FileReader(DICTIONARY_FILE);
            BufferedReader br = new BufferedReader(fr);
            String line;
            try {
                while ((line = br.readLine()) != null) {
                    dictionary.add(line.toUpperCase());
                }
                br.close();
            } catch (IOException e) {
                System.err.println("Can't read from file " + DICTIONARY_FILE + " : " + e);
                System.exit(1);
            }
        } catch (FileNotFoundException e) {
            System.err.println("Can't create the dictionary of words : " + e);
            System.exit(1);
        }


        // Creation of a listening socket bound to the specific port
        try (ServerSocket serverSocket = new ServerSocket(port)) {

            while (true) {
                try {
                    // Wait for a client connection request
                    Socket clientSocket = serverSocket.accept();
                    // Communicate with a client via clientSocket
                    new Thread(new ConnectionHandler(clientSocket, dictionary)).start();

                    // Close the socket and wait for another connection
                    //clientSocket.close();

                } catch (SocketException e) {
                    System.err.println(e);
                }
            }

        } catch (IOException ex) {
            System.err.println("Error while creating the server listening socket on port " + port + " : " + ex);
            System.exit(1);
        }
    }




}
