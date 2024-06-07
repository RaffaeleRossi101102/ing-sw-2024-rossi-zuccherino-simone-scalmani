package SoftEng_2024.Model.ModelMessages;

import SoftEng_2024.Model.Cards.Card;
import SoftEng_2024.Model.Cards.StarterCard;
import SoftEng_2024.View.View;

import java.util.List;

public class UpdatedHandMessage extends ModelMessage{
    List<Card> playerHand;
    public UpdatedHandMessage(double ID, String message, List<Card> playerHand,String senderNickname) {
        super(ID, message,senderNickname);
        this.playerHand=playerHand;
    }

    @Override
    public void executeMessage(View view) {
        //se la mano non è la mia, quindi almeno una carta è nascosta, setto i valori nella mano degli altri
        if(playerHand.get(0).getFront().getHidden())
            view.getLocalModel().setOtherPlayersHand(senderNickname,playerHand);
        else if(playerHand.size()==1)
            view.getLocalModel().setStarterCard((StarterCard) playerHand.get(0));
        else
            view.getLocalModel().setPersonalHand(playerHand);
    }
}
