package SoftEng_2024.Controller;
import SoftEng_2024.Model.Cards.*;
import SoftEng_2024.Model.Player_and_Board.*;
import SoftEng_2024.Model.*;
import SoftEng_2024.Model.GoalCard.*;
import SoftEng_2024.Model.Enums.*;
import SoftEng_2024.Network.ToModel.RMIServer;
import SoftEng_2024.Network.ToModel.ServerInterface;
import SoftEng_2024.Network.ToModel.SocketClientHandler;
import SoftEng_2024.Network.ToModel.SocketServer;


import java.rmi.RemoteException;
import java.util.*;

import static SoftEng_2024.Model.Cards.CardDeserializer.*;

public class GameController {

    private ServerInterface serverRMI;
    private SocketServer serverSocket;

    private Game game;
    private List<Player> clientPlayers = new ArrayList<>();
    private int maxPlayers;
    private HashMap<Double,Player> playerIdMap= new HashMap<Double, Player>();
    GameState gameState;
    //private List<Player>

    //METHODS**********************************************************************

    //Method that creates the Game with all the cards.
    public void gameInit() {
        // Instancing the decks as cards instead of queues, in order to shuffle them directly, then
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
        Queue<GoalCard> goalCardDeck = goalInit();

        this.game = new Game(new ArrayList<>(this.clientPlayers), goldDeck, resourceDeck, starterDeck, goalCardDeck);


        //Draw from the decks the cards visible on the table
        for (int i = 0; i < 2; i++) {
            game.getPublicCards().add(game.getResourceDeck().poll());
        }
        for (int i = 0; i < 2; i++) {
            game.getPublicCards().add(game.getGoldDeck().poll());
        }
    }
    //Method that creates all the goalCards
    private Queue<GoalCard> goalInit() {
        List<GoalCard> goalCardArrayList = new ArrayList<>();
        GoalCard goal;
        Angles[] objects;
        Angles resource;
        boolean tiltedForward;
        Angles baseResource;
        Angles sideResource;
        boolean baseTop;
        boolean sideLeft;
        int points;

        //ObjectsGoal
        points = 2;

        objects = new Angles[]{Angles.FEATHER, Angles.FEATHER};
        goal = new ObjectsGoalCard(objects, points, String.format("Get %s points for each pair of feathers on the board", points));
        goalCardArrayList.add(goal);


        objects = new Angles[]{Angles.SCROLL, Angles.SCROLL};
        goal = new ObjectsGoalCard(objects, points, String.format("Get %s points for each pair of scrolls on the board", points));
        goalCardArrayList.add(goal);

        objects = new Angles[]{Angles.INK, Angles.INK};
        goal = new ObjectsGoalCard(objects, points, String.format("Get %s points for each pair of inks on the board", points));
        goalCardArrayList.add(goal);

        points = 3;
        objects = new Angles[]{Angles.FEATHER, Angles.INK, Angles.SCROLL};
        goal = new ObjectsGoalCard(objects, points, String.format("Get %s points for each triplet of different objects on the board", points));
        goalCardArrayList.add(goal);

        //ResourceGoal
        points = 2;
        resource = Angles.FUNGI;
        goal = new ResourceGoalCard(resource, points, String.format("Get %s points for each triplet of mushrooms on the board", points));
        goalCardArrayList.add(goal);

        resource = Angles.PLANTS;
        goal = new ResourceGoalCard(resource, points, String.format("Get %s points for each triplet of plants on the board", points));
        goalCardArrayList.add(goal);

        resource = Angles.INSECTS;
        goal = new ResourceGoalCard(resource, points, String.format("Get %s points for each triplet of insects on the board", points));
        goalCardArrayList.add(goal);

        resource = Angles.ANIMALS;
        goal = new ResourceGoalCard(resource, points, String.format("Get %s points for each triplet of animals on the board", points));
        goalCardArrayList.add(goal);

        //DiagonalGoal
        resource = Angles.FUNGI;
        tiltedForward = true;
        goal = new DiagonalGoalCard(resource, points, tiltedForward, String.format("Get %s points for each ascending diagonal pattern of mushroom cards on the board", points));
        goalCardArrayList.add(goal);

        resource = Angles.PLANTS;
        tiltedForward = false;
        goal = new DiagonalGoalCard(resource, points, tiltedForward, String.format("Get %s points for each descending diagonal pattern of plant cards on the board", points));
        goalCardArrayList.add(goal);

        resource = Angles.ANIMALS;
        tiltedForward = true;
        goal = new DiagonalGoalCard(resource, points, tiltedForward, String.format("Get %s points for each ascending diagonal pattern of animal cards on the board", points));
        goalCardArrayList.add(goal);

        resource = Angles.INSECTS;
        tiltedForward = false;
        goal = new DiagonalGoalCard(resource, points, tiltedForward, String.format("Get %s points for each descending diagonal pattern of insect cards on the board", points));
        goalCardArrayList.add(goal);

        //StepGoalCard
        points = 3;
        baseResource = Angles.PLANTS;
        sideResource = Angles.FUNGI;
        baseTop = false;
        sideLeft = true;
        goal = new StepGoalCard(baseResource, sideResource, baseTop, sideLeft, points, String.format("Get %s points for each regular L-shaped (L) pattern with an %s card at the base and a %s card on the side", points, baseResource, sideResource));
        goalCardArrayList.add(goal);

        baseResource = Angles.INSECTS;
        sideResource = Angles.PLANTS;
        baseTop = false;
        sideLeft = false;
        goal = new StepGoalCard(baseResource, sideResource, baseTop, sideLeft, points, String.format("Get %s points for each L-shaped (with the side on the right: ┘) pattern with an %s card at the base and a %s card on the side", points, baseResource, sideResource));
        goalCardArrayList.add(goal);

        baseResource = Angles.FUNGI;
        sideResource = Angles.ANIMALS;
        baseTop = true;
        sideLeft = true;
        goal = new StepGoalCard(baseResource, sideResource, baseTop, sideLeft, points, String.format("Get %s points for each L-shaped (with the base on top: ┌) pattern with an %s card at the base and a %s card on the side", points, baseResource, sideResource));
        goalCardArrayList.add(goal);

        baseResource = Angles.ANIMALS;
        sideResource = Angles.INSECTS;
        baseTop = true;
        sideLeft = false;
        goal = new StepGoalCard(baseResource, sideResource, baseTop, sideLeft, points, String.format("Get %s points for each L-shaped (with the base on top and side on the right: ┐) pattern with an %s card at the base and a %s card on the side", points, baseResource, sideResource));
        goalCardArrayList.add(goal);


        Collections.shuffle(goalCardArrayList);

        return new LinkedList<>(goalCardArrayList);
    }
    //Method that creates the game
    public synchronized void createGame(int maxPlayers, String nickname, double ID) throws RemoteException {
        //if no one has already created the game, the number of maxPlayers is set and a new player is created
        if (clientPlayers.isEmpty()){
            this.maxPlayers=maxPlayers;
            gameInit();
            joinGame(nickname, ID);
        }
        else{
            //the method sends back a message saying that the game has already been created.
            System.err.println(nickname + " attempted to create a game");
            serverRMI.unregisterClient(ID);
            //serverSocket.unregisterClient(ID);
            //notify to the correct viewID
        }
    }
    public synchronized void joinGame(String nickname, double ID) throws RemoteException {

        if (!gameState.equals(GameState.CONNECTION)){
            //TODO: manda messaggio only reconnect
            return;
        }

        if(clientPlayers.isEmpty()) {
            addPlayer(nickname, ID);
        }
        else if(clientPlayers.size()<maxPlayers){
            for(Player player: clientPlayers){
                if(player.getNickname().equals(nickname)){
                    //TODO show "NickAlreadyChosenError + return
                }
            }
            //se esco dal for, significa che il nickname scelto è originale
            addPlayer(nickname,ID);
            System.out.println(nickname+ " has joined the game");
            //controllo se il player collegato è l'ultimo
            if(clientPlayers.size()==maxPlayers){
                gameState=GameState.STARTER;
                game.shufflePlayers();
                //TODO notify game status update
                //--> per le notify degli status ha senso far partire il thread?
                //Rischio che il thread di notify status venga eseguito dopo l'update delle hands.
            }
        }
        else{
            System.err.println("Someone tried to join the game...");
            serverRMI.unregisterClient(ID);
            //serverSocket.unregisterClient(ID);
            //TODO show error maxPlayerReached
        }
    }

    public void quit(double ID) throws RemoteException{
        //TODO disconnetti client e resilienza alle disconnessioni
        serverRMI.unregisterClient(ID);
        //serverSocket.unregisterClient(ID);
    }

    private void addPlayer(String nickname, double ID) {
        //istanzio la board
        Board board = new Board();
        //istanzio il player e gli assegno la board
        Player newPlayer = new Player(new ArrayList<>(), board, nickname);
        //lo aggiungo alla lista di player del controller
        this.clientPlayers.add(newPlayer);
        //add the player to the binding HashMap to link players to their viewID
        playerIdMap.put(ID,newPlayer);
        //e lo aggiungo al game
        game.getPlayers().add(newPlayer);
        System.out.println("Player "+ nickname+ " added to the game...");
    }

    public void reJoinGame(String nickname, double ID){

        if (gameState.equals(GameState.CONNECTION)){
            //TODO: manda messaggio only connect
            return;
        }

        //associa nuovo id al player nell'hashmap rimuovendo il vecchio id
        for (Double playerId : playerIdMap.keySet()) {

            if(nickname.equals(playerIdMap.get(playerId).getNickname())){
                Player player = playerIdMap.remove(playerId);
                playerIdMap.put(ID, player);
                System.out.println(nickname + " has reJoined and successfully remapped with the new ID: " + ID);
                //notify reJoin Observer
                return;
            }
        }
        System.err.println(nickname+ " hasn't a mapped player, reJoin not available");
        //TODO notify observer for a failed reJoin by "nickname"
    }

    public void playStarterCard(boolean flipped, double ID) {
        //piazza la carta starter del client che chiama il metodo
        try {
            Player player = playerIdMap.get(ID);
            player.getHand().get(0).setFlipped(flipped);
            player.getPlayerBoard().updateBoard(42, 42, player.getHand().remove(0));
        }catch(Board.notAvailableCellException | Board.necessaryResourcesNotAvailableException e){
            e.printStackTrace();
            throw new RuntimeException("Something went very wrong");
        }
        //per ogni player controlla se è stata piazzata la starter
        int counter=0;
        for(Player player:game.getPlayers()){
            if(player.getPlayerBoard().getCardList().size()==1){
                counter++;
            }
        }
        //se ogni player ha piazzato la carta, cambio lo stato
        if(counter==maxPlayers){
            gameState=GameState.SETCOLOR;
            //notify change in gameState
        }
        //notify observer for all public info and hand observer for ID player
    }

    public synchronized void setColor(Color color, double ID){
        int counter=0;
        boolean found = false;
        //controlla se il player ha scelto un colore non ancora preso
        for(Player player: clientPlayers){
            if(color.equals(player.getColor().get(0))){
                //ERROR, CLIENT HAS TO CHOOSE ANOTHER COLOR, SEND MESSAGE
                found = true;
            }
            if(!player.getColor().isEmpty()){
                counter++;
            }
        }
        if(found | counter==maxPlayers){
            //notify dell'errore
            return;
        }
        playerIdMap.get(ID).setColor(color);
        //avendo già contato il numero di giocatori che hanno già scelto il colore, basterà che il numero
        //di giocatori che hanno scelto il colore sia maxPlayers - 1, ovvero quello che l'ha scelto ora
        if(counter==maxPlayers-1){
            //notify change of state
            gameState=GameState.CHOOSEGOAL;
        }

        //parte un nuovo thread che controlla se tutti hanno scelto il colore.
        //se l'esito è positivo,
        //Thread t = new Thread(() -> {
          //  for(Player player:clientPlayers){
            //    if(player.getColor().isEmpty())
                    return;
            //}
            //updatePlayerHands();
            //updatePublicGoals();
            //FOR EACH OBSERVER NOTIFY (ALTRI THREAD PARTONO FORSE)
        //});
        //t.start();

        //notify observers
    }
    private void updatePlayerHands() {

        game.setFirstTurn(true);
        //dà le carte a tutti i giocatori
        for (Player player : game.getPlayers()) {

            game.drawFromTheDeck(player, 0);

            game.drawFromTheDeck(player, 0);

            game.drawFromTheDeck(player, 1);

        }
        game.setFirstTurn(false);
        //notify each client view for their own hand
    }

    private void updatePublicGoals() {
        GoalCard[] goalCards = new GoalCard[2];
        goalCards[0] = game.getGoalCardDeck().poll();
        goalCards[1] = game.getGoalCardDeck().poll();
        game.setPublicGoals(goalCards);
        //notify all clients for public goals
    }
    public void choosePrivateGoals(int choice, double ID){
        //discards a private goal from the player attrbute
        Player player=playerIdMap.get(ID);
        player.getAvailableGoals().remove(2-choice);
        //checks if all the players had already chosen their private goal
        int counter=0;
        for(Player player1: game.getPlayers()){
            if(player1.getAvailableGoals().size()==1)
                counter++;
        }
        if(counter==maxPlayers){
            gameState=GameState.PLAY;
            //notify we are ready to start the game
        }

    }
    public void playCard(int card, int row, int column, boolean flipped,double ID){
        Player player=playerIdMap.get(ID);
        player.getHand().get(card).setFlipped(flipped);
        game.PlayCard(player.getHand().get(card), player,row,column);
        //notify : in base a quello che ritorna il metodo playCard, notifico gli observer.
    }
    public void drawFromTheDeck(int deck,double ID){
        Player player=playerIdMap.get(ID);
        game.drawFromTheDeck(player,deck);
        //notify the right observers
    }
    public void drawFromPublicCards(int card,double ID){
        Player player=playerIdMap.get(ID);
        game.drawPublicCards(player,card);
        //notify the right observers
    }
    public void handOutStarterCards(){
        for(Player player: game.getPlayers()){
            //aggiungo una carta starter alla mano del player
            player.setHand(game.getStarterDeck().poll());
        }
    }
    //SONO AGGIUNTI ANCHE DENTRO ADD PLAYER, DA VEDERE SE HA SENSO QUESTO METODO O MENO
    public void handOutPrivateGoals(){
        for(Player player: game.getPlayers()){
            //aggiungo due goals
            player.getAvailableGoals().add(game.getGoalCardDeck().poll());
            player.getAvailableGoals().add(game.getGoalCardDeck().poll());
        }
    }
    public void handOutCards(){
        for(Player player: game.getPlayers()){
            //aggiungo due carte risorsa alla mano del player
            player.setHand(game.getResourceDeck().poll());
            player.setHand(game.getResourceDeck().poll());
            //aggiungo una carta ora alla mano
            player.setHand(game.getGoldDeck().poll());
        }
    }
    //method that checks if someone has arrived at 20 points by calling drawCard inside the game class
    private void checkingIfGameEnds(boolean gameEnd){
        //if no one arrived at 20 points, a new turn starts
        if(!gameEnd){
            game.TurnStart();
        }
        //else, the game state changes and the network manager stops taking messages
        else gameState=GameState.ENDGAME;
    }

    //GETTERS AND SETTERS*********************************************

    public void setServerRMI(ServerInterface serverRMI) {
        this.serverRMI = serverRMI;
    }

    public void setServerSocket(SocketServer serverSocket) {
        this.serverSocket = serverSocket;
    }

    public Game getGame() {
        return game;
    }

    public List<Player> getClientPlayers() {
        return clientPlayers;
    }

    public void setClientPlayers(List<Player> clientPlayers) {
        this.clientPlayers = clientPlayers;
    }

    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }

    public GameState getGameState() {
        return gameState;
    }


}
