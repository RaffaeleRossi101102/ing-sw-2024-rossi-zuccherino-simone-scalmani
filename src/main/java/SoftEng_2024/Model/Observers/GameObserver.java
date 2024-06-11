package SoftEng_2024.Model.Observers;

import SoftEng_2024.Model.Cards.Card;
import SoftEng_2024.Model.Enums.Angles;
import SoftEng_2024.Model.Enums.GameState;
import SoftEng_2024.Model.Game;
import SoftEng_2024.Model.GoalCard.GoalCard;
import SoftEng_2024.Model.ModelMessages.*;
import SoftEng_2024.Network.ToModel.NetworkManager;
import SoftEng_2024.Network.ToView.ObServerManager;

import java.util.List;

public class GameObserver {
    ObServerManager obServerManager;
    Game game;
    NetworkManager networkManager;
    public GameObserver(ObServerManager o,Game game,NetworkManager networkManager){
        obServerManager=o;
        this.game=game;
        this.networkManager=networkManager;
    }
    public void updatedDeck(String nickname, Card topCard, int whichDeck) {
        //only sends the back resource
        if (topCard != null) {
            if (whichDeck == 0)
                notifyServer(new UpdatedResourceDeckMessage("", topCard.getResources()[4]));
            else if (whichDeck == 1)
                notifyServer(new UpdatedGoldDeckMessage("", topCard.getResources()[4]));
        }
        else{
            if(whichDeck==0)
                notifyServer(new UpdatedResourceDeckMessage("", Angles.EMPTY));
            else if (whichDeck==1)
                notifyServer(new UpdatedGoldDeckMessage("",Angles.EMPTY));

        }

    }
    //notifies all the clients that someone drew from the public cards.
    //the messages will contain both the new public cards and the resource of the new top card
    public void updatedPublicCards(String nickname, int whichDeck){
        if(whichDeck==3){
            notifyServer(new UpdatedResourceDeckMessage("",game.getResourceDeck().peek().getResources()[4]));
            notifyServer(new UpdatedGoldDeckMessage("",game.getGoldDeck().peek().getResources()[4]));
            notifyServer(new UpdatedPublicCardsMessage("",game.getPublicCards()));
            return;
        }
        String message=nickname+" has drawn from the public cards";
        if(whichDeck/2==0){
            if(game.getResourceDeck().isEmpty()){
                //if after drawing the resource deck is empty, notify the client about it
                notifyServer(new UpdatedResourceDeckMessage(message,null));
            }
            else{
                //sending everyone the new top card of the resource deck
                notifyServer(new UpdatedResourceDeckMessage(message,game.getResourceDeck().peek().getResources()[4]));
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
        notifyServer(new UpdatedCurrentPlayerMessage(currentPlayerNick+" is your turn to play!",currentPlayerNick));
    }
    public void updatedGameState(GameState gameState){
        networkManager.wakeUpManager();
        notifyServer(new UpdatedGameStateMessage(" we have entered a new state",gameState));
    }
    public void updatedAck(boolean ack,double receiverID){
        notifyServer(new UpdatedAckMessage(receiverID,"",ack));
    }
    public void updatedError(double receiverID,String errorMessage){
        notifyServer(new UpdatedErrorMessage(receiverID,errorMessage));
    }
    public void gameIsEnding(){
        if(game.getGoldDeck().isEmpty() && game.getResourceDeck().isEmpty() && game.getPublicCards().isEmpty())
            notifyServer(new GameIsEndingMessage("BOTH THE DECKS AND THE PUBLIC CARDS ARE EMPTY... THE GAME IS ENDING..."));
        else
            notifyServer(new GameIsEndingMessage("SOMEONE HAS REACHED 20 POINTS... THE GAME IS ENDING..."));
    }
    public void updatedWinners(List<String> winnersNickname){

    }
    public void notifyServer(ModelMessage msg){
        obServerManager.addModelMessageToQueue(msg);
    }
}
