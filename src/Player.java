import java.util.*;
import java.awt.*;

public class Player extends GameObject{

    public static int relX, relY;

    public Player (int x, int y, ID id, Handler handler) {
        super(x, y, id, handler);
        relX = 0; relY = 3;
    }
    public void tick() {

        collision();
//        relX = (Stats.KEYPRESS[0]? -8: relX);
//        relX = (Stats.KEYPRESS[1]? 8: relX);
//        relX = (!Stats.KEYPRESS[0] && !Stats.KEYPRESS[1]? 0: relX);
//        relX = (Stats.KEYPRESS[0] && Stats.KEYPRESS[1]? 0: relX);
//        relX = (Stats.KEYPRESS[3]? 0: relX);
        if (Stats.KEYPRESS[0] && Stats.KEYPRESS[1])
            relX = 0;
        else if (Stats.KEYPRESS[0])
            relX = -1;
        else if (Stats.KEYPRESS[1])
            relX = 1;
        else
            relX = 0;

    }

    public void collision() {
        for (GameObject tempObject: handler.object) {
            if (tempObject.id == ID.Obstacle) {
                if (getBounds().intersects(tempObject.getCollisionBounds())) {
                    System.out.println("Collided");
//                    System.exit(0);
                }
                else if (getBounds().intersects(tempObject.getChargingBounds()))
                    Stats.CHARGE += 1.5;
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
        g.fillRect(x, y, 32, 48);
        Graphics2D g2d = (Graphics2D) g;
        g.setColor(Color.CYAN);
        g2d.draw(getBounds());
    }


    public Rectangle getBounds() {
        return new Rectangle(x, y, 32, 48);
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
