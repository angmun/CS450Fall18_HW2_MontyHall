package edu.stlawu.montyhallhw;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.AudioAttributes;
import android.media.SoundPool;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

/**
 * A simple {@link Fragment} subclass.
 */
public class GameFragment extends Fragment {

    // Required empty public constructor.
    public GameFragment() {

    }

    // Avoid hardcoding strings: initialize string constants for keys.
    public static final String WINS = "Win Value";
    public static final String LOSSES = "Loss Value";
    public static final String TOTAL = "Total Value";
    public static final String WINSsp = "Win Value";
    public static final String LOSSESsp = "Loss Value";
    public static final String TOTALsp = "Total Value";
    public static final String CLICKBTN = "Button Clicked?";
    public static final String CLICKBTNsp = "Button Clicked?";
    public static final String PREFERENCES = "MONTY PREFERENCES";
    public static final String NEW_CLICKED = "NEW CLICKED";
    public static final String CONTINUE_CLICKED = "CONTINUE CLICKED";

    // Declare the various views required to play the game.
    // Views with score values:
    private TextView wins = null;
    private TextView losses = null;
    private TextView total = null;

    // Linear layout containing the prompt:
    private TextView prompt = null;

    // Views with image buttons (the doors):
    private ImageButton door1 = null;
    private ImageButton door2 = null;
    private ImageButton door3 = null;

    // Have an ArrayList of ImageButtons so we can track the doors
    private ArrayList<ImageButton> buttonList = null;

    // Declare a timer for use to delay events.
    Timer timeit = null;

    // Check whether any button has been pressed to know if another chance is to be given.
    boolean clicked = false;

    // Declare variables for including sounds to the game for dramatic effect.
    private AudioAttributes aattr = null;
    private SoundPool soundPool = null;
    private int carSound = 0;
    private int goatSound = 0;
    private int doorCreak = 0;
    private int drumRoll = 0;

    // Change the image displayed to a selected door.
    private void doorSelected(final int doorNo) {
        // Change the door's image.
        buttonList.get(doorNo - 1).setImageLevel(4);

        // Reveal another door to have a goat.
        // Generate a random value accordingly.
        Random randomize = new Random();
        int option = randomize.nextInt(2);

        // Get another door to update.
        buttonList.get((doorNo + option) % 3).setImageLevel(7);

        // Disable the goat image button.
        buttonList.get((doorNo + option) % 3).setEnabled(false);

        this.prompt.setText(R.string.choose_again);
    }


    // Update the appearance of the doors accordingly on the second click.
    private void secondClicked(int doorNo) {
        // Set all doors accordingly.
        for (int i = 0; i < 3; i++) {
            buttonList.get(i).setImageLevel(0);
        }

        // Change the door's image.
        buttonList.get(doorNo - 1).setImageLevel(4);

        // Set prompt text for user to anticipate results.
        prompt.setText(R.string.win_lose);
    }


    // Update the values of the scores accordingly.
    private void updateScore(int option) {

        if (option == 6) {
            // The player scored a car!
            int new_win = Integer.parseInt(this.wins.getText().toString()) + 1;

            this.wins.setText(String.format("%s", new_win));

            int new_total = Integer.parseInt(this.wins.getText().toString()) -
                    Integer.parseInt(this.losses.getText().toString());

            this.total.setText(String.format("%s", new_total));


        }

        else {
            // The player is eating goat!
            int new_loss = Integer.parseInt(this.losses.getText().toString()) + 1;

            this.losses.setText(String.format("%s", new_loss));

            int new_total = Integer.parseInt(this.wins.getText().toString()) -
                    Integer.parseInt(this.losses.getText().toString());

            this.total.setText(String.format("%s", new_total));
        }
    }


    // Have a countdown before the revelation of the prize.
    private void reveal(final ImageButton aButton) {
        // Disable all the buttons.
        for (int i = 0; i < 3; i++) {
            buttonList.get(i).setEnabled(false);
        }

        // Change the image on the specific door accordingly.
        // Initialize a new timer.
        timeit = new Timer();

        // Play the drumroll.
        this.soundPool.play(this.drumRoll, 1, 1, 1, 0, 1);

        this.timeit.schedule(new TimerTask() {
            // Initialize a value to terminate the timer through.
            int count = 0;

            @Override
            public void run() {

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (count >= 3) {
                            // End the timer once 3 seconds has passed.
                            timeit.cancel();

                            // Show the door and the results of the game.
                            showDoor(aButton);
                        }

                        // Update the count.
                        count++;

                        // Update the door's image.
                        aButton.setImageLevel(4 - count);
                    }
                });
            }
        }, 0, 1000);

    }


    // Change the image displayed on the door accordingly.
    private void showDoor(final ImageButton aButton) {
        // Initialize the timer
        timeit = new Timer();

        // Play the door opening sound.
        this.soundPool.play(this.doorCreak, 1, 1, 1, 0, 1);

        // Generate a random value accordingly.
        Random randomize = new Random();
        final int option = randomize.nextInt(2) + 6;

        // Delay the result of the door selection.
        this.timeit.schedule(new TimerTask() {
            // Initialize a value to terminate the timer through.
            int count = 0;

            @Override
            public void run() {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (count >= 2) {
                            // End the timer once 2 seconds has passed.
                            timeit.cancel();

                            // Display the appropriate image accompanied by its sound.
                            if (option == 6) {
                                aButton.setImageLevel(6);
                                soundPool.play(carSound, 1, 1, 1, 0, 1);

                                // Update the score of the game.
                                updateScore(option);

                                // Reset the game.
                                reset();
                            } else {
                                aButton.setImageLevel(7);
                                soundPool.play(goatSound, 1, 1, 1, 0, 1);

                                // Update the score of the game.
                                updateScore(option);

                                // Reset the game.
                                reset();
                            }
                        }

                        // Update the count.
                        count++;
                    }
                });
            }
        }, 0, 1000);
    }


    // Reset the game conditions.
    private void reset() {
        // Delay the reset to allow time to see what one has won.
        timeit = new Timer();

        timeit.schedule(new TimerTask() {
            // Initialize a value to terminate the timer through.
            int count = 0;

            @Override
            public void run() {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (count >= 3) {
                            // End the timer.
                            timeit.cancel();

                            // Reset the clicker.
                            clicked = false;

                            // Reset the prompt.
                            prompt.setText(R.string.choose_a_door);

                            // Reset the doors.
                            for (int i = 0; i < 3; i++) {
                                buttonList.get(i).setEnabled(true);
                                buttonList.get(i).setImageLevel(0);
                            }
                        }

                        // Update the count.
                        count++;
                    }
                });
            }
        }, 0, 1000);
    }



    // OnCreate gets called when the fragment is created, before the UI views are constructed.
    @Override
    public void onCreate(Bundle savedInstanceState) {
        // Load any stored data from a previously saved instance of the fragment.
        super.onCreate(savedInstanceState);

        // Allows the MainActivity to restore the fragment by having it retained across configuration changes
        // e.g. switching to landscape mode from portrait
        setRetainInstance(true);
    }


    // OnCreateView gets called when the fragment draws its user interface initially.
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment.This returns the layout file as a view.
        View rootView = inflater.inflate(R.layout.fragment_game, container, false);

        // Initialize the various views required to play the game.
        // Views with score values:
        this.wins = (TextView) rootView.findViewById(R.id.tv_wins);
        this.losses = (TextView) rootView.findViewById(R.id.tv_losses);
        this.total = (TextView) rootView.findViewById(R.id.tv_total);

        // Text view containing the prompt:
        this.prompt = (TextView) rootView.findViewById(R.id.prompt);

        // Views with image buttons (the doors):
        this.door1 = rootView.findViewById(R.id.door1);
        this.door2 = rootView.findViewById(R.id.door2);
        this.door3 = rootView.findViewById(R.id.door3);

        // Initialize the arraylist of image buttons:
        this.buttonList = new ArrayList<>();
        buttonList.add(door1);
        buttonList.add(door2);
        buttonList.add(door3);

        // Initialize variables for including sounds to the game for dramatic effect.
        this.aattr = new AudioAttributes.Builder().setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .setUsage(AudioAttributes.USAGE_NOTIFICATION).build();
        this.soundPool = new SoundPool.Builder().setMaxStreams(1).setAudioAttributes(aattr).build();
        this.carSound = this.soundPool.load(getContext(), R.raw.oldcar, 1);
        this.goatSound = this.soundPool.load(getContext(), R.raw.goatbah, 1);
        this.doorCreak = this.soundPool.load(getContext(), R.raw.doorcreak, 1);
        this.drumRoll = this.soundPool.load(getContext(), R.raw.drumroll, 1);


        // Restore data saved using onSaveInstanceState.
        if(savedInstanceState != null){
            this.wins.setText(savedInstanceState.getCharSequence(WINS));
            this.losses.setText(savedInstanceState.getCharSequence(LOSSES));
            this.total.setText(savedInstanceState.getCharSequence(TOTAL));
            this.clicked = savedInstanceState.getBoolean(CLICKBTN);
        }

        // Check for saved shared preferences as well.
        SharedPreferences preferences = getActivity().getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor pref_edit = getActivity().getSharedPreferences(PREFERENCES,
                Context.MODE_PRIVATE).edit();

        // Continue button clicked.
        if(preferences.getBoolean(CONTINUE_CLICKED, false)){
            this.wins.setText(preferences.getString(WINSsp, "0"));
            this.losses.setText(preferences.getString(LOSSESsp, "0"));
            this.total.setText(preferences.getString(TOTALsp, "0"));
            this.clicked = preferences.getBoolean(CLICKBTNsp, false);

            // Reset the continue_clicked value.
            pref_edit.putBoolean(CONTINUE_CLICKED, false).apply();
        }

        // New button clicked.
        if(preferences.getBoolean(NEW_CLICKED, false)){
            this.wins.setText("0");
            this.losses.setText("0");
            this.total.setText("0");
            this.clicked = preferences.getBoolean(CLICKBTNsp, false);

            // Reset the new_clicked value.
            pref_edit.putBoolean(NEW_CLICKED, false).apply();
        }

        // Set up onClickListeners for the various buttons:
        this.door1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!clicked) {
                    // Select the door but permit the user to switch doors.
                    doorSelected(1);

                    // Animate the image button.
                    // TODO

                    // Update clicked.
                    clicked = true;
                } else {
                    // Update the appearance of the door as it has been selected.
                    secondClicked(1);

                    // Countdown then reveal the results.
                    reveal(door1);
                }
            }
        });

        this.door2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!clicked) {
                    // Select the door but permit the user to switch doors.
                    doorSelected(2);

                    // Update clicked.
                    clicked = true;
                } else {
                    // Update the appearance of the door as it has been selected.
                    secondClicked(2);

                    // Countdown then reveal the results.
                    reveal(door2);
                }

            }
        });

        this.door3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!clicked) {
                    // Select the door but permit the user to switch doors.
                    doorSelected(3);

                    // Update clicked.
                    clicked = true;
                } else {
                    // Update the appearance of the door as it has been selected.
                    secondClicked(3);

                    // Countdown then reveal the results.
                    reveal(door3);
                }
            }
        });

        return rootView;

    }


    // Save data for use between activities to implement the continue button.
    // Save data as user leaves activity through back button.
    @Override
    public void onPause() {
        super.onPause();
        // Utilize shared preferences to maintain data for the entire application.
        SharedPreferences.Editor pref_edit = getActivity().getSharedPreferences(PREFERENCES,
                Context.MODE_PRIVATE).edit();

        pref_edit.putString(WINSsp, wins.getText().toString()).apply();
        pref_edit.putString(LOSSESsp, losses.getText().toString()).apply();
        pref_edit.putString(TOTALsp, total.getText().toString()).apply();
        pref_edit.putBoolean(CLICKBTNsp, clicked).apply();
    }


    // Save data in case of a configuration change.
    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        // Preserve the scores from the respective TextViews.
        outState.putCharSequence(WINS, wins.getText());
        outState.putCharSequence(LOSSES, losses.getText());
        outState.putCharSequence(TOTAL, total.getText());

        // Preserve the status of the image button clicks.
        outState.putBoolean(CLICKBTN, clicked);
    }
}

