package SoftEng_2024.Model.ModelMessages;

import SoftEng_2024.View.View;

public class NumberOfMessages extends ModelMessage{
    int numberOfMessages;
    public NumberOfMessages(double ID, String message, String senderNickname,int numberOfMessages) {
        super(ID, message, senderNickname);
        this.numberOfMessages=numberOfMessages;
    }

    @Override
    public void executeMessage(View view) {
        view.getLocalModel().setNumberOfMessages(numberOfMessages);
        view.getLocalModel().increaseArrivedMessages();
    }
}
