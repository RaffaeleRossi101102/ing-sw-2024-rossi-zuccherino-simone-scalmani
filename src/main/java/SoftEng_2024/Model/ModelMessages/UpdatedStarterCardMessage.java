package SoftEng_2024.Model.ModelMessages;

import SoftEng_2024.Model.Cards.StarterCard;
import SoftEng_2024.View.View;

/**
 * UpdatedStarterCardMessage is a type of ModelMessage that represents an update to the starter card.
 */
public class UpdatedStarterCardMessage extends ModelMessage {

    StarterCard starterCard;

    /**
     * Constructs an UpdatedStarterCardMessage with a receiver ID and the updated starter card.
     *
     * @param ID The receiver's ID to whom the message is intended.
     * @param starterCard The updated starter card.
     */
    public UpdatedStarterCardMessage(double ID, StarterCard starterCard) {
        super(ID, "", "");
        this.starterCard = starterCard;
    }

    /**
     * Executes the specific action associated with this message on a given view.
     * It updates the local model's starter card and, if rejoining, increases the count of arrived messages.
     *
     * @param view The view on which to execute the message.
     */
    @Override
    public void executeMessage(View view) {
        view.getLocalModel().setStarterCard(starterCard);
        if(rejoining)
            view.getLocalModel().increaseArrivedMessages();
    }
}
