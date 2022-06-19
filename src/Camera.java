/*

Description:

The camera class, locked onto the player, responsible for mapping the real x and y-coordinates of the objects in the
game to a relative x and y-coordinate to be displayed on the screen. Further, allows checks of whether an object is out
of the current screen area and thus, allows for more efficient use of resources without compromising the
user-experience.

*/


import java.util.*;

public class Camera {

    //The player to lock onto
    private Player player;

    //The coordinates of the camera relative to the obstacles, dimensions of camera
    private static int relX, relY, width, height;

    //Constructor
    public Camera(Player player) {

        //Initialize variables
        this.player = player;
        relX = player.x - Game.WIDTH/2+16;
        relY = player.y-Game.HEIGHT+150;
        width = Game.WIDTH;
        height = Game.HEIGHT;
    }

    //Description: updates the camera position with reference to the speeder position
    //Parameters: none
    //Return: void
    public void tick() {

        //Update coordinates of the camera relative to the obstacles
        relX = player.x - Game.WIDTH/2+16;
        relY = player.y-Game.HEIGHT+150;
    }

    //Description: returns the relative coordinate of the x-value given in the parameter to the camera’s field-of-view
    //Parameters: the x-value
    //Return: the relative x-coordinate
    public static int getRelX(int x) {

        //Relative x-coordinate of the provided absolute x-coordinate to the camera
        return (x-relX);
    }

    //Description: returns the relative coordinate of the y-value given in the parameter to the camera’s field-of-view
    //Parameters: the y-value
    //Return: the relative y-coordinate
    public static int getRelY(int y) {

        //Relative y-coordinate of the provided absolute y-coordinate to the camera
        return (y-relY);
    }

    public static int getRelX() {
        return relX;
    }

    public static int getRelY() {
        return relY;
    }

    //Description: verifies whether the given GameObject is out of the camera’s field-of-view
    //Parameters: the GameObject in question
    //Return: whether the given GameObject is out of the camera’s field-of-view
    public static boolean outOfFrame(GameObject object) {

        //Whether the obstacle is entirely out of the camera's field-of-view
        return (object.x > relX + width || (object.x + object.width) < relX ) ||
                (object.y > relY + height || (object.y + object.height) < relY );

//        return !((object.x + object.width> relX && object.x < relX + width) && (object.y + object.height < relY && object.y < relY + height));
    }

    //Description: verifies whether a GameObject with given location and dimensions is out of the camera’s field-of-view
    //Parameters: the location and dimensions of the GameObject
    //Return: whether a GameObject with given location and dimensions is out of the camera’s field-of-view
    public static boolean outOfFrame(int x, int y, int objectWidth, int objectHeight) {

        //Whether the obstacle with the coordinates and dimensions provided is entirely out of the camera's field-of-view
        return (x > relX + width || (x + objectWidth) < relX ) ||
                (y > relY + height || (y + objectHeight) < relY );

//        return !((x + objectWidth > relX && x < relX + width) && (y + objectHeight < relY && y < relY + height));
    }

}
