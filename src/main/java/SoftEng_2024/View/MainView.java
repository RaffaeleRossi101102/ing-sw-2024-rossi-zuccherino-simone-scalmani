package SoftEng_2024.View;

import SoftEng_2024.Network.ToModel.ClientInterface;
import SoftEng_2024.Network.ToModel.ClientRMI;
import SoftEng_2024.Network.ToModel.SocketClient;
import javafx.application.Application;

import java.rmi.RemoteException;
import java.util.Scanner;

public class MainView {


    public static void main(String[] args) throws RemoteException {
        String ip="localhost";
        String socketPort="4567";
        String rmiPort="9999";
        if(args.length%2==0){
            System.out.println("Wrong command line arguments. If you want to set the server ip digit <--ip 'ipAddress'>. If the servers weren't started" +
                    " on the default ports make sure you've set them. Digit <--s 'port'> to set the socket port and/or <--rmi 'port'> to set the rmi port." +
                    "If you don't set the ports the corresponding server will start on the default port.");
            return;
        }
        for(int i=1; i< args.length;i+=2){
            if(args[i].equals("--ip"))
                ip=args[i+1];
            else if (args[i].equals("--rmi")) {
                rmiPort=args[i+1];
            } else if (args[i].equals("--s")) {
                socketPort=args[i+1];
            }
            else{
                System.out.println("Wrong command line arguments. If you want to set the server ip digit <--ip 'ipAddress'>. If the servers weren't started" +
                        " on the default ports make sure you've set them. Digit <--s 'port'> to set the socket port and/or <--rmi 'port'> to set the rmi port." +
                        "If you don't set the ports the corresponding server will start on the default port.");
            }
        }
        System.out.println("The selected ip address for the server is: "+ip+"\nThe selected socket port is: "+socketPort+
                "\nThe selected RMI port is: "+rmiPort);

        System.out.println("\n" +
                " ██████╗ ██████╗ ██████╗ ███████╗██╗  ██╗    ███╗   ██╗ █████╗ ████████╗██╗   ██╗██████╗  █████╗ ██╗     ██╗███████╗\n" +
                "██╔════╝██╔═══██╗██╔══██╗██╔════╝╚██╗██╔╝    ████╗  ██║██╔══██╗╚══██╔══╝██║   ██║██╔══██╗██╔══██╗██║     ██║██╔════╝\n" +
                "██║     ██║   ██║██║  ██║█████╗   ╚███╔╝     ██╔██╗ ██║███████║   ██║   ██║   ██║██████╔╝███████║██║     ██║███████╗\n" +
                "██║     ██║   ██║██║  ██║██╔══╝   ██╔██╗     ██║╚██╗██║██╔══██║   ██║   ██║   ██║██╔══██╗██╔══██║██║     ██║╚════██║\n" +
                "╚██████╗╚██████╔╝██████╔╝███████╗██╔╝ ██╗    ██║ ╚████║██║  ██║   ██║   ╚██████╔╝██║  ██║██║  ██║███████╗██║███████║\n" +
                " ╚═════╝ ╚═════╝ ╚═════╝ ╚══════╝╚═╝  ╚═╝    ╚═╝  ╚═══╝╚═╝  ╚═╝   ╚═╝    ╚═════╝ ╚═╝  ╚═╝╚═╝  ╚═╝╚══════╝╚═╝╚══════╝\n");
        //creating a random ID based on currentTimeMillis() method
        double ID = System.currentTimeMillis();
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
        if(connectionType.equals("rmi")){
            try {
                 client = new ClientRMI(ID,ip,Integer.parseInt(rmiPort));
            } catch (RemoteException e) {
                throw new RuntimeException("Something went wrong while trying to create the client...");
            }
        } else {
               client=new SocketClient(ip,Integer.parseInt(socketPort),ID);
        }

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
