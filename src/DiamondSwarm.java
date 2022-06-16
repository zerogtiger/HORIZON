import java.awt.*;
import java.util.*;

public class DiamondSwarm {
    private Map map;
    private int[][] obstacles;
    private int seed, width, height;
    private static Image diamond[][], ground[];
    Random r;

    public DiamondSwarm(Map map, int[][] obstacles, int seed) {
        this.map = map;
        this.obstacles = obstacles;
        this.seed = seed;
        r = new Random(seed);
        height = obstacles.length - 1;
        width = obstacles[1].length - 1;
        for (int i = 1; i <= height; i++) {
            Arrays.fill(obstacles[i], 0);
        }

        diamond = new Image[3][5];
        for (int i = 1; i <= 2; i++) {
            for (int j = 1; j <= 4; j++) {
                diamond[i][j] = Toolkit.getDefaultToolkit().getImage("appdata/pics/diamondSwarm/diamond/" + i + j + ".png");
            }
        }

        ground = new Image[19];
        for (int i = 1; i <= 18; i++) {
            ground[i] = Toolkit.getDefaultToolkit().getImage("appdata/pics/diamondSwarm/ground" + i + ".png");
        }
    }

    public void generateMap() {
        int row = 1;
        while (row <= height - 20) {
            int type = r.nextInt(0, 3);
            if (type == 0) {
                generateBarrier(row);
                row += 16;
            } else if (type == 1) {
                generateBarrier2(row);
                row += 18;
            } else if (type == 2) {
                generateBarrier3(row);
                row += 26;
            }
        }
        for (int i = 1; i <= height; i++) {
            for (int j = 1; j <= width; j++) {
                if (obstacles[i][j] <= 0) {
                    obstacles[i][j] = 200 + r.nextInt(1, 19);
                }
            }
        }
    }

    /*
    ◼       ◼       ◼

        ◼       ◼
    */
    public void generateBarrier(int row) {
        int column = 3;
        while (column <= width - 6) {
            addDiamond(row, column);
            addDiamond(row + 7, column + 3);
            column += 6;
        }
    }

    /*
    ◼◼◼◼ ◼◼◼◼            ◼◼◼◼ ◼◼◼◼
                ◼◼◼◼ ◼◼◼◼
    */
    public void generateBarrier2(int row) {
        int column = 2;
        int tempRow = row;
        while (column <= width - 20) {
            for (int i = 1; i <= 4; i++) {
                addDiamond(tempRow, column);
                column += 2;
            }
            column += 3;
            for (int i = 1; i <= 4; i++) {
                addDiamond(tempRow, column);
                column += 2;
            }
            tempRow = (tempRow == row ? row + r.nextInt(7, 9) : row);
        }
    }

    /*
    ◼   ◼
    ◼           ◼
    ◼   ◼       ◼
    ◼   ◼       ◼
    ◼   ◼
    ◼   ◼       ◼
    ◼   ◼       ◼
    ◼           ◼
    ◼   ◼       ◼
     */
    public void generateBarrier3(int row) {
        int column = 2;
        while (column <= width - 2) {
            for (int i = 1; i <= 9; i++) {
                addDiamond(row + i * 2, column);
                i += r.nextInt(0, 3);
            }
            column += r.nextInt(2, 5) * 2;
        }
    }

    private void addDiamond(int row, int column) {
        int add = (r.nextInt(2) == 0 ? 10 : 20);
        obstacles[row][column] = 221 + add;
        obstacles[row][column + 1] = 222 + add;
        obstacles[row + 1][column] = 223 + add;
        obstacles[row + 1][column + 1] = 224 + add;
    }

    public Image getDiamond(int index) {
        return diamond[index / 10][index % 10];
    }

    public Image getGround(int index) {
        return ground[index];
    }
}
