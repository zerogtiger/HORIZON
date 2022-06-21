/*

Description:

Obstacle objects for the game. Includes location, dimensions, and the image which should be rendered for the obstacle.
Checks whether the speeder is colliding with the object's various bounds and performs actions accordingly. Further,
verifies whether the obstacle is still in the camera frame on each game tick and removes itself from the handler if not
so to ensure efficiency.

*/


import java.awt.*;

public class Obstacle extends GameObject {

    //Game components to be referenced to
    private Map map;
    private Player player;
    private Game game;

    //Image to be rendered as the obstacle
    private Image image;

    //The value that represents this obstacle that was assigned in the 2D obstacles array
    private int value;

    //Constructor
    public Obstacle(Image image, int value, int x, int y, int width, int height, ID id, Handler handler, Map map, Player player, Game game) {

        //Super GameObject
        super(x, y, id, handler);

        //Initialize variables
        this.image = image;
        this.width = width;
        this.height = height;
        this.map = map;
        this.game = game;
        this.player = player;
        this.value = value;
    }

    //Various bounds of the obstacle to be used for collision calculation

    public Rectangle getBounds() {
        return new Rectangle(getRelX(), getRelY(), width, height);
    }

    public Rectangle getLeftChargingBounds() {
        return new Rectangle(getRelX() - 32, getRelY(), 32 + width / 2, height);
    }

    public Rectangle getRightChargingBounds() {
        return new Rectangle(getRelX() + width / 2, getRelY(), width / 2 + 32, height);
    }

    public Rectangle getLeftScratchingBounds() {
        return new Rectangle(getRelX() - 1, getRelY(), 1 + width / 2, height);
    }

    public Rectangle getRightScratchingBounds() {
        return new Rectangle(getRelX() + width / 2, getRelY(), width / 2 + 1, height);
    }

    public Rectangle getCollisionBounds() {
        return new Rectangle(getRelX(), getRelY() + height, width, Math.abs(player.velY));
    }

    public Rectangle getLeftBounds() {
        return new Rectangle(getRelX(), getRelY(), width / 2, height);
    }

    public Rectangle getRightBounds() {
        return new Rectangle(getRelX() + width / 2, getRelY(), width / 2, height);
    }

    //Description: updates the speeder collision situation with the current obstacle and verifies whether it is still in the camera field-of-view
    //Parameters: none
    //Return: void
    public void tick() {

        //Calculates collision with the player
        game.collision(this);

        //Removes the object from handler if it is out of the camera's field-of-view for resource efficiency and
        // replace the value in the 2D array to be reused
        if (Camera.outOfFrame(this)) {
            map.setObstacles((-y / Map.obstacleSize), (x / Map.obstacleSize) + Map.width / 2, value);
            handler.removeObject(this);
        }

    }

    //Description: renders the current obstacle
    //Parameters: the Graphics object to draw the obstacle
    //Return: void
    public void render(Graphics g) {
//        g.setColor(Color.lightGray);
//        g.fillRect(getRelX(), getRelY(), width, height);
//        Graphics2D g2d = (Graphics2D) g;
//        g.setColor(Color.yellow);
//        g2d.draw(getLeftChargingBounds());
//        g2d.draw(getRightChargingBounds());
//        g.setColor(Color.green);
//        g2d.draw(getCollisionBounds());
//        g.setColor(Color.cyan);
//        g2d.draw(getLeftScratchingBounds());
//        g2d.draw(getRightScratchingBounds());

        //Renders the obstacle image at the designated location, with the specified dimensions
        g.drawImage(image, getRelX(), getRelY(), width, height, game);

    }
}
