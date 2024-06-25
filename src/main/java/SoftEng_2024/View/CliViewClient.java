package SoftEng_2024.View;



import SoftEng_2024.Model.Enums.GameState;
import SoftEng_2024.Network.ToModel.ClientInterface;
import SoftEng_2024.View.ViewStates.ConnectionState;
import SoftEng_2024.View.ViewStates.ViewState;
import SoftEng_2024.View.ViewStates.WaitingState;

import java.rmi.RemoteException;

import static java.lang.Thread.sleep;

public class CliViewClient implements View {
    public Thread clientQueueExecutor;
    public ClientInterface client;
    private String command="";
    private final double ID;
    ViewState viewState;
    WaitingState waitingState;
    private final LocalModel localModel;

    public CliViewClient(double ID, ClientInterface client){
        this.ID=ID;
        this.client=client;
        localModel = new LocalModel();
    }

    public void run() {
        clientQueueExecutor = new Thread(()->
        {
            try {
                this.client.run();
            } catch (RemoteException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
        clientQueueExecutor.start();
        localModel.setPlayerState(GameState.CONNECTION);
        localModel.setGameState(GameState.CONNECTION);

        //First display to join, rejoin or quit
        this.viewState = new ConnectionState(this,client, ID);
        this.waitingState=new WaitingState(localModel,this);
        while(true)
        {
            try {
                this.viewState.display();//-->setta this.waitingstate con il giusto previous e il giusto next
                this.waitingState.display();// -->se ack successful--> view.setState(nextState) else previous
            }catch(InterruptedException e){

            }
        }
    }

    @Override
    public double getID() {
        return ID;
    }

    public LocalModel getLocalModel() {
        return localModel;
    }

    public void setViewState(ViewState viewState){
        this.viewState = viewState;
    }

    public WaitingState getWaitingState() {
        return waitingState;
    }
    public void setCommand(String command) {
        this.command = command;
    }

    public String getCommand() {
        return command;
    }

    public ViewState getViewState() {
        return viewState;
    }

}
