package SoftEng_2024;

import SoftEng_2024.Model.Player_and_Board.Board;
import SoftEng_2024.Network.ServerMain;
import SoftEng_2024.View.MainView;

import java.io.IOException;
import java.rmi.AlreadyBoundException;

public class GameLauncher {
        public static void main(String[] args) throws Board.necessaryResourcesNotAvailableException, AlreadyBoundException, Board.notAvailableCellException, IOException {
            if(args[0].equalsIgnoreCase("server")){
                ServerMain.main(args);
            }else if(args[0].equalsIgnoreCase("client")){
                MainView.main(args);
            }
            else
                System.out.println("Invalid command line arguments. Insert 'server' or 'client'.\n" +
                        "After 'server' you can choose the ports to start the server on. Insert <--s port> <--rmi port>, otherwise default ports will be set.\n" +
                        "After 'client' you can insert the ip address for the server and the server ports. " +
                        "Insert <ipAddress> <--s port> <--rmi port>. Make sure the ports are the same as the server ports.\n  ");
        }

}
