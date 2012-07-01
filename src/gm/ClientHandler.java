package gm;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ClientHandler extends Thread{
    Socket socket;
    BufferedReader reader;
    PrintWriter writer;
    Messagable messagable;
    String sourceId;
    boolean known = false;
    
    public ClientHandler(Socket socket) throws IOException{
        this.socket = socket;
        reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        writer = new PrintWriter(socket.getOutputStream());
    }
    
    public void setMessagable(Messagable messagable, String sourceId){
        this.messagable = messagable;
        this.sourceId = sourceId;
    }
    
    static Pattern pattern = Pattern.compile("HELO name=(.*?)");
    public void incoming(String line) throws IOException{
        if(!known){
            Matcher matcher = pattern.matcher(line);
            if(matcher.matches()){
                ServerEngine.getInstance().register(this);
                known = true;
            }else{
                socket.close();
                return;
            }
        }
        // Call incoming() in ServerEngine
        if(messagable != null){
            messagable.incoming(line, sourceId);
        }
    }
    
    public void outgoing(String line){
        writer.println(line);
        writer.flush();
    }
    
    @Override
    public void run(){
        System.out.println("Handling connection from " + socket.getInetAddress());
        String line = null;
        try{
            while((line = reader.readLine()) != null){
                incoming(line);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        if(known){
            ServerEngine.getInstance().unregister(sourceId);
        }
        System.out.println(socket.getInetAddress() + " has been disconnected.");
    }
}
