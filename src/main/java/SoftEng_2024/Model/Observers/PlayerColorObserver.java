package SoftEng_2024.Model.Observers;

import SoftEng_2024.Model.Enums.Color;
import SoftEng_2024.Model.ModelMessages.ModelMessage;
import SoftEng_2024.Model.Player_and_Board.Player;

public class PlayerColorObserver extends PlayerObserver{
    private Color playerColor;
    @Override
    public void lookForPlayerChanges(Player player) {
        //since the color can only be set once, player color has to be null in order to send the message
        if(playerColor==null){

        }
    }

    @Override
    public void updateServer(ModelMessage msg) {

    }
}
