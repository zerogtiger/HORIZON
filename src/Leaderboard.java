import java.awt.*;
import java.util.*;
import java.io.*;
public class Leaderboard {

    private LeaderboardEntry[] entries;
    private int last;

    public Leaderboard() {
        entries = new LeaderboardEntry[11];
        for (int i = 1; i <= 10; i++) {
            entries[i] = new LeaderboardEntry(i);
        }
    }

    public void add(LeaderboardEntry entry) {
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
    }

    public void render(Graphics g) {
        for (int i = 1; i <= 10; i++) {
            entries[i].render(g);
        }
    }
}
