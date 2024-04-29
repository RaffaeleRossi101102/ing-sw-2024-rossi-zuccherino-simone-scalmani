package SoftEng_2024.Network;

import SoftEng_2024.Model.GoalCard;
import SoftEng_2024.Model.Player;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ClientInterface extends Remote {
    void showServerMessage(String message) throws RemoteException;
    int choosePlayersMax() throws RemoteException;
    void showServerError(String error) throws RemoteException;
    String setNickname() throws RemoteException;
    boolean heartBeat() throws RemoteException;
    void setStartGame(boolean start) throws RemoteException;
    boolean getStartGame() throws RemoteException;
    boolean playStarterCard() throws RemoteException;
    String getNickname() throws RemoteException;
    void setGoals(GoalCard[] goals) throws RemoteException;
    String chooseColor() throws RemoteException;
    void playCard() throws RemoteException;
    void drawCard() throws RemoteException;
    void drawPublicCards() throws RemoteException;
    void drawFromTheDeck() throws RemoteException;

}
