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
    private final Handler handler;
    private final Handler ghandler;
    private HUD hud;
    private Stats stats;
    private Player player;
    private Pursuer pursuer;
    private Camera camera;
    private Map map;
    private Menu menu;

    public enum state {
        Game,
        Login,
        SignUp,
        GameOver,
        Menu,
        Options,
        Leaderboard;
    }

    public state gameState = state.Menu;

    public Game() {
        handler = new Handler();
        ghandler = new Handler();
        map = new Map(1, 1, handler, ghandler);
        hud = new HUD();
        stats = new Stats();
        menu = new Menu(this, handler);
        player = new Player(-16, HEIGHT, ID.Player, handler, 0, this);
        pursuer = new Pursuer(player);
        camera = new Camera(player);
        this.addMouseListener(menu);
        this.addKeyListener(new KeyInput(handler));

        new Window(WIDTH, HEIGHT, "HORIZON 極速狂飆", this);
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

    public void reset() {
        player.setY(HEIGHT);
        player.setX(-16);
        camera.tick();
        pursuer.setDistance(10000);
        Stats.speederDistance = 0;
        Stats.CHARGE = 0;
    }

//    public static boolean isInRange(int relX, int relY, int HRange, int VRange) {
//        return relX > Stats.trueX - HRange && relX < Stats.trueX + HRange && relY > Stats.trueY - VRange;
//    }

    private void tick() {
        if (gameState == state.Game) {
            ghandler.tick();
            handler.tick();
            camera.tick();
            map.tick();
            hud.tick();
            pursuer.tick();
        }
        else {
            menu.tick();
        }

    }

    private void render() {
        BufferStrategy bs = this.getBufferStrategy();
        if (bs == null) {
            this.createBufferStrategy(3);
            return;
        }

        Graphics g = bs.getDrawGraphics();

        g.setColor(new Color(59, 56, 53));
        g.fillRect(0,0,WIDTH+15, HEIGHT+15);

        if (gameState == state.Game) {
            ghandler.render(g);
            handler.render(g);
            hud.render(g);
            pursuer.render(g);
        }
        else {
            menu.render(g);
        }

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
                frames = 0;
            }
        }
        stop();
    }

    public static void main(String[] args) {
        new Game();
    }
}
