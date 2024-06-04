package SoftEng_2024.Model.ModelMessages;

import SoftEng_2024.View.View;

public class UpdatedIsOnlineMessage extends ModelMessage{
    boolean isPlayerOnline;
    public UpdatedIsOnlineMessage(double ID, String message,boolean isPlayerOnline,String senderNickname) {
        super(ID, message,senderNickname);
        this.isPlayerOnline=isPlayerOnline;
    }

    @Override
    public void executeMessage(View view) {
//        view.getLocalModel().setOnline
    }
}
