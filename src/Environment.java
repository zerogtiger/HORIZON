import java.util.*;
import java.awt.*;

public class Environment {
    private int seed, weather, time, iterator;
    private Image clear, overcast, rain[], sandstorm[], sandstormOverlay, timesOfDay[];

    public Environment(int seed) {
        this.seed = seed;
        this.weather = seed/100000;
        this.time = (seed%100000)/10000;
        iterator = 0;
        clear = Toolkit.getDefaultToolkit().getImage("appdata/weather/clear.png");
        overcast = Toolkit.getDefaultToolkit().getImage("appdata/weather/overcast.png");
        rain = new Image[80];
        for (int i = 0; i < 80; i++) {
            rain[i] = Toolkit.getDefaultToolkit().getImage("appdata/pics/weather/rainFrames/" + String.format("%04d", i + 1) + ".png");
        }
        sandstorm = new Image[40];
        for (int i = 0; i < 40; i++) {
            sandstorm[i] = Toolkit.getDefaultToolkit().getImage("appdata/pics/weather/sandstormFrames/" + String.format("%04d", i + 1) + ".png");
        }
        sandstormOverlay =Toolkit.getDefaultToolkit().getImage("appdata/pics/weather/sandstorm.png");

        timesOfDay = new Image[4];

        timesOfDay[0] = Toolkit.getDefaultToolkit().getImage("appdata/pics/weather/morning.png");
        timesOfDay[1] = Toolkit.getDefaultToolkit().getImage("appdata/pics/weather/noon.png");
        timesOfDay[2] = Toolkit.getDefaultToolkit().getImage("appdata/pics/weather/evening.png");
        timesOfDay[3] = Toolkit.getDefaultToolkit().getImage("appdata/pics/weather/night.png");
    }

    public void reset(int seed) {
        this.seed = seed;
        this.weather = seed/100000;
        this.time = (seed%100000)/10000;
    }

    public void tick() {
        iterator = (iterator + 1) % 420;
    }

    public void render(Graphics g) {
        if (weather == 0) {
            g.drawImage(clear, 0,0, Game.WIDTH, Game.HEIGHT, null);
        }
        else if (weather == 1) {
            g.drawImage(overcast, 0,0, Game.WIDTH, Game.HEIGHT, null);
        }
        else if (weather == 2) {
            int temp = iterator%140/2;
            if (temp >= 5) {
                g.drawImage(rain[temp], 0,0, Game.WIDTH, Game.HEIGHT, null);
            }
            else {
                g.drawImage(rain[temp], 0,0, Game.WIDTH, Game.HEIGHT, null);
                g.drawImage(rain[temp+75], 0,0, Game.WIDTH, Game.HEIGHT, null);
            }
            g.drawImage(overcast, 0,0, Game.WIDTH, Game.HEIGHT, null);
        }
        else if (weather == 3) {
            int temp = iterator%60/2;
            if (temp > 5) {
                g.drawImage(sandstorm[temp], 0,0, Game.WIDTH, Game.HEIGHT, null);
            }
            else {
                g.drawImage(sandstorm[temp], 0,0, Game.WIDTH, Game.HEIGHT, null);
                g.drawImage(sandstorm[temp+34], 0,0, Game.WIDTH, Game.HEIGHT, null);
            }
            g.drawImage(sandstormOverlay, 0,0, Game.WIDTH, Game.HEIGHT, null);
        }

        g.drawImage(timesOfDay[time], 0,0, Game.WIDTH, Game.HEIGHT, null);
    }
}
