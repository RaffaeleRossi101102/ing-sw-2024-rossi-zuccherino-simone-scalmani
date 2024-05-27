package SoftEng_2024.Network.ToView;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.concurrent.LinkedBlockingQueue;

import SoftEng_2024.Model.ModelMessages.*;
import SoftEng_2024.Network.ToModel.ServerInterface;
import SoftEng_2024.Network.ToModel.SocketServer;
import SoftEng_2024.Network.ToModel.SocketClientHandler;

public class ObServerManager {

    private LinkedBlockingQueue<ModelMessage> modelMessages;
    private ServerInterface serverRMI;
    private SocketServer socketServer;


    public ObServerManager(ServerInterface serverRMI, SocketServer socketServer){
        this.serverRMI = serverRMI;
        this.socketServer = socketServer;

    }


    public void run() throws IOException {
    ModelMessage msg;
        while(true){
            try {
                msg=modelMessages.take();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            serverRMI.addToClientQueue(msg);
            // TODO socketServer.addToClientQueue(msg);
        }
    }
}
