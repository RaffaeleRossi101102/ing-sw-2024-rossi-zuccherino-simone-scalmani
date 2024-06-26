package SoftEng_2024.Model.ModelMessages;

import SoftEng_2024.View.View;

/**
 * Represents a whisper message to be executed by the view.
 * Extends {@link ModelMessage}.
 */
public class WhisperMessage extends ModelMessage{
    private final String time;

    /**
     * Constructs a WhisperMessage with the specified ID, message content, sender's nickname, and timestamp.
     *
     * @param ID The ID associated with the message.
     * @param message The message content.
     * @param senderNickname The nickname of the sender.
     * @param time The timestamp of the message.
     */
    public WhisperMessage(double ID, String message, String senderNickname, String time) {
        super(ID, message, senderNickname);
        this.time=time;

    }

    /**
     * Executes the whisper message on the provided view.
     * Constructs a formatted message with timestamp, sender's nickname, and content, then adds it to the chat in the local model.
     *
     * @param view The view on which to execute the whisper message.
     */
    @Override
    public void executeMessage(View view) {
        String receiverMessage="[" + time + "]" + " " + senderNickname + " has whispered to you: " +message;
        view.getLocalModel().addToChat(receiverMessage);
    }
}
