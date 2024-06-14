package SoftEng_2024.Model.ModelMessages;

import SoftEng_2024.Model.GoalCard.GoalCard;
import SoftEng_2024.View.View;

public class UpdatedPublicGoalsMessage extends ModelMessage{
    GoalCard[] publicGoals;
    public UpdatedPublicGoalsMessage(String message, GoalCard[] publicGoals) {
        super(0, message, "game");
        this.publicGoals=publicGoals;
    }

    @Override
    public void executeMessage(View view) {
        if(!rejoining | (rejoining & view.getLocalModel().getPublicGoals().isEmpty()))
            view.getLocalModel().setPublicGoals(publicGoals);
        if(rejoining)
            view.getLocalModel().increaseArrivedMessages();
    }
}
