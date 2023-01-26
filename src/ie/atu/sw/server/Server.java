package ie.atu.sw.server;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/*
 * The Server class implements Runnable to start the server using a thread pool.
 * The server starts up and waits for socket connections on a specific
 *  port user-defined or with default port 13
 *
 * @fileName Server.java
 * @author Edivagner Ribeiro / ID G00411275
 *
 *
 */

public class Server implements Runnable {

    private static int PORT = 13;
    private static ServerSocket server;
    private static boolean serverShutdown;
    private static int numThreads = 50;
    private Socket socketClient = null;
    private BufferedWriter output;
    private static final ArrayList<ConnectionsHandler> connections = new ArrayList<>();
    private static final ExecutorService pool = Executors.newFixedThreadPool(numThreads);


    public static ArrayList<ConnectionsHandler> getConnections() {
        return connections;
    }

    public Server(int port) {
        PORT = port;
        serverShutdown = false;
    }

    public Server() {
        serverShutdown = false;
    }

    private void openServerSocket() {
        try {
            //server = new ServerSocket(PORT);
            server = new ServerSocket(PORT,1000, InetAddress.getByName("localhost"));

        } catch (IOException e) {
            throw new RuntimeException("Cannot open port " + PORT, e);
        }
    }

    private boolean serverShutdown() {
        return serverShutdown;
    }


    @Override
    public void run() {
        openServerSocket();
        System.out.println("Listening for connection on port " + PORT);

        while (!serverShutdown) {
            System.out.println("About to accept client connection...");
            System.out.println("");
            try {
                socketClient = server.accept();

                OutputStream outputStream = socketClient.getOutputStream();
                output = new BufferedWriter(new OutputStreamWriter(outputStream));

                System.out.println("Accept connection from " + socketClient);
                output.write("Chat Server Connected");
                output.newLine();
                output.flush();
                System.out.println("");
            } catch (SocketTimeoutException s) {
                    System.out.println("Server Socket timed out!");
                    break;
            } catch (IOException e) {
                if (serverShutdown()) {
                    System.out.println("Server Stopped.");
                    break;
                }
                throw new RuntimeException("Error accepting client connection", e);
            }
            ConnectionsHandler handler = new ConnectionsHandler(socketClient);
            connections.add(handler);
            pool.execute(handler);
        }
        pool.shutdown();
        System.out.println("[INFO] server off");
    }

    public static void shutdown(){
        try {
            serverShutdown = true;
            pool.shutdown();
            if (!server.isClosed()){
                server.close();
            }
            for (ConnectionsHandler c: connections) {
                c.shutdown();
            }
        } catch (IOException e){
            e.getStackTrace();
        }
    }

    public static void removeConnection(ConnectionsHandler connectionsHandler) {
        connections.remove(connectionsHandler);
    }

}

