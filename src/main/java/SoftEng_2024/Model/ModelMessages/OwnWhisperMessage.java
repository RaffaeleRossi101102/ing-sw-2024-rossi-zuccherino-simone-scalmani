package SoftEng_2024.Model.ModelMessages;

import SoftEng_2024.View.View;

import java.time.LocalDateTime;

public class OwnWhisperMessage extends ModelMessage{

    String time;
    public OwnWhisperMessage(double ID, String message, String whisperedNickname, String time) {
        super(ID, message, whisperedNickname);
        this.time=time;
    }

    @Override
    public void executeMessage(View view) {
        String ownMessage= "[" + time + "]" + " " +  "you have whispered to " + senderNickname+": " +message;
        view.getLocalModel().addToChat(ownMessage);
    }
}
