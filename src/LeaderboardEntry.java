/*

Description:

LeaderboardEntry objects storing leaderboard entry information for one specific entry, including entry-holder's name,
distance, seed, weather of run, time-of-day of run, and method of death. Responsible for rendering the individual entry
with the appropriate placement identifiers at the appropriate location on the screen.

*/

import java.awt.*;
import java.util.*;
import java.io.*;

public class LeaderboardEntry {

    //Name of entry-holder
    private String name;

    private static String
            //The various weather conditions
            weathers[] = {"Clear", "Overcast", "Rain", "Sandstorm"},
    //The various times of day
    timeOfDays[] = {"Morning", "Noon", "Evening", "Night"},
    //The various mediums of death in the game
    deathMethods[] = {"Collision", "Pursuer"},
    //The placement identifiers to be displayed depending on the placement of the leaderboard-entry
    placementIdentifier[] = {">>>>", ">>>", ">>", ">"};

    //Placement of entry on the leaderboard, user data for the entry, x and y-coordinates to display the entry, resp.
    private int placement, distance, seed, weather, timeOfDay, deathMethod, x, y;

    //The colors for the placement identifiers
    private static Color[] colors = {new Color(218, 26, 26),
            new Color(255, 204, 0),
            new Color(0, 178, 255),
            new Color(255, 255, 255)};

    //Whether the current entry has been initialized with actual player data
    private boolean isInitialized;

    //Constructor
    public LeaderboardEntry(String name, int distance, int seed, int deathMethod) {

        //Initialize variables
        this.name = name;
        this.distance = distance;
        this.seed = seed;
        this.weather = seed / 100000;
        this.timeOfDay = (seed % 100000) / 10000;
        this.deathMethod = deathMethod;
        x = 70;
        isInitialized = true;
    }

    //Constructor
    public LeaderboardEntry(int placement) {

        //Initialize variables
        this.placement = placement;
        x = 70;
        y = placement * 50 + 70;
        isInitialized = false;
    }

    //Constructor
    public LeaderboardEntry(int distance, int seed, int deathMethod, int x, int y) {

        //Initialize variables
        this.name = "Player";
        this.distance = distance;
        this.seed = seed;
        this.weather = seed / 100000;
        this.timeOfDay = (seed % 100000) / 10000;
        this.deathMethod = deathMethod;
        this.x = x;
        this.y = y;
        isInitialized = true;
    }

    //Description: produces the condensed String representation of the current leaderboard-entry to be saved to the leaderboard data .txt file
    //Parameters: none
    //Return: the condensed String representation of the current leaderboard-entry
    public String toFileString() {

        //The absolutely necessary data to derive the entire LeaderboardEntry strictly separated by spaces for clarity
        return (name + " " + distance + " " + seed + " " + deathMethod);
    }

    //Description: produces the String representation of the current leaderboard-entry to be displayed when rendered
    //Parameters: none
    //Return: the String representation of the current leaderboard-entry
    public String toString() {

        //String of the entry in its full form
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("%2d", placement));
        sb.append("  ");

        //Append user data if it is initialized
        if (isInitialized) {
            sb.append(String.format("%-20s", name));
            sb.append(String.format("%-7d", distance)).append("m    ");
            sb.append(String.format("%-13s", weathers[weather]));
            sb.append(String.format("%-11s", timeOfDays[timeOfDay]));
            sb.append(String.format("%-13s", deathMethods[deathMethod]));
            sb.append(String.format("%06d", seed));
        }
        //If not, just append dashes
        else
            sb.append("----------------    -------m    ---------    -------    ---------    ------");
        return sb.toString();
    }

    //Description: renders the current leaderboard-entry
    //Parameters: the Graphics object to draw the entry
    //Return: void
    public void render(Graphics g) {

        //Background for the entry
        g.setColor(new Color(0, 0, 0, 181));
        g.fillRect(x, y, 920, 40);

        //Placement identifiers
        g.setFont(new Font("Consolas", Font.BOLD, 25));
        g.setColor(colors[Game.clamp(placement, 1, 4) - 1]);
        g.drawString(String.format("%4s", placementIdentifier[Game.clamp(placement, 1, 4) - 1]),
                x + 15, y + 26);

        //Actual entry data
        g.setFont(new Font("Consolas", Font.PLAIN, 18));
        g.setColor((isInitialized? Color.white: Color.gray));
        g.drawString(toString(), x + 73, y + 26);
    }

    public int getDistance() {
        return (isInitialized ? distance : 0);
    }

    public void setPlacement(int placement) {
        this.placement = placement;
        y = placement * 48 + 96;
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
