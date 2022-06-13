import java.awt.*;
import java.util.*;
import java.io.*;
public class Leaderboard {

    private LeaderboardEntry[] entries;
    private Scanner inFile;
    private PrintWriter outFile;

    public Leaderboard() throws IOException {
        entries = new LeaderboardEntry[11];
        for (int i = 1; i <= 10; i++) {
            entries[i] = new LeaderboardEntry(i);
        }
        inFile = new Scanner(new File("appdata/userdata/leaderboard.txt"));
        ArrayList<LeaderboardEntry> arrayList = new ArrayList<>();
        for (int i = 1; i <= 10 && inFile.hasNextLine(); i++) {
            String[] temp = inFile.nextLine().split(" ");
            arrayList.add(new LeaderboardEntry(temp[0], Integer.parseInt(temp[1]), Integer.parseInt(temp[2]), Integer.parseInt(temp[3])));
        }
        inFile.close();
        for (int i = 0; i < arrayList.size(); i++) {
            add(arrayList.get(i));
        }
    }

    public void add(LeaderboardEntry entry) throws IOException {
        entry.setX(70);
        boolean isIn = false;
        int placement = 1;
        for (int i = 1; i <= 10; i++) {
            if (entry.getDistance() > entries[i].getDistance()) {
                placement = i;
                isIn = true;
                break;
            }
        }
        if (isIn) {
            for (int i = 9; i >= placement; i--) {
                entries[i+1] = entries[i];
                entries[i+1].setPlacement(i+1);
            }
            entry.setPlacement(placement);
            entries[placement] = entry;
        }
        outFile = new PrintWriter(new FileWriter("appdata/userdata/leaderboard.txt", false));
        for (int i = 1; i <= 10 && entries[i].getInitialized(); i++) {
            outFile.println(entries[i].toFileString());
        }
        outFile.close();
    }

    public boolean isOnLeaderboard(int distance) {
        return (entries[10].getDistance() < distance);
    }

    public void render(Graphics g) {
        for (int i = 1; i <= 10; i++) {
            entries[i].render(g);
        }
    }
}
