package SoftEng_2024.View.ViewStates;

import SoftEng_2024.Network.ToModel.ClientInterface;
import SoftEng_2024.View.CliViewClient;
import SoftEng_2024.View.ViewMessages.DrawFromPublicCardsMessage;
import SoftEng_2024.View.ViewMessages.DrawFromTheDeckMessage;
import SoftEng_2024.View.ViewMessages.ViewMessage;

import java.util.InputMismatchException;
import java.util.Scanner;

public class DrawState extends ViewState{


    private ViewState playState;
    public DrawState(CliViewClient view, ClientInterface client, double ID) {
        super(view,client,ID);
    }

    @Override
    public void display() {
        System.out.println("Now you have to draw a card from the 4 public cards or from one of the decks!");
        Scanner scanner = new Scanner(System.in);
        System.out.println("Type Draw From Deck, Draw Public Card, Chat or Quit");
        String command = scanner.nextLine();

        while (!commandChosen) {
            switch (command.trim().toLowerCase()) {
                case "drawfromdeck":
                    drawFromTheDeck();
                    commandChosen = true;
                    break;
                case "drawpubliccard":
                    drawPublicCard();
                    commandChosen = true;
                    break;
                case "chat":
                    writeInChat();
                    break;
                case "quit":
                    quit();
                    break;
                default:
                    System.err.println("Command not available... retry");
                    break;
            }
            System.out.println("Type Play Card, Chat or Quit");
            command = scanner.nextLine();
        }
        waitingState.setPreviousState(this);
        waitingState.setNextState(playState);
        waitingState.display();

    }

    public void setPlayState(ViewState playState) {
        this.playState = playState;
    }

    private void drawFromTheDeck(){
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
        try {
            card = input.nextInt();
        }catch(InputMismatchException e){
            card=-1;
            input.nextLine();
        }

        while(card!=1 && card!=2 && card!=3 && card!=4){
            System.out.println("Wrong Input!\nType which card you want to draw: [1,2] for resource cards [3,4] for gold cards ");
            try {
                card = input.nextInt();
            }catch(InputMismatchException e){
                card=-1;
                input.nextLine();
            }
        }

        card--;
        ViewMessage msg= new DrawFromPublicCardsMessage(card,this.ID);
        updateClient(msg);

    }
}
