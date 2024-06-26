package SoftEng_2024.View.ViewMessages;

import SoftEng_2024.Controller.GameController;
import SoftEng_2024.Model.Enums.Color;
/**
 * Represents a message that instructs the GameController to set a player's color.
 */
public class SetColorMessage implements ViewMessage {

    Color color;
    double ID;
    /**
     * Constructs a SetColorMessage with the specified color and player ID.
     *
     * @param color The color to set for the player.
     * @param ID    The ID of the player whose color is being set.
     */
    public SetColorMessage(Color color,double ID){
        this.color=color;
        this.ID=ID;
    }
    /**
     * Executes the operation to set the color for a player using the GameController.
     *
     * @param controller The GameController instance on which to execute the operation.
     */
    @Override
    public void executeMessage(GameController controller){
        controller.setColor(this.color,this.ID);
    }
}
