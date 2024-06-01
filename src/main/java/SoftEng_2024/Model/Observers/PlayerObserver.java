package SoftEng_2024.Model.Observers;

import SoftEng_2024.Model.ModelMessages.ModelMessage;
import SoftEng_2024.Model.Player_and_Board.Player;
import SoftEng_2024.Network.ToView.ObServerManager;

public abstract class PlayerObserver {
    protected double receiverID;

    protected ObServerManager obServerManager;
    public PlayerObserver(ObServerManager o,double ID){
        obServerManager=o;
        receiverID=ID;
    }
    public abstract void notify(Player observedPlayer);
    public void updateServer(ModelMessage msg){
        obServerManager.addModelMessageToQueue(msg);
    }


}
