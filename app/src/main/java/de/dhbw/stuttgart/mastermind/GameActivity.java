package de.dhbw.stuttgart.mastermind;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.view.View.OnClickListener;

import java.util.Random;

public class GameActivity extends AppCompatActivity implements OnClickListener{

    /*
        TODO:
        - make guesses and evaluation in one line
        - make game field scrollable
    */

    public int AnzRows = 10;
    private int _anzFields = 5;
    private int _anzColors = 6;

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

        if (same == false)
        {
            boolean[] ref = new boolean[_anzColors];

            for (int i = 0; i < _anzFields; i++)
            {
                Field tmpfield = new Field();

                int r;

                do {
                    r = random.nextInt(_anzColors);
                } while (ref[r] != false);

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
        rowLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        for (int i = 0; i < _anzFields; i++)
        {
            ImageView tmp = new ImageView(this);
            switch(row.Fields[i].getColor())
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
                default:
                    tmp.setImageResource(R.mipmap.ic_slot);
                    break;
            }
            rowLayout.addView(tmp);
        }

        return rowLayout;
    }

    public LinearLayout CreateDisplayableRow(Context context, Row row)
    {
        LinearLayout rowLayout = new LinearLayout(context);
        rowLayout.setOrientation(LinearLayout.HORIZONTAL);
        rowLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        for (int i = 0; i < _anzFields; i++)
        {
            ImageView tmp = new ImageView(this);
            switch(row.Fields[i].getColor())
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
                default:
                    tmp.setImageResource(R.mipmap.ic_slot);
                    break;
            }
            tmp.setId(i+10);
            tmp.setOnClickListener(this);

            rowLayout.addView(tmp);
        }

        return rowLayout;
    }

    public LinearLayout CreateDisplayableRowWithPins(Context context, Row row)
    {
        LinearLayout rowLayout = new LinearLayout(context);
        rowLayout.setOrientation(LinearLayout.VERTICAL);
        rowLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        LinearLayout one = new LinearLayout(context);
        one.setOrientation(LinearLayout.HORIZONTAL);
        one.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT));

        for (int i = 0; i < _anzFields; i++)
        {
            ImageView tmp = new ImageView(this);
            switch(row.Fields[i].getColor())
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
                default:
                    tmp.setImageResource(R.mipmap.ic_slot);
                    break;
            }

            one.addView(tmp);
        }


        LinearLayout two = new LinearLayout(context);
        two.setOrientation(LinearLayout.HORIZONTAL);
        two.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        int white = row.RightColor;
        int black = row.RightPlace;
        for (int j = 0; j < 4; j++)
        {
            ImageView tmp = new ImageView(this);
            if (white > 0)
            {
                tmp.setImageResource(R.mipmap.ic_white);
                two.addView(tmp);
                white--;
            }
            else if (black > 0)
            {
                tmp.setImageResource(R.mipmap.ic_black);
                two.addView(tmp);
                black--;
            }
        }

        for (int j = 0; j < _anzFields-4; j++)
        {
            ImageView tmp = new ImageView(this);
            if (white > 0)
            {
                tmp.setImageResource(R.mipmap.ic_white);
                two.addView(tmp);
                white--;
            }
            else if (black > 0)
            {
                tmp.setImageResource(R.mipmap.ic_black);
                two.addView(tmp);
                black--;
            }
        }

        rowLayout.addView(one);
        rowLayout.addView(two);

        return rowLayout;
    }

    public void CreateFarbauswahl(Context context)
    {
        LinearLayout ausw = new LinearLayout(context);
        ausw.setOrientation(LinearLayout.HORIZONTAL);
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


        if (Rows[rowNo].RightPlace == Master.Fields.length)
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    private void ShowWinningMessage()
    {
        LinearLayout masterCode = (LinearLayout) findViewById(R.id.game_mastercode);
        masterCode.setVisibility(LinearLayout.VISIBLE);

        final Intent again = new Intent(this, GameActivity.class);
        new AlertDialog.Builder(this)
                .setMessage("Glückwunsch! Du hast das Spiel in " + (ActiveRow+1) + " Zügen gewonnen!")
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

    protected void ShowLoserMessage()
    {
        LinearLayout masterCode = (LinearLayout) findViewById(R.id.game_mastercode);
        masterCode.setVisibility(LinearLayout.VISIBLE);

        final Intent again = new Intent(this, GameActivity.class);
        new AlertDialog.Builder(this)
                .setMessage("Du hast es nicht geschafft den Code zu knacken!")
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

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
    }

    @Override
    public void onClick(View v)
    {
        ImageView image;
        LinearLayout gamefield;

        switch(v.getId())
        {
            case R.id.btn_game_aufloesen:
                LinearLayout masterCode = (LinearLayout) findViewById(R.id.game_mastercode);
                masterCode.setVisibility(LinearLayout.VISIBLE);
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
                if (eval == true)
                {
                    Rows[ActiveRow] = FarbvorschlagRow;
                    if (EvaluateFarbvorschlagRow(ActiveRow) == true)
                    {
                        gamefield.addView(CreateDisplayableRowWithPins(this, Rows[ActiveRow]));
                        ShowWinningMessage();
                        break;
                    }
                    gamefield.addView(CreateDisplayableRowWithPins(this, Rows[ActiveRow]));
                    ActiveRow++;
                    if (ActiveRow == AnzRows)
                    {
                        ShowLoserMessage();
                        break;
                    }
                    ResetFarbvorschlagRow();
                    UpdateFarbvorschlagRow();
                }
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
