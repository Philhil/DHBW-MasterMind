package de.dhbw.stuttgart.mastermind;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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

public class Colorcode extends AppCompatActivity implements View.OnClickListener{

    private int _anzFields;
    private int _anzColors;
    private boolean _multiple;
    private boolean _empty;

    private int _displayWidth;
    private int _bigPeg;
    private int _farbAuswPeg;

    private Row _farbvorschlagRow;
    private LinearLayout _farbauswahl;
    private int _activeField = -1;


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
    }

    public void startGame(View view) {
        Intent intent = new Intent(this, GameActivity.class);
        Row masterrow;

        boolean eval = true;
        if (!_empty)
        {
            //if all fields are filled when duplicates aren't allowed
            for (int i = 0; i < _anzFields; i++)
            {
                if (_farbvorschlagRow.Fields[i].getColor() == -1)
                {
                    eval = false;
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
        SharedPreferences sharedPref = this.getSharedPreferences(getString(R.string.preference_file_key), MODE_PRIVATE);

        _anzColors = sharedPref.getInt(getString(R.string.prefkey_number_of_colors), 5);
        _anzFields = sharedPref.getInt(getString(R.string.prefkey_number_of_holes), 4);
        _multiple = sharedPref.getBoolean(getString(R.string.prefkey_multiple), false);
        _empty = sharedPref.getBoolean(getString(R.string.prefkey_empty), false);
    }

    public LinearLayout CreateDisplayableRow(Context context, Row row)
    {
        LinearLayout rowLayout = new LinearLayout(context);
        rowLayout.setOrientation(LinearLayout.HORIZONTAL);
        rowLayout.setHorizontalGravity(Gravity.CENTER);
        rowLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        for (int i = 0; i < _anzFields; i++)
        {
            ImageView tmp = row.Fields[i].getPicture(this);
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
            switch(i)
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

        switch(v.getId())
        {
            //Buttons in farbauswahl
            case -1:
                image = (ImageView) findViewById(_activeField);
                image.setImageResource(R.mipmap.ic_slot);
                _farbvorschlagRow.Fields[_activeField -10].setColor(-1);
                HideFarbauswahl();
                break;
            case 0:
                image = (ImageView) findViewById(_activeField);
                image.setImageResource(R.mipmap.ic_blue);
                _farbvorschlagRow.Fields[_activeField -10].setColor(0);
                HideFarbauswahl();
                break;
            case 1:
                image = (ImageView) findViewById(_activeField);
                image.setImageResource(R.mipmap.ic_green);
                _farbvorschlagRow.Fields[_activeField -10].setColor(1);
                HideFarbauswahl();
                break;
            case 2:
                image = (ImageView) findViewById(_activeField);
                image.setImageResource(R.mipmap.ic_lightblue);
                _farbvorschlagRow.Fields[_activeField -10].setColor(2);
                HideFarbauswahl();
                break;
            case 3:
                image = (ImageView) findViewById(_activeField);
                image.setImageResource(R.mipmap.ic_pink);
                _farbvorschlagRow.Fields[_activeField -10].setColor(3);
                HideFarbauswahl();
                break;
            case 4:
                image = (ImageView) findViewById(_activeField);
                image.setImageResource(R.mipmap.ic_red);
                _farbvorschlagRow.Fields[_activeField -10].setColor(4);
                HideFarbauswahl();
                break;
            case 5:
                image = (ImageView) findViewById(_activeField);
                image.setImageResource(R.mipmap.ic_yellow);
                _farbvorschlagRow.Fields[_activeField -10].setColor(5);
                HideFarbauswahl();
                break;
            case 6:
                image = (ImageView) findViewById(_activeField);
                image.setImageResource(R.mipmap.ic_grey);
                _farbvorschlagRow.Fields[_activeField -10].setColor(6);
                HideFarbauswahl();
                break;
            case 7:
                image = (ImageView) findViewById(_activeField);
                image.setImageResource(R.mipmap.ic_purple);
                _farbvorschlagRow.Fields[_activeField -10].setColor(7);
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
