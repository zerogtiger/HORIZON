import java.awt.*;
import java.util.Random;

public class Map {
    public static int obstacleSize = 32;
    public static int height = 450, width = 800;
    private int[][] obstacles = new int[height + 1][width + 1];
    private Handler handler, ghandler;
    private Player player;
    Random r;

    public Map(int type, int seed, Handler handler, Handler ghander, Player player) {
        r = new Random(seed);
//        for (int i = 1; i <= height; i++) {
//            for (int j = 1; j <= width; j++) {
//                obstacles[i][j] = (r.nextDouble()>0.95? 1: 2);
//            }
//        }
        //Debugging map
        for (int i = 1; i <= height; i++) {
            for (int j = 1; j <= width; j++) {
               if (j % 4 == 0 && i%15 < 9) obstacles[i][j] = 1;
               else obstacles[i][j] = 2;
            }
        }
        this.handler = handler;
        this.ghandler = ghander;
        this.player = player;
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
                if (obstacles[i][j] == 1 && !Camera.outOfFrame((j-width/2)*obstacleSize, -i*obstacleSize, obstacleSize, obstacleSize)) {
                    new Obstacle((j-width/2)*obstacleSize, -i*obstacleSize, obstacleSize, obstacleSize, ID.Obstacle, handler, this, player);
                    obstacles[i][j] = 0;
                }
                else if(obstacles[i][j] == 2 && !Camera.outOfFrame((j-width/2)*obstacleSize, -i*obstacleSize, obstacleSize, obstacleSize)) {
                    new Ground((j-width/2)*obstacleSize, -i*obstacleSize, obstacleSize, obstacleSize, ID.Ground, ghandler, this,
                            new Color(69, 69, 69));
                    obstacles[i][j] = 0;
                }
            }
        }
    }
}
