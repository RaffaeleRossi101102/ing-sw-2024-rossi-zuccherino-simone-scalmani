package SoftEng_2024.Model.Observers;

import SoftEng_2024.Model.ModelMessages.UpdatedHandMessage;
import SoftEng_2024.Model.ModelMessages.UpdatedIsOnlineMessage;
import SoftEng_2024.Model.Player_and_Board.Player;
import SoftEng_2024.Network.ToView.ObServerManager;

public class PlayerOnlineObserver extends PlayerObserver{
    boolean isPlayerOnline=true;
    public PlayerOnlineObserver(ObServerManager o, double ID) {
        super(o, ID);
    }

    @Override
    public void notify(Player observedPlayer) {
        if(isPlayerOnline!=observedPlayer.getIsOnline()){
            isPlayerOnline=!isPlayerOnline;
            if(isPlayerOnline)
                updateServer(new UpdatedIsOnlineMessage(receiverID,
                    observedPlayer.getNickname()+" is back online!",isPlayerOnline));
            else
                updateServer(new UpdatedIsOnlineMessage(receiverID,
                        observedPlayer.getNickname()+" disconnected",isPlayerOnline));
        }
    }
}
