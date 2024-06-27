package SoftEng_2024.Model.ModelMessages;

import SoftEng_2024.Model.Cards.Card;
import SoftEng_2024.View.View;

import java.util.List;
/**
 * Represents an updated message for the public cards to be executed by the view.
 * Extends {@link ModelMessage}.
 */
public class UpdatedPublicCardsMessage extends ModelMessage{

    private List<Card> publicCards;

    /**
     * Constructs an UpdatedPublicCardsMessage with the specified message and list of public cards.
     *
     * @param message The message content.
     * @param publicCards The list of public cards to update.
     */
    public UpdatedPublicCardsMessage(String message,List<Card> publicCards) {
        super(0, message, "game");
        this.publicCards=publicCards;
    }

    /**
     * Executes the updated public cards message on the provided view.
     * Updates the local model's public cards if not rejoining or if the current public cards are empty.
     *
     * @param view The view on which to execute the updated public cards message.
     */
    @Override
    public void executeMessage(View view) {
        if(!rejoining | (rejoining & view.getLocalModel().getPublicCards().isEmpty()))
         view.getLocalModel().setPublicCards(publicCards);
        if(rejoining)
            view.getLocalModel().increaseArrivedMessages();
    }
}
