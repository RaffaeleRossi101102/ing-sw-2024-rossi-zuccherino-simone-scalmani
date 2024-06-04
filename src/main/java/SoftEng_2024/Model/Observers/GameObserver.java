package SoftEng_2024.Model.Observers;

import SoftEng_2024.Model.Cards.Card;
import SoftEng_2024.Model.Game;
import SoftEng_2024.Model.GoalCard.GoalCard;
import SoftEng_2024.Model.ModelMessages.*;
import SoftEng_2024.Network.ToView.ObServerManager;

import java.util.List;

public class GameObserver {
    ObServerManager obServerManager;
    Game game;
    public GameObserver(ObServerManager o,Game game){
        obServerManager=o;
        this.game=game;
    }
    public void updatedDeck(String nickname, Card topCard, int whichDeck){
        //only sends the back resource
        if(whichDeck==0)
            notifyServer(new UpdatedResourceDeckMessage("",topCard.getResources()[4]));
        else if(whichDeck==1)
            notifyServer(new UpdatedGoldDeckMessage("",topCard.getResources()[4]));
    }
    public void updatedPublicCards(String nickname, int whichDeck){
        String message=nickname+" has drawn from the public cards";
        if(whichDeck/2==0){
            if(game.getResourceDeck().isEmpty()){
                //if after drawing the resource deck is empty, notify the client about it
                notifyServer(new UpdatedResourceDeckMessage(message,null));
            }
            else{
                //sending everyone the new top card of the resource deck
                notifyServer(new UpdatedGoldDeckMessage(message,game.getResourceDeck().peek().getResources()[4]));
            }
        }
        else{
            if(game.getGoldDeck().isEmpty()){
                //if after drawing the gold deck is empty, notify the client about it
                notifyServer(new UpdatedGoldDeckMessage(message,null));
            }
            else{
                //sending everyone the new top card of the resource deck
                notifyServer(new UpdatedGoldDeckMessage(message,game.getGoldDeck().peek().getResources()[4]));
            }
        }
        notifyServer(new UpdatedPublicCardsMessage(message,game.getPublicCards()));
    }
    public void updatedPublicGoals(GoalCard[] publicGoals){
        notifyServer(new UpdatedPublicGoals("The public goals have been chosen",publicGoals));
    }
    public void updatedCurrentPlayer(String currentPlayerNick){
        notifyServer();
    }
    public void notifyServer(ModelMessage msg){
        obServerManager.addModelMessageToQueue(msg);
    }
}
