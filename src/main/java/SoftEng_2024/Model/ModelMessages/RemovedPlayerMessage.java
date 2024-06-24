package SoftEng_2024.Model.ModelMessages;

import SoftEng_2024.View.View;

public class RemovedPlayerMessage extends ModelMessage{
    String removedPlayerNickname;
    public RemovedPlayerMessage( String message,String removedPlayerNickname) {
        super(0, message,"game");
        this.removedPlayerNickname=removedPlayerNickname;
    }

    @Override
    public void executeMessage(View view) {
        view.getLocalModel().getPlayersNickname().remove(removedPlayerNickname);
    }
}
