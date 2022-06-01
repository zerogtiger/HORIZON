import java.awt.*;
import java.util.Random;

public class Player extends GameObject {

    private boolean isChargingLeft = false, isChargingRight = false, isScratchingLeft = false, isScratchingRight = false;
    private final Game game;
    private final Image[] pics = {Toolkit.getDefaultToolkit().getImage("pics/SG.png"),
            Toolkit.getDefaultToolkit().getImage("pics/SGL1.png"),
            Toolkit.getDefaultToolkit().getImage("pics/SGR1.png")};

    public Player(int x, int y, ID id, Handler handler, int normalVelY, Game game) {
        super(x, y, id, handler);
        this.game = game;
        velX = 0;
        velY = 0;
    }

    public void tick() {
        if (Pursuer.distance <= 0)
            Game.gameState = Game.state.GameOver;
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
        if (isChargingLeft)
            Stats.CHARGE += 1;
        if (isChargingRight)
            Stats.CHARGE += 1;

        y += velY;
        x += velX;
        Stats.speederDistance += -velY;
        Stats.CHARGE = Game.clamp(Stats.CHARGE, 0, 800);
        isChargingLeft = false;
        isChargingRight = false;
    }

    public void render(Graphics g) {
        g.setColor(Color.red);
        if (velX == 0)
            g.drawImage(pics[0], getRelX(), getRelY(), 32, 48, game);
        else if (velX < 0)
            g.drawImage(pics[1], getRelX(), getRelY(), 32, 48, game);
        else if (velX > 0)
            g.drawImage(pics[2], getRelX(), getRelY(), 32, 48, game);
        if (isChargingLeft)
            g.fillRect(getRelX() - 32, getRelY() + 32, 48, 5);
        if (isChargingRight)
            g.fillRect(getRelX() + 16, getRelY() + 32, 48, 5);
//        g.fillRect(getRelX(), getRelY(), 32, 48);
//        Graphics2D g2d = (Graphics2D) g;
//        g.setColor(Color.CYAN);
//        g2d.draw(getBounds());
    }


    public boolean getIsChargingLeft() {
        return isChargingLeft;
    }

    public boolean getIsChargingRight() {
        return isChargingRight;
    }

    public boolean getIsScratchingLeft() {
        return isScratchingLeft;
    }

    public boolean getIsScratchingRight() {
        return isScratchingRight;
    }

    public void setChargingLeft(boolean chargingLeft) {
        isChargingLeft = chargingLeft;
    }

    public void setChargingRight(boolean chargingRight) {
        isChargingRight = chargingRight;
    }

    public void setScratchingLeft(boolean scratchingLeft) {
        isScratchingLeft = scratchingLeft;
    }

    public void setScratchingRight(boolean scratchingRight) {
        isScratchingRight = scratchingRight;
    }

    public Rectangle getBounds() {
        return new Rectangle(getRelX(), getRelY(), 32, 48);
    }

    public Rectangle getLeftChargingBounds() {
        return null;
    }

    public Rectangle getRightChargingBounds() {
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
