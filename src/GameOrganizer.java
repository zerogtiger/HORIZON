/*

Description:

Organizes the game: specifically, the map generation and endlessness aspect of the game. Responsible for generating a
new map based on a seed for a random object once the player reaches the end of the previous, and for resetting the
random object should the player die of collision or pursuer.

*/

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
    private long counter = -1;

    //Seed for random object
    private int seed;

    //Constructor
    public GameOrganizer(Game game) {

        //Initialize variables
        this.game = game;
        this.seed = game.getSeed();

        //Set the seed for the random object as the game seed's last 4 digits
        r = new Random(seed%10000);

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
        if (counter < Stats.speederDistance / 16000) {

            //Reset the player's position to the start of the new map
            player.setX(-16);
            player.setY(Game.HEIGHT);

            //Increase the pursuer's distance to account for the time between transitions
            pursuer.setDistance(Game.clamp(pursuer.getDistance() + 1000, 0, 11000));

            //Generate a new map
            game.setMap(new Map(r.nextInt(1,3), r.nextInt(10000), handler, ghandler, ahandler, player, game));

            //Forward the counter
            counter++;
            System.out.println("new map");
        }
    }

    //Description: resets the seed
    //Parameters: none
    //Return: void
    public void reset() {

        //Reset the seed and the counter
        r.setSeed(seed%10000);
        counter = -1;
    }

    public void setSeed(int seed) {
        this.seed = seed;
    }
}
