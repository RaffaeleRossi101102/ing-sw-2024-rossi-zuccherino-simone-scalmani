package SoftEng_2024.Model.ModelMessages;

import SoftEng_2024.View.View;


public class WhisperMessage extends ModelMessage{
    private final String time;


    public WhisperMessage(double ID, String message, String senderNickname, String time) {
        super(ID, message, senderNickname);
        this.time=time;

    }

    @Override
    public void executeMessage(View view) {
        String receiverMessage="[" + time + "]" + " " + senderNickname + " has whispered to you: " +message;
        view.getLocalModel().addToChat(receiverMessage);
    }
}
