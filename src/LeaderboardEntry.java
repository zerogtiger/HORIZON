import java.awt.*;
import java.util.*;
import java.io.*;

public class LeaderboardEntry {
    private String name;
    private static String weathers[] = {"Clear", "Rain", "Sandstorm", "Overcast"},
            timeOfDays[] = {"Morning", "Noon", "Evening", "Night"},
            deathMethods[] = {"Collision", "Pursuer"},
            placementIdentifier[] = {">>>>", ">>>", ">>", ">"};
    private int placement, distance, seed, weather, timeOfDay, deathMethod;
    private Color[] colors = {new Color(218, 26, 26),
            new Color(255, 204, 0),
            new Color(0, 178, 255),
            new Color(255, 255, 255)};
    private boolean isInitialized;

    public LeaderboardEntry(String name, int distance, int seed, int weather, int timeOfDay, int deathMethod) {
        this.name = name;
        this.distance = distance;
        this.seed = seed;
        this.weather = weather;
        this.timeOfDay = timeOfDay;
        this.deathMethod = deathMethod;
        isInitialized = true;
    }

    public LeaderboardEntry(int placement) {
        this.placement = placement;
        isInitialized = false;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("%2d", placement));
        sb.append("  ");
        if (isInitialized) {
            sb.append(String.format("%-20s", name));
            sb.append(String.format("%-6d", distance)).append("m    ");
            //Add unit -----------------------------------------^here
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
        g.fillRect(70, placement * 50 + 70, 915, 40);
        g.setFont(new Font("Consolas", Font.BOLD, 25));
        g.setColor(colors[Game.clamp(placement, 1, 4) - 1]);
        g.drawString(String.format("%4s", placementIdentifier[Game.clamp(placement, 1, 4) - 1]),
                85, placement * 50 + 70 + 26);
        g.setFont(new Font("Consolas", Font.PLAIN, 18));
        g.setColor(Color.white);
        g.drawString(toString(), 153, placement * 50 + 70 + 26);
    }

    public int getDistance() {
        return (isInitialized ? distance : 0);
    }

    public void setPlacement(int placement) {
        this.placement = placement;
    }

}
