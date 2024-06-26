package SoftEng_2024.Model.Observers;

import SoftEng_2024.Model.Cards.Card;
import SoftEng_2024.Model.Cards.ResourceCard;
import SoftEng_2024.Model.Cards.StarterCard;
import SoftEng_2024.Model.Enums.Angles;
import SoftEng_2024.Model.Enums.Color;
import SoftEng_2024.Model.Enums.GameState;
import SoftEng_2024.Model.Fronts.ResourceFront;
import SoftEng_2024.Model.Game;
import SoftEng_2024.Model.GoalCard.GoalCard;
import SoftEng_2024.Model.ModelMessages.*;
import SoftEng_2024.Model.Player_and_Board.Board;
import SoftEng_2024.Model.Player_and_Board.Player;
import SoftEng_2024.Network.ToView.ObServerManager;

import java.util.ArrayList;
import java.util.List;
/**
 * Observer responsible for handling updates specific to a player and notifying the server.
 */
public class PlayerObserver {

    protected double receiverID;
    private final String observedNickname;
    protected ObServerManager obServerManager;
    private int numberOfMessages=0;

    /**
     * Constructs a PlayerObserver with the Observer Manager, receiver ID, and observed player's nickname.
     *
     * @param o  The Observer Manager instance to handle message notifications.
     * @param ID               The ID of the receiver associated with this observer.
     * @param observedNickname The nickname of the player being observed.
     */
    public PlayerObserver(ObServerManager o,double ID,String observedNickname){
        obServerManager=o;
        receiverID=ID;
        this.observedNickname=observedNickname;
    }
    //method that notifies the observerManager after a hand is updated
    //if the caller is different from the observedPlayer, the sent message won't show the cards
    /**
     * Notifies the observer manager after a player's hand is updated.
     * If the caller is different from the observed player, the sent message won't show the cards.
     *
     * @param playerHand     The updated hand of the player.
     * @param callerNickname The nickname of the caller (player who initiated the update).
     */
    public void updatedHand(List<Card> playerHand,String callerNickname){
        //if the updated hand doesn't belong to the player connected with this observer, hide
        //the cards
        List<Card> updatedHand=new ArrayList<>();
        if(!callerNickname.equals(observedNickname)){
            for(Card card:playerHand){
                updatedHand.add(new ResourceCard(new ResourceFront(new Angles[1],0,new boolean[1]),true,card.cloneBackResources(), card.getCardID()));
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


    /**
     * Notifies the observer about the updated online status of a player.
     *
     * @param isPlayerOnline  Boolean indicating if the player is online or not.
     * @param callerNickname  The nickname of the caller (player who initiated the update).
     */
    public void updatedIsPlayerOnline(boolean isPlayerOnline,String callerNickname){
        if(isPlayerOnline)
            notifyServer(new UpdatedIsOnlineMessage(receiverID,
                    callerNickname+" is back online!", true,callerNickname));
        else
            notifyServer(new UpdatedIsOnlineMessage(receiverID,
                    callerNickname+" left the game", false,callerNickname));
    }

    /**
     * Notifies the observer about the updated color of a player.
     *
     * @param playerColor    The updated color of the player.
     * @param callerNickname The nickname of the caller (player who initiated the update).
     */
    public void updatedPlayerColor(Color playerColor,String callerNickname) {
        if(callerNickname.equals(observedNickname))
            notifyServer(new UpdatedColorMessage("",playerColor,callerNickname));
    }

    /**
     * Notifies the observer about the updated state of a player.
     *
     * @param playerState    The updated state of the player.
     * @param callerNickname The nickname of the caller (player who initiated the update).
     */
    public void updatedPlayerState(GameState playerState,String callerNickname) {
        if(callerNickname.equals(observedNickname))
            notifyServer(new UpdatedPlayerStateMessage(receiverID,"",playerState,callerNickname));
    }

    /**
     * Notifies the observer about the updated nickname of a player.
     *
     * @param nickname The updated nickname of the player.
     * @param callerID The ID of the caller (player who initiated the update).
     */
    public void updatedNickname(String nickname,double callerID){
        //if the nickname that is being set belongs to the observed player, send it to the player
        if(receiverID==callerID) {
            notifyServer(new UpdatedNicknameMessage(receiverID, "", nickname, true));
        }
        else {
            //else, it means that the observed player needs to get the caller nickname
            //and that the
            notifyServer(new UpdatedNicknameMessage(receiverID, "", nickname, false));
            notifyServer(new UpdatedNicknameMessage(callerID,"",observedNickname,false));
        }
    }

    /**
     * Notifies the observer about the updated available goals of a player.
     *
     * @param availableGoals The list of available goals of the player.
     * @param callerNickname The nickname of the caller (player who initiated the update).
     */
    public void updatedAvailableGoals(List<GoalCard> availableGoals,String callerNickname){
        //manda il messaggio solo al client che ha eseguito l'operazione
        if(observedNickname.equals(callerNickname))
            notifyServer(new UpdatedAvailableGoalsMessage(receiverID,"",availableGoals,observedNickname));
        //se viene chiamato l'observer di un altro player, non fa niente
    }

    /**
     * Sets the receiver ID for this observer.
     *
     * @param receiverID The ID of the receiver to set.
     */
    public void setReceiverID(double receiverID) {
        this.receiverID = receiverID;
    }

    /**
     * Updates the starter card and notifies the server with an UpdatedStarterCardMessage.
     *
     * @param starterCard The starter card that has been updated.
     */
    public void updatedStarterCard(StarterCard starterCard){
        notifyServer(new UpdatedStarterCardMessage(receiverID,starterCard));
    }

    /**
     * Retrieves the observed player's nickname.
     *
     * @return The observed player's nickname.
     */
    public String getObservedNickname() {
        return observedNickname;
    }
    //according to the game state, the player will be notified with the current situation
    /**
     * Notifies the observer about a player rejoining the game.
     *
     * @param game The Game instance containing current game state.
     */
    public void playerRejoining(Game game){
        numberOfMessages=0;
        Player currentPlayer;
        List<Card> updatedHand;
        Board board;
        for(Player player:game.getPlayers()){
            //sending everyone's nick to the rejoining player
            if(player.getNickname().equals(observedNickname)) {
                currentPlayer = player;
                updatedHand=currentPlayer.getHand();
                notifyServerForRejoin(new UpdatedNicknameMessage(receiverID,"", currentPlayer.getNickname(),true));
                //sending the player's available goals
                if(!currentPlayer.getAvailableGoals().isEmpty())
                    notifyServerForRejoin(new UpdatedAvailableGoalsMessage(receiverID,"",currentPlayer.getAvailableGoals(), currentPlayer.getNickname()));
                //sending the player's current state
                notifyServerForRejoin(new UpdatedPlayerStateMessage(receiverID,"",currentPlayer.getPlayerState(), currentPlayer.getNickname()));
                if(currentPlayer.getStarterCard()!=null){
                    notifyServerForRejoin(new UpdatedStarterCardMessage(receiverID,currentPlayer.getStarterCard()));
                }
            }
            else {
                notifyServerForRejoin(new UpdatedNicknameMessage(receiverID, "", player.getNickname(), false));
                updatedHand= new ArrayList<>();
                for (Card card : player.getHand()) {
                    updatedHand.add(new ResourceCard(new ResourceFront(new Angles[1], 0, new boolean[1]), true, card.cloneBackResources(), card.getCardID()));
                }
                if(!updatedHand.isEmpty())
                    updatedHand.get(0).getFront().setHidden(true);
            }
            //sending everyone's hand to the rejoining player
            if(!updatedHand.isEmpty())
                notifyServerForRejoin(new UpdatedHandMessage(receiverID,"",updatedHand, player.getNickname()));
            if(!player.getColor().isEmpty())
                notifyServerForRejoin(new UpdatedColorMessage("",player.getColor().get(0), player.getNickname()));
            //sending everyone's board to the rejoining player
            board=player.getPlayerBoard();
            notifyServerForRejoin(new UpdatedBoardMessage(receiverID,"", player.getNickname(), board.getCardBoard(), board.getCardList(), board.getAnglesCounter(), board.getScore()));

        }
        if(game.getCurrentPlayer()!=null)
            notifyServerForRejoin(new UpdatedCurrentPlayerMessage("",game.getCurrentPlayer().getNickname()));
        notifyServerForRejoin(new UpdatedPublicCardsMessage("",game.getPublicCards()));
        if(game.getResourceDeck().peek()!=null)
            notifyServerForRejoin(new UpdatedResourceDeckMessage("",game.getResourceDeck().peek().getCardBackAnglesType()[4]));
        if(game.getGoldDeck().peek()!=null)
            notifyServerForRejoin(new UpdatedGoldDeckMessage("",game.getGoldDeck().peek().getCardBackAnglesType()[4]));
        if(game.getPublicGoals()!=null)
            notifyServerForRejoin(new UpdatedPublicGoalsMessage("",game.getPublicGoals()));
        notifyServerForRejoin(new UpdatedGameStateMessage("",game.getGameState()));
        notifyServerForRejoin(new NumberOfMessages(receiverID,"","",numberOfMessages+1));
        //loops until it gets the right player

    }

    /**
     * Notifies the observer manager by adding a ModelMessage to the server's message queue.
     *
     * @param msg The ModelMessage to be added to the server's message queue.
     */
    public void notifyServer(ModelMessage msg){
        obServerManager.addModelMessageToQueue(msg);
    }

    /**
     * Retrieves the receiver ID associated with this observer.
     *
     * @return The receiver ID.
     */
    public double getReceiverID() {
        return receiverID;
    }

    /**
     * Notifies the observer manager about a message for rejoining purposes.
     * Marks the message as for rejoin and increments the message count.
     *
     * @param msg The ModelMessage to be added for rejoining.
     */
    private void notifyServerForRejoin(ModelMessage msg){
        msg.setRejoining(true);
        msg.setReceiverID(receiverID);
        this.numberOfMessages++;
        notifyServer(msg);
    }

}
