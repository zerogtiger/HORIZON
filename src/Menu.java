import java.util.*;
import java.io.*;
import java.awt.*;
import java.awt.event.*;

public class Menu implements MouseListener {
    private Handler handler;
    private Game game;
    private Leaderboard leaderboard;
    private Button[][] buttons; //menuButtons, gameOverButtons, leaderboardButtons, optionsButtons;
    private int focus, lastFocus, menuScreen = menuCode.get(Game.gameState);
    ;
    private int lmx, lmy;
    private boolean[] lastKeyboardState = new boolean[3]; // 0 for up, 1 for down, 2 for enter.
    private static HashMap<Game.state, Integer> menuCode = new HashMap<>() {
        {
            put(Game.state.Menu, 0);
            put(Game.state.GameOver, 1);
            put(Game.state.Leaderboard, 2);
            put(Game.state.Options, 3);
        }
    };

    public Menu(Game game, Leaderboard leaderboard, Handler handler) {
        focus = 0;
        lastFocus = 0;
        lmx = 0;
        lmy = 0;
        this.game = game;
        this.leaderboard = leaderboard;
        this.handler = handler;
        buttons = new Button[4][];
        buttons[0] = new Button[]{
                new Button("Start", 90, Game.HEIGHT - 220 + 21, 200, 27, game),
                new Button("Options", 90, Game.HEIGHT - 180 + 21, 200, 27, game),
                new Button("Leaderboard", 90, Game.HEIGHT - 140 + 21, 200, 27, game),
                new Button("Quit", 90, Game.HEIGHT - 100 + 21, 200, 27, game),
        };
        buttons[1] = new Button[]{
                new Button("Menu", 110, Game.HEIGHT - 240, 200, 27, game),
                new Button("Reload", 110, Game.HEIGHT - 200, 200, 27, game)
        };
        buttons[2] = new Button[]{
                new Button("Back", 60, Game.HEIGHT - 45, 200, 27, game),
        };
        buttons[3] = new Button[]{
                new Button("Back", 60, Game.HEIGHT - 50, 200, 27, game),
        };

    }

    public void buttonEntered() {
        if (game.gameState == Game.state.Menu) {
            if (focus == 0) {
                game.gameState = Game.state.Game;
                game.reset();
                System.out.println("Game Reset");
            } else if (focus == 1) {
                focus = 0;
                game.gameState = Game.state.Options;
            } else if (focus == 2) {
                focus = 0;
                game.gameState = Game.state.Leaderboard;
            } else if (focus == 3) {
                System.exit(0);
            }
        } else if (game.gameState == Game.state.GameOver) {
            if (focus == 0) {
                game.gameState = Game.state.Menu;
            } else if (focus == 1) {
                game.gameState = Game.state.Game;
                game.reset();
                System.out.println("Game Reset");
            }
        } else if (game.gameState == Game.state.Leaderboard) {
            if (focus == 0) {
                game.gameState = Game.state.Menu;
            }
        } else if (game.gameState == Game.state.Options) {
            if (focus == 0) {
                game.gameState = Game.state.Menu;
            }
        }
    }

    public void buttonEntered(int mx, int my) {
        if (game.gameState == Game.state.Menu) {
            if (buttons[0][0].isOver(mx, my)) {
                game.gameState = Game.state.Game;
                game.reset();
                System.out.println("Game Reset");
            } else if (buttons[0][1].isOver(mx, my)) {
                focus = 0;
                game.gameState = Game.state.Options;
            } else if (buttons[0][2].isOver(mx, my)) {
                focus = 0;
                game.gameState = Game.state.Leaderboard;
            } else if (buttons[0][3].isOver(mx, my)) {
                System.exit(0);
            }
        } else if (game.gameState == Game.state.GameOver) {
            if (buttons[1][0].isOver(mx, my)) {
                focus = 0;
                game.gameState = Game.state.Menu;
            } else if (buttons[1][1].isOver(mx, my)) {
                game.gameState = Game.state.Game;
                game.reset();
                System.out.println("Game Reset");
            }
        } else if (game.gameState == Game.state.Leaderboard) {
            if (buttons[2][0].isOver(mx, my)) {
                focus = 0;
                game.gameState = Game.state.Menu;
            }
        } else if (game.gameState == Game.state.Options) {
            if (buttons[3][0].isOver(mx, my)) {
                focus = 0;
                game.gameState = Game.state.Menu;
            }
        }
    }

    public void mouseClicked(MouseEvent e) {

    }

    public void mousePressed(MouseEvent e) {
        int mx = e.getX();
        int my = e.getY();
        buttonEntered(mx, my);
    }

    public void mouseReleased(MouseEvent e) {

    }

    public void mouseEntered(MouseEvent e) {

    }

    public void mouseExited(MouseEvent e) {

    }

    private boolean mouseOver(int mx, int my, int x, int y, int width, int height) {
        return (mx >= x && mx <= x + width && my >= y && my <= y + height);
    }

    public void tick() {
        Point p = MouseInfo.getPointerInfo().getLocation();
        int mx = (int) (p.getX() - game.getLocationOnScreen().getX());
        int my = (int) (p.getY() - game.getLocationOnScreen().getY());
        menuScreen = menuCode.get(Game.gameState);
        //If mouse moved
        if (lmx != mx || lmy != my) {
            for (int i = 0; i < buttons[menuScreen].length; i++) {
                if (buttons[menuScreen][i].isOver(mx, my)) {
                    focus = i;
                }
            }
            lmx = mx;
            lmy = my;
        }
        //If a key is pressed
        if ((lastKeyboardState[0] != Stats.getKeyPress()[0][3] || lastKeyboardState[1] != Stats.getKeyPress()[0][4])
                || lastKeyboardState[2] != Stats.getKeyPress()[0][5]) {
            //If ENTER is either pressed or lifted && is currently pressed
            if (lastKeyboardState[2] != Stats.getKeyPress()[0][5] && Stats.getKeyPress()[0][5]) {
                buttonEntered();
            }
            //If UP is either pressed or lifted && is currently pressed
            else if (lastKeyboardState[0] != Stats.getKeyPress()[0][3] && Stats.getKeyPress()[0][3]) {
                focus = (focus + buttons[menuScreen].length - 1) % buttons[menuScreen].length;
            }
            //If DOWN is either pressed or lifted && is currently pressed
            else if (lastKeyboardState[1] != Stats.getKeyPress()[0][4] && Stats.getKeyPress()[0][4]) {
                focus = (focus + 1) % buttons[menuScreen].length;
            }
            //Must check if state changed for computer to read the actively changing key
            //Update all keyboard states
            lastKeyboardState[0] = Stats.getKeyPress()[0][3];
            lastKeyboardState[1] = Stats.getKeyPress()[0][4];
            lastKeyboardState[2] = Stats.getKeyPress()[0][5];
        }

        //Require reset of focus for each menu change
        for (int i = 0; i < buttons[menuScreen].length; i++) {
            buttons[menuScreen][i].setFocused(false);
        }
        buttons[menuScreen][focus].setFocused(true);

    }

    public void render(Graphics g) {
        Font courier = new Font("Courier New", Font.PLAIN, 18);
        Font courier2 = new Font("Courier New", Font.BOLD, 60);

        if (game.gameState == Game.state.Menu) {
            g.setColor(Color.white);
            g.setFont(courier);
            g.drawImage(Toolkit.getDefaultToolkit().getImage("pics/mainBackground2.png"), 0, 0, 400 * 3, 225 * 3, game);
            g.drawImage(Toolkit.getDefaultToolkit().getImage("pics/title.png"), (Game.WIDTH - 310 * 3) / 2, 270, 310 * 3, 24 * 3, game);
        }
        if (game.gameState == Game.state.GameOver) {
            g.setFont(courier2);
            g.setColor(new Color(219, 198, 191, 50));
            g.fillRect(70, 70, Game.WIDTH - 140, Game.HEIGHT - 140);
            g.drawImage(Toolkit.getDefaultToolkit().getImage("pics/gameOver.png"), 110, 110, 138 * 3, 42 * 3, game);
            g.setColor(Color.white);
            g.drawImage(Toolkit.getDefaultToolkit().getImage("pics/distance.png"), 110, 300, 83 * 3, 5 * 3, game);
            g.drawString(String.valueOf(Stats.speederDistance), 110, 380);
        }
        if (game.gameState == Game.state.Leaderboard) {
            g.setFont(courier2);
            g.setColor(new Color(0, 0, 0, 200));
            g.drawImage(Toolkit.getDefaultToolkit().getImage("pics/leaderboardBackground.png"), 0, 0, Game.WIDTH, Game.HEIGHT, game);
            g.drawImage(Toolkit.getDefaultToolkit().getImage("pics/leaderboard.png"), 60, 40, 318 * 3, 15 * 3, game);
            leaderboard.render(g);
        }
        if (game.gameState == Game.state.Options) {
            g.drawImage(Toolkit.getDefaultToolkit().getImage("pics/optionsBackground.png"), 0, 0, Game.WIDTH, Game.HEIGHT, game);
        }
        for (Button b : buttons[menuScreen]) {
            b.render(g);
        }
    }

    public void setFocus(int focus) {
        this.focus = focus;
    }
}
