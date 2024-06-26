package SoftEng_2024.Model.ModelMessages;

import SoftEng_2024.Model.Cards.Card;
import SoftEng_2024.Model.Cards.StarterCard;
import SoftEng_2024.View.View;

import java.util.List;
/**
 * Represents an updated message for the player's hand to be executed by the view.
 * Extends {@link ModelMessage}.
 */
public class UpdatedHandMessage extends ModelMessage{

    List<Card> playerHand;

    /**
     * Constructs an UpdatedHandMessage with the specified ID, message, player hand, and sender's nickname.
     *
     * @param ID The ID associated with the message.
     * @param message The message content.
     * @param playerHand The updated player's hand.
     * @param senderNickname The nickname of the sender.
     */
    public UpdatedHandMessage(double ID, String message, List<Card> playerHand,String senderNickname) {
        super(ID, message,senderNickname);
        this.playerHand=playerHand;
    }

    /**
     * Executes the updated hand message on the provided view.
     * Updates the local model's hand state based on conditions, including handling hidden cards and starter cards.
     *
     * @param view The view on which to execute the updated hand message.
     */
    @Override
    public void executeMessage(View view) {
        //se la mano non è la mia, quindi almeno una carta è nascosta, setto i valori nella mano degli altri
        if(!rejoining) {
            if (playerHand.get(0).getFront().getHidden())
                view.getLocalModel().setOtherPlayersHand(senderNickname, playerHand);
            else
                view.getLocalModel().setPersonalHand(playerHand);
        } else{
            if(playerHand.get(0).getFront().getHidden()){
                if(!view.getLocalModel().getOtherPlayersHand().containsKey(senderNickname))
                    view.getLocalModel().setOtherPlayersHand(senderNickname, playerHand);
                else if(view.getLocalModel().getOtherPlayersHand().get(senderNickname).isEmpty())
                    view.getLocalModel().setOtherPlayersHand(senderNickname, playerHand);
            }
            else if (!playerHand.get(0).getFront().getHidden() & view.getLocalModel().getPersonalHand().isEmpty()) {
                view.getLocalModel().setPersonalHand(playerHand);
            }
            view.getLocalModel().increaseArrivedMessages();
        }

    }
}
