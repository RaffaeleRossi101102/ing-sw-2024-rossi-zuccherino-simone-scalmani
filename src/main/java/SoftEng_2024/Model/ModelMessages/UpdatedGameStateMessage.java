package SoftEng_2024.Model.ModelMessages;

import SoftEng_2024.Model.Enums.GameState;
import SoftEng_2024.View.View;
/**
 * Represents an updated game state message to be executed by the view.
 * Extends {@link ModelMessage}.
 */
public class UpdatedGameStateMessage extends ModelMessage{

    GameState gameState;

    /**
     * Constructs an UpdatedGameStateMessage with the specified message and game state.
     *
     * @param message The message associated with the game state update.
     * @param gameState The new game state.
     */
    public UpdatedGameStateMessage( String message, GameState gameState) {
        super(0, message, "game");
        this.gameState=gameState;
    }

    /**
     * Executes the game state update message on the provided view.
     * Depending on conditions, updates the game state and increments arrived messages.
     *
     * @param view The view on which to execute the game state update message.
     */
    @Override
    public void executeMessage(View view) {
        if(!rejoining | (rejoining & view.getLocalModel().getGameState().equals(GameState.CONNECTION)))
            view.getLocalModel().setGameState(gameState);
        if(rejoining)
            view.getLocalModel().increaseArrivedMessages();
    }
}
