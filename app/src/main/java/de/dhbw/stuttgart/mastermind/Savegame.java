package de.dhbw.stuttgart.mastermind;

import com.google.gson.Gson;

/**
 * Created by philipp on 23.04.17.
 */

public class Savegame
{
    public final Row[] game;
    public final Row master;
    public final int activeRow;
    public final int anzRows;
    public final int anzFields;
    public final int anzColors;
    public final int backgroundColor;
    public final boolean multiple;
    public final boolean empty;
    public final boolean undo;
    public final boolean singlePlayer;

    public Savegame(Row[] game, Row master, int activeRow, int anzRows, int anzFields, int anzColors, int backgroundColor, boolean multiple, boolean empty, boolean undo, boolean singlePlayer) {
        this.game = game;
        this.master = master;
        this.activeRow = activeRow;
        this.anzRows = anzRows;
        this.anzFields = anzFields;
        this.anzColors = anzColors;
        this.backgroundColor = backgroundColor;
        this.multiple = multiple;
        this.empty = empty;
        this.undo = undo;
        this.singlePlayer = singlePlayer;
    }

    @Override
    public String toString() {
        Gson gson = new Gson();

        return gson.toJson(this);
    }
}
