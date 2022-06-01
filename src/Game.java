import java.awt.image.BufferStrategy;
import java.util.*;
import java.io.*;
import java.awt.*;

public class Game extends Canvas implements Runnable {
    //Replace canvas with JFrame, no need to implement runnable,
    //Replace render() with paintComponent. Try without buffer and if screen flickers, use Ms. Wong's buffer system
    //Replace render() with repaint() in run() method

    @Serial
    private static final long serialVersionUID = -1442798787354930462L;

    public static final int WIDTH = 1200, HEIGHT = WIDTH / 16 * 9;

    private Thread thread;
    private boolean running = false;

    private Random r = new Random();
    private final Handler handler;
    private final Handler ghandler;
    private HUD hud;
    private Stats stats;
    private static Player player;
    private Pursuer pursuer;
    private Camera camera;
    private Map map;
    private Menu menu;
    private GameOrganizer gameOrganizer;

    private int seed = 1;

    public enum state {
        Game,
        Login,
        SignUp,
        GameOver,
        Menu,
        Options,
        Leaderboard,
        Pause;
    }

    public static state gameState = state.Menu;

    public Game() {
        handler = new Handler();
        ghandler = new Handler();
        hud = new HUD();
        stats = new Stats();
        menu = new Menu(this, handler);
        player = new Player(-16, HEIGHT, ID.Player, handler, 0, this);
        pursuer = new Pursuer(player);
        camera = new Camera(player);
        gameOrganizer = new GameOrganizer(this);
        this.addMouseListener(menu);
        this.addKeyListener(new KeyInput(handler));

        new Window(WIDTH, HEIGHT, "HORIZON極速狂飆", this);
    }

    public static int clamp(int val, int min, int max) {
        return Math.max(min, Math.min(max, val));
    }

    public static void collision(GameObject obstacle) {
        if (player.getBounds().intersects(obstacle.getCollisionBounds()) && player.getY() >= obstacle.getY() + obstacle.height) {
//            gameState = state.GameOver;
        } else if (player.getBounds().intersects(obstacle.getLeftBounds()) && player.getY() < obstacle.getY() + obstacle.getHeight()) {
            player.setX(obstacle.getX() - 32);
            Stats.KEYPRESS[0][1] = false;
            Stats.KEYPRESS[1][1] = false;
        } else if (player.getBounds().intersects(obstacle.getRightBounds()) && player.getY() < obstacle.getY() + obstacle.getHeight()) {
            player.setX(obstacle.getX() + obstacle.width);
            Stats.KEYPRESS[0][0] = false;
            Stats.KEYPRESS[1][0] = false;
        }
        player.setChargingLeft(player.getBounds().intersects(obstacle.getRightChargingBounds()) || player.getIsChargingLeft());
        player.setChargingRight(player.getBounds().intersects(obstacle.getLeftChargingBounds()) || player.getIsChargingRight());
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
        gameOrganizer.reset();
        player.setY(HEIGHT);
        player.setX(-16);
        camera.tick();
        pursuer.setDistance(10000);
        Stats.speederDistance = 0;
        Stats.CHARGE = 0;
    }

    private void tick() {
        if (gameState == state.Game) {
            gameOrganizer.tick();
            ghandler.tick();
            handler.tick();
            camera.tick();
            map.tick();
            hud.tick();
            pursuer.tick();
        } else {
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
        g.fillRect(0, 0, WIDTH + 15, HEIGHT + 15);

        if (gameState == state.Game) {
            ghandler.render(g);
            handler.render(g);
            hud.render(g);
            pursuer.render(g);
        } else {
            menu.render(g);
        }
        drawRuler(g);
        g.dispose();
        bs.show();
    }

    public void drawRuler(Graphics g) {
        Color[] colors = {new Color(0, 200, 0),
                new Color(200, 0, 0),
                new Color(50, 100, 200),
                new Color(200, 200, 0)};
        int curr = 0;
        for (int i = 0; i < 60; i++) {
            g.setColor(colors[i % 4]);
            g.fillRect(curr, 0, 20, 4);
            curr += 20;
        }
        curr = 0;
        for (int i = 0; i < 34; i++) {
            g.setColor(colors[i % 4]);
            g.fillRect(0, curr, 4, 20);
            curr += 20;
        }
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
        } catch (Exception e) {
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
            delta += (now - lastTime) / ns;
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

    public static Player getPlayer() {
        return player;
    }

    public static void setPlayer(Player player) {
        Game.player = player;
    }

    public Camera getCamera() {
        return camera;
    }

    public void setCamera(Camera camera) {
        this.camera = camera;
    }

    public Handler getGhandler() {
        return ghandler;
    }

    public Handler getHandler() {
        return handler;
    }

    public HUD getHud() {
        return hud;
    }

    public int getSeed() {
        return seed;
    }

    public Pursuer getPursuer() {
        return pursuer;
    }

    public Map getMap() {
        return map;
    }

    public void setMap(Map map) {
        this.map = map;
    }



    public static void main(String[] args) {
        new Game();
    }
}
