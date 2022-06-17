import java.util.*;
import java.io.*;

public class GameOrganizer {
    private Random r;

    private Game game;
    private Player player;
    private Pursuer pursuer;
    private Map map;
    private Handler handler, ghandler, ahandler;
    private long counter = -1;
    private int seed;

    public GameOrganizer(Game game) {
        this.game = game;
        this.seed = game.getSeed();
        r = new Random(seed%10000);
        this.handler = game.getHandler();
        this.ghandler = game.getGhandler();
        this.ahandler = game.getAhandler();
        this.player = Game.getPlayer();
        this.pursuer = game.getPursuer();
        this.map = game.getMap();
    }

    public void tick() {
        if (counter < Stats.speederDistance / 16000) {
            player.setX(-16);
            player.setY(Game.HEIGHT);
            pursuer.setDistance(pursuer.getDistance() + 1000);
            game.setMap(new Map(r.nextInt(1,3), r.nextInt(10000), handler, ghandler, ahandler, player));
            counter++;
            System.out.println("new map");
        }
    }

    public void reset() {
        r.setSeed(seed%10000);
        counter = -1;
    }

    public void setSeed(int seed) {
        this.seed = seed;
    }
}
