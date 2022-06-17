import java.awt.*;
import java.util.*;
import java.io.*;

public class LeaderboardEntry {
    private String name;
    private static String weathers[] = {"Clear", "Overcast", "Rain", "Sandstorm"},
            timeOfDays[] = {"Morning", "Noon", "Evening", "Night"},
            deathMethods[] = {"Collision", "Pursuer"},
            placementIdentifier[] = {">>>>", ">>>", ">>", ">"};
    private int placement, distance, seed, weather, timeOfDay, deathMethod, x, y;
    private static Color[] colors = {new Color(218, 26, 26),
            new Color(255, 204, 0),
            new Color(0, 178, 255),
            new Color(255, 255, 255)};
    private boolean isInitialized;

    public LeaderboardEntry(String name, int distance, int seed, int deathMethod) {
        this.name = name;
        this.distance = distance;
        this.seed = seed;
        this.weather = seed/100000;
        this.timeOfDay = (seed%100000)/10000;
        this.deathMethod = deathMethod;
        x = 70;
        isInitialized = true;
    }

    public LeaderboardEntry(int placement) {
        this.placement = placement;
        x = 70;
        y = placement * 50 + 70;
        isInitialized = false;
    }

    public LeaderboardEntry(int distance, int seed, int deathMethod, int x, int y) {
        this.name = "Player";
        this.distance = distance;
        this.seed = seed;
        this.weather = seed/100000;
        this.timeOfDay = (seed%100000)/10000;
        this.deathMethod = deathMethod;
        this.x = x;
        this.y = y;
        isInitialized = true;
    }

    public String toFileString() {
        return (name + " " + distance + " " + seed + " " + deathMethod);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("%2d", placement));
        sb.append("  ");
        if (isInitialized) {
            sb.append(String.format("%-20s", name));
            sb.append(String.format("%-6d", distance)).append("m    ");
            sb.append(String.format("%-13s", weathers[weather]));
            sb.append(String.format("%-11s", timeOfDays[timeOfDay]));
            sb.append(String.format("%-13s", deathMethods[deathMethod]));
            sb.append(String.format("%06d", seed));
        } else
            sb.append("----------------    ------m    ---------    -------    ---------    ------");
        return sb.toString();
    }

    public void render(Graphics g) {
        g.setColor(new Color(0, 0, 0, 200));
        g.fillRect(x, y, 915, 40);
        g.setFont(new Font("Consolas", Font.BOLD, 25));
        g.setColor(colors[Game.clamp(placement, 1, 4) - 1]);
        g.drawString(String.format("%4s", placementIdentifier[Game.clamp(placement, 1, 4) - 1]),
                x+15, y + 26);
        g.setFont(new Font("Consolas", Font.PLAIN, 18));
        g.setColor(Color.white);
        g.drawString(toString(), x+73, y + 26);
    }

    public int getDistance() {
        return (isInitialized ? distance : 0);
    }

    public void setPlacement(int placement) {
        this.placement = placement;
        y = placement * 50 + 70;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public boolean getInitialized() {
        return isInitialized;
    }

    public void setX(int x) {
        this.x = x;
    }
}
