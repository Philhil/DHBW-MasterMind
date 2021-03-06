package de.dhbw.stuttgart.mastermind;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Point;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.github.amlcurran.showcaseview.ShowcaseView;
import com.github.amlcurran.showcaseview.targets.ViewTarget;

public class Colorcode extends AppCompatActivity implements View.OnClickListener{

    private int _anzFields;
    private int _anzColors;
    private boolean _multiple;
    private boolean _empty;

    private int _displayWidth;
    private int _bigPeg;
    private int _farbAuswPeg;
    private int _backgroundColor;

    private Row _farbvorschlagRow;
    private LinearLayout _farbauswahl;
    private int _activeField = -1;

    private int[] _gameColors;

    private ShowcaseView _showcaseView;
    private int _counter = 0;
    private boolean _showHelp;
    private boolean _helpShown = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_colorcode);

        getSettingsFromPreferences();

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        _displayWidth = size.x;

        _farbAuswPeg = _displayWidth/9;
        _bigPeg = _displayWidth/8;

        _farbauswahl = CreateFarbauswahl(this);
        _farbvorschlagRow = ResetFarbvorschlagRow();

        // add a row with the buttons to touch
        LinearLayout farbvorschlag = (LinearLayout) findViewById(R.id.colorcode_farbvorschlag);
        farbvorschlag.addView(_farbauswahl);
        _farbauswahl.setVisibility(LinearLayout.INVISIBLE);
        farbvorschlag.addView(CreateDisplayableRow(this, _farbvorschlagRow));

        int color = Color.TRANSPARENT;
        switch(_backgroundColor)
        {
            case R.string.settings_backgroundGreen:
                color = Color.GREEN;
                break;
            case R.string.settings_backgroundGrey:
                color = Color.GRAY;
                break;
            case R.string.settings_backgroundWhite:
                color = Color.WHITE;
                break;
        }
        findViewById(R.id.colorcode).setBackgroundColor(color);

        if (_showHelp)
        {
            createShowcase();

            SharedPreferences sharedPref = this.getSharedPreferences(getString(R.string.preference_file_key), MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();

            editor.putBoolean(getString(R.string.prefkey_help_colorcode), false);
            editor.commit();
        }
    }

    private void createShowcase()
    {
        _helpShown = true;
        _showcaseView = new ShowcaseView.Builder(this)
                .setTarget(new ViewTarget(findViewById(R.id.colorcode_farbvorschlag)))
                .setContentTitle("Farbe auswählen")
                .setContentText("Hier kannst Du eine Farbe auswählen")
                .setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        switch (_counter) {
                            case 0:
                                _showcaseView.setShowcase(new ViewTarget(findViewById(R.id.colorcode_start)), true);
                                _showcaseView.setContentTitle("Spiel starten");
                                _showcaseView.setContentText("Hier startest du das Spiel für den zweiten Spieler");
                                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                                params.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
                                _showcaseView.setButtonPosition(params);
                                break;
                            case 1:
                                _helpShown = false;
                                _showcaseView.hide();
                                break;
                        }
                        _counter++;
                    }
                })
                .build();
    }

    public void startGame(View view) {
        Intent intent = new Intent(this, GameActivity.class);
        Row masterrow;

        boolean eval = true;
        if (!_empty)
        {
            //if all fields are filled when empty isn't allowed
            for (int i = 0; i < _anzFields; i++)
            {
                if (_farbvorschlagRow.Fields[i].getColor() == -1)
                {
                    eval = false;
                    Toast toast = Toast.makeText(getApplicationContext(), "Leere Felder nicht erlaubt", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER,0,0);
                    toast.show();
                    break;
                }
            }
        }
        if (!_multiple && eval)
        {
            boolean colors[];
            colors = new boolean[_anzColors+1];

            //if all fields are filled unique, when multiple aren't allowed
            for (int i = 0; i < _anzFields; i++)
            {
                if (!colors[_farbvorschlagRow.Fields[i].getColor()+1])
                {
                    colors[_farbvorschlagRow.Fields[i].getColor()+1] = true;
                }
                else
                {
                    eval = false;
                    Toast toast = Toast.makeText(getApplicationContext(), "Doppelte Felder nicht erlaubt", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER,0,0);
                    toast.show();
                    break;
                }
            }
        }
        if (eval)
        {
            masterrow = _farbvorschlagRow;
            intent.putExtra("masterRow", masterrow);
            startActivity(intent);
            finish();
        }
    }

    private void getSettingsFromPreferences()
    {
        int[] pictures = {R.mipmap.ic_red, R.mipmap.ic_green, R.mipmap.ic_purple, R.mipmap.ic_blue, R.mipmap.ic_grey, R.mipmap.ic_pink, R.mipmap.ic_turquoise, R.mipmap.ic_yellow, R.mipmap.ic_brown};


        SharedPreferences sharedPref = this.getSharedPreferences(getString(R.string.preference_file_key), MODE_PRIVATE);

        _anzColors = sharedPref.getInt(getString(R.string.prefkey_number_of_colors), 5);
        _anzFields = sharedPref.getInt(getString(R.string.prefkey_number_of_holes), 4);
        _multiple = sharedPref.getBoolean(getString(R.string.prefkey_multiple), false);
        _empty = sharedPref.getBoolean(getString(R.string.prefkey_empty), false);
        _backgroundColor = sharedPref.getInt(getString(R.string.prefkey_background_color), R.string.settings_backgroundWhite);
        _showHelp = sharedPref.getBoolean(getString(R.string.prefkey_help_colorcode), true);
        int arr[] = new int[8];
        for(int i=0;i<8;i++)
        {
            arr[i] = sharedPref.getInt("color_" + i, pictures[i]);
        }
        _gameColors = arr;
    }

    public ImageView getPictureToColor(int color)
    {
        ImageView tmp = new ImageView(this);

        if (color == -1)
        {
            tmp.setImageResource(R.mipmap.ic_slot);
        }
        else
        {
            tmp.setImageResource(_gameColors[color]);
        }

        return tmp;
    }

    public LinearLayout CreateDisplayableRow(Context context, Row row)
    {
        LinearLayout rowLayout = new LinearLayout(context);
        rowLayout.setOrientation(LinearLayout.HORIZONTAL);
        rowLayout.setHorizontalGravity(Gravity.CENTER);
        rowLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        for (int i = 0; i < _anzFields; i++)
        {
            ImageView tmp = getPictureToColor(row.Fields[i].getColor());
            tmp.setLayoutParams(new LinearLayout.LayoutParams(_bigPeg, _bigPeg));
            tmp.setId(i+10);
            tmp.setOnClickListener(this);

            rowLayout.addView(tmp);
        }

        return rowLayout;
    }

    public LinearLayout CreateFarbauswahl(Context context)
    {
        LinearLayout ausw = new LinearLayout(context);
        ausw.setOrientation(LinearLayout.HORIZONTAL);
        ausw.setHorizontalGravity(Gravity.CENTER);
        ausw.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        int minval = 0;
        if (_empty)
        {
            minval = -1;
        }

        for (int i = minval; i < _anzColors; i++)
        {
            ImageView tmp = new ImageView(this);
            /*switch(i)
            {
                case -1:
                    tmp.setImageResource(R.mipmap.ic_slot);
                    break;
                case 0:
                    tmp.setImageResource(R.mipmap.ic_blue);
                    break;
                case 1:
                    tmp.setImageResource(R.mipmap.ic_green);
                    break;
                case 2:
                    tmp.setImageResource(R.mipmap.ic_lightblue);
                    break;
                case 3:
                    tmp.setImageResource(R.mipmap.ic_pink);
                    break;
                case 4:
                    tmp.setImageResource(R.mipmap.ic_red);
                    break;
                case 5:
                    tmp.setImageResource(R.mipmap.ic_yellow);
                    break;
                case 6:
                    tmp.setImageResource(R.mipmap.ic_grey);
                    break;
                case 7:
                    tmp.setImageResource(R.mipmap.ic_purple);
                    break;
            }*/
            switch(i)
            {
                case -1:
                    tmp.setImageResource(R.mipmap.ic_slot);
                    break;
                default:
                    tmp.setImageResource(_gameColors[i]);
                    break;
            }
            tmp.setLayoutParams(new LinearLayout.LayoutParams(_farbAuswPeg,_farbAuswPeg));
            tmp.setId(i);
            tmp.setOnClickListener(this);

            ausw.addView(tmp);
        }

        return ausw;
    }

    public Row ResetFarbvorschlagRow()
    {
        Row tmp = new Row(_anzFields);

        for (int i = 0; i < _anzFields; i++)
        {
            Field tmpfield = new Field();
            tmpfield.setColor(-1);
            tmp.Fields[i] = tmpfield;
        }

        tmp.RightColor = 0;
        tmp.RightPlace = 0;

        return tmp;
    }

    public void ShowFarbauswahl(int id)
    {
        _farbauswahl.setVisibility(LinearLayout.VISIBLE);
        _activeField = id;
    }

    public void HideFarbauswahl()
    {
        _farbauswahl.setVisibility(LinearLayout.INVISIBLE);
        _activeField = -1;
    }

    @Override
    public void onClick(View v)
    {
        ImageView image;

        if (!_helpShown)
        {
            switch (v.getId())
            {
                //Buttons in farbauswahl
                case -1:
                    image = (ImageView) findViewById(_activeField);
                    image.setImageResource(R.mipmap.ic_slot);
                    _farbvorschlagRow.Fields[_activeField - 10].setColor(-1);
                    HideFarbauswahl();
                    break;
                case 0:
                    image = (ImageView) findViewById(_activeField);
                    image.setImageResource(_gameColors[0]);
                    _farbvorschlagRow.Fields[_activeField - 10].setColor(0);
                    HideFarbauswahl();
                    break;
                case 1:
                    image = (ImageView) findViewById(_activeField);
                    image.setImageResource(_gameColors[1]);
                    _farbvorschlagRow.Fields[_activeField - 10].setColor(1);
                    HideFarbauswahl();
                    break;
                case 2:
                    image = (ImageView) findViewById(_activeField);
                    image.setImageResource(_gameColors[2]);
                    _farbvorschlagRow.Fields[_activeField - 10].setColor(2);
                    HideFarbauswahl();
                    break;
                case 3:
                    image = (ImageView) findViewById(_activeField);
                    image.setImageResource(_gameColors[3]);
                    _farbvorschlagRow.Fields[_activeField - 10].setColor(3);
                    HideFarbauswahl();
                    break;
                case 4:
                    image = (ImageView) findViewById(_activeField);
                    image.setImageResource(_gameColors[4]);
                    _farbvorschlagRow.Fields[_activeField - 10].setColor(4);
                    HideFarbauswahl();
                    break;
                case 5:
                    image = (ImageView) findViewById(_activeField);
                    image.setImageResource(_gameColors[5]);
                    _farbvorschlagRow.Fields[_activeField - 10].setColor(5);
                    HideFarbauswahl();
                    break;
                case 6:
                    image = (ImageView) findViewById(_activeField);
                    image.setImageResource(_gameColors[6]);
                    _farbvorschlagRow.Fields[_activeField - 10].setColor(6);
                    HideFarbauswahl();
                    break;
                case 7:
                    image = (ImageView) findViewById(_activeField);
                    image.setImageResource(_gameColors[7]);
                    _farbvorschlagRow.Fields[_activeField - 10].setColor(7);
                    HideFarbauswahl();
                    break;
                //Buttons in Farbvorschlag
                case 10:
                    ShowFarbauswahl(v.getId());
                    break;
                case 11:
                    ShowFarbauswahl(v.getId());
                    break;
                case 12:
                    ShowFarbauswahl(v.getId());
                    break;
                case 13:
                    ShowFarbauswahl(v.getId());
                    break;
                case 14:
                    ShowFarbauswahl(v.getId());
                    break;
                case 15:
                    ShowFarbauswahl(v.getId());
                    break;
                case 16:
                    ShowFarbauswahl(v.getId());
                    break;
                case 17:
                    ShowFarbauswahl(v.getId());
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public void onBackPressed(){
        new AlertDialog.Builder(this)
                .setMessage(R.string.game_quit)
                .setCancelable(true)
                .setNegativeButton(R.string.btn_yes, new DialogInterface.OnClickListener()
                {
                    public void onClick(final DialogInterface dialog, final int id)
                    {
                        finish();
                    }
                })
                .setPositiveButton(R.string.btn_no, new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(final DialogInterface dialog, final int which)
                    {
                        dialog.dismiss();
                    }
                }).create().show();
    }
}
