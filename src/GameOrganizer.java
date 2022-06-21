/*

Description:

Organizes the game: specifically, the map generation and endlessness aspect of the game. Responsible for generating a
new map based on a seed for a random object once the player reaches the end of the previous, and for resetting the
random object should the player die of collision or pursuer.

*/

import java.awt.*;
import java.util.*;

public class GameOrganizer {

    //Random object which will be the basis of the random, seed-based map generation
    private Random r;

    //Game components to be referenced to
    private Game game;
    private Player player;
    private Pursuer pursuer;
    private Map map;
    private Handler handler, ghandler, ahandler;

    //Counter to keep track of the number of maps traversed
    private long zoneCounter = -1;
    private long currCounter = 0;

    //Seed for random object and temporary variables to store information of the next zone, respectively
    private int seed, tempSeed, tempType;

    //Constructor
    public GameOrganizer(Game game) {

        //Initialize variables
        this.game = game;
        this.seed = game.getSeed();

        //Set the seed for the random object as the game seed's last 4 digits
        r = new Random(seed%10000);
        
        //Generate next seed and zone type
        tempType = r.nextInt(1,3);
        tempSeed = r.nextInt(10000);

        this.handler = game.getHandler();
        this.ghandler = game.getGhandler();
        this.ahandler = game.getAhandler();

        this.player = Game.getPlayer();
        this.pursuer = game.getPursuer();
        this.map = game.getMap();
    }

    //Description: checks whether the player has reached the end of a zone and perform actions accordingly
    //Parameters: none
    //Return: void
    public void tick() {

        //Generate a new map if the speeder traversed through another zone
        if (zoneCounter < player.getDistance() / 17000) {

            //Reset the player's position to the start of the new map
            player.setX(-16);
            player.setY(Game.HEIGHT*2);

            //Increase the pursuer's distance to account for the time between transitions
            pursuer.setDistance(Game.clamp(pursuer.getDistance() + 1200, 0, 11000));

            //Generate a new map
            game.setMap(new Map(tempType, tempSeed, handler, ghandler, ahandler, player, game));

            //Forward the zoneCounter
            zoneCounter++;
            System.out.println("new map");
        }
        if (currCounter < (player.getDistance()+8500) / 17000) {

            //Generate next seed and zone type
            tempType = r.nextInt(1,3);
            tempSeed = r.nextInt(10000);

            //Forward the currCounter
            currCounter++;
        }
    }

    //Description: resets the seed
    //Parameters: none
    //Return: void
    public void reset() {

        //Reset the seed and the counters
        r.setSeed(seed%10000);
        zoneCounter = -1;
        currCounter = 0;

        //Generate next seed and zone type
        tempType = r.nextInt(1,3);
        tempSeed = r.nextInt(10000);
    }

    public void setSeed(int seed) {
        this.seed = seed;
    }

    public long getZoneCounter() {
        return zoneCounter;
    }

    public long getCurrCounter() {
        return currCounter;
    }

    public int getTempType() {
        return tempType;
    }
}
