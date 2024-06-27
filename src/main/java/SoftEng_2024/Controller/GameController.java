package SoftEng_2024.Controller;
import SoftEng_2024.Model.Cards.*;
import SoftEng_2024.Model.ModelMessages.BroadcastMessage;
import SoftEng_2024.Model.ModelMessages.ChatErrorMessage;
import SoftEng_2024.Model.ModelMessages.OwnWhisperMessage;
import SoftEng_2024.Model.ModelMessages.WhisperMessage;
import SoftEng_2024.Model.Observers.BoardObserver;
import SoftEng_2024.Model.Observers.GameObserver;
import SoftEng_2024.Model.Observers.PlayerObserver;
import SoftEng_2024.Model.Player_and_Board.*;
import SoftEng_2024.Model.*;
import SoftEng_2024.Model.GoalCard.*;
import SoftEng_2024.Model.Enums.*;
import SoftEng_2024.Network.ServerMain;
import SoftEng_2024.Network.ToModel.NetworkManager;
import SoftEng_2024.Network.ToModel.ServerInterface;
import SoftEng_2024.Network.ToModel.SocketServer;
import SoftEng_2024.Network.ToView.ObServerManager;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static SoftEng_2024.Model.Cards.CardDeserializer.*;


/**
 * Controller class of the game. It provides all the methods that modify the model.
 * All the view messages call one of the GameController's methods.
 */
public class GameController {
    /**
     * the reference to the RMI server
     */
    private ServerInterface serverRMI;

    /**
     * the reference to the socket server
     */
    private SocketServer serverSocket;

    /**
     * the reference to the game class
     */
    private Game game;

    /**
     * list containing all the Players
     */
    private List<Player> clientPlayers = new ArrayList<>();

    /**
     * the number of players the game will have
     */
    private int maxPlayers=0;

    /**
     * hashMap that links the clientID to the Player object
     */
    private HashMap<Double,Player> playerIdMap= new HashMap<>();

    /**
     * list containing all the playerObservers
     */
    private List<PlayerObserver> playerObservers=new ArrayList<>();

    /**
     * reference to the object that manages messages from the model to the view
     */
    private ObServerManager toViewManager;

    /**
     * reference to the object that manages messages from the view to the model
     */
    private NetworkManager networkManager;

    /**
     * timer that starts when there's only one player online
     */
    private Timer terminationTimer;

    //METHODS**********************************************************************

    /**
     * Method that creates the Game with all the cards and the observer.
     * It is called at the beginning of the run method of the NetworkManager
     */
    public void gameInit() {
        // Instancing the decks as cards instead of queues, in order to shuffle them directly, then
        // converting them into queues
        // ------------------------------------------------------------------------------------------------------
        // Instancing the queues which will act as card decks - queues since we can easily pop the top card
        List<Card> resourceDeckTemp = new LinkedList<>();
        List<Card> goldDeckTemp = new LinkedList<>();
        List<Card> starterDeckTemp = new LinkedList<>();

        // Calling the deserialization methods from the SoftEng_2024.Model.Cards.CardDeserializer class, which then generates every single
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

        this.game = new Game(new ArrayList<>(this.clientPlayers), goldDeck, resourceDeck, starterDeck, goalCardDeck,networkManager);
        this.game.setGameObserver(new GameObserver(this.toViewManager,game,this.networkManager));

    }

    /**
     * Method that creates all the goalCards
     * @return the queue containing all the goal cards
     */
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
        goal = new ObjectsGoalCard(objects, points, String.format("Get %s points for each pair of feathers on the board", points), 102);
        goalCardArrayList.add(goal);


        objects = new Angles[]{Angles.SCROLL, Angles.SCROLL};
        goal = new ObjectsGoalCard(objects, points, String.format("Get %s points for each pair of scrolls on the board", points), 100);
        goalCardArrayList.add(goal);

        objects = new Angles[]{Angles.INK, Angles.INK};
        goal = new ObjectsGoalCard(objects, points, String.format("Get %s points for each pair of inks on the board", points), 101);
        goalCardArrayList.add(goal);

        points = 3;
        objects = new Angles[]{Angles.FEATHER, Angles.INK, Angles.SCROLL};
        goal = new ObjectsGoalCard(objects, points, String.format("Get %s points for each triplet of different objects on the board", points), 99);
        goalCardArrayList.add(goal);

        //ResourceGoal
        points = 2;
        resource = Angles.FUNGI;
        goal = new ResourceGoalCard(resource, points, String.format("Get %s points for each triplet of mushrooms on the board", points), 95);
        goalCardArrayList.add(goal);

        resource = Angles.PLANTS;
        goal = new ResourceGoalCard(resource, points, String.format("Get %s points for each triplet of plants on the board", points), 96);
        goalCardArrayList.add(goal);

        resource = Angles.INSECTS;
        goal = new ResourceGoalCard(resource, points, String.format("Get %s points for each triplet of insects on the board", points), 97);
        goalCardArrayList.add(goal);

        resource = Angles.ANIMALS;
        goal = new ResourceGoalCard(resource, points, String.format("Get %s points for each triplet of animals on the board", points), 98);
        goalCardArrayList.add(goal);

        //DiagonalGoal
        resource = Angles.FUNGI;
        tiltedForward = true;
        goal = new DiagonalGoalCard(resource, points, tiltedForward, String.format("Get %s points for each ascending diagonal pattern of mushroom cards on the board", points), 87);
        goalCardArrayList.add(goal);

        resource = Angles.PLANTS;
        tiltedForward = false;
        goal = new DiagonalGoalCard(resource, points, tiltedForward, String.format("Get %s points for each descending diagonal pattern of plant cards on the board", points), 88);
        goalCardArrayList.add(goal);

        resource = Angles.ANIMALS;
        tiltedForward = true;
        goal = new DiagonalGoalCard(resource, points, tiltedForward, String.format("Get %s points for each ascending diagonal pattern of animal cards on the board", points), 89);
        goalCardArrayList.add(goal);

        resource = Angles.INSECTS;
        tiltedForward = false;
        goal = new DiagonalGoalCard(resource, points, tiltedForward, String.format("Get %s points for each descending diagonal pattern of insect cards on the board", points), 90);
        goalCardArrayList.add(goal);

        //StepGoalCard
        points = 3;
        baseResource = Angles.PLANTS;
        sideResource = Angles.FUNGI;
        baseTop = false;
        sideLeft = true;
        goal = new StepGoalCard(baseResource, sideResource, baseTop, sideLeft, points, String.format("Get %s points for each regular L-shaped (L) pattern with an %s card at the base and a %s card on the side", points, baseResource, sideResource), 91);
        goalCardArrayList.add(goal);

        baseResource = Angles.INSECTS;
        sideResource = Angles.PLANTS;
        //baseTop = false;
        sideLeft = false;
        goal = new StepGoalCard(baseResource, sideResource, baseTop, sideLeft, points, String.format("Get %s points for each L-shaped (with the side on the right: ┘) pattern with an %s card at the base and a %s card on the side", points, baseResource, sideResource), 92);
        goalCardArrayList.add(goal);

        baseResource = Angles.FUNGI;
        sideResource = Angles.ANIMALS;
        baseTop = true;
        sideLeft = true;
        goal = new StepGoalCard(baseResource, sideResource, baseTop, sideLeft, points, String.format("Get %s points for each L-shaped (with the base on top: ┌) pattern with an %s card at the base and a %s card on the side", points, baseResource, sideResource), 93);
        goalCardArrayList.add(goal);

        baseResource = Angles.ANIMALS;
        sideResource = Angles.INSECTS;
        //baseTop = true;
        sideLeft = false;
        goal = new StepGoalCard(baseResource, sideResource, baseTop, sideLeft, points, String.format("Get %s points for each L-shaped (with the base on top and side on the right: ┐) pattern with an %s card at the base and a %s card on the side", points, baseResource, sideResource), 94);
        goalCardArrayList.add(goal);


        Collections.shuffle(goalCardArrayList);

        return new LinkedList<>(goalCardArrayList);
    }

    /**
     * Method that creates the game
     * The first player Object is created and the maxPlayers attribute is set
     * @param maxPlayers the user's choice that defines how many players the game will have
     * @param nickname the nickname that the user chose
     * @param ID univocal ID for the client object
     */
    public synchronized void createGame(int maxPlayers, String nickname, double ID)  {
        //if no one has already created the game, the number of maxPlayers is set and a new player is created
        if (clientPlayers.isEmpty()){
            this.maxPlayers=maxPlayers;
            System.out.println(nickname + " has created a game with " + maxPlayers + " players");
            joinGame(nickname, ID);
        }
        else{
            //the method sends back a message saying that the game has already been created.
            System.err.println(nickname + " attempted to create a game");
            game.getErrorMessageBindingMap().put(ID,"");
            sendErrorMessageAndUnRegister(ID,"You tried to create a game when there's an already existing one! Please try joining the game instead of creating one!");

        }
    }

    /**
     * method that makes a user join the game.
     * @param nickname the nickname chosen by the user
     * @param ID univocal ID for the client object
     */
    public synchronized void joinGame(String nickname, double ID)   {
        //The player is automatically inserted into the hashmap containing the IDs and acks and the one containing the errors
        if(!game.getAckIdBindingMap().containsKey(ID))
            game.getAckIdBindingMap().put(ID,false);
        if(!game.getErrorMessageBindingMap().containsKey(ID))
            game.getErrorMessageBindingMap().put(ID,"");
        if (!game.getGameState().equals(GameState.CONNECTION)){
            System.err.println(nickname + " attempted to join an already started game");
            //TODO: manda messaggio only reconnect
            sendErrorMessageAndUnRegister(ID,"The game has already started, you can only reconnect!!");
            return;
        }
        if(maxPlayers==0){
            System.err.println(nickname+ " tried to join a game that hasn't been created yet...");
            sendErrorMessageAndUnRegister(ID,"You tried to join a non existing game! You need to create a game before joining one!");
            return;
        }
        if(clientPlayers.isEmpty()) {
            System.out.println(nickname + " has joined the game");
            addPlayer(nickname, ID);
            playerIdMap.get(ID).setPlayerState(GameState.STARTER);
        }
        else if(clientPlayers.size()<maxPlayers){
            for(Player player: clientPlayers){
                if(player.getNickname().equals(nickname)){
                    System.err.println(nickname + " tried to join the game with a nickname already used by someone");
                    //TODO show "NickAlreadyChosenError + return
                    sendErrorMessageAndUnRegister(ID,"You chose an aleady existing nickname, please insert a new one!");
                    return;
                }
            }
            //if I exit the for loop, it means that the chosen nickname is original
            addPlayer(nickname,ID);
            playerIdMap.get(ID).setPlayerState(GameState.STARTER);
            System.out.println(nickname+ " has joined the game");
            if(clientPlayers.size()==maxPlayers)
                checkIfNextState();
        }
        else{
            System.out.println("[ERROR] Someone tried to join the game...");
            sendErrorMessageAndUnRegister(ID,"MaxPlayers already reached, please wait for the game to finish...Or for us to implement multiple games :)");
        }
    }

    /**
     * if this method is called before the beginning of the game, it will remove the player object of the caller
     * else, it will separate the client from the Player object.
     * If only one player remains online after disconnecting the caller, this method will start a timer. if this timer
     * expires, the remaining player will be declared as the winner and the program will end.
     * @param ID univocal ID for the client
     * @throws IOException if something goes wrong with the unRegisterClient method
     */
    public void quit(double ID) throws IOException {
        Player removedPlayer;
        //TODO: VALUTARE SE SERVE CONTROLLARE SE IL GIOCATORE SIA ISTATO AGGIUNTO ALLA LISTA DEI PLAYERS O MENO
        // CHE LA STAMPA COMMENTATA DAVA PROBLEMI

        serverRMI.unregisterClient(ID);
        serverSocket.unRegisterClient(ID);
        if(!playerIdMap.containsKey(ID)){
            return;
        }
        if (game.getGameState().equals(GameState.CONNECTION)){
            if(clientPlayers.size()==1)
                maxPlayers=0;
            removedPlayer = playerIdMap.remove(ID);
            clientPlayers.remove(removedPlayer);
            game.getPlayers().remove(removedPlayer);
            game.getAckIdBindingMap().remove(ID);
            game.getErrorMessageBindingMap().remove(ID);
            playerObservers.remove(removedPlayer.getPlayerObserver());
            //the observer linked to that player will be collected by the garbage collector
        }else{
            //if the game isn't in the connection state, the player will be set offline
            removedPlayer=playerIdMap.get(ID);
            if(removedPlayer.getIsOnline())
                game.setOnlinePlayersCounter(-1);
            removedPlayer.setOnline(false);
            game.getAckIdBindingMap().remove(ID);
            game.getErrorMessageBindingMap().remove(ID);
            //if the game is in play state and the player disconnected during their turn and there's at least two players still online, set them to not playing and make
            //another player start their turn
            if (game.getGameState().equals(GameState.PLAY) && removedPlayer.getPlayerState().equals(GameState.PLAY) & game.getOnlinePlayersCounter()!=1){
                playerIdMap.get(ID).setPlayerState(GameState.NOTPLAYING);
                game.turnEnd(playerIdMap.get(ID).getNickname());
                //TODO: SE è ARRIVATO A 20 PUNTI E POI QUITTA?
            //and if the game isn't already in the play state, it will check if all the other players already did their move
            }else if(game.getGameState().ordinal()<GameState.PLAY.ordinal()){
                checkIfNextState();
            }
            //and if the player disconnected during their turn but there's only 1 player still online, just set the removedPlayer state
            //to NOTPLAYING
            if(removedPlayer.getPlayerState().equals(GameState.PLAY))
                removedPlayer.setPlayerState(GameState.NOTPLAYING);

            int onlinePlayers = game.getOnlinePlayersCounter();

            if(onlinePlayers==1){
                //cancel the previous timer if it exists
                if(terminationTimer!=null){
                    terminationTimer.cancel();
                    terminationTimer.purge();
                }

                //create a new timer
                terminationTimer = new Timer();
                //after 5 seconds, if the number of players hasn't changed, it will end the game and notify the player still online
                TimerTask terminationTask = new TimerTask() {
                    @Override
                    public void run() {
                        //after 5 seconds, if the number of players hasn't changed, it will end the game and notify the player still online
                        if (game.getOnlinePlayersCounter() == 1) {
                            System.out.println("EXECUTING GAME TERMINATION ACTION, ONLY 1 PLAYER ONLINE");
                            game.triggerWinnerDueToForfeit();

                        }
                    }
                };

                //start the timer with a 5-seconds delay
                //TODO:CHANGE DELAY
                terminationTimer.schedule(terminationTask, 30000);

            }else if(onlinePlayers == 0){
                if(terminationTimer!=null){
                    terminationTimer.cancel();
                    terminationTimer.purge();
                }
                System.out.println("[ERROR] NO MORE PLAYERS IN GAME... ENDING GAME TERMINATION ACTION");
                ServerMain.quitAll();
            }
        }
    }

    /**
     * method that makes the user rejoin the game
     * it checks whether the player can or can't reconnect
     * @param nickname is the nickname that the user had before crashing/quitting
     * @param ID univocal ID for the client
     */
    public synchronized void reJoinGame(String nickname, double ID){
        if (game.getGameState().equals(GameState.CONNECTION)){
            if(maxPlayers==0)
                sendErrorMessageAndUnRegister(ID,"You tried to reconnect when the game hasn't been created. Try creating a game instead!");
            else
                sendErrorMessageAndUnRegister(ID,"You tried to reconnect when the game hasn't started. Try joining instead!");
            return;
        }
        //associate new id with the player in the hashmap by removing the old id
        for (Double playerId : playerIdMap.keySet()) {
            if(nickname.equals(playerIdMap.get(playerId).getNickname()) && !playerIdMap.get(playerId).getIsOnline()){
                Player player = playerIdMap.remove(playerId);
                playerIdMap.put(ID, player);
                game.getAckIdBindingMap().put(ID,true);
                game.getErrorMessageBindingMap().put(ID,"");

                //TODO playerIdMap.get(ID).setOnline(true); da valutare se messo in altro messaggio e metodo (recovered)
                System.out.println(nickname + " has reJoined and successfully remapped with the new ID: " + ID);
                PlayerObserver o=player.getPlayerObserver();
                o.setReceiverID(ID);
                o.playerRejoining(game);
                game.setAckIdBindingMap(ID,true);
                //if the state of the game is the same as the state of the player set online as true
                //this case includes the player being in not playing and the game being in play
                if(game.getGameState().ordinal() <= player.getPlayerState().ordinal()) {
                    player.setOnline(true);
                    game.setOnlinePlayersCounter(1);
                }

                if(player.getIsOnline() & game.getOnlinePlayersCounter()==2 & player.getPlayerState().ordinal()>game.getGameState().ordinal() & !game.getGameState().equals(GameState.PLAY)){
                    checkIfNextState();
                }
                else if( player.getIsOnline() & game.getOnlinePlayersCounter()==2 & player.getPlayerState().equals(GameState.NOTPLAYING) & game.getGameState().equals(GameState.PLAY)){
                    game.turnStart();
                }
                return;
            }
        }
        System.err.println(nickname+ " hasn't a mapped player, reJoin not available");
        sendErrorMessageAndUnRegister(ID,"The nickname you chose doesn't belong to anyone that is offline! Please insert the nickname you had before disconnecting...");

    }


    /**
     * method that initializes the player object and adds it to all the data structures
     * it also initializes the observer linked to the new player
     * @param nickname nickname chosen by the user
     * @param ID univocal ID for the client
     */
    private void addPlayer(String nickname, double ID) {

        Board board = new Board();

        Player newPlayer = new Player(new ArrayList<>(), board);

        this.clientPlayers.add(newPlayer);

        PlayerObserver newPlayerObserver=new PlayerObserver(toViewManager,ID,nickname);
        BoardObserver newBoardObserver=new BoardObserver(nickname,toViewManager);
        board.setObserver(newBoardObserver);

        for(Player p:clientPlayers){
            p.addObserver(newPlayerObserver);
        }

        for(PlayerObserver o:playerObservers){
            newPlayer.addObserver(o);
        }

        playerObservers.add(newPlayerObserver);
        //add the player to the binding HashMap to link players to their viewID
        playerIdMap.put(ID,newPlayer);

        newPlayer.setNickname(nickname,ID);
        game.getPlayers().add(newPlayer);
        game.setAckIdBindingMap(ID,true);
        game.setOnlinePlayersCounter(1);
    }




    /**
     * method that places the starter card at the center of the player's board. Directly calls the board methods
     * @param flipped the side on which the player chose to play the card
     * @param ID univocal ID for the client
     */
    public void playStarterCard(boolean flipped, double ID) {
        //places the starter card of the client calling the method
        try {
            Player player = playerIdMap.get(ID);
            player.getStarterCard().setFlipped(flipped);
            player.getPlayerBoard().updateBoard(42, 42, player.getStarterCard());
            player.setStarterCard(null);
            player.setPlayerState(GameState.SETCOLOR);
            if (player.getPlayerState().ordinal() >= game.getGameState().ordinal() & !player.getIsOnline()){
                player.setOnline(true);
                game.setOnlinePlayersCounter(1);
            }
            game.setAckIdBindingMap(ID,true);
        }catch(Board.notAvailableCellException | Board.necessaryResourcesNotAvailableException e){
            e.printStackTrace();
            throw new RuntimeException("Something went really wrong");
        }
        //if every player has placed the card, I change the status
        checkIfNextState();
    }



    /**
     * method that sets the player color according to their choice
     * @param color color chosen by the user
     * @param ID univocal ID for the client
     */
    public synchronized void setColor(Color color, double ID){
        Player currentPLayer=playerIdMap.get(ID);
        //checks if the player has chosen a color not yet taken
        for(Player player: clientPlayers){
            if(!player.getColor().isEmpty()) {
                if (color.equals(player.getColor().get(0))) {
                    sendErrorMessage(ID, color + " already taken, please insert a different one");
                    return;
                }
            }
        }
        currentPLayer.setColor(color);
        //if the player is offline and has recovered, put it online and not yet taken
        playerIdMap.get(ID).setPlayerState(GameState.CHOOSEGOAL);
        if(currentPLayer.getPlayerState().ordinal()>= game.getGameState().ordinal() & !currentPLayer.getIsOnline()) {
            currentPLayer.setOnline(true);
            game.setOnlinePlayersCounter(1);
        }

        game.setAckIdBindingMap(ID,true);
        //if each player has chosen the color I change the status
        checkIfNextState();
    }



    /**
     * method that draws the 2 public goals
     */
    public void updatePublicGoals() {
        GoalCard[] goalCards = new GoalCard[2];
        goalCards[0] = game.getGoalCardDeck().poll();
        goalCards[1] = game.getGoalCardDeck().poll();
        game.setPublicGoals(goalCards);
    }


    /**
     * method that sets the private goals chosen by the player
     * @param choice integer corresponding to one of the two private goals proposed
     * @param ID univocal ID for the client
     */
    public void choosePrivateGoals(int choice, double ID){
        //discards a private goal from the player attribute
        Player player=playerIdMap.get(ID);
        List<GoalCard> privateGoal=new ArrayList<>(player.getAvailableGoals());
        privateGoal.remove(2-choice);
        player.setAvailableGoals(privateGoal);
        game.setAckIdBindingMap(ID,true);
        player.setPlayerState(GameState.NOTPLAYING);
        if(!player.getIsOnline()) {
            player.setOnline(true);
            game.setOnlinePlayersCounter(1);
        }
        //checks if all the players had already chosen their private goal
        checkIfNextState();
    }

    /**
     * method that plays the card that the player chose to play. It calls the corresponding game method.
     * If something goes wrong, it will set the errorString linked to the player and the gameObserver will notify about it
     * @param card card that the user wants to play
     * @param row chosen row of the matrix
     * @param column chosen column of the matrix
     * @param flipped side on which to play the card
     * @param ID univocal ID for the caller client
     */
    public void playCard(int card, int row, int column, boolean flipped,double ID){
        Player player=playerIdMap.get(ID);
        player.getHand().get(card).setFlipped(flipped);
        int result= game.playCard(player.getHand().get(card), player,row,column);
        switch(result){
            case 1:
                game.setAckIdBindingMap(ID,true);
                //checks if the player reached 20 points, if that is true and everyone played their last turn, the game ends
                game.checkIfIsLastTurn();
                break;
            case -1:
                sendErrorMessage(ID,"You tried to place a card where you couldn't! Please try placing it in a different place.");
                break;
            case 0:
                sendErrorMessage(ID,"You don't have enough resources to play this card! Please choose a different one.");
                break;
            case -3:
                sendErrorMessage(ID,"You tried to place a card during another player's turn! Please wait for your turn to place a card.");
                break;
            case -2:
                sendErrorMessage(ID,"You've already placed a card this turn! You can only draw a card.");
                break;

        }
    }

    /**
     * method that makes the player draw from the deck they chose to draw from. It calls the corresponding game method.
     * If something goes wrong, it will set the errorString linked to the player and the gameObserver will notify about it
     * @param deck deck to draw from
     * @param ID univocal ID for the caller client
     */
    public void drawFromTheDeck(int deck,double ID){
        Player player=playerIdMap.get(ID);
        int result=game.drawFromTheDeck(player,deck);
        switch (result){
            case 1:
                game.setAckIdBindingMap(ID,true);
                playerIdMap.get(ID).setPlayerState(GameState.NOTPLAYING);
                game.turnEnd(player.getNickname());
                break;
            case -1:
                sendErrorMessage(ID,"You tried to draw a card during another player's turn! Please wait for your turn.");
                break;
            case -2:
                sendErrorMessage(ID,"You tried to draw from the resource deck but the deck is empty! Try drawing from the public cards or from the gold deck.");
                break;
            case -3:
                sendErrorMessage(ID,"You tried to draw from the gold deck but the deck is empty! Try drawing from the public cards or from the resource deck.");
                break;
            case 0:
                sendErrorMessage(ID,"You tried to draw a card but you've already drawn one! Please wait for your next turn to play and draw.");
                break;
        }

    }

    /**
     * method that makes the player draw the public card they chose from. It calls the corresponding game method.
     * If something goes wrong, it will set the errorString linked to the player and the gameObserver will notify about it
     * @param card integer corresponding to one of the 4 public cards
     * @param ID univocal ID for the caller client
     */
    public void drawFromPublicCards(int card,double ID){
        Player player=playerIdMap.get(ID);
        int result=game.drawPublicCards(player,card);
        switch (result){
            case 1:
                game.setAckIdBindingMap(ID,true);
                playerIdMap.get(ID).setPlayerState(GameState.NOTPLAYING);
                game.turnEnd(player.getNickname());

                break;
            case -1:
                sendErrorMessage(ID,"You tried to draw a card from the public cards but there are no more cards left!");
                break;
            case -2:
                sendErrorMessage(ID,"You tried to draw a card during another player's turn! Please wait for your turn.");
                break;
            case 0:
                sendErrorMessage(ID,"You tried to draw a card but you've already drawn one! Please wait for your next turn to play and draw.");
                break;
            case -3:
                sendErrorMessage(ID,"You tried to draw a card but there's no card in that position! Try inserting another index.");
        }
    }


    /**
     * method that sends a chat message to a specified user
     * @param receiver nickname of the receiver user
     * @param message message to send
     * @param ID univocal ID for the caller client
     */
    public void whisper(String receiver,String message,double ID){
        boolean found=false;
        for (Player player:clientPlayers){
            if(player.getNickname().equals(receiver)){
                found=true;
                //if the receiver nickname is found
                if(player.getIsOnline()) {
                    double receiverID=player.getPlayerObserver().getReceiverID();
                    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
                    LocalDateTime now = LocalDateTime.now();
                    String time=dtf.format(now);
                    //sends the message both to the receiver user and to the sender user
                    toViewManager.addModelMessageToQueue(new WhisperMessage(receiverID,message,playerIdMap.get(ID).getNickname(),time));
                    toViewManager.addModelMessageToQueue(new OwnWhisperMessage(ID,message,receiver,time));
                }
                else
                    sendChatErrorMessage(ID,"The player you're trying to whisper to isn't online! You can only whisper to online players...");
            }
        }
        if(!found)
            sendChatErrorMessage(ID,"You tried whispering to a non existing player!");
    }

    /**
     * broadcasts a message to every player in the game
     * @param message message to send
     * @param ID univocal ID for the caller client
     */
    public void broadcast(String message, double ID){
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        String time=dtf.format(now);
        String completeMsg="["+time+"] "+ playerIdMap.get(ID).getNickname()+": "+message;
        for(double playerID:playerIdMap.keySet()){
            toViewManager.addModelMessageToQueue(new BroadcastMessage(playerID,completeMsg));
        }
    }

    /**
     * method called by the network manager object which gives out the starter cards to all the players.
     */
    public void handOutStarterCards(){
        for(Player player: game.getPlayers()){
            //adding a starter card to the player's hand
            player.setStarterCard((StarterCard) game.getStarterDeck().poll());
        }
    }

    /**
     * method called by the network manager object which hands out the private goals to each player
     */
    public void handOutPrivateGoals(){
        for(Player player: game.getPlayers()){
            List<GoalCard> availableGoals=new ArrayList<>();
            availableGoals.add(game.getGoalCardDeck().poll());
            availableGoals.add(game.getGoalCardDeck().poll());
            player.setAvailableGoals(availableGoals);
        }
    }

    /**
     * method called by the network manager object which gives out the cards to all the players.
     * calls the corresponding game method
     */
    public void handOutCards(){
        for(Player player: game.getPlayers()){
            //if i'm giving out the cards to the last player, send everyone a message containing the top angles of the deck
            game.handOutCards(player, player.equals(game.getPlayers().get(maxPlayers - 1)));

        }
    }


//    private void checkingIfGameEnds(boolean gameEnd){
//        //if no one arrived at 20 points, a new turn starts
//        if(!gameEnd){
//            game.turnStart();
//        }
//        //else, the game state changes and the network manager stops taking messages
//        else{
//            game.setGameState(GameState.ENDGAME);
//        }
//    }

    /**
     * method that calls the setters of the game attributes which contain the last result of the player move.
     * these setters will trigger the gameObserver which will notify the player that an error occurred
     * @param ID univocal ID for the client that needs to receive the error message
     * @param ErrorMessage string containing the error message
     */
    private void sendErrorMessage(double ID,String ErrorMessage){
        game.setErrorMessageBindingMap(ID,ErrorMessage);
        game.setAckIdBindingMap(ID,false);
    }


    /**
     * auxiliary method that assures that both the error and ack messages arrive before unregistering the client from the
     * corresponding server
     * @param ID univocal ID for the client that needs to receive the ack followed by the error message
     * @param ErrorMessage string containing the error message
     */
    private void sendErrorMessageAndUnRegister(double ID,String ErrorMessage){
        game.setAckAndError(ID,ErrorMessage);
    }

    /**
     * method that checks whether every player has made their move before switching to the next game state
     *     it won't switch the game state if there's only one player still online
     */
    private synchronized void checkIfNextState(){
            if(1 == game.getOnlinePlayersCounter()){
                return;
            }
            boolean found=false;
            for(Player player: clientPlayers){
                //if the player is online, I check its status, otherwise I skip it
                if(player.getIsOnline()){
                    //if the player is online, I check if its status is the same as that of the game
                    //so whether it performed the state operation or not
                    if(player.getPlayerState().ordinal()<= game.getGameState().ordinal()){
                        found=true;
                    }
                }
                else
                    System.err.println(player.getNickname());
            }
            if(!found){
                //if the online players have all done the operation, I call a new state
                GameState nextState= game.getGameState().nextState();
                game.setGameState(nextState);
            }
    }


    //GETTERS AND SETTERS*********************************************

    /**
     * Gets the ObServerManager for the view.
     *
     * @return the ObServerManager for the view.
     */
    public ObServerManager getToViewManager() {
        return toViewManager;
    }

    /**
     * Sets the ObServerManager for the view.
     *
     * @param toViewManager the ObServerManager to set for the view.
     */
    public void setToViewManager(ObServerManager toViewManager) {
        this.toViewManager = toViewManager;
    }

    /**
     * Sets the ServerInterface for RMI communication.
     *
     * @param serverRMI the ServerInterface to set for RMI communication.
     */
    public void setServerRMI(ServerInterface serverRMI) {
        this.serverRMI = serverRMI;
    }

    /**
     * Sets the SocketServer for socket communication.
     *
     * @param serverSocket the SocketServer to set for socket communication.
     */
    public void setServerSocket(SocketServer serverSocket) {
        this.serverSocket = serverSocket;
    }

    /**
     * Gets the current Game instance.
     *
     * @return the current Game instance.
     */
    public Game getGame() {
        return game;
    }

    /**
     * Gets the list of client players.
     *
     * @return the list of client players.
     */
    public List<Player> getClientPlayers() {
        return clientPlayers;
    }

    /**
     * Sets the list of client players.
     *
     * @param clientPlayers the list of client players to set.
     */
    public void setClientPlayers(List<Player> clientPlayers) {
        this.clientPlayers = clientPlayers;
    }

    /**
     * Sets the NetworkManager for managing network operations.
     *
     * @param networkManager the NetworkManager to set for managing network operations.
     */
    public void setNetworkManager(NetworkManager networkManager) {
        this.networkManager = networkManager;
    }

    /**
     * Sends a chat error message to the view manager.
     *
     * @param ID the ID of the chat message.
     * @param errorString the error message to send.
     */
    private void sendChatErrorMessage(double ID, String errorString){
        toViewManager.addModelMessageToQueue(new ChatErrorMessage(ID,errorString));
    }
}
