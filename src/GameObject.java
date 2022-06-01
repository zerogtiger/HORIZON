import org.w3c.dom.css.Rect;

import java.util.*;
import java.awt.*;

public abstract class GameObject {

    protected int x, y;
    protected ID id;
    protected int velX, velY, width, height;
    protected Handler handler;

    public GameObject(int x, int y, ID id, Handler handler) {
        this.x = x;
        this.y = y;
        this.id = id;
        this.handler = handler;
        handler.addObject(this);
    }

    public abstract void tick();
    public abstract void render(Graphics g);
    public abstract Rectangle getBounds();
    public abstract Rectangle getLeftChargingBounds();
    public abstract Rectangle getRightChargingBounds();
    public abstract Rectangle getCollisionBounds();
    public abstract Rectangle getLeftBounds();
    public abstract Rectangle getRightBounds();


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

//    public int getRelX() {
//        return relX;
//    }
//
//    public int getRelY() {
//        return relY;
//    }
//
//    public void setRelX(int relX) {
//        this.relX = relX;
//    }
//
//    public void setRelY(int relY) {
//        this.relY = relY;
//    }
}
