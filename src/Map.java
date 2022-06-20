/*

Description:

Generates the map for a zone in the game. Calls map generating functions from various environment map generating
classes to store integer values in a 2D array to reflect type and placement of the obstacle. Further, generates airborne
objects for the game via a similar 2D array, storing its randomly generated velocity and set dimensions in a custom Pair
class. Responsible for adding objects that are in the camera frame to the handler to be updated and rendered.

*/

import java.util.*;

public class Map {

    //Size of each obstacle unit
    public static int obstacleSize = 32;

    //Number of obstacles in the y and x-direction, type of zone, respectively
    public static int height = 450, width = 800, type;

    //2D obstacle array to store obstacle placement and type information
    private int[][] obstacles = new int[height + 1][width + 1];

    //2D Pair array to store the AirborneObjects information
    private Pair[][] airborneObject = new Pair[height + 1][width + 1];

    //Handler for the obstacles, ground, airborne objects, respectively
    private Handler handler, ghandler, ahandler;

    //Other game components to be referenced to
    private Player player;
    private Game game;

    //Random object to generate the map
    Random r;

    //Map generators for the two available zones
    private SandSpearNest sandSpearNest;
    private DiamondSwarm diamondSwarm;

    //Custom private pair class for a 4 tuple
    private static class Pair {
        int v;
        int w;
        int x;
        int y;

        //Constructor
        public Pair(int v0, int w0, int x0, int y0) {
            v = v0;
            w = w0;
            x = x0;
            y = y0;
        }
    }

    //Constructor
    public Map(int type, int seed, Handler handler, Handler ghander, Handler ahandler, Player player, Game game) {

        //Set seed for random object
        r = new Random(seed);

        //Initialize variables
        this.handler = handler;
        this.ghandler = ghander;
        this.ahandler = ahandler;
        this.player = player;
        this.game = game;
        this.type = type;


        //Initializing the map generators
        sandSpearNest = new SandSpearNest(this, obstacles, r.nextInt(10000));
        diamondSwarm = new DiamondSwarm(this, obstacles, r.nextInt(10000));

//        for (int i = 1; i <= height; i++) {
//            for (int j = 1; j <= width; j++) {
//                obstacles[i][j] = (r.nextDouble() > 0.95 ? 1 : 2);
//            }
//        }
        //Debugging map
//        for (int i = 1; i <= height; i++) {
//            for (int j = 1; j <= width; j++) {
//               if (j % 6 == 0 && i%15 < 1) obstacles[i][j] = 1;
//               else obstacles[i][j] = 2;
//            }
//        }

        //Array for direction of movement of the airborne objects
        int[][] vel = {
                {-1, -1, 0, 1, 1, 1, 0, -1},
                {0, -1, -1, -1, 0, 1, 1, 1},
        };

        //Generating the airborne objects
        for (int i = 1; i <= height; i++) {
            for (int j = 1; j <= width; j++) {
                if (r.nextDouble() > 0.9997) {
                    int index = r.nextInt(0, 8);

                    //(v, w, x, y) = (velX, velY, width, height)
                    airborneObject[i][j] = new Pair(vel[0][index] * r.nextInt(2, 4), vel[1][index] * r.nextInt(2, 4), 64, 64);
                }
            }
        }

        //Generate map based on the type provided
        if (type == 1) {
            sandSpearNest.generateMap();
        } else if (type == 2) {
            diamondSwarm.generateMap();
        }

        //Debugging code: need removal
//        this.type = 2;
//        for (int i = 1; i <= height; i++) {
//            for (int j = 1; j <= width; j++) {
//                if (j % 6 == 0 && i % 15 < 14) obstacles[i][j] = 221;
//                else obstacles[i][j] = 201;
//            }
//        }
    }

    //Description: adds the obstacles or ground tiles from the current map that are in the camera field-of-view to the according handler
    //Parameters: none
    //Return: void
    public void tick() {
        for (int i = 1; i <= height; i++) {
            for (int j = 1; j <= width; j++) {

                //Add airborne objects to the handler if it is in the camera's field of view
                if (airborneObject[i][j] != null &&
                        !Camera.outOfFrame((j - width / 2) * obstacleSize, -i * obstacleSize,
                                airborneObject[i][j].x, airborneObject[i][j].y)) {

                    new AirborneObject("appdata/images/drone/", ID.AirBorne,
                            (j - width / 2) * obstacleSize, -i * obstacleSize,
                            airborneObject[i][j].x, airborneObject[i][j].y,
                            airborneObject[i][j].v, airborneObject[i][j].w,
                            ahandler, player, this, game);

                    airborneObject[i][j] = null;
                }

                //Depending on the type of zone and the value in the obstacles 2D array, add designated GameObjects to
                // the handler if it is in the camera's field of view

                //Sand-Spear Nest:
                if (type == 1) {

                    //Wall
                    if (obstacles[i][j] >= 110 && obstacles[i][j] < 140 &&
                            !Camera.outOfFrame((j - width / 2) * obstacleSize, -i * obstacleSize,
                                    obstacleSize, obstacleSize)) {

                        new Obstacle(sandSpearNest.getWall(obstacles[i][j] - 110), obstacles[i][j],
                                (j - width / 2) * obstacleSize, -i * obstacleSize,
                                obstacleSize, obstacleSize, ID.Obstacle, handler, this, player, game);

                        obstacles[i][j] = 0;
                    }

                    //Sand-Spear
                    else if (obstacles[i][j] >= 140 &&
                            !Camera.outOfFrame((j - width / 2) * obstacleSize, -i * obstacleSize,
                                    obstacleSize, obstacleSize)) {

                        new Obstacle(sandSpearNest.getSandSpear(obstacles[i][j] - 140), obstacles[i][j],
                                (j - width / 2) * obstacleSize, -i * obstacleSize,
                                obstacleSize, obstacleSize, ID.Obstacle, handler, this, player, game);

                        obstacles[i][j] = 0;

                    }

                    //Ground
                    else if (obstacles[i][j] > 100 &&
                            !Camera.outOfFrame((j - width / 2) * obstacleSize, -i * obstacleSize,
                                    obstacleSize, obstacleSize)) {

                        new Ground(sandSpearNest.getGround(obstacles[i][j] - 100), obstacles[i][j],
                                (j - width / 2) * obstacleSize, -i * obstacleSize,
                                obstacleSize, obstacleSize, ID.Ground, ghandler, this, game);

                        obstacles[i][j] = 0;

                    }

                }

                //Diamond Swarm:
                else if (type == 2) {

                    //Diamond
                    if (obstacles[i][j] >= 220 && obstacles[i][j] < 250 &&
                            !Camera.outOfFrame((j - width / 2) * obstacleSize, -i * obstacleSize,
                                    obstacleSize, obstacleSize)) {

                        new Obstacle(diamondSwarm.getDiamond(obstacles[i][j] - 220), obstacles[i][j],
                                (j - width / 2) * obstacleSize,-i * obstacleSize,
                                obstacleSize, obstacleSize, ID.Obstacle, handler, this, player, game);

                        obstacles[i][j] = 0;

                    }

                    //Ground
                    else if (obstacles[i][j] > 200 &&
                            !Camera.outOfFrame((j - width / 2) * obstacleSize, -i * obstacleSize,
                                    obstacleSize, obstacleSize)) {

                        new Ground(diamondSwarm.getGround(obstacles[i][j] - 200), obstacles[i][j],
                                (j - width / 2) * obstacleSize, -i * obstacleSize,
                                obstacleSize, obstacleSize, ID.Ground, ghandler, this, game);

                        obstacles[i][j] = 0;

                    }
                }
            }
        }
    }


    public int[][] getObstacles() {
        return obstacles;
    }

    public void setObstacles(int i, int j, int value) {
        obstacles[i][j] = value;
    }

    //Useless method: Need removal
    public void setAirborneObject(int i, int j, int v0, int w0, int x0, int y0) {
        if (i <= 0 && i >= height && j <= 0 && j >= width)
            airborneObject[i][j] = new Pair(v0, w0, x0, y0);
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
