package ie.atu.sw.server;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;

/*
 * The ConnectionsHandler class implements Runnable to
 * handle multiple client connections and exchange of messages via socket.
 * When a client enters, they receive the message to define a name.
 * The server sends the message to every other connection a new client has entered.
 *
 * Three functions are defined for communication:
 *
 * \chatConnect  --> returns the message that it is connected
 * \name --> the next entry will be the client new name
 * \q --> close the connection with the server
 *
 * When there are no more connections to the server, it will shut down.
 *
 * @fileName Server.java
 * @author Edivagner Ribeiro / ID G00411275
 *
 *
 */

public class ConnectionsHandler implements Runnable {

    private final Socket socketClient;
    private BufferedReader input;
    private BufferedWriter output;
    //private PrintWriter output;
    private String clientName;
    private static String message;
    private ArrayList<ConnectionsHandler> connections = Server.getConnections();


    public ConnectionsHandler(Socket socketClient) {
        this.socketClient = socketClient;
    }

    public static String getMessage() {
        return message;
    }

    @Override
    public void run() {
        try {
            InputStream inputStream = socketClient.getInputStream();
            OutputStream outputStream = socketClient.getOutputStream();

            input = new BufferedReader(new InputStreamReader(inputStream));
            output = new BufferedWriter(new OutputStreamWriter(outputStream));
            //output = new PrintWriter(outputStream, true); // autoFlush

            Date now = new Date();

            // ask client set a name
            output.write("SERVER: Please enter a name: ");
            output.newLine();
            output.flush();
            //output.println("SERVER: Please enter a name: ");
            clientName = input.readLine();

            System.out.println(now + " - Accepted connection from " + clientName);
            messageSendAll("SERVER: " + clientName + " joined the room.");

            clientMessage();

            outputStream.close();
            inputStream.close();

            System.out.println("Request processed: " + now + " " + clientName + " " + socketClient);


        } catch (IOException e) {
            //report exception somewhere.
            //e.printStackTrace();
            System.out.println("Connect failed from: " + socketClient);
            Server.removeConnection(this);
        }
    }

    private void clientMessage() throws IOException {
        while ((message = input.readLine()) != null) {
            if ((message.startsWith("\\chatConnect"))) {
                output.write("YES... Chat Connect!");
                output.newLine();
                output.flush();
            } else if (message.startsWith("\\name")) {
                String[] messageSplit = message.split("\s", 2);
                if (messageSplit.length == 2) {
                    messageSendAll(">> " + clientName + " rename to " + messageSplit[1]);
                    clientName = messageSplit[1];
                    output.write("SERVER: Successfully changed name to " + clientName);
                    output.newLine();
                    output.flush();
                    //output.println("SERVER: Successfully changed name to " + clientName);
                } else {
                    output.write("SERVER: No name provided.");
                    output.newLine();
                    output.flush();
                    //output.println("SERVER: No name provided.");
                }

            } else if (message.startsWith("\\q")) {
                messageSendAll("SERVER: " + clientName + " left the room.");
                shutdown();
                statusSocket();
                Server.removeConnection(this);
                break;
            } else {
                messageSendAll(clientName + ": " + message);
            }
        }
    }

    public void messageSend(String message) throws IOException {
        output.write(message);
        output.newLine();
        output.flush();
        // output.println(message);
    }

    public void messageSendAll(String message) throws IOException {
        for (ConnectionsHandler c : connections) {
            c.messageSend(message);
        }
    }

    public void shutdown() {
        try {
            input.close();
            output.close();
            if (!socketClient.isClosed()) {
                socketClient.close();
            }
        } catch (IOException e) {
            e.getStackTrace();
        }
    }

    // When there are no more connections to the server, it will shut down.
    public void statusSocket() {
        int numConnections = connections.size();
        int offline = 0;
        System.out.println("");
        System.out.println("number connections online: " + numConnections);
        for (ConnectionsHandler ch : connections) {
            boolean status = ch.socketClient.isClosed();
            System.out.println(" --> " + ch.socketClient + " --> online? " + !status);
            if (status) {
                offline++;
            }
        }
        System.out.println("");
        if (numConnections == offline) {
            System.out.println("");
            System.out.println("=======================================================");
            System.out.println("| No more active connections, shutting down the server |");
            System.out.println("=======================================================");
            System.out.println("");
            Server.shutdown();
        }
    }
}
