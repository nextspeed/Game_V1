package gm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ServerEngine implements Messagable {

    public static final String ID_PREFIX = "ID";
    public static final int MAX_LOCX = 99;
    public static final int MAX_LOCY = 99;
    static Pattern pattern = Pattern.compile("HELO(.*?)");
    int currentId = 0;
    private static ServerEngine engine;

    public static ServerEngine getInstance() {
        if (engine == null) {
            engine = new ServerEngine();
        }
        return engine;
    }
    HashMap<String, ClientHandler> handlers = new HashMap<String, ClientHandler>();
    HashMap<String, Player> players = new HashMap<String, Player>();

    public void register(ClientHandler handler) {

        String givenId = ID_PREFIX + (++currentId);
        Player player = new Player();
        players.put(givenId, player);

        handler.setMessagable(this, givenId);
        GameMessage message = new GameMessage("HELO");
        message.addParam("id", givenId);
        message.addParam("locx", String.valueOf(player.locx));
        message.addParam("locy", String.valueOf(player.locy));
        handler.outgoing(message.toString());
        handlers.put(givenId, handler);
    }

    public void unregister(String sourceId) {
        handlers.remove(sourceId);
    }

    @Override
    public void incoming(String line, String sourceId) {
        System.out.println(line);
        GameMessage m = GameMessage.parse(line);
        Player player = players.get(sourceId);
        if ("MV".equals(m.command)) {
            checkMoveStep(m, player);
            player.move(m.i("dirx"), m.i("diry"));
            checkLocation(player);
            sendPlayerPos(sourceId, player);
        }

        //System.out.println("==> "+line);

        //Matcher matcher = pattern.matcher(line);
        //System.out.println("Match : " + matcher.matches());       
        sendOtherPlayer(line);


    }

    public void sendPlayerPos(String sourceId, Player player) {
        GameMessage m = new GameMessage("POS");
        m.addParam("locx", String.valueOf(player.locx));
        m.addParam("locy", String.valueOf(player.locy));
        handlers.get(sourceId).outgoing(m.toString());
        System.out.println("sending " + m.toString());
    }

    public void sendOtherPlayer(String line) {
        Pattern pattern = Pattern.compile("OPR(.*?)");
        Matcher matcher = pattern.matcher(line);

        Set<String> sourceId = players.keySet();
        ArrayList<String> keys = new ArrayList<String>(sourceId);
        System.out.println("key set of players : " + keys);
        if (matcher.matches()) {
            for (String key : keys) {
                Player player = players.get(key);
                GameMessage m = new GameMessage("PR");
                m.addParam("ID", key);
                m.addParam("locx", String.valueOf(player.locx));
                m.addParam("locy", String.valueOf(player.locy));
                handlers.get(key).outgoing(m.toString());
            }
        }

        System.out.println("OPR sended!!");
    }

    public void checkLocation(Player player) {

        if (player.locx > MAX_LOCX) {
            player.locx = MAX_LOCX;
        }
        if (player.locy > MAX_LOCY) {
            player.locy = MAX_LOCY;
        }
        if (player.locx < 0) {
            player.locx = 0;
        }
        if (player.locy < 0) {
            player.locy = 0;
        }
    }

    public void checkMoveStep(GameMessage m, Player player) {

        if (m.i("dirx") > 1) {
            m.addParam("dirx", String.valueOf(1));
        }
        if (m.i("dirx") < -1) {
            m.addParam("dirx", String.valueOf(-1));
        }
        if (m.i("diry") > 1) {
            m.addParam("diry", String.valueOf(1));
        }
        if (m.i("diry") < -1) {
            m.addParam("diry", String.valueOf(-1));
        }

    }
}
