import java.util.*;
import java.awt.*;

public class Player extends GameObject{

    public static int relX, relY;

    public Player (int x, int y, ID id, Handler handler) {
        super(x, y, id, handler);
        relX = 0; relY = 7;
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

    public void tick() {
        x += velX;
        y += velY;

        collision();
        relX = (KeyInput.KEYPRESS[0]? -8: relX);
        relX = (KeyInput.KEYPRESS[1]? 8: relX);
        relX = (!KeyInput.KEYPRESS[0] && !KeyInput.KEYPRESS[1]? 0: relX);
        relX = (KeyInput.KEYPRESS[0] && KeyInput.KEYPRESS[1]? 0: relX);
    }

    public void collision() {
        for (GameObject tempObject: handler.object) {
            if (tempObject.id == ID.Obstacle) {
                if (getBounds().intersects(tempObject.getCollisionBounds())) {
                    System.out.println("Collided");
                    System.exit(0);
                }
                else if (getBounds().intersects(tempObject.getChargingBounds()))
                    Stats.CHARGE += 1.5;
                if (getBounds().intersects(tempObject.getBounds())){
                    relX = 0;
                    x = Game.reverseClamp(x, tempObject.x-32, tempObject.x+tempObject.width);
                }
            }
        }
    }

    public void render(Graphics g) {
        g.setColor(Color.red);
        g.fillRect(x, y, 32, 48);
    }


}
