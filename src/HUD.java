/*

Description:

Heads-up display of the game to be shown during the game. Displays information such as the charge levels of the speeder,
pursuer distance representation, and speeder distance for the user to reference.

*/

import java.awt.*;
import java.util.*;

public class HUD {

    //Helper value to locate the center of the screen
    private int center = Game.WIDTH / 2;
    private int width = 70;

    //Iterator to help display animation
    private int iterator;

    public void tick() {
        iterator = (iterator + 1) % 120;
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
        g.fillRect(100 + 400 - Stats.CHARGE / 2, 75, Stats.CHARGE / 2, 8);
        g.fillRect(center + 100, 75, Stats.CHARGE / 2, 8);

        //Border for speeder charge
        g.setColor(Color.white);
        g2d.drawLine(100, 75, 500, 75);
        g2d.drawLine(500, 75, 500, 75 + 6);
        g2d.drawLine(center + 100, 75, center + 500, 75);
        g2d.drawLine(center + 100, 75, center + 100, 75 + 6);

        //Pursuer distance graphical representation:
        g2d.setStroke(new BasicStroke(1));
        g2d.drawLine(0, 40, Game.WIDTH, 40);

        g.setColor((Pursuer.distance < 2000 ? new Color(208, 0, 0, 255) : Color.white));

        //Pursuer position
        g.fillPolygon(new int[]{(int) ((10000 - Pursuer.distance) / 10000.0 * 600 - 15),
                (int) ((10000 - Pursuer.distance) / 10000.0 * 600),
                (int) ((10000 - Pursuer.distance) / 10000.0 * 600) + 15,
                (int) ((10000 - Pursuer.distance) / 10000.0 * 600)}, new int[]{40, 40 + 15, 40, 40 - 15}, 4);

        g.fillPolygon(new int[]{(int) (1200 - (10000 - Pursuer.distance) / 10000.0 * 600 - 15),
                (int) (1200 - (10000 - Pursuer.distance) / 10000.0 * 600),
                (int) (1200 - (10000 - Pursuer.distance) / 10000.0 * 600) + 15,
                (int) (1200 - (10000 - Pursuer.distance) / 10000.0 * 600)}, new int[]{40, 40 + 15, 40, 40 - 15}, 4);

        //Pursuer trail
        g2d.setStroke(new BasicStroke(8));
        g2d.drawLine(-20, 40, (int) ((10000 - Pursuer.distance) / 10000.0 * 600), 40);
        g2d.drawLine(1220, 40, (int) (1200 - (10000 - Pursuer.distance) / 10000.0 * 600), 40);

        //Pursuer Warning
        if (Pursuer.distance < 2000) {

            float opacity = 1;
            if (iterator % 60 < 15 || iterator % 60 >= 45) {
                opacity = (float) (iterator % 60 < 15 ? (iterator % 60) / 14.0 : (59 - iterator % 60) / 14.0);
            }
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, opacity));

            //Message background
            g.setColor(new Color(200, 0, 0));
            g2d.fillRect(28, 90, (iterator < 60? 278: 197), 30);
            g2d.fillRect(Game.WIDTH-(iterator < 60? 279: 197)-28, 90, (iterator < 60? 279: 197), 30);

            //Warning message
            g2d.setFont(new Font("Courier New", Font.BOLD, 34));
            g2d.setColor(Color.white);
            g2d.drawString((iterator < 60 ? "Pursuer Close" : "Boost Now"), 32, 115);
            g2d.drawString(String.format("%13s", iterator < 60 ? "Pursuer Close" : "Boost Now"), 1200 - 32 - 270, 115);

            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1));
        }

        //Speeder distance
        g.setColor(Color.white);
        g.setFont(new Font("Courier New", Font.BOLD, 34));

        //Centering the distance
        int digits = String.valueOf(Stats.speederDistance).length();
        g.drawString(String.valueOf(Stats.speederDistance), 600 - 10 * digits, 85);

    }

}
