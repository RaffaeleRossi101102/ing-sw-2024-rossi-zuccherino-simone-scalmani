package SoftEng_2024.Network;

import SoftEng_2024.Controller.GameInit;

import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;
import java.util.*;
import java.util.Scanner;


public class RMIServer implements ServerInterface{

    List<ClientInterface> clients = new ArrayList<>();
    boolean answer;
    GameInit controller;
    int maxPlayers;
    public RMIServer(GameInit controller){this.controller=controller;}
    @Override
    public void connect(ClientInterface client) throws RemoteException {
        //sinchronizing on this so that no one can call the server's methods while a client is connecting
        synchronized (this) {
            //First client connected can decide how many players will have to join
            //checking inside Controller's list of players
            if (controller.getClientPlayers().isEmpty()) {
                System.err.println("Asking the first client how many players will join the game...");
                maxPlayers = client.choosePlayersMax();
            }
            if (controller.getClientPlayers().size() < maxPlayers) {
                //creo i Player e faccio settare il nickname
                controller.addPlayer();
               // controller.getClientPlayers().getLast().setNickname(client.setNickname());
                client.showServerMessage("Got it :)");
                clients.add(client);
            } else {
                client.showServerError("Unable to connect: reached maxPlayers in game");
            }
            if (clients.size() == maxPlayers) {
                startGame();
            }
        }
    }


    public void startGame() throws RemoteException {
        //client.ping
        //inizializza il game e la partita





    }
    public void notifyAllClients(String s)throws RemoteException{
        for (ClientInterface client: clients){
            client.showServerMessage(s);
        }
    }
    private void setAnswer(boolean answer) throws RemoteException{
        this.answer = answer;
    }

    public static void main(String[] args) throws RemoteException, AlreadyBoundException {
        System.out.println("Insert name association with the server: ");
        Scanner nameServer = new Scanner(System.in);
        String registryName= String.valueOf(nameServer);
        ServerInterface engine = new RMIServer(new GameInit());
        ServerInterface stub= (ServerInterface) UnicastRemoteObject.exportObject(engine,0);
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
}
