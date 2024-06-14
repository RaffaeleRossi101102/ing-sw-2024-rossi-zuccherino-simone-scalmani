package SoftEng_2024.View.ViewStates;

import SoftEng_2024.Model.Enums.GameState;
import SoftEng_2024.Network.ToModel.ClientInterface;
import SoftEng_2024.View.CliViewClient;
import SoftEng_2024.View.ViewMessages.PlayCardMessage;
import SoftEng_2024.View.ViewMessages.ViewMessage;
import java.util.InputMismatchException;
import java.util.Scanner;

public class PlayState extends ViewState{



    private ViewState nextState;

    public PlayState(CliViewClient view, ClientInterface client, double ID){
        super(view,client,ID);
    }

    @Override
    public void display() {
        System.out.println("It's your turn!");
        if(view.getLocalModel().getPersonalHand().size() == 3) {
//            Scanner scanner = new Scanner(System.in);
            System.out.println("Type Play Card, Chat or Quit");
//            String command = scanner.nextLine();
            //loops until the player chooses a command different from writeInChat
            while (!commandChosen) {
                switch (view.getCommand().trim().replaceAll("\\s+", "").toLowerCase()) {
                    case "playcard":
                        commandChosen = true;
                        playCard();
                        view.setCommand("");
                        listenDefaultCommand();
                        break;
                    case "chat":
                        writeInChat();
                        System.out.println("Type Play Card, Chat or Quit");
                        view.setCommand("");
                        listenDefaultCommand();
                        break;
                    case "quit":
                        quit();
                        break;
                    case"":
                        break;
                    default:
                        System.err.println("Command not available... retry");
                        System.out.println("Type Play Card, Chat or Quit");
                        view.setCommand("");
                        listenDefaultCommand();
                        break;
                }
                if(commandChosen){
                    commandChosen=false;
                    break;
                }
            }
            this.view.getWaitingState().setPreviousState(this);
            this.view.getWaitingState().setNextState(nextState);
        } else {
            System.out.println("When you logged off last time, you played a card but didn't draw another one. " +
                               "Now, you'll need to finish your turn by drawing a card without playing...");
            nextState.display();

        }
    }

    public void setNextState(ViewState nextState) {
        this.nextState = nextState;
    }

    private void playCard(){
        Scanner input = new Scanner(System.in);
        int card;
        String answer;
        System.out.println("Type the index (1, 2 or 3) of the card you want to play, or type 'exit' to cancel ");
        answer = input.nextLine();
        while(!answer.equals("1") && !answer.equals("2") && !answer.equals("3") && !answer.equals("exit")){
            System.err.println("Wrong input... retry!!\nType the index (1, 2 or 3) of the card you want to play, or type 'exit' to cancel");
            answer = input.nextLine();

        }
        if(answer.equals("exit")){
            commandChosen = false;
            return;
        }

        card = Integer.parseInt(answer);
        card--;

        int row;
        int column;
        //TODO: verificare corretto funzionamento degli scanner e del trim
        System.out.println("Type the row and the column (0 <= r & c <= 84) of the cell, or type 'exit' to cancel ");
        try {
            row = input.nextInt();
            column = input.nextInt();
        }catch(InputMismatchException e){
            row=-1;
            column=-1;
            String command = input.nextLine().trim().toLowerCase();
            if (command.equals("exit")){
                commandChosen = false;
                return;
            }
        }
        while(row<0 | row>84 | column<0 | column>84){
            System.err.println("Wrong input, type an integer between '0' and '84' either for the row and the column, or type 'exit' to cancel");
            try {
                row = input.nextInt();
                column = input.nextInt();
            }catch(InputMismatchException e){
                row=-1;
                column=-1;
                String command = input.nextLine().trim().toLowerCase();
                if (command.equals("exit")){
                    commandChosen = false;
                    return;
                }
            }
        }
        boolean flipped;
        System.out.println("Type the side of the card (front or back), or type 'exit' to cancel ");
        input.nextLine();
        answer=input.nextLine();
        while(!answer.equals("front") && !answer.equals("back") && !answer.equals("exit")){
             
            System.err.println("Wrong input... retry!! \nType the side of the card (front or back), or type 'exit' to cancel");
            answer = input.nextLine();
        }
        if(answer.equals("exit")){
            commandChosen = false;
            return;
        }

        flipped = !answer.equals("front");

        ViewMessage msg = new PlayCardMessage(card, row, column, flipped, this.ID);
        updateClient(msg);
    }
}
