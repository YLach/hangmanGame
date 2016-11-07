/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author YannL
 */
public class HangmanClient {

    // Help message to use the client
    static final String USAGE = "java HangmanClient [serverIP] [port]";

    // Timeout (in ms)
    static final int TIMEOUT = 1000;
    
    

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // IP adress of the server
        String ipAddress = "localhost";
        // Port number of the server
        int port = 80;

        // Parse input arguments
        if (args.length > 0) {
            ipAddress = args[0];
        }

        // Display help if asked
        if (ipAddress.equalsIgnoreCase("-h") || ipAddress.equalsIgnoreCase("-help")) {
            System.out.println(USAGE);
            System.exit(1);
        }

        if (args.length > 1) {
            try {
                port = Integer.parseInt(args[1]);
            } catch (NumberFormatException e) {
                System.err.println(USAGE);
                System.exit(1);
            }
        }

        System.out.println("IP = " + ipAddress + " Port = " + port);
        
        // Parse input arguments
        if (args.length > 1) {
            try (Socket socket = new Socket(InetAddress.getByName(ipAddress), port)) {
                // Set socket's attributes
                socket.setSoTimeout(TIMEOUT);

            } catch (UnknownHostException ex) {
                System.err.println("Unknown host : " + ex);
                System.exit(1);
            } catch (IOException ex) {
                System.err.println("Error while creating the client socket : " + ex);
                System.exit(1);
            }
        }

    }

}
