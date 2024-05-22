package SoftEng_2024.Network.ToModel;


import java.io.*;
import java.net.*;

public class SocketServer{
    private final int port;
    private ServerSocket serverSocket;
    private final NetworkManager manager;
    public SocketServer(int port, NetworkManager manager) {
        this.port = port;
        this.manager = manager;
    }
    public void startServer() throws IOException {
        try{
            serverSocket = new ServerSocket(port);
            System.out.println("SERVER STARTED AT PORT:" + port);
        } catch(IOException e){
            System.err.println(e.getMessage());
            return;
        }
        while(true){
            try{
                Socket socket = serverSocket.accept();
                Thread t = new SocketClientHandler(socket, manager);
                t.start();
            }catch(IOException e){
                break; //esce quando si chiude il server
            }
        }
    }
}