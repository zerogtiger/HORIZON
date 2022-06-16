import java.awt.*;

public class Ground extends GameObject{
    private Map map;
    private Image image;
    private int value;

    public Ground(Image image, int value, int x, int y, int width, int height, ID id, Handler handler, Map map) {
        super(x, y, id, handler);
        this.width = width;
        this.height = height;
        this.map = map;
        this.image = image;
        this.value = value;
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

    public Rectangle getLeftScratchingBounds() {
        return null;
    }

    public Rectangle getRightScratchingBounds() {
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
            map.setObstacles(-y/Map.obstacleSize, (x/Map.obstacleSize) + Map.width/2, value);
            handler.removeObject(this);
        }
    }

    public void render(Graphics g) {
        g.drawImage(image, getRelX(), getRelY(), width, height, null);
    }
}
