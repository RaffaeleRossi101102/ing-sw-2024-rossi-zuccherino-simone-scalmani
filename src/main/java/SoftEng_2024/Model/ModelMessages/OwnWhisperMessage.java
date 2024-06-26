package SoftEng_2024.Model.ModelMessages;

import SoftEng_2024.View.View;

import java.time.LocalDateTime;
/**
 * OwnWhisperMessage class represents a specific type of ModelMessage used for indicating
 * a whisper message sent by the local user.
 * <p>
 * This class extends ModelMessage and overrides the executeMessage method to add the
 * own whisper message to the chat of a specified view's local model.
 */
public class OwnWhisperMessage extends ModelMessage{
    /**
     * The time at which the whisper message was sent.
     */
    String time;

    /**
     * Constructs an OwnWhisperMessage with a receiver ID, message content, whispered nickname,
     * and the time of the whisper.
     *
     * @param ID The receiver's ID to whom the message is intended.
     * @param message The content of the whisper message.
     * @param whisperedNickname The nickname of the recipient of the whisper.
     * @param time The time at which the whisper message was sent.
     */

    public OwnWhisperMessage(double ID, String message, String whisperedNickname, String time) {
        super(ID, message, whisperedNickname);
        this.time=time;
    }

    /**
     * Executes the own whisper message by formatting it with the time and sender information,
     * then adding it to the chat of the specified view's local model.
     *
     * @param view The view on which to execute the own whisper message.
     */
    @Override
    public void executeMessage(View view) {
        String ownMessage= "[" + time + "]" + " " +  "you have whispered to " + senderNickname+": " +message;
        view.getLocalModel().addToChat(ownMessage);
    }
}
