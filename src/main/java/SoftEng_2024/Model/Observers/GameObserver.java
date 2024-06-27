package SoftEng_2024.Model.Observers;

import SoftEng_2024.Model.Cards.Card;
import SoftEng_2024.Model.Enums.Angles;
import SoftEng_2024.Model.Enums.GameState;
import SoftEng_2024.Model.Game;
import SoftEng_2024.Model.GoalCard.GoalCard;
import SoftEng_2024.Model.ModelMessages.*;
import SoftEng_2024.Network.ToModel.NetworkManager;
import SoftEng_2024.Network.ToView.ObServerManager;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.List;
/**
 * Observer responsible for handling game updates and notifying the server.
 */
public class GameObserver {

    ObServerManager obServerManager;
    Game game;
    NetworkManager networkManager;

    /**
     * Constructs a GameObserver with the Observer Manager, game instance, and network manager.
     *
     * @param o The Observer Manager instance to handle message notifications.
     * @param game            The Game instance being observed.
     * @param networkManager  The Network Manager instance for managing network interactions.
     */
    public GameObserver(ObServerManager o,Game game,NetworkManager networkManager){
        obServerManager=o;
        this.game=game;
        this.networkManager=networkManager;
    }

    /**
     * Notifies the server with an updated deck message based on the top card of a specified deck.
     *
     * @param topCard   The top Card of the deck.
     * @param whichDeck Specifies which deck is updated (0 for resource deck, 1 for gold deck).
     */
    public void updatedDeck(String nickname, Card topCard, int whichDeck) {
        //only sends the back resource
        if (topCard != null) {
            if (whichDeck == 0)
                notifyServer(new UpdatedResourceDeckMessage("", topCard.getCardBackAnglesType()[4]));
            else if (whichDeck == 1)
                notifyServer(new UpdatedGoldDeckMessage("", topCard.getCardBackAnglesType()[4]));
        }
        else{
            if(whichDeck==0)
                notifyServer(new UpdatedResourceDeckMessage("", null));
            else if (whichDeck==1)
                notifyServer(new UpdatedGoldDeckMessage("",null));

        }

    }


    //notifies all the clients that someone drew from the public cards.
    //the messages will contain both the new public cards and the resource of the new top card
    /**
     * Notifies all clients about a player drawing from the public cards.
     * Updates include new public cards and the top card's resource type.
     *
     * @param nickname  The nickname of the player who drew from the public cards.
     * @param whichDeck Specifies which deck is updated (3 for public cards).
     */
    public void updatedPublicCards(String nickname, int whichDeck){
        if(whichDeck==3){
//            notifyServer(new UpdatedResourceDeckMessage("",game.getResourceDeck().peek().getCardBackAnglesType()[4]));
//            notifyServer(new UpdatedGoldDeckMessage("",game.getGoldDeck().peek().getCardBackAnglesType()[4]));
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
                notifyServer(new UpdatedResourceDeckMessage(message,game.getResourceDeck().peek().getCardBackAnglesType()[4]));
            }
        }
        else{
            if(game.getGoldDeck().isEmpty()){
                //if after drawing the gold deck is empty, notify the client about it
                notifyServer(new UpdatedGoldDeckMessage(message,null));
            }
            else{
                //sending everyone the new top card of the resource deck
                notifyServer(new UpdatedGoldDeckMessage(message,game.getGoldDeck().peek().getCardBackAnglesType()[4]));
            }
        }
        notifyServer(new UpdatedPublicCardsMessage(message,game.getPublicCards()));
    }

    /**
     * Notifies clients about updated public goals.
     *
     * @param publicGoals The array of GoalCards representing the updated public goals.
     */
    public void updatedPublicGoals(GoalCard[] publicGoals){
        notifyServer(new UpdatedPublicGoalsMessage("The public goals have been chosen",publicGoals));
    }

    /**
     * Notifies clients about the updated current player.
     *
     * @param currentPlayerNick The nickname of the current player.
     */
    public void updatedCurrentPlayer(String currentPlayerNick){
        notifyServer(new UpdatedCurrentPlayerMessage(currentPlayerNick+" is your turn to play!",currentPlayerNick));
    }

    /**
     * Notifies clients about the updated game state.
     *
     * @param gameState The updated GameState.
     */
    public void updatedGameState(GameState gameState){
        networkManager.wakeUpManager();
        notifyServer(new UpdatedGameStateMessage(" we have entered a new state",gameState));
    }

    /**
     * Notifies clients about an updated acknowledgement.
     *
     * @param ack         The acknowledgement status.
     * @param receiverID  The ID of the receiver.
     */
    public void updatedAck(boolean ack,double receiverID){
        notifyServer(new UpdatedAckMessage(receiverID,"",ack));
    }

    /**
     * Notifies clients about an updated error message.
     *
     * @param receiverID    The ID of the receiver.
     * @param errorMessage  The error message content.
     */
    public void updatedError(double receiverID,String errorMessage){
        notifyServer(new UpdatedErrorMessage(receiverID,errorMessage));
    }

    /**
     * Unregisters a client from the server and notifies all clients about the unregistration.
     *
     * @param message     The message to be sent to clients.
     * @param receiverID  The ID of the receiver.
     * @throws IOException If an IO error occurs during unregistration process.
     */
    public void unRegisterClient(String message, double receiverID) throws IOException {

        obServerManager.getSocketServer().addToClientQueue(new UpdatedErrorMessage(receiverID,message));
        obServerManager.getSocketServer().addToClientQueue(new UpdatedAckMessage(receiverID,"",false));
        obServerManager.getServerRMI().addToClientQueue(new UpdatedAckMessage(receiverID,"",false));
        obServerManager.getServerRMI().addToClientQueue(new UpdatedErrorMessage(receiverID,message));
        obServerManager.getServerRMI().unregisterClient(receiverID);
        obServerManager.getSocketServer().unRegisterClient(receiverID);

    }

    /**
     * Notifies clients that the game is ending.
     * Checks if both decks and public cards are empty or if a player reached 20 points.
     */
    public void gameIsEnding(){
        if(game.getGoldDeck().isEmpty() && game.getResourceDeck().isEmpty() && game.getPublicCards().isEmpty())
            notifyServer(new GameIsEndingMessage("BOTH THE DECKS AND THE PUBLIC CARDS ARE EMPTY... THE GAME IS ENDING..."));
        else
            notifyServer(new GameIsEndingMessage("SOMEONE HAS REACHED 20 POINTS... THE GAME IS ENDING..."));
    }

    /**
     * Notifies client that he is the last player standing in the game.
     *
     */
    public void lastPlayerStanding(String winnerNickname){
        notifyServer(new LastPlayerStandingMessage("YOU'RE THE LAST PLAYER STILL ONLINE! IF NO ONE REJOINS THE GAME IN 30 SECONDS YOU'LL BE THE WINNER!"));
    }

    /**
     * Notifies clients that a player has been removed from the game.
     *
     * @param nickname The nickname of the removed player.
     */
    public void removedPlayer(String nickname){
        notifyServer(new RemovedPlayerMessage("",nickname));
    }

    /**
     * Notifies clients about updated winners of the game.
     *
     * @param winnersNickname The list of nicknames of the winners.
     */
    public void updatedWinners(List<String> winnersNickname){
        notifyServer(new UpdatedWinnersMessage("",winnersNickname));
        System.out.println("winnerrrsss");
    }

    /**
     * Notifies clients that a player won due to opponent's forfeit.
     *
     * @param winnerNickname The nickname of the winner due to forfeit.
     */
    public void winnerDueToForfeit(String winnerNickname){
        notifyServer(new WinnerDueToForfeitMessage(winnerNickname));
    }

    /**
     * Notifies the server by adding a ModelMessage to the server's message queue.
     *
     * @param msg The ModelMessage to be added to the server's message queue.
     */
    public void notifyServer(ModelMessage msg){
        obServerManager.addModelMessageToQueue(msg);
    }
}
