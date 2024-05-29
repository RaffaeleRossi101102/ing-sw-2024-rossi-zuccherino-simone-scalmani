package SoftEng_2024.View.ViewStates;

import SoftEng_2024.Model.Enums.GameState;
import SoftEng_2024.Network.ToModel.ClientInterface;
import SoftEng_2024.View.CliViewClient;
import SoftEng_2024.View.ViewMessages.PlayCardMessage;
import SoftEng_2024.View.ViewMessages.ViewMessage;

import java.util.InputMismatchException;
import java.util.Scanner;

public class PlayState extends ViewState{



    private ViewState drawState;

    public PlayState(CliViewClient view, ClientInterface client, double ID){
        super(view,client,ID);
    }

    @Override
    public void display() {
        System.out.println("Wait for your turn to play, meanwhile...");
        defaultCommand(GameState.NOTPLAYING);
        System.out.println("Now it's your turn!");
        // TODO if(view.getLocalModel().getPlayerHand().size() == 3) {
            Scanner scanner = new Scanner(System.in);
            System.out.println("Type Play Card, Chat or Quit");
            String command = scanner.nextLine();
            //loops until the player chooses a command different from writeInChat
            while (!commandChosen) {
                switch (command.trim().toLowerCase()) {
                    case "playcard":
                        playCard();
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
                if(commandChosen)
                    break;
                System.out.println("Type Play Card, Chat or Quit");
                command = scanner.nextLine();
            }
        //TODO } else {
        // System.out.println("When you logged off last time, you played a card but didn't draw another.
        //                     Now, you'll need to finish your turn by drawing a card without playing...");
        // }
        waitingState.setPreviousState(this);
        waitingState.setNextState(drawState);
        waitingState.display();
    }

    public void setDrawState(ViewState drawState) {
        this.drawState = drawState;
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


}
