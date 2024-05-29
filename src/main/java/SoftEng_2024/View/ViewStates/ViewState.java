package SoftEng_2024.View.ViewStates;

import SoftEng_2024.Model.Enums.GameState;
import SoftEng_2024.Network.ToModel.ClientInterface;
import SoftEng_2024.View.CliViewClient;
import SoftEng_2024.View.ViewMessages.BroadcastMessage;
import SoftEng_2024.View.ViewMessages.QuitMessage;
import SoftEng_2024.View.ViewMessages.ViewMessage;
import SoftEng_2024.View.ViewMessages.WhisperMessage;

import java.rmi.RemoteException;
import java.util.Scanner;

public abstract class ViewState {
    protected CliViewClient view;

    protected ClientInterface client;

    protected double ID;
    protected boolean commandChosen;
    protected WaitingState waitingState;

    protected String defaultCommand;

    public ViewState(CliViewClient view,ClientInterface client, double ID){
        this.view=view;
        this.client=client;
        this.ID=ID;
        this.commandChosen=false;
        waitingState = new WaitingState();
    }
    public abstract void display();

    protected void updateClient(ViewMessage msg){
        try {
            client.update(msg);
        } catch (RemoteException e) {
            System.err.println("Something went wrong, retry!");
        }
    }

    protected void defaultCommand(GameState gameState) {
        if(view.getLocalModel().getState().equals(gameState)) {
            setDefaultCommand("");
            listenDefaultCommand();
            while (view.getLocalModel().getState().equals(gameState)) {
                switch (defaultCommand.trim().toLowerCase()) {
                    case "chat":
                        writeInChat();
                        listenDefaultCommand();
                        setDefaultCommand("");
                        break;
                    case "quit":
                        quit();
                        listenDefaultCommand();
                        setDefaultCommand("");
                    case "":
                        break;
                    default:
                        System.err.println("Command not available... retry");
                        listenDefaultCommand();
                        setDefaultCommand("");
                        break;
                }
            }
        }
    }

    protected void listenDefaultCommand(){
        Thread t = new Thread(() -> {
            System.out.println("Type 'chat' or 'quit' to use the chat or to quit the game");
            Scanner scanner = new Scanner(System.in);
            setDefaultCommand(scanner.nextLine());
        });
        t.start();
    }

    protected void writeInChat(){
        System.out.println("Inside write in chat command...");
        System.out.println("Do you want to whisper or to broadcast your message? Type 'whisper' or 'broadcast'");
        Scanner scanner= new Scanner(System.in);
        String choice= scanner.nextLine();
        while(!choice.equals("whisper") & !choice.equals("broadcast")){
            System.out.println("Wrong input, please write 'whisper' or 'broadcast'");
            choice=scanner.nextLine();
        }
        if(choice.equals("whisper"))
            whisper();
        else
            broadcast();
    }

    protected void whisper(){
        System.out.println("Inside whisper command...");
        System.out.println("Who do you want to whisper to? Type the player's nickname:");
        Scanner scanner= new Scanner(System.in);
        String nickname= scanner.nextLine();
        //TODO: confronto con i nickname degli utenti (nel server o nel local model? se nel local model, dove si trova,
        // nella view o nello stato?)
        updateClient(new WhisperMessage(typeMessage(),nickname,ID));
    }

    protected void broadcast(){
        System.out.println("Inside broadcast command...");
        updateClient(new BroadcastMessage(typeMessage(),ID));

    }

    protected String typeMessage(){
        System.out.println("Type your message [max 128 char]");
        Scanner scanner= new Scanner(System.in);
        String message= scanner.nextLine();
        while(message.length()>128){
            System.out.println("Error, message too long, please make it shorter than 128 characters");
            message= scanner.nextLine();
        }
        return message;
    }

    protected void quit() {
        Scanner input = new Scanner(System.in);
        String answer;
        System.out.println("Are you sure you want to quit? y/n");
        answer = input.nextLine();
        while (!answer.equals("y") && !answer.equals("n")) {
            System.err.println("Wrong input, type y/n");
            answer = input.nextLine();
        }
        if (answer.equals("y")) {
            ViewMessage msg = new QuitMessage(this.ID);
            updateClient(msg);
            System.exit(0);
        }
    }


    public void setDefaultCommand(String defaultCommand) {
        this.defaultCommand = defaultCommand;
    }
}
