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
/**
 * The main class to start the server application.
 */
public class ServerMain {
    //method that creates
    /**
     * Main method to initialize and start the server components.
     *
     * @param args Command-line arguments:
     *             - If 1 argument: Use default port for SocketServer and default port for RMIServer.
     *             - If 2 arguments: Specify custom port for SocketServer and default port for RMIServer.
     *             - If 3 arguments: Specify custom port for SocketServer and custom port for RMIServer.
     * @throws AlreadyBoundException              If an RMI object is already bound to the registry.
     * @throws RuntimeException                  If a runtime exception occurs during server execution.
     * @throws Board.necessaryResourcesNotAvailableException If necessary resources for the board are not available.
     * @throws Board.notAvailableCellException    If a cell on the board is not available.
     * @throws IOException                       If an I/O error occurs.
     */
    public static void main(String[] args) throws AlreadyBoundException, RuntimeException, Board.necessaryResourcesNotAvailableException, Board.notAvailableCellException, IOException {
        //crea
        //TODO: PRENDI IN INPUT LA PORTA PER OGNI SERVER, SE NON SPECIFICATA METTI QUELLA DI DEFAULT

        GameController controller= new GameController();

        NetworkManager managerToModel= new NetworkManager(controller);
        ServerInterface engineRMI;

        SocketServer serverSocket;
        if(args.length==3){
            engineRMI = new RMIServer(managerToModel,Integer.parseInt(args[2]));
            serverSocket = new SocketServer(Integer.parseInt(args[1]), managerToModel);
        }
        else if(args.length==2) {
            serverSocket = new SocketServer(Integer.parseInt(args[1]), managerToModel);
            engineRMI= new RMIServer(managerToModel,9999);
        }
        else if(args.length==1) {
            serverSocket = new SocketServer(4567, managerToModel);
            engineRMI= new RMIServer(managerToModel,9999);
        } else {
            serverSocket = null;
            engineRMI = null;
        }
        for(int i=0;i< args.length;i++)
            System.out.println(args[i] + " ");

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
            } catch (InterruptedException | IOException e) {
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
    /**
     * Method to quit the server application by terminating the JVM.
     */
    public static void quitAll(){
        System.exit(0);
    }

}
