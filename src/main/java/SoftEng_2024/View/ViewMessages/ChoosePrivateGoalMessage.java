package SoftEng_2024.View.ViewMessages;

import SoftEng_2024.Controller.GameController;
/**
 * Represents a message that instructs the GameController to choose a private goal for a player.
 */
public class ChoosePrivateGoalMessage implements ViewMessage {
    int goalIndex;
    double ID;
    /**
     * Constructs a ChoosePrivateGoalMessage with the specified goal index and player ID.
     *
     * @param goalIndex The index of the private goal to choose.
     * @param ID        The ID of the player choosing the private goal.
     */
    public ChoosePrivateGoalMessage(int goalIndex, double ID) {
        this.goalIndex = goalIndex;
        this.ID = ID;
    }
    /**
     * Executes the operation to choose a private goal on the GameController.
     *
     * @param controller The GameController instance on which to execute the operation.
     */
    @Override
    public void executeMessage(GameController controller) {
        controller.choosePrivateGoals(goalIndex, ID);
    }
}
