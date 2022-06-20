/*

Description:

Main JFrame for the game responsible for drawing the window on the screen with the according application icon and title,
and displaying the game to allow user interactions.

*/


import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.io.*;

public class Window {

    //JPanel and JFrame that will be displayed
    private JPanel game;
    private JFrame frame;

    //Constructor
    public Window(int width, int height, String title, Game game) {

        //Initialize the JPanel
        this.game = game;

        //Set JPanel properties
        game.setPreferredSize(new Dimension(width, height));
        game.setMaximumSize(new Dimension(width, height));
        game.setMinimumSize(new Dimension(width, height));
        game.setFocusable(true);
        game.addKeyListener(game.getKeyInput());
        game.addMouseListener(game.getMenu());

        //Initialize the JFrame
        frame = new JFrame(title);

        //Set JFrame properties
        frame.setIconImage(Toolkit.getDefaultToolkit().getImage("appdata/images/icon.png"));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(game);
        frame.setResizable(false);
        frame.pack();
        frame.setLocationRelativeTo(null);

        //Display the window
        frame.setVisible(true);
        game.start();
    }

}
