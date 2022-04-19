import java.awt.*;
import java.util.*;
import java.io.*;

public class HUD{

    private int center = Game.WIDTH/2;
    private int width = 70;

    public void tick() {
        Stats.CHARGE = Game.clamp((int) Stats.CHARGE, 0, 400);
    }

    public void render(Graphics g) {
        g.setColor(Color.white);
        g.drawRect(center-405-width, 15, 405 ,10);
        g.drawRect(center+width, 15, 405 ,10);
        g.setColor(Color.orange);
        g.fillRect(center-width-3- (int) Stats.CHARGE, 18, (int) Stats.CHARGE, 4);
        g.fillRect(center+width+3, 18, (int) Stats.CHARGE, 4);

    }
}
