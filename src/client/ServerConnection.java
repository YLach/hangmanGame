package client;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.stream.Stream;


/**
 * Created by YannL on 08/11/2016.
 */
public class ServerConnection implements Runnable {

    // Timeout (in ms)
    static final int TIMEOUT = 1000;

    // IP adress of the server
    private String ipAddress = "localhost";

    // Port number of the server
    private int port = 80;

    // Streams
    private BufferedInputStream in;
    private BufferedOutputStream out;

    // Socket
    private Socket socket;

    // ConnectionState
    private boolean connected = false;

    /**
     * Constructor TODO
     * @param ipAddress
     * @param port
     */
    public ServerConnection(String ipAddress, int port) {
        this.ipAddress = ipAddress;
        this.port = port;
        connection();
    }

    private void connection() {
        try {
            socket = new Socket(InetAddress.getByName(ipAddress), port);
            // Set socket's attributes
            socket.setSoTimeout(TIMEOUT);

            // Streams initialization
            in = new BufferedInputStream(socket.getInputStream());
            out = new BufferedOutputStream(socket.getOutputStream());
            connected = true;

        } catch (UnknownHostException ex) {
            System.err.println("Unknown host : " + ex);
        } catch (IOException ex) {
            System.err.println("Error while creating the client socket : " + ex);
        }
    }

    /**
     * TODO
     */
    public void deconnection() {
        if (isConnected() && !socket.isClosed()) {
            try {
                // Close streams and socket
                in.close();
                out.close();
                socket.close();
            } catch (IOException e) {
                System.err.println("Error while closing the client socket : " + e);
                System.exit(1);
            }
        }
    }

    /**
     * TODO
     * @param stringToSend
     * @return
     * @throws IOException
     */
    public String callServer(String stringToSend) throws IOException {
        byte[] msg = stringToSend.getBytes();
        out.write(msg, 0, msg.length);
        out.flush();


        /*
        StreamTokenizer st = new StreamTokenizer(new BufferedReader(new InputStreamReader(socket.getInputStream())));
        StringBuilder sb = new StringBuilder();
        boolean first = true;
        while (st.nextToken() != StreamTokenizer.TT_EOF) {
            if (first)
                first = false;
            else
                sb.append(' ');
            sb.append(st.sval);
        }

        return sb.toString();*/
        byte[] fromServer = new byte[4096];
        int length = in.read(fromServer, 0, fromServer.length);
        return new String(fromServer, 0, length);
    }

    /**
     * TODO
     * @return
     */
    public boolean isConnected() {
        return connected;
    }


    /**
     * TODO
     */
    @Override
    public void run() {
        //Multithreaded version
        try (Socket socket = new Socket(InetAddress.getByName(ipAddress), port)) {
            // Set socket's attributes
            socket.setSoTimeout(TIMEOUT);

            in = new BufferedInputStream(socket.getInputStream());
            out = new BufferedOutputStream(socket.getOutputStream());


        } catch (UnknownHostException ex) {
            System.err.println("Unknown host : " + ex);
            System.exit(1);
        } catch (IOException ex) {
            System.err.println("Error while creating the client socket : " + ex);
            System.exit(1);
        }
    }
}
