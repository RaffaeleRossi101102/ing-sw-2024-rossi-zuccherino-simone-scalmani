package SoftEng_2024.View.ViewStates;


import SoftEng_2024.Network.ToModel.ClientInterface;
import SoftEng_2024.View.CliViewClient;
import SoftEng_2024.View.ViewMessages.*;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Scanner;

public class ConnectionState extends ViewState {

    public ConnectionState(CliViewClient view, ClientInterface client, double ID) {
        super(view,client,ID);
    }

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
                    nextState=new RejoinState(view,client,ID);
//                    nextState.display();
//                    return;
                    commandChosen = true;

                    break;
                case "quit":
                    quit();
                    break;
                default:
                    System.err.println("Command not available... retry");
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
            System.err.println("Server not found or something went terribly wrong!");
            System.exit(0);
        }

        ViewMessage msg = new CreateGameMessage(nickname, maxPlayers, this.ID);
        updateClient(msg);

    }

    private int askMaxPlayers(Scanner input) {
        System.out.println("Insert how many players the game will have, or type 'exit' to cancel");
        String maxPlayers = input.nextLine().toLowerCase().trim().replaceAll("\\s+", "");

        while(!maxPlayers.equals("2") && !maxPlayers.equals("3") && !maxPlayers.equals("4") && !maxPlayers.equals("exit")){
            System.err.println("The maximum number of players has to be between 2 and 4, or you can type 'exit' to cancel");
            maxPlayers = input.nextLine();
        }

        if(maxPlayers.equals("exit")){
            return -1;
        }


        return Integer.parseInt(maxPlayers);
    }

    private String askNickname(Scanner input){
        String nickname;
        System.out.println("Type your nickname (max 20 char), or type 'exit' to cancel");
        nickname=input.nextLine();
        while(nickname.length()>20){
            System.err.println("Nice name, but make it shorter! (max 20 char)");
            nickname=input.nextLine();
        }
        System.out.println("Got it :)");
        return nickname;
    }

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
            System.err.println("Error 404... server not found");
            return;
        }

        ViewMessage msg= new JoinGameMessage(nickname,this.ID);
        updateClient(msg);
    }
    private void reJoin(){
        Scanner scanner = new Scanner(System.in);
        System.out.println("Type the nickname you chose when you first connected (max 20 char), or type 'exit' to cancel");
        String nickname = scanner.nextLine();
        while(nickname.length()>20){
            System.err.println("There are not nicknames this long in game, retry...");
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
