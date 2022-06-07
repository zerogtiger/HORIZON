import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.io.*;

public class Window extends JFrame {

    private JPanel game;


    public Window(int width, int height, String title, Game game) {
        this.game = game;
        game.setPreferredSize(new Dimension(width, height));
        game.setMaximumSize(new Dimension(width, height));
        game.setMinimumSize(new Dimension(width, height));
        game.setFocusable(true);
        game.addKeyListener(game.getKeyInput());
        game.addMouseListener(game.getMenu());

        JFrame frame = new JFrame(title);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(game);
        frame.setResizable(false);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        game.start();
    }

}
