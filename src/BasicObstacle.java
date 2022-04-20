import java.awt.*;
import java.util.*;
import java.io.*;

public class BasicObstacle extends GameObject{

    private Random r = new Random();

    public BasicObstacle(int x, int y, int width, int height, ID id, Handler handler) {
        super(x, y, id, handler);
        this.width = width;
        this.height = height;
        velY = Player.relY;
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }

    public Rectangle getChargingBounds() {
        return new Rectangle(x-50, y, width+100, height);
    }

    public Rectangle getCollisionBounds() {
        return new Rectangle(x+2, y+height-5, width-4, 5);
    }

    public Rectangle getLeftBounds() {
        return new Rectangle(x, y, width/2, height);
    }

    public Rectangle getRightBounds() {
        return new Rectangle(x+width/2, y, width/2, height);
    }

    public void tick() {
        velX = -Player.relX;
        velY = Player.relY;
        x += velX;
        y += velY;
//        if (x < -width-1000) {
//            new BasicObstacle(r.nextInt(300)-300-width, r.nextInt(-height, Game.HEIGHT), r.nextInt(50, 200),
//                    r.nextInt(50, 200), ID.Obstacle, handler);
//            handler.removeObject(this);
//        }
//        else if (x > Game.WIDTH+1000) {
//            new BasicObstacle(r.nextInt(300)+Game.WIDTH, r.nextInt(-height, Game.HEIGHT), r.nextInt(50, 200),
//                    r.nextInt(50, 200), ID.Obstacle, handler);
//            handler.removeObject(this);
//        }
        if (y > Game.HEIGHT){
            new BasicObstacle(
                    r.nextInt(-1000, Game.WIDTH+1000),
                    r.nextInt(-300, 300)-600,
                    r.nextInt(50, 200),
                    r.nextInt(200, 400),
                    ID.Obstacle, handler);
            handler.removeObject(this);
        }

    }



    public void render(Graphics g) {
        g.setColor(Color.lightGray);
        g.fillRect(x, y, width, height);
        Graphics2D g2d = (Graphics2D) g;
        g.setColor(Color.yellow);
        g2d.draw(getChargingBounds());
        g.setColor(Color.green);
        g2d.draw(getCollisionBounds());
        g.setColor(Color.cyan);
        g2d.draw(getLeftBounds());
        g2d.draw(getRightBounds());

    }
}
