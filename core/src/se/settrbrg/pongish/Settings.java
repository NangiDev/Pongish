package se.settrbrg.pongish;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

public class Settings {
    public static boolean musicEnabled = true;
    public static boolean soundEnabled = true;
    public static int[] highScores = new int[] {0,0,0,0,0};
    public final static String file = "data/highScore.txt";

    public static void load() {
        try {
            FileHandle fileHandle = Gdx.files.local(file);
            String[] strings = fileHandle.readString().split("\n");
            musicEnabled = Boolean.parseBoolean(strings[0]);
            soundEnabled = Boolean.parseBoolean(strings[1]);
            for (int i = 1; i < 6; i++) {
                highScores[i] = Integer.parseInt(strings[i+1]);
            }
        } catch (Throwable e) {}
    }

    public static void save() {
        try {
            FileHandle fileHandle = Gdx.files.local(file);
            fileHandle.writeString(Boolean.toString(musicEnabled)+"\n", false);
            fileHandle.writeString(Boolean.toString(soundEnabled)+"\n", true);
            for (int i = 0; i < 5; i++) {
                fileHandle.writeString(Integer.toString(highScores[i])+"\n", true);
            }
        } catch (Throwable e) {}
    }

    public static void addScore (int score) {
        for (int i = 0; i < 5; i++) {
            if (highScores[i] < score) {
                for (int j =4; j > i; j--) {
                    highScores[j] = highScores[j -1];
                }
                highScores[i] = score;
                break;
            }
        }
    }
}
