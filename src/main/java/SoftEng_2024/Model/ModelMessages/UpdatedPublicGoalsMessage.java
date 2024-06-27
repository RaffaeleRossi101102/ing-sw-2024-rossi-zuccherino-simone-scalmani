package SoftEng_2024.Model.ModelMessages;

import SoftEng_2024.Model.GoalCard.GoalCard;
import SoftEng_2024.View.View;
/**
 * Represents an updated message for the public goals to be executed by the view.
 * Extends {@link ModelMessage}.
 */
public class UpdatedPublicGoalsMessage extends ModelMessage{

    GoalCard[] publicGoals;

    /**
     * Constructs an UpdatedPublicGoalsMessage with the specified message and array of public goals.
     *
     * @param message The message content.
     * @param publicGoals The array of public goals to update.
     */
    public UpdatedPublicGoalsMessage(String message, GoalCard[] publicGoals) {
        super(0, message, "game");
        this.publicGoals=publicGoals;
    }

    /**
     * Executes the updated public goals message on the provided view.
     * Updates the local model's public goals if not rejoining or if the current public goals are empty.
     *
     * @param view The view on which to execute the updated public goals message.
     */
    @Override
    public void executeMessage(View view) {
        if(!rejoining | (rejoining & view.getLocalModel().getPublicGoals().isEmpty()))
            view.getLocalModel().setPublicGoals(publicGoals);
        if(rejoining)
            view.getLocalModel().increaseArrivedMessages();
    }
}
