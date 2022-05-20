import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.io.*;

public class Window extends Canvas {

    @Serial
    private static final long serialVersionUID = 9034494958129720942L;
    private JPanel panel;

//    public Window(int width, int height, String title, Game game) {
//        JFrame frame = new JFrame(title);
//        panel = new JPanel();
//        panel.setPreferredSize(new Dimension(width, height));
//
////        frame.setPreferredSize(new Dimension(width, height));
////        frame.setMaximumSize(new Dimension(width, height));
////        frame.setMinimumSize(new Dimension(width, height));
//
//        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        frame.setResizable(false);
////        panel.add(game);
//        frame.add(panel);
//
////        frame.add(game);
//        frame.pack();
//        frame.setLocationRelativeTo(null);
//        frame.setVisible(true);
//        game.start();
//    }
//
//    public void paintComponent(Graphics g) {
//        super.paintComponent(g);
//        g.drawRect(0,0,200,200);
//    }


    public Window(int width, int height, String title, Game game) {
        JFrame frame = new JFrame(title);
        frame.setPreferredSize(new Dimension(width+14, height+37));
        frame.setMaximumSize(new Dimension(width+14, height+37));
        frame.setMinimumSize(new Dimension(width+14, height+37));

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.add(game);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        game.start();
    }

}
