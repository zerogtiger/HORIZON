/*

Description:

A class for airborne objects in the game, which are currently only drones with a dynamic propeller to distract the
player. Takes on its own velocities and dimensions and are independent of all other objects in the game. The "airborne"
effect is illustrated through the amplified and opposite movement of the airborne objects relative to the player. The
object removes itself from the handler once the camera moves past the object in the y-direction and is not designed to
be reversible.

*/

import java.awt.*;

public class AirborneObject extends GameObject {

    //Dimensions
    private int width;
    private int height;

    //Velocities
    private final int velX;
    private final int velY;

    //Iterator to be increased every tick
    private int iterator;

    //Other components to be used
    private Handler handler;
    private Player player;
    private Game game;

    //Frames for the animation
    private Image[] frames;

    //Constructor
    public AirborneObject(String filepath, ID id, int x, int y, int width, int height, int velX, int velY, Handler handler, Player player, Game game) {

        //Super GameObject
        super(x, y, id, handler);

        //Initialize variables
        this.width = width;
        this.height = height;
        this.velX = velX;
        this.velY = velY;
        this.handler = handler;
        this.player = player;
        this.game = game;

        //Adding itself to the handler
        handler.addObject(this);

        //Reset iterator
        iterator = 0;

        //Compile the frames of the drone animation into the array for easy access
        frames = new Image[30];
        for (int i = 0; i < 29; i++) {
            frames[i] = Toolkit.getDefaultToolkit().getImage(filepath + String.format("%04d", i + 1) + ".png");
        }
    }

    //Description: updates the object by updating the iterator, position, and verifies whether it is still in frame
    //Parameters: none
    //Return: void
    public void tick() {

        //Increase iterator
        iterator = (iterator + 1) % 720;

        //Update x and y-coordinates
        x += velX - player.getVelX() / 4;
        y += velY - player.getVelY() / 5;

        //Removes the object from handler if the camera has moved beyond it
        if (y > Camera.getRelY() + Game.HEIGHT)
            handler.removeObject(this);
    }

    //Description: renders the image of the airborne object at the designated location with specified dimensions
    //Parameters: the Graphics object to draw the image
    //Return: void
    public void render(Graphics g) {

        //Repeatedly draw each frame of the drone at approximately 30 FPS
        int frame = iterator % 60;
        g.drawImage(frames[frame / 2], getRelX(), getRelY(), width, height, game);
    }

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
}
