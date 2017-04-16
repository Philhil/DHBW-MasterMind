package de.dhbw.stuttgart.mastermind;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.content.ContentValues;
import android.database.Cursor;
import java.util.ArrayList;
import java.util.List;


public class HighscoreDataSource {

    private static final String LOG_TAG = HighscoreDataSource.class.getSimpleName();

    private SQLiteDatabase database;
    private HighscoreDbHelper dbHelper;

    private String[] columns = {
            HighscoreDbHelper.COLUMN_ID,
            HighscoreDbHelper.COLUMN_NAME,
            HighscoreDbHelper.COLUMN_DURATION,
            HighscoreDbHelper.COLUMN_TRIES,
            HighscoreDbHelper.COLUMN_COLORS,
            HighscoreDbHelper.COLUMN_FIELDS
    };


    public HighscoreDataSource(Context context) {
        //Log.d(LOG_TAG, "Unsere DataSource erzeugt jetzt den dbHelper.");
        dbHelper = new HighscoreDbHelper(context);
    }

    public void open() {
        //Log.d(LOG_TAG, "Eine Referenz auf die Datenbank wird jetzt angefragt.");
        database = dbHelper.getWritableDatabase();
        //Log.d(LOG_TAG, "Datenbank-Referenz erhalten. Pfad zur Datenbank: " + database.getPath());
    }

    public void close() {
        dbHelper.close();
        //Log.d(LOG_TAG, "Datenbank mit Hilfe des DbHelpers geschlossen.");
    }

    public HighscoreItem createHighscoreItem(String name, String duration, int tries, int colors, int fields) {
        ContentValues values = new ContentValues();
        values.put(HighscoreDbHelper.COLUMN_NAME, name);
        values.put(HighscoreDbHelper.COLUMN_DURATION, duration);
        values.put(HighscoreDbHelper.COLUMN_TRIES, tries);
        values.put(HighscoreDbHelper.COLUMN_COLORS, colors);
        values.put(HighscoreDbHelper.COLUMN_FIELDS, fields);

        long insertId = database.insert(HighscoreDbHelper.TABLE_HIGHSCORE_LIST, null, values);

        Cursor cursor = database.query(HighscoreDbHelper.TABLE_HIGHSCORE_LIST,
                columns, HighscoreDbHelper.COLUMN_ID + "=" + insertId,
                null, null, null, null);

        cursor.moveToFirst();
        HighscoreItem highscoreItem = cursorToHighscoreItem(cursor);
        cursor.close();

        return highscoreItem;
    }

    private HighscoreItem cursorToHighscoreItem(Cursor cursor) {
        int idIndex = cursor.getColumnIndex(HighscoreDbHelper.COLUMN_ID);
        int idName = cursor.getColumnIndex(HighscoreDbHelper.COLUMN_NAME);
        int idDuration = cursor.getColumnIndex(HighscoreDbHelper.COLUMN_DURATION);
        int idTries = cursor.getColumnIndex(HighscoreDbHelper.COLUMN_TRIES);
        int idColors = cursor.getColumnIndex(HighscoreDbHelper.COLUMN_COLORS);
        int idFields = cursor.getColumnIndex(HighscoreDbHelper.COLUMN_FIELDS);

        String name = cursor.getString(idName);
        String duration = cursor.getString(idDuration);
        int id = cursor.getInt(idIndex);
        int tries = cursor.getInt(idTries);
        int colors = cursor.getInt(idColors);
        int fields = cursor.getInt(idFields);

        HighscoreItem highscoreItem = new HighscoreItem(id, name, duration, tries, colors, fields);

        return highscoreItem;
    }

    public List<HighscoreItem> getAllHighscoreItems() {
        List<HighscoreItem> highscoreItemList = new ArrayList<>();

        Cursor cursor = database.query(HighscoreDbHelper.TABLE_HIGHSCORE_LIST,
                columns, null, null, null, null, HighscoreDbHelper.COLUMN_DURATION + " ASC");

        cursor.moveToFirst();
        HighscoreItem highscoreItem;

        while(!cursor.isAfterLast()) {
            highscoreItem = cursorToHighscoreItem(cursor);
            highscoreItemList.add(highscoreItem);
            //Log.d(LOG_TAG, "ID: " + highscoreItem.id + ", Inhalt: " + highscoreItem.toString());
            cursor.moveToNext();
        }

        cursor.close();

        return highscoreItemList;
    }

    public void deleteHighscoreItem(HighscoreItem highscoreItem) {
        long id = highscoreItem.id;

        database.delete(HighscoreDbHelper.TABLE_HIGHSCORE_LIST,
                HighscoreDbHelper.COLUMN_ID + "=" + id,
                null);

        //Log.d(LOG_TAG, "Eintrag gelöscht! ID: " + id + " Inhalt: " + highscoreItem.toString());
    }

    public void deleteAllItems()
    {
        List<HighscoreItem> items = getAllHighscoreItems();

        for (HighscoreItem item: items)
        {
            deleteHighscoreItem(item);
        }
    }
}