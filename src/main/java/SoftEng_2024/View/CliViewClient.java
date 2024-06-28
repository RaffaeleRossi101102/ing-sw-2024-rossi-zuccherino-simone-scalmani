package SoftEng_2024.View;



import SoftEng_2024.Model.Enums.GameState;
import SoftEng_2024.Network.ToModel.ClientInterface;
import SoftEng_2024.View.ViewStates.ConnectionState;
import SoftEng_2024.View.ViewStates.ViewState;
import SoftEng_2024.View.ViewStates.WaitingState;

import java.rmi.RemoteException;

import static java.lang.Thread.sleep;
/**
 * Represents a CLI-based view client that interacts with a game through a {@link ClientInterface}.
 * Manages states of connection, waiting, and displays based on user input and game updates.
 */
public class CliViewClient implements View {
    public Thread clientQueueExecutor;
    public ClientInterface client;
    private String command="";
    private final double ID;
    ViewState viewState;
    WaitingState waitingState;
    private final LocalModel localModel;
    /**
     * Constructs a CliViewClient instance with the specified ID and client interface.
     *
     * @param ID     The unique identifier for this client.
     * @param client The client interface used for communication with the game server.
     */
    public CliViewClient(double ID, ClientInterface client){
        this.ID=ID;
        this.client=client;
        localModel = new LocalModel();
    }
    /**
     * Starts the client execution, initializing necessary threads and setting initial game states.
     */
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
                this.viewState.display();//-->sets this.waitingstate with the right previous and next state
                this.waitingState.display();// -->if ack successful--> view.setState(nextState) else previous
            }catch(InterruptedException e){

            }
        }
    }
    /**
     * Retrieves the ID of this client.
     *
     * @return The ID of this client.
     */
    @Override
    public double getID() {
        return ID;
    }
    /**
     * Retrieves the local model associated with this client.
     *
     * @return The local model of this client.
     */
    public LocalModel getLocalModel() {
        return localModel;
    }
    /**
     * Sets the current view state of this client.
     *
     * @param viewState The new view state to set.
     */
    public void setViewState(ViewState viewState){
        this.viewState = viewState;
    }
    /**
     * Retrieves the waiting state of this client.
     *
     * @return The waiting state of this client.
     */
    public WaitingState getWaitingState() {
        return waitingState;
    }
    /**
     * Sets a command to be executed by this client.
     *
     * @param command The command to set.
     */
    public void setCommand(String command) {
        this.command = command;
    }
    /**
     * Retrieves the current command set for this client.
     *
     * @return The current command.
     */
    public String getCommand() {
        return command;
    }
    /**
     * Retrieves the current view state of this client.
     *
     * @return The current view state.
     */
    public ViewState getViewState() {
        return viewState;
    }

}
