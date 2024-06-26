package SoftEng_2024.Model.ModelMessages;

import SoftEng_2024.Model.Cards.Card;
import SoftEng_2024.Model.Enums.Angles;
import SoftEng_2024.View.View;

/**
 * Represents an updated message for the top resource card in the resource deck to be executed by the view.
 * Extends {@link ModelMessage}.
 */
public class UpdatedResourceDeckMessage extends ModelMessage{

    private Angles topResource;

    /**
     * Constructs an UpdatedResourceDeckMessage with the specified message and top resource card.
     *
     * @param message The message content.
     * @param topResource The top resource card to update.
     */
    public UpdatedResourceDeckMessage(String message, Angles topResource) {
        super(0, message, "Game");
        this.topResource=topResource;
    }

    /**
     * Executes the updated resource deck message on the provided view.
     * Updates the local model's top resource card if not rejoining or if the current top resource card is null.
     *
     * @param view The view on which to execute the updated resource deck message.
     */
    @Override
    public void executeMessage(View view) {
        if(!rejoining | (rejoining & view.getLocalModel().getTopResourceCard()==null))
            view.getLocalModel().setTopResourceCard(topResource);
        if(rejoining)
            view.getLocalModel().increaseArrivedMessages();
    }
}
