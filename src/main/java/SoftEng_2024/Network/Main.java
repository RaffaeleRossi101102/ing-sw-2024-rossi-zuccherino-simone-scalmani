package SoftEng_2024.Network;


import SoftEng_2024.Controller.GameController;
import SoftEng_2024.Model.Player_and_Board.Board;
import SoftEng_2024.Network.ToModel.NetworkManager;
import SoftEng_2024.Network.ToModel.RMIServer;
import SoftEng_2024.Network.ToModel.ServerInterface;
import SoftEng_2024.Network.ToView.ObServerManager;
import SoftEng_2024.Network.ToView.RMIObServer;
import SoftEng_2024.Network.ToView.SocketObServer;


import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;

public class Main {
    //method that creates
    public static void main(String[] args) throws AlreadyBoundException, RemoteException, RuntimeException, Board.necessaryResourcesNotAvailableException, Board.notAvailableCellException {
        //crea
        GameController controller= new GameController();
        NetworkManager managerToModel= new NetworkManager(controller);
        managerToModel.setRunning(true);
        ServerInterface engineRMI = new RMIServer(managerToModel);
        RMIObServer obServerRMI = new RMIObServer();
        ObServerManager managerToView = new ObServerManager();




        engineRMI.run();
        managerToModel.run();
        managerToView.run();
        obServerRMI.run();

        //SocketObServer obServerSocket = new SocketObServer();
        //ServerSocket serverSocket=new ServerSocket(controller);
    }
}
