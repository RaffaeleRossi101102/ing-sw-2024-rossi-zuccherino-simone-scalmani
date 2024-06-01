package SoftEng_2024.Model.Observers;

import SoftEng_2024.Model.Enums.Color;
import SoftEng_2024.Model.ModelMessages.ModelMessage;
import SoftEng_2024.Model.ModelMessages.UpdatedColorMessage;
import SoftEng_2024.Model.Player_and_Board.Player;
import SoftEng_2024.Network.ToView.ObServerManager;

import java.util.ArrayList;
import java.util.List;

public class PlayerColorObserver extends PlayerObserver{
    private List<Color> playerColor;

    public PlayerColorObserver(ObServerManager o, double ID) {
        super(o, ID);
        playerColor=new ArrayList<>();
    }

    @Override
    public void notify(Player player) {
        //since the color can only be set once, player color has to be null in order to send the message
        if(playerColor.isEmpty()){
            playerColor=player.getColor();
            updateServer(new UpdatedColorMessage(receiverID,"",playerColor));
            player.removeObserver(this);
        }
    }
}
