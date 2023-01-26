package ie.atu.sw.client;

import java.io.*;
import java.net.Socket;

/*
 * The ClientThread class implements Runnable to start the ChatClient using a thread.
 * The ChatClient starts up and waits for setting a name
 * Client starts with user defined port or default port 13
 * After starting, multiple clients can exchange messages with each other.
 *
 * @fileName ClientThread.java
 * @author Edivagner Ribeiro / ID G00411275
 *
 *
 */

public class ClientThread implements Runnable {
    private String host = "localhost";
    private static int PORT = 13;
    private Socket client;
    private BufferedReader input;
    private BufferedWriter output;
    private boolean clientOFF;


    public ClientThread(int port) {
        PORT = port;
        clientOFF = false;
    }

    public ClientThread() {
        clientOFF = false;
    }

    @Override
    public void run() {
        try {
            client = new Socket(host, PORT);
            System.out.println("Connected to Server on host " + host + ":" + PORT);
            //client.setSoTimeout(15000);

            InputStream inputStream = client.getInputStream();
            OutputStream outputStream = client.getOutputStream();

            output = new BufferedWriter(new OutputStreamWriter(outputStream));
            input = new BufferedReader(new InputStreamReader(inputStream));

            InputHandler in = new InputHandler();
            Thread t = new Thread(in);

            t.start();

            String clientMessage;
            while ((clientMessage = input.readLine()) != null) {
                System.out.println(clientMessage);
            }

        } catch (IOException e) {
            shutClient();
            ClientMenu.setKeepRunning(false);
            System.out.println("[INFO] Disconnected... Do not possible to access the server.");
        }
    }

    private void shutClient() {
        clientOFF = true;
        try {
            if (client != null) {
                client.shutdownInput();
                client.shutdownOutput();
                client.close();
            }
        } catch (IOException e) {
            e.getStackTrace();
        }
    }


    private class InputHandler implements Runnable {
        @Override
        public void run() {
                BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
                do{
                    try {
                        String message = br.readLine();
                        if (message.equals("\\q")) {
                            output.write(message);
                            output.newLine();
                            output.flush();
                            br.close();
                            shutClient();
                            ClientMenu.setKeepRunning(false);
                        } else {
                            output.write(message);
                            output.newLine();
                            output.flush();
                        }
                    } catch (IOException e) {
                        //ClientMenu.setKeepRunning(false);
                        shutClient();
                        System.out.println("[INFO] Sorry... Runnable Stopped.");
                    }

            } while (!clientOFF) ;
        }
    }
}
