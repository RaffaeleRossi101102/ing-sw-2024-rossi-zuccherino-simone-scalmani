package SoftEng_2024.Network;

import SoftEng_2024.Controller.GameInit;
import SoftEng_2024.Model.Board;
import SoftEng_2024.Model.Board.notAvailableCellException;
import SoftEng_2024.Model.GoalCard;
import SoftEng_2024.Model.Player;

import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;



public class RMIServer implements ServerInterface{

    HashMap<ClientInterface, Player> clients = new HashMap<>();
    List<ClientInterface> disconnectedClients= new ArrayList<>();
    boolean answer;
    final GameInit controller;
  //  int maxPlayers;
    public RMIServer(GameInit controller){this.controller=controller;}
    @Override
    public void connect(ClientInterface client) throws RemoteException {
        //sinchronizing on this so that no one can call the server's methods while a client is connecting
        synchronized (clients) {
            try {
                //First client connected can decide how many players will have to join
                //checking inside Controller's list of players
                if (controller.getClientPlayers().isEmpty()) {
                    System.err.println("Asking the first client how many players will join the game...");
                    int maxPlayers = client.choosePlayersMax();
                    setMaxPlayers(maxPlayers);
                    //the game is initialized with 0 players a
                    controller.gameInit();
                }
                //il client non prende il posto di nessun client disconnesso
                if (controller.getClientPlayers().size() < controller.getMaxPlayers()) {
                    //creo i Player e faccio settare il nickname
                    String nick = client.setNickname();
                    for (Player player : controller.getClientPlayers()) {
                        while (nick.equals(player.getNickname())) {
                            client.showServerError("Nickname already taken, insert a new nickname... ");
                            nick = client.setNickname();
                        }
                    }
                    client.showServerMessage("Got it :)");
                    //notifico tutti i client che un nuovo player si è unito
                    controller.notifyAllClients(client.getNickname() + " has joined the game");
                    clients.put(client, controller.addPlayer(nick, this));
                    //controller.getClientPlayers().get(controller.getClientPlayers().size()-1).setNickname(client.setNickname());

                    //lo aggiungo alla lista di client
                    //se non è l'ultimo player gli dico di aspettare
                }//sotto c'è il caso in cui il client che vuole collegarsi deve prendere il posto di uno offline
                //quindi la lista di player è piena e il counter di client offline >0
                else if (controller.getOfflinePlayers()>0) {
                    //mi salvo il player associato al primo client disconnesso
                    Player newPlayer = clients.remove(disconnectedClients.get(0));
                    clients.put(client,newPlayer);
                    controller.setOfflinePlayers(controller.getOfflinePlayers()-1);
                    //client.setNullNickname();
                    String nick = client.setNickname();
                    for(Player player: controller.getClientPlayers()) {
                        while (nick.equals(player.getNickname())) {
                            client.showServerError("Nickname already taken, insert a new nickname... ");
                            nick = client.setNickname();
                        }
                    }
                    client.showServerMessage("Got it :)");
                    newPlayer.setOnline(true);
                    notifyClient(client.getNickname(), "You connected to an already started match with this card in you hand:\n"
                            + clients.get(client).getHand().get(0).getPrintableCardString(false)
                            + "\n" + clients.get(client).getHand().get(0).getPrintableCardString(true)
                            + "\nGet ready to play you starter card");
                    controller.notifyAllClients(client.getNickname() + " has joined the game");
                }
                //nessuno è offline e il client non può collegarsi
                else {
                    client.showServerError("Unable to connect: reached maxPlayers in game");
                    return;
                }
                if (controller.getClientPlayers().size() != controller.getMaxPlayers() | controller.getOfflinePlayers()!=0)
                    notifyClient(client.getNickname(), "Waiting for all the players to join the game...");
                else {

                    controller.setWait(false);
                }
//            if (controller.getClientPlayers().size() == controller.getMaxPlayers()) {
//                controller.notifyAll();
//            }
//                controller.notifyAllClients("We are ready to start the game!");
//                controller.startGame();
//            }
            }catch(RemoteException re){
                System.err.println("Something went wrong...");
                removeFromServer(client);
            }
        }
    }
    public void startGame() throws RemoteException {
        //client.ping
        int cont =0;
        for(ClientInterface client : clients.keySet()){
            client.setStartGame(true);
        }
    }
    public boolean notifyAllClients(String s)throws RemoteException{
        boolean result=true;
        for (ClientInterface client : clients.keySet()) {
            try {
                if (clients.get(client).getIsOnline()) {
                    client.showServerMessage(s);
                }
            }catch(RemoteException re){
                removeFromServer(client);
                result=false;
            }
        }
    return result;
    }
    public boolean notifyClient (String nickname, String msg) throws RemoteException {
        boolean result=true;
        for (ClientInterface client : clients.keySet()) {
            try{
                if (clients.get(client).getNickname().equals(nickname) && clients.get(client).getIsOnline()) {
                    client.showServerMessage(msg);
                    break;
                }
            }catch(RemoteException re){
                removeFromServer(client);
            }
        }
        return result;
    }
    public void printPlayerHand (ClientInterface client) throws RemoteException{
        System.out.println("Executing printPlayerHand due to a client requests");
        controller.printPlayerHand(clients.get(client));
    }
    public void printPublicCardToClient(ClientInterface client) throws RemoteException{
        System.out.println("Executing printPublicCardToClient due to a client requests");
        controller.printPublicCardToClient(clients.get(client));
    }
    public void printBackDeckToClient(ClientInterface client) throws RemoteException{
        System.out.println("Executing printBackDeckToClient due to a client requests");
        controller.printPublicCardToClient(clients.get(client));
    }
    public void setAnswer(boolean answer) throws RemoteException{
        this.answer = answer;
    }
//    public void addToController() throws RemoteException{
//        controller.addServers(this);
//    }

    public void run() throws RemoteException, AlreadyBoundException {
        //System.out.println("Insert name association with the server: ");
        //Scanner nameServer = new Scanner(System.in);
        String registryName= "localhost";//String.valueOf(nameServer);
        //ServerInterface engine = new RMIServer(new GameInit());
        //this.addToController();
//        System.out.println("added to controller");
        ServerInterface stub= (ServerInterface) UnicastRemoteObject.exportObject(this,0);
        Registry registry = null;
        try {
            registry = LocateRegistry.createRegistry(6969);
        } catch (RemoteException e) {
            throw new RuntimeException("Something went wrong, retry... ");
        }
        try {
            registry.bind(registryName, stub);
            System.out.println("ServerBound");
        } catch (RemoteException remoteException ) {
            throw new RuntimeException("Something went wrong, retry... ");
        }
    }
    public void setMaxPlayers(int i) throws RemoteException{
        controller.setMaxPlayers(i);
    }
    @Override
    public void playStarterCard() throws notAvailableCellException, Board.necessaryResourcesNotAvailableException, RemoteException {
        notifyAllClients("Waiting other players to place their starter card...");
        for (ClientInterface cl : clients.keySet()) {
            try {
                if(clients.get(cl).getIsOnline()){
                    //mostro al client la sua mano
                    controller.printPlayerHand(clients.get(cl));
                    //per ogni client connesso, chiedo l'input e gioco la carta.
                    controller.playStarterCard(cl.playStarterCard(), clients.get(cl));
                }
            }catch(RemoteException re){
                removeFromServer(cl);

            }
        }

    }
    public void choosePrivateGoals(ClientInterface client, GoalCard choice) throws RemoteException{
        clients.get(client).setGoalCard(choice);
        System.out.println("Client: "+ clients.get(client).getNickname()+" has chosen his/her private goal");
    }
    public void showControllerMessage(String s) throws RemoteException{
        System.out.println(s);
    }

    public HashMap<ClientInterface, Player> getClients() throws RemoteException {
        return clients;
    }

   public ServerInterface getServer(){
        return this;
   }
   public void setColor() throws RemoteException{
        //eseguo  il metodo solo sui client online
        notifyAllClients("Waiting for other players to choose their color");
        String color;
        for(ClientInterface client:clients.keySet()){
            try {
                if (clients.get(client).getIsOnline()) {
                    client.showServerMessage("Choose your color between: RED - BLUE - GREEN - YELLOW ");
                    color = client.chooseColor();
                    clients.get(client).setColor(color);
                    client.showServerMessage("Got it :)");
                }
            }catch(RemoteException re){
                removeFromServer(client);
            }
        }
   }
   public void playCard(int card, ClientInterface client,int r, int c,boolean flipped) throws RemoteException {
        controller.playCard(card,clients.get(client),r,c,flipped);
   }
   public void drawFromTheDeck(ClientInterface client,int deck)throws RemoteException{
        controller.drawFromTheDeck(clients.get(client),deck);
   }
   public void drawPublicCards(ClientInterface client, int card) throws RemoteException{
        controller.drawPublicCards(clients.get(client),card);
   }
   public void removeFromServer(ClientInterface client) throws RemoteException{
        System.err.println(clients.get(client).getNickname()+" has crashed");
        disconnectedClients.add(client);
        clients.get(client).setOnline(false);
        if (!clients.get(client).getDisconnectionResilience()){
            controller.setOfflinePlayers(controller.getOfflinePlayers() + 1);
        }
   }

   public void disconnect(){

   }

}
