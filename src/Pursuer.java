import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.io.*;

public class Pursuer {
    public static int distance;
    private int vel;
    private Player player;
    private Image[] frames = new Image[250];
    private JPanel game;

    public Pursuer(Player player, Game game) {
        distance = 10000;
        vel = 12;
        this.player = player;
        this.game = game;
        for (int i = 0; i < 250; i++) {
            frames[i] = Toolkit.getDefaultToolkit().getImage("appdata/pics/pursuer/" + String.format("%04d", i + 1) + ".png");
        }
    }

    public void tick() {
        if (player.velY <= -11)
            vel = -player.velY / 5;
        else
            vel = (Stats.speederDistance) / 10000 + 14;
        distance = Game.clamp(distance - (vel + player.velY), 0, 11000);
    }

    public void render(Graphics g) {
//        g.setColor(new Color(17, 124, 237, Math.max(255 - Math.min(distance, 1000) * 255/1000, 1)));
        if (distance < 2000) {
            System.out.println("less");
            int index = 249-(distance/8);
            g.drawImage(frames[index], player.getRelX() - 50, player.getRelY() - 42, 132, 132, game);
        }
    }

    public void setDistance(int distance) {
        Pursuer.distance = distance;
    }

    public int getDistance() {
        return distance;
    }
}
