import java.awt.*;
import java.util.*;

public class Map {
    public static int obstacleSize = 32;
    public static int height = 450, width = 800, type;
    private int[][] obstacles = new int[height + 1][width + 1];
    private Pair[][] airborneObject = new Pair[height + 1][width + 1];
    private Handler handler, ghandler, ahandler;
    private Player player;
    Random r;

    private SandSpearNest sandSpearNest;
    private DiamondSwarm diamondSwarm;

    private static class Pair implements Comparable<Pair> {
        int v;
        int w;
        int x;
        int y;

        public Pair(int v0, int w0, int x0, int y0) {
            v = v0;
            w = w0;
            x = x0;
            y = y0;
        }

        @Override
        public String toString() {
            return ("(" + v + ", " + w + ")");
        }

        @Override
        public int compareTo(Pair other) {
            return Integer.compare(w, other.w);
        }
    }

    public Map(int type, int seed, Handler handler, Handler ghander, Handler ahandler, Player player) {
        r = new Random(seed);
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
        int[][] vel = {
                {-1, -1, 0, 1, 1, 1, 0, -1},
                {0, -1, -1, -1, 0, 1, 1, 1},
        };
        for (int i = 1; i <= height; i++) {
            for (int j = 1; j <= width; j++) {
                if (r.nextDouble() > 0.9997) {
                    int index = r.nextInt(0, 8);

                    //                                               velX                                       velY                                        width    height
                    airborneObject[i][j] = new Pair(vel[0][index] * r.nextInt(2, 4), vel[1][index] * r.nextInt(2, 4), 64, 64);
                }
            }
        }

        this.handler = handler;
        this.ghandler = ghander;
        this.ahandler = ahandler;
        this.player = player;
        this.type = type;

        sandSpearNest = new SandSpearNest(this, obstacles, r.nextInt(10000));
        diamondSwarm = new DiamondSwarm(this, obstacles, r.nextInt(10000));
//        drone = new AirBorneObject(Toolkit.getDefaultToolkit().getImage("appdata/pics/drone.gif"), ID.AirBorne, 0,0, 64, 64, 0, 1, handler);
        if (type == 1) {
            sandSpearNest.generateMap();
        } else if (type == 2) {
            diamondSwarm.generateMap();
        }
    }

    public int[][] getObstacles() {
        return obstacles;
    }

    public void setObstacles(int i, int j, int value) {
        obstacles[i][j] = value;
    }

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

    public void tick() {
        for (int i = 1; i <= height; i++) {
            for (int j = 1; j <= width; j++) {
                if (airborneObject[i][j] != null && !Camera.outOfFrame((j - width / 2) * obstacleSize, -i * obstacleSize, airborneObject[i][j].x, airborneObject[i][j].y)) {
                    new AirBorneObject("appdata/pics/drone/", ID.AirBorne,
                            (j - width / 2) * obstacleSize, -i * obstacleSize,
                            airborneObject[i][j].x, airborneObject[i][j].y, airborneObject[i][j].v, airborneObject[i][j].w, ahandler, player, this);
                    airborneObject[i][j] = null;
                }
                if (type == 1) {
                    if (obstacles[i][j] >= 110 && obstacles[i][j] < 140 && !Camera.outOfFrame((j - width / 2) * obstacleSize, -i * obstacleSize, obstacleSize, obstacleSize)) {
                        new Obstacle(sandSpearNest.getWall(obstacles[i][j] - 110), obstacles[i][j],
                                (j - width / 2) * obstacleSize, -i * obstacleSize, obstacleSize, obstacleSize,
                                ID.Obstacle, handler, this, player);
                        obstacles[i][j] = 0;
                    }
                    if (obstacles[i][j] >= 140 && !Camera.outOfFrame((j - width / 2) * obstacleSize, -i * obstacleSize, obstacleSize, obstacleSize)) {
                        new Obstacle(sandSpearNest.getSandSpear(obstacles[i][j] - 140), obstacles[i][j],
                                (j - width / 2) * obstacleSize, -i * obstacleSize, obstacleSize, obstacleSize,
                                ID.Obstacle, handler, this, player);
                        obstacles[i][j] = 0;
                    } else if (obstacles[i][j] > 100 && !Camera.outOfFrame((j - width / 2) * obstacleSize, -i * obstacleSize, obstacleSize, obstacleSize)) {
                        new Ground(sandSpearNest.getGround(obstacles[i][j] - 100), obstacles[i][j],
                                (j - width / 2) * obstacleSize, -i * obstacleSize, obstacleSize, obstacleSize,
                                ID.Ground, ghandler, this);
                        obstacles[i][j] = 0;
                    }
                } else if (type == 2) {
                    if (obstacles[i][j] >= 220 && obstacles[i][j] < 250 && !Camera.outOfFrame((j - width / 2) * obstacleSize, -i * obstacleSize, obstacleSize, obstacleSize)) {
                        new Obstacle(diamondSwarm.getDiamond(obstacles[i][j] - 220), obstacles[i][j],
                                (j - width / 2) * obstacleSize,

                                -i * obstacleSize, obstacleSize, obstacleSize,
                                ID.Obstacle, handler, this, player);
                        obstacles[i][j] = 0;
                    } else if (obstacles[i][j] > 200 && !Camera.outOfFrame((j - width / 2) * obstacleSize, -i * obstacleSize, obstacleSize, obstacleSize)) {
                        new Ground(diamondSwarm.getGround(obstacles[i][j] - 200), obstacles[i][j],
                                (j - width / 2) * obstacleSize, -i * obstacleSize, obstacleSize, obstacleSize,
                                ID.Ground, ghandler, this);
                        obstacles[i][j] = 0;
                    }
                }
            }
        }
    }
}
