/*

Description:

An abstract class which all game-related components are extending. Contains location and dimensions of component,
component ID, component velocity, the handler which the game object will add itself to, and abstract methods which
require individual implementations.

*/

import java.util.*;
import java.awt.*;

public abstract class GameObject {

    //x and y-coordinates and dimensions
    protected int x, y, width, height;

    //ID of GameObject
    protected ID id;

    //x and y velocity
    protected int velX, velY;

    //Handler which it belongs to
    protected Handler handler;

    //Constructor
    public GameObject(int x, int y, ID id, Handler handler) {

        //Initialize variables
        this.x = x;
        this.y = y;
        this.id = id;
        this.handler = handler;

        //Add itself to the designated handler
        handler.addObject(this);
    }

    //Abstract methods which require individual implementation
    public abstract void tick();
    public abstract void render(Graphics g);
    public abstract Rectangle getBounds();
    public abstract Rectangle getLeftChargingBounds();
    public abstract Rectangle getRightChargingBounds();
    public abstract Rectangle getLeftScratchingBounds();
    public abstract Rectangle getRightScratchingBounds();
    public abstract Rectangle getCollisionBounds();
    public abstract Rectangle getLeftBounds();
    public abstract Rectangle getRightBounds();


    //Getters and setters
    public void setX (int x) {
        this.x = x;
    }

    public void setY (int y) {
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getRelX() {
        return Camera.getRelX(x);
    }

    public int getRelY() {
        return Camera.getRelY(y);
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public void setID (ID id) {
        this.id = id;
    }

    public ID getId() {
        return id;
    }

    public void setVelX (int velX) {
        this.velX = velX;
    }

    public void setVelY (int velY) {
        this.velY = velY;
    }

    public int getVelX () {
        return velX;
    }

    public int getVelY () {
        return velY;
    }
}
