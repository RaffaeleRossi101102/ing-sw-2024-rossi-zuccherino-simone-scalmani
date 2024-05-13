package SoftEng_2024.Controller;

import SoftEng_2024.Model.Board;
import SoftEng_2024.Model.Cards.Card;
import SoftEng_2024.Model.GoalCard;
import SoftEng_2024.Model.Player;


import java.util.*;

public class Game {
    private List<Player> players;
    private int playerIndex = 0;
    private Player currentPlayer;
    private Queue<Card> goldDeck;
    private Queue<Card> resourceDeck;
    private Queue<Card> starterDeck;
    private GoalCard[] publicGoals;
    private List<Card> publicCards;
    private Queue<GoalCard> goalCardDeck;
    private boolean play;
    private boolean draw;
    private boolean gameEnd;
    private boolean firstTurn= true;
    private static int maxScore=0;

    public Game(List<Player> players,Queue<Card> goldDeck, Queue<Card> resourceDeck, Queue<Card> starterDeck, Queue<GoalCard> goalCardDeck){
        this.players = players;
        this.goldDeck = goldDeck;
        this.resourceDeck = resourceDeck;
        this.starterDeck = starterDeck;
        this.publicCards=new ArrayList<>();
        this.goalCardDeck = goalCardDeck;
    }

    protected List<Integer> GameEnd() {
        //somma i punteggi ottenuti dai goal ai punteggi ottenuti piazzando le carte
        int [] playerScore= new int[players.size()];
        List<Integer> indexWinners= new ArrayList<>();
        int goalScore;
        int counter;
        int[] goalTypes= new int[players.size()];
        Board localBoard;
        for(Player player:players){
            localBoard=player.getPlayerBoard();
            playerScore[players.indexOf(player)] = localBoard.getScore();
            goalScore= player.getAvailableGoals().getFirst().calcScore(localBoard);
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
        //trovo il massimo punteggio e azzero i punti dei giocatori che hanno sicuramente perso
        int max = Arrays.stream(playerScore).max().getAsInt();
        counter=players.size();
        for (int i=0; i<playerScore.length; i++){
            if(playerScore[i] !=max){
                playerScore[i]= (-1);
                goalTypes[i]= (-1);
                counter--;
            }
        }
        //ora playerScore avrà o -1 o il punteggio massimo
        //adesso scorro goalTypes e elimino i giocatori che hanno fatto meno obiettivi

        max=Arrays.stream(goalTypes).max().getAsInt();
        for (int i=0; i<goalTypes.length; i++){
            if(goalTypes[i] !=max){
                playerScore[i]= (-1);
                counter--;
            }
        }
        //if(counter >1) System.out.println("The number of winners is " + counter + " and they are... ");
        //ora in playerScore ho gli indici dei vincitori
        for(int i=0; i<playerScore.length; i++){
            if(playerScore[i]!= (-1)){
                indexWinners.add(i);
            }
        }
        return indexWinners;
    }
    //prende dalla lista di player il current player e aggiorna i campi booleani draw e play
    public void TurnStart(){
        if(players.get(playerIndex).getIsOnline()) {
            currentPlayer = players.get(playerIndex);
            currentPlayer.isPlaying = true;
            play = true;
        }
        else {
            playerIndex=(playerIndex+1)%players.size();
        }
    }
    //fa scorrere la lista e controlla se maxscore è arrivato a 20 e se il giro dei turni è finito
    //in questo caso vado a gameEnd dove verranno calcolati i punteggi dei goal e sommati ai punteggi correnti dei giocatori
    //else, passa il turno al prossimo ritornando a turn start
    public boolean TurnEnd(){
        gameEnd= false;
        if((maxScore >= 20 && playerIndex == players.size() - 1) || (goldDeck.isEmpty() && resourceDeck.isEmpty() && publicCards.isEmpty())){
            gameEnd = true;
        }
        if(playerIndex == players.size()-1){
            playerIndex = 0;
        }else{
            playerIndex++;
        }
        return gameEnd;
    }
    //metodo che aggiorna il model in base alla carta scelta dal client
    //ESISTE IL MODO PER ASCOLTARE SOLTANTO UN PLAYER?
    public int PlayCard(Card playedCard,Player player,int r,int c){
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
                player.getHand().remove(playedCard);
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
    public int drawPublicCards(Player player, int index){
        //draw =false
        int result=0;
        //se il giocatore non ha già pescato la carta ed è quello giusto,
        if (player.equals(currentPlayer) && draw){
            //aggiungi la carta pubblica alla sua mano
            if(!publicCards.isEmpty()) {
                player.getHand().add(publicCards.get(index));
                draw = false;
                //tutto liscio
                result = 1;
                //e rimpiazza il suo spazio con una carta
                //se la carta pescata era risorsa:
                if (!resourceDeck.isEmpty()) {
                    if (index < 2) publicCards.set(index, resourceDeck.poll());
                }
                //se la carta pescata era oro:
                if (!goldDeck.isEmpty()) {
                    if (index >= 2) publicCards.set(index, goldDeck.poll());
                }
            }else {
                result = -1;
            }
        }
        if(!player.equals(currentPlayer)) result=-2;
        return result;
    }
    public int drawFromTheDeck(Player player, int whichDeck){
        int result=0;
        if((player.equals(currentPlayer) && draw) | firstTurn){
            //firstTurn=false;
            //whichDeck è associato al click del client quando seleziona il deck da pescare
            //se è uguale a 0 prendo il deck risorsa se è =1 quello oro
            if(whichDeck==0) {
                if(!resourceDeck.isEmpty()) {
                    player.getHand().add(resourceDeck.poll());
                    draw=false;
                    result=1;
                }
                else result=-2;
            }
            if(whichDeck==1) {
                if(!goldDeck.isEmpty()) {
                    player.getHand().add(goldDeck.poll());
                    draw=false;
                    result=1;
                }
                else result=-3;
            }
        }
        if(!player.equals(currentPlayer)) result=-1;
        return result;
    }
    //metodo che aggiorna il current player,
    public GoalCard[] getPublicGoals() {
        return publicGoals;
    }

    public void setFirstTurn(boolean firstTurn) {
        this.firstTurn = firstTurn;
    }

    public void setPublicGoals(GoalCard[] publicGoals) {
        this.publicGoals = publicGoals;
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

    public Queue<Card> getStarterDeck() {
        return starterDeck;
    }

    public void setPlayerIndex(int playerIndex) {
        this.playerIndex = playerIndex;
    }
    public void shufflePlayers(){
        Collections.shuffle(this.players);
    }
    public boolean getGameEnd(){
        return this.gameEnd;
    }
}

