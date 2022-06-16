import java.awt.*;
import java.util.*;
import java.io.*;

public class SandSpearNest {
    private Map map;
    private int[][] obstacles;
    private int seed, width, height;
    private static Image wall[], sandSpear[], ground[];
    Random r;

    public SandSpearNest(Map map, int[][] obstacles, int seed) {
        this.map = map;
        this.obstacles = obstacles;
        this.seed = seed;
        r = new Random(seed);
        height = obstacles.length - 1;
        width = obstacles[1].length - 1;
        for (int i = 1; i <= height; i++) {
            Arrays.fill(obstacles[i], 0);
        }
        wall = new Image[17];
        for (int i = 1; i <= 16; i++) {
            wall[i] = Toolkit.getDefaultToolkit().getImage("appdata/pics/sandSpearNest/wall/" + i + ".png");
        }
        sandSpear = new Image[57];
        for (int i = 1; i <= 16; i++) {
            sandSpear[i] = Toolkit.getDefaultToolkit().getImage("appdata/pics/sandSpearNest/sandSpear/" + i + ".png");
        }
        for (int i = 21; i <= 36; i++) {
            sandSpear[i] = Toolkit.getDefaultToolkit().getImage("appdata/pics/sandSpearNest/sandSpear/" + i + ".png");
        }
        for (int i = 41; i <= 56; i++) {
            sandSpear[i] = Toolkit.getDefaultToolkit().getImage("appdata/pics/sandSpearNest/sandSpear/" + i + ".png");
        }
        ground = new Image[9];
        for (int i = 1; i <= 8; i++) {
            ground[i] = Toolkit.getDefaultToolkit().getImage("appdata/pics/sandSpearNest/ground" + i + ".png");
        }
    }

    public void generateMap() {
        int row = 1;
        while (row <= 410) {
            generateWall(row);
            row += r.nextInt(50, 80);
        }
        for (int i = 6; i <= height - 6; i++) {
            for (int j = 6; j <= width - 6; j++) {
                if (r.nextDouble() > 0.99) {
                    generateNest(i, j);
                }
            }
        }
        for (int i = 1; i <= height; i++) {
            for (int j = 1; j <= width; j++) {
                if (obstacles[i][j] <= 0) {
                    obstacles[i][j] = 100 + r.nextInt(1, 9);
                }
            }
        }
    }

    private void generateWall(int row) {
        int column = 2;
        while (column <= width - 3) {
            addWall(row, column);
            column += r.nextInt(25, 40);
        }
    }

    private void addWall(int row, int column) {
        int length = r.nextInt(4, 6) * 8;
        for (int i = 0; i < length; i++) {
            obstacles[row + i][column] = 110 + (i % 8) * 2 + 1;
            obstacles[row + i][column - 1] = -1;
            obstacles[row + i][column - 2] = -1;
            obstacles[row + i][column + 1] = 110 + (i % 8) * 2 + 2;
            obstacles[row + i][column + 2] = -1;
            obstacles[row + i][column + 3] = -1;
        }
    }

    private void generateNest(int row, int column) {
        boolean isCovered = false;
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                isCovered = isCovered || obstacles[row + i][column + j] != 0;
            }
        }
        if (!isCovered) {
            for (int i = -2; i < 6; i++) {
                for (int j = -2; j < 6; j++) {
                    obstacles[row + i][column + j] = (obstacles[row + i][column + j] == 0 ? -1 : obstacles[row + i][column + j]);
                }
            }
            int add = r.nextInt(3)*20;
            obstacles[row][column] = 141+add;
            obstacles[row][column+1] = 142+add;
            obstacles[row][column+2] = 143+add;
            obstacles[row][column+3] = 144+add;

            obstacles[row+1][column] = 145+add;
            obstacles[row+1][column+1] = 146+add;
            obstacles[row+1][column+2] = 147+add;
            obstacles[row+1][column+3] = 148+add;

            obstacles[row+2][column] = 149+add;
            obstacles[row+2][column+1] = 150+add;
            obstacles[row+2][column+2] = 151+add;
            obstacles[row+2][column+3] = 152+add;

            obstacles[row+3][column] = 153+add;
            obstacles[row+3][column+1] = 154+add;
            obstacles[row+3][column+2] = 155+add;
            obstacles[row+3][column+3] = 156+add;
        }
    }

    public Image getWall(int index) {
        return wall[index];
    }

    public Image getSandSpear(int index) {
        return sandSpear[index];
    }
    public Image getGround(int index) {
        return ground[index];
    }
//
//    private void addNest(int row, int column) {
//
//    }


}
