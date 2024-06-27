package SoftEng_2024.View.ViewStates;

import SoftEng_2024.Model.Enums.Color;
import SoftEng_2024.Model.Enums.GameState;
import SoftEng_2024.Network.ToModel.ClientInterface;
import SoftEng_2024.View.CliViewClient;
import SoftEng_2024.View.ViewMessages.SetColorMessage;
import SoftEng_2024.View.ViewMessages.ViewMessage;

import java.util.HashMap;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;
/**
 * SetColorState represents the state where players choose their pawn color at the beginning of the game.
 * Players can choose from available colors and interact with the game through commands such as setting color,
 * showing the board, chatting, or quitting.
 */
public class SetColorState extends ViewState{
    /**
     * Constructs a SetColorState object.
     *
     * @param view   The CliViewClient associated with this state.
     * @param client The ClientInterface through which interactions with the game server occur.
     * @param ID     The unique identifier of the player in the game.
     */
    public SetColorState(CliViewClient view, ClientInterface client,double ID){
        super(view,client,ID);
    }
    /**
     * Displays the set color state, allowing the player to interactively choose their pawn color.
     *
     * @throws InterruptedException If the thread is interrupted while waiting for player actions.
     */
    @Override
    public synchronized void display() throws InterruptedException {
        String indications="Type  Set Color, Show Board, Chat or Quit";
        listenDefaultCommand(true);
        defaultCommand(GameState.STARTER,"Waiting for all the players play their starter card...");
        System.out.println("Everyone played their starter card! Now you all have to choose your color!");
        System.out.println(indications);
        wait();
        //loops until the player chooses a command different from writeInChat
        while(!commandChosen){
            switch(view.getCommand().trim().replaceAll("\\s+", "").toLowerCase()) {
                case "setcolor":
                    commandChosen=true;
                    setColor();
                    view.setCommand("");
                    listenDefaultCommand(true);
                    if(!commandChosen) {
                        System.out.println(indications);
                        wait();
                    }
                    break;
                case "chat":
                    writeInChat();
                    view.setCommand("");
                    listenDefaultCommand(true);
                    System.out.println(indications);
                    wait();
                    break;
                case "showboard":
                    printPlayerBoard(false);
                    view.setCommand("");
                    listenDefaultCommand(true);
                    System.out.println(indications);
                    wait();
                    break;
                case "quit":
                    quit();
                    break;
                case "":
                    break;
                default:
                    System.out.println("[ERROR] Command not available... retry");
                    view.setCommand("");
                    listenDefaultCommand(true);
                    System.out.println(indications);
                    wait();
                    break;
            }
            if(commandChosen)
                break;

        }
        this.view.getWaitingState().setPreviousState(this);
        this.view.getWaitingState().setNextState(new ChooseGoalState(view,client,ID));

    }
    /**
     * Allows the player to choose their pawn color by interacting with console input.
     * Validates the chosen color against available options and sends a SetColorMessage to update the game state.
     */
    private void setColor() {
        Scanner input = new Scanner(System.in);
        String answer;
        Color color;
        ConcurrentHashMap<String, Color> colorMap = view.getLocalModel().getPlayersColor();
        System.out.println("The available colors are: ");
        for(Color availableColor : Color.values()) {
            if(!colorMap.containsValue(availableColor) && !availableColor.equals(Color.BLACK) & !availableColor.equals(Color.EMPTY)){
                System.out.print(availableColor+ " ");
            }
        }
        System.out.println("Type the color you want for your pawn, or type 'exit' to cancel");
        answer = input.nextLine().trim().replaceAll("\\s+", "").toUpperCase();
        boolean correctColor = false;
        while (!correctColor) {
            if(answer.equals("EXIT")){
                 
                commandChosen=false;
                return;
            }
            else if ((answer.equals("RED") | answer.equals("BLUE") | answer.equals("YELLOW") | answer.equals("GREEN")) && !colorMap.containsValue(Color.valueOf(answer))) {
                correctColor = true;
            }else if(!answer.equals("RED") && !answer.equals("BLUE") && !answer.equals("YELLOW") && !answer.equals("GREEN")){
                System.out.println("[ERROR] Wrong input, please choose between the colors: Red , Blue, Green, Yellow");
                 
                answer = input.nextLine().trim().replaceAll("\\s+", "").toUpperCase();
            } else if (colorMap.containsValue(Color.valueOf(answer))){
                System.out.println("[ERROR] The color you chose has been already chosen by someone else... retry");
                 
                answer = input.nextLine().trim().replaceAll("\\s+", "").toUpperCase();
            }
        }
         
        color = Color.valueOf(answer);
        ViewMessage msg = new SetColorMessage(color, this.ID);
        updateClient(msg);
    }
}
