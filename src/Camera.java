import java.util.*;
import java.io.*;

public class Camera {

    private Player player;
    private static int relX, relY, width, height;

    public Camera(Player player) {
        this.player = player;
        relX = -Game.WIDTH/2;
        relY = player.y-Game.HEIGHT+200;
        width = Game.WIDTH;
        height = Game.HEIGHT;
    }

    public void tick() {
        relX = player.x - Game.WIDTH/2+16;
        relY = player.y-Game.HEIGHT+200;
    }

    public static int getRelX(int x) {
        return (x-relX);
    }

    public static int getRelY(int y) {
        return (y-relY);
    }

    public static int getRelX() {
        return relX;
    }

    public static int getRelY() {
        return relY;
    }

    public static boolean outOfFrame(GameObject object) {
        return (object.x > relX + width || (object.x + object.width) < relX ) ||
                (object.y > relY + height || (object.y + object.height) < relY );

//        return !((object.x + object.width> relX && object.x < relX + width) && (object.y + object.height < relY && object.y < relY + height));
    }

    public static boolean outOfFrame(int x, int y, int objectWidth, int objectHeight) {
        return (x > relX + width || (x + objectWidth) < relX ) ||
                (y > relY + height || (y + objectHeight) < relY );

//        return !((x + objectWidth > relX && x < relX + width) && (y + objectHeight < relY && y < relY + height));
    }

}
