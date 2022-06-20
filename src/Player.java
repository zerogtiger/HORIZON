/*

Description:

Main player/speeder for the game to be controlled by the user. Responsible for rendering the speeder in various
postures depending on its velocities and states; rendering the visual effects, including charging, scratching, sound
barrier, &c; responsible for updating the velocities and position of the speeder according to the states of key-presses
to reflect a dynamic, airborne sensation; responsible for updating charge of the speeder.

*/

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import java.awt.*;
import java.io.File;
import java.util.*;

public class Player extends GameObject {

    //Booleans to report the state of the speeder
    private boolean isChargingLeft = false, isChargingRight = false, isScratchingLeft = false, isScratchingRight = false, isBumped = false;

    //Game which the speeder belongs to
    private final Game game;

    //Images of speeder postures
    private final Image[] speeder = {Toolkit.getDefaultToolkit().getImage("appdata/pics/SG.png"),
            Toolkit.getDefaultToolkit().getImage("appdata/pics/SGL1.png"),
            Toolkit.getDefaultToolkit().getImage("appdata/pics/SGL2.png"),
            Toolkit.getDefaultToolkit().getImage("appdata/pics/SGR1.png"),
            Toolkit.getDefaultToolkit().getImage("appdata/pics/SGR2.png")},

    //Images of effects
    soundBarrier = new Image[9],
            scratchingLeft = new Image[8],
            scratchingRight = new Image[8],
            chargingLeft = new Image[9],
            chargingRight = new Image[9];

    //Sound effects
    private Clip charging, collision, scratching;

    //iterator to assist in displaying animations
    private int iterator;

    //Constructor
    public Player(int x, int y, ID id, Handler handler, int normalVelY, Game game) {

        //Super GameObject
        super(x, y, id, handler);

        //Initialize variables
        iterator = 0;
        this.game = game;
        velX = 0;
        velY = 0;

        //Loading sound effects
        try {

            //Charging
            AudioInputStream sound = AudioSystem.getAudioInputStream(new File("appdata/audio/charging.wav"));
            charging = AudioSystem.getClip();
            charging.open(sound);

            //Volume of charging sound
            FloatControl gainControl = (FloatControl) charging.getControl(FloatControl.Type.MASTER_GAIN);
            float range = gainControl.getMaximum() - gainControl.getMinimum();
            float gain = (float) ((range * 0.85) + gainControl.getMinimum());
            gainControl.setValue(gain);

            charging.loop(Clip.LOOP_CONTINUOUSLY);
            charging.stop();

            //Collision
            sound = AudioSystem.getAudioInputStream(new File("appdata/audio/collision2.wav"));
            collision = AudioSystem.getClip();
            collision.open(sound);

            //Volume of collision sound
            gainControl = (FloatControl) collision.getControl(FloatControl.Type.MASTER_GAIN);
            range = gainControl.getMaximum() - gainControl.getMinimum();
            gain = (float) ((range * 0.9) + gainControl.getMinimum());
            gainControl.setValue(gain);

            //Scratching
            sound = AudioSystem.getAudioInputStream(new File("appdata/audio/scratching.wav"));
            scratching = AudioSystem.getClip();
            scratching.open(sound);

            //Volume of scratching sound
            gainControl = (FloatControl) scratching.getControl(FloatControl.Type.MASTER_GAIN);
            range = gainControl.getMaximum() - gainControl.getMinimum();
            gain = (float) ((range * 0.85) + gainControl.getMinimum());
            gainControl.setValue(gain);

            scratching.loop(Clip.LOOP_CONTINUOUSLY);
            scratching.stop();

        } catch (Exception e) {
            e.printStackTrace();
        }


        //Compile images of effects
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

    //Description: updates the speeder velocity, position with reference to the key-pressed states. Updates charge and distance travelled
    //Parameters: none
    //Return: void
    public void tick() {

        //Forward iterator
        iterator = (iterator + 1) % 360;

        //Check pursuer distance
        if (Pursuer.distance <= 0) {
            Game.gameOver(Stats.speederDistance, game.getSeed(), 1);
        }

        //Update speeder velocities depending on the key states
        if (Stats.debug == 0) {
            //y-velocity
            if (isScratchingLeft || isScratchingRight)
                velY = Game.clamp(velY + (iterator % 3 == 0 ? 1 : 0), -25, -3);
            else if ((Stats.getKeyPress()[0][2] || (Stats.getKeyPress()[0][0] && Stats.getKeyPress()[0][1]) || Stats.getKeyPress()[0][3]) && Stats.CHARGE > 0) {
                velY = Game.clamp(velY - (iterator % 3 == 0 ? 1 : 0), -25, 0);
                Stats.CHARGE -= 3;
            } else {
                if (velY < -10) {
                    velY = Game.clamp(velY + (iterator % 4 == 0 ? 1 : 0), -25, -10);
                } else {
                    velY = Game.clamp(velY - (iterator % 5 == 0 ? 1 : 0), -10, 0);
                }
            }

            //x-velocity
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

        //Increase charge
        if (isChargingLeft && velY < -8)
            Stats.CHARGE += 2;
        if (isChargingRight && velY < -8)
            Stats.CHARGE += 2;

        //Update location of speeder
        y += velY;
        x += velX;

        //Update distance
        Stats.speederDistance += -velY;
        Stats.CHARGE = Game.clamp(Stats.CHARGE, 0, 800);

//        if ((isChargingLeft || isChargingRight) && velY < -8) {
//            game.setCharging(true);
//        } else {
//            game.setCharging(false);
//        }
//        if (isScratchingLeft || isScratchingRight) {
//            game.setScratching(true);
//        } else {
//            game.setScratching(false);
//        }
//        if (isBumped) {
//            game.setCollision(true);
//        }

        //Play sound effects according to speeder states
        if (game.getGameEffect()) {
            if (((isChargingLeft || isChargingRight) && velY < -8)) {
                charging.loop(Clip.LOOP_CONTINUOUSLY);
                charging.start();
            } else if (charging.isActive()) {
                charging.stop();
            }
            if (isScratchingLeft || isScratchingRight) {
                scratching.loop(Clip.LOOP_CONTINUOUSLY);
                scratching.start();
            } else if (scratching.isActive()) {
                scratching.stop();
            }
            if (isBumped && !collision.isActive()) {
                collision.setFramePosition(0);
                collision.start();
            }
        } else {
            stopSounds();
        }


        //Reset speeder states
        isChargingLeft = false;
        isChargingRight = false;
        isScratchingLeft = false;
        isScratchingRight = false;
        isBumped = false;
    }

    //Description: renders the speeder
    //Parameters: the Graphics object to draw the speeder
    //Return: void
    public void render(Graphics g) {

        //Render specific speeder posture
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

    public void stopSounds() {
        if (charging.isActive())
            charging.stop();
        if (scratching.isActive())
            scratching.stop();
        if (collision.isActive())
            collision.stop();
    }
}
