import java.awt.*;
import java.util.*;
import java.io.*;

public class HUD{

    private int center = Game.WIDTH/2;
    private int width = 70;

    public void tick() {

    }

    public void render(Graphics g) {
        g.setColor(Color.white);
        g.drawRect(center-405-width, 15, 405 ,10);
        g.drawRect(center+width, 15, 405 ,10);
        g.setColor(Color.orange);
        g.fillRect(center-width-3- Stats.CHARGE/2, 18, Stats.CHARGE/2, 4);
        g.fillRect(center+width+3, 18, Stats.CHARGE/2, 4);
        g.setColor(Color.green);
        g.drawString(String.valueOf(Stats.speederDistance), Game.WIDTH/2-5, 45);
        g.setColor(Color.red);
        g.drawString(String.valueOf(Pursuer.distance), Game.WIDTH/2-5, 70);
    }

}
