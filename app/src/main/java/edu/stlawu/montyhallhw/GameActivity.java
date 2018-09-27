package edu.stlawu.montyhallhw;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class GameActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Load data from a previously saved instance of the activity.
        super.onCreate(savedInstanceState);

        // The layout for the activity.
        setContentView(R.layout.activity_game);
    }
}
