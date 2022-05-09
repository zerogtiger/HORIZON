import java.awt.*;
import java.util.Random;

public class Player extends GameObject {

    Random r = new Random();
    private final Game game;

    public Player(int x, int y, ID id, Handler handler, int normalVelY, Game game) {
        super(x, y, id, handler);
        this.game = game;
        velX = 0;
        velY = 0;
    }

    public void tick() {
        if (Stats.debug == 0) {
            if (Stats.KEYPRESS[0][0] && Stats.KEYPRESS[0][1])
                velX = 0;
            else if (Stats.KEYPRESS[0][0])
                velX = -8;
            else if (Stats.KEYPRESS[0][1])
                velX = 8;
            else
                velX = 0;
            if ((Stats.KEYPRESS[0][2] || Stats.KEYPRESS[0][0] && Stats.KEYPRESS[0][1]) && Stats.CHARGE > 0) {
                velY = -18;
                Stats.CHARGE -= 3;
            } else
                velY = -10;
        } else {
            if (Stats.KEYPRESS[1][0] && Stats.KEYPRESS[1][1])
                velX = 0;
            else if (Stats.KEYPRESS[1][0])
                velX = -8;
            else if (Stats.KEYPRESS[1][1])
                velX = 8;
            else
                velX = 0;
            if (Stats.KEYPRESS[1][3] && Stats.KEYPRESS[1][4])
                velY = 0;
            else if (Stats.KEYPRESS[1][3])
                velY = -8;
            else if (Stats.KEYPRESS[1][4])
                velY = 8;
            else
                velY = 0;
        }
        y += velY;
        x += velX;
        Stats.speederDistance += -velY;
        Stats.CHARGE = Game.clamp(Stats.CHARGE, 0, 800);
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

    public Rectangle getLeftBounds() {
        return null;
    }

    public Rectangle getRightBounds() {
        return null;
    }

}
