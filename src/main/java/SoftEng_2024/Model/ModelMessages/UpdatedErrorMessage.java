package SoftEng_2024.Model.ModelMessages;

import SoftEng_2024.View.View;
/**
 * Represents an updated error message to be executed by the view.
 * Extends {@link ModelMessage}.
 */
public class UpdatedErrorMessage extends ModelMessage{

    /**
     * Constructs an UpdatedErrorMessage with the specified ID and message.
     *
     * @param ID The ID associated with the error message.
     * @param message The error message content.
     */
    public UpdatedErrorMessage(double ID, String message ) {
        super(ID, message, "game");
    }

    /**
     * Executes the error message on the provided view.
     * Sets the error message in the local model of the view.
     *
     * @param view The view on which to execute the error message.
     */
    @Override
    public void executeMessage(View view) {
       // System.err.println(message);
        view.getLocalModel().setErrorLog(message);
    }

}
