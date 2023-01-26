package ie.atu.sw.client;

import java.util.Scanner;

/*
 * The ClientMenu class creates a menu that provides the user with 4 different options:
 * (1) Search for servers on localhost
 * (2) Set port for localhost Chat Client
 * (3) Run Chat Client
 * (4) Quit
 *
 * @fileName ServerMenu.java
 *
 * @author Edivagner Ribeiro / ID G00411275
 *
 */

public class ClientMenu {

    private final Scanner s;
    private static boolean keepRunning;
    private static int PORT = 13;
    private FindPort fp = new FindPort();

    public static void setKeepRunning(boolean keepRunning) {
        ClientMenu.keepRunning = keepRunning;
    }


    public ClientMenu() {
        s = new Scanner(System.in);
        keepRunning = true;
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
                    System.out.println("");
                    fp.findChatServer(startPort, endPort);

                } else if (choice == 2) {// (2) Set port for localhost server
                    if ((fp.getResultsPort().size()) == 0) {
                        System.out.println("=======================================================");
                        System.out.println("[INFO] You can use option (1) Search for servers ");
                        System.out.println("=======================================================");
                        System.out.println("");
                    } else {
                        System.out.println("=======================================================");
                        System.out.println("[INFO] There is a server on port:");
                        System.out.println(fp.getResultsPort());
                        System.out.println("=======================================================");
                        System.out.println("");
                    }


                    System.out.println("");
                    System.out.println("X~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~X");
                    System.out.println("X Make sure it is on the same port as the Chat Server. X");
                    System.out.println("X~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~X");
                    System.out.println("");
                    System.out.println("Please enter the PORT number");
                    PORT = Integer.parseInt(s.next()); // Waits on Input from User

                } else if (choice == 3) {// (3) Run Chat Client
                    System.out.println("");
                    System.out.println("=======================================================");
                    System.out.println("|           Starting... Waiting for connection         |");
                    System.out.println("=======================================================");
                    System.out.println("");

                    new ClientThread(PORT).run();

                    // keepRunning = false;

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
        System.out.println("*                       Chat Client                        *");
        System.out.println("*                                                          *");
        System.out.println("************************************************************");
        System.out.println("(1) Search for servers on localhost");
        System.out.println("(2) Set port for localhost Chat Client");
        System.out.println("(3) Run Chat Client");
        System.out.println("(4) Quit");
        System.out.println("");
        System.out.print("Select Option [1-4]> ");

    }
}
