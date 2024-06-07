package SoftEng_2024.Model.ModelMessages;

import SoftEng_2024.Model.Cards.Card;
import SoftEng_2024.Model.Enums.Angles;
import SoftEng_2024.View.View;

public class UpdatedResourceDeckMessage extends ModelMessage{
    private Angles topResourceCard;
    public UpdatedResourceDeckMessage(String message, Angles topResourceCard) {
        super(0, message, "Game");
        this.topResourceCard=topResourceCard;
    }

    @Override
    public void executeMessage(View view) {

    }
}
