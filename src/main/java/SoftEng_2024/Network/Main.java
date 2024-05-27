package SoftEng_2024.Network;


import SoftEng_2024.Controller.GameController;
import SoftEng_2024.Model.Player_and_Board.Board;
import SoftEng_2024.Network.ToModel.NetworkManager;
import SoftEng_2024.Network.ToModel.RMIServer;
import SoftEng_2024.Network.ToModel.ServerInterface;
import SoftEng_2024.Network.ToModel.SocketServer;
import SoftEng_2024.Network.ToView.ObServerManager;


import java.io.IOException;
import java.rmi.AlreadyBoundException;

public class Main {
    //method that creates
    public static void main(String[] args) throws AlreadyBoundException, RuntimeException, Board.necessaryResourcesNotAvailableException, Board.notAvailableCellException, IOException {
        //crea
        GameController controller= new GameController();

        NetworkManager managerToModel= new NetworkManager(controller);
        managerToModel.setRunning(true);

        ServerInterface engineRMI = new RMIServer(managerToModel);


        SocketServer serverSocket = new SocketServer(4567, managerToModel); //Attenzione alla porta da inserire

        //ObServerManager managerToView = new ObServerManager(engineRMI, serverSocket);

        controller.setServerRMI(engineRMI);
        controller.setServerSocket(serverSocket);






        engineRMI.run();
        serverSocket.startServer();
        managerToModel.run();
        //managerToView.run();

        //SocketObServer obServerSocket = new SocketObServer();

    }
}
