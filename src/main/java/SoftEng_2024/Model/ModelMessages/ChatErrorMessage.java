package SoftEng_2024.Model.ModelMessages;

import SoftEng_2024.View.View;

/**
 * ChatErrorMessage class represents a specific type of ModelMessage used for signaling
 * errors related to chat functionality.
 * <p>
 * This class extends ModelMessage and overrides the executeMessage method to set a chat
 * error message in the local model of a specified view.
 */
public class ChatErrorMessage extends ModelMessage{
    /**
     * Constructs a ChatErrorMessage with a unique ID and an error message.
     *
     * @param ID The unique identifier of the chat error message.
     * @param message The error message indicating the issue with chat functionality.
     */
    public ChatErrorMessage(double ID, String message) {
        super(ID, message, "controller");
    }
    /**
     * Executes the chat error message by setting the error message in the chat functionality
     * of the provided view's local model.
     *
     * @param view The view on which to execute the chat error message.
     */
    @Override
    public void executeMessage(View view) {
        view.getLocalModel().setChatError(message);
    }
}
