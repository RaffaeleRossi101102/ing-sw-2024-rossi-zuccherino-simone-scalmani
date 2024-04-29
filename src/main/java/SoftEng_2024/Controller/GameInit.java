package SoftEng_2024.Controller;

import SoftEng_2024.Model.Cards.*;
import SoftEng_2024.Model.Cards.ResourceCard;
import SoftEng_2024.Model.Player;
import SoftEng_2024.Model.*;
import SoftEng_2024.Model.Enums.*;
import SoftEng_2024.Network.ClientInterface;
import SoftEng_2024.Network.RMIServer;
import SoftEng_2024.Network.ServerInterface;


import java.math.*;
import java.io.Serializable;
import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;

import java.util.*;


import static SoftEng_2024.Model.Cards.CardDeserializer.*;

/*
1) chiama il costruttore del game con players
2) distribuisce le starter
3)gioca le starter prendendo l'imput
   updateboard(42,42,scanner)
*/

public class GameInit {
    private volatile Game game;
    private volatile List<Player> clientPlayers = new ArrayList<>();
    private volatile HashMap<Player, ServerInterface> playerServerBindingMap = new HashMap<>();
    private volatile List<ServerInterface> servers = new ArrayList<>();
    private volatile int maxPlayers = 0;


    //Queue goldDeck;
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
        //metodo che istanzia tutto di paolo
        //istanzio il game con i parametri del Json

        this.game = new Game(new ArrayList<>(this.clientPlayers), goldDeck, resourceDeck, starterDeck, goalCardDeck); //parametri paolo.

        for (int i = 0; i < 2; i++) {
            game.getPublicCards().add(game.getResourceDeck().poll()); //poll toglie la carta dal deck a differenza di peek che
            // "ne restituisce una copia" non so se è un problema che public cards inizialmente sia vuoto. non mi viene un metodo diverso per riempire al difuori dei due cicli for orribili
        }
        for (int i = 0; i < 2; i++) {
            game.getPublicCards().add(game.getGoldDeck().poll());
        }


    }

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
    public void run(ServerInterface serverRMI) throws RemoteException, InterruptedException, RuntimeException, AlreadyBoundException, Board.necessaryResourcesNotAvailableException, Board.notAvailableCellException {
        //salva in una lista i server collegati
        //ServerInterface serverRMI=new RMIServer(this);
        //serverRMI.run();
        addServers(serverRMI);
        //metodo che controlla il flusso di gioco
        //si devono collegare tutti i client
        //aspetto che tutti i client si connettano
        // System.out.println("asghstjsthegfagrthy"+servers.size());
        for (ServerInterface server : servers) {
            server.showControllerMessage("Waiting for clients to connect...");
        }
        //System.out.println(maxPlayers + " "+ clientPlayers.size());
        while (maxPlayers == 0 | clientPlayers.size() < maxPlayers) {
            //aspetto
            // if(maxPlayers!=0) System.out.println(maxPlayers+ " "+ clientPlayers.size());
        }
        notifyAllClients("We are ready to start the game!");
        game.shufflePlayers();
        //per ogni client dentro ogni server, viene chiamato il metodo playStarterCard
        for (ServerInterface server : servers) {
            try {
                server.playStarterCard();
                server.setColor();
                //ricorda la pedina nera
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        }
        //se sono uscito dal for
        try {
            //do a tutti le carte
            updatePlayerHands();
            //mette sul terreno i public goals e li mostra a tutti
            updatePublicGoals();
            GoalCard[] privateGoals = new GoalCard[2];
            //per ogni client
            for (ClientInterface clientInterface : servers.get(0).getClients().keySet()) {
                //fa vedere due goal
                showPrivateGoals(clientInterface);
            }
            //aspetto che tutti i client abbiano scelto l'obiettivo
            for (Player player : clientPlayers) {
                //se trovo un player che non ha scelto l'obiettivo mi fermo
                while (player.getGoal() == null) {
                }
            }
            //se sono uscito significa che tutti hanno scelto l'obiettivo e il gioco può iniziare
            //prendo il player randomico per iniziare il turno
            notifyAllClients("The first turn is starting!");
            game.TurnStart();
            servers.get(0).notifyAllClients(game.getCurrentPlayer().getNickname() + " is your turn, play a card!");
            //modifico l'attributo booleano gameStart di ogni client e li faccio uscire dal ciclo while
            for (ClientInterface client : servers.get(0).getClients().keySet()) {
                client.setStartGame(true);
            }
            //da qui in poi si è in ascolto delle richieste dei client
            while (!game.getGameEnd()) {
                //aspetta
            }
            //quando esci, chiama gameEnd e blocca i client
            for (ClientInterface client : servers.get(0).getClients().keySet()) {
                client.setStartGame(false);
            }
            List<Player> winners = new ArrayList<>();
            for (Integer i : game.GameEnd()) {
                winners.add(game.getPlayers().get(i));
            }
            if(winners.size()>1)notifyAllClients("WE HAVE MORE THAN A WINNER...");
            else    notifyAllClients("WE HAVE A WINNER...");
            for (Player winner : winners)
                notifyAllClients("CONGRATULATIONS " + winner.getNickname() + "YOU WON THE GAME!!!");
            //making the clients quit
            for (ClientInterface client : servers.get(0).getClients().keySet()) {
                client.setStartGame(true);
            }

        } catch (RemoteException re) {
            System.err.println("Something went wrong...");
            re.printStackTrace();
        }
    }
    public void playStarterCard(boolean flipped, Player player) throws Board.notAvailableCellException, Board.necessaryResourcesNotAvailableException, RemoteException {
        //piazza la carta starter del client che chiama il metodo
        player.getHand().get(0).setFlipped(flipped);
        player.getPlayerBoard().updateBoard(42, 42, player.getHand().remove(0));

        //mostro a tutti la carta giocata
        notifyAllClients(player.getNickname() + "has played the starter card:" + player.getPlayerBoard().getCardBoard()[42][42].getCard().getPrintableCardString(flipped));


    }
    public Player addPlayer(String nickname, ServerInterface server) throws RemoteException {
        //istanzio la board
        Board board = new Board();
        //istanzio il player e gli assegno la board
        Player newPlayer = new Player(new ArrayList<>(), board, nickname);
        //lo aggiungo alla lista di player del controller
        this.clientPlayers.add(newPlayer);
        //aggiungo una carta starter alla mano del player
        newPlayer.setHand(game.getStarterDeck().poll());
        //gliela mostro
        servers.get(0).notifyClient(nickname, "Questa é la tua starter card:\n"
                + newPlayer.getHand().get(newPlayer.getHand().size() - 1).getPrintableCardString(false)
                + "\n" + newPlayer.getHand().get(newPlayer.getHand().size() - 1).getPrintableCardString(true));
        //e lo aggiungo al game
        game.getPlayers().add(newPlayer);
        playerServerBindingMap.put(newPlayer, server);
        //System.out.println(this.clientPlayers.size());
        return newPlayer;

    }
    public void updatePlayerHands() throws RemoteException {

        game.setFirstTurn(true);
        //dà le carte a tutti i giocatori
        for (Player player : game.getPlayers()) {
            ServerInterface server = playerServerBindingMap.get(player);

            game.drawFromTheDeck(player, 0);
            servers.get(0).notifyClient(player.getNickname(), "This is your card[1]:\n"
                    + player.getHand().get(player.getHand().size() - 1).getPrintableCardString(false)
                    + "\n" + player.getHand().get(player.getHand().size() - 1).getPrintableCardString(true));
            game.drawFromTheDeck(player, 0);
            servers.get(0).notifyClient(player.getNickname(), "This is your card[2]:\n"
                    + player.getHand().get(player.getHand().size() - 1).getPrintableCardString(false)
                    + "\n" + player.getHand().get(player.getHand().size() - 1).getPrintableCardString(true));
            game.drawFromTheDeck(player, 1);
            servers.get(0).notifyClient(player.getNickname(), "This is your card[3]:\n");
                    //+ player.getHand().get(player.getHand().size() - 1).getPrintableCardString(false)
                    //+ "\n" + player.getHand().get(player.getHand().size() - 1).getPrintableCardString(true));
        }

        game.setFirstTurn(false);
    }

    public void updatePublicGoals() throws RemoteException {
        GoalCard[] goalCards = new GoalCard[2];
        goalCards[0] = game.getGoalCardDeck().poll();
        goalCards[1] = game.getGoalCardDeck().poll();
        game.setPublicGoals(goalCards);
        servers.get(0).notifyAllClients(String.format("These are the public goals:\n%s\n%s", goalCards[0].getGoalType(), goalCards[1].getGoalType()));
    }

    public void privateGoal(GoalCard goalCard, Player player) {
        player.setGoalCard(goalCard);
    }



    public void showPrivateGoals(ClientInterface client) throws RemoteException {
        GoalCard[] privateGoals = new GoalCard[2];
        privateGoals[0] = game.getGoalCardDeck().poll();
        privateGoals[1] = game.getGoalCardDeck().poll();
        servers.get(0).notifyClient(client.getNickname(), "Choose between: " + privateGoals[0] + "digit [1]");
        servers.get(0).notifyClient(client.getNickname(), " and " + privateGoals[1] + " digit [2]");
        client.setGoals(privateGoals);
    }
    public void playCard(int card, Player player,int r, int c) throws RemoteException {
        //prova a giocare la carta
        int result=game.PlayCard(player.getHand().get(card),player,r,c);
        //se tutto va a buon fine, notifica tutti i client del corretto piazzamento
        if(result>0){
            servers.get(0).notifyAllClients(player.getNickname()+" has played a card:\n"
                    + player.getPlayerBoard().getCardBoard()[r][c].getCard().getPrintableCardString(player.getPlayerBoard().getCardBoard()[r][c].getCard().getFlipped())
                    + "\non the board in position"+ r + c);
            servers.get(0).notifyClient(player.getNickname(),"You successfully played a card, now you have to draw!");
        }
        else if(result==-1){
            playerServerBindingMap.get(player).notifyClient(player.getNickname(),"You tried to place a card where you can't! Try again");
        }
        else if(result==0){
            playerServerBindingMap.get(player).notifyClient(player.getNickname(),"You don't have enough resources to place this card! Try again");
        } else if (result==-3) {
            playerServerBindingMap.get(player).notifyClient(player.getNickname(),"You tried to play a card but it's not your turn! Wait for the current player to end their turn");
        }
        else if(result==-2){
            playerServerBindingMap.get(player).notifyClient(player.getNickname(),"You tried to play a card but you've already played one!");
        }
    }
    public void drawFromTheDeck(Player player,int deck) throws RemoteException {
        int result=game.drawFromTheDeck(player,deck-1);
        if(result>0){
            servers.get(0).notifyAllClients(player.getNickname()+" has drawn a card from the deck");
            printPlayerHand(player);
            //METODO DI FRA CHE FA VEDERE A TUTTI IL DORSO DEI DECK
            if(!game.TurnEnd()){
                game.TurnStart();
            }
        } else if (result==0) {
            playerServerBindingMap.get(player).notifyClient(player.getNickname(),"You tried to draw a card but you've already drawn one!");
        } else if (result==-1) {
            playerServerBindingMap.get(player).notifyClient(player.getNickname(),"You tried to draw a card but it's not your turn! Please wait for the current player to end their turn");
        } else if (result==-2) {
            playerServerBindingMap.get(player).notifyClient(player.getNickname(),"You tried to draw a card from the resource deck but it's empty!");
        } else if (result==-3) {
            playerServerBindingMap.get(player).notifyClient(player.getNickname(),"You tried to draw a card from the gold deck but it's empty!");
        }
    }
    public void drawPublicCards(Player player, int card) throws RemoteException {
        int result=game.drawPublicCards(player,card);
        if(result>0){
            servers.get(0).notifyAllClients(player.getNickname()+" has drawn a public card");
            printPlayerHand(player);
            printPublicCardsToAll();
            //METODO DI FRA CHE FA VEDERE A TUTTI IL DORSO DEI DECK
            //se non è stato settato a true il gameEnd, chiamo turn start
            if(!game.TurnEnd()){
                game.TurnStart();
            }
        }else if(result==0){
            playerServerBindingMap.get(player).notifyClient(player.getNickname(),"You tried to draw a card but you've already drawn one!");
        } else if (result==-1) {
            playerServerBindingMap.get(player).notifyClient(player.getNickname(),"You tried to draw a card from the public cards, but there are no more cards to draw from!");
        }else if(result==-2){
            playerServerBindingMap.get(player).notifyClient(player.getNickname(),"You tried to draw a card but it's not your turn! Please wait for the current player to end their turn");
        }
    }

    public void printPlayerHand(Player player) {

        try {
            servers.get(0).notifyClient(player.getNickname(), "Ecco le carte che hai in mano:\n");
        } catch (RemoteException e) {
            System.err.println("Something went wrong, retry...");
        }
        int i = 1;
        for (Card card : player.getHand()) {
            try {

                servers.get(0).notifyClient(player.getNickname(), String.format("Carta[%s]\n" + card.getPrintableCardString(false)
                        + "\n" + card.getPrintableCardString(true), i));
            } catch (RemoteException e) {
                System.err.println("Something went wrong, retry...");
            }
            i++;
        }
    }

    public void printBackDeckToClient(Player player) {
        try {
            servers.get(0).notifyClient(player.getNickname(), "In cima ai deck ci sono:\n");
        } catch (RemoteException e) {
            System.err.println("Something went wrong, retry...");
        }
        try {
            if (!game.getGoldDeck().isEmpty()) {
                servers.get(0).notifyClient(player.getNickname(), game.getGoldDeck().peek().getPrintableCardString(false));
            } else {
                servers.get(0).notifyClient(player.getNickname(), "The decks are empty!!\n");
            }
            if (!game.getResourceDeck().isEmpty()) {
                servers.get(0).notifyClient(player.getNickname(), game.getResourceDeck().peek().getPrintableCardString(false));
            } else {
                servers.get(0).notifyClient(player.getNickname(), "The decks are empty!!\n");
            }
        } catch (RemoteException e) {
            System.err.println("Something went wrong, retry...");
        }
    }
    public void printBackDeckToAll (){
            try {
                if(!game.getGoldDeck().isEmpty()) {
                    servers.get(0).notifyAllClients("On top of the decks there are::\n");
                    game.getGoldDeck().peek().getPrintableCardString(true);
                }
                else{
                    servers.get(0).notifyAllClients("The gold deck is empty");
                }
            } catch (RemoteException e) {
                System.err.println("Something went wrong, retry...");
            }
    }

    public void printPublicCardsToAll (){
        try {
            servers.get(0).notifyAllClients("Ecco le carte sul tavolo:\n");
        } catch (RemoteException e) {
            System.err.println("Something went wrong, retry...");
        }
        int i = 1;
        for (Card card : game.getPublicCards()){
            try {
                servers.get(0).notifyAllClients(String.format("\nCarta[%s]\n" + card.getPrintableCardString(false)
                        + "\n" + card.getPrintableCardString(true), i));
            } catch (RemoteException e) {
                System.err.println("Something went wrong, retry...");
            }
            i++;
        }
    }
    public void printPublicCardToClient(Player player){
        try {
            servers.get(0).notifyClient(player.getNickname(), "Ecco le carte sul tavolo:\n");
        } catch (RemoteException e) {
            System.err.println("Something went wrong, retry...");
        }
        int i = 1;
        for (Card card : game.getPublicCards()){
            try {
                servers.get(0).notifyClient(player.getNickname(), String.format("\nCarta[%s]\n" + card.getPrintableCardString(false)
                        + "\n" + card.getPrintableCardString(true), i));
            } catch (RemoteException e) {
                System.err.println("Something went wrong, retry...");
            }
            i++;
        }
    }

    public void setMaxPlayers(int max) {
        maxPlayers = max;
    }

    public int getMaxPlayers() {
        return maxPlayers;
    }
    public void startGame() throws RemoteException {
        for (ServerInterface server: servers){
            server.startGame();
        }
    }
    public Game getGame() {
        return game;
    }

    public List<Player> getClientPlayers() {
        return clientPlayers;
    }

    public void addServers(ServerInterface server) {
        this.servers.add(server);
    }

    public void notifyAllClients(String message) throws RemoteException {
        for (ServerInterface s : servers) {
            s.notifyAllClients(message);
        }
    }


}
