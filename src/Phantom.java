/*

Description:

The phantom speeder which displays the course taken by the player in the last run, allowing the player to reference its
previous path and adjust for a higher score in their next run. Responsible for rendering the phantom speeder in various
postures and depending on its velocity and states and renders the visual effects.

BUG: phantom speeder disappears at the end of a zone due to the zone transition mechanism of player teleportation.

*/


import java.awt.*;
import java.util.*;

public class Phantom extends GameObject {

    //Queue to store the state of the speeder in the last run
    private Queue<Tuple> lastState;

    //Booleans to replicate the state of the speeder
    private boolean isChargingLeft = false, isChargingRight = false, isScratchingLeft = false, isScratchingRight = false;

    //Game which the phantom belongs to
    private final Game game;

    //Images of phantom postures
    private final Image[] speeder = {Toolkit.getDefaultToolkit().getImage("appdata/images/phantom/PG.png"),
            Toolkit.getDefaultToolkit().getImage("appdata/images/phantom/PGL1.png"),
            Toolkit.getDefaultToolkit().getImage("appdata/images/phantom/PGL2.png"),
            Toolkit.getDefaultToolkit().getImage("appdata/images/phantom/PGR1.png"),
            Toolkit.getDefaultToolkit().getImage("appdata/images/phantom/PGR2.png")},

    //Images of effects
    soundBarrier = new Image[9],
            scratchingLeft = new Image[8],
            scratchingRight = new Image[8],
            chargingLeft = new Image[9],
            chargingRight = new Image[9];

    //iterator to assist in displaying animations
    private int iterator;

    //Constructor
    public Phantom(int x, int y, ID id, Handler handler, Queue<Tuple> lastState, Game game) {

        //Super GameObject
        super(x, y, id, handler);

        //Initialize variables
        iterator = 0;
        this.lastState = lastState;
        this.game = game;

        //Load last speeder states. Approximately half a second ahead of the true player
        for (int i = 1; i <= 30; i++) {
            if (lastState.isEmpty()) {
                handler.removeObject(this);
            } else {

                //Obtain player at current tick
                Tuple lastSpeederState = lastState.poll();

                //Phantom position
                x = lastSpeederState.x;
                y = lastSpeederState.y;

                //Phantom velocity
                velX = lastSpeederState.velX;
                velY = lastSpeederState.velY;

                //Phantom state
                isChargingLeft = lastSpeederState.isChargingLeft;
                isChargingRight = lastSpeederState.isChargingRight;
                isScratchingLeft = lastSpeederState.isScratchingLeft;
                isScratchingRight = lastSpeederState.isScratchingRight;
            }
        }

        //Compile images of effects
        for (int i = 0; i < 9; i++) {
            soundBarrier[i] = Toolkit.getDefaultToolkit().getImage("appdata/images/phantom/soundBarrier/" + String.format("%04d", i + 1) + ".png");
        }
        for (int i = 0; i < 8; i++) {
            scratchingLeft[i] = Toolkit.getDefaultToolkit().getImage("appdata/images/phantom/scratching/left/" + String.format("%04d", i + 8) + ".png");
        }
        for (int i = 0; i < 8; i++) {
            scratchingRight[i] = Toolkit.getDefaultToolkit().getImage("appdata/images/phantom/scratching/right/" + String.format("%04d", i + 8) + ".png");
        }
        for (int i = 0; i < 9; i++) {
            chargingLeft[i] = Toolkit.getDefaultToolkit().getImage("appdata/images/phantom/charging/left/" + String.format("%04d", i + 1) + ".png");
        }
        for (int i = 0; i < 9; i++) {
            chargingRight[i] = Toolkit.getDefaultToolkit().getImage("appdata/images/phantom/charging/right/" + String.format("%04d", i + 1) + ".png");
        }
    }

    //Description: updates the phantom position and velocity and removes itself from handler if there are no further reported data from player
    //Parameters: none
    //Return: void
    public void tick() {

        //Forward iterator
        iterator = (iterator + 1) % 360;

        //Update location of phantom
        if (lastState.isEmpty()) {
            handler.removeObject(this);

        } else {

            //Obtain player state at current tick
            Tuple lastSpeederState = lastState.poll();

            //Phantom position
            x = lastSpeederState.x;
            y = lastSpeederState.y;

            //Phantom velocity
            velX = lastSpeederState.velX;
            velY = lastSpeederState.velY;

            //Phantom state
            isChargingLeft = lastSpeederState.isChargingLeft;
            isChargingRight = lastSpeederState.isChargingRight;
            isScratchingLeft = lastSpeederState.isScratchingLeft;
            isScratchingRight = lastSpeederState.isScratchingRight;
        }
    }

    //Description: renders the phantom
    //Parameters: the Graphics object to draw the phantom
    //Return: void
    public void render(Graphics g) {

        //Render specific phantom posture
        g.setColor(Color.red);
        if (velX == 0)
            g.drawImage(speeder[0], getRelX(), getRelY(), 32, 48, game);
        else if (-7 < velX && velX < 0)
            g.drawImage(speeder[1], getRelX(), getRelY(), 32, 48, game);
        else if (velX < -7)
            g.drawImage(speeder[2], getRelX(), getRelY(), 32, 48, game);
        else if (0 < velX && velX < 7)
            g.drawImage(speeder[3], getRelX(), getRelY(), 32, 48, game);
        else if (7 < velX)
            g.drawImage(speeder[4], getRelX(), getRelY(), 32, 48, game);

        //Renders charging effects
        if (isChargingLeft && velY < -8) {
            int index = iterator % 18 / 2;
            g.drawImage(chargingLeft[index], getRelX() - 32, getRelY() + 32, 48, 10, game);
        }
        if (isChargingRight && velY < -8) {
            int index = iterator % 18 / 2;
            g.drawImage(chargingRight[index], getRelX() + 16, getRelY() + 32, 48, 10, game);
        }

        //Renders scratching effects
        if (isScratchingLeft) {
            int index = iterator % 16 / 2;
            g.drawImage(scratchingLeft[index], getRelX(), getRelY() + 37, 10, 10, game);
        }
        if (isScratchingRight) {
            int index = iterator % 16 / 2;
            g.drawImage(scratchingRight[index], getRelX() + 32 - 15, getRelY() + 37, 10, 10, game);
        }

        //Renders sound barrier effects
        Graphics2D g2d = (Graphics2D) g;
        int index = (iterator % 18) / 2;
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (float) (Math.min(0, velY + 11) / -14.0)));
        g2d.drawImage(soundBarrier[index], getRelX() - 8, getRelY() + 5, 48, 40, game);
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1));
    }

    public void reset() {
        handler.removeObject(this);
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
}
