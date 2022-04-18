import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class KeyInput extends KeyAdapter {

    private Handler handler;

    public KeyInput (Handler handler) {
        this.handler = handler;
    }

    public void keyPressed (KeyEvent e) {
        int key = e.getKeyCode();
        for (int i = 0; i < handler.object.size(); i++) {
            GameObject tempObject = handler.object.get(i);
            int speed = 4;
            if (tempObject.getId() == ID.Obstacle)  {
                speed += 2;
                if (key == KeyEvent.VK_LEFT) tempObject.setVelX(speed);
                else if (key == KeyEvent.VK_RIGHT) tempObject.setVelX(-speed);
            }

            if (key == KeyEvent.VK_ESCAPE)
                System.exit(0);
        }

    }

    public void keyReleased (KeyEvent e) {
        int key = e.getKeyCode();

        for (int i = 0; i < handler.object.size(); i++) {
            GameObject tempObject = handler.object.get(i);
            if (tempObject.getId() == ID.Obstacle)  {
                if (key == KeyEvent.VK_LEFT) tempObject.setVelX(0);
                else if (key == KeyEvent.VK_RIGHT) tempObject.setVelX(0);
            }
        }
    }

}
