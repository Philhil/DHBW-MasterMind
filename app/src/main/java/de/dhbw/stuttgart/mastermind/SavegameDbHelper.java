package de.dhbw.stuttgart.mastermind;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by philipp on 23.04.17.
 */

public class SavegameDbHelper extends SQLiteOpenHelper
{
    public static final String DB_NAME = "savegame_list.db";
    public static final int DB_VERSION = 1;

    public static final String TABLE_SAVEGAME_LIST = "savegame_list";

    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_SAVEGAME = "savegame";

    public static final String SQL_CREATE =
            "CREATE TABLE " + TABLE_SAVEGAME_LIST +
                    "(" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_NAME + " TEXT NOT NULL, " +
                    COLUMN_SAVEGAME + " TEXT NOT NULL);";

    private static final String LOG_TAG = HighscoreDbHelper.class.getSimpleName();


    public SavegameDbHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        //Log.d(LOG_TAG, "DbHelper hat die Datenbank: " + getDatabaseName() + " erzeugt.");
    }

    // Die onCreate-Methode wird nur aufgerufen, falls die Datenbank noch nicht existiert
    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            //Log.d(LOG_TAG, "Die Tabelle wird mit SQL-Befehl: " + SQL_CREATE + " angelegt.");
            db.execSQL(SQL_CREATE);
        }
        catch (Exception ex) {
            //Log.e(LOG_TAG, "Fehler beim Anlegen der Tabelle: " + ex.getMessage());
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
