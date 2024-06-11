package SoftEng_2024.Model.ModelMessages;

import SoftEng_2024.Model.Cards.Card;
import SoftEng_2024.Model.Enums.Angles;
import SoftEng_2024.View.View;

public class UpdatedResourceDeckMessage extends ModelMessage{
    private Angles topResource;
    public UpdatedResourceDeckMessage(String message, Angles topResource) {
        super(0, message, "Game");
        this.topResource=topResource;
    }

    @Override
    public void executeMessage(View view) {
        if(!rejoining | (rejoining & view.getLocalModel().getTopResourceCard()==null))
            view.getLocalModel().setTopResourceCard(topResource);
    }
}
