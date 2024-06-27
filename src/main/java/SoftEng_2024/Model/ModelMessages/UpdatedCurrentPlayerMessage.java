package SoftEng_2024.Model.ModelMessages;

import SoftEng_2024.View.View;
/**
 * UpdatedCurrentPlayerMessage class represents a specific type of ModelMessage used for updating
 * the current player in a game.
 * <p>
 * This class extends ModelMessage and overrides the executeMessage method to update the
 * current turn player's nickname in the local model of a specified view.
 */
public class UpdatedCurrentPlayerMessage extends ModelMessage{

    /**
     * The nickname of the current player to be updated.
     */
    String currentPlayerNickname;

    /**
     * Constructs an UpdatedCurrentPlayerMessage with a message content and the nickname
     * of the current player.
     *
     * @param message The content of the message.
     * @param currentPlayerNickname The nickname of the current player.
     */
    public UpdatedCurrentPlayerMessage( String message, String currentPlayerNickname) {
        super(0, message, "game");
        this.currentPlayerNickname=currentPlayerNickname;
    }

    /**
     * Executes the updated current player message by setting the current turn player's
     * nickname in the local model of the specified view. Additionally, it increases the
     * arrived messages count if the sender is rejoining the game and the current turn
     * player nickname is being set for the first time.
     *
     * @param view The view on which to execute the updated current player message.
     */
    @Override
    public void executeMessage(View view) {
        if (!rejoining | (rejoining & view.getLocalModel().getCurrentTurnPlayerNickname()==null))
            view.getLocalModel().setCurrentTurnPlayerNickname(currentPlayerNickname);
        if(rejoining)
            view.getLocalModel().increaseArrivedMessages();
    }
}
