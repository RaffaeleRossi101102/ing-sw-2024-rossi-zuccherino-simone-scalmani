package SoftEng_2024.View.ViewStates;

import SoftEng_2024.Model.Enums.GameState;
import SoftEng_2024.Network.ToModel.ClientInterface;
import SoftEng_2024.View.CliViewClient;
import SoftEng_2024.View.LocalModel;
import SoftEng_2024.View.ViewMessages.BroadcastMessage;
import SoftEng_2024.View.ViewMessages.QuitMessage;
import SoftEng_2024.View.ViewMessages.ViewMessage;
import SoftEng_2024.View.ViewMessages.WhisperMessage;

import java.rmi.RemoteException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public abstract class ViewState {

    protected CliViewClient view;

    protected ClientInterface client;
    protected LocalModel model;
    protected double ID;
    protected boolean commandChosen;
    protected String defaultCommand;
    protected final long entryTimer;
    protected final long ultimatumTimer;
    protected final long chatTimer;
    protected final long writingMessageTimer;
    protected final long quitTimer;
    protected final long playStarterTimer;
    protected final long setColorTimer;
    protected final long goalTimer;
    protected final long playTimer;
    protected final long drawTimer;


    public ViewState(CliViewClient view,ClientInterface client, double ID){
        this.view=view;
        this.client=client;
        this.ID=ID;
        this.commandChosen=false;
        this.model= view.getLocalModel();
        entryTimer = 10;
        ultimatumTimer = 5;
        chatTimer = 25;
        writingMessageTimer = 100;
        quitTimer = 20;
        playStarterTimer = 20;
        setColorTimer = 20;
        goalTimer = 20;
        playTimer = 60;
        drawTimer = 30;
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
//            listenDefaultCommand();
            while (view.getLocalModel().getState().equals(gameState) && !view.getLocalModel().getPlayerState().equals(GameState.PLAY)) {
                //System.out.println("Lo stato di ora è:" +view.getLocalModel().getState());
                switch (defaultCommand.trim().replaceAll("\\s+", "").toLowerCase()) {
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
        Thread defaultCommandThread = new Thread(() -> {
            System.out.println("Type 'chat', 'quit' to use the chat or to quit the game");
            Scanner scanner = new Scanner(System.in);
            String command = scanner.nextLine().toLowerCase().trim().replaceAll("\\s+", "");
            setDefaultCommand(command);
        });
        defaultCommandThread.start();
    }

    protected void writeInChat(){
        System.out.println("Inside write in chat command...");
        System.out.println("Do you want to whisper or to broadcast your message? Type 'whisper' , 'broadcast' or 'exit' to cancel");
        Scanner scanner = new Scanner(System.in);
        String choice= scanner.nextLine().toLowerCase().trim().replaceAll("\\s+", "");
        while(!choice.equals("whisper") & !choice.equals("broadcast") & !choice.equals("exit")){
            
            System.out.println("Wrong input, please write 'whisper' , 'broadcast' or 'exit' to cancel");
            choice=scanner.nextLine().toLowerCase().trim().replaceAll("\\s+", "");
            if(choice.equals("exit")) return;
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
        while(!view.getLocalModel().getPlayersNickname().contains(nickname)){
            
            System.out.println("There isn't a player with that nickname! Retry or type 'exit' to cancel...");
            nickname= scanner.nextLine();
            if(nickname.equals("exit")) return;
        }
        
        String msg = typeMessage();
        
        if (!msg.equals("exit")) {
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
            LocalDateTime now = LocalDateTime.now();
            updateClient(new WhisperMessage("[" + dtf.format(now) + "]" + " " +
                    model.getNickname() + " " + "has whispered to you: " + msg, nickname, ID));
        }
    }

    protected void broadcast(){
        System.out.println("Inside broadcast command...");
        
        String msg = typeMessage();
        
        if (!msg.equals("exit")) {
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
            LocalDateTime now = LocalDateTime.now();
            System.out.println("sto per mandare il messaggio");
            updateClient(new BroadcastMessage("[" + dtf.format(now) + "]" + " " +
                    model.getNickname() + ": " + msg, ID));
            System.out.println("messaggio mandato");
        }

    }

    protected String typeMessage(){
        
        System.out.println("Type your message [max 128 char] or type 'exit' to cancel");
        Scanner scanner= new Scanner(System.in);
        String message= scanner.nextLine().trim();
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
        answer = input.nextLine().trim().toLowerCase();
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
