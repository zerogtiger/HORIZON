/*

Description:

One of the two available zone environment maps for the player to speed through. Includes various random map generation
functions, based on a 4 digit seed, which are unique to the zone. The zone map is recorded in a 2D array. The obstacle
placements and type of obstacles are noted as integers of various values in the array to be referenced when displaying
the map and generating the obstacles.

*/


import java.awt.*;
import java.util.*;

public class DiamondSwarm {

    //Map which the zone belongs to
    private Map map;

    //Obstacles 2D array which will be modified to reflect the map of the zone
    private int[][] obstacles;

    //Respectively: seed for zone, width of zone, height of zone
    private int seed, width, height;

    //Images for the obstacles and grounds to be rendered to reflect the zone environment
    private static Image diamond[][], ground[];

    //Random object which the zone generation is based upon
    Random r;

    //Constructor
    public DiamondSwarm(Map map, int[][] obstacles, int seed) {

        //Initialize variables
        this.map = map;
        this.obstacles = obstacles;
        this.seed = seed;
        height = obstacles.length - 1;
        width = obstacles[1].length - 1;

        //Initialize random object
        r = new Random(seed);

        //Fill the array with a standard value
        for (int i = 1; i <= height; i++) {
            Arrays.fill(obstacles[i], 0);
        }

        //Compile images of obstacles
        diamond = new Image[3][5];
        for (int i = 1; i <= 2; i++) {
            for (int j = 1; j <= 4; j++) {
                diamond[i][j] = Toolkit.getDefaultToolkit().getImage("appdata/pics/diamondSwarm/diamond/" + i + j + ".png");
            }
        }

        //Compile images of grounds
        ground = new Image[19];
        for (int i = 1; i <= 18; i++) {
            ground[i] = Toolkit.getDefaultToolkit().getImage("appdata/pics/diamondSwarm/ground" + i + ".png");
        }
    }

    //Description: generates a zone of the diamond swarm environment
    //Parameters: none
    //Return: void
    public void generateMap() {

        //Current row
        int row = 1;

        //Generate a random barrier and move up a specific number of rows until the entire zone is filled
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

        //Fill the untouched cells with random grounds
        for (int i = 1; i <= height; i++) {
            for (int j = 1; j <= width; j++) {
                if (obstacles[i][j] <= 0) {
                    obstacles[i][j] = 200 + r.nextInt(1, 19);
                }
            }
        }
    }

    //Description: generates the first style of barriers across an entire row
    //Parameters: the row to generate the obstacles
    //Return: void
    /*
    ◼       ◼       ◼...

        ◼       ◼    ...
    */
    public void generateBarrier(int row) {

        //Current column
        int column = 3;

        //Generate a zigzag pattern of diamonds until the entire row is filled
        while (column <= width - 6) {
            addDiamond(row, column);
            addDiamond(row + 7, column + 3);
            column += 6;
        }
    }

    //Description: generates the second style of barriers across an entire row
    //Parameters: the row to generate the obstacles
    //Return: void
    /*
    ◼◼◼◼ ◼◼◼◼            ◼◼◼◼ ◼◼◼◼...
                ◼◼◼◼ ◼◼◼◼             ...
    */
    public void generateBarrier2(int row) {

        //Current column and row
        int column = 2;
        int tempRow = row;

        //Generate an alternating barrier of diamonds until the entire row is filled
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

    //Description: generates the third style of barriers across an entire row
    //Parameters: the row to generate the obstacles
    //Return: void
    /*
    ◼   ◼        ...
    ◼           ◼...
    ◼   ◼       ◼...
    ◼   ◼       ◼...
    ◼   ◼         ...
    ◼   ◼       ◼...
    ◼   ◼       ◼...
    ◼           ◼...
    ◼   ◼       ◼...
     */
    public void generateBarrier3(int row) {

        //Current column
        int column = 2;

        //Generate a random, vertical lineup of diamonds until the entire row is filled
        while (column <= width - 2) {
            for (int i = 1; i <= 9; i++) {
                addDiamond(row + i * 2, column);
                i += r.nextInt(0, 3);
            }
            column += r.nextInt(2, 5) * 2;
        }
    }

    //Description: adds a diamond to a collection of 4 cells in the obstacles array
    //Parameters: the location of the diamond to be added
    //Return: void
    private void addDiamond(int row, int column) {

        //Random diamond orientation
        int add = (r.nextInt(2) == 0 ? 10 : 20);

        //Fill the collection of 4 cells with the values each representing a quarter of a diamond
        obstacles[row][column] = 221 + add;
        obstacles[row][column + 1] = 222 + add;
        obstacles[row + 1][column] = 223 + add;
        obstacles[row + 1][column + 1] = 224 + add;
    }

    public Image getDiamond(int index) {

        //Return the wanted image of diamond
        return diamond[index / 10][index % 10];
    }

    public Image getGround(int index) {

        //Return the wanted image of ground
        return ground[index];
    }
}
