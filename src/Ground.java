import java.awt.*;

public class Ground extends GameObject{
    private final Color color;
    private Map map;

    public Ground(int x, int y, int width, int height, ID id, Handler handler, Map map, Color color) {
        super(x, y, id, handler);
        this.width = width;
        this.height = height;
        this.color = color;
        this.map = map;
    }

    public Rectangle getBounds() {
        return null;
    }

    public Rectangle getLeftChargingBounds() {
        return null;
    }

    public Rectangle getRightChargingBounds() {
        return null;
    }

    public Rectangle getChargingBounds() {
        return null;
    }

    public Rectangle getCollisionBounds() {
        return null;
    }

    public Rectangle getLeftBounds() {
        return null;
    }

    public Rectangle getRightBounds() {
        return null;
    }

    public void tick() {
        if (Camera.outOfFrame(this)){
            map.setObstacles(-y/Map.obstacleSize, (x/Map.obstacleSize) + Map.width/2, 2);
            handler.removeObject(this);
        }
    }

    public void render(Graphics g) {
        g.setColor(color);
        g.fillRect(getRelX()+5, getRelY()+5, width-10, height-10);
    }
}
