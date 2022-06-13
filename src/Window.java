import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.io.*;

public class Window {

    private JPanel game;
    private JFrame frame;

    public Window(int width, int height, String title, Game game) {
        this.game = game;
        game.setPreferredSize(new Dimension(width, height));
        game.setMaximumSize(new Dimension(width, height));
        game.setMinimumSize(new Dimension(width, height));
        game.setFocusable(true);
        game.addKeyListener(game.getKeyInput());
        game.addMouseListener(game.getMenu());

        frame = new JFrame(title);
        frame.setIconImage(Toolkit.getDefaultToolkit().getImage("appdata/pics/icon.png"));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(game);
        frame.setResizable(false);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        game.start();
    }

}
