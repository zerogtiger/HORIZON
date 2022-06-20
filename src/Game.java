/*

Description:

Main game class responsible for hosting the game. Responsible for storing the current game state, updating according
components, and rendering any according components. Contains various classes responsible for different parts of the game
such as menu, heads-up-display (HUD), map, pursuer, &c. Further acts as a central toolkit hub with utility methods that
are accessed by other objects during both standalone and interacting calculations. Contains main game loop and
paintComponent methods utilizing the off-screen buffer.

*/

import javax.sound.sampled.*;
import javax.swing.*;
import java.util.*;
import java.io.*;
import java.awt.*;

public class Game extends JPanel implements Runnable {

    //Width and height of window to be displayed
    public static final int WIDTH = 1200, HEIGHT = WIDTH / 16 * 9;

    //Framework related variables
    private Thread thread;
    private JFrame frame;
    private boolean running = false;

    //Game component related variables
    private Random r = new Random();

    //Respectively: object handler, ground handler, airborne objects handler
    private final Handler handler;
    private final Handler ghandler;
    private final Handler ahandler;

    private HUD hud;
    private Stats stats;

    private Camera camera;
    private static Player player;
    private Pursuer pursuer;

    private GameOrganizer gameOrganizer;
    private Map map;
    private Environment environment;

    private static Leaderboard leaderboard;
    private static Menu menu;
    private KeyInput keyInput;

    //Game music
    private Clip menuMusic, inGameMusic;

    //Music control variables
    private boolean isMenuMusic, isGameMusic, isGameEffect;

    //Off-screen buffer to smooth graphics
    Image offScreenImage;
    private Graphics offScreenBuffer;
    private long startTime, timeElapsed, frameCount = 0;

//    private JTextField textField;

    //Seed for game
    private static int seed;

    //Game state to be referenced when displaying components
    public static state gameState = state.Lead;

    //Enum for all game states
    public enum state {
        Game,
        GameOver,
        Menu,
        Options,
        Leaderboard,
        LeaderboardEntry,
        Pause,
        Lead,
        GameReady,
        Tutorial;
    }

    //Constructor
    public Game() throws IOException {

        //Generate random seed
        seed = r.nextInt(4) * 100000;
        seed += r.nextInt(4) * 10000;
        seed += r.nextInt(10000);

        //Initialize objects and variables
        handler = new Handler();
        ghandler = new Handler();
        ahandler = new Handler();

        hud = new HUD();
        stats = new Stats();

        leaderboard = new Leaderboard();
        menu = new Menu(this, leaderboard, handler);
        keyInput = new KeyInput();

        player = new Player(-16, HEIGHT * 2, ID.Player, handler, 0, this);
        pursuer = new Pursuer(player, this);
        camera = new Camera(player);

        gameOrganizer = new GameOrganizer(this);
        environment = new Environment(seed, this);

        //Reset sound control variables
        isMenuMusic = true;
        isGameMusic = true;
        isGameEffect = true;

        //Loading audio
        try {

            //Menu background music
            AudioInputStream sound = AudioSystem.getAudioInputStream(new File("appdata/audio/menuMusic.wav"));
            menuMusic = AudioSystem.getClip();
            menuMusic.open(sound);

            //In-game music
            sound = AudioSystem.getAudioInputStream(new File("appdata/audio/inGameMusic.wav"));
            inGameMusic = AudioSystem.getClip();
            inGameMusic.open(sound);

        } catch (Exception e) {
            e.printStackTrace();
        }

        //Volumn of game music
        FloatControl gainControl = (FloatControl) menuMusic.getControl(FloatControl.Type.MASTER_GAIN);
        float range = gainControl.getMaximum() - gainControl.getMinimum();
        float gain = (float) ((range * 0.95) + gainControl.getMinimum());
        gainControl.setValue(gain);

        gainControl = (FloatControl) inGameMusic.getControl(FloatControl.Type.MASTER_GAIN);
        range = gainControl.getMaximum() - gainControl.getMinimum();
        gain = (float) ((range * 0.9) + gainControl.getMinimum());
        gainControl.setValue(gain);

        menuMusic.loop(Clip.LOOP_CONTINUOUSLY);
        inGameMusic.loop(Clip.LOOP_CONTINUOUSLY);

//        textField = new JTextField("Test");
//        this.setLayout(null);
//        this.add(textField);
//        textField.setBounds(25, 20, 200, 25);
//        textField.setBackground(new Color(255,255,255));
//        textField.setFont(new Font("Monospaced", Font.BOLD, 18));
//        textField.setVisible(false);
        this.setDoubleBuffered(true);
        this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        this.setMaximumSize(new Dimension(WIDTH, HEIGHT));
        this.setMinimumSize(new Dimension(WIDTH, HEIGHT));
        this.setFocusable(true);
        this.addKeyListener(this.getKeyInput());
        this.addMouseListener(this.getMenu());

        frame = new JFrame("HORIZON極速狂飆");
        frame.setIconImage(Toolkit.getDefaultToolkit().getImage("appdata/pics/icon.png"));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(this);
        frame.setResizable(false);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        this.start();
    }

    //Description: clamps the given value within the given range
    //Parameters: the value and the range to be clamped between
    //Return: the clamped value
    public static int clamp(int val, int min, int max) {

        //Clamps the provided value between specified min and max values
        return Math.max(min, Math.min(max, val));
    }

    //Description: calculates the intersection situation of the player with the given object, and toggle game-state and player variables accordingly
    //Parameters: the GameObject in question
    //Return: void
    public static void collision(GameObject obstacle) {

        //If the speeder is intersecting with the collision bounds, then end the current game
        if (player.getBounds().intersects(obstacle.getCollisionBounds()) && player.getY() >= obstacle.getY() + obstacle.height) {
            menu.setFocus(0);
            gameOver(Stats.speederDistance, seed, 0);
        }

        //If the speeder is intersecting with the sides of the obstacle, then move the speeder outside the obstacle
        else if (player.getBounds().intersects(obstacle.getLeftBounds()) && player.getY() < obstacle.getY() + obstacle.getHeight()) {
            player.setBumped(true);
            player.setX(obstacle.getX() - 32);
            player.setVelX(0);
            player.setVelY(clamp(player.getVelY() + 10, -25, -3));
            Stats.setKeyPress(0, 1, false);
            Stats.setKeyPress(1, 1, false);
        } else if (player.getBounds().intersects(obstacle.getRightBounds()) && player.getY() < obstacle.getY() + obstacle.getHeight()) {
            player.setBumped(true);
            player.setX(obstacle.getX() + obstacle.width);
            player.setVelX(0);
            player.setVelY(clamp(player.getVelY() + 7, -25, -3));
            Stats.setKeyPress(0, 0, false);
            Stats.setKeyPress(1, 0, false);
        }

        //Set the charging states for the left and right sides depending on the intersection situation
        player.setChargingLeft(player.getLeftChargingBounds().intersects(obstacle.getRightChargingBounds()) || player.getIsChargingLeft());
        player.setChargingRight(player.getRightChargingBounds().intersects(obstacle.getLeftChargingBounds()) || player.getIsChargingRight());

        //Set the scratching states for the left and right sides depending on the intersection situation
        player.setScratchingLeft(player.getLeftChargingBounds().intersects(obstacle.getRightScratchingBounds()) || player.getIsScratchingLeft());
        player.setScratchingRight(player.getRightChargingBounds().intersects(obstacle.getLeftScratchingBounds()) || player.getIsScratchingRight());

    }

    //Useless method
    public static int reverseClamp(int val, int min, int max) {
        if (val < min || val > max)
            return val;
        else if (Math.abs(val - min) > Math.abs(max - val))
            return max;
        else if (Math.abs(val - min) < Math.abs(max - val))
            return min;
        return val;
    }

    //Description: performs the according end-of-game procedures
    //Parameters: the distance the speeder travelled, the seed of the game, and the death method of the player
    //Return: void
    public static void gameOver(int distance, int seed, int deathMethod) {

        //Verifies whether the current game is worthy of a leaderboard entry and moves to the according game state
        if (leaderboard.isOnLeaderboard(distance)) {
            menu.setTempLeaderboard(distance, seed, deathMethod);
            gameState = state.LeaderboardEntry;
        } else {
            gameState = state.GameOver;
        }
    }

    //Description: performs the according game-resetting procedures for the next round
    //Parameters: none
    //Return: void
    public void reset() {

        System.out.println("Seed: " + seed);

        //Reset the seed for the GameOrganizer
        gameOrganizer.setSeed(seed);
        gameOrganizer.reset();

        //Resets the seed for the environment
        environment.reset(seed);

        //Reset the player position and velocity
        player.setY(HEIGHT * 2);
        player.setX(-16);
        player.setVelX(0);
        player.setVelY(-10);

        //Ensures the camera is locked onto the player at the start of the next game
        camera.tick();

        //Reset pursuer distance
        pursuer.setDistance(10000);

        //Reset speeder distance travelled and charge
        Stats.speederDistance = 0;
        Stats.CHARGE = 0;

        //Reset the keyboard states
        keyInput.reset();
    }

    //Smoother graphics
    public void update(Graphics g) {
        paint(g);
    }

    @Override
    public void paintComponent(Graphics g) {
        //super.paintComponent(g);
//        g = bs.getDrawGraphics();

//        g.setColor(new Color(59, 56, 53));
//        g.fillRect(0, 0, WIDTH + 15, HEIGHT + 15);
//
//        if (gameState == state.Game || gameState == state.Pause) {
//            ghandler.render(g);
//            handler.render(g);
//            ahandler.render(g);
//            environment.render(g);
//            hud.render(g);
//            pursuer.render(g);
//        }
//        menu.render(g);
//        g.dispose();

        //Set up the offscreen buffer the first time paint() is called
        if (offScreenBuffer == null) {
            offScreenImage = createImage(WIDTH, HEIGHT);
            offScreenBuffer = offScreenImage.getGraphics();
        }

        //Clear the offScreenBuffer
        offScreenBuffer.clearRect(0, 0, this.getWidth(), this.getHeight());

        //    super.paintComponent(g);

        //Painting the background
        offScreenBuffer.setColor(new Color(59, 56, 53));
        offScreenBuffer.fillRect(0, 0, WIDTH + 15, HEIGHT + 15);

        //Render objects to the off-screen buffer
        //Only render certain objects in certain designated game states to avoid inefficient use of resources
        if (gameState == state.Game || gameState == state.Pause) {

            //Draw zone warning
            int nextType = gameOrganizer.getTempType();
            long zoneCount = gameOrganizer.getCurrCounter();

            if ((Stats.speederDistance + 8500) / 17000 == zoneCount) {

                offScreenBuffer.setColor(Color.white);
                offScreenBuffer.setFont(new Font("Courier New", Font.PLAIN, 25));
                offScreenBuffer.drawString("Incoming Zone: " + (nextType == 1 ? "Sand Spear Nest" : "Diamond Swarm"), 140, 150);

                offScreenBuffer.setFont(new Font("Courier New", Font.PLAIN, 16));
                offScreenBuffer.drawString("Total Zone Travelled: " + zoneCount, 140, 180);
            }

            ghandler.render(offScreenBuffer);
            handler.render(offScreenBuffer);
            ahandler.render(offScreenBuffer);
//            environment.render(offScreenBuffer);
            hud.render(offScreenBuffer);
            pursuer.render(offScreenBuffer);
        }
//        if (gameState == state.LeaderboardEntry) {
//            textField.setVisible(true);
//        }
//        else
//            textField.setVisible(false);

        //Render the menu
        menu.render(offScreenBuffer);

        //Transfer the offScreenBuffer to the screen
        g.drawImage(offScreenImage, 0, 0, WIDTH, HEIGHT, this);

        drawRuler(g);
        g.dispose();
    }

    //Description: updates all components of the game
    //Parameters: none
    //Return: void
    private void tick() throws IOException {

        //Ensure according game audio is playing
        if (gameState == state.Game || gameState == state.Pause) {
            if (menuMusic.isActive()) {
                menuMusic.stop();
            }
            if (!inGameMusic.isActive() && isGameMusic) {
                inGameMusic.setFramePosition(0);
                inGameMusic.start();
            } else if (!isGameMusic) {
                inGameMusic.stop();
            }
        } else {
            if (inGameMusic.isActive()) {
                inGameMusic.stop();
            }
            if (!menuMusic.isActive() && isMenuMusic) {
                menuMusic.setFramePosition(0);
                menuMusic.start();
            } else if (!isMenuMusic) {
                menuMusic.stop();
            }
            player.stopSounds();
            pursuer.stopSounds();
//            if (inGameMusic.isActive() || !menuMusic.isActive()) {
//                menuMusic.setFramePosition(0);
//                menuMusic.start();
//                inGameMusic.stop();
////                charging.stop();
////                scratching.stop();
////                collision.stop();
//                player.stopSounds();
//                pursuer.stopSounds();
//            }
        }
//        if (!isMenuMusic && menuMusic.isActive()) {
//            menuMusic.stop();
//        }
//        if (!isGameMusic && inGameMusic.isActive()) {
//            inGameMusic.stop();
//        }

        //Updates game components
        menu.tick();

        //Only update certain components in certain designated game states to avoid unwanted bugs and inefficient use
        // of resources
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

        //Increase time elapsed and frame count
        timeElapsed = System.currentTimeMillis() - startTime;
        frameCount++;

    }

    //Require removal
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

    //Description: starts the game thread
    //Parameters: none
    //Return: void
    public synchronized void start() {

        //Start the thread and ensure the game-loop can be entered
        thread = new Thread(this);
        thread.start();
        running = true;
        startTime = System.currentTimeMillis();
        timeElapsed = 0;
    }

    //Description: stops the game thread
    //Parameters: none
    //Return: void
    public synchronized void stop() {

        //Stop the thread and end the game
        try {
            thread.join();
            running = false;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //Description: main game loop
    //Parameters: none
    //Return: void
    public void run() {

        //Game loop
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
            //Output the average FPS of the game
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

    public void setSeed(int seed) {
        this.seed = seed;
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

    public boolean getGameEffect() {
        return isGameEffect;
    }

    public boolean getGameMusic() {
        return isGameMusic;
    }

    public boolean getMenuMusic() {
        return isMenuMusic;
    }

    public void setGameEffect(boolean gameEffect) {
        isGameEffect = gameEffect;
    }

    public void setGameMusic(boolean gameMusic) {
        isGameMusic = gameMusic;
    }

    public void setMenuMusic(boolean menuMusic) {
        isMenuMusic = menuMusic;
    }
}
