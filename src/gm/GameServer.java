package gm;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class GameServer {
    ServerSocket serverSocket;
    
    public GameServer(String addr, int port) throws Exception {
        serverSocket = new ServerSocket(port, 0, InetAddress.getByName(addr));
    }
    
    public void start() throws IOException {
        while(true){
            Socket socket = serverSocket.accept();
            ClientHandler handler = new ClientHandler(socket);
            handler.start();
        }
    }
    
    public static void main(String[] args) throws Exception {
        new GameServer("172.30.232.126", 5555).start();
    }
}
