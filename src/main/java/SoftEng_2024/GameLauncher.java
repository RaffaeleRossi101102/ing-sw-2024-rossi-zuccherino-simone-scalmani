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
        }

}
