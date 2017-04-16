package de.dhbw.stuttgart.mastermind;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class HighscoreDbHelper extends SQLiteOpenHelper{

    public static final String DB_NAME = "highscore_list.db";
    public static final int DB_VERSION = 1;

    public static final String TABLE_HIGHSCORE_LIST = "highscore_list";

    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_DURATION = "duration";
    public static final String COLUMN_TRIES = "tries";
    public static final String COLUMN_COLORS = "colors";
    public static final String COLUMN_FIELDS = "fields";

    public static final String SQL_CREATE =
            "CREATE TABLE " + TABLE_HIGHSCORE_LIST +
                    "(" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_NAME + " TEXT NOT NULL, " +
                    COLUMN_DURATION + " TEXT NOT NULL, " +
                    COLUMN_TRIES + " INTEGER NOT NULL, " +
                    COLUMN_COLORS + " INTEGER NOT NULL, " +
                    COLUMN_FIELDS + " INTEGER NOT NULL);";

    private static final String LOG_TAG = HighscoreDbHelper.class.getSimpleName();


    public HighscoreDbHelper(Context context) {
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