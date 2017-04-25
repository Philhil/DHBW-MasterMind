package de.dhbw.stuttgart.mastermind;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;

public class StartActivity extends AppCompatActivity implements HighscoreFragment.OnListFragmentInteractionListener, SavegameFragment.OnListFragmentInteractionListener {

    @Override
    public void onListFragmentInteraction(HighscoreItem item) {
    }

    @Override
    public void onListFragmentInteraction(SavegameItem item) {
    }

    private Fragment fragment;
    public static FragmentManager fragmentManager;

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
                case R.id.navigation_impressum:
                    fragment = new ImpressumFragment();
                    break;
            }
            final FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.replace(R.id.content, fragment).addToBackStack("Fragment").commit();

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

        switch(view.getId())
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
