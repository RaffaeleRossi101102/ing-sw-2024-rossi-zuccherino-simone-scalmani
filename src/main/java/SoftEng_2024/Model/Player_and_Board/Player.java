package SoftEng_2024.Model.Player_and_Board;

import SoftEng_2024.Model.Cards.Card;
import SoftEng_2024.Model.Cards.StarterCard;
import SoftEng_2024.Model.Enums.Color;
import SoftEng_2024.Model.Enums.GameState;
import SoftEng_2024.Model.GoalCard.GoalCard;
import SoftEng_2024.Model.Observers.PlayerObserver;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a player in the game.
 */
public class Player {

    private List<Card> hand;
    private String nickname;
    private final Board playerBoard;
    private List<GoalCard> availableGoals;
    private final List<Color> color;
    private boolean isOnline;
    private final List<PlayerObserver> playerObservers;
    private GameState playerState;
    private StarterCard starterCard;

    // CONSTRUCTOR

    /**
     * Constructs a player with a specified initial hand of cards and associated game board.
     *
     * @param hand       The initial hand of cards for the player.
     * @param playerBoard The game board associated with the player.
     */
    public Player(List<Card> hand, Board playerBoard) {
        this.hand = hand;
        this.playerBoard = playerBoard;
        this.isOnline = true;
        this.availableGoals = new ArrayList<>();
        this.color = new ArrayList<>();
        this.playerState = GameState.CONNECTION;
        playerObservers = new ArrayList<>();
    }

    // OBSERVER METHODS

    /**
     * Adds an observer to the player.
     *
     * @param o The observer to add.
     */
    public void addObserver(PlayerObserver o) {
        playerObservers.add(o);
    }

    // SETTERS

    /**
     * Sets the nickname of the player and notifies observers.
     *
     * @param nickname The new nickname of the player.
     * @param ID       The ID associated with the player.
     */
    public void setNickname(String nickname, double ID) {
        this.nickname = nickname;
        for (PlayerObserver o : playerObservers)
            o.updatedNickname(nickname, ID);
    }

    /**
     * Sets the color associated with the player and notifies observers.
     *
     * @param color The color to set for the player.
     */
    public void setColor(Color color) {
        this.color.add(color);
        for (PlayerObserver o : playerObservers)
            o.updatedPlayerColor(color, nickname);
    }

    /**
     * Sets the online status of the player and notifies observers.
     *
     * @param isOnline true if the player is online, false otherwise.
     */
    public void setOnline(boolean isOnline) {
        this.isOnline = isOnline;
        for (PlayerObserver o : playerObservers)
            o.updatedIsPlayerOnline(isOnline, nickname);
    }

    /**
     * Adds a card to the player's hand and notifies observers.
     *
     * @param card The card to add to the player's hand.
     */
    public synchronized void addCard(Card card) {
        this.hand.add(card);
        for (PlayerObserver o : playerObservers)
            o.updatedHand(hand, nickname);
    }

    /**
     * Sets the starter card and notifies the player observer of the update.
     * This method is synchronized to ensure thread safety.
     *
     * @param starterCard The new starter card to be set.
     */
    public synchronized void setStarterCard(StarterCard starterCard){
        this.starterCard=starterCard;
        getPlayerObserver().updatedStarterCard(starterCard);
    }

    /**
     * Sets the player's hand of cards and notifies observers.
     *
     * @param hand The new hand of cards for the player.
     */
    public synchronized void setHand(List<Card> hand) {
        this.hand = hand;
        for (PlayerObserver o : playerObservers)
            o.updatedHand(this.hand, nickname);
    }

    /**
     * Removes a card from the player's hand and notifies observers.
     *
     * @param playedCard The card to remove from the player's hand.
     */
    public synchronized void removeCard(Card playedCard) {
        this.hand.remove(playedCard);
        for (PlayerObserver o : playerObservers)
            o.updatedHand(hand, nickname);
    }

    /**
     * Sets the state of the player and notifies observers.
     *
     * @param playerState The new state of the player.
     */
    public void setPlayerState(GameState playerState) {
        this.playerState = playerState;
        for (PlayerObserver o : playerObservers)
            o.updatedPlayerState(playerState, nickname);
    }

    /**
     * Sets the available goals for the player and notifies observers.
     *
     * @param availableGoals The list of available goal cards for the player.
     */
    public void setAvailableGoals(List<GoalCard> availableGoals) {
        this.availableGoals = availableGoals;
        for (PlayerObserver o : playerObservers)
            o.updatedAvailableGoals(availableGoals, nickname);
    }

    // GETTERS

    /**
     * Retrieves the observer associated with the player.
     *
     * @return The observer object associated with the player.
     */
    public PlayerObserver getPlayerObserver() {
        for (PlayerObserver o : playerObservers) {
            if (o.getObservedNickname().equals(nickname))
                return o;
        }
        return null;
    }

    /**
     * Retrieves the nickname of the player.
     *
     * @return The nickname of the player.
     */
    public String getNickname() {
        return nickname;
    }

    /**
     * Retrieves the game board associated with the player.
     *
     * @return The game board associated with the player.
     */
    public Board getPlayerBoard() {
        return playerBoard;
    }

    /**
     * Retrieves the hand of cards held by the player.
     *
     * @return The list of cards in the player's hand.
     */
    public synchronized List<Card> getHand() {
        return hand;
    }

    /**
     * Retrieves the list of colors associated with the player.
     *
     * @return The list of colors associated with the player.
     */
    public List<Color> getColor() {
        return color;
    }

    /**
     * Checks if the player is currently online.
     *
     * @return true if the player is online, false otherwise.
     */
    public boolean getIsOnline() {
        return isOnline;
    }

    /**
     * Retrieves the list of available goal cards for the player.
     *
     * @return The list of available goal cards for the player.
     */
    public List<GoalCard> getAvailableGoals() {
        return availableGoals;
    }

    /**
     * Retrieves the current state of the player.
     *
     * @return The current state of the player.
     */
    public GameState getPlayerState() {
        return playerState;
    }

    /**
     * Retrieves the current starter card.
     *
     * @return The current starter card.
     */

    public StarterCard getStarterCard() {
        return starterCard;
    }
}

