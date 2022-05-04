import java.util.*;
import java.io.*;
import java.awt.*;
import java.awt.event.*;

public class Menu extends MouseAdapter{
    private Handler handler;
    private Game game;

    public Menu(Game game, Handler handler) {
        this.game = game;
        this.handler = handler;
    }

    public void mousePressed(MouseEvent e) {
        super.mousePressed(e);
        int mx = e.getX();
        int my = e.getY();

        if (game.gameState == Game.state.Menu) {
            if (mouseOver(mx, my, 60, Game.HEIGHT-280, 200, 32)) {
                game.gameState = Game.state.Game;
                game.reset();
            }
            else if (mouseOver(mx, my, 60, Game.HEIGHT-230, 200, 32)) {
                game.gameState = Game.state.Options;
            }
            else if (mouseOver(mx, my, 60, Game.HEIGHT-180, 200, 32)) {
                game.gameState = Game.state.Leaderboard;
            }
            else if (mouseOver(mx, my, 60, Game.HEIGHT-130, 200, 32)) {
                System.exit(0);
            }
        }
        else if (game.gameState == Game.state.GameOver) {
            if (mouseOver(mx, my, 110, Game.HEIGHT-180, 200, 32)) {
                game.gameState = Game.state.Menu;
            }
            else if (mouseOver(mx, my, 110, Game.HEIGHT-130, 200, 32)) {
                game.gameState = Game.state.Game;
                game.reset();
            }
        }


    }

    private boolean mouseOver(int mx, int my, int x, int y, int width, int height) {
        return (mx >= x && mx <= x+width && my >= y && my <= y+height);
    }

    public void tick() {

    }

    public void render(Graphics g) {
        Font courier = new Font("Courier New", Font.PLAIN, 18);
        Font courier2 = new Font("Courier New", Font.BOLD, 60);
        if (game.gameState == Game.state.Menu) {
            g.setColor(Color.white);
            g.setFont(courier);
            g.drawImage(Toolkit.getDefaultToolkit().getImage("pics/title.png"), 60, 200, 310*3, 24*3, game);
            g.drawString("START", 90, Game.HEIGHT-280+21);
            g.drawString("OPTIONS", 90, Game.HEIGHT-230+21);
            g.drawString("LEADERBOARD", 90, Game.HEIGHT-180+21);
            g.drawString("QUIT", 90, Game.HEIGHT-130+21);
            g.drawRect(60, Game.HEIGHT-280, 200, 32);
            g.drawRect(60, Game.HEIGHT-230, 200, 32);
            g.drawRect(60, Game.HEIGHT-180, 200, 32);
            g.drawRect(60, Game.HEIGHT-130, 200, 32);
        }
        if (game.gameState == Game.state.GameOver) {
            g.setFont(courier2);
            g.setColor(new Color(219, 198, 191, 50));
            g.fillRect(70, 70, Game.WIDTH-140, Game.HEIGHT-140);
            g.drawImage(Toolkit.getDefaultToolkit().getImage("pics/gameOver.png"), 110,  110, 138*3, 42*3, game);
            g.setColor(Color.white);
            g.drawImage(Toolkit.getDefaultToolkit().getImage("pics/distance.png"), 110, 300, 83*3, 5*3, game);
            g.drawString(String.valueOf(Stats.speederDistance), 110, 380);
            g.setFont(courier);
            g.drawString("MENU", 150, Game.HEIGHT-180+21);
            g.drawString("RELOAD", 150, Game.HEIGHT-130+21);
            g.drawRect(110, Game.HEIGHT-180, 200, 32);
            g.drawRect(110, Game.HEIGHT-130, 200, 32);



        }
    }


}
