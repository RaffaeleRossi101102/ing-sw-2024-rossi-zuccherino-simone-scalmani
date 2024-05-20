package SoftEng_2024.View;

import SoftEng_2024.Network.ToModel.ClientInterface;
import SoftEng_2024.Network.ToModel.ClientRMI;
import SoftEng_2024.Network.ToModel.ServerInterface;
import SoftEng_2024.Network.ToView.ClientObServerRMI;
import SoftEng_2024.Network.ToView.ObServerInterface;
import SoftEng_2024.Network.ToView.RMIObServer;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;

public class MainView {

    public static void main(String[] args) {
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
            try {
                //lookup of the to model registry
                Registry registry = LocateRegistry.getRegistry("localhost", 6969);
                ServerInterface server = (ServerInterface) registry.lookup("localhost");
                ClientInterface client= new ClientRMI(server, ID);

                //lookup of the to view registry
                Registry registry2 = LocateRegistry.getRegistry("localhost", 9696);
                RMIObServer server2 = (RMIObServer) registry2.lookup("localhost");
                ObServerInterface client2 = new ClientObServerRMI(server2);

            //running the chosen type of UI
                if(viewType.equals("CLI"))
                    new CliViewClient(ID,client).run();
                else if(viewType.equals("GUI"))
                    System.out.println("La gui nun ce sta");
            }catch(RemoteException | NotBoundException e){
                System.err.println("Something went wrong during the binding. Retry... ");
                e.printStackTrace();
            }
        } else if (connectionType.equals("Socket")) {
            System.out.println("Er Socket nun ce sta");
        }
    }
}
