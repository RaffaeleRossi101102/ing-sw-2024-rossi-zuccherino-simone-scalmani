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
import java.rmi.RemoteException;

public class Main {
    //method that creates
    public static void main(String[] args) throws AlreadyBoundException, RuntimeException, Board.necessaryResourcesNotAvailableException, Board.notAvailableCellException, IOException {
        //crea
        GameController controller= new GameController();

        NetworkManager managerToModel= new NetworkManager(controller);
        managerToModel.setRunning(true);

        ServerInterface engineRMI = new RMIServer(managerToModel);


        SocketServer serverSocket = new SocketServer(4567, managerToModel); //Attenzione alla porta da inserire

        ObServerManager managerToView = new ObServerManager(engineRMI, serverSocket);

        controller.setServerRMI(engineRMI);
        controller.setServerSocket(serverSocket);
        controller.setToViewManager(managerToView);






        Thread RMIThread = new Thread(() -> {
            try {
                engineRMI.run();
            } catch (RemoteException | AlreadyBoundException e) {
                throw new RuntimeException(e);
            }
        });
        Thread socketThread = new Thread(() -> {
            try {
                serverSocket.startServer(serverSocket);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        Thread managerToModelThread = new Thread(() -> {
            try {
                managerToModel.run();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
        Thread managerToViewThread = new Thread(() -> {
            try {
                managerToView.run();
            } catch (IOException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        });

        RMIThread.start();
        socketThread.start();
        managerToModelThread.start();
        managerToViewThread.start();



    }
}
