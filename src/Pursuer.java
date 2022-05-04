import java.awt.*;
import java.util.*;
import java.io.*;

public class Pursuer {
    public static int distance;
    private int vel;
    private Player player;

    public Pursuer(Player player) {
        distance = 10000;
        vel = 12;
        this.player = player;
    }

    public void tick() {
        if (player.velY == -18)
            vel = -3;
        else
            vel = (Stats.speederDistance)/10000 + 14;
        distance = Game.clamp(distance - (vel + player.velY), 0, 11000);
    }

    public void render(Graphics g) {
//        g.setColor(new Color(17, 124, 237, Math.max(255 - Math.min(distance, 1000) * 255/1000, 1)));
        if (distance<1000) {
            g.setColor(new Color(17, 124, 237, 150));
            g.fillOval(player.getRelX() - 50, player.getRelY()-42, 132, 132);
        }
    }

    public void setDistance(int distance) {
        Pursuer.distance = distance;
    }
}
