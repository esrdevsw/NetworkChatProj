package ie.atu.sw.client;

/*
 * The application is run using the ClientRunner Class.
 * It accomplishes this by starting the ClientRunner Class.
 * That class is used to show a menu and call all the
 * classes required to run and configure the Client Socket on chat server.
 *
 * @fileName ServerRunner.java
 * @author Edivagner Ribeiro / ID G00411275
 */

public class ClientRunner {
    public static void main(String[] args) {
        ClientMenu cm = new ClientMenu();
        cm.start();
    }
}
