import java.awt.*;
import java.util.Random;

public class Player extends GameObject {

    private boolean isChargingLeft = false, isChargingRight = false, isScratchingLeft = false, isScratchingRight = false, isBumped = false;
    private final Game game;
    private final Image[] pics = {Toolkit.getDefaultToolkit().getImage("appdata/pics/SG.png"),
            Toolkit.getDefaultToolkit().getImage("appdata/pics/SGL1.png"),
            Toolkit.getDefaultToolkit().getImage("appdata/pics/SGR1.png")};
    private int iterator[];
    private int iteratorValue = 3;

    public Player(int x, int y, ID id, Handler handler, int normalVelY, Game game) {
        super(x, y, id, handler);
        iterator = new int[]{0, 0}; //Y, X
        this.game = game;
        velX = 0;
        velY = 0;
    }

    public void tick() {
        iterator[0] = (iterator[0] + 1) % 360;
        iterator[1] = (iterator[1] + 1) % 360;
        if (Pursuer.distance <= 0) {
            Game.gameOver(Stats.speederDistance, game.getSeed(), 1);
        }
        if (Stats.debug == 0) {
            if (isScratchingLeft || isScratchingRight)
                velY = Game.clamp(velY + (iterator[0] % 3 == 0 ? 1 : 0), -25, -3);
            else if ((Stats.getKeyPress()[0][2] || Stats.getKeyPress()[0][0] && Stats.getKeyPress()[0][1]) && Stats.CHARGE > 0) {
                velY = Game.clamp(velY - (iterator[0] % 3 == 0 ? 1 : 0), -25, 0);
                Stats.CHARGE -= 3;
            } else {
                if (velY < -10) {
                    velY = Game.clamp(velY + (iterator[0] % 4 == 0 ? 1 : 0), -25, -10);
                } else {
                    velY = Game.clamp(velY - (iterator[0] % 5 == 0 ? 1 : 0), -10, 0);
                }
            }
            if (Stats.getKeyPress()[0][0] && Stats.getKeyPress()[0][1]) {

            } else if (Stats.getKeyPress()[0][0]) {
                velX = Game.clamp(velX - (iterator[1] % 3 == 0 ? (velX > -7 ? 2 : 1) : 0), -10, 10);
            } else if (Stats.getKeyPress()[0][1]) {
                velX = Game.clamp(velX + (iterator[1] % 3 == 0 ? (velX < 7 ? 2 : 1) : 0), -10, 10);
            } else {
                if (velX > 0) {
                    velX = Game.clamp(velX - (iterator[1] % 3 == 0 ? (velX < 7 ? 2 : 1) : 0), 0, 10);
                } else if (velX < 0) {
                    velX = Game.clamp(velX + (iterator[1] % 3 == 1 ? (velX > -7 ? 2 : 1) : 0), -10, 0);
                }
            }
        } else {
            if (Stats.getKeyPress()[1][3] && Stats.getKeyPress()[1][4])
                velY = 0;
            else if (Stats.getKeyPress()[1][3])
                velY = -8;
            else if (Stats.getKeyPress()[1][4])
                velY = 8;
            else
                velY = 0;
            if (Stats.getKeyPress()[1][0]) {
                velX = Game.clamp(velX - (iterator[1] % 3 == 0 ? (velX > -7 ? 2 : 1) : 0), -10, 10);
            } else if (Stats.getKeyPress()[1][1]) {
                velX = Game.clamp(velX + (iterator[1] % 3 == 0 ? (velX < 7 ? 2 : 1) : 0), -10, 10);
            } else {
                if (velX > 0) {
                    velX = Game.clamp(velX - (iterator[1] % 3 == 0 ? (velX < 7 ? 2 : 1) : 0), 0, 10);
                } else if (velX < 0) {
                    velX = Game.clamp(velX + (iterator[1] % 3 == 1 ? (velX > -7 ? 2 : 1) : 0), -10, 0);
                }
            }

        }
        if (isChargingLeft && velY < -8)
            Stats.CHARGE += 2;
        if (isChargingRight && velY < -8)
            Stats.CHARGE += 2;


        y += velY;
        x += velX;
        Stats.speederDistance += -velY;
        Stats.CHARGE = Game.clamp(Stats.CHARGE, 0, 800);
        isChargingLeft = false;
        isChargingRight = false;
        isScratchingLeft = false;
        isScratchingRight = false;
    }

    public void render(Graphics g) {
        g.setColor(Color.red);
        if (velX == 0)
            g.drawImage(pics[0], getRelX(), getRelY(), 32, 48, game);
        else if (velX < 0)
            g.drawImage(pics[1], getRelX(), getRelY(), 32, 48, game);
        else if (velX > 0)
            g.drawImage(pics[2], getRelX(), getRelY(), 32, 48, game);
        if (isChargingLeft && velY < -8)
            g.fillRect(getRelX() - 32, getRelY() + 32, 48, 5);
        if (isChargingRight && velY < -8)
            g.fillRect(getRelX() + 16, getRelY() + 32, 48, 5);

//        g.fillRect(getRelX(), getRelY(), 32, 48);
        Graphics2D g2d = (Graphics2D) g;
        g.setColor(Color.CYAN);
//        g2d.draw(getLeftChargingBounds());
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

    public boolean getBumped() {
        return isBumped;
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

    public void setBumped(boolean bumped) {
        isBumped = bumped;
    }

    public Rectangle getBounds() {
        return new Rectangle(getRelX(), getRelY(), 32, 48);
    }

    public Rectangle getLeftChargingBounds() {
        return new Rectangle(getRelX(), getRelY() + 32, 32, 2);
    }

    public Rectangle getRightChargingBounds() {
        return new Rectangle(getRelX(), getRelY() + 32, 32, 2);
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

    public int[] getIterator() {
        return iterator;
    }

    public void setIterator(int index, int value) {
        iterator[index] = value;
    }
}
