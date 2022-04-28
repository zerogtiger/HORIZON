import java.awt.*;
import java.util.*;
import java.io.*;

public class BasicObstacle extends GameObject{

    public BasicObstacle(int x, int y, int width, int height, ID id, Handler handler) {
        super(x, y, id, handler);
        this.width = width;
        this.height = height;
    }

    public Rectangle getBounds() {
        return new Rectangle(getRelX(), getRelY(), width, height);
    }

    public Rectangle getChargingBounds() {
        return new Rectangle(getRelX()-50, getRelY(), width+100, height);
    }

    public Rectangle getCollisionBounds() {
        return new Rectangle(getRelX()+2, getRelY()+height-5, width-4, 5);
    }

    public Rectangle getLeftBounds() {
        return new Rectangle(getRelX(), getRelY(), width/2, height);
    }

    public Rectangle getRightBounds() {
        return new Rectangle(getRelX()+width/2, getRelY(), width/2, height);
    }

    public void tick() {
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
        if (Camera.outOfFrame(this)){
//            new BasicObstacle(
//                    r.nextInt(-1000, Game.WIDTH+1000),
//                    r.nextInt(-300, 300)-600,
//                    r.nextInt(50, 200),
//                    r.nextInt(200, 400),
//                    ID.Obstacle, handler);
//            System.out.println("x: " + x);
//            System.out.println("y: " + y);
            Stats.obstacles[-y/120][(x/75) + 150] = true;
            handler.removeObject(this);
        }

    }



    public void render(Graphics g) {
        g.setColor(Color.lightGray);
        g.fillRect(getRelX(), getRelY(), width, height);
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
