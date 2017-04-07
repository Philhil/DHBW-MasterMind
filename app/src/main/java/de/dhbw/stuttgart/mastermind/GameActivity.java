package de.dhbw.stuttgart.mastermind;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Point;
import android.os.SystemClock;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.view.View.OnClickListener;
import android.widget.TextView;

import java.util.Random;

public class GameActivity extends AppCompatActivity implements OnClickListener{

    //TODO: defines, should later come from the settings or the saved game
    public int AnzRows = 10;
    //TODO: if _anzFields < _anzColors -> same colors must be switched on in settings
    private int _anzFields = 4;
    private int _anzColors = 5;

    private int _displayWidth;
    private int _bigPeg;
    private int _smallPeg;
    private int _smallPin;
    private long _pause_timeDifference = 0;

    public int ActiveField = -1;
    public int ActiveRow = 0;

    public Row Rows[] = new Row[AnzRows];
    public Row Master;
    public LinearLayout Farbauswahl;
    public Row FarbvorschlagRow;

    public void SetMaster(boolean same)
    {
        Row tmp = new Row(_anzFields);
        Random random = new Random();

        if (!same)
        {
            boolean[] ref = new boolean[_anzColors];

            for (int i = 0; i < _anzFields; i++)
            {
                Field tmpfield = new Field();

                int r;

                do {
                    r = random.nextInt(_anzColors);
                } while (ref[r]);

                ref[r] = true;

                tmpfield.setColor(r);
                tmp.Fields[i] = tmpfield;
            }
        }
        else
        {
            for (int i = 0; i < _anzFields; i++)
            {
                Field tmpfield = new Field();

                tmpfield.setColor(random.nextInt(_anzColors));
                tmp.Fields[i] = tmpfield;
            }
        }

        tmp.RightColor = 0;
        tmp.RightPlace = _anzFields;

        Master = tmp;
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

    public void UpdateFarbvorschlagRow()
    {
        for (int i = 0; i < _anzFields; i++)
        {
            ImageView tmp;
            tmp = (ImageView) findViewById(i+10);
            tmp.setImageResource(R.mipmap.ic_slot);
        }
    }

    public LinearLayout CreateMasterRow(Context context, Row row)
    {
        LinearLayout rowLayout = new LinearLayout(context);
        rowLayout.setOrientation(LinearLayout.HORIZONTAL);
        rowLayout.setHorizontalGravity(Gravity.CENTER);
        rowLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        for (int i = 0; i < _anzFields; i++)
        {
            ImageView tmp = row.Fields[i].getPicture(this);
            tmp.setLayoutParams(new LinearLayout.LayoutParams(_bigPeg,_bigPeg));
            rowLayout.addView(tmp);
        }

        return rowLayout;
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

    public LinearLayout CreateDisplayableRowWithPins(Context context, Row row)
    {
        LinearLayout rowLayout = new LinearLayout(context);
        rowLayout.setOrientation(LinearLayout.HORIZONTAL);
        rowLayout.setHorizontalGravity(Gravity.CENTER);
        rowLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        TextView rowNumber = new TextView(context);
        rowNumber.append((ActiveRow + 1) + ". ");
        rowNumber.setTextSize(25);
        rowLayout.addView(rowNumber);

        for (int i = 0; i < _anzFields; i++)
        {
            ImageView tmp = row.Fields[i].getPicture(this);
            tmp.setLayoutParams(new LinearLayout.LayoutParams(_smallPeg,_smallPeg));
            rowLayout.addView(tmp);
        }

        LinearLayout pinsLayout = new LinearLayout(context);
        pinsLayout.setOrientation(LinearLayout.VERTICAL);
        pinsLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT));

        LinearLayout pinsTop = new LinearLayout(context);
        pinsTop.setOrientation(LinearLayout.HORIZONTAL);
        pinsTop.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        LinearLayout pinsBottom = new LinearLayout(context);
        pinsBottom.setOrientation(LinearLayout.HORIZONTAL);
        pinsBottom.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        int white = row.RightColor;
        int black = row.RightPlace;
        for (int j = 0; j < (_anzFields-(_anzFields/2)); j++)
        {
            ImageView tmp = new ImageView(this);
            if (white > 0)
            {
                tmp.setImageResource(R.mipmap.ic_white);
                tmp.setLayoutParams(new LinearLayout.LayoutParams(_smallPin,_smallPin));
                white--;
            }
            else if (black > 0)
            {
                tmp.setImageResource(R.mipmap.ic_black);
                tmp.setLayoutParams(new LinearLayout.LayoutParams(_smallPin,_smallPin));
                black--;
            }
            else
            {
                tmp.setImageResource(R.mipmap.ic_grey_round);
                tmp.setLayoutParams(new LinearLayout.LayoutParams(_smallPin,_smallPin));
                tmp.setVisibility(ImageView.INVISIBLE);
            }
            pinsTop.addView(tmp);
        }

        for (int j = 0; j < (_anzFields/2); j++)
        {
            ImageView tmp = new ImageView(this);
            if (white > 0)
            {
                tmp.setImageResource(R.mipmap.ic_white);
                tmp.setLayoutParams(new LinearLayout.LayoutParams(_smallPin,_smallPin));
                white--;
            }
            else if (black > 0)
            {
                tmp.setImageResource(R.mipmap.ic_black);
                tmp.setLayoutParams(new LinearLayout.LayoutParams(_smallPin,_smallPin));
                black--;
            }
            else
            {
                tmp.setImageResource(R.mipmap.ic_grey_round);
                tmp.setLayoutParams(new LinearLayout.LayoutParams(_smallPin,_smallPin));
                tmp.setVisibility(ImageView.INVISIBLE);
            }
            pinsBottom.addView(tmp);
        }

        pinsLayout.addView(pinsTop);
        pinsLayout.addView(pinsBottom);

        rowLayout.addView(pinsLayout);

        return rowLayout;
    }

    public void CreateFarbauswahl(Context context)
    {
        LinearLayout ausw = new LinearLayout(context);
        ausw.setOrientation(LinearLayout.HORIZONTAL);
        ausw.setHorizontalGravity(Gravity.CENTER);
        ausw.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        for (int i = 0; i < _anzColors; i++)
        {
            ImageView tmp = new ImageView(this);
            switch(i)
            {
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
            tmp.setLayoutParams(new LinearLayout.LayoutParams(_bigPeg,_bigPeg));
            tmp.setId(i);
            tmp.setOnClickListener(this);

            ausw.addView(tmp);
        }

        Farbauswahl = ausw;
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

    public boolean EvaluateFarbvorschlagRow(int rowNo)
    {
        boolean[] codeUsed = new boolean[Master.Fields.length];
        boolean[] guessUsed = new boolean[Rows[rowNo].Fields.length];

        // Compare correct color and position
        for (int i = 0; i < Master.Fields.length; i++) {
            if (Master.Fields[i].getColor() == Rows[rowNo].Fields[i].getColor()) {
                Rows[rowNo].RightPlace++;
                codeUsed[i] = guessUsed[i] = true;
            }
        }

        // Compare matching colors for "pins" that were not used
        for (int i = 0; i < Master.Fields.length; i++) {
            for (int j = 0; j < Rows[rowNo].Fields.length; j++) {
                if (!codeUsed[i] && !guessUsed[j] && Master.Fields[i].getColor() == Rows[rowNo].Fields[j].getColor()) {
                    Rows[rowNo].RightColor++;
                    codeUsed[i] = guessUsed[j] = true;
                    break;
                }
            }
        }


        return Rows[rowNo].RightPlace == Master.Fields.length;
    }

    private void ShowPopup(String msg, boolean end)
    {
        if(end)
        {
            gameHasEnded();
        }

        final Intent again = new Intent(this, GameActivity.class);
        new AlertDialog.Builder(this)
                .setMessage(msg)
                .setCancelable(true)
                .setNeutralButton("Neustart", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        startActivity(again);
                        finish();
                    }
                }).setNegativeButton("Zurück zum Hauptmenü", new DialogInterface.OnClickListener() {
            public void onClick(final DialogInterface dialog, final int id) {
                finish();
            }
        }).setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(final DialogInterface dialog, final int which) {
                dialog.dismiss();
            }
        }).create().show();
    }

    private void chronometer(boolean start) {

        Chronometer chronometer = (Chronometer) findViewById(R.id.time);
        if (start)
        {
            chronometer.setBase(_pause_timeDifference + SystemClock.elapsedRealtime());
            chronometer.start();
            _pause_timeDifference = 0;
        }
        else
        {
            chronometer.stop();
            _pause_timeDifference  = chronometer.getBase() - SystemClock.elapsedRealtime();
        }
    }

    private void gameHasEnded()
    {
        LinearLayout masterCode = (LinearLayout) findViewById(R.id.game_mastercode);
        masterCode.setVisibility(LinearLayout.VISIBLE);

        //make all buttons unusable
        Button button = (Button) findViewById(R.id.btn_game_rueckgaengig);
        button.setText(R.string.btn_main_menue);
        button.setId(R.id.btn_main_menue);
        button = (Button) findViewById(R.id.btn_game_aufloesen);
        button.setEnabled(false);
        button = (Button) findViewById(R.id.btn_game_pause);
        button.setEnabled(false);
        button = (Button) findViewById(R.id.btn_game_pruefen);
        button.setText(R.string.btn_new_game);
        button.setId(R.id.btn_new_game);

        //delete farbauswahl and farbvorschlag
        LinearLayout farbvorschlag = (LinearLayout) findViewById(R.id.farbvorschlag);
        farbvorschlag.removeAllViewsInLayout();

        ((Chronometer) findViewById(R.id.time)).stop();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        _displayWidth = size.x;

        _bigPeg = _displayWidth/8;
        _smallPeg = (_displayWidth/8)-20;
        _smallPin = _smallPeg/2;

        SetMaster(false);
        CreateFarbauswahl(this);
        ResetFarbvorschlagRow();

        //add mastercode, but invisible
        LinearLayout masterCode = (LinearLayout) findViewById(R.id.game_mastercode);
        masterCode.setVisibility(LinearLayout.INVISIBLE);
        masterCode.addView(CreateMasterRow(this, Master));
        // add a row with the buttons to touch
        LinearLayout farbvorschlag = (LinearLayout) findViewById(R.id.farbvorschlag);
        farbvorschlag.addView(Farbauswahl);
        Farbauswahl.setVisibility(LinearLayout.INVISIBLE);
        farbvorschlag.addView(CreateDisplayableRow(this, FarbvorschlagRow));

        Button button = (Button) findViewById(R.id.btn_game_aufloesen);
        button.setOnClickListener(this);
        button = (Button) findViewById(R.id.btn_game_pruefen);
        button.setOnClickListener(this);
        button = (Button) findViewById(R.id.btn_game_pause);
        button.setOnClickListener(this);
        button = (Button) findViewById(R.id.btn_game_rueckgaengig);
        button.setOnClickListener(this);

        chronometer(true);
    }

    @Override
    public void onClick(View v)
    {
        ImageView image;
        LinearLayout gamefield;

        switch(v.getId())
        {
            case R.id.btn_game_aufloesen:
                ShowPopup("Du hast das Spiel aufgelöst!", true);
                break;
            case R.id.btn_game_pause:
                final Intent again = new Intent(this, GameActivity.class);
                new AlertDialog.Builder(this)
                        .setMessage(R.string.title_break)
                        .setPositiveButton(R.string.label_ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(final DialogInterface dialog, final int which) {
                                dialog.dismiss();
                                chronometer(true);
                            }
                        }).create().show();

                chronometer(false);
                break;
            case R.id.btn_game_rueckgaengig:
                if (ActiveRow>0)
                {
                    gamefield = (LinearLayout) findViewById(R.id.game_field);
                    //gamefield.removeViewAt(ActiveRow-1);
                    gamefield.removeViewAt(0);
                    ActiveRow--;
                }
                break;
            case R.id.btn_game_pruefen:
                gamefield = (LinearLayout) findViewById(R.id.game_field);
                //if all fields are filled
                boolean eval = true;
                for (int i = 0; i < _anzFields;i++)
                {
                    if (FarbvorschlagRow.Fields[i].getColor() == -1)
                    {
                        eval = false;
                        break;
                    }
                }
                if (eval)
                {
                    Rows[ActiveRow] = FarbvorschlagRow;
                    if (EvaluateFarbvorschlagRow(ActiveRow))
                    {
                        long minutes = (((SystemClock.elapsedRealtime() - ((Chronometer) findViewById(R.id.time)).getBase()) / 1000))/60;
                        long seconds = (((SystemClock.elapsedRealtime() - ((Chronometer) findViewById(R.id.time)).getBase()) / 1000))%60;

                        //gamefield.addView(CreateDisplayableRowWithPins(this, Rows[ActiveRow]));
                        gamefield.addView(CreateDisplayableRowWithPins(this, Rows[ActiveRow]),0);
                        if (ActiveRow == 0)
                        {
                            ShowPopup("Glückwunsch! Du hast das Spiel nach " + String.format("%02d", minutes) + ":" + String.format("%02d", seconds) + " Sekunden und einem Zug gewonnen!", true);
                        }
                        else
                        {
                            ShowPopup("Glückwunsch! Du hast das Spiel nach " + String.format("%02d", minutes) + ":" + String.format("%02d", seconds) + " in " + (ActiveRow + 1) + " Zügen gewonnen!", true);
                        }
                        break;
                    }
                    //gamefield.addView(CreateDisplayableRowWithPins(this, Rows[ActiveRow]));
                    gamefield.addView(CreateDisplayableRowWithPins(this, Rows[ActiveRow]),0);
                    ActiveRow++;
                    if (ActiveRow == AnzRows)
                    {
                        ShowPopup("Du hast es nicht geschafft den Code zu knacken!", true);
                        break;
                    }
                    ResetFarbvorschlagRow();
                    UpdateFarbvorschlagRow();
                }
                break;
            case R.id.btn_main_menue:
                finish();
                break;
            case R.id.btn_new_game:
                Intent newGame = new Intent(this, GameActivity.class);
                newGame.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(newGame);
                break;
            //Buttons in farbauswahl
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
}
