package SoftEng_2024.Model.ModelMessages;

import SoftEng_2024.View.View;
/**
 * LastPlayerStandingMessage class represents a specific type of ModelMessage used for notifying
 * that a single player remains as the last player standing in the game.
 * <p>
 * This class extends ModelMessage and overrides the executeMessage method to print a
 * message indicating that the last player is standing to the error console.
 */
public class LastPlayerStandingMessage extends ModelMessage {
    /**
     * Constructs a LastPlayerStandingMessage with a message indicating the last player standing.
     *
     * @param message The message indicating that a single player remains as the last player standing.
     */
    public LastPlayerStandingMessage(String message) {
        super(0, message, "game");

    }
    /**
     * Executes the last player standing message by printing the message to the error console.
     *
     * @param view The view on which to execute the last player standing message (not used in this implementation).
     */
    @Override
    public void executeMessage(View view) {
        System.err.println("\n\n" + message + "\n\n");
        view.getLocalModel().setQuitAll(true);
    }
}
