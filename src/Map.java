import java.awt.*;
import java.util.Random;

public class Map {
    public static int obstacleSize = 32;
    public static int height = 450, width = 800, type;
    private int[][] obstacles = new int[height + 1][width + 1];
    private Handler handler, ghandler;
    private Player player;
    Random r;

    private SandSpearNest sandSpearNest;
    private DiamondSwarm diamondSwarm;

    public Map(int type, int seed, Handler handler, Handler ghander, Player player) {
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
        this.handler = handler;
        this.ghandler = ghander;
        this.player = player;
        this.type = type;
        sandSpearNest = new SandSpearNest(this, obstacles, r.nextInt(10000));
        diamondSwarm = new DiamondSwarm(this, obstacles, r.nextInt(10000));
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

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public void tick() {
        for (int i = 1; i <= height; i++) {
            for (int j = 1; j <= width; j++) {
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
