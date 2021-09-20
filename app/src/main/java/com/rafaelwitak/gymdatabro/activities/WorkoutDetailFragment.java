/*
 * Copyright (c) 2021, Rafael Witak.
 */

package com.rafaelwitak.gymdatabro.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.rafaelwitak.gymdatabro.R;
import com.rafaelwitak.gymdatabro.database.Exercise;
import com.rafaelwitak.gymdatabro.database.GymBroDatabase;
import com.rafaelwitak.gymdatabro.database.MasterDao;
import com.rafaelwitak.gymdatabro.database.WorkoutStep;
import com.rafaelwitak.gymdatabro.util.UniqueWorkout;
import java8.util.stream.Collectors;
import java8.util.stream.StreamSupport;

import java.util.List;

/**
 * A fragment representing a single Workout detail screen.
 * This fragment is either contained in a {@link WorkoutListActivity}
 * in two-pane mode (on tablets) or a {@link WorkoutDetailActivity}
 * on handsets.
 */
public class WorkoutDetailFragment extends Fragment {
    public static final String ARG_ITEM_ID = "item_id";
    private UniqueWorkout uniqueWorkout;
    private final MasterDao masterDao =
            GymBroDatabase.getDatabase(getContext()).masterDao();

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public WorkoutDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final Bundle arguments = getArguments();
        if (arguments != null) {
            if (arguments.containsKey(ARG_ITEM_ID)) {
                // Load the content specified by the fragment arguments.
                final int instanceId = getArguments().getInt(ARG_ITEM_ID);
                uniqueWorkout = masterDao.getUniqueWorkoutFromInstanceId(instanceId);
                assert uniqueWorkout != null;

                Activity activity = this.getActivity();
                if (activity != null) {
                    CollapsingToolbarLayout appBarLayout =
                            activity.findViewById(R.id.toolbar_layout);
                    if (appBarLayout != null) {
                        appBarLayout.setTitle(uniqueWorkout.computeName());
                    }
                }
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.workout_detail, container, false);

        // Show the content as text in a TextView.
        if (uniqueWorkout != null) {
            final List<WorkoutStep> workoutSteps =
                    masterDao.getAllStepsForWorkoutId(uniqueWorkout.getWorkoutId());

            if (workoutSteps != null) {
                final String exerciseNames = StreamSupport.stream(workoutSteps)
                        .map(WorkoutStep::getExerciseID)
                        .map(masterDao::getExerciseByID)
                        .map(Exercise::getName)
                        .collect(Collectors.joining(",\n"))
                        + ".";
                ((TextView) rootView.findViewById(R.id.workout_detail))
                        .setText(exerciseNames);
            }
        }

        return rootView;
    }
}