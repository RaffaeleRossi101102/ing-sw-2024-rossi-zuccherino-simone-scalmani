package SoftEng_2024.Network;

import SoftEng_2024.Model.Board;
import SoftEng_2024.Model.GoalCard;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Scanner;
import java.util.*;

public class RMIClient extends UnicastRemoteObject implements ClientInterface{
    private volatile boolean startGame=false;

    private volatile GoalCard[] goals;
    private String nickname;
    final ServerInterface server;
    public RMIClient(ServerInterface server) throws RemoteException{
        this.server=server;
    }
    public void showServerMessage(String message) throws RemoteException{
        System.out.println(message);
    }
    public void showServerError(String error) throws RemoteException{
        System.err.println(error);
    }
    public int choosePlayersMax() throws RemoteException{
        System.out.print("Enter the number of players who will play: ");
        Scanner scanner = new Scanner(System.in);
        int number= scanner.nextInt();
        while(number <2 || number >4){
            System.err.println("The number of players has to be between 2 and 4: retry...");
            number=scanner.nextInt();
        }
        return number;
    }

    public String setNickname() throws RemoteException{
        System.out.println("Insert your nickname: ");
        Scanner scanner = new Scanner(System.in);
        nickname= scanner.nextLine();
        this.nickname=nickname;
        return nickname;
    }

    //metodo che serve per segnalare l'attività del client
    public boolean heartBeat() throws RemoteException {
        return true;
    }

    public boolean playStarterCard() throws RemoteException{
        String answer;
        System.out.println("Do you want to play the card on its back or front? (Type 'back' or 'front')");
        Scanner scanner= new Scanner(System.in);
        answer = scanner.nextLine();

        while (!answer.equals("front") && !answer.equals("back")){
            System.err.println("I didn't understand... Type 'back' or 'front'");
            answer = scanner.nextLine();
        }

        return !answer.equals("front");
    }



    private void run() throws RemoteException, Board.notAvailableCellException, Board.necessaryResourcesNotAvailableException {
        this.server.connect(this);
        //after the connection, each client waits
        // until the controller asks them to do a specific action (still in preparation phase of the game)
        while(goals==null){
            //posso lasciare comunque la funzione della chat?
        }
        //once out of the while loop, each client will have to choose their private goal
        Scanner scanner= new Scanner(System.in);
        boolean inputValid=false;
        int choice=-1;
        while(!inputValid) {
            try {
                choice = scanner.nextInt();
                choice--;
                while (choice != 0 && choice != 1) {
                    System.err.println("Wrong input,in order to choose write [1] or [2]...");
                    choice = scanner.nextInt();
                    choice--;
                }
                inputValid=true;
            }catch(InputMismatchException ime){
                System.err.println("Wrong input, in order to choose write [1] or [2]...");
                scanner.nextLine();
            }
        }
        server.choosePrivateGoals(this,goals[choice]);
        System.out.println("Got it your goal is: "+goals[choice].getGoalType()+" remember to keep it a secret!");
        //now the preparation phase has ended. the real game officially starts
        //waiting until the controller chooses the first player and returns from TurnStart
        while(!startGame){}
        //inside this while loop
        while(startGame){
            System.out.println("Type: [1] to Play a card || [2] to draw a card");
            Scanner scan=new Scanner(System.in);
            int command=0;
            inputValid=false;
            while(!inputValid){
                try{
                    command= scan.nextInt();
                    while(command!= 1 && command!= 2){
                        System.err.println("Wrong input. Please type 1 to play a card or 2 to draw.");
                        command= scanner.nextInt();
                    }
                    inputValid=true;
                    if(command==1)  playCard();
                    if(command==2)  drawCard();
                }catch(InputMismatchException ime) {
                    System.err.println("Wrong input, please type only integers.");
                    scan.nextLine();
                }
            }
        }
        System.out.println("Calculating scores...");
        while(!startGame){}

    }


    public static void main(String[] args) throws RemoteException, NotBoundException, Board.notAvailableCellException, Board.necessaryResourcesNotAvailableException {
        System.out.println("Choose between RMI-> [1] and Sockets-> [2]");
        Scanner scanner= new Scanner(System.in);
        int command= scanner.nextInt();
        while(command != 1 && command !=2){
            System.err.println("I didn't understand... digit [1] or [2]");
            command= scanner.nextInt();
        }
        if(command==1){
            //creating the client as an RMI client
            Registry registry = LocateRegistry.getRegistry("ServerRMI", 6969);
            ServerInterface server = (ServerInterface) registry.lookup("ServerRMI");
            new RMIClient(server).run();
        }
        else{
            //creating the client as a Socket client
        }


    }


    public String chooseColor() throws RemoteException{
        Scanner scanner= new Scanner(System.in);
        String color= scanner.nextLine();
        while(!color.equals("RED") && !color.equals("BLUE") && !color.equals("GREEN") && !color.equals("YELLOW")){
            System.err.println("Wrong input, choose between BLUE, GREEN, YELLOW AND RED...");
            color= scanner.nextLine();
        }
        return color;
    }
    public void playCard() throws RemoteException {
        boolean validInput = false;
        server.printPlayerHand(this);
        System.out.println("Choose the card you want to play: [1] [2] [3]");
        Scanner scanner = new Scanner(System.in);
        int card = 0;
        int row=0;
        int column=0;
        boolean flipped = false;
        String side;
        while (!validInput) {
            try {
                card = scanner.nextInt();
                while (card < 1 | card > 3) {
                    System.err.println("Wrong input, write [1] [2] or [3] to play the corresponding card");
                    card = scanner.nextInt();
                }
                System.out.println("Now choose the side you want to play the card: type 'front' or 'back'");
                scanner.nextLine();
                side=scanner.nextLine();
                while(!side.equals("front") && !side.equals("back")){
                    System.err.println("Wrong input, type front or back!");
                    side=scanner.nextLine();
                }
                if(side.equals("front")) flipped=false;
                if(side.equals("back")) flipped=true;
                System.out.println("Now choose where you want to play the card: [row] [column]. Type only positive numbers < 85");
                row = scanner.nextInt();
                column = scanner.nextInt();
                while(row<0 | column <0 | row>84 | column>84){
                    System.err.println("Wrong input, the rows and columns need to be positive and <85!");
                    row=scanner.nextInt();
                    column=scanner.nextInt();
                }
                
                validInput = true;
            } catch (InputMismatchException ime) {
                System.err.println("Wrong input, please insert integers");
                scanner.nextLine();
            }
        }
        server.playCard(card - 1, this, row, column,flipped);
    }
    public void drawCard() throws RemoteException{
        boolean validInput=false;
        server.printPublicCardToClient(this);
        server.printBackDeckToClient(this);
        System.out.println("Choose between drawing from the deck [1] and drawing from the public cards [2]");
        Scanner scanner= new Scanner(System.in);
        int choice=0;
        while(!validInput){
            try{
                choice=scanner.nextInt();
                while(choice!=1 && choice!=2){
                    System.err.println("Wrong input, write [1] if you want to draw from the deck or [2] if you want to draw from the public cards");
                    choice=scanner.nextInt();
                }
                validInput=true;
            }catch(InputMismatchException ime){
                System.err.println("Wrong input, please insert integers");
                scanner.nextLine();
            }
        }
        if(choice==1)drawFromTheDeck();
        if(choice==2)drawPublicCards();
    }
    public void drawFromTheDeck() throws RemoteException{
        System.out.println("You chose to draw from the deck, which deck do you want to draw from? Type [1]-->Resource deck or [2]-->Gold deck");
        boolean inputValid=false;
        Scanner scanner= new Scanner(System.in);
        int deck=0;
        while(!inputValid){
            try{
                deck=scanner.nextInt();
                while(deck!=1 && deck!=2){
                    System.err.println("Wrong input, type [1]-->Resource deck. Type [2]-->Gold deck");
                    deck=scanner.nextInt();
                }
                inputValid=true;
            }catch(InputMismatchException ime){
                System.err.println("Wrong input, please insert integers");
                scanner.nextLine();
            }
        }
        server.drawFromTheDeck(this,deck);
    }
    public void drawPublicCards() throws RemoteException{
        System.out.println("You chose to draw from the public cards, which card do you want to draw? Type {[1] [2]}-->first two resource cards {[3] [4]}-->first two gold cards ");
        boolean inputValid=false;
        Scanner scanner= new Scanner(System.in);
        int card=0;
        while(!inputValid){
            try{
                card=scanner.nextInt();
                while(card<1 || card>4){
                    System.err.println("Wrong input. Type {[1] [2]}-->first two resource cards {[3] [4]}-->first two gold cards");
                    card=scanner.nextInt();
                }
                inputValid=true;
            }catch(InputMismatchException ime){
                System.err.println("Wrong input, please insert integers");
                scanner.nextLine();
            }
        }
        server.drawPublicCards(this,card-1);
    }
    public boolean getStartGame() {
        return startGame;
    }

    public void setStartGame(boolean startGame) {
        this.startGame = startGame;
    }

    public String getNickname() throws RemoteException{
        return nickname;
    }


    public void setGoals(GoalCard[] goals) throws RemoteException{
        this.goals = goals;
    }
    public void setNullNickname()throws RemoteException{
        this.nickname=null;
    }


}
