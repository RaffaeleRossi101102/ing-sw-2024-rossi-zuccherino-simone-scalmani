package SoftEng_2024.Model.Observers;

import SoftEng_2024.Controller.GameController;
import SoftEng_2024.Model.Cards.Card;
import SoftEng_2024.Model.ModelMessages.ModelMessage;
import SoftEng_2024.Model.ModelMessages.UpdatedHandMessage;
import SoftEng_2024.Model.Player_and_Board.Player;

import java.util.ArrayList;
import java.util.List;

public class PlayerHandObserver extends PlayerObserver{
    private double ID;
    GameController controller;
    private List<Card> playerHand;
    public PlayerHandObserver(GameController controller,double ID){
        this.controller=controller;
        this.playerHand=new ArrayList<>();
        this.ID=ID;
    }

    @Override
    public void lookForPlayerChanges(Player player) {
        //if the hand in the player is different from the one previously observed, send a message
        //else, no action.
        if(!playerHand.equals(player.getHand())) {
            this.playerHand=player.getHand();
            updateServer(new UpdatedHandMessage(ID, "updatedHandMessage", playerHand));
        }

    }

    @Override
    public void updateServer(ModelMessage msg) {
        controller.getToViewManager().addModelMessageToQueue(msg);
    }

}
