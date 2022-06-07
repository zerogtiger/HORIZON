import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.*;

public class KeyInput implements KeyListener {


    public KeyInput() {
    }

    public void keyTyped(KeyEvent e) {

    }

    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_D) {
            Stats.debug = 1;
        }
        if (Stats.debug == 0) {
            if (key == KeyEvent.VK_LEFT)
                Stats.setKeyPress(0,0, true);
            if (key == KeyEvent.VK_RIGHT)
                Stats.setKeyPress(0,1, true);
            if (key == KeyEvent.VK_SPACE)
                Stats.setKeyPress(0,2, true);
            if (key == KeyEvent.VK_UP)
                Stats.setKeyPress(0,3, true);
            if (key == KeyEvent.VK_DOWN)
                Stats.setKeyPress(0,4, true);
            if (key == KeyEvent.VK_ENTER)
                Stats.setKeyPress(0,5, true);
        } else {
            if (key == KeyEvent.VK_LEFT)
                Stats.setKeyPress(1,0, true);
            if (key == KeyEvent.VK_RIGHT)
                Stats.setKeyPress(1,1, true);
            if (key == KeyEvent.VK_SPACE)
                Stats.setKeyPress(1,2, true);
            if (key == KeyEvent.VK_UP)
                Stats.setKeyPress(1,3, true);
            if (key == KeyEvent.VK_DOWN)
                Stats.setKeyPress(1,4, true);
            if (key == KeyEvent.VK_ENTER)
                Stats.setKeyPress(1,5, true);
        }

    }

    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_D) {
            Stats.debug = 0;
        }
        if (Stats.debug == 0) {
            if (key == KeyEvent.VK_LEFT)
                Stats.setKeyPress(0,0, false);
            if (key == KeyEvent.VK_RIGHT)
                Stats.setKeyPress(0,1, false);
            if (key == KeyEvent.VK_SPACE)
                Stats.setKeyPress(0,2, false);
            if (key == KeyEvent.VK_UP)
                Stats.setKeyPress(0,3, false);
            if (key == KeyEvent.VK_DOWN)
                Stats.setKeyPress(0,4, false);
            if (key == KeyEvent.VK_ENTER)
                Stats.setKeyPress(0,5, false);
        } else {
            if (key == KeyEvent.VK_LEFT)
                Stats.setKeyPress(1,0, false);
            if (key == KeyEvent.VK_RIGHT)
                Stats.setKeyPress(1,1, false);
            if (key == KeyEvent.VK_SPACE)
                Stats.setKeyPress(1,2, false);
            if (key == KeyEvent.VK_UP)
                Stats.setKeyPress(1,3, false);
            if (key == KeyEvent.VK_DOWN)
                Stats.setKeyPress(1,4, false);
            if (key == KeyEvent.VK_ENTER)
                Stats.setKeyPress(1,5, false);
        }

    }

}
