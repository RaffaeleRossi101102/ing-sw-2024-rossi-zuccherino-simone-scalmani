package SoftEng_2024.Network.ToView;

import java.io.IOException;
import java.util.concurrent.LinkedBlockingQueue;
import SoftEng_2024.Model.ModelMessages.*;
import SoftEng_2024.Network.ToModel.ServerInterface;
import SoftEng_2024.Network.ToModel.SocketServer;

public class ObServerManager {

    private LinkedBlockingQueue<ModelMessage> modelMessages;
    private ServerInterface serverRMI;
    private SocketServer socketServer;


    public ObServerManager(ServerInterface serverRMI, SocketServer socketServer){
        this.serverRMI = serverRMI;
        this.socketServer = socketServer;
        modelMessages = new LinkedBlockingQueue<>();

    }


    public void run() throws IOException, InterruptedException {
    ModelMessage msg;
        while(true){
            if (!modelMessages.isEmpty()) {
                try {

                    msg = modelMessages.take();


                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                serverRMI.addToClientQueue(msg);
                socketServer.addToClientQueue(msg);
            }else{
                Thread.sleep(2000);
            }
        }
    }
    public void addModelMessageToQueue(ModelMessage msg){
        modelMessages.add(msg);
    }
}
