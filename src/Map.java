import java.awt.*;
import java.util.Random;

public class Map {
    public static int height = 450, width = 800;
    private int[][] obstacles = new int[height + 1][width + 1];
    private Handler handler, ghandler;
    Random r;

    public Map(int type, int seed, Handler handler, Handler ghander) {
        r = new Random(seed);
        for (int i = 1; i <= height; i++) {
            for (int j = 1; j <= width; j++) {
                obstacles[i][j] = (r.nextDouble()>0.95? 1: 2);
            }
        }
        this.handler = handler;
        this.ghandler = ghander;
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
                if (obstacles[i][j] == 1 && !Camera.outOfFrame((j-width/2)*32, -i*32, 32, 32)) {
                    new Obstacle((j-width/2)*32, -i*32, 32, 32, ID.Obstacle, handler, this);
                    obstacles[i][j] = 0;
                }
                else if(obstacles[i][j] == 2 && !Camera.outOfFrame((j-width/2)*32, -i*32, 32, 32)) {
                    new Ground((j-width/2)*32, -i*32, 32, 32, ID.Ground, ghandler, this,
                            new Color(r.nextInt(255), r.nextInt(255), r.nextInt(255), 30));
                    obstacles[i][j] = 0;
                }
            }
        }
    }
}
