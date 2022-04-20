import java.awt.*;
import java.util.*;
import java.io.*;

public class ObstacleFalloff extends GameObject{

    private int width, height;

    public ObstacleFalloff(int x, int y, int width, int height, ID id, Handler handler) {
        super(x-50, y, id, handler);
        this.width = width+100;
        this.height = height;
        velY = Player.relY;
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }

    @Override
    public Rectangle getChargingBounds() {
        return null;
    }

    @Override
    public Rectangle getCollisionBounds() {
        return null;
    }

    @Override
    public Rectangle getLeftBounds() {
        return null;
    }

    @Override
    public Rectangle getRightBounds() {
        return null;
    }

    public void tick() {
        x += velX;
        y += velY;
    }


    public void render(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g.setColor(Color.green);
        g2d.draw(getBounds());
    }
}
