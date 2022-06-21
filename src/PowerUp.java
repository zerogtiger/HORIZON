/*

Description:

Power-up pick-up object of the game. Allows the player to have temporary unlimited charge to reach a greater distance
in the game. Checks whether the speeder is colliding with the power-up's bounds and performs actions accordingly. Further,
verifies whether the power-up is still in the camera frame on each game tick and removes itself from the handler if not
so to ensure efficiency.

*/


import java.awt.*;

public class PowerUp extends GameObject {

    //Dimensions
    private int width;
    private int height;

    //Iterator to be increased every tick
    private int iterator;

    //Other components to be used
    private Handler handler;
    private Map map;
    private Game game;

    //The value that represents this power-up that was assigned in the 2D power-up array
    private int value;

    //Frames for the animation
    private Image[] frames;

    //Constructor
    public PowerUp(String filepath, int value, int x, int y, int width, int height, ID id, Handler handler, Map map, Game game) {

        //Super GameObject
        super(x, y, id, handler);

        //Initialize variables
        this.width = width;
        this.height = height;
        this.map = map;
        this.game = game;
        this.value = value;
        this.handler = handler;

        //Compile the frames of the power-up animation into the array for easy access
        frames = new Image[20];
        for (int i = 0; i < 20; i++) {
            frames[i] = Toolkit.getDefaultToolkit().getImage(filepath + String.format("%04d", i + 1) + ".png");
        }
    }

    //Collision bound of the power-up to be used for collision calculation
    public Rectangle getBounds() {
        return new Rectangle(getRelX(), getRelY(), width, height);
    }

    //Description: updates the speeder collision situation with the current power-up and verifies whether it is still in the camera field-of-view
    //Parameters: none
    //Return: void
    public void tick() {

        //Forward iterator
        iterator = (iterator + 1) % 80;

        //Calculates collision with the player
        game.pickUpCollision(this);

        //Removes the object from handler if it is out of the camera's field-of-view for resource efficiency and
        // replace the value in the 2D array to be reused
        if (Camera.outOfFrame(this)) {
            map.setPowerUp((-y / Map.obstacleSize), (x / Map.obstacleSize) + Map.width / 2, value);
            handler.removeObject(this);
        }
    }

    //Description: renders the current power-up
    //Parameters: the Graphics object to draw the power-up
    //Return: void
    public void render(Graphics g) {
        //Repeatedly draw each frame of the power-up animation at approximately 30 FPS
        int frame = iterator % 40;
        g.drawImage(frames[frame / 2], getRelX(), getRelY(), width, height, game);
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
