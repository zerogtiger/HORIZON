/*

Description:

Ground objects for the game. Includes location, dimensions, and the image which should be displayed for the ground.
Verifies that itself is still in the camera frame on each game tick and removes itself from the handler if not so to
ensure efficiency.

*/

import java.awt.*;

public class Ground extends GameObject{

    //Game components to be referenced to
    private Map map;
    private Game game;

    //Image to be rendered as the ground
    private Image image;

    //The value that represents this ground object that was assigned in the 2D obstacles array
    private int value;

    //Constructor
    public Ground(Image image, int value, int x, int y, int width, int height, ID id, Handler handler, Map map, Game game) {

        //Super GameObject
        super(x, y, id, handler);

        //Initialize variables
        this.width = width;
        this.height = height;
        this.map = map;
        this.game = game;
        this.image = image;
        this.value = value;
    }

    //the ground does not have any collision bounds and thus, return null for all
    public Rectangle getBounds() {
        return null;
    }

    public Rectangle getLeftChargingBounds() {
        return null;
    }

    public Rectangle getRightChargingBounds() {
        return null;
    }

    public Rectangle getLeftScratchingBounds() {
        return null;
    }

    public Rectangle getRightScratchingBounds() {
        return null;
    }

    public Rectangle getCollisionBounds() {
        return null;
    }

    public Rectangle getLeftBounds() {
        return null;
    }

    public Rectangle getRightBounds() {
        return null;
    }

    //Description: checks whether the current ground tile is out of the camera’s field-of-view. If so, remove it from the handler
    //Parameters: none
    //Return: void
    public void tick() {

        //Removes the object from handler if it is out of the camera's field-of-view for resource efficiency and
        // replace the value in the 2D array to be reused
        if (Camera.outOfFrame(this)){
            map.setObstacles(-y/Map.obstacleSize, (x/Map.obstacleSize) + Map.width/2, value);
            handler.removeObject(this);
        }
    }

    //Description: renders the current ground tile to the screen
    //Parameters: the Graphics object to draw the image
    //Return: void
    public void render(Graphics g) {

        //Renders the ground image at the designated location, with the specified dimensions
        g.drawImage(image, getRelX(), getRelY(), width, height, game);
    }
}
