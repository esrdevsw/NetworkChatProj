package ie.atu.sw.client;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

/*
 * The FindPort class check the ports on localhost
 * and keep them in a list to present the possibilities found to the user
 *
 * @fileName ServerRunner.java
 * @author Edivagner Ribeiro / ID G00411275
 */

public class FindPort  {
    private String host = "localhost";

    private ArrayList<Integer> resultsPort = new ArrayList<>();

    public ArrayList<Integer> getResultsPort() {
        return resultsPort;
    }


    public void findChatServer(int startPort, int endPort) {
        if (!resultsPort.isEmpty()){
            resultsPort = new ArrayList<>();
        }

        for (int i = startPort; i < endPort; i++) {
            try {
                Socket s = new Socket(host, i);
                resultsPort.add(i);
                s.close();
                System.out.println(">> Server on port " + i);
            } catch (UnknownHostException ex) {
                System.err.println(ex);
                break;
            } catch (IOException ex) {
                // must not be a server on this port
            }
        }
        System.out.println("");
        System.out.println("=======================================================");
        System.out.println("[INFO] There is a server on port:");
        System.out.println(">> "+ resultsPort);
        System.out.println("=======================================================");

    }
}
