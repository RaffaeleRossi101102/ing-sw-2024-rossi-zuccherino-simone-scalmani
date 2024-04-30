package SoftEng_2024.Network;


import SoftEng_2024.Model.Board;
import SoftEng_2024.Model.GoalCard;
import SoftEng_2024.Model.Player;

import java.rmi.AlreadyBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.HashMap;

public interface ServerInterface extends Remote {
    HashMap<ClientInterface, Player> getClients() throws RemoteException;
    void showControllerMessage(String s) throws RemoteException;
    public void connect(ClientInterface client) throws RemoteException;
    public void startGame() throws RemoteException;
    void notifyAllClients(String s)throws RemoteException;
    //void addToController ()throws RemoteException;
    void setAnswer(boolean answer) throws RemoteException;
    void setMaxPlayers(int i) throws RemoteException;
    void playStarterCard() throws RemoteException, Board.notAvailableCellException, Board.necessaryResourcesNotAvailableException;
    void notifyClient (String nick, String msg) throws RemoteException;
    void run() throws RemoteException, AlreadyBoundException;
    public void choosePrivateGoals(ClientInterface client, GoalCard choice) throws RemoteException;
    void setColor() throws RemoteException;
    void playCard(int card, ClientInterface client,int r, int c,boolean flipped) throws RemoteException;
    void drawFromTheDeck(ClientInterface client,int deck)throws RemoteException;
    void drawPublicCards(ClientInterface client, int card) throws RemoteException;
    void printPlayerHand (ClientInterface client) throws RemoteException;
    void printPublicCardToClient(ClientInterface client) throws RemoteException;
    void printBackDeckToClient(ClientInterface client) throws RemoteException;
    void removeFromServer(ClientInterface client) throws RemoteException;
}
