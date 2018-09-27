package edu.stlawu.montyhallhw;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v4.app.Fragment;
import edu.stlawu.montyhallhw.MainFragment.OnFragmentInteractionListener;

// OnFragmentListener for the MainFragment subclass of Fragment, contained in this activity.

// Main activity that is visible at the beginning of the game.
// Allows the user to start a new game, find out more about the game,
// or resume a previously started game.
public class MainActivity
        extends AppCompatActivity
        implements MainFragment.OnFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Load data from a previously saved instance of the activity.
        super.onCreate(savedInstanceState);

        // The layout for the activity.
        setContentView(R.layout.activity_main);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {
    }

}
