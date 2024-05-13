package SoftEng_2024.Network;


import SoftEng_2024.Controller.GameController;
import SoftEng_2024.Model.Board;
import SoftEng_2024.Network.ToModel.NetworkManager;
import SoftEng_2024.Network.ToModel.RMIServer;
import SoftEng_2024.Network.ToModel.ServerInterface;


import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;

public class Main {
    //method that creates
    public static void main(String[] args) throws AlreadyBoundException, RemoteException, RuntimeException, Board.necessaryResourcesNotAvailableException, Board.notAvailableCellException {
        //crea
        GameController controller= new GameController();
        NetworkManager manager= new NetworkManager(controller);
        manager.setRunning(true);
        ServerInterface engineRMI = new RMIServer(manager);

        engineRMI.run();
        manager.run();

        System.out.println("sono dopo");
        //ServerSocket serverSocket=new ServerSocket(controller);
    }
}
