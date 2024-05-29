package SoftEng_2024.Model.ModelMessages;

import SoftEng_2024.Model.Cards.Card;
import SoftEng_2024.View.View;

import java.util.List;

public class UpdatedHandMessage extends ModelMessage{
    List<Card> playerHand;
    public UpdatedHandMessage(double ID, String message, List<Card> playerHand) {
        super(ID, message);
        this.playerHand=playerHand;
    }

    @Override
    public void executeMessage(View view) {
        view.getLocalModel().setPersonalHand(playerHand);
    }
}
