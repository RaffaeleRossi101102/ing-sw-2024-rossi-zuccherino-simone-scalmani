package SoftEng_2024.View.ViewStates;


import SoftEng_2024.Network.ToModel.ClientInterface;
import SoftEng_2024.View.CliViewClient;
import SoftEng_2024.View.ViewMessages.*;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Scanner;
/**
 * Represents the initial state where a player can create a game, join an existing game, or rejoin a previous game.
 * Extends {@link ViewState}.
 */
public class ConnectionState extends ViewState {
    /**
     * Constructs a ConnectionState instance with the specified CLI view client, client interface, and player ID.
     *
     * @param view   The CLI view client managing the state transitions.
     * @param client The client interface used for communication with the game server.
     * @param ID     The ID of the player associated with this state.
     */
    public ConnectionState(CliViewClient view, ClientInterface client, double ID) {
        super(view,client,ID);
    }
    /**
     * Displays the options for creating a game, joining a game, or rejoining a game.
     * Waits for user input and processes commands until a valid command is chosen.
     */
    @Override
    public void display() {
        ViewState nextState=new StarterState(view,client,ID);
        Scanner scanner = new Scanner(System.in);
        System.out.println("Type  Create Game, Join or ReJoin to play!");
        String command = scanner.nextLine();
        //Stays in the while loop until a different command from "chat" gets chosen
        while (!commandChosen) {
            switch (command.trim().toLowerCase().replaceAll("\\s+", "")) {
                case "creategame":
                    commandChosen = true;
                    createGame();
                    break;
                case "join":
                    commandChosen = true;
                    join();
                    break;
                case "rejoin":
                    reJoin();
                    commandChosen = true;
                    nextState=new RejoinState(view,client,ID);

                    break;
                case "quit":
                    quit();
                    break;
                default:
                    System.out.println("[ERROR] Command not available... retry");
                    break;
            }
            if(commandChosen)
                break;
            System.out.println("Type  Create Game, Join or ReJoin to play!");
            command=scanner.nextLine();
        }
        //once the user chose a command he will have to wait for the model's response
        view.getWaitingState().setPreviousState(new ConnectionState(this.view,this.client,this.ID));
        view.getWaitingState().setNextState(nextState);
    }
    /**
     * Handles the process of creating a new game.
     * Prompts the user for nickname and maximum players, then sends a message to update the client.
     */
    private void createGame() {
        System.out.println("Inside create game command...");

        Scanner input = new Scanner(System.in);

        String nickname;
        nickname=askNickname(input);

        if(nickname.equals("exit")){
            commandChosen = false;
            return;
        }

        int maxPlayers = askMaxPlayers(input);
        if(maxPlayers==-1){
            commandChosen = false;
            return;
        }
        try {
            client.registerToServer(ID, client);
        } catch (RemoteException | NotBoundException e) {
            System.out.println("[ERROR] Server not found or something went terribly wrong!");
            System.exit(0);
        }

        ViewMessage msg = new CreateGameMessage(nickname, maxPlayers, this.ID);
        updateClient(msg);

    }
    /**
     * Prompts the user to enter the maximum number of players for the game.
     * Validates the input and returns the number of players chosen.
     *
     * @param input The scanner object to read user input.
     * @return The maximum number of players chosen, or -1 if the operation is canceled.
     */
    private int askMaxPlayers(Scanner input) {
        System.out.println("Insert how many players the game will have, or type 'exit' to cancel");
        String maxPlayers = input.nextLine().toLowerCase().trim().replaceAll("\\s+", "");

        while(!maxPlayers.equals("2") && !maxPlayers.equals("3") && !maxPlayers.equals("4") && !maxPlayers.equals("exit")){
            System.out.println("[ERROR] The maximum number of players has to be between 2 and 4, or you can type 'exit' to cancel");
            maxPlayers = input.nextLine();
        }

        if(maxPlayers.equals("exit")){
            return -1;
        }


        return Integer.parseInt(maxPlayers);
    }
    /**
     * Prompts the user to enter a nickname for joining or rejoining the game.
     *
     * @param input The scanner object to read user input.
     * @return The nickname entered by the user.
     */
    private String askNickname(Scanner input){
        String nickname;
        System.out.println("Type your nickname (max 20 char), or type 'exit' to cancel");
        nickname=input.nextLine();
        while(nickname.length()>20){
            System.out.println("[ERROR] Nice name, but make it shorter! (max 20 char)");
            nickname=input.nextLine();
        }
        System.out.println("Got it :)");
        return nickname;
    }
    /**
     * Handles the process of joining an existing game.
     * Prompts the user for nickname and sends a message to update the client.
     */
    private void join() {
        System.out.println("Inside join command...");

        Scanner input = new Scanner(System.in);

        String nickname;
        nickname=askNickname(input);

        if(nickname.equals("exit")){
            commandChosen = false;
            return;
        }

        try {
            client.registerToServer(ID, client);
        } catch (RemoteException | NotBoundException e) {
            System.out.println("[ERROR] Error 404... server not found");
            return;
        }

        ViewMessage msg= new JoinGameMessage(nickname,this.ID);
        updateClient(msg);
    }
    /**
     * Handles the process of rejoining a previous game.
     * Prompts the user for nickname and sends a message to update the client.
     */
    private void reJoin(){
        Scanner scanner = new Scanner(System.in);
        System.out.println("Type the nickname you chose when you first connected (max 20 char), or type 'exit' to cancel");
        String nickname = scanner.nextLine();
        while(nickname.length()>20){
            System.out.println("[ERROR] There are not nicknames this long in game, retry...");
            nickname = scanner.nextLine();
        }
        if(nickname.equals("exit")){
            commandChosen = false;
            return;
        }
        try {
            client.registerToServer(ID,client);
        } catch (RemoteException | NotBoundException e) {
            throw new RuntimeException(e);
        }
        ViewMessage msg = new ReJoinMessage(this.ID, nickname);
        updateClient(msg);
    }

}
