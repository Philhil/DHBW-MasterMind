package de.dhbw.stuttgart.mastermind;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class Colorcode extends AppCompatActivity {

    private int _anzFields;
    private int _anzColors;
    private boolean _multiple;
    private boolean _empty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_colorcode);

        getSettingsFromPreferences();
    }

    public void startGame(View view) {
        Intent intent = new Intent(this, GameActivity.class);

        //DUMMY DATA
        Row masterrow = new Row(_anzFields);
        Field[] Fields = new Field[_anzFields];
        for (int i = 0; i < _anzFields; i++)
        {
            Fields[i] = new Field();
            Fields[i].setColor(1);
        }

        masterrow.Fields = Fields;

        intent.putExtra("masterRow", masterrow);
        startActivity(intent);
        finish();
    }

    private void getSettingsFromPreferences()
    {
        SharedPreferences sharedPref = this.getSharedPreferences(getString(R.string.preference_file_key), MODE_PRIVATE);

        _anzColors = sharedPref.getInt(getString(R.string.prefkey_number_of_colors), 5);
        _anzFields = sharedPref.getInt(getString(R.string.prefkey_number_of_holes), 4);
        _multiple = sharedPref.getBoolean(getString(R.string.prefkey_multiple), false);
        _empty = sharedPref.getBoolean(getString(R.string.prefkey_empty), false);
    }
}
