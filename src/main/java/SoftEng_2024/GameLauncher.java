package SoftEng_2024;

import SoftEng_2024.Model.Player_and_Board.Board;
import SoftEng_2024.Network.Main;
import SoftEng_2024.View.MainView;

import java.io.IOException;
import java.rmi.AlreadyBoundException;

public class GameLauncher {
        public static void main(String[] args) throws Board.necessaryResourcesNotAvailableException, AlreadyBoundException, Board.notAvailableCellException, IOException {
            if(args[0].equalsIgnoreCase("server")){
                Main.main(args);
            }else if(args[0].equalsIgnoreCase("client")){
//                String [] specifics = new String[3];
//                specifics[0] = args[1];
//                specifics[1] = args[2];
//                if(args.length==4){
//                    specifics[2] = args[3];
//                }
                MainView.main(args);
            }
        }

}
