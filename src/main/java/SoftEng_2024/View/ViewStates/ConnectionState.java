package SoftEng_2024.View.ViewStates;


import SoftEng_2024.Network.ToModel.ClientInterface;
import SoftEng_2024.View.CliViewClient;
import SoftEng_2024.View.ViewMessages.CreateGameMessage;
import SoftEng_2024.View.ViewMessages.JoinGameMessage;
import SoftEng_2024.View.ViewMessages.ReJoin;
import SoftEng_2024.View.ViewMessages.ViewMessage;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.InputMismatchException;
import java.util.Scanner;

public class ConnectionState extends ViewState {

    public ConnectionState(CliViewClient view, ClientInterface client, double ID) {
        super(view,client,ID);
    }

    @Override
    public void display() {
        ViewState nextState=new StarterState(view,client,ID);
        Scanner scanner = new Scanner(System.in);
        System.out.println("Type  CreateGame, Join or ReJoin to play!");
        String command = scanner.nextLine();
        //Stays in the while loop until a different command from "chat" gets chosen
        while (!commandChosen) {
            switch (command.trim().toLowerCase()) {
                case "creategame":
                    createGame();
                    commandChosen = true;
                    break;
                case "join":
                    join();
                    commandChosen = true;
                    break;
                case "rejoin":
                    reJoin();
                    nextState=new RejoinState(view,client,ID);
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
            System.out.println("Type  CreateGame, Join or ReJoin to play!");
            command=scanner.nextLine();
        }
        //once the user chose a command he will have to wait for the model's response
        waitingState.setPreviousState(this);
        waitingState.setNextState(nextState);
        waitingState.display();
    }
    private void createGame() {
        System.out.println("Inside createGame command...");
        Scanner input = new Scanner(System.in);
        System.out.println("Type your nickname (max 20 char)");
        String nickname=input.nextLine();
        while(nickname.length()>20){
            System.err.println("Nice name, but make it shorter! (max 20 char)");
            nickname= input.nextLine();
        }
        System.out.println("Got it :)");
        System.out.println("Insert how many players the game will have");
        int maxPlayers = input.nextInt();
        boolean inputValid = false;
        while(!inputValid){
            try{
                while(maxPlayers<2 | maxPlayers>4){
                    System.err.println("The maximum number of players has to be between 2 and 4");
                    maxPlayers = input.nextInt();
                }
                inputValid = true;
            }catch(InputMismatchException e){
                System.err.println("Wrong type of input, insert an integer");
                input.nextLine();
            }
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
    private void join() {
        System.out.println("Inside join command...");
        Scanner input = new Scanner(System.in);
        String nickname;
        System.out.println("Type your nickname (max 20 char)");
        nickname=input.nextLine();
        while(nickname.length()>20){
            System.err.println("Nice name, but make it shorter! (max 20 char)");
            nickname=input.nextLine();
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
        System.out.println("Type the nickname you chose on first connection (max 20 char)");
        String nickname = scanner.nextLine();
        while(nickname.length()>20){
            System.err.println("There are not nicknames this long in game, retry...");
            nickname = scanner.nextLine();
        }

        ViewMessage msg = new ReJoin(this.ID, nickname);
        updateClient(msg);
    }

}
