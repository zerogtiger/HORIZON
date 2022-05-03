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


    }

    private boolean mouseOver(int mx, int my, int x, int y, int width, int height) {
        return (mx >= x && mx <= x+width && my >= y && my <= y+height);
    }

    public void tick() {

    }

    public void render(Graphics g) {
        Font ariel = new Font("Consolas", Font.PLAIN, 14);
        g.setColor(Color.white);
        g.drawImage(Toolkit.getDefaultToolkit().getImage("pics/title.png"), 60, 200, 337*3, 32*3, game);
        g.drawString("START", 142, Game.HEIGHT-280+21);
        g.drawString("OPTIONS", 135, Game.HEIGHT-230+21);
        g.drawString("LEADERBOARD", 116, Game.HEIGHT-180+21);
        g.drawString("QUIT", 148, Game.HEIGHT-130+21);
        g.drawRect(60, Game.HEIGHT-280, 200, 32);
        g.drawRect(60, Game.HEIGHT-230, 200, 32);
        g.drawRect(60, Game.HEIGHT-180, 200, 32);
        g.drawRect(60, Game.HEIGHT-130, 200, 32);
    }


}
