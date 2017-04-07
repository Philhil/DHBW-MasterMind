package de.dhbw.stuttgart.mastermind;

/**
 * Created by Flip on 06.04.17.
 */


public class HighscoreItem {
    public final int id;
    public final String name;
    public final long duration;
    public final int tries;
    public final int colors;
    public final int fields;

    public HighscoreItem(int id, String name, long duration, int tries, int colors, int fields) {
        this.id = id;
        this.name = name;
        this.duration = duration;
        this.tries = tries;
        this.colors = colors;
        this.fields = fields;
    }

    @Override
    public String toString() {
        return name;
    }
}