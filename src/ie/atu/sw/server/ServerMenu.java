package ie.atu.sw.server;

import ie.atu.sw.client.FindPort;

import java.util.Scanner;

/*
 * The ServerMenu class creates a menu that provides the user with 4 different options:
 * (1) Search for servers on localhost
 * (2) Set port for localhost server
 * (3) Run Server
 * (4) Quit
 *
 * @fileName ServerMenu.java
 *
 * @author Edivagner Ribeiro / ID G00411275
 *
 */

public class ServerMenu {

    private final Scanner s;
    private boolean keepRunning = true;
    private static int PORT = 13;

    public ServerMenu() {
        s = new Scanner(System.in);
    }

    public void start() {
        do {
            showOptions();
            try {
                int choice = Integer.parseInt(s.next()); // Waits on Input from User

                if (choice == 1) {// (1) Search for servers on localhost
                    System.out.println("Search for servers on localhost");
                    System.out.print("Start Port: ");
                    int startPort = Integer.parseInt(s.next()); // Waits on Input from User
                    System.out.print("end Port: ");
                    int endPort = Integer.parseInt(s.next()); // Waits on Input from User

                    new FindPort().findChatServer(startPort, endPort);

                } else if (choice == 2) {// (2) Set port for localhost server

                    System.out.println("Please enter the PORT number");
                    PORT = Integer.parseInt(s.next()); // Waits on Input from User

                } else if (choice == 3) {// (3) Run Server
                    System.out.println("");
                    System.out.println("=======================================================");
                    System.out.println("|       Starting Server... Waiting for connection     |");
                    System.out.println("=======================================================");
                    System.out.println("");
                    Server server = new Server(PORT);
                    new Thread(server).start();

                    keepRunning = false;

                } else if (choice == 4) {// (4) Quit
                    System.out.println("[INFO] Shutting down...please wait...");
                    keepRunning = false;
                } else {
                    System.out.println("");
                    System.out.println("[ERROR] Invalid input.");
                }
            } catch (NumberFormatException nfe) {
                System.out.println("");
                System.out.println("[ERROR] Invalid input.");
                System.out.println(nfe.getMessage());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } while (keepRunning);
    }

    /*
     * display the options menu
     */
    private void showOptions() {
        System.out.println("");
        System.out.println("");
        System.out.println("************************************************************");
        System.out.println("*       ATU - Dept. Computer Science & Applied Physics     *");
        System.out.println("*                                                          *");
        System.out.println("*               Network Programming Project                *");
        System.out.println("*                                                          *");
        System.out.println("*                       Chat Server                        *");
        System.out.println("*                                                          *");
        System.out.println("************************************************************");
        System.out.println("(1) Search for servers on localhost");
        System.out.println("(2) Set port for localhost server");
        System.out.println("(3) Run Server");
        System.out.println("(4) Quit");
        System.out.println("");
        System.out.print("Select Option [1-4]> ");

    }
}
