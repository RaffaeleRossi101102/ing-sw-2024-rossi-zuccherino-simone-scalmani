package SoftEng_2024.Model.ModelMessages;

import SoftEng_2024.Model.Enums.Angles;
import SoftEng_2024.View.View;

public class UpdatedGoldDeckMessage extends ModelMessage{
    private Angles topResourceCard;
    public UpdatedGoldDeckMessage(String message, Angles topResourceCard) {
        super(0, message, "Game");
        this.topResourceCard=topResourceCard;
    }

    @Override
    public void executeMessage(View view) {

    }
}
