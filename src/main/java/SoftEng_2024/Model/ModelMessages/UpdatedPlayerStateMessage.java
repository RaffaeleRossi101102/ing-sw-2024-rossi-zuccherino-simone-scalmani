package SoftEng_2024.Model.ModelMessages;

import SoftEng_2024.Model.Enums.GameState;
import SoftEng_2024.View.View;
/**
 * Represents an updated message for the player's state (game state) to be executed by the view.
 * Extends {@link ModelMessage}.
 */
public class UpdatedPlayerStateMessage extends ModelMessage{

    GameState playerState;

    /**
     * Constructs an UpdatedPlayerStateMessage with the specified ID, message, game state, and sender's nickname.
     *
     * @param ID The ID associated with the message.
     * @param message The message content.
     * @param gs The game state of the player.
     * @param senderNickname The nickname of the sender.
     */
    public UpdatedPlayerStateMessage(double ID, String message,GameState gs,String senderNickname) {
        super(ID, message,senderNickname);
        playerState=gs;
    }

    /**
     * Executes the updated player state message on the provided view.
     * Updates the local model's player state based on the provided game state.
     *
     * @param view The view on which to execute the updated player state message.
     */
    @Override
    public void executeMessage(View view) {
        view.getLocalModel().setPlayerState(playerState);
        if(rejoining)
            view.getLocalModel().increaseArrivedMessages();
    }
}
