/*

Description:

Responsible for "chasing" the speeder as reflected through a numerical value of the relative distance between the two,
which is rendered graphically in the HUD class. Updates the relative distance to the speeder every game tick depending
on the speeder's velocity and distance travelled. Renders the pursuer's "net" to catch the speeder only when the
relative distance is reduced to a certain amount to make the user panik.

*/


import javax.swing.*;
import java.awt.*;
import java.util.*;

public class Pursuer {

    //Relative distance to speeder
    public static int distance;

    //Pursuer velocity
    private int vel;

    //Game components to be referenced to
    private Player player;
    private JPanel game;

    //Animation for pursuer "net"
    private Image[] frames = new Image[250];

    //Constructor
    public Pursuer(Player player, Game game) {

        //Initialize variables
        distance = 10000;
        vel = 12;
        this.player = player;
        this.game = game;

        //Compile frames for pursuer "net" animation
        for (int i = 0; i < 250; i++) {
            frames[i] = Toolkit.getDefaultToolkit().getImage("appdata/pics/pursuer/" + String.format("%04d", i + 1) + ".png");
        }
    }

    //Description: updates the pursuer’s velocity and its relative distance with the speeder
    //Parameters: none
    //Return: void
    public void tick() {

        //Updates pursuer velocity depending on velocity of speeder
        if (player.velY <= -11)
            vel = -player.velY / 5;
        else
            vel = (Stats.speederDistance) / 17000 + 14;

        //Updates pursuer distance relative to speeder
        distance = Game.clamp(distance - (vel + player.velY), 0, 11000);
    }

    //Description: renders the pursuer, if necessary
    //Parameters: the Graphics object to draw the pursuer
    //Return: void
    public void render(Graphics g) {

        //Render animation of pursuer "net" once the pursuer is in close proximity to the speeder
        if (distance < 2000) {
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
