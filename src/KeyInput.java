import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class KeyInput extends KeyAdapter {

    private Handler handler;

    public KeyInput(Handler handler) {
        this.handler = handler;
    }

    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_D) {
            Stats.debug = 1;
        }
        if (Stats.debug == 0) {
            if (key == KeyEvent.VK_LEFT)
                Stats.KEYPRESS[0][0] = true;
            if (key == KeyEvent.VK_RIGHT)
                Stats.KEYPRESS[0][1] = true;
            if (key == KeyEvent.VK_SPACE)
                Stats.KEYPRESS[0][2] = true;
        } else {
            if (key == KeyEvent.VK_LEFT)
                Stats.KEYPRESS[1][0] = true;
            if (key == KeyEvent.VK_RIGHT)
                Stats.KEYPRESS[1][1] = true;
            if (key == KeyEvent.VK_SPACE)
                Stats.KEYPRESS[1][2] = true;
            if (key == KeyEvent.VK_UP)
                Stats.KEYPRESS[1][3] = true;
            if (key == KeyEvent.VK_DOWN)
                Stats.KEYPRESS[1][4] = true;
        }
//        if (key == KeyEvent.VK_UP)
//            Player.relVelY = -5;
//        if (key == KeyEvent.VK_DOWN)
//            Player.relVelY = +5;
//        for (int i = 0; i < handler.object.size(); i++) {
//            GameObject tempObject = handler.object.get(i);
//            if (tempObject.getId() == ID.Player) {
//                if (key == KeyEvent.VK_LEFT) Player.relX = -8;
//                else if (key == KeyEvent.VK_RIGHT) Player.relX = 8;
//            }
//
//            else if (tempObject.getId() == ID.Obstacle || tempObject.id == ID.ObstacleFalloff)  {
//                tempObject.setVelX(-Player.relX);
//                tempObject.setVelY(Player.relY);
//            }
//
//            if (key == KeyEvent.VK_ESCAPE)
//                System.exit(0);
//        }

    }

    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_D) {
            Stats.debug = 0;
        }
        if (Stats.debug == 0) {
            if (key == KeyEvent.VK_LEFT)
                Stats.KEYPRESS[0][0] = false;
            if (key == KeyEvent.VK_RIGHT)
                Stats.KEYPRESS[0][1] = false;
            if (key == KeyEvent.VK_SPACE)
                Stats.KEYPRESS[0][2] = false;
        } else {
            if (key == KeyEvent.VK_LEFT)
                Stats.KEYPRESS[1][0] = false;
            if (key == KeyEvent.VK_RIGHT)
                Stats.KEYPRESS[1][1] = false;
            if (key == KeyEvent.VK_SPACE)
                Stats.KEYPRESS[1][2] = false;
            if (key == KeyEvent.VK_UP)
                Stats.KEYPRESS[1][3] = false;
            if (key == KeyEvent.VK_DOWN)
                Stats.KEYPRESS[1][4] = false;
        }
//        if (key == KeyEvent.VK_UP)
//            Player.relVelY = 0;
//        if (key == KeyEvent.VK_DOWN)
//            Player.relVelY = -0;
//        for (int i = 0; i < handler.object.size(); i++) {
//            GameObject tempObject = handler.object.get(i);
//            if (tempObject.getId() == ID.Player) {
//                if (key == KeyEvent.VK_LEFT) Player.relX = 0;
//                else if (key == KeyEvent.VK_RIGHT) Player.relX = 0;
//            }
//
//            else if (tempObject.getId() == ID.Obstacle || tempObject.id == ID.ObstacleFalloff)  {
//                tempObject.setVelX(0);
//            }
//
//            if (key == KeyEvent.VK_ESCAPE)
//                System.exit(0);
//        }
    }
//
//    public boolean[] getKeyPress() {
//        return keyPress;
//    }
}
