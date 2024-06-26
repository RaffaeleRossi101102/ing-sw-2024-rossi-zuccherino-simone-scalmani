package SoftEng_2024.Model;

import SoftEng_2024.Model.Cards.Card;
import SoftEng_2024.Model.Cards.GoldCard;
import SoftEng_2024.Model.Cards.ResourceCard;
import SoftEng_2024.Model.Enums.Angles;
import SoftEng_2024.Model.Enums.GameState;
import SoftEng_2024.Model.Fronts.GoldFront;
import SoftEng_2024.Model.Fronts.ResourceFront;
import SoftEng_2024.Model.GoalCard.GoalCard;
import SoftEng_2024.Model.Observers.GameObserver;
import SoftEng_2024.Model.Player_and_Board.Board;
import SoftEng_2024.Model.Player_and_Board.Player;


import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * class containing all the model objects necessary for the game. It also has the methods that will modify them.
 * these methods can only be called by the gameController
 */
public class Game {
    /**
     * state of the game
     */
    private volatile GameState gameState;
    /**
     * list of the player objects
     */
    private List<Player> players;
    /**
     * index of the list of players to which the current player corresponds
     */
    private int playerIndex = 0;
    /**
     * player in turn
     */
    private Player currentPlayer;
    /**
     * queue containing the gold cards
     */
    private Queue<Card> goldDeck;
    /**
     * queue containing the resource cards
     */
    private Queue<Card> resourceDeck;
    /**
     * queue containing the starter cards
     */
    private Queue<Card> starterDeck;
    /**
     * array containing the two public goals
     */
    private GoalCard[] publicGoals;
    /**
     * array containing the four public cards
     */
    private List<Card> publicCards;
    /**
     * queue containing the goal cards
     */
    private Queue<GoalCard> goalCardDeck;
    /**
     * boolean that indicates whether the player has already played a card
     */
    private boolean play;
    /**
     * boolean that indicates whether the player has already drawn a card
     */
    private boolean draw;

    /**
     * max points scored by a player
     */
    private int maxScore=0;

    /**
     * observer of the game
     */
    private GameObserver gameObserver;

    /**
     * counter of the online players
     */
    private int onlinePlayersCounter;

    /**
     * thread-safe hash map that has the client ID as the key and the ack of the last operation as the value
     */
    private ConcurrentHashMap<Double,Boolean> AckIdBindingMap;
    /**
     * thread-safe hash map that has the client ID as the key and the errorMessage of the last operation as the value
     */
    private ConcurrentHashMap<Double,String> ErrorMessageBindingMap;

    /**
     * if the timer expires, this String will contain the nickname of the last online player.
     * this will be the winner
     */
    private String winnerDueToForfeit;
    //TODO: notificare il cambiamento di:
    //TODO: TUTTI I DECK--> ogni volta che viene pescata una carta ---
    //TODO: LE PUBLIC CARDS--> vanno fatte vedere sempre aggiornate ---
    //TODO: I PUBLIC GOALS-->vanno fatti vedere una volta posizionati ---
    //TODO: I CURRENT PLAYERS--> ogni volta che cambia, va fatto sapere ai client ---
    //TODO: LO STATO DEL GIOCO--> ogni volta che cambia, va fatto sapere a tutti i client
    //TODO: SE UN'OPERAZIONE è SUCCESSFUL O MENO

    public Game(List<Player> players, Queue<Card> goldDeck, Queue<Card> resourceDeck, Queue<Card> starterDeck, Queue<GoalCard> goalCardDeck){
        this.players = players;
        this.goldDeck = goldDeck;
        this.resourceDeck = resourceDeck;
        this.starterDeck = starterDeck;
        this.publicCards = new ArrayList<>();
        this.goalCardDeck = goalCardDeck;
        this.onlinePlayersCounter = 0;
        this.AckIdBindingMap=new ConcurrentHashMap<>();
        this.ErrorMessageBindingMap=new ConcurrentHashMap<>();
        this.gameState=GameState.CONNECTION;

    }

    /**
     * method that calculates the points each player made by completing both private and public goals
     */
    public void gameEnd() {
        //add the scores obtained from the goals to the scores obtained by placing the cards
        int [] playerScore= new int[players.size()];
        List<String> nicknameWinners= new ArrayList<>();
        int goalScore;
        int counter;
        int[] goalTypes= new int[players.size()];
        Board localBoard;
        for(Player player:players){
            localBoard=player.getPlayerBoard();
            playerScore[players.indexOf(player)] = localBoard.getScore();
            goalScore= player.getAvailableGoals().get(0).calcScore(localBoard);
            if(goalScore!=0){
                goalTypes[players.indexOf(player)]++;
                playerScore[players.indexOf(player)] += goalScore;
            }
            goalScore = publicGoals[0].calcScore(localBoard);
            if(goalScore!=0){
                goalTypes[players.indexOf(player)]++;
                playerScore[players.indexOf(player)] += goalScore;
            }
            goalScore=publicGoals[1].calcScore(localBoard);
            if(goalScore!=0){
                goalTypes[players.indexOf(player)]++;
                playerScore[players.indexOf(player)] += goalScore;
            }
        }
        // find the maximum score and reset the points of the players who definitely lost
        int max = Arrays.stream(playerScore).max().getAsInt();
        counter=players.size();
        for (int i=0; i<playerScore.length; i++){
            if(playerScore[i] !=max){
                playerScore[i]= (-1);
                goalTypes[i]= (-1);
                counter--;
            }
        }
        //now playerScore will have either -1 or the maximum score
        //now I scroll through goalTypes and eliminate the players who have scored fewer goals

        max=Arrays.stream(goalTypes).max().getAsInt();
        for (int i=0; i<goalTypes.length; i++){
            if(goalTypes[i] !=max){
                playerScore[i]= (-1);
                counter--;
            }
        }

        //now in playerScore I have the indexes of the winners
        for(int i=0; i<playerScore.length; i++){
            if(playerScore[i]!= (-1)){
                nicknameWinners.add(players.get(i).getNickname());
            }
        }
        setWinners(nicknameWinners);
    }

    /**
     * method that triggers the observer to send the winners
     * @param winners list of winners
     */
    public void setWinners(List<String> winners){
        gameObserver.updatedWinners(winners);
    }


    /**
     * method that starts a new turn
     * If the game isn't ending, it will check if the player index points to an online player.
     * If the check is true, that player will start their turn. Else, it will increase the index and make the same checks.
     */
    public synchronized void turnStart(){
        if(!checkIfGameEnd()){
//            int onlinePlayersCounter = 0;
//            for (Player c : players) {
//                if (c.getIsOnline())
//                    onlinePlayersCounter++;
//            }
            if (onlinePlayersCounter != 1) {
                while (!players.get(playerIndex).getIsOnline()) {
                    playerIndex = (playerIndex + 1) % players.size();
                }
                currentPlayer = players.get(playerIndex);
                currentPlayer.setPlayerState(GameState.PLAY);
                play = true;
                gameObserver.updatedCurrentPlayer(currentPlayer.getNickname());
            }
        }
    }

    /**
     * method that checks if the game has to end. If this check is true, this method will change the game state and the
     * network manager will stop reading messages and will call the gameEnd method
     * @return
     */
    public synchronized boolean checkIfGameEnd(){
        boolean gameEnd = true;
        for(Player p:players){
            if(!p.getPlayerState().equals(GameState.ENDGAME) && p.getIsOnline()) {
                gameEnd = false;
                break;
            }
        }
        if(gameEnd){
            gameState=GameState.ENDGAME;
            gameObserver.gameIsEnding();
        }
        return gameEnd;
    }

    public void checkIfIsLastTurn(){
        if(maxScore>=20 | (goldDeck.isEmpty() && resourceDeck.isEmpty() && publicCards.isEmpty())){
            for(int i=0; i<=playerIndex; i++){
                if(!players.get(i).getPlayerState().equals(GameState.ENDGAME))
                    players.get(i).setPlayerState(GameState.ENDGAME);
            }
        }
    }
    //fa scorrere la lista e controlla se maxscore è arrivato a 20 e se il giro dei turni è finito
    //in questo caso vado a gameEnd dove verranno calcolati i punteggi dei goal e sommati ai punteggi correnti dei giocatori
    //else, passa il turno al prossimo ritornando a turn start
    public synchronized void turnEnd(String nickname){
        if(nickname.equals(currentPlayer.getNickname())) {
            checkIfIsLastTurn();
            //questo è l'ultimo turno del current player
            if (!checkIfGameEnd()) {
//            currentPlayer.setPlayerState(GameState.NOTPLAYING);
                if (playerIndex == players.size() - 1) {
                    playerIndex = 0;
                } else {
                    playerIndex++;
                }
                turnStart();
            }
        }
    }
    //metodo che aggiorna il model in base alla carta scelta dal client
    //ESISTE IL MODO PER ASCOLTARE SOLTANTO UN PLAYER?
    public int playCard(Card playedCard, Player player, int r, int c){
        int result=-2;
        try{
            //controllo che il player che ha chiamato questo metodo sia quello che deve giocare il turno
            //controllo che il player che ha chiamato questo metodo non l'abbia chiamato già prima
            if(player.equals(currentPlayer) && play ){
                //Possibile che questo controllo vada nel controller
                //se ciò avviene, chiamo updateBoard che verifica se la carta è giocabile. Se non è giocabile, viene
                //chiamata l'eccezione
                player.getPlayerBoard().updateBoard(r,c,playedCard);
                play=false;
                draw=true;
                player.removeCard(playedCard);
                //dopo che ho giocato la carta, devo aggiornare max score
                if(player.getPlayerBoard().getScore() > maxScore) maxScore=player.getPlayerBoard().getScore();
                result=1;
            }
        } catch (Board.notAvailableCellException nace) {
            return -1;
        }
        catch (Board.necessaryResourcesNotAvailableException e) {
            return 0;
        }
        if(!player.equals(currentPlayer)) result=-3;
        return result;
    }
    //RICORDA L'ECCEZIONE PER PLAYER NON CORRETTO, GIà PESCATO/GIOCATO E CARTE FINITE
//    public int drawPublicCards(Player player, int index){
//        //draw =false
//        int result=0;
//        //se il giocatore non ha già pescato la carta ed è quello giusto,
//        if (player.equals(currentPlayer) && draw){
//            //se le carte pubbliche non sono finite
//            if(!publicCards.isEmpty()) {
//                //e la carta che voglio pescare c'è
//                if(publicCards.get(index)!=null) {
//                    player.addCard(publicCards.get(index));
//                    draw = false;
//                    //tutto liscio
//                    result = 1;
//                    //rimpiazza lo spazio della carta pescata con una carta del deck se il deck non è vuoto
//                    //risorsa
//                    if(index<2){
//                        if(!resourceDeck.isEmpty())
//                            publicCards.set(index, resourceDeck.poll());
//                        else
//                            publicCards.set(index,null);
//                    }
//                    else{
//                        if(!goldDeck.isEmpty())
//                            publicCards.set(index, goldDeck.poll());
//                        else
//                            publicCards.set(index,null);
//                    }
//                    gameObserver.updatedPublicCards(player.getNickname(), index);
//                }
//                else{
//                    result=-3;
//                }
//            }else {
//                result = -1;
//            }
//        }
//        if(!player.equals(currentPlayer)) result=-2;
//        return result;
//    }

    public int drawPublicCards(Player player, int index) {
        //checks if the player is allowed to draw a card
        if (!player.equals(currentPlayer)) {
            return -2;
        }
        //checks if the player has already drawn a card
        if (!draw) {
            return 0;
        }
        //checks if the public cards are empty
        if (publicCards.isEmpty()) {
            return -1;
        }
        //checks if the index is out of bounds or if the value is null
        if (index < 0 || index >= publicCards.size() || publicCards.get(index) == null) {
            return -3;
        }

        //Draws the card
        player.addCard(publicCards.get(index));
        draw = false;

        //Replaces the drawn card with a new one from the appropriate deck
        if (index < 2) {
            publicCards.set(index, !resourceDeck.isEmpty() ? resourceDeck.poll() : null);
        } else {
            publicCards.set(index, !goldDeck.isEmpty() ? goldDeck.poll() : null);
        }

        //Notify observers
        gameObserver.updatedPublicCards(player.getNickname(), index);

        return 1;
    }



    public int drawFromTheDeck(Player player, int whichDeck){
        int result=0;
        if((player.equals(currentPlayer) && draw) ){
            //firstTurn=false;
            //whichDeck è associato al click del client quando seleziona il deck da pescare
            //se è uguale a 0 prendo il deck risorsa se è =1 quello oro
            if(whichDeck==0) {
                if(!resourceDeck.isEmpty()) {
                    player.addCard(resourceDeck.poll());
                    draw=false;
                    result=1;
                    gameObserver.updatedDeck(player.getNickname(),resourceDeck.peek(),whichDeck);
                }
                else result=-2;
            }
            if(whichDeck==1) {
                if(!goldDeck.isEmpty()) {
                    player.addCard(goldDeck.poll());
                    draw=false;
                    result=1;
                    gameObserver.updatedDeck(player.getNickname(), goldDeck.peek(),whichDeck);
                }
                else result=-3;
            }
        }
        if(!player.equals(currentPlayer)) result=-1;
        return result;
    }
    //method that gives out the cards to the player and if necessary, notifies about the changes to the deck
    public void handOutCards(Player player,boolean updateAboutTheDecks){
        List<Card> newHand=new ArrayList<>();
        newHand.add(resourceDeck.poll());
        newHand.add(resourceDeck.poll());
        newHand.add(goldDeck.poll());
//        newHand.add(new ResourceCard(new ResourceFront(new Angles[]{Angles.EMPTY,Angles.EMPTY,Angles.EMPTY,Angles.EMPTY}, 7, new boolean[4]), false, new Angles[]{Angles.EMPTY,Angles.EMPTY,Angles.EMPTY,Angles.EMPTY,Angles.ANIMALS}, 0));
//        newHand.add(new ResourceCard(new ResourceFront(new Angles[]{Angles.EMPTY,Angles.EMPTY,Angles.EMPTY,Angles.EMPTY}, 7, new boolean[4]), false, new Angles[]{Angles.EMPTY,Angles.EMPTY,Angles.EMPTY,Angles.EMPTY, Angles.PLANTS}, 0));
//        newHand.add(new ResourceCard(new ResourceFront(new Angles[]{Angles.EMPTY,Angles.EMPTY,Angles.EMPTY,Angles.EMPTY}, 7, new boolean[4]), false, new Angles[]{Angles.EMPTY,Angles.EMPTY,Angles.EMPTY,Angles.EMPTY, Angles.FUNGI}, 0));

        player.setHand(newHand);
        if(updateAboutTheDecks) {
            gameObserver.updatedDeck(player.getNickname(), resourceDeck.peek(), 0);
            gameObserver.updatedDeck(player.getNickname(), goldDeck.peek(), 1);
        }
    }
    public void updatePublicCards(){
        publicCards.add(resourceDeck.poll());
        publicCards.add(resourceDeck.poll());
        publicCards.add(goldDeck.poll());
        publicCards.add(goldDeck.poll());
        System.out.println();
        gameObserver.updatedPublicCards("",3);
    }
    //metodo che aggiorna il current player,
    public GoalCard[] getPublicGoals() {
        return publicGoals;
    }

    public synchronized void setAckIdBindingMap(double ID, boolean ack) {
        //se non è stato inserito l'Id in precedenza, lo setto ora
        if(!AckIdBindingMap.containsKey(ID))
            AckIdBindingMap.put(ID,ack);
        else
            //altrimenti lo rimpiazzo con il nuovo ack
            AckIdBindingMap.replace(ID,ack);
        gameObserver.updatedAck(ack,ID);
    }

    public void setErrorMessageBindingMap(double ID,String errorMessage) {
        ErrorMessageBindingMap.put(ID,errorMessage);
        gameObserver.updatedError(ID,errorMessage);
        //TODO: VALUTARE CASO CHAT!!! Fare una mappa a parte/controllare nella view/invece di una stringa mettere una

    }
    public void setAckAndError(double ID,  String errorMessage) {
        try {
            gameObserver.unRegisterClient(errorMessage, ID);
        } catch (IOException e) {
            System.err.println("Something went terribly wrong! (at Game setAckAndError)");
        }
    }


    public void removePlayerFromList(Player removedPlayer,double playerID){
        players.remove(removedPlayer);
        getAckIdBindingMap().remove(playerID);
        ErrorMessageBindingMap.remove(playerID);
        gameObserver.removedPlayer(removedPlayer.getNickname());
    }
    public void setPublicGoals(GoalCard[] publicGoals) {
        this.publicGoals = publicGoals;
        gameObserver.updatedPublicGoals(this.publicGoals);
    }
    public void setGameState(GameState gameState) {
        this.gameState = gameState;
        gameObserver.updatedGameState(gameState);
    }

    public void setOnlinePlayersCounter(int add) {
        synchronized ((Integer)this.onlinePlayersCounter) {
            onlinePlayersCounter += add;

            if (onlinePlayersCounter == 1 & add < 0) {
                gameObserver.lastPlayerStanding(winnerDueToForfeit);
                for (Player player : players) {
                    if (player.getIsOnline()) {
                        this.winnerDueToForfeit = player.getNickname();
                        break;
                    }
                }
            }
        }
    }

    public void triggerWinnerDueToForfeit() {
        gameObserver.winnerDueToForfeit(winnerDueToForfeit);
    }

    public Queue<GoalCard> getGoalCardDeck() {
        return goalCardDeck;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public List<Card> getPublicCards() {
        return publicCards;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public Queue<Card> getGoldDeck() {
        return goldDeck;
    }

    public Queue<Card> getResourceDeck() {
        return resourceDeck;
    }

    public GameObserver getGameObserver() {
        return gameObserver;
    }

    public Queue<Card> getStarterDeck() {
        return starterDeck;
    }

    public GameState getGameState() {
        return gameState;
    }

    public void setPlayerIndex(int playerIndex) {
        this.playerIndex = playerIndex;
    }
    public void shufflePlayers(){
        Collections.shuffle(this.players);
    }


    public ConcurrentHashMap<Double, Boolean> getAckIdBindingMap() {
        return AckIdBindingMap;
    }

    public ConcurrentHashMap<Double, String> getErrorMessageBindingMap() {
        return ErrorMessageBindingMap;
    }

    public void setGameObserver(GameObserver gameObserver) {
        this.gameObserver = gameObserver;
    }

    public void setMaxScore(int maxScore) {
        this.maxScore = maxScore;
    }

    public int getMaxScore(){return this.maxScore;}

    public int getOnlinePlayersCounter() {
        synchronized ((Integer)this.onlinePlayersCounter) {
            return onlinePlayersCounter;
        }
    }

}

