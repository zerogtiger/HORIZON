import javax.swing.*;
import java.util.*;
import java.io.*;
import java.awt.*;

public class Game extends JPanel implements Runnable {
    //Replace canvas with JPanel, no need to implement runnable,
    //Replace render() with paintComponent. Try without buffer and if screen flickers, use Ms. Wong's buffer system
    //Replace render() with repaint() in run() method

    public static final int WIDTH = 1200, HEIGHT = WIDTH / 16 * 9;

    private Thread thread;
    private boolean running = false;

    private Random r = new Random();
    private final Handler handler;
    private final Handler ghandler;
    private final Handler ahandler;
    private HUD hud;
    private Stats stats;
    private static Player player;
    private Pursuer pursuer;
    private Camera camera;
    private Map map;
    private static Menu menu;
    private KeyInput keyInput;
    private GameOrganizer gameOrganizer;
    private static Leaderboard leaderboard;
    private Environment environment;

//    private JTextField textField;

    private static int seed;

    private long startTime, timeElapsed, frameCount = 0;

    public static state gameState = state.Lead;

    public enum state {
        Game,
        GameOver,
        Menu,
        Options,
        Leaderboard,
        LeaderboardEntry,
        Pause,
        Lead,
        GameReady;
    }

    public Game() throws IOException {
        seed = r.nextInt( 4)*100000;
        seed += r.nextInt( 4)*10000;
        seed+= r.nextInt(10000);
        handler = new Handler();
        ghandler = new Handler();
        ahandler = new Handler();
        hud = new HUD();
        stats = new Stats();
        leaderboard = new Leaderboard();
        menu = new Menu(this, leaderboard, handler);
        keyInput = new KeyInput();
        player = new Player(-16, HEIGHT, ID.Player, handler, 0, this);
        pursuer = new Pursuer(player);
        camera = new Camera(player);
        gameOrganizer = new GameOrganizer(this);
        environment = new Environment(seed);

//        textField = new JTextField("Test");
//        this.setLayout(null);
//        this.add(textField);
//        textField.setBounds(25, 20, 200, 25);
//        textField.setBackground(new Color(255,255,255));
//        textField.setFont(new Font("Monospaced", Font.BOLD, 18));
//        textField.setVisible(false);

        new Window(WIDTH, HEIGHT, "HORIZON極速狂飆", this);
    }

    public static int clamp(int val, int min, int max) {
        return Math.max(min, Math.min(max, val));
    }

    public static void collision(GameObject obstacle) {
        if (player.getBounds().intersects(obstacle.getCollisionBounds()) && player.getY() >= obstacle.getY() + obstacle.height) {
            menu.setFocus(0);
            gameOver(Stats.speederDistance, seed, 0);

        } else if (player.getBounds().intersects(obstacle.getLeftBounds()) && player.getY() < obstacle.getY() + obstacle.getHeight()) {
            player.setX(obstacle.getX() - 32);
            player.setVelX(0);
            player.setVelY(clamp(player.getVelY() + 10, -25, -3));
            Stats.setKeyPress(0, 1, false);
            Stats.setKeyPress(1, 1, false);
        } else if (player.getBounds().intersects(obstacle.getRightBounds()) && player.getY() < obstacle.getY() + obstacle.getHeight()) {
            player.setX(obstacle.getX() + obstacle.width);
            player.setVelX(0);
            player.setVelY(clamp(player.getVelY() + 7, -25, -3));
            Stats.setKeyPress(0, 0, false);
            Stats.setKeyPress(1, 0, false);
        }
        player.setChargingLeft(player.getLeftChargingBounds().intersects(obstacle.getRightChargingBounds()) || player.getIsChargingLeft());
        player.setChargingRight(player.getRightChargingBounds().intersects(obstacle.getLeftChargingBounds()) || player.getIsChargingRight());
        player.setScratchingLeft(player.getLeftChargingBounds().intersects(obstacle.getRightScratchingBounds()) || player.getIsScratchingLeft());
        player.setScratchingRight(player.getRightChargingBounds().intersects(obstacle.getLeftScratchingBounds()) || player.getIsScratchingRight());

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

    public static void gameOver(int distance, int seed, int deathMethod) {
        if (leaderboard.isOnLeaderboard(distance)) {
            menu.setTempLeaderboard(distance, seed, deathMethod);
            gameState = state.LeaderboardEntry;
        } else {
            gameState = state.GameOver;
        }
    }

    public void reset() {
        gameOrganizer.setSeed(seed);
        gameOrganizer.reset();
        environment.reset(seed);
        player.setY(HEIGHT);
        player.setX(-16);
        player.setVelX(0);
        player.setVelY(-10);
        camera.tick();
        pursuer.setDistance(10000);
        Stats.speederDistance = 0;
        Stats.CHARGE = 0;
        keyInput.reset();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.setColor(new Color(59, 56, 53));
        g.fillRect(0, 0, WIDTH + 15, HEIGHT + 15);

        if (gameState == state.Game || gameState == state.Pause) {
            ghandler.render(g);
            handler.render(g);
            ahandler.render(g);
            environment.render(g);
            hud.render(g);
            pursuer.render(g);
        }
//        if (gameState == state.LeaderboardEntry) {
//            textField.setVisible(true);
//        }
//        else
//            textField.setVisible(false);
        menu.render(g);
//        drawRuler(g);
        g.dispose();
    }

    private void tick() throws IOException {
        menu.tick();
        if (gameState == state.Game) {
            gameOrganizer.tick();
            ghandler.tick();
            handler.tick();
            ahandler.tick();
            camera.tick();
            map.tick();
            hud.tick();
            pursuer.tick();
            environment.tick();
        }
        timeElapsed = System.currentTimeMillis() - startTime;
        frameCount++;

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
        startTime = System.currentTimeMillis();
        timeElapsed = 0;
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
        while (running) {
            try {
                tick();
            } catch (IOException e) {
                e.printStackTrace();
            }
            this.repaint();
            try {
                Thread.sleep(1000 / 70);
            } catch (Exception e) {
                e.printStackTrace();
            }
            System.out.println("FPS: " + (double) frameCount / (timeElapsed / 1000.0));
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

    public Handler getAhandler() {
        return ahandler;
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

    public static void setSeed(int seed) {
        Game.seed = seed;
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

    public KeyInput getKeyInput() {
        return keyInput;
    }

    public Menu getMenu() {
        return menu;
    }

    public static void main(String[] args) throws IOException {
        new Game();
    }
}
