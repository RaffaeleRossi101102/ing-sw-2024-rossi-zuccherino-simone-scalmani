package SoftEng_2024.Model.Observers;

import SoftEng_2024.Controller.GameController;
import SoftEng_2024.Model.Cards.Card;
import SoftEng_2024.Model.ModelMessages.ModelMessage;
import SoftEng_2024.Model.ModelMessages.UpdatedHandMessage;
import SoftEng_2024.Model.Player_and_Board.Player;
import SoftEng_2024.Network.ToView.ObServerManager;

import java.util.ArrayList;
import java.util.List;

public class PlayerHandObserver extends PlayerObserver{

    GameController controller;
    private List<Card> playerHand;
    public PlayerHandObserver(ObServerManager o, double ID){
        super(o,ID);
        this.playerHand=new ArrayList<>();
    }

    @Override
    public void notify(Player player) {
        //if the hand in the player is different from the one previously observed, send a message
        //else, no action.
        if(!playerHand.equals(player.getHand())) {
            this.playerHand.clear();
            this.playerHand.addAll(player.getHand());
            updateServer(new UpdatedHandMessage(receiverID, "updatedHandMessage", playerHand));
        }

    }

}
