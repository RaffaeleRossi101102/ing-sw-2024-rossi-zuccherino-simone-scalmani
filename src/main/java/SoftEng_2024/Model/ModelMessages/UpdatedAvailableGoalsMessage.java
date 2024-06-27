package SoftEng_2024.Model.ModelMessages;

import SoftEng_2024.Model.GoalCard.GoalCard;
import SoftEng_2024.View.View;
import java.util.List;
/**
 * UpdatedAvailableGoalsMessage class represents a specific type of ModelMessage used for updating
 * the list of available goal cards in a game.
 * <p>
 * This class extends ModelMessage and overrides the executeMessage method to update the
 * available goal cards in the local model of a specified view.
 */
public class UpdatedAvailableGoalsMessage extends ModelMessage{

    /**
     * The list of available goal cards to be updated.
     */
    List<GoalCard> availableGoals;

    /**
     * Constructs an UpdatedAvailableGoalsMessage with a receiver ID, message content,
     * list of available goal cards, and sender's nickname.
     *
     * @param ID The receiver's ID to whom the message is intended.
     * @param message The content of the message.
     * @param availableGoals The list of available goal cards to be updated.
     * @param senderNickname The nickname of the sender of the message.
     */
    public UpdatedAvailableGoalsMessage(double ID, String message,List<GoalCard> availableGoals,String senderNickname) {
        super(ID, message,senderNickname);
        this.availableGoals=availableGoals;
    }


    /**
     * Executes the updated available goals message by setting the list of available goal cards
     * and optionally increasing the arrived messages count in the local model of the specified view
     * if the sender is rejoining the game.
     *
     * @param view The view on which to execute the updated available goals message.
     */
    @Override
    public void executeMessage(View view) {
        view.getLocalModel().setAvailableGoals(availableGoals);
        if(rejoining)
            view.getLocalModel().increaseArrivedMessages();
    }
}
