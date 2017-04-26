package de.dhbw.stuttgart.mastermind;


/**
 * Created by philipp on 23.04.17.
 */

public class SavegameItem
{
    public final int id;
    public final String name;
    public final String saveGame;

    public SavegameItem(int id, String name, String saveGame) {
        this.id = id;
        this.name = name;
        this.saveGame = saveGame;
    }

    @Override
    public String toString() {
        return name;
    }
}
