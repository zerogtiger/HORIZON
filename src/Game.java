import java.awt.image.BufferStrategy;
import java.util.*;
import java.io.*;
import java.awt.*;

public class Game extends Canvas implements Runnable{

    @Serial
    private static final long serialVersionUID = -1442798787354930462L;

    public static final int WIDTH = 1200, HEIGHT = WIDTH/16*9;

    private Thread thread;
    private boolean running = false;

    private Random r;
    private Handler handler;

    public Game() {
        handler = new Handler();
        this.addKeyListener(new KeyInput(handler));
        new Window(WIDTH, HEIGHT, "HORIZON", this);

        r = new Random();
        handler.addObject(new Player(WIDTH/2, HEIGHT/2, ID.Player));
        handler.addObject(new Obstacle(WIDTH/2-400, -700, ID.Obstacle));
        handler.addObject(new Obstacle(WIDTH/2+400, -1100, ID.Obstacle));
        handler.addObject(new Obstacle(WIDTH/2-100, -1000, ID.Obstacle));
    }

    private void render() {
        BufferStrategy bs = this.getBufferStrategy();
        if (bs == null) {
            this.createBufferStrategy(3);
            return;
        }

        Graphics g = bs.getDrawGraphics();
        float[] HSB = Color.RGBtoHSB(59, 56, 53, null);

        g.setColor(Color.getHSBColor(HSB[0], HSB[1], HSB[2]));
        g.fillRect(0,0,WIDTH, HEIGHT);

        handler.render(g);

        g.dispose();
        bs.show();
    }

    public synchronized void start() {
        thread = new Thread(this);
        thread.start();
        running = true;
    }

    public synchronized void stop() {
        try {
            thread.join();
            running = false;
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void run() {
        long lastTime = System.nanoTime();
        double amountOfTicks = 60.0;
        double ns = 1000000000 / amountOfTicks;
        double delta = 0;
        long timer = System.currentTimeMillis();
        int frames = 0;
        while (running) {
            long now = System.nanoTime();
            delta += (now-lastTime) / ns;
            lastTime = now;
            while (delta >= 1) {
                tick();
                delta--;
            }
            if (running)
                render();
            frames++;

            if (System.currentTimeMillis() - timer > 1000) {
                timer += 1000;
                System.out.println("FPS: " + frames);
                frames = 0;
            }
        }
        stop();
    }

    private void tick() {
        handler.tick();
    }

    public static void main(String[] args) {
        new Game();
    }
}
