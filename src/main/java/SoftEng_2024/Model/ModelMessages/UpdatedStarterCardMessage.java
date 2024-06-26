package SoftEng_2024.Model.ModelMessages;

import SoftEng_2024.Model.Cards.StarterCard;
import SoftEng_2024.View.View;

public class UpdatedStarterCardMessage extends ModelMessage {
    StarterCard starterCard;
    public UpdatedStarterCardMessage(double ID, StarterCard starterCard) {
        super(ID, "", "");
        this.starterCard = starterCard;
    }

    @Override
    public void executeMessage(View view) {
        view.getLocalModel().setStarterCard(starterCard);
        if(rejoining)
            view.getLocalModel().increaseArrivedMessages();
    }
}
