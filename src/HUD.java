/*

Description:

Heads-up display of the game to be shown during the game. Displays information such as the charge levels of the speeder,
pursuer distance representation, and speeder distance for the user to reference.

*/

import java.awt.*;
import java.util.*;

public class HUD{

    //Helper value to locate the center of the screen
    private int center = Game.WIDTH/2;
    private int width = 70;

    public void tick() {

    }

    //Description: renders the heads-up display for the game
    //Parameters: the Graphics object to draw the HUD visuals
    //Return: void
    public void render(Graphics g) {

        Graphics2D g2d = (Graphics2D) g;
        g2d.setStroke(new BasicStroke(2));

        //Speeder charge display:
        //Speeder charge level
        g.setColor(Color.orange);
        g.fillRect(100+400-Stats.CHARGE/2, 75, Stats.CHARGE/2, 8);
        g.fillRect(center+100, 75, Stats.CHARGE/2, 8);

        //Border for speeder charge
        g.setColor(Color.white);
        g2d.drawLine(100, 75, 500, 75);
        g2d.drawLine(500, 75, 500, 75+6);
        g2d.drawLine(center + 100, 75, center+500, 75);
        g2d.drawLine(center + 100, 75, center+100, 75+6);

        //Pursuer distance graphical representation:
        g2d.setStroke(new BasicStroke(1));
        g2d.drawLine(0, 40, Game.WIDTH, 40);

        //Pursuer position
        g.fillPolygon(new int[] {(int) ((10000-Pursuer.distance)/10000.0*600-15),
                (int) ((10000-Pursuer.distance)/10000.0*600),
                (int) ((10000-Pursuer.distance)/10000.0*600)+15,
                (int) ((10000-Pursuer.distance)/10000.0*600)}, new int[] {40, 40+15, 40, 40-15}, 4);

        g.fillPolygon(new int[] {(int) (1200-(10000-Pursuer.distance)/10000.0*600-15),
                (int) (1200-(10000-Pursuer.distance)/10000.0*600),
                (int) (1200-(10000-Pursuer.distance)/10000.0*600)+15,
                (int) (1200-(10000-Pursuer.distance)/10000.0*600)}, new int[] {40, 40+15, 40, 40-15}, 4);

        //Pursuer trail
        g.setColor(Color.white);
        g2d.setStroke(new BasicStroke(8));
        g2d.drawLine(0, 40, (int) ((10000-Pursuer.distance)/10000.0*600), 40);
        g2d.drawLine(1200, 40, (int) (1200-(10000-Pursuer.distance)/10000.0*600), 40);

        //Speeder distance
        g.setFont(new Font("Courier New", Font.BOLD, 30));
        g.drawString(String.valueOf(Stats.speederDistance), Game.WIDTH/2-25, 75);

    }

}
