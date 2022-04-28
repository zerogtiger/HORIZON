import java.util.*;
import java.io.*;

public class Stats {
    public static int speederDistance = 0;
    public static boolean[][] obstacles = new boolean[151][301];
    public static boolean[] KEYPRESS = new boolean[3]; //0 for left, 1 for right, 2 for space, 3 for lockX.
    public static double CHARGE = 100;
    private Handler handler;
}
