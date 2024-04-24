package SoftEng_2024.Controller;

import SoftEng_2024.Model.Cards.*;
import SoftEng_2024.Model.Cards.ResourceCard;
import SoftEng_2024.Model.Player;
import SoftEng_2024.Model.*;
import SoftEng_2024.Model.Enums.*;


import java.util.*;


import static SoftEng_2024.Model.Cards.CardDeserializer.*;

/*
1) chiama il costruttore del game con players
2) distribuisce le starter
3)gioca le starter prendendo l'imput
   updateboard(42,42,scanner)
*/

public class GameInit {
    private Game game;
    private List<Player> clientPlayers=new ArrayList<>();
    //Queue goldDeck;
    public void gameInit() {
        // TO DO LATER - Instancing the decks as cards instead of queues, in order to shuffle them directly, then
        // converting them into queues
        // ------------------------------------------------------------------------------------------------------
        // Instancing the queues which will act as card decks - queues since we can easily pop the top card
        List<Card> resourceDeckTemp = new LinkedList<>();
        List<Card> goldDeckTemp = new LinkedList<>();
        List<Card> starterDeckTemp = new LinkedList<>();

        // Calling the deserialization methods from the CardDeserializer class, which then generates every single
        // card from the information stored in the .json files. Every card is then added to the corresponding deck
        resourceCardDeserialize(resourceDeckTemp);
        goldCardDeserialize(goldDeckTemp);
        starterCardDeserialize(starterDeckTemp);

        Collections.shuffle(resourceDeckTemp);
        Collections.shuffle(goldDeckTemp);
        Collections.shuffle(starterDeckTemp);

        // Creating a temporary copy of the queues, but using lists instead, since the queue collection does not allow
        // calling the shuffle method
        Queue<Card> resourceDeck = new LinkedList<>(resourceDeckTemp);
        Queue<Card> goldDeck = new LinkedList<>(goldDeckTemp);
        Queue<Card> starterDeck = new LinkedList<>(starterDeckTemp);

        //costruisco i 16 goal, inserisco in una lista, faccio shuffle, aggiungo alla coda
        List<GoalCard> goalCardArrayList = new ArrayList<>();
        GoalCard goal;
        Angles[] objects;
        Angles resource;
        boolean tiltedForward;
        Angles baseResource;
        Angles sideResource;
        boolean baseTop;
        boolean sideLeft;

        //ObjectsGoal
        objects = new Angles[]{Angles.FEATHER, Angles.FEATHER};
        goal = new ObjectsGoalCard(objects,2);
        goalCardArrayList.add(goal);

        objects = new Angles[]{Angles.SCROLL, Angles.SCROLL};
        goal = new ObjectsGoalCard(objects,2);
        goalCardArrayList.add(goal);

        objects = new Angles[]{Angles.INK, Angles.INK};
        goal = new ObjectsGoalCard(objects,2);
        goalCardArrayList.add(goal);

        objects = new Angles[]{Angles.FEATHER, Angles.INK, Angles.SCROLL};
        goal = new ObjectsGoalCard(objects,3);
        goalCardArrayList.add(goal);

        //ResourceGoal
        resource = Angles.FUNGI;
        goal = new ResourceGoalCard(resource, 2);
        goalCardArrayList.add(goal);

        resource = Angles.PLANTS;
        goal = new ResourceGoalCard(resource, 2);
        goalCardArrayList.add(goal);

        resource = Angles.INSECTS;
        goal = new ResourceGoalCard(resource, 2);
        goalCardArrayList.add(goal);

        resource = Angles.ANIMALS;
        goal = new ResourceGoalCard(resource, 2);
        goalCardArrayList.add(goal);

        //DiagonalGoal
        resource = Angles.FUNGI;
        tiltedForward = true;
        goal = new DiagonalGoalCard(resource, 2, tiltedForward);
        goalCardArrayList.add(goal);

        resource = Angles.PLANTS;
        tiltedForward = false;
        goal = new DiagonalGoalCard(resource, 2, tiltedForward);
        goalCardArrayList.add(goal);

        resource = Angles.ANIMALS;
        tiltedForward = true;
        goal = new DiagonalGoalCard(resource, 2, tiltedForward);
        goalCardArrayList.add(goal);

        resource = Angles.INSECTS;
        tiltedForward = false;
        goal = new DiagonalGoalCard(resource, 2, tiltedForward);
        goalCardArrayList.add(goal);

        //StepGoalCard
        baseResource = Angles.PLANTS;
        sideResource = Angles.FUNGI;
        baseTop = false;
        sideLeft = true;
        goal = new StepGoalCard(baseResource, sideResource, baseTop, sideLeft, 3);
        goalCardArrayList.add(goal);

        baseResource = Angles.INSECTS;
        sideResource = Angles.PLANTS;
        baseTop = false;
        sideLeft = false;
        goal = new StepGoalCard(baseResource, sideResource, baseTop, sideLeft, 3);
        goalCardArrayList.add(goal);

        baseResource = Angles.FUNGI;
        sideResource = Angles.ANIMALS;
        baseTop = true;
        sideLeft = true;
        goal = new StepGoalCard(baseResource, sideResource, baseTop, sideLeft, 3);
        goalCardArrayList.add(goal);

        baseResource = Angles.ANIMALS;
        sideResource = Angles.INSECTS;
        baseTop = true;
        sideLeft = false;
        goal = new StepGoalCard(baseResource, sideResource, baseTop, sideLeft, 3);
        goalCardArrayList.add(goal);


        Collections.shuffle(goalCardArrayList);
        Queue<GoalCard> goalCardDeck = new LinkedList<>(goalCardArrayList);
        //metodo che istanzia tutto di paolo
        //istanzio il game con i parametri del Json

        this.game = new Game(this.clientPlayers, goldDeck, resourceDeck, starterDeck, goalCardDeck); //parametri paolo.

        for(int i=0; i<2; i++){
            game.getPublicCards().add(game.getResourceDeck().poll()); //poll toglie la carta dal deck a differenza di peek che
            // "ne restituisce una copia" non so se è un problema che public cards inizialmente sia vuoto. non mi viene un metodo diverso per riempire al difuori dei due cicli for orribili
        }
        for(int i=0; i<2; i++){
            game.getPublicCards().add(game.getGoldDeck().poll());
        }
        for(Player player: game.getPlayers()){
            //ad ogni player viene aggiunta una carta dello starter deck
            player.setHand(game.getStarterDeck().poll());
        }

    }

    public void playInit(boolean flipped, Player player) throws Board.notAvailableCellException, Board.necessaryResourcesNotAvailable {
        //piazza la carta starter del client che chiama il metodo
        player.getHand().get(0).setFlipped(flipped);
        //player.getPlayerBoard().getCardBoard()[42][42].setPlaceable(true);
        player.getPlayerBoard().updateBoard(42,42,player.getHand().remove(0));
    }


    // dentro starter card c'è gia flipped modificato nella view usiamo questa per non mettere la starter nella hand del player
/*
    public void playInit(StarterCard starterCard, Player player) throws Board.notAvailableCellException, Board.necessaryResourcesNotAvailable {
        //piazza la carta starter del client che chiama il metodo
        player.getPlayerBoard().updateBoard(42,42,starterCard);
    }
*/
    //giocatore pesca due carte risorsa e una oro
    //pesca i publicGoals
    public void updatePlayerHands(){
        //dà le carte a tutti i giocatori
        for(Player player: game.getPlayers()){
            game.drawFromTheDeck(player,0);
            game.setFirstTurn(true);
            game.drawFromTheDeck(player,0);
            game.setFirstTurn(true);
            game.drawFromTheDeck(player,1);
            game.setFirstTurn(true);
        }

    }
    public void updatePublicGoals(){
        GoalCard[] goalCards = new GoalCard[2];
        goalCards[0]=game.getGoalCardDeck().poll();
        goalCards[1]=game.getGoalCardDeck().poll();
        game.setPublicGoals(goalCards);

    }
    public void privateGoal(GoalCard goalCard, Player player){
        player.setGoalCard(goalCard);
    }
    /*
    public void PrivateGoal(int i, Player player){
        player.setGoalCard(goalCard[i]);

    }
    for(Player player: game.getPlayers()){
            //assegno due goalcard ad ogni giocatore in un array
    }
    */
    //addPlayer aggiunge i giocatori all'attributo della classe
    public void addPlayer(){
        //istanzio la board
        Board board= new Board();
        //istanzio il player e gli assegno la board
        Player newPlayer= new Player(new ArrayList<>(),board);
        //lo aggiungo alla lista di player del controller
        this.clientPlayers.add(newPlayer);
    }

    public Game getGame() {
        return game;
    }

    public List<Player> getClientPlayers() {
        return clientPlayers;
    }
}
