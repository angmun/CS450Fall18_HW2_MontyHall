package edu.stlawu.montyhallhw;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioAttributes;
import android.media.SoundPool;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MainFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class MainFragment extends Fragment {
    // Avoiding hard-coded strings by initializing them as constants.
    public static final String PREFERENCES = "MONTY PREFERENCES";
    public static final String NEW_CLICKED = "NEW CLICKED";
    public static final String CONTINUE_CLICKED = "CONTINUE CLICKED";

    // Required to manage interactions between fragment and activity that contains it.
    private OnFragmentInteractionListener mListener;

    //

    // Required empty public constructor.
    public MainFragment() {
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
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            final Bundle savedInstanceState) {

        // Inflate the layout for this fragment.This returns the layout file as a view.
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        // Initialize variables for including a sound to the alert dialog button when clicked.
        AudioAttributes aattr = new AudioAttributes.Builder().setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .setUsage(AudioAttributes.USAGE_NOTIFICATION).build();
        final SoundPool soundPool = new SoundPool.Builder().setMaxStreams(1).setAudioAttributes(aattr).build();
        final int whoopieSound = soundPool.load(getContext(), R.raw.whoopie, 1);

        // Initialize the about button and set an onClick listener to define the behavior when clicked.
        View aboutButton = rootView.findViewById(R.id.about_button);
        aboutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Set up an alert dialog to display information about the game.
                // Use a custom theme for the alert dialog.
                AlertDialog.Builder builder =
                        new AlertDialog.Builder(getActivity());

                // Set the layout for the alert dialog.
                builder.setView(R.layout.alert_dialog);

                // Set up button on alert dialog.
                builder.setPositiveButton(R.string.ok,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // Play the whoopie sound.
                                soundPool.play(whoopieSound, 1, 1, 1, 0, 1);
                            }
                        });

                // Display the alert dialog.
                builder.show();
            }
        });


        // Initialize the new button and set an onClick listener to define the behavior when clicked.
        final View newButton = rootView.findViewById(R.id.new_button);

        // Initialize an animation to apply to the new button.
        Animation animate = AnimationUtils.loadAnimation(getContext(), R.anim.shake);

        // Animate the button before a user selects an option.
        newButton.startAnimation(animate);

        newButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // First end the animation.
                newButton.clearAnimation();

                // When the new button is clicked, we move to the game activity.
                // Set and save a boolean value representing the state of the new button.
                // Utilize shared preferences to maintain data for the entire application.
                SharedPreferences.Editor pref_edit = getActivity().getSharedPreferences(PREFERENCES,
                        Context.MODE_PRIVATE).edit();
                pref_edit.putBoolean(NEW_CLICKED, true).apply();

                // Move to the game activity.
                Intent intent = new Intent(getActivity(), GameActivity.class);
                getActivity().startActivity(intent);
            }
        });


        // Initialize the continue button and set an onClick listener to define the behavior when clicked.
        View continueButton = rootView.findViewById(R.id.continue_button);
        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // When the continue button is clicked, we move to the game activity.
                // Set and save a boolean value representing the state of the continue button.
                // Utilize shared preferences to maintain data for the entire application.
                SharedPreferences.Editor pref_edit = getActivity().getSharedPreferences(PREFERENCES,
                        Context.MODE_PRIVATE).edit();
                pref_edit.putBoolean(CONTINUE_CLICKED, true).apply();

                // Move to the game activity.
                Intent intent = new Intent(getActivity(), GameActivity.class);
                getActivity().startActivity(intent);
            }
        });

        return rootView;
    }


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
