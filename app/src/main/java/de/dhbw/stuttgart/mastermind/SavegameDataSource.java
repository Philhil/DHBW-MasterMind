package de.dhbw.stuttgart.mastermind;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by philipp on 23.04.17.
 */

public class SavegameDataSource
{
    private static final String LOG_TAG = HighscoreDataSource.class.getSimpleName();

    private SQLiteDatabase database;
    private SavegameDbHelper dbHelper;

    private String[] columns = {
            SavegameDbHelper.COLUMN_ID,
            SavegameDbHelper.COLUMN_NAME,
            SavegameDbHelper.COLUMN_SAVEGAME
    };


    public SavegameDataSource(Context context) {
        //Log.d(LOG_TAG, "Unsere DataSource erzeugt jetzt den dbHelper.");
        dbHelper = new SavegameDbHelper(context);
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

    public SavegameItem createSavegameItem(String name, String savegame) {
        ContentValues values = new ContentValues();
        values.put(SavegameDbHelper.COLUMN_NAME, name);
        values.put(SavegameDbHelper.COLUMN_SAVEGAME, savegame);

        long insertId = database.insert(SavegameDbHelper.TABLE_SAVEGAME_LIST, null, values);

        Cursor cursor = database.query(SavegameDbHelper.TABLE_SAVEGAME_LIST,
                columns, SavegameDbHelper.COLUMN_ID + "=" + insertId,
                null, null, null, null);

        cursor.moveToFirst();
        SavegameItem savegameItem = cursorToSavegameItem(cursor);
        cursor.close();

        return savegameItem;
    }

    private SavegameItem cursorToSavegameItem(Cursor cursor) {
        int idIndex = cursor.getColumnIndex(SavegameDbHelper.COLUMN_ID);
        int idName = cursor.getColumnIndex(SavegameDbHelper.COLUMN_NAME);
        int idsaveGame = cursor.getColumnIndex(SavegameDbHelper.COLUMN_SAVEGAME);

        String name = cursor.getString(idName);
        String saveGame = cursor.getString(idsaveGame);
        int id = cursor.getInt(idIndex);

        SavegameItem savegameItem = new SavegameItem(id, name, saveGame);

        return savegameItem;
    }

    public List<SavegameItem> getAllSavegameItems() {
        List<SavegameItem> savegameItemList = new ArrayList<>();

        Cursor cursor = database.query(SavegameDbHelper.TABLE_SAVEGAME_LIST,
                columns, null, null, null, null, SavegameDbHelper.COLUMN_NAME + " ASC");

        cursor.moveToFirst();
        SavegameItem savegameItem;

        while(!cursor.isAfterLast()) {
            savegameItem = cursorToSavegameItem(cursor);
            savegameItemList.add(savegameItem);
            cursor.moveToNext();
        }

        cursor.close();

        return savegameItemList;
    }

    public void deleteSavegameItem(SavegameItem savegameItem) {
        long id = savegameItem.id;

        database.delete(SavegameDbHelper.TABLE_SAVEGAME_LIST,
                SavegameDbHelper.COLUMN_ID + "=" + id,
                null);

        //Log.d(LOG_TAG, "Eintrag gelöscht! ID: " + id + " Inhalt: " + highscoreItem.toString());
    }

    public void deleteAllItems()
    {
        database.delete(SavegameDbHelper.TABLE_SAVEGAME_LIST, null, null);
    }
}
