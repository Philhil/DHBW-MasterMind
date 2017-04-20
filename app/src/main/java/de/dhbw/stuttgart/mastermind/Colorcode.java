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

    public Row FarbvorschlagRow;
    public LinearLayout Farbauswahl;
    public int ActiveField = -1;


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

        CreateFarbauswahl(this);
        ResetFarbvorschlagRow();

        // add a row with the buttons to touch
        LinearLayout farbvorschlag = (LinearLayout) findViewById(R.id.colorcode_farbvorschlag);
        farbvorschlag.addView(Farbauswahl);
        Farbauswahl.setVisibility(LinearLayout.INVISIBLE);
        farbvorschlag.addView(CreateDisplayableRow(this, FarbvorschlagRow));

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
                if (FarbvorschlagRow.Fields[i].getColor() == -1)
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
                if (!colors[FarbvorschlagRow.Fields[i].getColor()+1])
                {
                    colors[FarbvorschlagRow.Fields[i].getColor()+1] = true;
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
            masterrow = FarbvorschlagRow;
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

    public void CreateFarbauswahl(Context context)
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

        Farbauswahl = ausw;
    }

    public void ResetFarbvorschlagRow()
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

        FarbvorschlagRow = tmp;
    }

    public void ShowFarbauswahl(int id)
    {
        Farbauswahl.setVisibility(LinearLayout.VISIBLE);
        ActiveField = id;
    }

    public void HideFarbauswahl()
    {
        Farbauswahl.setVisibility(LinearLayout.INVISIBLE);
        ActiveField = -1;
    }

    @Override
    public void onClick(View v)
    {
        ImageView image;

        switch(v.getId())
        {
            //Buttons in farbauswahl
            case -1:
                image = (ImageView) findViewById(ActiveField);
                image.setImageResource(R.mipmap.ic_slot);
                FarbvorschlagRow.Fields[ActiveField-10].setColor(-1);
                HideFarbauswahl();
                break;
            case 0:
                image = (ImageView) findViewById(ActiveField);
                image.setImageResource(R.mipmap.ic_blue);
                FarbvorschlagRow.Fields[ActiveField-10].setColor(0);
                HideFarbauswahl();
                break;
            case 1:
                image = (ImageView) findViewById(ActiveField);
                image.setImageResource(R.mipmap.ic_green);
                FarbvorschlagRow.Fields[ActiveField-10].setColor(1);
                HideFarbauswahl();
                break;
            case 2:
                image = (ImageView) findViewById(ActiveField);
                image.setImageResource(R.mipmap.ic_lightblue);
                FarbvorschlagRow.Fields[ActiveField-10].setColor(2);
                HideFarbauswahl();
                break;
            case 3:
                image = (ImageView) findViewById(ActiveField);
                image.setImageResource(R.mipmap.ic_pink);
                FarbvorschlagRow.Fields[ActiveField-10].setColor(3);
                HideFarbauswahl();
                break;
            case 4:
                image = (ImageView) findViewById(ActiveField);
                image.setImageResource(R.mipmap.ic_red);
                FarbvorschlagRow.Fields[ActiveField-10].setColor(4);
                HideFarbauswahl();
                break;
            case 5:
                image = (ImageView) findViewById(ActiveField);
                image.setImageResource(R.mipmap.ic_yellow);
                FarbvorschlagRow.Fields[ActiveField-10].setColor(5);
                HideFarbauswahl();
                break;
            case 6:
                image = (ImageView) findViewById(ActiveField);
                image.setImageResource(R.mipmap.ic_grey);
                FarbvorschlagRow.Fields[ActiveField-10].setColor(6);
                HideFarbauswahl();
                break;
            case 7:
                image = (ImageView) findViewById(ActiveField);
                image.setImageResource(R.mipmap.ic_purple);
                FarbvorschlagRow.Fields[ActiveField-10].setColor(7);
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
