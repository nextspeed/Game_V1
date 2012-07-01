package gm;

import java.util.HashMap;
import java.util.Map;

public class GameMessage {
    String command;
    Map<String, String> params = new HashMap<String, String>();
    
    public static GameMessage parse(String line){
        String main_items[] = line.split(" ");
        GameMessage message = new GameMessage(main_items[0]);
        String pairs[] = main_items[1].split(",");
        for(String pair : pairs){
            String items[] = pair.split("=");
            message.addParam(items[0].trim(), items[1].trim());
        }
        return message;
    }
    
    public GameMessage(String command){
        this.command = command;
    }
    
    public void addParam(String key, String value){
        params.put(key, value);
    }
    
    public String s(String key){
        return params.get(key);
    }
    
    public int i(String key){
        return Integer.parseInt(s(key));
    }
    
    @Override
    public String toString(){
        StringBuilder builder = new StringBuilder();
        builder.append(command);
        builder.append(" ");
        for(String key : params.keySet()){
            builder.append(key + "=" + params.get(key));
            builder.append(",");
        }
        return builder.toString();
    }
    
    public static void main(String[] args) {
        GameMessage m = new GameMessage("XX");
        m.addParam("a", "x");
        m.addParam("b", "y");
        GameMessage n = GameMessage.parse(m.toString());
        System.out.println(n.command + ".");
        System.out.println(n.s("a") + ".");
    }
}
