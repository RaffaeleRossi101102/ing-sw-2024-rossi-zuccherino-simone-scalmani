package SoftEng_2024.Model.ModelMessages;

import SoftEng_2024.Model.Player_and_Board.Cell;
import SoftEng_2024.View.View;

import java.util.ArrayList;
/**
 * UpdatedBoardMessage class represents a specific type of ModelMessage used for updating
 * the board state in a game.
 * <p>
 * This class extends ModelMessage and overrides the executeMessage method to update the
 * board state (card board, card list, angles counter, and score) in the local model of
 * a specified view.
 */
public class UpdatedBoardMessage extends ModelMessage{

    /**
     * The 2D array representing the card board state.
     */
    private Cell[][] cardBoard;

    /**
     * The list of cells representing the card list state.
     */
    private ArrayList<Cell> cardList;

    /**
     * The array representing the angles counter state.
     */
    private int[] anglesCounter;

    /**
     * The score associated with the board state.
     */
    private int score;

    /**
     * Constructs an UpdatedBoardMessage with a receiver ID, message content, sender's nickname,
     * card board state, card list state, angles counter state, and score.
     *
     * @param ID The receiver's ID to whom the message is intended.
     * @param message The content of the message.
     * @param senderNickname The nickname of the sender of the message.
     * @param cardBoard The 2D array representing the card board state.
     * @param cardList The list of cells representing the card list state.
     * @param anglesCounter The array representing the angles counter state.
     * @param score The score associated with the board state.
     */
    public UpdatedBoardMessage(double ID, String message, String senderNickname,Cell[][] cardBoard,ArrayList<Cell> cardList,int[] anglesCounter,int score) {
        super(ID, message, senderNickname);
        this.cardBoard=cardBoard;
        this.cardList=cardList;
        this.anglesCounter=anglesCounter;
        this.score=score;
    }

    /**
     * Executes the updated board message by setting the board state (card board, card list,
     * angles counter, and score) in the local model of the specified view. Additionally,
     * it increases the arrived messages count if the sender is rejoining the game.
     *
     * @param view The view on which to execute the updated board message.
     */
    @Override
    public void executeMessage(View view) {
        if( !rejoining | (rejoining & !view.getLocalModel().getPlayersBoards().containsKey(senderNickname)) )
            view.getLocalModel().setLocalBoard(senderNickname,cardBoard,cardList,score,anglesCounter);
        else if(rejoining & view.getLocalModel().getPlayersBoards().get(senderNickname).getCardList().isEmpty())
            view.getLocalModel().setLocalBoard(senderNickname,cardBoard,cardList,score,anglesCounter);
        if(rejoining)
            view.getLocalModel().increaseArrivedMessages();
    }
}
