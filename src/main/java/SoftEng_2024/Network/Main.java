package SoftEng_2024.Network;


import SoftEng_2024.Controller.GameInit;
import SoftEng_2024.Model.Board;


import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.util.List;

public class Main {
    ServerInterface serverRMI;

    public static void main(String[] args) throws AlreadyBoundException, RemoteException, RuntimeException, Board.necessaryResourcesNotAvailableException, Board.notAvailableCellException {
        GameInit controller= new GameInit();
        ServerInterface engineRMI = new RMIServer(controller);
        //inizializza socketServer
        try {
            engineRMI.run();
            controller.run(engineRMI);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println("sono dopo");
        //ServerSocket serverSocket=new ServerSocket(controller);
    }
}
