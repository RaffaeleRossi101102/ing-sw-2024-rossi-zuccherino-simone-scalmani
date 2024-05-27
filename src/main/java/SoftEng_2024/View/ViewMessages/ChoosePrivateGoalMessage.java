package SoftEng_2024.View.ViewMessages;

import SoftEng_2024.Controller.GameController;

public class ChoosePrivateGoalMessage implements ViewMessage {
    int goalIndex;
    double ID;

    public ChoosePrivateGoalMessage(int goalIndex, double ID) {
        this.goalIndex = goalIndex;
        this.ID = ID;
    }

    @Override
    public void executeMessage(GameController controller) {
        controller.choosePrivateGoals(goalIndex, ID);
    }
}
