package SoftEng_2024.Model.Observers;

import SoftEng_2024.Model.Enums.GameState;
import SoftEng_2024.Model.ModelMessages.UpdatedPlayerStateMessage;
import SoftEng_2024.Model.Player_and_Board.Player;
import SoftEng_2024.Network.ToView.ObServerManager;

public class PlayerStateObserver extends PlayerObserver{
    GameState playerState;
    public PlayerStateObserver(ObServerManager o, double ID) {
        super(o, ID);
        this.playerState=GameState.CONNECTION;
    }

    @Override
    public void notify(Player observedPlayer) {
        GameState currentPlayerState=observedPlayer.getPlayerState();
        if(!currentPlayerState.equals(playerState)){
            playerState=currentPlayerState;
            updateServer(new UpdatedPlayerStateMessage(receiverID,"",playerState));
        }
    }
}
