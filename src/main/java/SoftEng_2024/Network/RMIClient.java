package SoftEng_2024.Network;

import javax.naming.*;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Scanner;
import java.util.*;

public class RMIClient extends UnicastRemoteObject implements ClientInterface{
    final ServerInterface server;
    public RMIClient(ServerInterface server) throws RemoteException{
        this.server=server;
    }
    public void showServerMessage(String message) throws RemoteException{
        System.out.println(message);
    }
    public void showServerError(String error) throws RemoteException{
        System.err.println(error);
    }
    public int choosePlayersMax() throws RemoteException{
        System.out.print("Enter the number of players who will play: ");
        Scanner scanner = new Scanner(System.in);
        int number= scanner.nextInt();
        while(number <2 || number >4){
            System.err.println("The number of players has to be between 2 and 4: retry...");
            number=scanner.nextInt();
        }
        return number;
    }

    public String setNickname() throws RemoteException{
        System.out.println("Insert your nickname: ");
        Scanner scanner = new Scanner(System.in);
        return scanner.nextLine();
    }

    //metodo che serve per segnalare l'attività del client
    public boolean heartBeat() throws RemoteException {
        String answer;
        System.out.print("Are you still online? (y/n)");
        Scanner scanner = new Scanner(System.in);
        answer = scanner.nextLine();
        while (!answer.equals("y") && !answer.equals("n")) {
            System.err.println("Didn't understant... type 'y' or 'n'");
            answer = scanner.nextLine();
        }

        if (scanner.nextLine().equals("y")) {
            return true;
        } else if (scanner.nextLine().equals("n")) {
            return false;
        }else {
            return false;
        }
    }

    private void run() throws RemoteException{
        this.server.connect(this);
    }

    public static void main(String[] args) throws RemoteException, NotBoundException {
        System.out.println("Choose between RMI-> [1] and Sockets-> [2]");
        Scanner scanner= new Scanner(System.in);
        int command= scanner.nextInt();
        while(command != 1 && command !=2){
            System.err.println("I didn't understand... digit [1] or [2]");
            command= scanner.nextInt();
        }
        if(command==1){
            //creating the client as an RMI client
            Registry registry = LocateRegistry.getRegistry("ServerRMI", 6969);
            ServerInterface server = (ServerInterface) registry.lookup("ServerRMI");

            new RMIClient(server).run();
        }
        else{
            //creating the client as a Socket client
        }


    }
}
