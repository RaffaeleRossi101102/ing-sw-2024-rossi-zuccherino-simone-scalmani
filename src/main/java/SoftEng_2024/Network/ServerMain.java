package SoftEng_2024.Network;


import SoftEng_2024.Controller.GameController;
import SoftEng_2024.Model.Player_and_Board.Board;
import SoftEng_2024.Network.ToModel.NetworkManager;
import SoftEng_2024.Network.ToModel.RMIServer;
import SoftEng_2024.Network.ToModel.ServerInterface;
import SoftEng_2024.Network.ToModel.SocketServer;
import SoftEng_2024.Network.ToView.ObServerManager;


import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
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

        GameController controller= new GameController();

        NetworkManager managerToModel= new NetworkManager(controller);
        ServerInterface engineRMI = null;

        SocketServer serverSocket = null;
        if(args.length%2==0) {
            System.out.println("Please insert both which server you want to set the port on and the port. Digit <--s port>, <--rmi port> or both. If nothing is set" +
                    " the server will start on the default ports");
            return;
        }
        if(args.length==3 ){
            if( args[1].equals("--s")) {
                engineRMI = new RMIServer(managerToModel, 53784);
                serverSocket = new SocketServer(Integer.parseInt(args[2]), managerToModel);

            }
            else if(args[1].equals("--rmi")){
                engineRMI = new RMIServer(managerToModel, Integer.parseInt(args[2]));
                serverSocket = new SocketServer(59697, managerToModel);

            }
            else {
                System.out.println("Wrong command line argument.Digit <--s port>, <--rmi port> or both. If nothing is set" +
                        " the server will start on the default ports");
                return;
            }
        }
        else if(args.length==5) {
            if(args[1].equals("--s") && args[3].equals("--rmi")) {
                serverSocket = new SocketServer(Integer.parseInt(args[2]), managerToModel);
                engineRMI = new RMIServer(managerToModel, Integer.parseInt(args[4]));

            } else if (args[1].equals("--rmi") && args[3].equals("--s")) {
                serverSocket = new SocketServer(Integer.parseInt(args[4]), managerToModel);
                engineRMI = new RMIServer(managerToModel, Integer.parseInt(args[2]));

            }
            else{
                System.out.println("Wrong command line argument.Digit <--s port>, <--rmi port> or both. If nothing is set"+
                        " the server will start on the default ports");
                return;
            }
        }
        else if(args.length==1) {
            serverSocket = new SocketServer(59697, managerToModel);
            engineRMI = new RMIServer(managerToModel, 53784);

        }

        ObServerManager managerToView = new ObServerManager(engineRMI, serverSocket);

        controller.setServerRMI(engineRMI);
        controller.setServerSocket(serverSocket);
        controller.setToViewManager(managerToView);

        ServerInterface finalEngineRMI = engineRMI;
        Thread RMIThread = new Thread(() -> {
            try {
                finalEngineRMI.run();
            } catch (RemoteException | AlreadyBoundException e) {
                throw new RuntimeException(e);
            }
        });
        SocketServer finalServerSocket = serverSocket;
        Thread socketThread = new Thread(() -> {
            try {
                finalServerSocket.startServer(finalServerSocket);
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
