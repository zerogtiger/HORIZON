import java.awt.*;

public class Obstacle extends GameObject{

    private Map map;
    private Player player;
    public Obstacle(int x, int y, int width, int height, ID id, Handler handler, Map map, Player player) {
        super(x, y, id, handler);
        this.width = width;
        this.height = height;
        this.map = map;
        this.player = player;
    }

    public Rectangle getBounds() {
        return new Rectangle(getRelX(), getRelY(), width, height);
    }

    public Rectangle getChargingBounds() {
        return new Rectangle(getRelX()-50, getRelY(), width+100, height);
    }

    public Rectangle getCollisionBounds() {
        return new Rectangle(getRelX(), getRelY()+height, width, Math.abs(player.velY));
    }

    public Rectangle getLeftBounds() {
        return new Rectangle(getRelX(), getRelY(), width/2, height);
    }

    public Rectangle getRightBounds() {
        return new Rectangle(getRelX()+width/2, getRelY(), width/2, height);
    }

    public void tick() {
        Game.collision(this);
        if (Camera.outOfFrame(this)){
            map.setObstacles((-y/Map.obstacleSize), (x/Map.obstacleSize) + Map.width/2, 1);
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
