/*

Description:

One of the two available zone environment maps for the player to speed through. Includes various random map generation
functions, based on a 4 digit seed, which are unique to the zone. The zone map is recorded in a 2D array. The obstacle
placements and type of obstacles are noted as integers of various values in the array to be referenced when displaying
the map and generating the obstacles.

*/


import java.awt.*;
import java.util.*;
import java.io.*;

public class SandSpearNest {

    //Map which the zone belongs to
    private Map map;

    //Obstacles 2D array which will be modified to reflect the map of the zone
    private int[][] obstacles;

    //Respectively: seed for zone, width of zone, height of zone
    private int seed, width, height;

    //Images for the obstacles and grounds to be rendered to reflect the zone environment
    private static Image wall[], sandSpear[], ground[];

    //Random object which the zone generation is based upon
    Random r;

    //Constructor
    public SandSpearNest(Map map, int[][] obstacles, int seed) {

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
        wall = new Image[17];
        for (int i = 1; i <= 16; i++) {
            wall[i] = Toolkit.getDefaultToolkit().getImage("appdata/images/sandSpearNest/wall/" + i + ".png");
        }
        sandSpear = new Image[57];
        for (int i = 1; i <= 16; i++) {
            sandSpear[i] = Toolkit.getDefaultToolkit().getImage("appdata/images/sandSpearNest/sandSpear/" + i + ".png");
        }
        for (int i = 21; i <= 36; i++) {
            sandSpear[i] = Toolkit.getDefaultToolkit().getImage("appdata/images/sandSpearNest/sandSpear/" + i + ".png");
        }
        for (int i = 41; i <= 56; i++) {
            sandSpear[i] = Toolkit.getDefaultToolkit().getImage("appdata/images/sandSpearNest/sandSpear/" + i + ".png");
        }

        //Compile images of grounds
        ground = new Image[9];
        for (int i = 1; i <= 8; i++) {
            ground[i] = Toolkit.getDefaultToolkit().getImage("appdata/images/sandSpearNest/ground/ground" + i + ".png");
        }
    }

    //Description: generates a zone of the sand-spear nest environment
    //Parameters: none
    //Return: void
    public void generateMap() {

        //Current row
        int row = 1;

        //Generate a wall randomly and move up a specific number of rows until the entire zone is filled
        while (row <= 410) {
            generateWall(row);
            row += r.nextInt(50, 80);
        }

        //Generate a sand spear randomly until the entire zone is traversed
        for (int i = 6; i <= height - 6; i++) {
            for (int j = 6; j <= width - 6; j++) {
                if (r.nextDouble() > 0.99) {
                    generateNest(i, j);
                }
            }
        }

        //Fill the untouched cells with random grounds
        for (int i = 1; i <= height; i++) {
            for (int j = 1; j <= width; j++) {
                if (obstacles[i][j] <= 0) {
                    obstacles[i][j] = 100 + r.nextInt(1, 9);
                }
            }
        }
    }

    //Description: generate walls for a specific row of the zone
    //Parameters: the row to generate the wall
    //Return: void
    private void generateWall(int row) {

        //Current column
        int column = 2;

        //Add a wall to the column until the entire row is traversed
        while (column <= width - 3) {
            addWall(row, column);
            column += r.nextInt(25, 40);
        }
    }

    //Description: adds a wall to a collection of cells in the obstacles array
    //Parameters: the location of the wall to be added
    //Return: void
    private void addWall(int row, int column) {

        //Length of wall
        int length = r.nextInt(4, 6) * 8;

        //Add values to the obstacles array to reflect portions of the wall
        for (int i = 0; i < length; i++) {
            obstacles[row + i][column] = 110 + (i % 8) * 2 + 1;
            obstacles[row + i][column - 1] = -1;
            obstacles[row + i][column - 2] = -1;
            obstacles[row + i][column + 1] = 110 + (i % 8) * 2 + 2;
            obstacles[row + i][column + 2] = -1;
            obstacles[row + i][column + 3] = -1;
        }
    }

    //Description: adds a sand-spear nest to a collection of 16 cells in the obstacles array
    //Parameters: the location of the sand-spear to be added
    //Return: void
    private void generateNest(int row, int column) {

        //Ensure there are no other obstacles in the proximity of the nest that will be generated
        boolean isCovered = false;
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                isCovered = isCovered || obstacles[row + i][column + j] != 0;
            }
        }

        //Generate the nest
        if (!isCovered) {

            //Marks a border of values to prevent overly close generation of nests
            for (int i = -2; i < 6; i++) {
                for (int j = -2; j < 6; j++) {
                    obstacles[row + i][column + j] = (obstacles[row + i][column + j] == 0 ? -1 : obstacles[row + i][column + j]);
                }
            }

            //Random nest orientation
            int add = r.nextInt(3) * 20;

            //Add values to the obstacles array to reflect portions of the nest
            obstacles[row][column] = 141 + add;
            obstacles[row][column + 1] = 142 + add;
            obstacles[row][column + 2] = 143 + add;
            obstacles[row][column + 3] = 144 + add;

            obstacles[row + 1][column] = 145 + add;
            obstacles[row + 1][column + 1] = 146 + add;
            obstacles[row + 1][column + 2] = 147 + add;
            obstacles[row + 1][column + 3] = 148 + add;

            obstacles[row + 2][column] = 149 + add;
            obstacles[row + 2][column + 1] = 150 + add;
            obstacles[row + 2][column + 2] = 151 + add;
            obstacles[row + 2][column + 3] = 152 + add;

            obstacles[row + 3][column] = 153 + add;
            obstacles[row + 3][column + 1] = 154 + add;
            obstacles[row + 3][column + 2] = 155 + add;
            obstacles[row + 3][column + 3] = 156 + add;
        }
    }

    public Image getWall(int index) {

        //Return the wanted image of wall
        return wall[index];
    }

    public Image getSandSpear(int index) {

        //Return the wanted image of sand spear
        return sandSpear[index];
    }

    public Image getGround(int index) {

        //Return the wanted image of ground
        return ground[index];
    }
}
