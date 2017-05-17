package de.dhbw.stuttgart.mastermind;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Point;
import android.os.SystemClock;
import android.os.Vibrator;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.MenuItemHoverListener;
import android.text.InputType;
import android.view.Display;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.amlcurran.showcaseview.ShowcaseView;
import com.github.amlcurran.showcaseview.targets.ActionViewTarget;
import com.github.amlcurran.showcaseview.targets.Target;
import com.github.amlcurran.showcaseview.targets.ViewTarget;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.Random;

public class GameActivity extends AppCompatActivity implements OnClickListener
{

    private int _anzRows;
    private int _anzFields;
    private int _anzColors;
    private boolean _multiple;
    private boolean _empty;

    private boolean _undo = false;

    private int _backgroundColor;

    private boolean _singlePlayer;
    private boolean _saveGame = false;

    private int _displayWidth;
    private int _bigPeg;
    private int _farbAuswPeg;
    private int _smallPeg;
    private int _smallPin;
    private long _pause_timeDifference = 0;
    private String _winTimestring;

    private int _gameColors[];

    private int _activeField = -1;
    private int _activeRow = 0;

    private Row _rows[];
    private Row _master;
    private LinearLayout _farbauswahl;
    private Row _farbforschlagRow;

    private ShowcaseView _showcaseView;
    private int _counter = 0;
    private boolean _showHelp = false;
    private boolean _helpShown = false;

    public Row SetMaster(boolean same, boolean empty)
    {
        Row tmp;

        tmp = new Row(_anzFields);
        Random random = new Random();

        if (!same)
        {
            boolean[] ref = new boolean[_anzColors + 1];

            for (int i = 0; i < _anzFields; i++)
            {
                Field tmpfield = new Field();

                int r;

                do
                {
                    if (!empty)
                    {
                        r = random.nextInt(_anzColors);
                    } else
                    {
                        r = random.nextInt(_anzColors + 1) - 1;
                    }
                } while (ref[r + 1]);

                ref[r + 1] = true;

                tmpfield.setColor(r);
                tmp.Fields[i] = tmpfield;
            }
        } else
        {
            for (int i = 0; i < _anzFields; i++)
            {
                Field tmpfield = new Field();

                if (!empty)
                {
                    tmpfield.setColor(random.nextInt(_anzColors));
                } else
                {
                    tmpfield.setColor(random.nextInt(_anzColors + 1) - 1);
                }
                tmp.Fields[i] = tmpfield;
            }
        }

        tmp.RightColor = 0;
        tmp.RightPlace = _anzFields;


        return tmp;
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

    public void UpdateFarbvorschlagRow()
    {
        for (int i = 0; i < _anzFields; i++)
        {
            ImageView tmp;
            tmp = (ImageView) findViewById(i + 10);
            tmp.setImageResource(R.mipmap.ic_slot);
        }
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

    public LinearLayout CreateMasterRow(Context context, Row row)
    {
        LinearLayout rowLayout = new LinearLayout(context);
        rowLayout.setOrientation(LinearLayout.HORIZONTAL);
        rowLayout.setHorizontalGravity(Gravity.CENTER);
        rowLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        for (int i = 0; i < _anzFields; i++)
        {
            ImageView tmp = getPictureToColor(row.Fields[i].getColor());
            tmp.setLayoutParams(new LinearLayout.LayoutParams(_bigPeg, _bigPeg));
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
            ImageView tmp = getPictureToColor(row.Fields[i].getColor());
            tmp.setLayoutParams(new LinearLayout.LayoutParams(_bigPeg, _bigPeg));
            tmp.setId(i + 10);
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

        /*
        TextView rowNumber = new TextView(context);
        rowNumber.append((_activeRow + 1) + ". ");
        rowNumber.setTextSize(25);
        rowLayout.addView(rowNumber);*/

        for (int i = 0; i < _anzFields; i++)
        {
            ImageView tmp = getPictureToColor(row.Fields[i].getColor());
            tmp.setLayoutParams(new LinearLayout.LayoutParams(_smallPeg, _smallPeg));
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
        for (int j = 0; j < (_anzFields - (_anzFields / 2)); j++)
        {
            ImageView tmp = new ImageView(this);
            if (white > 0)
            {
                tmp.setImageResource(R.mipmap.ic_white);
                tmp.setLayoutParams(new LinearLayout.LayoutParams(_smallPin, _smallPin));
                white--;
            } else if (black > 0)
            {
                tmp.setImageResource(R.mipmap.ic_black);
                tmp.setLayoutParams(new LinearLayout.LayoutParams(_smallPin, _smallPin));
                black--;
            } else
            {
                tmp.setImageResource(R.mipmap.ic_grey_round);
                tmp.setLayoutParams(new LinearLayout.LayoutParams(_smallPin, _smallPin));
                tmp.setVisibility(ImageView.INVISIBLE);
            }
            pinsTop.addView(tmp);
        }

        for (int j = 0; j < (_anzFields / 2); j++)
        {
            ImageView tmp = new ImageView(this);
            if (white > 0)
            {
                tmp.setImageResource(R.mipmap.ic_white);
                tmp.setLayoutParams(new LinearLayout.LayoutParams(_smallPin, _smallPin));
                white--;
            } else if (black > 0)
            {
                tmp.setImageResource(R.mipmap.ic_black);
                tmp.setLayoutParams(new LinearLayout.LayoutParams(_smallPin, _smallPin));
                black--;
            } else
            {
                tmp.setImageResource(R.mipmap.ic_grey_round);
                tmp.setLayoutParams(new LinearLayout.LayoutParams(_smallPin, _smallPin));
                tmp.setVisibility(ImageView.INVISIBLE);
            }
            pinsBottom.addView(tmp);
        }

        pinsLayout.addView(pinsTop);
        pinsLayout.addView(pinsBottom);

        rowLayout.addView(pinsLayout);

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
            /*switch (i)
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
            tmp.setLayoutParams(new LinearLayout.LayoutParams(_farbAuswPeg, _farbAuswPeg));
            tmp.setId(i);
            tmp.setOnClickListener(this);

            ausw.addView(tmp);
        }

        return ausw;
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

    public boolean EvaluateFarbvorschlagRow(int rowNo)
    {
        boolean[] codeUsed = new boolean[_master.Fields.length];
        boolean[] guessUsed = new boolean[_rows[rowNo].Fields.length];

        // Compare correct color and position
        for (int i = 0; i < _master.Fields.length; i++)
        {
            if (_master.Fields[i].getColor() == _rows[rowNo].Fields[i].getColor())
            {
                _rows[rowNo].RightPlace++;
                codeUsed[i] = guessUsed[i] = true;
            }
        }

        // Compare matching colors for "pins" that were not used
        for (int i = 0; i < _master.Fields.length; i++)
        {
            for (int j = 0; j < _rows[rowNo].Fields.length; j++)
            {
                if (!codeUsed[i] && !guessUsed[j] && _master.Fields[i].getColor() == _rows[rowNo].Fields[j].getColor())
                {
                    _rows[rowNo].RightColor++;
                    codeUsed[i] = guessUsed[j] = true;
                    break;
                }
            }
        }


        return _rows[rowNo].RightPlace == _master.Fields.length;
    }

    private void ShowPopup(String msg, boolean end, boolean win)
    {
        if (end)
        {
            gameHasEnded();
        }
        if (!win || _undo)
        {
            final Intent again;
            if (_singlePlayer)
            {
                again = new Intent(this, GameActivity.class);
            } else
            {
                again = new Intent(this, Colorcode.class);
            }
            new AlertDialog.Builder(this)
                    .setMessage(msg)
                    .setCancelable(false)
                    .setNeutralButton("Neustart", new DialogInterface.OnClickListener()
                    {
                        public void onClick(final DialogInterface dialog, final int id)
                        {
                            startActivity(again);
                            finish();
                        }
                    }).setNegativeButton("Zurück zum Hauptmenü", new DialogInterface.OnClickListener()
            {
                public void onClick(final DialogInterface dialog, final int id)
                {
                    finish();
                }
            }).setPositiveButton("OK", new DialogInterface.OnClickListener()
            {
                @Override
                public void onClick(final DialogInterface dialog, final int which)
                {
                    dialog.dismiss();
                }
            }).create().show();
        } else
        {
            final Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

            if (v.hasVibrator())
            {
                //long[] pattern = {0, 500, 110, 500, 110, 450, 110, 200, 110, 170, 40, 450, 110, 200, 110, 170, 40, 500};
                long[] pattern = {10,10};

                v.vibrate(pattern, -1);
            }

            final Intent again;
            if (_singlePlayer)
            {
                again = new Intent(this, GameActivity.class);
            } else
            {
                again = new Intent(this, Colorcode.class);
            }
            final EditText name = new EditText(this);
            name.setInputType(InputType.TYPE_CLASS_TEXT);
            name.setText("Spielername", TextView.BufferType.EDITABLE);
            new AlertDialog.Builder(this)
                    .setMessage(msg)
                    .setCancelable(false)
                    .setView(name)
                    .setNeutralButton("Neustart", new DialogInterface.OnClickListener()
                    {
                        public void onClick(final DialogInterface dialog, final int id)
                        {
                            startActivity(again);
                            v.cancel();
                            finish();
                        }
                    }).setNegativeButton("Zurück zum Hauptmenü", new DialogInterface.OnClickListener()
            {
                public void onClick(final DialogInterface dialog, final int id)
                {
                    v.cancel();
                    finish();
                }
            }).setPositiveButton("Highscore speichern", new DialogInterface.OnClickListener()
            {
                @Override
                public void onClick(final DialogInterface dialog, final int which)
                {
                    HighscoreDataSource ds = new HighscoreDataSource(GameActivity.this);
                    ds.open();
                    ds.createHighscoreItem(name.getText().toString(), _winTimestring, _activeRow + 1, _anzColors, _anzFields);
                    ds.close();
                    v.cancel();
                    dialog.dismiss();
                }
            }).create().show();
        }
    }

    private void chronometer(boolean start)
    {

        Chronometer chronometer = (Chronometer) findViewById(R.id.time);
        if (start)
        {
            chronometer.setBase(_pause_timeDifference + SystemClock.elapsedRealtime());
            chronometer.start();
            _pause_timeDifference = 0;
        } else
        {
            chronometer.stop();
            _pause_timeDifference = chronometer.getBase() - SystemClock.elapsedRealtime();
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

        if (_saveGame)
        {
            //Delete Savegame from db
            SavegameDataSource ds = new SavegameDataSource(GameActivity.this);
            ds.open();
            ds.deleteSavegameItem(getIntent().getLongExtra("id", -1));
            ds.close();
        }
    }

    private void getSettingsFromPreferences()
    {
        int[] pictures = {R.mipmap.ic_red, R.mipmap.ic_green, R.mipmap.ic_purple, R.mipmap.ic_blue, R.mipmap.ic_grey, R.mipmap.ic_pink, R.mipmap.ic_turquoise, R.mipmap.ic_yellow, R.mipmap.ic_brown};


        SharedPreferences sharedPref = this.getSharedPreferences(getString(R.string.preference_file_key), MODE_PRIVATE);

        _anzColors = sharedPref.getInt(getString(R.string.prefkey_number_of_colors), 5);
        _anzFields = sharedPref.getInt(getString(R.string.prefkey_number_of_holes), 4);
        _anzRows = sharedPref.getInt(getString(R.string.prefkey_number_of_rounds), 10);
        _multiple = sharedPref.getBoolean(getString(R.string.prefkey_multiple), false);
        _empty = sharedPref.getBoolean(getString(R.string.prefkey_empty), false);
        _backgroundColor = sharedPref.getInt(getString(R.string.prefkey_background_color), R.string.settings_backgroundWhite);
        _showHelp = sharedPref.getBoolean(getString(R.string.prefkey_help_game), true);
        int arr[] = new int[8];
        for(int i=0;i<8;i++)
        {
            arr[i] = sharedPref.getInt("color_" + i, pictures[i]);
        }
        _gameColors = arr;
    }

    private void loadSaveGame(Savegame tmp)
    {
        _anzColors = tmp.anzColors;
        _anzFields = tmp.anzFields;
        _anzRows = tmp.anzRows;
        _multiple = tmp.multiple;
        _empty = tmp.empty;
        _backgroundColor = tmp.backgroundColor;
        _singlePlayer = tmp.singlePlayer;
        _rows =  tmp.game;
        _master = tmp.master;
        _farbforschlagRow = tmp.farbvorschlag;
        _activeRow = tmp.activeRow;
        _pause_timeDifference = tmp.pause_timeDifference;
        _gameColors = tmp.gameColors;

        LinearLayout gamefield;
        gamefield = (LinearLayout) findViewById(R.id.game_field);
        for (int i = 0; i < _activeRow; i++)
        {
            gamefield.addView(CreateDisplayableRowWithPins(this, _rows[i]),0);
        }
    }

    private Savegame getSavegame()
    {
        return new Savegame(_rows, _master, _farbforschlagRow,_activeRow, _anzRows, _anzFields, _anzColors, _backgroundColor, _multiple, _empty, _undo, _singlePlayer, _pause_timeDifference, _gameColors);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        _displayWidth = size.x;

        _farbAuswPeg = _displayWidth/9;
        _bigPeg = _displayWidth/8;
        _smallPeg = (_displayWidth/8)-20;
        _smallPin = _smallPeg/2;

        Intent intent = getIntent();
        if (intent.hasExtra("masterRow"))
        {
            getSettingsFromPreferences();
            _master = intent.getParcelableExtra("masterRow");
            _rows = new Row[_anzRows];
            _singlePlayer = false;
            _farbforschlagRow = ResetFarbvorschlagRow();
        }
        else if (intent.hasExtra("saveGame"))
        {
            Gson gson = new Gson();
            String obj = intent.getStringExtra("saveGame");
            Savegame tmp = gson.fromJson(obj, new TypeToken<Savegame>(){}.getType());

            loadSaveGame(tmp);
            _saveGame = true;

            StartActivity.fragmentManager.popBackStack();
        }
        else
        {
            getSettingsFromPreferences();
            _master = SetMaster(_multiple, _empty);
            _rows = new Row[_anzRows];
            _singlePlayer = true;
            _farbforschlagRow = ResetFarbvorschlagRow();
        }

        _farbauswahl = CreateFarbauswahl(this);

        //add mastercode, but invisible
        LinearLayout masterCode = (LinearLayout) findViewById(R.id.game_mastercode);
        masterCode.setVisibility(LinearLayout.INVISIBLE);
        masterCode.addView(CreateMasterRow(this, _master));
        // add a row with the buttons to touch
        LinearLayout farbvorschlag = (LinearLayout) findViewById(R.id.farbvorschlag);
        farbvorschlag.addView(_farbauswahl);
        _farbauswahl.setVisibility(LinearLayout.INVISIBLE);
        farbvorschlag.addView(CreateDisplayableRow(this, _farbforschlagRow));

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
        findViewById(R.id.game).setBackgroundColor(color);


        Button button = (Button) findViewById(R.id.btn_game_aufloesen);
        button.setOnClickListener(this);
        button = (Button) findViewById(R.id.btn_game_pruefen);
        button.setOnClickListener(this);
        button = (Button) findViewById(R.id.btn_game_pause);
        button.setOnClickListener(this);
        button = (Button) findViewById(R.id.btn_game_rueckgaengig);
        button.setOnClickListener(this);

        if (_showHelp)
        {
            createShowcase();

            SharedPreferences sharedPref = this.getSharedPreferences(getString(R.string.preference_file_key), MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();

            editor.putBoolean(getString(R.string.prefkey_help_game), false);
            editor.commit();
        }
        else
        {
            chronometer(true);
        }
    }

    private void createShowcase()
    {
        _helpShown = true;
        _showcaseView = new ShowcaseView.Builder(this)
                .setTarget(Target.NONE)
                .setContentTitle("Spiel spielen")
                .setContentText("Hier versuchst du den Mastercode zu erraten. Wenn Du das Spiel gewinnst, kannst Du dich in die Highscoreliste eintragen.")
                .setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        switch (_counter) {
                            case 0:
                                _showcaseView.setShowcase(new ViewTarget(findViewById(R.id.game_footer)), true);
                                _showcaseView.setContentTitle("Farbe auswählen");
                                _showcaseView.setContentText("Hier kannst Du eine Farbe auswählen");
                                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                                params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
                                params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
                                _showcaseView.setButtonPosition(params);
                                break;
                            case 1:
                                _showcaseView.setShowcase(new ViewTarget(findViewById(R.id.btn_game_aufloesen)), true);
                                _showcaseView.setContentTitle("Spiel auflösen");
                                _showcaseView.setContentText("Hier kannst Du das Spiel auflösen");
                                break;
                            case 2:
                                _showcaseView.setShowcase(new ViewTarget(findViewById(R.id.btn_game_pause)), true);
                                _showcaseView.setContentTitle("Spiel pausieren");
                                _showcaseView.setContentText("Hier kannst Du das Spiel pausieren und speichern");
                                break;
                            case 3:
                                _showcaseView.setShowcase(new ViewTarget(findViewById(R.id.btn_game_rueckgaengig)), true);
                                _showcaseView.setContentTitle("Reihe rückgängig");
                                _showcaseView.setContentText("Hier kannst Du eine Reihe rückgängig machen. Dadurch lässt sich aber bei Spielgewinn kein Highscore speichern.");
                                break;
                            case 4:
                                _showcaseView.setShowcase(new ViewTarget(findViewById(R.id.btn_game_pruefen)), true);
                                _showcaseView.setContentTitle("Eingabe prüfen");
                                _showcaseView.setContentText("Hier kannst Du deine Eingabe überprüfen");
                                params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                                params.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
                                params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
                                _showcaseView.setButtonPosition(params);
                                break;
                            case 5:
                                _helpShown = false;
                                _showcaseView.hide();
                                chronometer(true);
                                break;
                        }
                        _counter++;
                    }
                })
                .build();
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

    private void undoRow()
    {
        _undo = true;
        LinearLayout gamefield = (LinearLayout) findViewById(R.id.game_field);
        //gamefield.removeViewAt(_activeRow-1);
        gamefield.removeViewAt(0);
        _activeRow--;
    }

    @Override
    public void onClick(View v)
    {
        ImageView image;
        LinearLayout gamefield;

        if (!_helpShown)
        {
            switch (v.getId())
            {
                case R.id.btn_game_aufloesen:
                    new AlertDialog.Builder(this)
                            .setMessage(R.string.game_dissolve)
                            .setCancelable(true)
                            .setNegativeButton(R.string.btn_yes, new DialogInterface.OnClickListener()
                            {
                                public void onClick(final DialogInterface dialog, final int id)
                                {
                                    gameHasEnded();
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
                    break;
                case R.id.btn_game_pause:
                    if (_saveGame)
                    {
                        new AlertDialog.Builder(this)
                                .setCancelable(false)
                                .setMessage(R.string.title_break)
                                .setNeutralButton(R.string.label_weiter, new DialogInterface.OnClickListener()
                                {
                                    @Override
                                    public void onClick(final DialogInterface dialog, final int which)
                                    {
                                        dialog.dismiss();
                                        chronometer(true);
                                    }
                                }).setPositiveButton("Spiel überspeichern", new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(final DialogInterface dialog, final int which)
                            {
                                SavegameDataSource ds = new SavegameDataSource(GameActivity.this);
                                ds.open();
                                ds.updateSavegameItem(getIntent().getLongExtra("id", -1), getSavegame().toString());
                                ds.close();
                                dialog.dismiss();
                                finish();
                            }
                        }).create().show();
                    } else
                    {
                        final EditText name = new EditText(this);
                        name.setInputType(InputType.TYPE_CLASS_TEXT);
                        name.setText("Name", TextView.BufferType.EDITABLE);
                        new AlertDialog.Builder(this)
                                .setCancelable(false)
                                .setView(name)
                                .setMessage(R.string.title_break)
                                .setNeutralButton(R.string.label_weiter, new DialogInterface.OnClickListener()
                                {
                                    @Override
                                    public void onClick(final DialogInterface dialog, final int which)
                                    {
                                        dialog.dismiss();
                                        chronometer(true);
                                    }
                                }).setPositiveButton("Spiel speichern", new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(final DialogInterface dialog, final int which)
                            {
                                SavegameDataSource ds = new SavegameDataSource(GameActivity.this);
                                ds.open();
                                ds.createSavegameItem(name.getText().toString(), getSavegame().toString());
                                ds.close();
                                dialog.dismiss();
                                finish();
                            }
                        }).create().show();
                    }

                    chronometer(false);
                    break;
                case R.id.btn_game_rueckgaengig:
                    if (_activeRow == 0)
                    {
                        Toast toast = Toast.makeText(getApplicationContext(), "Keine Reihe verfügbar", Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                    }
                    else
                    {
                        if (!_undo)
                        {
                            new AlertDialog.Builder(this)
                                    .setMessage(R.string.game_undofirst)
                                    .setCancelable(true)
                                    .setNegativeButton(R.string.btn_yes, new DialogInterface.OnClickListener()
                                    {
                                        public void onClick(final DialogInterface dialog, final int id)
                                        {
                                            undoRow();
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
                        else
                        {
                            new AlertDialog.Builder(this)
                                    .setMessage(R.string.game_undo)
                                    .setCancelable(true)
                                    .setNegativeButton(R.string.btn_yes, new DialogInterface.OnClickListener()
                                    {
                                        public void onClick(final DialogInterface dialog, final int id)
                                        {
                                            undoRow();
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
                    break;
                case R.id.btn_game_pruefen:
                    gamefield = (LinearLayout) findViewById(R.id.game_field);

                    boolean eval = true;
                    if (!_empty)
                    {
                        //if all fields are filled when duplicates aren't allowed
                        for (int i = 0; i < _anzFields; i++)
                        {
                            if (_farbforschlagRow.Fields[i].getColor() == -1)
                            {
                                eval = false;
                                Toast toast = Toast.makeText(getApplicationContext(), "Leere Felder nicht erlaubt", Toast.LENGTH_SHORT);
                                toast.setGravity(Gravity.CENTER, 0, 0);
                                toast.show();
                                break;
                            }
                        }
                    }
                    if (!_multiple && eval)
                    {
                        boolean colors[];
                        colors = new boolean[_anzColors + 1];

                        //if all fields are filled unique, when multiple aren't allowed
                        for (int i = 0; i < _anzFields; i++)
                        {
                            if (!colors[_farbforschlagRow.Fields[i].getColor() + 1])
                            {
                                colors[_farbforschlagRow.Fields[i].getColor() + 1] = true;
                            } else
                            {
                                eval = false;
                                Toast toast = Toast.makeText(getApplicationContext(), "Doppelte Felder nicht erlaubt", Toast.LENGTH_SHORT);
                                toast.setGravity(Gravity.CENTER, 0, 0);
                                toast.show();
                                break;
                            }
                        }
                    }
                    if (eval)
                    {
                        _rows[_activeRow] = _farbforschlagRow;
                        if (EvaluateFarbvorschlagRow(_activeRow))
                        {
                            long minutes = (((SystemClock.elapsedRealtime() - ((Chronometer) findViewById(R.id.time)).getBase()) / 1000)) / 60;
                            long seconds = (((SystemClock.elapsedRealtime() - ((Chronometer) findViewById(R.id.time)).getBase()) / 1000)) % 60;

                            _winTimestring = String.format("%02d", minutes) + ":" + String.format("%02d", seconds);

                            //gamefield.addView(CreateDisplayableRowWithPins(this, _rows[_activeRow]));
                            gamefield.addView(CreateDisplayableRowWithPins(this, _rows[_activeRow]), 0);
                            if (_activeRow == 0)
                            {
                                ShowPopup("Glückwunsch! Du hast das Spiel nach " + _winTimestring + " Sekunden und einem Zug gewonnen!", true, true);
                            } else
                            {
                                ShowPopup("Glückwunsch! Du hast das Spiel nach " + _winTimestring + " in " + (_activeRow + 1) + " Zügen gewonnen!", true, true);
                            }
                            break;
                        }
                        //gamefield.addView(CreateDisplayableRowWithPins(this, _rows[_activeRow]));
                        gamefield.addView(CreateDisplayableRowWithPins(this, _rows[_activeRow]), 0);
                        _activeRow++;
                        if (_activeRow == _anzRows)
                        {
                            ShowPopup("Du hast es nicht geschafft den Code zu knacken!", true, false);
                            break;
                        }
                        _farbforschlagRow = ResetFarbvorschlagRow();
                        UpdateFarbvorschlagRow();
                    }
                    break;
                case R.id.btn_main_menue:
                    finish();
                    break;
                case R.id.btn_new_game:
                    final Intent again;
                    if (_singlePlayer)
                    {
                        again = new Intent(this, GameActivity.class);
                    } else
                    {
                        again = new Intent(this, Colorcode.class);
                    }
                    again.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    finish();
                    startActivity(again);
                    break;
                //Buttons in farbauswahl
                case -1:
                    image = (ImageView) findViewById(_activeField);
                    image.setImageResource(R.mipmap.ic_slot);
                    _farbforschlagRow.Fields[_activeField - 10].setColor(-1);
                    HideFarbauswahl();
                    break;
                case 0:
                    image = (ImageView) findViewById(_activeField);
                    image.setImageResource(_gameColors[0]);
                    _farbforschlagRow.Fields[_activeField - 10].setColor(0);
                    HideFarbauswahl();
                    break;
                case 1:
                    image = (ImageView) findViewById(_activeField);
                    image.setImageResource(_gameColors[1]);
                    _farbforschlagRow.Fields[_activeField - 10].setColor(1);
                    HideFarbauswahl();
                    break;
                case 2:
                    image = (ImageView) findViewById(_activeField);
                    image.setImageResource(_gameColors[2]);
                    _farbforschlagRow.Fields[_activeField - 10].setColor(2);
                    HideFarbauswahl();
                    break;
                case 3:
                    image = (ImageView) findViewById(_activeField);
                    image.setImageResource(_gameColors[3]);
                    _farbforschlagRow.Fields[_activeField - 10].setColor(3);
                    HideFarbauswahl();
                    break;
                case 4:
                    image = (ImageView) findViewById(_activeField);
                    image.setImageResource(_gameColors[4]);
                    _farbforschlagRow.Fields[_activeField - 10].setColor(4);
                    HideFarbauswahl();
                    break;
                case 5:
                    image = (ImageView) findViewById(_activeField);
                    image.setImageResource(_gameColors[5]);
                    _farbforschlagRow.Fields[_activeField - 10].setColor(5);
                    HideFarbauswahl();
                    break;
                case 6:
                    image = (ImageView) findViewById(_activeField);
                    image.setImageResource(_gameColors[6]);
                    _farbforschlagRow.Fields[_activeField - 10].setColor(6);
                    HideFarbauswahl();
                    break;
                case 7:
                    image = (ImageView) findViewById(_activeField);
                    image.setImageResource(_gameColors[7]);
                    _farbforschlagRow.Fields[_activeField - 10].setColor(7);
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
}
