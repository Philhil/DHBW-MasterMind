package de.dhbw.stuttgart.mastermind;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

public class StartActivity extends AppCompatActivity implements HighscoreFragment.OnListFragmentInteractionListener {

    @Override
    public void onListFragmentInteraction(HighscoreItem item) {
    }

    private Fragment fragment;
    private FragmentManager fragmentManager;
    public static final String MESSAGE_GAMEMODE = "de.dhbw.stuttgart.mastermind.GAMEMODE";


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
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
            }
            final FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.replace(R.id.content, fragment).commit();

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
    }

    public void startGame(View view) {

        switch(view.getId())
        {
            case R.id.button_startGame:
                Intent intent = new Intent(this, GameActivity.class);
                startActivity(intent);
                break;
            case R.id.button_2playerGame:
                //intent.putExtra(MESSAGE_GAMEMODE, "2Player"); //TODO enum?
                //TODO: Open view to select game mode
                break;
            case R.id.button_loadGame:
                //intent.putExtra(MESSAGE_GAMEMODE, "load"); //TODO enum?
                //TODO: Open view to load a game
                break;
        }
    }
}
