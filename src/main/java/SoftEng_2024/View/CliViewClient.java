package SoftEng_2024.View;



import SoftEng_2024.Network.ToModel.ClientInterface;
import SoftEng_2024.View.ViewMessages.*;
import SoftEng_2024.View.ViewStates.ConnectionState;
import SoftEng_2024.View.ViewStates.ViewState;


import java.rmi.RemoteException;
import java.util.InputMismatchException;
import java.util.Scanner;

public class CliViewClient implements View {
    ClientInterface client;
    double ID;
    private LocalModel localModel;

    public CliViewClient(double ID,ClientInterface client){
        this.ID=ID;
        this.client=client;
        localModel = new LocalModel();
    }

    public void run(){
        System.out.println("The CliView is running!");
        Thread clientQueueExecutor = new Thread(()->
        {
            try {
                this.client.run();
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        });
        clientQueueExecutor.start();
        //First display to join, rejoin or quit
        ViewState state = new ConnectionState(this,client, ID);
        state.display();
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

      //  updateClient(msg);

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
       // updateClient(msg);

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
     //   updateClient(msg);

    }


    public LocalModel getLocalModel() {
        return localModel;
    }
}
