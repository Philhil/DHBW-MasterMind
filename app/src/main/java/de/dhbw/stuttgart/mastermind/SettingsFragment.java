package de.dhbw.stuttgart.mastermind;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SettingsFragment} factory method to
 * create an instance of this fragment.
 */
public class SettingsFragment extends Fragment {

    private SharedPreferences _sharedPref;
    private SharedPreferences.Editor _editor;

    private int _anzColors;
    private int _anzFields;
    private int[] _gameColors = new int[8];
    private String[] _allcolors = {"Rot","Grün","Lila","Blau","Grau","Rosa","Türkis","Gelb","Braun"};
    private int[] _pictures = {R.mipmap.ic_red, R.mipmap.ic_green, R.mipmap.ic_purple, R.mipmap.ic_blue, R.mipmap.ic_grey, R.mipmap.ic_pink, R.mipmap.ic_turquoise, R.mipmap.ic_yellow, R.mipmap.ic_brown};

    public SettingsFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        _sharedPref = getActivity().getSharedPreferences(getString(R.string.preference_file_key), getActivity().MODE_PRIVATE);
        _editor = _sharedPref.edit();

        if (_sharedPref.getBoolean("initialized", true))
        {
            initPrefSettings();
            _editor.putBoolean("initialized", false).commit();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_settings, container, false);

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(view.getContext(), android.R.layout.simple_spinner_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        for (String color : _allcolors)
        {
            spinnerAdapter.add(color);
        }

        _gameColors = loadArrayFromPreferences();

        final Spinner color1 = (Spinner) view.findViewById(R.id.farbe1);
        color1.setAdapter(spinnerAdapter);
        color1.setSelection(getPositionFromPicture(_gameColors[0]));
        color1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                int tmp = getPictureFromPosition(position);

                for (int i = 0; i < _anzColors; i++)
                {
                    if (_gameColors[i] == tmp && i != 0)
                    {
                        Toast toast = Toast.makeText(view.getContext(), "Farbe bereits ausgewählt!", Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                        color1.setSelection(getPositionFromPicture(_gameColors[0]));
                        return;
                    }
                }
                _gameColors[0] = tmp;
                saveArrayAtIndToPreferences(_gameColors, 0);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        });

        final Spinner color2 = (Spinner) view.findViewById(R.id.farbe2);
        color2.setAdapter(spinnerAdapter);
        color2.setSelection(getPositionFromPicture(_gameColors[1]));
        color2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                int tmp = getPictureFromPosition(position);

                for (int i = 0; i < _anzColors; i++)
                {
                    if (_gameColors[i] == tmp && i != 1)
                    {
                        Toast toast = Toast.makeText(view.getContext(), "Farbe bereits ausgewählt!", Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                        color2.setSelection(getPositionFromPicture(_gameColors[1]));
                        return;
                    }
                }
                _gameColors[1] = tmp;
                saveArrayAtIndToPreferences(_gameColors, 1);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        });

        final Spinner color3 = (Spinner) view.findViewById(R.id.farbe3);
        color3.setAdapter(spinnerAdapter);
        color3.setSelection(getPositionFromPicture(_gameColors[2]));
        color3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                int tmp = getPictureFromPosition(position);

                for (int i = 0; i < _anzColors; i++)
                {
                    if (_gameColors[i] == tmp && i != 2)
                    {
                        Toast toast = Toast.makeText(view.getContext(), "Farbe bereits ausgewählt!", Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                        color3.setSelection(getPositionFromPicture(_gameColors[2]));
                        return;
                    }
                }
                _gameColors[2] = tmp;
                saveArrayAtIndToPreferences(_gameColors, 2);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        });

        final Spinner color4 = (Spinner) view.findViewById(R.id.farbe4);
        color4.setAdapter(spinnerAdapter);
        color4.setSelection(getPositionFromPicture(_gameColors[3]));
        color4.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                int tmp = getPictureFromPosition(position);

                for (int i = 0; i < _anzColors; i++)
                {
                    if (_gameColors[i] == tmp && i != 3)
                    {
                        Toast toast = Toast.makeText(view.getContext(), "Farbe bereits ausgewählt!", Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                        color4.setSelection(getPositionFromPicture(_gameColors[3]));
                        return;
                    }
                }
                _gameColors[3] = tmp;
                saveArrayAtIndToPreferences(_gameColors, 3);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        });

        final Spinner color5 = (Spinner) view.findViewById(R.id.farbe5);
        color5.setAdapter(spinnerAdapter);
        color5.setSelection(getPositionFromPicture(_gameColors[4]));
        color5.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                int tmp = getPictureFromPosition(position);

                for (int i = 0; i < _anzColors; i++)
                {
                    if (_gameColors[i] == tmp && i != 4)
                    {
                        Toast toast = Toast.makeText(view.getContext(), "Farbe bereits ausgewählt!", Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                        color5.setSelection(getPositionFromPicture(_gameColors[4]));
                        return;
                    }
                }
                _gameColors[4] = tmp;
                saveArrayAtIndToPreferences(_gameColors, 4);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        });

        final Spinner color6 = (Spinner) view.findViewById(R.id.farbe6);
        color6.setAdapter(spinnerAdapter);
        color6.setSelection(getPositionFromPicture(_gameColors[5]));
        color6.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                int tmp = getPictureFromPosition(position);

                for (int i = 0; i < _anzColors; i++)
                {
                    if (_gameColors[i] == tmp && i != 5 && _anzColors > 5)
                    {
                        Toast toast = Toast.makeText(view.getContext(), "Farbe bereits ausgewählt!", Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                        color6.setSelection(getPositionFromPicture(_gameColors[5]));
                        return;
                    }
                }
                _gameColors[5] = tmp;
                saveArrayAtIndToPreferences(_gameColors, 5);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        });

        final Spinner color7 = (Spinner) view.findViewById(R.id.farbe7);
        color7.setAdapter(spinnerAdapter);
        color7.setSelection(getPositionFromPicture(_gameColors[6]));
        color7.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                int tmp = getPictureFromPosition(position);

                for (int i = 0; i < _anzColors; i++)
                {
                    if (_gameColors[i] == tmp && i != 6 && _anzColors > 5)
                    {
                        Toast toast = Toast.makeText(view.getContext(), "Farbe bereits ausgewählt!", Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                        color7.setSelection(getPositionFromPicture(_gameColors[6]));
                        return;
                    }
                }
                _gameColors[6] = tmp;
                saveArrayAtIndToPreferences(_gameColors, 6);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        });

        final Spinner color8 = (Spinner) view.findViewById(R.id.farbe8);
        color8.setAdapter(spinnerAdapter);
        color8.setSelection(getPositionFromPicture(_gameColors[7]));
        color8.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                int tmp = getPictureFromPosition(position);

                for (int i = 0; i < _anzColors; i++)
                {
                    if (_gameColors[i] == tmp && i != 7 && _anzColors > 5)
                    {
                        Toast toast = Toast.makeText(view.getContext(), "Farbe bereits ausgewählt!", Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                        color8.setSelection(getPositionFromPicture(_gameColors[7]));
                        return;
                    }
                }
                _gameColors[7] = tmp;
                saveArrayAtIndToPreferences(_gameColors, 7);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        });

        //Multiple checkbox
        final CheckBox multipleBox = (CheckBox) view.findViewById(R.id.multiple_color_checkBox);
        final CheckBox emptyBox = (CheckBox) view.findViewById(R.id.empty_space_checkBox);

        multipleBox.setChecked(_sharedPref.getBoolean(getString(R.string.prefkey_multiple), false));
        multipleBox.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (((CheckBox) v).isChecked())
                {
                    _editor.putBoolean(getString(R.string.prefkey_multiple), true);
                }
                else
                {
                    if (emptyBox.isChecked())
                    {
                        if (_anzFields <= _anzColors+1)
                        {
                            _editor.putBoolean(getString(R.string.prefkey_multiple), false);
                        }
                        else
                        {
                            ((CheckBox) v).setChecked(true);
                        }
                    }
                    else
                    {
                        if (_anzFields <= _anzColors)
                        {
                            _editor.putBoolean(getString(R.string.prefkey_multiple), false);
                        }
                        else
                        {
                            ((CheckBox) v).setChecked(true);
                        }
                    }
                }
                _editor.commit();
            }
        });
        //Empty checkbox
        emptyBox.setChecked(_sharedPref.getBoolean(getString(R.string.prefkey_empty), false));
        emptyBox.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (((CheckBox) v).isChecked())
                {
                    _editor.putBoolean(getString(R.string.prefkey_empty), true);
                    if (_anzFields > _anzColors+1)
                    {
                        multipleBox.setChecked(true);
                        _editor.putBoolean(getString(R.string.prefkey_multiple), true);
                    }
                }
                else
                {
                    _editor.putBoolean(getString(R.string.prefkey_empty), false);
                    if (_anzFields > _anzColors)
                    {
                        multipleBox.setChecked(true);
                        _editor.putBoolean(getString(R.string.prefkey_multiple), true);
                    }
                }

                _editor.commit();
            }
        });
        //Anz Colors
        final RadioGroup anzColors = (RadioGroup) view.findViewById(R.id.group_anz_colors);
        switch(_sharedPref.getInt(getString(R.string.prefkey_number_of_colors), 5))
        {
            case 5:
                _anzColors = 5;
                anzColors.check(R.id.number_of_colors_5);
                break;
            case 6:
                _anzColors = 6;
                anzColors.check(R.id.number_of_colors_6);
                break;
            case 8:
                _anzColors = 8;
                anzColors.check(R.id.number_of_colors_8);
                break;
        }
        showSpinners(view, _anzColors);
        anzColors.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch(checkedId){
                    case R.id.number_of_colors_5:
                        _editor.putInt(getString(R.string.prefkey_number_of_colors), 5);
                        _anzColors = 5;
                        break;
                    case R.id.number_of_colors_6:
                        _editor.putInt(getString(R.string.prefkey_number_of_colors), 6);
                        _anzColors = 6;
                        break;
                    case R.id.number_of_colors_8:
                        _editor.putInt(getString(R.string.prefkey_number_of_colors), 8);
                        _anzColors = 8;
                        break;
                }
                showSpinners(view, _anzColors);
                if (emptyBox.isChecked())
                {
                    if (_anzFields > _anzColors+1)
                    {
                        multipleBox.setChecked(true);
                        _editor.putBoolean(getString(R.string.prefkey_multiple), true);
                    }
                }
                else
                {
                    if (_anzFields > _anzColors)
                    {
                        multipleBox.setChecked(true);
                        _editor.putBoolean(getString(R.string.prefkey_multiple), true);
                    }
                }
                _editor.commit();
            }
        });
        //Anz holes
        RadioGroup anzHoles = (RadioGroup) view.findViewById(R.id.group_anz_holes);
        switch(_sharedPref.getInt(getString(R.string.prefkey_number_of_holes), 4))
        {
            case 3:
                _anzFields = 3;
                anzHoles.check(R.id.number_of_holes_3);
                break;
            case 4:
                _anzFields = 4;
                anzHoles.check(R.id.number_of_holes_4);
                break;
            case 5:
                _anzFields = 5;
                anzHoles.check(R.id.number_of_holes_5);
                break;
            case 6:
                _anzFields = 6;
                anzHoles.check(R.id.number_of_holes_6);
                break;
            case 8:
                _anzFields = 8;
                anzHoles.check(R.id.number_of_holes_8);
                break;
        }
        anzHoles.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch(checkedId){
                    case R.id.number_of_holes_3:
                        _editor.putInt(getString(R.string.prefkey_number_of_holes), 3);
                        _anzFields = 3;
                        break;
                    case R.id.number_of_holes_4:
                        _editor.putInt(getString(R.string.prefkey_number_of_holes), 4);
                        _anzFields = 4;
                        break;
                    case R.id.number_of_holes_5:
                        _editor.putInt(getString(R.string.prefkey_number_of_holes), 5);
                        _anzFields = 5;
                        break;
                    case R.id.number_of_holes_6:
                        _editor.putInt(getString(R.string.prefkey_number_of_holes), 6);
                        _anzFields = 6;
                        break;
                    case R.id.number_of_holes_8:
                        _editor.putInt(getString(R.string.prefkey_number_of_holes), 8);
                        _anzFields = 8;
                        break;
                }
                if (emptyBox.isChecked())
                {
                    if (_anzFields > _anzColors+1)
                    {
                        multipleBox.setChecked(true);
                        _editor.putBoolean(getString(R.string.prefkey_multiple), true);
                    }
                }
                else
                {
                    if (_anzFields > _anzColors)
                    {
                        multipleBox.setChecked(true);
                        _editor.putBoolean(getString(R.string.prefkey_multiple), true);
                    }
                }
                _editor.commit();
            }
        });

        //Seekbar for rounds
        final TextView anzRoundsLabel = (TextView) view.findViewById(R.id.round);
        SeekBar roundsBar = (SeekBar) view.findViewById(R.id.round_seekBar);
        roundsBar.setProgress(_sharedPref.getInt(getString(R.string.prefkey_number_of_rounds), 10));
        anzRoundsLabel.setText(getString(R.string.settings_round) + ": " + String.valueOf(roundsBar.getProgress()));
        roundsBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener()
        {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
            {
                if (progress >= 1){
                    seekBar.setProgress(progress);
                    anzRoundsLabel.setText(getString(R.string.settings_round) + ": " + String.valueOf(progress));
                    _editor.putInt(getString(R.string.prefkey_number_of_rounds), progress);
                }
                else {
                    seekBar.setProgress(1);
                    anzRoundsLabel.setText(getString(R.string.settings_round) + ": " + String.valueOf(1));
                    _editor.putInt(getString(R.string.prefkey_number_of_rounds), 1);
                }
                _editor.commit();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar)
            {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar)
            {

            }
        });
        //background color
        RadioGroup backColor = (RadioGroup) view.findViewById(R.id.group_background_color);
        switch(_sharedPref.getInt(getString(R.string.prefkey_background_color), R.string.settings_backgroundWhite))
        {
            case R.string.settings_backgroundWhite:
                backColor.check(R.id.backgroundWhite_radioButton);
                break;
            case R.string.settings_backgroundGreen:
                backColor.check(R.id.backgroundGreen_radioButton);
                break;
            case R.string.settings_backgroundGrey:
                backColor.check(R.id.backgroundGrey_radioButton);
                break;
        }
        backColor.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch(checkedId){
                    case R.id.backgroundWhite_radioButton:
                        _editor.putInt(getString(R.string.prefkey_background_color), R.string.settings_backgroundWhite);
                        break;
                    case R.id.backgroundGrey_radioButton:
                        _editor.putInt(getString(R.string.prefkey_background_color), R.string.settings_backgroundGrey);
                        break;
                    case R.id.backgroundGreen_radioButton:
                        _editor.putInt(getString(R.string.prefkey_background_color), R.string.settings_backgroundGreen);
                        break;
                }
                _editor.commit();
            }
        });

        Button deleteHighscore = (Button) view.findViewById(R.id.highscore_delete);
        deleteHighscore.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                new AlertDialog.Builder(view.getContext())
                        .setMessage(R.string.settings_highscore_delete)
                        .setCancelable(true)
                        .setNegativeButton(R.string.btn_yes, new DialogInterface.OnClickListener()
                        {
                            public void onClick(final DialogInterface dialog, final int id)
                            {
                                HighscoreDataSource ds = new HighscoreDataSource(SettingsFragment.this.getContext());
                                ds.open();
                                ds.deleteAllItems();
                                ds.close();
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
        });
        Button deleteSavegames = (Button) view.findViewById(R.id.savedgames_delete);
        deleteSavegames.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                new AlertDialog.Builder(view.getContext())
                        .setMessage(R.string.settings_savedgames_delete)
                        .setCancelable(true)
                        .setNegativeButton(R.string.btn_yes, new DialogInterface.OnClickListener()
                        {
                            public void onClick(final DialogInterface dialog, final int id)
                            {
                                SavegameDataSource ds = new SavegameDataSource(SettingsFragment.this.getContext());
                                ds.open();
                                ds.deleteAllItems();
                                ds.close();
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
        });
        Button showHelp = (Button) view.findViewById(R.id.show_showcase);
        showHelp.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                _editor.putBoolean(getString(R.string.prefkey_help_home), true);
                _editor.putBoolean(getString(R.string.prefkey_help_game), true);
                _editor.putBoolean(getString(R.string.prefkey_help_colorcode), true);
                _editor.commit();
            }
        });

        return view;
    }

    private void showSpinners(View v, int anz)
    {
        switch(anz)
        {
            case 5:
                v.findViewById(R.id.farbe1).setVisibility(View.VISIBLE);
                v.findViewById(R.id.farbe2).setVisibility(View.VISIBLE);
                v.findViewById(R.id.farbe3).setVisibility(View.VISIBLE);
                v.findViewById(R.id.farbe4).setVisibility(View.VISIBLE);
                v.findViewById(R.id.farbe5).setVisibility(View.VISIBLE);
                v.findViewById(R.id.farbe6).setVisibility(View.INVISIBLE);
                v.findViewById(R.id.farbe7).setVisibility(View.INVISIBLE);
                v.findViewById(R.id.farbe8).setVisibility(View.INVISIBLE);
                break;
            case 6:
                v.findViewById(R.id.farbe1).setVisibility(View.VISIBLE);
                v.findViewById(R.id.farbe2).setVisibility(View.VISIBLE);
                v.findViewById(R.id.farbe3).setVisibility(View.VISIBLE);
                v.findViewById(R.id.farbe4).setVisibility(View.VISIBLE);
                v.findViewById(R.id.farbe5).setVisibility(View.VISIBLE);
                Spinner c6 = (Spinner) v.findViewById(R.id.farbe6);
                c6.setVisibility(View.VISIBLE);
                for (int x = 0; x < anz; x++)
                {
                    if (_gameColors[x] == _pictures[c6.getSelectedItemPosition()] && x != 5)
                    {
                        c6.setSelection(getNextFreeColor());
                    }
                }

                v.findViewById(R.id.farbe7).setVisibility(View.INVISIBLE);
                v.findViewById(R.id.farbe8).setVisibility(View.INVISIBLE);
                break;
            case 8:
                v.findViewById(R.id.farbe1).setVisibility(View.VISIBLE);
                v.findViewById(R.id.farbe2).setVisibility(View.VISIBLE);
                v.findViewById(R.id.farbe3).setVisibility(View.VISIBLE);
                v.findViewById(R.id.farbe4).setVisibility(View.VISIBLE);
                v.findViewById(R.id.farbe5).setVisibility(View.VISIBLE);
                c6 = (Spinner) v.findViewById(R.id.farbe6);
                c6.setVisibility(View.VISIBLE);
                for (int x = 0; x < anz-1; x++)
                {
                    if (_gameColors[x] == _pictures[c6.getSelectedItemPosition()] && x != 5)
                    {
                        c6.setSelection(getNextFreeColor());
                    }
                }
                Spinner c7 = (Spinner) v.findViewById(R.id.farbe7);
                c7.setVisibility(View.VISIBLE);
                for (int x = 0; x < anz-1; x++)
                {
                    if (_gameColors[x] == _pictures[c7.getSelectedItemPosition()] && x != 6)
                    {
                        c7.setSelection(getNextFreeColor());
                    }
                }
                Spinner c8 = (Spinner) v.findViewById(R.id.farbe8);
                c8.setVisibility(View.VISIBLE);
                for (int x = 0; x < anz-1; x++)
                {
                    if (_gameColors[x] == _pictures[c8.getSelectedItemPosition()] && x != 7)
                    {
                        c8.setSelection(getNextFreeColor());
                    }
                }
                break;
        }
    }

    private int getNextFreeColor()
    {
        boolean usedColors[] = new boolean[9];
        int tmp;

        for (int pic : _gameColors)
        {
            if ((tmp = getPositionFromPicture(pic)) != -1)
            {
                usedColors[tmp] = true;
            }
        }

        for (int x = 0; x < usedColors.length; x++)
        {
            if (!usedColors[x])
            {
                return x;
            }
        }

        return -1;
    }

    private int getPositionFromPicture(int pic)
    {
        for (int i = 0; i < _pictures.length; i++)
        {
            if (pic == _pictures[i])
            {
                return i;
            }
        }

        return -1;
    }

    private int getPictureFromPosition(int pos)
    {
        return _pictures[pos];
    }

    private void saveArrayToPreferences(int[] arr)
    {
        for(int i=0;i<arr.length;i++)
        {
            _editor.remove("color_" + i);
            _editor.putInt("color_" + i, arr[i]);
        }
    }

    private void saveArrayAtIndToPreferences(int[] arr, int ind)
    {
        _editor.remove("color_" + ind);
        _editor.putInt("color_" + ind, arr[ind]);
        _editor.commit();
    }

    private int[] loadArrayFromPreferences()
    {
        int arr[] = new int[8];
        for(int i=0;i<8;i++)
        {
            arr[i] = _sharedPref.getInt("color_" + i, _pictures[i]);
        }

        return arr;
    }

    private void initPrefSettings()
    {
        _editor.putInt(getString(R.string.prefkey_number_of_colors), 5);
        _anzColors = 5;
        _editor.putBoolean(getString(R.string.prefkey_multiple), false);
        _editor.putBoolean(getString(R.string.prefkey_empty), false);
        _editor.putInt(getString(R.string.prefkey_number_of_holes), 4);
        _anzFields = 4;
        _editor.putInt(getString(R.string.prefkey_number_of_rounds), 10);
        _editor.putInt(getString(R.string.prefkey_background_color), R.string.settings_backgroundWhite);

        for (int i = 0; i < _gameColors.length; i++)
        {
            _gameColors[i] = _pictures[i];
        }
        saveArrayToPreferences(_gameColors);

        _editor.commit();
    }
}
