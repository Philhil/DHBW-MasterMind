package de.dhbw.stuttgart.mastermind;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;

import com.github.amlcurran.showcaseview.ShowcaseView;
import com.github.amlcurran.showcaseview.targets.ActionItemTarget;
import com.github.amlcurran.showcaseview.targets.ActionViewTarget;
import com.github.amlcurran.showcaseview.targets.Target;
import com.github.amlcurran.showcaseview.targets.ViewTarget;

public class StartActivity extends AppCompatActivity implements HighscoreFragment.OnListFragmentInteractionListener, SavegameFragment.OnListFragmentInteractionListener {

    @Override
    public void onListFragmentInteraction(HighscoreItem item) {
    }

    @Override
    public void onListFragmentInteraction(SavegameItem item) {
    }

    private Fragment fragment;
    public static FragmentManager fragmentManager;

    private ShowcaseView _showcaseView;
    private int _counter = 0;
    private boolean _showHelp;
    private boolean _helpShown = false;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            if (!_helpShown)
            {
                switch (item.getItemId())
                {
                    case R.id.navigation_home:
                        fragment = new HomeFragment();
                        break;
                    case R.id.navigation_highscore:
                        fragment = new HighscoreFragment();
                        break;
                    case R.id.navigation_settings:
                        fragment = new SettingsFragment();
                        break;
                    case R.id.navigation_help:
                        fragment = new HelpFragment();
                        break;
                    case R.id.navigation_impressum:
                        fragment = new ImpressumFragment();
                        break;
                }
                final FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.content, fragment).addToBackStack("Fragment").commit();
            }
            return true;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        fragmentManager = getSupportFragmentManager();
        fragment = new HomeFragment();
        final FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.content, fragment).commit();

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        SharedPreferences sharedPref = this.getSharedPreferences(getString(R.string.preference_file_key), MODE_PRIVATE);
        _showHelp = sharedPref.getBoolean(getString(R.string.prefkey_help_home), true);

        if (_showHelp)
        {
            createShowcase();

            SharedPreferences.Editor editor = sharedPref.edit();

            editor.putBoolean(getString(R.string.prefkey_help_home), false);
            editor.commit();
        }
    }

    private void createShowcase()
    {
        _helpShown = true;
        _showcaseView = new ShowcaseView.Builder(this)
                .setTarget(Target.NONE)
                .setContentTitle("Mastermind")
                .setContentText("Willkommen beim Spiel Mastermind.")
                .setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        switch (_counter) {
                            case 0:
                                _showcaseView.setShowcase(new ViewTarget(findViewById(R.id.button_startGame)), true);
                                _showcaseView.setContentTitle("Spiel starten");
                                _showcaseView.setContentText("Hier startest Du das Spiel gegen den Computer.");
                                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                                params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
                                params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
                                _showcaseView.setButtonPosition(params);
                                break;
                            case 1:
                                _showcaseView.setShowcase(new ViewTarget(findViewById(R.id.button_2playerGame)), true);
                                _showcaseView.setContentTitle("Zweispielerspiel starten");
                                _showcaseView.setContentText("Hier startest Du ein Spiel gegen einen zweiten Spieler.");
                                break;
                            case 2:
                                _showcaseView.setShowcase(new ViewTarget(findViewById(R.id.button_loadGame)), true);
                                _showcaseView.setContentTitle("Spiel laden");
                                _showcaseView.setContentText("Hier kannst Du ein gespeichertes Spiel laden und fortsetzen.");
                                break;
                            case 3:
                                _showcaseView.setShowcase(new ViewTarget(findViewById(R.id.navigation_home)), true);
                                _showcaseView.setContentTitle("Home");
                                _showcaseView.setContentText("Hier gelangst Du zum Hauptbildschirm, von welchem Du die Spiele starten kannst.");
                                break;
                            case 4:
                                _showcaseView.setShowcase(new ViewTarget(findViewById(R.id.navigation_highscore)), true);
                                _showcaseView.setContentTitle("Highscore");
                                _showcaseView.setContentText("Hier kannst Du die Highscoreliste anzeigen.");
                                break;
                            case 5:
                                _showcaseView.setShowcase(new ViewTarget(findViewById(R.id.navigation_settings)), true);
                                _showcaseView.setContentTitle("Einstellungen");
                                _showcaseView.setContentText("Hier kannst Du die Spieleinstellungen vornehmen. Diese werden automatisch gespeichert.");
                                params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                                params.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
                                params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
                                _showcaseView.setButtonPosition(params);
                                break;
                            case 6:
                                _showcaseView.setShowcase(new ViewTarget(findViewById(R.id.navigation_help)), true);
                                _showcaseView.setContentTitle("Hilfe");
                                _showcaseView.setContentText("Hier kannst Du eine ausführliche Hilfe und Spielbeschreibung anzeigen.");
                                break;
                            case 7:
                                _showcaseView.setShowcase(new ViewTarget(findViewById(R.id.navigation_impressum)), true);
                                _showcaseView.setContentTitle("Impressum");
                                _showcaseView.setContentText("Hier kannst Du das Impressum anzeigen.");
                                break;
                            case 8:
                                _helpShown = false;
                                _showcaseView.hide();
                                break;
                        }
                        _counter++;
                    }
                })
                .build();
    }

    @Override
    public void onBackPressed()
    {
        new AlertDialog.Builder(this)
                .setMessage(R.string.home_exitwarning)
                .setCancelable(true)
                .setNegativeButton("Ja", new DialogInterface.OnClickListener()
                    {
                        public void onClick(final DialogInterface dialog, final int id)
                        {
                            System.exit(0);
                        }
                    })
                    .setPositiveButton("Nein", new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(final DialogInterface dialog, final int which)
                        {
                            dialog.dismiss();
                        }
                    }).create().show();

    }

    public void startGame(View view) {

        if (!_helpShown)
        {
            switch (view.getId())
            {
                case R.id.button_startGame:
                    Intent intent = new Intent(this, GameActivity.class);
                    startActivity(intent);
                    break;
                case R.id.button_2playerGame:
                    Intent intent_colorcode = new Intent(this, Colorcode.class);
                    startActivity(intent_colorcode);
                    break;
                case R.id.button_loadGame:
                    fragment = new SavegameFragment();
                    final FragmentTransaction transaction = fragmentManager.beginTransaction();
                    transaction.replace(R.id.content, fragment).addToBackStack("SavegameFragment").commit();
                    break;
            }
        }
    }
}
