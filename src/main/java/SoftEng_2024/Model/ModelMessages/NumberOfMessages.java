package SoftEng_2024.Model.ModelMessages;

import SoftEng_2024.View.View;
/**
 * NumberOfMessages class represents a specific type of ModelMessage used for setting
 * the number of messages and increasing the count of arrived messages in the local model
 * of a specified view.
 * <p>
 * This class extends ModelMessage and provides functionality to handle the count of
 * messages received.
 */
public class NumberOfMessages extends ModelMessage{
    int numberOfMessages;
    /**
     * Constructs a NumberOfMessages with a receiver ID, message content, sender's nickname,
     * and the number of messages.
     *
     * @param ID The receiver's ID to whom the message is intended.
     * @param message The content of the message.
     * @param senderNickname The nickname of the sender of the message.
     * @param numberOfMessages The number of messages to be set in the local model.
     */
    public NumberOfMessages(double ID, String message, String senderNickname,int numberOfMessages) {
        super(ID, message, senderNickname);
        this.numberOfMessages=numberOfMessages;
    }

    /**
     * Executes the message by setting the number of messages and increasing the arrived messages count
     * in the local model of the specified view.
     *
     * @param view The view on which to execute the message.
     */
    @Override
    public void executeMessage(View view) {
        view.getLocalModel().setNumberOfMessages(numberOfMessages);
        view.getLocalModel().increaseArrivedMessages();
    }
}
