package ie.atu.sw.server;

/*
 * The application is run using the ServerRunner Class.
 * It accomplishes this by starting the ServerMenu Class.
 * That class is used to show a menu and call all the
 * classes required to run and configure the Server.
 *
 * @fileName ServerRunner.java
 * @author Edivagner Ribeiro / ID G00411275
 */

public class ServerRunner {
    public static void main(String[] args) {
        ServerMenu sm = new ServerMenu();
        sm.start();
    }
}
