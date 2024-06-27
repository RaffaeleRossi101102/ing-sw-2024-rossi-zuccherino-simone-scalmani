package SoftEng_2024.Model.ModelMessages;

import SoftEng_2024.Model.Enums.GameState;
import SoftEng_2024.View.View;

import java.util.List;
/**
 * Represents an updated message for announcing winners to be executed by the view.
 * Extends {@link ModelMessage}.
 */
public class UpdatedWinnersMessage extends ModelMessage{

    List<String> winnersNickname;

    /**
     * Constructs an UpdatedWinnersMessage with the specified message and list of winners' nicknames.
     *
     * @param message The message content.
     * @param winnersNickname The list of winners' nicknames to update.
     */
    public UpdatedWinnersMessage( String message,List<String> winnersNickname) {
        super(0, message, "game");
        this.winnersNickname=winnersNickname;

    }

    /**
     * Executes the updated winners message on the provided view.
     * Sets the winners' nicknames and updates the game state to ENDGAME in the local model.
     *
     * @param view The view on which to execute the updated winners message.
     */
    @Override
    public void executeMessage(View view) {
        view.getLocalModel().setWinnersNickname(winnersNickname);
        view.getLocalModel().setGameState(GameState.ENDGAME);
        if (rejoining)
            view.getLocalModel().increaseArrivedMessages();
    }

}
