package SoftEng_2024.Model.Observers;

import SoftEng_2024.Model.Cards.Card;
import SoftEng_2024.Model.Cards.ResourceCard;
import SoftEng_2024.Model.Enums.Angles;
import SoftEng_2024.Model.Enums.Color;
import SoftEng_2024.Model.Enums.GameState;
import SoftEng_2024.Model.Fronts.Front;
import SoftEng_2024.Model.Fronts.ResourceFront;
import SoftEng_2024.Model.GoalCard.GoalCard;
import SoftEng_2024.Model.ModelMessages.*;
import SoftEng_2024.Network.ToView.ObServerManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PlayerObserver {
    protected double receiverID;
    private final String observedNickname;
    protected ObServerManager obServerManager;
    public PlayerObserver(ObServerManager o,double ID,String observedNickname){
        obServerManager=o;
        receiverID=ID;
        this.observedNickname=observedNickname;
    }
    //method that notifies the observerManager after a hand is updated
    //if the caller is different from the observedPlayer, the sent message won't show the cards
    public void updatedHand(List<Card> playerHand,String callerNickname){
        //if the updated hand doesn't belong to the player connected with this observer, hide
        //the cards
        List<Card> updatedHand=new ArrayList<>();
        if(!callerNickname.equals(observedNickname)){
            for(Card card:playerHand){
                updatedHand.add(new ResourceCard(new ResourceFront(new Angles[1],0,new boolean[1]),true,card.cloneBackResources()));
            }
            updatedHand.get(0).getFront().setHidden(true);
        }
        else
            updatedHand.addAll(playerHand);
        //if the player has less than 3 cards, it means that he has played a card, so send
        //with the message a string that says so
        if(playerHand.size()<3)
            notifyServer(new UpdatedHandMessage(receiverID,callerNickname+ " has played a card",updatedHand, callerNickname));
        //else, it means that the player has drawn a card, so the corresponding string
        //will be sent by the game observer
        else
            notifyServer(new UpdatedHandMessage(receiverID,"", updatedHand, callerNickname));
    }
    public void updatedIsPlayerOnline(boolean isPlayerOnline,String callerNickname){
        if(isPlayerOnline)
            notifyServer(new UpdatedIsOnlineMessage(receiverID,
                    observedNickname+" is back online!", true,callerNickname));
        else
            notifyServer(new UpdatedIsOnlineMessage(receiverID,
                    observedNickname+" left the game", false,callerNickname));
    }
    public void updatedPlayerColor(Color playerColor,String callerNickname) {
        if(callerNickname.equals(observedNickname))
            notifyServer(new UpdatedColorMessage("",playerColor,callerNickname));
    }
    public void updatedPlayerState(GameState playerState,String callerNickname) {
        if(callerNickname.equals(observedNickname))
            notifyServer(new UpdatedPlayerStateMessage(receiverID,"",playerState,callerNickname));
    }
    public void updatedNickname(String nickname,double callerID){
        //if the nickname that is being set belongs to the observed player, send it to the player
        if(receiverID==callerID)
            notifyServer(new UpdatedNicknameMessage(receiverID,"",nickname,true));
        else {
            //else, it means that the observed player needs to get the caller nickname
            //and that the
            notifyServer(new UpdatedNicknameMessage(receiverID, "", nickname, false));
            notifyServer(new UpdatedNicknameMessage(callerID,"",observedNickname,false));
        }
    }
    public void updatedAvailableGoals(List<GoalCard> availableGoals,String callerNickname){
        //manda il messaggio solo al client che ha eseguito l'operazione
        if(observedNickname.equals(callerNickname))
            notifyServer(new UpdatedAvailableGoalsMessage(receiverID,"",availableGoals,observedNickname));
        //se viene chiamato l'observer di un altro player, non fa niente
    }

    public void setReceiverID(double receiverID) {
        this.receiverID = receiverID;
    }

    public void notifyServer(ModelMessage msg){
        obServerManager.addModelMessageToQueue(msg);
    }


}
