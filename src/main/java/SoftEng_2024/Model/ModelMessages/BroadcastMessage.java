package SoftEng_2024.Model.ModelMessages;

import SoftEng_2024.View.View;

/**
 * BroadcastMessage class represents a specific type of ModelMessage used for broadcasting
 * messages to all connected views.
 * <p>
 * This class extends ModelMessage and overrides the executeMessage method to add the
 * broadcast message to the chat of a specified view.
 */
public class BroadcastMessage extends ModelMessage{

    /**
     * Constructs a BroadcastMessage with a unique ID and a message to broadcast.
     *
     * @param ID The unique identifier of the broadcast message.
     * @param message The message content to be broadcasted.
     */
    public BroadcastMessage(double ID, String message) {
        super(ID, message,"controller");
    }

    /**
     * Executes the broadcast message by adding the message content to the chat of
     * the provided view.
     *
     * @param view The view on which to execute the broadcast message.
     */
    @Override
    public void executeMessage(View view) {
        view.getLocalModel().addToChat(message);
    }
}
