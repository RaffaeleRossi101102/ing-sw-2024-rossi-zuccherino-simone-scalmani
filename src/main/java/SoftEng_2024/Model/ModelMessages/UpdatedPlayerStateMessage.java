package SoftEng_2024.Model.ModelMessages;

import SoftEng_2024.Model.Enums.GameState;
import SoftEng_2024.View.View;

public class UpdatedPlayerStateMessage extends ModelMessage{
    GameState playerState;
    public UpdatedPlayerStateMessage(double ID, String message,GameState gs,String senderNickname) {
        super(ID, message,senderNickname);
        playerState=gs;
    }

    @Override
    public void executeMessage(View view) {
        if(view.getID()==ID){
            view.getLocalModel().setPlayerState(playerState);
        }
    }
}
