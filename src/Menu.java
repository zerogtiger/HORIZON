/*

Description:

Main menu system for the game. Renders menu components based on the current game state and interacts with user
accordingly. Includes buttons for the menu screens via a 2D ragged array, textfields for user entry, and verifies
validity of inputted text. Responsible for setting the focus for the buttons according to keyboard presses and/or mouse
movement, rendering the various screens, including the lead screen, and for functionalities when buttons are pressed by
the user.

*/

import javax.swing.*;
import java.util.*;
import java.io.*;
import java.awt.*;
import java.awt.event.*;

public class Menu implements MouseListener {

    //Various game components to be referenced to
    private Handler handler;
    private Game game;
    private Leaderboard leaderboard;

    //Ragged array of buttons for each game state
    private Button[][] buttons;

    //HashMap to map game states to integer values for ease of referencing
    private static HashMap<Game.state, Integer> menuCode = new HashMap<>() {
        {
            put(Game.state.Menu, 0);
            put(Game.state.GameOver, 1);
            put(Game.state.Leaderboard, 2);
            put(Game.state.Options, 3);
            put(Game.state.LeaderboardEntry, 4);
            put(Game.state.Pause, 5);
            put(Game.state.Game, 6);
            put(Game.state.Lead, 7);
            put(Game.state.GameReady, 8);
            put(Game.state.Tutorial, 9);
        }
    };

    //Button focused, current screen, iterator to assist in displaying animations, respectively
    private int focus, menuScreen = menuCode.get(Game.gameState), iterator;

    //Textfields for leaderboard name entry and seed entry, respectively
    private TextField leaderboardEntryTextField, seedEntryTextField;

    //Temporary leaderboard entry to display when entering leaderboard name
    private LeaderboardEntry tempLeaderboardEntry;

    //Whether the name is valid
    private boolean isInvalid;

    //Last mouse coordinates
    private int lmx, lmy;

    //Last keyboard states
    private boolean[] lastKeyboardState = new boolean[4]; // 0 for up, 1 for down, 2 for enter, 4 for esc.

    //Constructor
    public Menu(Game game, Leaderboard leaderboard, Handler handler) {

        //Initialize variables
        focus = 0;
        lmx = 0;
        lmy = 0;
        iterator = 0;
        isInvalid = false;
        this.game = game;
        this.leaderboard = leaderboard;
        this.handler = handler;
        leaderboardEntryTextField = new TextField("Player");
        seedEntryTextField = new TextField(String.format("%06d", game.getSeed()));

        //Add textfields to the game
        game.setLayout(null);
        game.add(leaderboardEntryTextField);
        game.add(seedEntryTextField);

        //Set up textfields
        leaderboardEntryTextField.setBounds(262, Game.HEIGHT - 220, 400, 25);
        leaderboardEntryTextField.setBackground(new Color(255, 255, 255));
        leaderboardEntryTextField.setFont(new Font("Monospaced", Font.BOLD, 18));
        leaderboardEntryTextField.setVisible(false);

        seedEntryTextField.setBounds(352, Game.HEIGHT - 280, 82, 27);
        seedEntryTextField.setBackground(new Color(255, 255, 255));
        seedEntryTextField.setFont(new Font("Monospaced", Font.BOLD, 21));
        seedEntryTextField.setVisible(false);

        //Buttons to be displayed for all game states
        buttons = new Button[10][];
        buttons[0] = new Button[]{
                new Button("Start Game", 90, Game.HEIGHT - 270 + 21, 200, 27, game),
                new Button("Tutorial", 90, Game.HEIGHT - 230 + 21, 200, 27, game),
                new Button("Options", 90, Game.HEIGHT - 190 + 21, 200, 27, game),
                new Button("Quit", 90, Game.HEIGHT - 150 + 21, 200, 27, game),
        };
        buttons[1] = new Button[]{
                new Button("Reload", 820, 420, 200, 27, game),
                new Button("Menu", 820, 460, 200, 27, game),
        };
        buttons[2] = new Button[]{
                new Button("Back", 60, Game.HEIGHT - 40, 200, 27, game),
        };
        buttons[3] = new Button[]{
                new Button("Toggle", 550, 228, 200, 27, game),
                new Button("Toggle", 550, 288, 200, 27, game),
                new Button("Toggle", 550, 348, 200, 27, game),
                new Button("Back", 60, Game.HEIGHT - 50, 200, 27, game),
        };
        buttons[4] = new Button[]{
                new Button("Enter", 850, Game.HEIGHT - 220, 200, 27, game),
                new Button("Skip", 850, Game.HEIGHT - 180, 200, 27, game),
        };
        buttons[5] = new Button[]{
                new Button("Resume", 850, Game.HEIGHT - 220, 200, 27, game),
                new Button("Menu", 850, Game.HEIGHT - 180, 200, 27, game),
        };
        buttons[6] = new Button[]{
                new Button("Pause", 960, Game.HEIGHT - 40, 200, 27, game),
        };
        buttons[7] = new Button[]{
                new Button("Skip", 970, Game.HEIGHT - 40, 200, 27, game),
        };
        buttons[8] = new Button[]{
                new Button("Start", 122, Game.HEIGHT - 280, 200, 27, game),
                new Button("Set Seed", 122, Game.HEIGHT - 240, 200, 27, game),
                new Button("Leaderboard", 122, Game.HEIGHT - 200, 200, 27, game),
                new Button("Back", 122, Game.HEIGHT - 160, 200, 27, game),
        };
        buttons[9] = new Button[]{
                new Button("Back", 60, Game.HEIGHT - 50, 200, 27, game),
        };

    }

    //Description: set up the dummy leaderboard and set the name entry textfield to visible in the LeaderboardEntry screen
    //Parameters: the distance the speeder travelled, the seed of the game, and the death method of the player
    //Return: void
    public void setTempLeaderboard(int distance, int seed, int deathMethod) {

        //Set up components in the LeaderboardEntry screen
        tempLeaderboardEntry = new LeaderboardEntry(distance, seed, deathMethod, 150, Game.HEIGHT - 270);
        leaderboardEntryTextField.setVisible(true);
    }

    //Description: sets the focus for the buttons depending on the mouse and/or keyboard actions
    //Parameters: none
    //Return: void
    public void tick() throws IOException {

        //Forward iterator
        iterator = (iterator + 1) % 1080;

        //Track mouse position
        Point p = MouseInfo.getPointerInfo().getLocation();
        int mx = (int) (p.getX() - game.getLocationOnScreen().getX());
        int my = (int) (p.getY() - game.getLocationOnScreen().getY());

        //Get current screen integer reference
        menuScreen = menuCode.get(Game.gameState);

        //Update the button focus for the current screen if the mouse moved
        if (lmx != mx || lmy != my) {
            for (int i = 0; i < buttons[menuScreen].length; i++) {
                if (buttons[menuScreen][i].isOver(mx, my)) {
                    focus = i;
                }
            }
            lmx = mx;
            lmy = my;
        }

        //Update the button focus for the current screen if a key is pressed
        if ((lastKeyboardState[0] != Stats.getKeyPress()[0][3] || lastKeyboardState[1] != Stats.getKeyPress()[0][4])
                || lastKeyboardState[2] != Stats.getKeyPress()[0][5] || lastKeyboardState[3] != Stats.getKeyPress()[0][6]) {

            //Perform the button's functionality if ENTER is either pressed or lifted && is currently pressed
            if (lastKeyboardState[2] != Stats.getKeyPress()[0][5] && Stats.getKeyPress()[0][5]) {
                buttonEntered();
            }

            //Shift the focus upwards cyclically if UP is either pressed or lifted && is currently pressed
            else if (lastKeyboardState[0] != Stats.getKeyPress()[0][3] && Stats.getKeyPress()[0][3]) {
                focus = (focus + buttons[menuScreen].length - 1) % buttons[menuScreen].length;
            }

            //Shift the focus downwards cyclically if DOWN is either pressed or lifted && is currently pressed
            else if (lastKeyboardState[1] != Stats.getKeyPress()[0][4] && Stats.getKeyPress()[0][4]) {
                focus = (focus + 1) % buttons[menuScreen].length;
            }

            //Enter the Pause screen if the ESCAPE key is pressed
            else if (lastKeyboardState[3] != Stats.getKeyPress()[0][6] && Stats.getKeyPress()[0][6] && Game.gameState == Game.state.Game) {
                Game.gameState = Game.state.Pause;
            }

            //Must check if state changed for computer to read the actively changing key

            //Update all keyboard states
            lastKeyboardState[0] = Stats.getKeyPress()[0][3];
            lastKeyboardState[1] = Stats.getKeyPress()[0][4];
            lastKeyboardState[2] = Stats.getKeyPress()[0][5];
            lastKeyboardState[3] = Stats.getKeyPress()[0][6];
        }

        //Reset the focus for the current screen
        for (int i = 0; i < buttons[menuScreen].length; i++) {
            buttons[menuScreen][i].setFocused(false);
        }
        buttons[menuScreen][focus].setFocused(true);

        //Update the name displayed on the leaderboard if on LeaderboardEntry screen
        if (Game.gameState == Game.state.LeaderboardEntry) {

            //A few spaces added to avoid StringIndexOutOfBoundsException when cutting the string as it is edited
            //               |
            tempLeaderboardEntry.setName(removeSpaces((removeSpaces(leaderboardEntryTextField.getText()) + "   ").substring(0, Math.min(16, removeSpaces(leaderboardEntryTextField.getText()).length()))));
            //               |_______________________________________________________________________________^
        }

        //Set the seed entry textfield's visibility depending on the current game state
        if (Game.gameState != Game.state.GameReady) {
            seedEntryTextField.setVisible(false);
        }

        //Read the seed and report validity
        else if (seedEntryTextField.isVisible()) {
            isInvalid = !isSeedValid(seedEntryTextField.getText());
        }
    }

    //Description: renders the menu depending on the current screen
    //Parameters: the Graphics object to draw the components
    //Return: void
    public void render(Graphics g) {

        //Fonts to be used
        Font courier = new Font("Courier New", Font.PLAIN, 18);
        Font courier2 = new Font("Courier New", Font.BOLD, 60);

        //Render different components depending on the game state

        //Main menu:
        if (game.gameState == Game.state.Menu) {
            g.drawImage(Toolkit.getDefaultToolkit().getImage("appdata/images/menuBackground/mainBackground2.png"), 0, 0, 400 * 3, 225 * 3, game);
            g.drawImage(Toolkit.getDefaultToolkit().getImage("appdata/images/text/title.png"), (Game.WIDTH - 380 * 3) / 2, 270, 380 * 3, 20 * 3, game);

            g.setColor(Color.LIGHT_GRAY);
            g.setFont(new Font("Consolas", Font.PLAIN, 12));
            g.drawString("Tiger Ding: Art | Design | Program | Music", 10, Game.HEIGHT-23);

            g.setColor(Color.LIGHT_GRAY);
            g.setFont(new Font("Consolas", Font.PLAIN, 10));
            g.drawString("Special thanks to PikPok for inspirations.", 10, Game.HEIGHT-10);
        }

        //Game over:
        else if (game.gameState == Game.state.GameOver) {

            g.drawImage(Toolkit.getDefaultToolkit().getImage("appdata/images/menuBackground/menuBackground.png"), 0, 0, Game.WIDTH, Game.HEIGHT, game);

            g.setFont(courier2);
            g.setColor(new Color(35, 35, 35, 199));
            g.fillRect(120, 140, Game.WIDTH - 240, Game.HEIGHT - 280);

            g.drawImage(Toolkit.getDefaultToolkit().getImage("appdata/images/text/gameOver.png"), 155, 170, 138 * 3, 42 * 3, game);
            g.drawImage(Toolkit.getDefaultToolkit().getImage("appdata/images/text/distance.png"), 155, 380, 83 * 3, 5 * 3, game);

            g.setColor(Color.white);
            g.drawString(String.valueOf(Stats.speederDistance), 155, 460);
        }

        //Leaderboard:
        else if (game.gameState == Game.state.Leaderboard) {

            g.drawImage(Toolkit.getDefaultToolkit().getImage("appdata/images/menuBackground/leaderboardBackground.png"), 0, 0, Game.WIDTH, Game.HEIGHT, game);
            g.drawImage(Toolkit.getDefaultToolkit().getImage("appdata/images/text/leaderboard.png"), 60, 40, 318 * 3, 15 * 3, game);

            leaderboard.render(g);
        }

        //Options:
        else if (game.gameState == Game.state.Options) {

//            new Button("Toggle", 120, 150, 200, 27, game),
//                    new Button("Toggle", 120, 190, 200, 27, game),
//                    new Button("Toggle", 120, 230, 200, 27, game),
//
            g.drawImage(Toolkit.getDefaultToolkit().getImage("appdata/images/menuBackground/optionsBackground.png"), 0, 0, Game.WIDTH, Game.HEIGHT, game);

            //Game music options
            g.setColor(Color.white);
            g.setFont(new Font("Courier New", Font.PLAIN, 25));
            g.drawString("Menu Background Music: ", 120, 250);
            g.drawString("Game Background Music: ", 120, 310);
            g.drawString("In-Game Sound Effects: ", 120, 370);

            g.setFont(new Font("Courier New", Font.BOLD, 25));
            g.setColor((game.getMenuMusic() ? Color.white : Color.lightGray));
            g.drawString(String.format("%3s", (game.getMenuMusic() ? "ON" : "OFF")), 460, 250);
            g.setColor((game.getGameMusic() ? Color.white : Color.lightGray));
            g.drawString(String.format("%3s", (game.getGameMusic() ? "ON" : "OFF")), 460, 310);
            g.setColor((game.getGameEffect() ? Color.white : Color.lightGray));
            g.drawString(String.format("%3s", (game.getGameEffect() ? "ON" : "OFF")), 460, 370);
        }

        //Leaderboard entry:
        else if (game.gameState == Game.state.LeaderboardEntry) {

            g.drawImage(Toolkit.getDefaultToolkit().getImage("appdata/images/menuBackground/menuBackground.png"), 0, 0, Game.WIDTH, Game.HEIGHT, game);

            g.setColor(new Color(35, 35, 35, 199));
            g.fillRect(120, 140, Game.WIDTH - 240, Game.HEIGHT - 280);

            g.drawImage(Toolkit.getDefaultToolkit().getImage("appdata/images/text/gameOver.png"), 155, 170, 138 * 3, 42 * 3, game);

            g.setColor(Color.white);
            g.setFont(new Font("Courier New", Font.PLAIN, 25));
            g.drawString("Congratulations! You have entered the leaderboard!", 150, Game.HEIGHT - 320);

            g.setFont(new Font("Courier New", Font.PLAIN, 18));
            g.drawString("Enter your name below (16 character max): ", 150, Game.HEIGHT - 290);

            if (isInvalid) {
                g.setColor(Color.red);
                g.setFont(new Font("Courier New", Font.BOLD, 18));

                g.drawString("<E> Invalid Name.", 270, Game.HEIGHT - 170);
            }

            tempLeaderboardEntry.render(g);
        }

        //Pause menu:
        else if (game.gameState == Game.state.Pause) {

            g.setColor(new Color(35, 35, 35, 199));
            g.fillRect(120, 140, Game.WIDTH - 240, Game.HEIGHT - 280);

            g.drawImage(Toolkit.getDefaultToolkit().getImage("appdata/images/text/gamePaused.png"), 155, 170, 189 * 3, 40 * 3, game);
            g.drawImage(Toolkit.getDefaultToolkit().getImage("appdata/images/text/distance.png"), 155, 360, 83 * 3, 5 * 3, game);

            g.setColor(Color.white);
            g.setFont(courier2);
            g.drawString(String.valueOf(Stats.speederDistance), 155, 440);
        }

        //Main game:
        else if (game.gameState == Game.state.Game) {
            g.setColor(new Color(35, 35, 35, 199));
            g.fillRect(943, Game.HEIGHT - 45, 234, 37);
        }

        //Lead:
        else if (game.gameState == Game.state.Lead) {

            //Images that will be used
            Image[] leadImages = {
                    Toolkit.getDefaultToolkit().getImage("appdata/images/lead/E1.png"),
                    Toolkit.getDefaultToolkit().getImage("appdata/images/lead/E2.png"),
                    Toolkit.getDefaultToolkit().getImage("appdata/images/lead/E3.png"),
                    Toolkit.getDefaultToolkit().getImage("appdata/images/lead/C1.png"),
                    Toolkit.getDefaultToolkit().getImage("appdata/images/lead/C2.png"),
                    Toolkit.getDefaultToolkit().getImage("appdata/images/lead/C3.png"),
                    Toolkit.getDefaultToolkit().getImage("appdata/images/lead/C4.png")
            };

            //Switch to main menu if the number of frames exceeds designated range
            if (iterator >= 720)
                game.gameState = Game.state.Menu;
            else {

                //Main menu background to smooth transition
                g.drawImage(Toolkit.getDefaultToolkit().getImage("appdata/images/menuBackground/mainBackground2.png"), 0, 0, 400 * 3, 225 * 3, game);
                g.drawImage(Toolkit.getDefaultToolkit().getImage("appdata/images/text/title.png"), (Game.WIDTH - 380 * 3) / 2, 270, 380 * 3, 20 * 3, game);

                //Changes opacity of background near the end of the lead
                g.setColor(new Color(0, 0, 0, (int) ((1 - Math.min(Math.max(0, (iterator - 580) / 140.0), 1)) * 255)));
                g.fillRect(0, 0, Game.WIDTH, Game.HEIGHT);

                Graphics2D g2d = (Graphics2D) g;

                //First line
                float opacity = (float) (iterator >= 480 ? 1 - Math.min(Math.max(0, (iterator - 580) / 140.0), 1) : Math.min(iterator / 60.0, 1));
                g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, opacity));
                g2d.drawImage(leadImages[0], 200, 75 - 20, 177 * 3, 15 * 3, null);
                g2d.drawImage(leadImages[3], 200, 320 - 20, 171 * 3, 15 * 3, null);

                //Second line
                opacity = (float) (iterator >= 480 ? 1 - Math.min(Math.max(0, (iterator - 580) / 140.0), 1) : Math.min((Math.max(0, (iterator - 120) / 60.0)), 1));
                g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, opacity));
                g2d.drawImage(leadImages[1], 200, 150 - 20, 212 * 3, 15 * 3, null);
                g2d.drawImage(leadImages[4], 200, 395 - 20, 281 * 3, 15 * 3, null);

                //Third line
                opacity = (float) (iterator >= 480 ? 1 - Math.min(Math.max(0, (iterator - 580) / 140.0), 1) : Math.min((Math.max(0, (iterator - 240) / 60.0)), 1));
                g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, opacity));
                g2d.drawImage(leadImages[2], 200, 225 - 20, 149 * 3, 15 * 3, null);
                g2d.drawImage(leadImages[5], 200, 470 - 20, 200 * 3, 15 * 3, null);

                //Fourth line
                opacity = (float) (iterator >= 480 ? 1 - Math.min(Math.max(0, (iterator - 580) / 140.0), 1) : Math.min((Math.max(0, (iterator - 360) / 60.0)), 1));
                g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, opacity));
                g2d.drawImage(leadImages[6], 200, 545, 286 * 3, 15 * 3, null);
                g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1));
            }
        }

        //Game ready:
        else if (game.gameState == Game.state.GameReady) {

            g.drawImage(Toolkit.getDefaultToolkit().getImage("appdata/images/menuBackground/gameReadyBackground.png"), 0, 0, 400 * 3, 225 * 3, game);
            g.drawImage(Toolkit.getDefaultToolkit().getImage("appdata/images/text/gameReady.png"), 110, 250, 128 * 3, 30 * 3, game);

            if (isInvalid && seedEntryTextField.isVisible()) {
                g.setColor(Color.red);
                g.setFont(new Font("Courier New", Font.BOLD, 20));
                g.drawString("<E> Invalid Seed.", 452, Game.HEIGHT - 262);
            }
        }

        //Tutorial:
        else if (game.gameState == Game.state.Tutorial) {

            g.drawImage(Toolkit.getDefaultToolkit().getImage("appdata/images/menuBackground/tutorialBackground.png"), 0, 0, 400 * 3, 225 * 3, game);
            g.drawImage(Toolkit.getDefaultToolkit().getImage("appdata/images/text/instructions.png"), 120, 110, 207 * 3, 10 * 3, game);

            //Instructions text
            g.setColor(Color.white);
            g.setFont(new Font("Courier New", Font.PLAIN, 18));

            int sx = 130, sy = 200, lineSpace = 25;

            g.drawString("Master your speed through mystical landscapes as you escape from a", sx, sy);
            g.drawString("mysterious pursuer. Dodge obstacles to avoid collisions and draw close", sx, sy + lineSpace);
            g.drawString("alongside them to obtain charges to accelerate and outrun the pursuer.", sx, sy + 2 * lineSpace);
            g.drawString("The HORIZON is ahead.", sx, sy + 3 * lineSpace);

            g.setFont(new Font("Courier New", Font.BOLD, 23));
            g.drawString("Menu Navigation:", sx, sy + 5 * lineSpace);

            g.setFont(new Font("Courier New", Font.PLAIN, 18));
            g.drawString("Use either the mouse or arrow keys to navigate the game menu. Left", sx, sy + 7 * lineSpace);
            g.drawString("click on the button or press ENTER on the keyboard to select.", sx, sy + 8 * lineSpace);

            g.setFont(new Font("Courier New", Font.BOLD, 23));
            g.drawString("In-game operations:", sx, sy + 10 * lineSpace);

            g.setFont(new Font("Courier New", Font.PLAIN, 18));
            g.drawString("Use the left and right-arrow keys to control the direction of the", sx, sy + 12 * lineSpace);
            g.drawString("speeder. Press the space-bar, up-arrow, or the left and right-arrow keys", sx, sy + 13 * lineSpace);
            g.drawString("simultaneously to accelerate should the speeder have sufficient charge.", sx, sy + 14 * lineSpace);
        }


        //Renders the buttons for each screen
        for (Button b : buttons[menuScreen]) {
            b.render(g);
        }
    }

    //Description: performs according actions depending on the current screen and button entered/pressed
    //Parameters: none
    //Return: void
    public void buttonEntered() throws IOException {

        //Perform various actions depending on the screen and focus of the button

        //Main menu:
        if (game.gameState == Game.state.Menu) {
            if (focus == 0) {
                game.gameState = Game.state.GameReady;
            } else if (focus == 1) {
                focus = 0;
                game.gameState = Game.state.Tutorial;
            } else if (focus == 2) {
                focus = 3;
                game.gameState = Game.state.Options;
            } else if (focus == 3) {
                System.exit(0);
            }
        }

        //Game over:
        else if (game.gameState == Game.state.GameOver) {
            if (focus == 0) {
                game.gameState = Game.state.Game;
                game.reset();
                System.out.println("Game Reset");
            } else if (focus == 1) {
                focus = 0;
                game.gameState = Game.state.GameReady;
            }
        }

        //Leaderboard:
        else if (game.gameState == Game.state.Leaderboard) {
            if (focus == 0) {
                game.gameState = Game.state.GameReady;
            }
        }

        //Options:
        else if (game.gameState == Game.state.Options) {
            if (focus == 0) {
                game.setMenuMusic(!game.getMenuMusic());
            } else if (focus == 1) {
                game.setGameMusic(!game.getGameMusic());
            } else if (focus == 2) {
                game.setGameEffect(!game.getGameEffect());
            } else if (focus == 3) {
                focus = 0;
                game.gameState = Game.state.Menu;
            }
        }

        //Leaderboard entry:
        else if (game.gameState == Game.state.LeaderboardEntry) {
            if (focus == 0) {

                //Checks for validity of leaderboard name and perform according actions
                tempLeaderboardEntry.setName(removeSpaces((removeSpaces(leaderboardEntryTextField.getText()) + "   ").substring(0, Math.min(16, removeSpaces(leaderboardEntryTextField.getText()).length()))));
                if (tempLeaderboardEntry.getName().equals("")) {
                    isInvalid = true;
                } else {
                    leaderboard.add(tempLeaderboardEntry);
                    leaderboardEntryTextField.setVisible(false);
                    leaderboardEntryTextField.setText("Player");
                    isInvalid = false;
                    game.gameState = Game.state.GameOver;
                }
            } else if (focus == 1) {
                focus = 0;
                leaderboardEntryTextField.setVisible(false);
                leaderboardEntryTextField.setText("Player");
                isInvalid = false;
                game.gameState = Game.state.GameOver;
            }
        }

        //Pause menu:
        else if (game.gameState == Game.state.Pause) {
            if (focus == 0) {
                focus = 0;
                Game.gameState = Game.state.Game;
            } else if (focus == 1) {
                focus = 0;
                Game.gameState = Game.state.GameReady;
            }
        }

        //Main game:
        else if (game.gameState == Game.state.Game) {
            if (focus == 0) {
                focus = 0;
                Game.gameState = Game.state.Pause;
            }
        }

        //Lead:
        else if (game.gameState == Game.state.Lead) {
            if (focus == 0) {
                focus = 0;
                Game.gameState = Game.state.Menu;
            }
        }

        //Game ready:
        else if (game.gameState == Game.state.GameReady) {
            if (focus == 0) {

                //Perform various actions based on whether the seed entry textfield is visible
                if (seedEntryTextField.isVisible() && !isInvalid) {
                    game.setSeed(Integer.parseInt(seedEntryTextField.getText()));
                    game.reset();
                    game.gameState = Game.state.Game;
                    System.out.println("Game Reset seed");
                } else if (!seedEntryTextField.isVisible()) {
                    game.gameState = Game.state.Game;
                    game.reset();
                    System.out.println("Game Reset");
                }
            } else if (focus == 1) {
                seedEntryTextField.setVisible(!seedEntryTextField.isVisible());
            } else if (focus == 2) {
                focus = 0;
                game.gameState = Game.state.Leaderboard;
            } else if (focus == 3) {
                focus = 0;
                game.gameState = Game.state.Menu;
            }
        }

        //Tutorial:
        else if (game.gameState == Game.state.Tutorial) {
            if (focus == 0) {
                game.gameState = Game.state.Menu;
            }
        }
    }

    //Description: performs according actions depending on the current screen and button entered/pressed
    //Parameters: the x and y-coordinates of the mouse cursor when the button was pressed
    //Return: void
    public void buttonEntered(int mx, int my) throws IOException {

        //Perform various actions depending on the screen and focus of the button

        //Main menu:
        if (game.gameState == Game.state.Menu) {
            if (buttons[0][0].isOver(mx, my)) {
                focus = 0;
                game.gameState = Game.state.GameReady;
            } else if (buttons[0][1].isOver(mx, my)) {
                focus = 0;
                game.gameState = Game.state.Tutorial;
            } else if (buttons[0][2].isOver(mx, my)) {
                focus = 3;
                game.gameState = Game.state.Options;
            } else if (buttons[0][3].isOver(mx, my)) {
                System.exit(0);
            }
        }

        //Game over:
        else if (game.gameState == Game.state.GameOver) {
            if (buttons[1][0].isOver(mx, my)) {
                focus = 0;
                game.gameState = Game.state.Game;
                game.reset();
                System.out.println("Game Reset");
            } else if (buttons[1][1].isOver(mx, my)) {
                focus = 0;
                game.gameState = Game.state.GameReady;
            }
        }

        //Leaderboard:
        else if (game.gameState == Game.state.Leaderboard) {
            if (buttons[2][0].isOver(mx, my)) {
                focus = 0;
                game.gameState = Game.state.GameReady;
            }
        }

        //Options:
        else if (game.gameState == Game.state.Options) {
            if (buttons[3][0].isOver(mx, my)) {
                game.setMenuMusic(!game.getMenuMusic());
            } else if (buttons[3][1].isOver(mx, my)) {
                game.setGameMusic(!game.getGameMusic());
            } else if (buttons[3][2].isOver(mx, my)) {
                game.setGameEffect(!game.getGameEffect());
            } else if (buttons[3][3].isOver(mx, my)) {
                focus = 0;
                game.gameState = Game.state.Menu;
            }
        }

        //Leaderboard entry:
        else if (game.gameState == Game.state.LeaderboardEntry) {
            if (buttons[4][0].isOver(mx, my)) {

                //Checks for validity of leaderboard name and perform according actions
                tempLeaderboardEntry.setName(removeSpaces((removeSpaces(leaderboardEntryTextField.getText()) + "   ").substring(0, Math.min(16, removeSpaces(leaderboardEntryTextField.getText()).length()))));
                if (tempLeaderboardEntry.getName().equals("")) {
                    isInvalid = true;
                } else {
                    leaderboard.add(tempLeaderboardEntry);
                    leaderboardEntryTextField.setVisible(false);
                    leaderboardEntryTextField.setText("Player");
                    isInvalid = false;
                    focus = 0;
                    game.gameState = Game.state.GameOver;
                }
            } else if (buttons[4][1].isOver(mx, my)) {
                leaderboardEntryTextField.setVisible(false);
                leaderboardEntryTextField.setText("Player");
                isInvalid = false;
                focus = 0;
                game.gameState = Game.state.GameOver;
            }
        }

        //Pause menu:
        else if (game.gameState == Game.state.Pause) {
            if (buttons[5][0].isOver(mx, my)) {
                focus = 0;
                Game.gameState = Game.state.Game;
            } else if (buttons[5][1].isOver(mx, my)) {
                focus = 0;
                Game.gameState = Game.state.GameReady;
            }
        }

        //Main game:
        else if (game.gameState == Game.state.Game) {
            if (buttons[6][0].isOver(mx, my)) {
                focus = 0;
                Game.gameState = Game.state.Pause;
            }
        }

        //Lead:
        else if (game.gameState == Game.state.Lead) {
            if (buttons[7][0].isOver(mx, my)) {
                focus = 0;
                Game.gameState = Game.state.Menu;
            }
        }

        //Game ready:
        else if (game.gameState == Game.state.GameReady) {
            if (buttons[8][0].isOver(mx, my)) {

                //Perform various actions based on whether the seed entry textfield is visible
                if (seedEntryTextField.isVisible() && !isInvalid) {
                    focus = 0;
                    game.setSeed(Integer.parseInt(seedEntryTextField.getText()));
                    game.gameState = Game.state.Game;
                    game.reset();
                    System.out.println("Game Reset");
                } else if (!seedEntryTextField.isVisible()) {
                    focus = 0;
                    game.gameState = Game.state.Game;
                    game.reset();
                    System.out.println("Game Reset");
                }
            } else if (buttons[8][1].isOver(mx, my)) {
                seedEntryTextField.setVisible(!seedEntryTextField.isVisible());
            } else if (buttons[8][2].isOver(mx, my)) {
                focus = 0;
                game.gameState = Game.state.Leaderboard;
            } else if (buttons[8][3].isOver(mx, my)) {
                focus = 0;
                game.gameState = Game.state.Menu;
            }
        }

        //Tutorial:
        else if (game.gameState == Game.state.Tutorial) {
            if (buttons[9][0].isOver(mx, my)) {
                focus = 0;
                game.gameState = Game.state.Menu;
            }
        }
    }

    //Description: removes all spaces from the given String. Used for names on leaderboard-entries
    //Parameters: the String in question
    //Return: the reformatted String
    private String removeSpaces(String s) {

        //removes all spaces from a given string
        StringBuilder sb = new StringBuilder();
        String[] temp = s.split(" ");
        for (String tempString : temp) {
            sb.append(tempString);
        }
        System.out.println(Arrays.toString(temp));
        return sb.toString();
    }

    //Description: verifies whether a given seed is valid to be used to generate an environment
    //Parameters: the seed in question
    //Return: whether the given seed is valid
    private boolean isSeedValid(String s) {

        //Whether a game seed is valid
        boolean isSeedValid = true;
        int i = 0;
        try {
            i = Integer.parseInt(s);
            if (!(i / 100000 >= 0 && i / 100000 <= 3 && (i % 100000) / 10000 >= 0 && (i % 100000) / 10000 <= 3 && i > 0 && i < 1000000 && i % 10000 > 0)) {
                isSeedValid = false;
            }
            if (s.length() != 6) {
                isSeedValid = false;
            }
        } catch (Exception e) {
            isSeedValid = false;
        }
        return isSeedValid;
    }

    public void mouseClicked(MouseEvent e) {

    }

    public void mousePressed(MouseEvent e) {

        //Get mouse coordinates
        int mx = e.getX();
        int my = e.getY();
        try {
            buttonEntered(mx, my);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void mouseReleased(MouseEvent e) {

    }

    public void mouseEntered(MouseEvent e) {

    }

    public void mouseExited(MouseEvent e) {

    }

    public void setFocus(int focus) {
        this.focus = focus;
    }
}
