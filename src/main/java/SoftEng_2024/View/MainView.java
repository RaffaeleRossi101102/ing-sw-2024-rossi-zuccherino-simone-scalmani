package SoftEng_2024.View;

import SoftEng_2024.Network.ToModel.ClientInterface;
import SoftEng_2024.Network.ToModel.ClientRMI;
import SoftEng_2024.Network.ToModel.SocketClient;
import javafx.application.Application;

import java.rmi.RemoteException;
import java.util.Scanner;
/**
 * The main entry point for the Codex Naturalis game application. This class
 * allows users to choose the type of connection (RMI or Socket) and the type
 * of view (CLI or GUI) they want to use. It initializes the appropriate client
 * based on user input and starts the corresponding view.
 */
public class MainView {

    /**
     * Main method that initializes and starts the Codex Naturalis game application.
     *
     * @param args Command-line arguments:
     *             args[0] - Unused.
     *             args[1] - IP address for the connection.
     *             args[2] - Optional: port number for the connection.
     * @throws RemoteException If an error occurs during RMI connection setup.
     */
    public static void main(String[] args) throws RemoteException {
        System.out.println("\n" +
                " ██████╗ ██████╗ ██████╗ ███████╗██╗  ██╗    ███╗   ██╗ █████╗ ████████╗██╗   ██╗██████╗  █████╗ ██╗     ██╗███████╗\n" +
                "██╔════╝██╔═══██╗██╔══██╗██╔════╝╚██╗██╔╝    ████╗  ██║██╔══██╗╚══██╔══╝██║   ██║██╔══██╗██╔══██╗██║     ██║██╔════╝\n" +
                "██║     ██║   ██║██║  ██║█████╗   ╚███╔╝     ██╔██╗ ██║███████║   ██║   ██║   ██║██████╔╝███████║██║     ██║███████╗\n" +
                "██║     ██║   ██║██║  ██║██╔══╝   ██╔██╗     ██║╚██╗██║██╔══██║   ██║   ██║   ██║██╔══██╗██╔══██║██║     ██║╚════██║\n" +
                "╚██████╗╚██████╔╝██████╔╝███████╗██╔╝ ██╗    ██║ ╚████║██║  ██║   ██║   ╚██████╔╝██║  ██║██║  ██║███████╗██║███████║\n" +
                " ╚═════╝ ╚═════╝ ╚═════╝ ╚══════╝╚═╝  ╚═╝    ╚═╝  ╚═══╝╚═╝  ╚═╝   ╚═╝    ╚═════╝ ╚═╝  ╚═╝╚═╝  ╚═╝╚══════╝╚═╝╚══════╝\n");
        //creating a random ID based on currentTimeMillis() method
        double ID = System.currentTimeMillis();
        String ip;
        int port = 0;
        System.out.println("Welcome to CODEX NATURALIS!");
        //Ask user which type of connection he wants to use
        System.out.println("Choose your type of connection, type: 'RMI' or 'Socket' ");
        Scanner scan = new Scanner(System.in);
        String connectionType;
        connectionType= scan.nextLine().trim().toLowerCase().replaceAll("\\s+", "");
        while(!connectionType.equals("rmi") && !connectionType.equals("socket")){
            System.err.println("Wrong input! Type: 'RMI' or 'Socket' ");
            connectionType= scan.nextLine().trim().toLowerCase();
        }
        System.out.println("Got it :)");

        //Ask user which type of view he wants to use
        String viewType;
        System.out.println("Choose your type of view, type CLI or GUI");
        viewType= scan.nextLine().trim().toLowerCase().replaceAll("\\s+", "");
        while(!viewType.equals("cli") && !viewType.equals("gui")){
            System.err.println("Wrong input! Type: 'CLI' or 'GUI' ");
            viewType= scan.nextLine().trim().toLowerCase();
        }
        System.out.println("Got it :)");


        ClientInterface client;
        ip=args[1];

        if(connectionType.equals("rmi")){
            try {
                if(args.length==3){
                    port=Integer.parseInt(args[2]);
                }
                else
                    port=9999;
                 client = new ClientRMI(ID,ip,port);
            } catch (RemoteException e) {
                throw new RuntimeException("Something went wrong while trying to create the client...");
            }
        } else {
            if(args.length==3){
               client=new SocketClient(ip,Integer.parseInt(args[2]),ID);
            }
            else
                client=new SocketClient(ip,4567,ID);
        }
        System.out.println(ip+port);
        if(viewType.equals("cli")) {
            CliViewClient cliView = new CliViewClient(ID, client);
            client.setView(cliView);
            cliView.run();
        }
        else {
            GUIMain guiView = new GUIMain();
            client.setView(guiView);
            GUIMain.setID(ID);
            GUIMain.setClient(client);
            GUIMain.setLocalModel(new LocalModel());
            guiView.run();
        }
    }


}
