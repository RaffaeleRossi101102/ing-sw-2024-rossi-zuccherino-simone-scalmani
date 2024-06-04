package SoftEng_2024.Model.ModelMessages;

import SoftEng_2024.Model.GoalCard.GoalCard;
import SoftEng_2024.View.View;

public class UpdatedPublicGoals extends ModelMessage{
    GoalCard[] publicGoals;
    public UpdatedPublicGoals(String message,GoalCard[] publicGoals) {
        super(0, message, "game");
        this.publicGoals=publicGoals;
    }

    @Override
    public void executeMessage(View view) {

    }
}
