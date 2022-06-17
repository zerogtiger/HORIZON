import java.awt.*;
import java.util.*;

public class AirBorneObject extends GameObject {

    private int width;
    private int height;
    private final int velX;
    private final int velY;
    private int iterator;
    private Handler handler;
    private Player player;
    private Map map;
    private Image[] frames;

    public AirBorneObject(String filepath, ID id, int x, int y, int width, int height, int velX, int velY, Handler handler, Player player, Map map) {
        super(x, y, id, handler);
        this.width = width;
        this.height = height;
        this.velX = velX;
        this.velY = velY;
        this.handler = handler;
        this.player = player;
        this.map = map;
        handler.addObject(this);
        iterator = 0;
        frames = new Image[30];
        for (int i = 0; i < 29; i++) {
            frames[i] = Toolkit.getDefaultToolkit().getImage(filepath + String.format("%04d", i+1) + ".png");
        }
    }


    public void tick() {
        iterator = (iterator + 1) % 720;
        x += velX - player.getVelX()/4;
        y += velY - player.getVelY()/5;;
        if (y > Camera.getRelY() + Game.HEIGHT)
            handler.removeObject(this);
    }

    public void render(Graphics g) {
        int frame = iterator % 60;
        g.drawImage(frames[frame / 2], getRelX(), getRelY(), width, height, null);
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
}
