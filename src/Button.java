/*

Description:

A custom button class with custom appearances and focused animations. Customizable in location, dimensions, and labelled
text. Designed to work on all screens regardless of content. Does not extend any MouseListener methods and instead,
manually checks for overlaps of provided mouse coordinates with button area when the according method called to detect
hovering and presses.

*/

import javax.swing.*;
import javax.tools.Tool;
import java.awt.event.*;
import java.util.*;
import java.io.*;
import java.awt.*;

public class Button{

    //Label for the button
    private String label;

    //Location and dimension
    private int x, y, width, height;

    //Border images
    private Image[] borders = new Image[]{
            Toolkit.getDefaultToolkit().getImage("appdata/pics/buttonBorderLeft.png"),
            Toolkit.getDefaultToolkit().getImage("appdata/pics/buttonBorderRight.png")
    };

    //Whether the button is focused by the user
    private boolean isFocused;

    //The button's colour and font
    private Color color = Color.white;
    private Font font = new Font("Courier New", Font.BOLD, 22);

    //The game it belongs to
    private Game game;

    //Constructor
    public Button(String label, int x, int y, int width, int height, Game game) {

        //Initialize variables
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

    //Description: renders button at specified location, with specified dimensions and styles
    //Parameters: the Graphics object to draw the button
    //Return: void
    public void render(Graphics g) {

        //Renders the button on screen at the designated location with specified dimensions
        g.setColor(color);
        g.setFont(font);

        //Renders the String label
        g.drawString(label, x + 25, y+19);

        //Default border
        g.drawImage(borders[0], x, y+3, 4 * 3, 7 * 3, game);

        //Focused borders
        if (isFocused) {
            g.drawImage(borders[0], x - 12, y+3, 4 * 3, 7 * 3, game);
            g.drawImage(borders[1], x + width - 4 * 3, y+3, 4 * 3, 7 * 3, game);
            g.drawImage(borders[1], x + width - 4 * 3+12, y+3, 4 * 3, 7 * 3, game);
        }
    }

    //Description: verifies whether the mouse cursor hovers over the current button
    //Parameters: the x and y-coordinates of the mouse cursor
    //Return: whether the mouse cursor hovers over the current button
    public boolean isOver(int mx, int my) {

        //If the mouse location is within the area of the button defined by its coordinates and dimensions
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
