package SoftEng_2024.Model.ModelMessages;

import SoftEng_2024.View.View;

/**
 * GameIsEndingMessage class represents a specific type of ModelMessage used for notifying
 * that the game is ending.
 * <p>
 * This class extends ModelMessage and overrides the executeMessage method to print a
 * message indicating that the game is ending to the console.
 */
public class GameIsEndingMessage extends ModelMessage{

    /**
     * Constructs a GameIsEndingMessage with a message indicating that the game is ending.
     *
     * @param message The message indicating that the game is ending.
     */
    public GameIsEndingMessage(String message) {
        super(0,message ,"game");
    }

    /**
     * Executes the game ending message by printing the message to the console.
     *
     * @param view The view on which to execute the game ending message (not used in this implementation).
     */
    @Override
    public void executeMessage(View view) {

        System.out.println("\n\n" + message+"\n\n");

    }
}
