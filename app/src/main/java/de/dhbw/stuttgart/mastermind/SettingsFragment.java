package de.dhbw.stuttgart.mastermind;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;


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
                    if (_anzFields <= _anzColors+1)
                    {
                        multipleBox.setChecked(true);
                        _editor.putBoolean(getString(R.string.prefkey_multiple), true);
                    }
                }
                else
                {
                    _editor.putBoolean(getString(R.string.prefkey_empty), false);
                    if (_anzFields <= _anzColors)
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
                        _anzColors = 7;
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
        switch(_sharedPref.getInt(getString(R.string.prefkey_background_color), R.id.backgroundWhite_radioButton))
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

        return view;
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
        _editor.commit();
    }
}
