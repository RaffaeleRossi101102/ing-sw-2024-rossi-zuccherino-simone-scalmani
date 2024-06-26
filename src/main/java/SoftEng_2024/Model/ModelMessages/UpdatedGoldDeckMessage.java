package SoftEng_2024.Model.ModelMessages;

import SoftEng_2024.Model.Enums.Angles;
import SoftEng_2024.View.View;
/**
 * Represents an updated message for the top gold card in the deck to be executed by the view.
 * Extends {@link ModelMessage}.
 */
public class UpdatedGoldDeckMessage extends ModelMessage{

    private Angles topGoldCard;

    /**
     * Constructs an UpdatedGoldDeckMessage with the specified message and top gold card angle.
     *
     * @param message The message associated with the top gold card update.
     * @param topGoldCard The angle of the new top gold card.
     */
    public UpdatedGoldDeckMessage(String message, Angles topGoldCard) {
        super(0, message, "Game");
        this.topGoldCard=topGoldCard;
    }

    /**
     * Executes the top gold card update message on the provided view.
     * Depending on conditions, updates the top gold card in the local model and increments arrived messages.
     *
     * @param view The view on which to execute the top gold card update message.
     */
    @Override
    public void executeMessage(View view) {
        if(!rejoining | (rejoining & view.getLocalModel().getTopGoldCard()==null))
            view.getLocalModel().setTopGoldCard(topGoldCard);
        if(rejoining)
            view.getLocalModel().increaseArrivedMessages();
    }
}
