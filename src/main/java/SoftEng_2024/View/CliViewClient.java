package SoftEng_2024.View;


import SoftEng_2024.Model.Enums.Color;
import SoftEng_2024.Network.ToModel.ClientInterface;
import SoftEng_2024.View.ViewMessages.*;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.InputMismatchException;
import java.util.Scanner;

public class CliViewClient implements View {

    ClientInterface client;
    double ID;

    public CliViewClient(double ID,ClientInterface client){
        this.ID=ID;
        this.client=client;
    }

    public void run(){
        System.out.println("The CliView is running!");
        System.out.println("Type  CreateGame, Join or ReJoin to play!");

        Thread clientQueueExecutor = new Thread(()->
        {
            try {
                this.client.run();
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        });
        clientQueueExecutor.start();

        Scanner scanner= new Scanner(System.in);
        String command=scanner.nextLine();

        do {
            switch(command){
                case "CreateGame":
                    createGame();
                    break;
                case "PlayCard":
                    playCard();
                    break;
                case "DrawFromTheDeck":
                    drawFromTheDeck();
                    break;
                case "DrawPublicCard":
                    drawPublicCard();
                    break;
                case "Join":
                    join();
                    break;
                case "Chat":
                    writeInChat();
                    break;
                case "ReJoin":
                    reJoin();
                    break;
                case "SetColorMessage":
                    setColorMessage();
                    break;
                case "PlayStarterCard":
                    playStarterCard();
                    break;
                case "ChoosePrivateGoal":
                    choosePrivateGoal();
                    break;
                default:
                    System.err.println("Command not available... retry");
                    break;
            }
            //timer to check AFK status
            System.out.println("Type a command");
            command=scanner.nextLine();

        }while (!command.equals("quit"));
        quit();
    }

    private void choosePrivateGoal() {
        Scanner scanner= new Scanner(System.in);
        String answer;
        System.out.println("Type [1] or [2] to choose your private goal");
        answer=scanner.nextLine();
        while(!answer.equals("1") && !answer.equals("2")){
            System.err.println("Wrong input, type [1] or [2] to choose your private goal");
            answer=scanner.nextLine();
        }

        int choice = Integer.parseInt(answer);
        System.out.println("Got it, keep it a secret!");
        ViewMessage msg= new ChoosePrivateGoal(choice,ID);
        updateClient(msg);
    }

    private void playStarterCard() {
        Scanner input= new Scanner(System.in);
        String answer;
        boolean flipped;
        do {
            System.out.println("Type the side of the card (front or back): ");
            answer = input.nextLine();
        }while(!answer.equals("front") && !answer.equals("back"));
        flipped= !answer.equals("front");
        ViewMessage msg= new PlayStarterCardMessage(flipped,this.ID);
        updateClient(msg);
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
            return;
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
    private void setColorMessage() {
        Scanner input = new Scanner(System.in);
        String answer;
        Color color;
        System.out.println("Type the color (RED,BLUE,GREEN,YELLOW) you want for your pawn");
        answer = input.nextLine();
        while(!answer.equals("RED") && !answer.equals("BLUE") && !answer.equals("YELLOW") && !answer.equals("GREEN")){
            System.err.println("Wrong input, please choose between the colors: [RED,BLUE,GREEN,YELLOW]");
            answer=input.nextLine();
        }
        color = Color.valueOf(answer);
        ViewMessage msg = new SetColorMessage(color, this.ID);
        updateClient(msg);
    }

    private void playCard(){
        Scanner input = new Scanner(System.in);
        int card;

        System.out.println("Type the index (1, 2 or 3) of the card you want to play: ");
        try {
            card = input.nextInt();
        }catch (InputMismatchException e){
            card = -1;
            input.nextLine();
            System.err.println("Wrong input, type an integer!");
        }
        while(card!=1 && card!=2 &&card!=3 ){
            System.err.println("Wrong input, type '1', '2' or '3'");
            try {
                card = input.nextInt();
            }catch (InputMismatchException e){
                card = -1;
                input.nextLine();
                System.err.println("Wrong input, type an integer!");
            }
        }
        card--;

        int row;
        int column;

        System.out.println("Type the row and the column (0 <= r,c <= 84) of the cell: ");
        try {
            row = input.nextInt();
            column = input.nextInt();
        }catch(InputMismatchException e){
            row=-1;
            column=-1;
            input.nextLine();
            System.err.println("Wrong input, type an integer!");
        }
        while(row<0 | row>84 | column<0 | column>84){
            System.err.println("Wrong input, type a integer between '0' and '84' either for the row and the column");
            try {
                row = input.nextInt();
                column = input.nextInt();
            }catch(InputMismatchException e){
                row=-1;
                column=-1;
                input.nextLine();
                System.err.println("Wrong input, type an integer!");
            }
        }

        boolean flipped;
        String answer;
        System.out.println("Type the side of the card (front or back): ");
        answer = input.nextLine();
        while(!answer.equals("front") && !answer.equals("back")){
            System.err.println("Wrong input, type 'front' or 'back'!");
            answer = input.nextLine();
        }

        flipped = !answer.equals("front");

        ViewMessage msg = new PlayCardMessage(card, row, column, flipped, this.ID);

        updateClient(msg);

    }

    private void drawFromTheDeck(){
        //public void drawFromTheDeck(int deck,double ID)
        Scanner input = new Scanner(System.in);
        String deck;
        int index;
        System.out.println("Type which deck (gold or resources) you want to draw from: ");
        deck = input.nextLine();
        while(!deck.equals("gold") && !deck.equals("resources")){
            System.err.println("Wrong input, type 'gold' or 'resources'!");
            deck = input.nextLine();

        }

        index = deck.equals("gold") ? 1 : 0;
        ViewMessage msg = new DrawFromTheDeckMessage(index, this.ID);
        updateClient(msg);

    }

    private void drawPublicCard(){
        Scanner input=new Scanner(System.in);
        int card;
        do {
            System.out.println("Type which card you want to draw: [1,2] for resource cards [3,4] for gold cards ");
            try {
                card = input.nextInt();
            }catch(InputMismatchException e){
                card=-1;
                input.nextLine();
                System.err.println("Wrong input, type an integer!");
            }
        }while(card!=1 && card!=2 && card!=3 && card!=4);
        ViewMessage msg= new DrawFromPublicCardsMessage(card,this.ID);
        updateClient(msg);

    }

    private void writeInChat(){

    }

    private void reJoin(){
        Scanner scanner = new Scanner(System.in);
        System.out.println("Type the nickname you chose on first connection: ");
        String nickname = scanner.nextLine();

    }

    private void quit() {
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

    private void updateClient(ViewMessage msg){
        try {
            client.update(msg);
        } catch (RemoteException e) {
            System.err.println("Something went wrong, retry!");
        }
    }

}
