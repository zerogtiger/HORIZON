import java.awt.*;
import java.util.*;
import java.io.*;

public class HUD{

    private int center = Game.WIDTH/2;
    private int width = 70;

    public void tick() {

    }

    public void render(Graphics g) {

//        g.drawRect(center-405-width, 15, 405 ,10);
//        g.drawRect(center+width, 15, 405 ,10);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setStroke(new BasicStroke(2));
        //Charge Display
        g.setColor(Color.orange);
        g.fillRect(100+400-Stats.CHARGE/2, 75, Stats.CHARGE/2, 8);
        g.fillRect(center+100, 75, Stats.CHARGE/2, 8);

        //Border
        g.setColor(Color.white);
        g2d.drawLine(100, 75, 500, 75);
        g2d.drawLine(500, 75, 500, 75+6);
        g2d.drawLine(center + 100, 75, center+500, 75);
        g2d.drawLine(center + 100, 75, center+100, 75+6);

        //Pursuer
        g2d.setStroke(new BasicStroke(1));
        g2d.drawLine(0, 40, Game.WIDTH, 40);

        g.fillPolygon(new int[] {(int) ((10000-Pursuer.distance)/10000.0*600-15),
                (int) ((10000-Pursuer.distance)/10000.0*600),
                (int) ((10000-Pursuer.distance)/10000.0*600)+15,
                (int) ((10000-Pursuer.distance)/10000.0*600)}, new int[] {40, 40+15, 40, 40-15}, 4);

        g.fillPolygon(new int[] {(int) (1200-(10000-Pursuer.distance)/10000.0*600-15),
                (int) (1200-(10000-Pursuer.distance)/10000.0*600),
                (int) (1200-(10000-Pursuer.distance)/10000.0*600)+15,
                (int) (1200-(10000-Pursuer.distance)/10000.0*600)}, new int[] {40, 40+15, 40, 40-15}, 4);

        //Distance
        g.setColor(Color.white);
        g2d.setStroke(new BasicStroke(8));
//        g.setColor(new Color(217, 0, 0));
        g2d.drawLine(0, 40, (int) ((10000-Pursuer.distance)/10000.0*600), 40);
        g2d.drawLine(1200, 40, (int) (1200-(10000-Pursuer.distance)/10000.0*600), 40);

        g.setFont(new Font("Courier New", Font.BOLD, 30));
        g.drawString(String.valueOf(Stats.speederDistance), Game.WIDTH/2-25, 75);
//        g.setColor(Color.red);
//        g.drawString(String.valueOf(Pursuer.distance), Game.WIDTH/2-15, 85);

    }

}
