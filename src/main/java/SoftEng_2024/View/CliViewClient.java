package SoftEng_2024.View;



import SoftEng_2024.Network.ToModel.ClientInterface;
import SoftEng_2024.View.ViewStates.ConnectionState;
import SoftEng_2024.View.ViewStates.ViewState;
import java.rmi.RemoteException;

public class CliViewClient implements View {
    ClientInterface client;
    double ID;
    private final LocalModel localModel;

    public CliViewClient(double ID,ClientInterface client){
        this.ID=ID;
        this.client=client;
        localModel = new LocalModel();
    }

    public void run(){
        //TODO logo CLIArt
        System.out.println("WELCOME TO CODEX NATURALIS BY CRANIO CREATIONS");
        Thread clientQueueExecutor = new Thread(()->
        {
            try {
                this.client.run();
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        });
        clientQueueExecutor.start();
        //First display to join, rejoin or quit
        ViewState state = new ConnectionState(this,client, ID);
        state.display();
    }
    @Override
    public double getID() {
        return ID;
    }

    public LocalModel getLocalModel() {
        return localModel;
    }

}
