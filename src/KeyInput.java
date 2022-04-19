import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class KeyInput extends KeyAdapter {

    private Handler handler;
    public static boolean[] KEYPRESS;

    public KeyInput (Handler handler) {
        this.handler = handler;
        KEYPRESS = new boolean[3]; //0 for left, 1 for right, 2 for space.
    }

    public void keyPressed (KeyEvent e) {
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_LEFT)
            KEYPRESS[0] = true;
        if (key == KeyEvent.VK_RIGHT)
            KEYPRESS[1] = true;
        if (key == KeyEvent.VK_SPACE)
            KEYPRESS[2] = true;
        if (key == KeyEvent.VK_ESCAPE)
            System.exit(0);
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

    public void keyReleased (KeyEvent e) {
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_LEFT)
            KEYPRESS[0] = false;
        if (key == KeyEvent.VK_RIGHT)
            KEYPRESS[1] = false;
        if (key == KeyEvent.VK_SPACE)
            KEYPRESS[2] = false;
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
