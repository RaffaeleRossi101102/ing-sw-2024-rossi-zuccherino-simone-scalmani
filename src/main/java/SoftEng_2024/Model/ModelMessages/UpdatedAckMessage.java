package SoftEng_2024.Model.ModelMessages;

import SoftEng_2024.View.View;
/**
 * UpdatedAckMessage class represents a specific type of ModelMessage used for updating
 * acknowledgement status in the context of a game.
 * <p>
 * This class extends ModelMessage and overrides the executeMessage method to update
 * acknowledgment status in the local model of a specified view.
 */
public class UpdatedAckMessage extends ModelMessage{

    /**
     * The acknowledgement status to be updated.
     */
    boolean ack;

    /**
     * Constructs an UpdatedAckMessage with a receiver ID, message content, acknowledgement status.
     *
     * @param ID The receiver's ID to whom the message is intended.
     * @param message The content of the message.
     * @param ack The acknowledgement status to be updated.
     */
    public UpdatedAckMessage(double ID, String message, boolean ack) {
        super(ID, message, "game");
        this.ack=ack;
    }

    /**
     * Executes the updated acknowledgment message by setting the acknowledgment status
     * and marking acknowledgment received in the local model of the specified view.
     *
     * @param view The view on which to execute the updated acknowledgment message.
     */
    @Override
    public void executeMessage(View view) {
        view.getLocalModel().setAckSuccessful(ack);
        view.getLocalModel().setAckReceived(true);

    }
}
