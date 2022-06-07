import java.util.*;
import java.io.*;

public class Stats {
    public static int speederDistance;
    private static boolean[][] KEYPRESS;
    //0 for normal mode, 1 for debug mode
    //0 for left, 1 for right, 2 for space, 3 for up, 4 for down, 5 for enter.
    public static int CHARGE;
    public static int debug;

    public Stats() {
        speederDistance = 0;
        KEYPRESS = new boolean[2][6];
        CHARGE = 0;
        debug = 0;
    }
    public static void setKeyPress(int x, int y, boolean b) {
        KEYPRESS[x][y] = b;
    }
    public static boolean[][] getKeyPress() {
        return KEYPRESS;
    }

}
