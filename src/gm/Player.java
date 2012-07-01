package gm;

/**
 *
 * @author lbosssk
 */
public class Player {
    int locx;
    int locy;
    
    public Player(){
        locx = (int)(Math.random() * (ServerEngine.MAX_LOCX + 1));
        locy = (int)(Math.random() * (ServerEngine.MAX_LOCY + 1));
    }
    
    public void move(int dirx, int diry){
        locx += dirx;
        locy += diry;
    }
}
