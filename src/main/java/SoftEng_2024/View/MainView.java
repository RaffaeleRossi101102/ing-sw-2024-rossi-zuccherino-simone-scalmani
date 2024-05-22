package SoftEng_2024.View;

import SoftEng_2024.Network.ToModel.ClientInterface;
import SoftEng_2024.Network.ToModel.ClientRMI;
import SoftEng_2024.Network.ToModel.SocketClient;
import java.rmi.RemoteException;
import java.util.Scanner;

public class MainView {

    public static void main(String[] args) throws RemoteException {
        //creating a random ID based on currentTimeMillis() method
        double ID = System.currentTimeMillis();
        System.out.println("Welcome to CODEX NATURALIS!");
        //Ask user which type of connection he wants to use
        System.out.println("Choose your type of connection, type: 'RMI' or 'Socket' ");
        Scanner scan= new Scanner(System.in);
        String connectionType;
        connectionType= scan.nextLine();
        while(!connectionType.equals("RMI") && !connectionType.equals("Socket")){
            System.err.println("Wrong input! Type: 'RMI' or 'Socket' ");
            connectionType= scan.nextLine();
        }
        System.out.println("Got it :)");

        //Ask user which type of view he wants to use
        String viewType;
        System.out.println("Choose your type of view, type CLI or GUI");
        viewType= scan.nextLine();
        while(!viewType.equals("CLI") && !viewType.equals("GUI")){
            System.err.println("Wrong input! Type: 'CLI' or 'GUI' ");
            viewType= scan.nextLine();
        }
        System.out.println("Got it :)");
        //creating an RMI connection
        if(connectionType.equals("RMI")){
            ClientInterface client;
            try {
                 client = new ClientRMI(ID);
            } catch (RemoteException e) {
                throw new RuntimeException("Something went wrong while trying to create the client...");
            }

            if(viewType.equals("CLI")) {
                CliViewClient cliView = new CliViewClient(ID, client);
                client.setView(cliView);
                cliView.run();
            }
            else {
                System.out.println("La gui nun ce sta per ora, paolo incoming :)");
            }

        } else {

            ClientInterface socketClient = new SocketClient("localhost",4567, ID);

            if(viewType.equals("CLI")) {

                new CliViewClient(ID, socketClient).run();
            } else {

                System.out.println("La gui nun ce sta per ora, paolo incoming :)");
            }

        }
    }
}
