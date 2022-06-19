/*

Description:

Responsible for displaying the various weather conditions and times-of-day overlays for the game dependent on the seed.
Includes both static and dynamic displays of weather conditions and times-of-day. Renders to the entire screen to block
the camera and distract the player and in severe conditions, cover their vision for incoming obstacles. Frames for
dynamic overlays are displayed at approximately 30 FPS.

*/

import java.util.*;
import java.awt.*;

public class Environment {

    //Respectively: seed for game; weather condition; time-of-day; iterator to be increased every tick
    private int seed, weather, time, iterator;

    //Game to be referenced
    private Game game;

    //Image overlays for the various weather conditions and times-of-day
    private Image clear, overcast, rain[], sandstorm[], sandstormOverlay, timesOfDay[];

    //Constructor
    public Environment(int seed, Game game) {

        //Initialize variables
        this.game = game;
        this.seed = seed;

        //Derive weather condition and time-of-day data from the recorded seed
        this.weather = seed/100000;
        this.time = (seed%100000)/10000;

        //Initialize iterator
        iterator = 0;

        //Compile image overlays to be displayed

        //Weather conditions:
        clear = Toolkit.getDefaultToolkit().getImage("appdata/weather/clear.png");
        overcast = Toolkit.getDefaultToolkit().getImage("appdata/weather/overcast.png");

        rain = new Image[80];
        for (int i = 0; i < 80; i++) {
            rain[i] = Toolkit.getDefaultToolkit().getImage("appdata/pics/weather/rainFrames/" + String.format("%04d", i + 1) + ".png");
        }

        sandstorm = new Image[40];
        for (int i = 0; i < 40; i++) {
            sandstorm[i] = Toolkit.getDefaultToolkit().getImage("appdata/pics/weather/sandstormFrames/" + String.format("%04d", i + 1) + ".png");
        }

        sandstormOverlay =Toolkit.getDefaultToolkit().getImage("appdata/pics/weather/sandstorm.png");

        //Times-of-day:
        timesOfDay = new Image[4];
        timesOfDay[0] = Toolkit.getDefaultToolkit().getImage("appdata/pics/weather/morning.png");
        timesOfDay[1] = Toolkit.getDefaultToolkit().getImage("appdata/pics/weather/noon.png");
        timesOfDay[2] = Toolkit.getDefaultToolkit().getImage("appdata/pics/weather/evening.png");
        timesOfDay[3] = Toolkit.getDefaultToolkit().getImage("appdata/pics/weather/night.png");
    }

    //Description: resets the seed for the environment
    //Parameters: the seed to be reset to
    //Return: void
    public void reset(int seed) {

        //Reset the seed and re-derive weather condition and time-of-day should the seed of the game change
        this.seed = seed;
        this.weather = seed/100000;
        this.time = (seed%100000)/10000;
    }

    //Description: updates the iterator
    //Parameters: none
    //Return: void
    public void tick() {

        //Update the iterator
        iterator = (iterator + 1) % 420;
    }

    //Description: Renders the specified weather condition and time-of-day overlays
    //Parameters: the Graphics object to draw the overlays
    //Return: void
    public void render(Graphics g) {

        /*Renders the specified weather condition overlay

        0 --> clear
        1 --> overcast
        2 --> rain
        3 --> sandstorm
        */

        if (weather == 0) {
            g.drawImage(clear, 0,0, Game.WIDTH, Game.HEIGHT, game);
        }
        else if (weather == 1) {
            g.drawImage(overcast, 0,0, Game.WIDTH, Game.HEIGHT, game);
        }
        else if (weather == 2) {

            //Repeatedly render the 80 frame rain overlay at approximately 30 FPS
            //Loops 70 times as the rain appears and disappears for the first and last 10 frames to ensure smooth,
            // unfragmented rain drops.
            int temp = iterator%140/2;
            if (temp >= 5) {
                g.drawImage(rain[temp], 0,0, Game.WIDTH, Game.HEIGHT, game);
            }
            else {
                g.drawImage(rain[temp], 0,0, Game.WIDTH, Game.HEIGHT, game);
                g.drawImage(rain[temp+75], 0,0, Game.WIDTH, Game.HEIGHT, game);
            }
            g.drawImage(overcast, 0,0, Game.WIDTH, Game.HEIGHT, game);
        }
        else if (weather == 3) {

            //Repeatedly render the 40 frame sandstorm overlay at approximately 30 FPS
            //Loops 30 times as the sand appears and disappears for the first and last 5 frames to ensure smooth,
            // unfragmented sand movement.
            int temp = iterator%60/2;
            if (temp > 5) {
                g.drawImage(sandstorm[temp], 0,0, Game.WIDTH, Game.HEIGHT, game);
            }
            else {
                g.drawImage(sandstorm[temp], 0,0, Game.WIDTH, Game.HEIGHT, game);
                g.drawImage(sandstorm[temp+34], 0,0, Game.WIDTH, Game.HEIGHT, game);
            }
            g.drawImage(sandstormOverlay, 0,0, Game.WIDTH, Game.HEIGHT, game);
        }

        /*Renders the specified time-of-day overlay

        0 --> morning
        1 --> noon
        2 --> evening
        3 --> night
        */
        g.drawImage(timesOfDay[time], 0,0, Game.WIDTH, Game.HEIGHT, game);
    }
}
