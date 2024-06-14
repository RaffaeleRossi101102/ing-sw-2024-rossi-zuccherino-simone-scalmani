package SoftEng_2024.Model.ModelMessages;

import SoftEng_2024.Model.GoalCard.GoalCard;
import SoftEng_2024.View.View;
import java.util.List;

public class UpdatedAvailableGoalsMessage extends ModelMessage{
    List<GoalCard> availableGoals;
    public UpdatedAvailableGoalsMessage(double ID, String message,List<GoalCard> availableGoals,String senderNickname) {
        super(ID, message,senderNickname);
        this.availableGoals=availableGoals;
    }

    @Override
    public void executeMessage(View view) {
        view.getLocalModel().setAvailableGoals(availableGoals);
        if(rejoining)
            view.getLocalModel().increaseArrivedMessages();
    }
}
