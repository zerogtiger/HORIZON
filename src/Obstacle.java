import java.awt.*;

public class Obstacle extends GameObject{

    public Obstacle(int x, int y, ID id) {
        super(x, y, id);
    }

    public void tick() {
        x += velX;
        y += 5;
    }


    public void render(Graphics g) {
        g.setColor(Color.lightGray);
        g.fillRect(x, y, 300, 200);
    }
}
