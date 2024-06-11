package SoftEng_2024.Model.ModelMessages;

import SoftEng_2024.Model.Cards.Card;
import SoftEng_2024.View.View;

import java.util.List;

public class UpdatedPublicCardsMessage extends ModelMessage{
    private List<Card> publicCards;
    public UpdatedPublicCardsMessage(String message,List<Card> publicCards) {
        super(0, message, "game");
        this.publicCards=publicCards;
    }

    @Override
    public void executeMessage(View view) {
        if(!rejoining | (rejoining & view.getLocalModel().getPublicCards().isEmpty()))
         view.getLocalModel().setPublicCards(publicCards);
    }
}
