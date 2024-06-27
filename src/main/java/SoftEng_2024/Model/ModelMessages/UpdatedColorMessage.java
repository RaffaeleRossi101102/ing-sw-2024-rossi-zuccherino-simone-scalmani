package SoftEng_2024.Model.ModelMessages;

import SoftEng_2024.Model.Enums.Color;
import SoftEng_2024.View.View;

import java.util.List;
/**
 * UpdatedColorMessage class represents a specific type of ModelMessage used for updating
 * the color of a player in a game.
 * <p>
 * This class extends ModelMessage and overrides the executeMessage method to update the
 * color of a player in the local model of a specified view.
 */
public class UpdatedColorMessage extends ModelMessage{

    /**
     * The color to be updated for the player.
     */
    private final Color playerColor;

    /**
     * Constructs an UpdatedColorMessage with a message content, player's color, and sender's nickname.
     *
     * @param message The content of the message.
     * @param playerColor The color to be updated for the player.
     * @param senderNickname The nickname of the sender of the message.
     */
    public UpdatedColorMessage( String message,Color playerColor,String senderNickname) {
        super(0, message,senderNickname);
        this.playerColor=playerColor;
    }

    /**
     * Executes the updated color message by setting the player's color in the local model of
     * the specified view. Additionally, it increases the arrived messages count if the sender
     * is rejoining the game and their color is being updated for the first time.
     *
     * @param view The view on which to execute the updated color message.
     */
    @Override
    public void executeMessage(View view) {
        if(!rejoining | (rejoining & !view.getLocalModel().getPlayersColor().containsKey(senderNickname)))
            view.getLocalModel().setPlayersColor(senderNickname,playerColor);
        if(rejoining)
            view.getLocalModel().increaseArrivedMessages();
    }
}
