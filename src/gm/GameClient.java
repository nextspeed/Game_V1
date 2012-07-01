package gm;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

public class GameClient extends Thread{
    PrintWriter writer;
    BufferedReader reader;
    Socket socket;
    
    public GameClient(String addr, int port) throws IOException{
        socket = new Socket(InetAddress.getByName(addr), port);
        writer = new PrintWriter(socket.getOutputStream());
    }
    
    public void incoming(String line){
        
    }
    
    public void outgoing(String line){
        writer.println(line);
        writer.flush();
    }
    
    public void run(){
        try{
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String line = null;
            while((line = reader.readLine()) != null){
                incoming(line);
            }
        }catch(Exception e){
            e.printStackTrace();     
        }
    }
    
    public static void main(String[] args) throws Exception{
        new GameClient("172.30.232.126", 5555);
    }
}
