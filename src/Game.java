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

    private Random r = new Random();
    private Handler handler;
    private HUD hud;
    private Stats stats;
    private Player player;
    private Pursuer pursuer;
    private Camera camera;

    public Game() {
        handler = new Handler();
        hud = new HUD();
        stats = new Stats();

        this.addKeyListener(new KeyInput(handler));
        new Window(WIDTH, HEIGHT, "HORIZON 極速狂飆", this);

        player = new Player(-16, HEIGHT/2+200, ID.Player, handler);
        pursuer = new Pursuer(player);
        camera = new Camera(player);

//        handler.addObject(player);
        for (int i = 1; i <= 150; i++) {
            for (int j = 1; j <= 300; j++) {
                Stats.obstacles[i][j] = Math.random()>0.95;
            }
        }

//        new BasicObstacle(200, 200, 5, 5,
//                ID.Obstacle, handler);
//        for (int i = 1; i <= 10; i++) {
//            new BasicObstacle(
//                    r.nextInt(-1000, Game.WIDTH+1000),
//                    r.nextInt(-500, 300)-300,
//                    r.nextInt(50, 200),
//                    r.nextInt(200, 400),
//                    ID.Obstacle, handler);
//        }
    }

    public static int clamp(int val, int min, int max) {
        return Math.max(min, Math.min(max, val));
    }
    public static int reverseClamp(int val, int min, int max) {
        if (val < min || val > max)
            return val;
        else if (Math.abs(val - min) > Math.abs(max - val))
            return max;
        else if (Math.abs(val - min) < Math.abs(max - val))
            return min;
        return val;
    }

//    public static boolean isInRange(int relX, int relY, int HRange, int VRange) {
//        return relX > Stats.trueX - HRange && relX < Stats.trueX + HRange && relY > Stats.trueY - VRange;
//    }

    private void tick() {
        handler.tick();
        hud.tick();
        pursuer.tick();
        camera.tick();
    }

    private void render() {
        BufferStrategy bs = this.getBufferStrategy();
        if (bs == null) {
            this.createBufferStrategy(3);
            return;
        }

        Graphics g = bs.getDrawGraphics();

        g.setColor(new Color(59, 56, 53));
        g.fillRect(0,0,WIDTH, HEIGHT);

        handler.render(g);
        hud.render(g);
        pursuer.render(g);

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
        this.requestFocus();
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
                System.out.println("relX: " + Camera.getRelX());
                System.out.println("relY: " + Camera.getRelY());
                frames = 0;
            }
        }
        stop();
    }

    public static void main(String[] args) {
        new Game();
        for (int i = 1; i <= 10; i++) {
            for (int j = 1; j <= 30; j++) {
                System.out.printf("%5b, ", Stats.obstacles[i][j]);
            }
            System.out.println();
        }
    }
}
