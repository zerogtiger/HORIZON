import javax.swing.*;
import javax.tools.Tool;
import java.awt.event.*;
import java.util.*;
import java.io.*;
import java.awt.*;

public class Button{

    private String label;
    private int x, y, width, height;
    private Image[] borders = new Image[]{
            Toolkit.getDefaultToolkit().getImage("pics/buttonBorderLeft.png"),
            Toolkit.getDefaultToolkit().getImage("pics/buttonBorderRight.png")
    };
    private boolean isFocused;
    private Color color = Color.white;
    private Font font = new Font("Courier New", Font.BOLD, 22);
    private Game game;

    public Button(String label, int x, int y, int width, int height, Game game) {
        this.label = label;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.game = game;
    }


//    public boolean tick() {
//        Point p = MouseInfo.getPointerInfo().getLocation();
//        int mx = (int) (p.getX() - game.getLocationOnScreen().getX());
//        int my = (int) (p.getY() - game.getLocationOnScreen().getY());
//        return (mx >= x && mx <= x + width && my >= y && my <= y + height);
//    }

    public void render(Graphics g) {
        g.setColor(color);
//        g.drawRect(x, y, width, height);
        g.setFont(font);
        g.drawString(label, x + 25, y+19);
        g.drawImage(borders[0], x, y+3, 4 * 3, 7 * 3, null);
        if (isFocused) {
            g.drawImage(borders[0], x - 12, y+3, 4 * 3, 7 * 3, null);
            g.drawImage(borders[1], x + width - 4 * 3, y+3, 4 * 3, 7 * 3, null);
            g.drawImage(borders[1], x + width - 4 * 3+12, y+3, 4 * 3, 7 * 3, null);
        }
    }

    public boolean isOver(int mx, int my) {
        return (mx >= x && mx <= x + width && my >= y && my <= y + height);
    }

    public void setFocused(boolean focused) {
        isFocused = focused;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public void setFont(Font font) {
        this.font = font;
    }
}
