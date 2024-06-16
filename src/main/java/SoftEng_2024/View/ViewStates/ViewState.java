package SoftEng_2024.View.ViewStates;

import SoftEng_2024.Model.Cards.Card;
import SoftEng_2024.Model.Enums.GameState;
import SoftEng_2024.Model.Player_and_Board.*;
import SoftEng_2024.Network.ToModel.ClientInterface;
import SoftEng_2024.View.CliViewClient;
import SoftEng_2024.View.LocalBoard;
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
    protected Scanner scanner;
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
        scanner=new Scanner(System.in);
        this.defaultCommand="";
    }
    public abstract void display();

    protected void updateClient(ViewMessage msg){
        try {
            client.update(msg);
        } catch (RemoteException e) {
            System.err.println("Something went wrong, retry!");
        }
    }


    protected void defaultCommand(GameState gameState,String waitMessage) {
        if(view.getLocalModel().getState().equals(gameState)) {
            System.out.println(waitMessage);
            System.out.println("Type Show Board, Show hand, chat, quit to see a player's board, to see your hand, to use the chat or to quit the game");
//            listenDefaultCommand();
            while (view.getLocalModel().getState().equals(gameState) && !view.getLocalModel().getPlayerState().equals(GameState.PLAY)) {
                switch (view.getCommand().trim().replaceAll("\\s+", "").toLowerCase()) {
                    case "showboard":
                        printPlayerBoard();
                        view.setCommand("");
                        listenDefaultCommand();
                        System.out.println("Type Show Board, chat, quit to see a player's board, to use the chat or to quit the game");
                        break;
                    case "showhand":
                        if(view.getLocalModel().getPersonalHand().isEmpty())
                            System.err.println("The hand isn't available yet, please continue with the game");
                        else
                            showHand();
                        view.setCommand("");
                        listenDefaultCommand();
                        System.out.println("Type Show Board, chat, quit to see a player's board, to use the chat or to quit the game");
                        break;
                    case "chat":
                        writeInChat();
                        listenDefaultCommand();
                        view.setCommand("");
                        System.out.println("Type Show Board, chat, quit to see a player's board, to use the chat or to quit the game");
                        break;
                    case "quit":
                        quit();
                        listenDefaultCommand();
                        view.setCommand("");
                    case "":
                        break;
                    default:
                        System.err.println("Command not available... retry");
                        listenDefaultCommand();
                        view.setCommand("");
                        System.out.println("Type Show Board, chat, quit to see a player's board, to use the chat or to quit the game");
                        break;
                }
            }
        }
    }

    protected void listenDefaultCommand(){
        Thread defaultCommandThread = new Thread(() -> {
            //System.out.println("Type 'chat', 'quit' to use the chat or to quit the game");
            //Scanner scanner = new Scanner(System.in);
            String command = scanner.nextLine().toLowerCase().trim().replaceAll("\\s+", "");
            view.setCommand(command);
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
            //updateClient(msg);
            System.exit(0);
        }

    }

    protected void printPlayerBoard(){
        List<String> players = new ArrayList<>();
        players.add(view.getLocalModel().getNickname());
        Scanner input= new Scanner(System.in);
        System.out.println("Whose board do you want to display?");
        int i=1;
        System.out.print("[0]--> your board ~~ ");
        for(String nickname: view.getLocalModel().getPlayersNickname().keySet()){
            players.add(nickname);
            System.out.print("["+i+"]--> "+ nickname+"'s board ~~");
            i++;
        }
        int choice = -1;
        try {
            choice = input.nextInt();
        }catch(InputMismatchException e){
            System.err.println("Wrong input, please insert a number...");
            input.nextLine();
        }
        while(choice<0 | choice > view.getLocalModel().getPlayersNickname().keySet().size()){
            System.err.println("Wrong input, choose a number from the proposed ones");
            choice = -1;
            try {
                choice = input.nextInt();
            }catch(InputMismatchException e) {
                System.err.println("Wrong input, please insert a number...");
                input.nextLine();
            }
        }
        System.out.println();
        if(!view.getLocalModel().getPlayersBoards().containsKey(players.get(choice))) {
            System.err.println("The board isn't already available, please wait...");
            return;
        }
        LocalBoard boardToPrint=view.getLocalModel().getPlayersBoards().get(players.get(choice));

        int minRow = 85;
        int minColumn = 85;
        int maxRow = 0;
        int maxColumn = 0;
        String upCard = "";
        String midCard="";
        String downCard="";

        for(Cell cell:boardToPrint.getCardList()){
            if(cell.getRow()<minRow)
                minRow=cell.getRow();
            if(cell.getRow()>maxRow)
                maxRow=cell.getRow();
            if(cell.getColumn()<minColumn)
                minColumn=cell.getColumn();
            if(cell.getColumn()>maxColumn)
                maxColumn=cell.getColumn();
        }
        System.out.print("   ");
        for(int c = minColumn-1; c<=maxColumn+1; c++){
            if(c>=0 & c< 85) {
                if(c<10)
                    System.out.print("  " + c + "  ");
                else
                    System.out.print("  " + c + " ");
            }
        }
//       |_____40___41___42_
//        43_|░░░||P-A||░░░|
//        _3_|░░░|| G ||░░░|
//           |░░░||P-A||░░░|
        System.out.println();
        if(minRow==0)
            minRow++;
        if(maxRow==84)
            maxRow--;
        if(minColumn==0)
            minColumn++;
        if(maxColumn==84)
            maxColumn--;
        for(int r=minRow-1;r<=maxRow+1;r++){
            upCard="   ";
            midCard="";
            downCard="   ";
            if(r<10) {

                midCard = midCard + " " + r + " ";
            }
            else
                midCard=midCard+r + " ";
            for(int c=minColumn-1;c<=maxColumn+1;c++){
                //se la cella non è vuota, aggiungi alle stringhe la carta
                if(boardToPrint.getCardBoard()[r][c].getCard()!=null){
                    upCard=upCard+boardToPrint.getCardBoard()[r][c].getCard().displayGraphicCard()[0];
                    midCard=midCard+boardToPrint.getCardBoard()[r][c].getCard().displayGraphicCard()[1];
                    downCard=downCard+boardToPrint.getCardBoard()[r][c].getCard().displayGraphicCard()[2];
                }
                else{
                    upCard=upCard + "|‾‾‾|";
                    midCard=midCard+ "|   |";
                    downCard=downCard+"|___|";
                }
            }
            System.out.println(upCard);
            System.out.println(midCard);
            System.out.println(downCard);
        }
    }
    protected void showHand(){
        String upperHand="";
        String midHand="";
        String downHand="";
        int cardIndex=1;
        System.out.println(" [1]     [2]     [3] ");
        for(Card card: view.getLocalModel().getPersonalHand()){
            upperHand=upperHand+ card.displayGraphicCard()[0]+"   ";
            midHand= midHand+ card.displayGraphicCard()[1]+"   ";
            downHand= downHand+ card.displayGraphicCard()[2]+"   ";
//            System.out.println("["+cardIndex+"] "+card.getPrintableCardString());
//            cardIndex++;
        }
        System.out.println(upperHand);
        System.out.println(midHand);
        System.out.println(downHand);
        for(Card card: view.getLocalModel().getPersonalHand()){
            System.out.println("["+cardIndex+"] "+card.getPrintableCardString());
            cardIndex++;
        }
    }
    public void setDefaultCommand(String defaultCommand) {
        this.defaultCommand = defaultCommand;
    }
}
