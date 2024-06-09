package SoftEng_2024.Network.ToModel;

import SoftEng_2024.Model.ModelMessages.ModelMessage;
import SoftEng_2024.View.View;
import SoftEng_2024.View.ViewMessages.ViewMessage;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

public class ClientRMI extends UnicastRemoteObject implements ClientInterface {
    private View view;
    private ServerInterface server;
    public volatile LinkedBlockingQueue<ModelMessage> modelQueue;
    double ID;

    public ClientRMI(double ID) throws RemoteException {
        this.modelQueue=new LinkedBlockingQueue<>();
        this.ID = ID;
    }

    public synchronized void run() throws InterruptedException {
        while(true){
            //se andasse in wait anche se non ha la coda vuota???
            if(modelQueue.isEmpty()){
              wait();
            }else {
                //System.out.println("the message executing is: "+modelQueue.peek());
                modelQueue.take().executeMessage(this.view);
                //System.out.println("executed Message:");
            }
        }
    }
    @Override
    public  void update(ViewMessage msg) throws RemoteException {
        server.addToNetworkManager(msg);

    }

    @Override
    public void quit(ViewMessage msg) throws RemoteException {

    }

    @Override
    public synchronized void addToViewQueue(ModelMessage msg) throws RemoteException {
        //System.err.println("sto aggiungendo il messaggio: "+msg);
        modelQueue.add(msg);
        notifyAll();
    }

    public void registerToServer(double ID, ClientInterface client) throws RemoteException, NotBoundException {
        //lookup on the registry
        Registry registry = LocateRegistry.getRegistry("localhost", 6969);
        this.server = (ServerInterface) registry.lookup("localhost");
        server.registerClient(ID, client);
    }

//    private void pollThreaded()  {
//        Thread t = new Thread(() -> {
//            try {
//                System.out.println(modelQueue.peek());
//                //System.out.println("sto per eseguire il messaggio diocane" + modelQueue.take());
//
//                System.out.println("Ho eseguito il messaggio");
//            } catch (InterruptedException e) {
//                System.err.println("Something went wrong while executing the messages");
//                throw new RuntimeException(e);
//            }
//        });
//        t.start();
//    }

    @Override
    public void pong() throws RemoteException{
    }




    public void setView(View view) {
        this.view = view;
    }
}
