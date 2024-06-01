package SoftEng_2024.Model.ModelMessages;

import SoftEng_2024.View.View;

public class UpdatedIsOnlineMessage extends ModelMessage{
    boolean isPlayerOnline;
    public UpdatedIsOnlineMessage(double ID, String message,boolean isPlayerOnline) {
        super(ID, message);
        this.isPlayerOnline=isPlayerOnline;
    }

    @Override
    public void executeMessage(View view) {
//        view.getLocalModel().setOnline
    }
}
