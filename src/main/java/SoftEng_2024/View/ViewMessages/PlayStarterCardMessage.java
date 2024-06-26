package SoftEng_2024.View.ViewMessages;

import SoftEng_2024.Controller.GameController;
/**
 * Represents a message that instructs the GameController to play a starter card.
 */
public class PlayStarterCardMessage implements ViewMessage {

    boolean flipped;
    double ID;
    /**
     * Constructs a PlayStarterCardMessage with the specified parameters.
     *
     * @param flipped Indicates whether the starter card should be flipped.
     * @param ID      The ID of the player playing the starter card.
     */
    public PlayStarterCardMessage(boolean flipped, double ID){
        this.flipped=flipped;
        this.ID=ID;
    }
    /**
     * Executes the operation to play a starter card using the GameController.
     *
     * @param controller The GameController instance on which to execute the operation.
     */
    @Override
    public void executeMessage(GameController controller) {
        controller.playStarterCard(flipped,ID);
    }
}
