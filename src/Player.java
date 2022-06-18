import java.awt.*;
import java.util.Random;

public class Player extends GameObject {

    private boolean isChargingLeft = false, isChargingRight = false, isScratchingLeft = false, isScratchingRight = false, isBumped = false;
    private final Game game;
    private final Image[] speeder = {Toolkit.getDefaultToolkit().getImage("appdata/pics/SG.png"),
            Toolkit.getDefaultToolkit().getImage("appdata/pics/SGL1.png"),
            Toolkit.getDefaultToolkit().getImage("appdata/pics/SGL2.png"),
            Toolkit.getDefaultToolkit().getImage("appdata/pics/SGR1.png"),
            Toolkit.getDefaultToolkit().getImage("appdata/pics/SGR2.png")},
            soundBarrier = new Image[9],
            scratchingLeft = new Image[8], scratchingRight = new Image[8], chargingLeft = new Image[9], chargingRight = new Image[9];
    private int iterator;
    private int iteratorValue = 3;

    public Player(int x, int y, ID id, Handler handler, int normalVelY, Game game) {
        super(x, y, id, handler);
        iterator = 0;
        this.game = game;
        velX = 0;
        velY = 0;
        for (int i = 0; i < 9; i++) {
            soundBarrier[i] = Toolkit.getDefaultToolkit().getImage("appdata/pics/soundBarrier/" + String.format("%04d", i + 1) + ".png");
        }
        for (int i = 0; i < 8; i++) {
            scratchingLeft[i] = Toolkit.getDefaultToolkit().getImage("appdata/pics/scratching/left/" + String.format("%04d", i + 8) + ".png");
        }
        for (int i = 0; i < 8; i++) {
            scratchingRight[i] = Toolkit.getDefaultToolkit().getImage("appdata/pics/scratching/right/" + String.format("%04d", i + 8) + ".png");
        }
        for (int i = 0; i < 9; i++) {
            chargingLeft[i] = Toolkit.getDefaultToolkit().getImage("appdata/pics/charging/left/" + String.format("%04d", i + 1) + ".png");
        }
        for (int i = 0; i < 9; i++) {
            chargingRight[i] = Toolkit.getDefaultToolkit().getImage("appdata/pics/charging/right/" + String.format("%04d", i + 1) + ".png");
        }
    }

    public void tick() {
        iterator = (iterator + 1) % 360;
        if (Pursuer.distance <= 0) {
            Game.gameOver(Stats.speederDistance, game.getSeed(), 1);
        }
        if (Stats.debug == 0) {
            if (isScratchingLeft || isScratchingRight)
                velY = Game.clamp(velY + (iterator % 3 == 0 ? 1 : 0), -25, -3);
            else if ((Stats.getKeyPress()[0][2] || Stats.getKeyPress()[0][0] && Stats.getKeyPress()[0][1]) && Stats.CHARGE > 0) {
                velY = Game.clamp(velY - (iterator % 3 == 0 ? 1 : 0), -25, 0);
                Stats.CHARGE -= 3;
            } else {
                if (velY < -10) {
                    velY = Game.clamp(velY + (iterator % 4 == 0 ? 1 : 0), -25, -10);
                } else {
                    velY = Game.clamp(velY - (iterator % 5 == 0 ? 1 : 0), -10, 0);
                }
            }
            if (Stats.getKeyPress()[0][0] && !Stats.getKeyPress()[0][1]) {
                velX = Game.clamp(velX - (iterator % 3 == 0 ? (velX > -7 ? 2 : 1) : 0), -10, 10);
            } else if (Stats.getKeyPress()[0][1] && !Stats.getKeyPress()[0][0]) {
                velX = Game.clamp(velX + (iterator % 3 == 0 ? (velX < 7 ? 2 : 1) : 0), -10, 10);
            } else {
                if (velX > 0) {
                    velX = Game.clamp(velX - (iterator % 3 == 0 ? (velX < 7 ? 2 : 1) : 0), 0, 10);
                } else if (velX < 0) {
                    velX = Game.clamp(velX + (iterator % 3 == 1 ? (velX > -7 ? 2 : 1) : 0), -10, 0);
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
                velX = Game.clamp(velX - (iterator % 3 == 0 ? (velX > -7 ? 2 : 1) : 0), -10, 10);
            } else if (Stats.getKeyPress()[1][1]) {
                velX = Game.clamp(velX + (iterator % 3 == 0 ? (velX < 7 ? 2 : 1) : 0), -10, 10);
            } else {
                if (velX > 0) {
                    velX = Game.clamp(velX - (iterator % 3 == 0 ? (velX < 7 ? 2 : 1) : 0), 0, 10);
                } else if (velX < 0) {
                    velX = Game.clamp(velX + (iterator % 3 == 1 ? (velX > -7 ? 2 : 1) : 0), -10, 0);
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
        if ((Stats.getKeyPress()[0][0] && Stats.getKeyPress()[0][1]) || (!Stats.getKeyPress()[0][0] && !Stats.getKeyPress()[0][1]))
            g.drawImage(speeder[0], getRelX(), getRelY(), 32, 48, game);
        else if (Stats.getKeyPress()[0][0] && velX > -7)
            g.drawImage(speeder[1], getRelX(), getRelY(), 32, 48, game);
        else if (Stats.getKeyPress()[0][0])
            g.drawImage(speeder[2], getRelX(), getRelY(), 32, 48, game);
        else if (Stats.getKeyPress()[0][1] && velX < 7)
            g.drawImage(speeder[3], getRelX(), getRelY(), 32, 48, game);
        else if (Stats.getKeyPress()[0][1])
            g.drawImage(speeder[4], getRelX(), getRelY(), 32, 48, game);
        if (isChargingLeft && velY < -8) {
            int index = iterator % 18 / 2;
            g.drawImage(chargingLeft[index], getRelX() - 32, getRelY() + 32, 48, 10, game);
        }
//            g.fillRect(getRelX() - 32, getRelY() + 32, 48, 5);
        if (isChargingRight && velY < -8) {
            int index = iterator % 18 / 2;
            g.drawImage(chargingRight[index], getRelX() + 16, getRelY() + 32, 48, 10, game);
        }

//        g.fillRect(getRelX(), getRelY(), 32, 48);
        g.setColor(Color.CYAN);
//        g2d.draw(getLeftChargingBounds());
        if (isScratchingLeft) {
            int index = iterator % 16 / 2;
            g.drawImage(scratchingLeft[index], getRelX(), getRelY() + 37, 10, 10, game);
        }
        if (isScratchingRight) {
            int index = iterator % 16 / 2;
            g.drawImage(scratchingRight[index], getRelX() + 32 - 15, getRelY() + 37, 10, 10, game);
        }
        Graphics2D g2d = (Graphics2D) g;
        int index = (iterator%18)/2;
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (float) (Math.min(0, velY + 11) / -14.0)));
        g2d.drawImage(soundBarrier[index], getRelX() - 8, getRelY() + 5, 48, 40, game);
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1));
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

    public int getIterator() {
        return iterator;
    }

    public void setIterator(int value) {
        iterator = value;
    }
}
