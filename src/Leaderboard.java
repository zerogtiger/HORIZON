/*

Description:

Contains leaderboard information of the game via an array of LeaderboardEntry objects. Responsible for adding entries in
sorted, decreasing order by distance, saving and loading leaderboard data to and from a .txt file, and for rendering the
leaderboard.

*/

import java.awt.*;
import java.util.*;
import java.io.*;

public class Leaderboard {

    //Array of LeaderboardEntries
    private LeaderboardEntry[] entries;

    //Scanner and PrintWriter objects to load and save leaderboard data
    private Scanner inFile;
    private PrintWriter outFile;

    //Constructor
    public Leaderboard() throws IOException {

        //Initialize LeaderboardEntry array with dummy entries
        entries = new LeaderboardEntry[11];
        for (int i = 1; i <= 10; i++) {
            entries[i] = new LeaderboardEntry(i);
        }

        //Load saved data
        inFile = new Scanner(new File("appdata/userdata/leaderboard.txt"));

        //First transfer the loaded LeaderboardEntry objects to an ArrayList as adding it directly to the
        // LeaderboardEntry array using the following function will open the .txt file for both reading and writing at
        // the same time.
        ArrayList<LeaderboardEntry> arrayList = new ArrayList<>();
        for (int i = 1; i <= 10 && inFile.hasNextLine(); i++) {
            String[] temp = inFile.nextLine().split(" ");
            arrayList.add(new LeaderboardEntry(temp[0], Integer.parseInt(temp[1]), Integer.parseInt(temp[2]), Integer.parseInt(temp[3])));
        }
        inFile.close();

        //Add the LeaderboardEntry objects to the LeaderboardEntry array
        for (int i = 0; i < arrayList.size(); i++) {
            add(arrayList.get(i));
        }
    }

    //Description: inserts the given LeaderboardEntry into the leaderboard, maintaining sorted order. Updates the
    //             leaderboard information in the leaderboard data file
    //Parameters: the LeaderboardEntry to be added
    //Return: void
    public void add(LeaderboardEntry entry) throws IOException {

        //Set the x-coordinate of the entry to be uniform
        entry.setX(70);

        //Detecting the correct placement for the new entry
        boolean isIn = false;
        int placement = 1;
        for (int i = 1; i <= 10; i++) {
            if (entry.getDistance() > entries[i].getDistance()) {
                placement = i;
                isIn = true;
                break;
            }
        }

        //Add the new entry into the array and shuffle all lower entries downwards
        if (isIn) {
            for (int i = 9; i >= placement; i--) {
                entries[i + 1] = entries[i];
                entries[i + 1].setPlacement(i + 1);
            }
            entry.setPlacement(placement);
            entries[placement] = entry;
        }

        //Save the leaderboard information
        outFile = new PrintWriter(new FileWriter("appdata/userdata/leaderboard.txt", false));
        for (int i = 1; i <= 10 && entries[i].getInitialized(); i++) {
            outFile.println(entries[i].toFileString());
        }
        outFile.close();
    }

    //Description: verifies whether a LeaderboardEntry with the given distance should be added to the leaderboard
    //Parameters: the distance to be considered
    //Return: whether the LeaderboardEntry with the given distance should be added to the leaderboard
    public boolean isOnLeaderboard(int distance) {

        //Whether an entry with the provided distance is worthy of entering the leaderboard
        return (entries[10].getDistance() < distance);
    }

    //Description: renders current leaderboard entries to the screen
    //Parameters: the Graphics object to draw the entries
    //Return: void
    public void render(Graphics g) {

        //Entry information label
        g.setColor(Color.white);
        g.setFont(new Font("Consolas", Font.PLAIN, 18));
        g.drawString(" P  Name                Distance    Weather      Time       Death        Seed", 143, 135);

        //Render all LeaderboardEntry objects in the array
        for (int i = 1; i <= 10; i++) {
            entries[i].render(g);
        }
    }
}
