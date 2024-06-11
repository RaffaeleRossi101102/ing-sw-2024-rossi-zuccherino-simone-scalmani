package SoftEng_2024.Model.ModelMessages;

import SoftEng_2024.Model.Enums.Angles;
import SoftEng_2024.View.View;

public class UpdatedGoldDeckMessage extends ModelMessage{
    private Angles topGoldCard;
    public UpdatedGoldDeckMessage(String message, Angles topGoldCard) {
        super(0, message, "Game");
        this.topGoldCard=topGoldCard;
    }

    @Override
    public void executeMessage(View view) {
        if(!rejoining)
            view.getLocalModel().setTopGoldCard(topGoldCard);
        else if (view.getLocalModel().getTopGoldCard()==null) {
            view.getLocalModel().setTopGoldCard(topGoldCard);
        }
    }
}
