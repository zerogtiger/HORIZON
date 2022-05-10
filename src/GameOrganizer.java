import java.util.*;
import java.io.*;

public class GameOrganizer {
    private Random r;

    private Game game;
    private Player player;
    private Pursuer pursuer;
    private Map map;
    private Handler handler, ghandler;
    private long counter = -1;
    private int seed;

    public GameOrganizer(Game game) {
        this.game = game;
        this.seed = game.getSeed();
        r = new Random(seed);
        this.handler = game.getHandler();
        this.ghandler = game.getGhandler();
        this.player = Game.getPlayer();
        this.pursuer = game.getPursuer();
        this.map = game.getMap();
    }

    public void tick() {
        if (counter < Stats.speederDistance/16000) {
            player.setX(-16);
            player.setY(Game.HEIGHT);
            pursuer.setDistance(pursuer.getDistance()-1000);
            game.setMap(new Map(r.nextInt(6), r.nextInt(10000), handler, ghandler, player));
            counter++;
            System.out.println("new map");
        }
    }

    public void reset() {
        r.setSeed(seed);
        counter = -1;
    }
}
