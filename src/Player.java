import java.util.*;
import java.awt.*;

public class Player extends GameObject{

    public Player (int x, int y, ID id, Handler handler) {
        super(x, y, id, handler);
        velX = 0; velY = 0;
    }
    public void tick() {
        collision();
        if (Stats.KEYPRESS[0] && Stats.KEYPRESS[1])
            velX = 0;
        else if (Stats.KEYPRESS[0])
            velX = -8;
        else if (Stats.KEYPRESS[1])
            velX = 8;
        else
            velX = 0;
        if (Stats.KEYPRESS[2] && Stats.CHARGE>0) {
            velY = -18;
            Stats.CHARGE-=1.5;
        }
        else
            velY = -10;
        y += velY;
        x += velX;
        Stats.speederDistance += -velY;

        for (int i = 1; i <= 150; i++) {
            for (int j = 1; j <= 300; j++) {
                if (Stats.obstacles[i][j] && !Camera.outOfFrame((j-150)*75, -i*120, 75, 120)) {
                    new BasicObstacle((j-150)*75, -i*120, 75, 120, ID.Obstacle, handler);
                    Stats.obstacles[i][j] = false;
                }
            }
        }
    }

    public void collision() {
        for (GameObject tempObject: handler.object) {
            if (tempObject.id == ID.Obstacle) {
                if (getBounds().intersects(tempObject.getCollisionBounds())) {
//                    System.exit(0);
                }
                else if (getBounds().intersects(tempObject.getChargingBounds()))
                    Stats.CHARGE += 1;
                if (getBounds().intersects(tempObject.getLeftBounds())) {
                    Stats.KEYPRESS[1] = false;
                }
                else if (getBounds().intersects(tempObject.getRightBounds())) {
                    Stats.KEYPRESS[0] = false;
                }
            }
        }
    }

    public void render(Graphics g) {
        g.setColor(Color.red);
        g.fillRect(getRelX(), getRelY(), 32, 48);
        Graphics2D g2d = (Graphics2D) g;
        g.setColor(Color.CYAN);
        g2d.draw(getBounds());
    }


    public Rectangle getBounds() {
        return new Rectangle(getRelX(), getRelY(), 32, 48);
    }

    public Rectangle getChargingBounds() {
        return null;
    }

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

}