package com.rafaelwitak.gymdatabro;

import android.os.Bundle;
import android.view.View;
import android.widget.SeekBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.LiveData;

import com.rafaelwitak.gymdatabro.database.GymBroDatabase;
import com.rafaelwitak.gymdatabro.database.Set;
import com.rafaelwitak.gymdatabro.database.WorkoutStep;
import com.rafaelwitak.gymdatabro.databinding.ActivityWorkoutStepBinding;

public class WorkoutStepActivity extends AppCompatActivity {

    private GymBroDatabase database;
    private ActivityWorkoutStepBinding binding;
    private WorkoutStep currentWorkoutStep;
    private Set performedSet;
    private Toolbar toolbar;
    private SeekBar painSlider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout_step);
        currentWorkoutStep = getCurrentWorkoutStep();
        database = MainActivity.database;
        performedSet = new Set();

        // bind all Views with IDs automatically
        binding = ActivityWorkoutStepBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        // set up Toolbar
        toolbar = binding.toolbar.getRoot();
        setSupportActionBar(toolbar);

        // set up painSlider SeekBar
        painSlider = binding.stepPainSlider;
        painSlider.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if (painLevelInsideBounds(i)) {
                    performedSet.painLevel = i;
                }
                else {
                    throw new IndexOutOfBoundsException("Pain Level out of bounds");
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        toolbar.setTitle("Current Workout Name");
        toolbar.setSubtitle("Current Exercise");



        // TODO rebuild database and update schema in IDE50
        // TODO include pain level row / seekbar

        setupWorkoutStepViewRows();
    }

    private boolean painLevelInsideBounds(Integer painLevel) {
        return (0 <= painLevel && painLevel <= getResources().getInteger(R.integer.pain_max));
    }

    private void setupWorkoutStepViewRows() {

        if (currentWorkoutStep.reps == null) {
            binding.stepRepsRow.setVisibility(View.GONE);
        }
        else {
            binding.stepRepsPrescribed.setText(currentWorkoutStep.reps);
            binding.stepRepsPerformed.setText(currentWorkoutStep.reps);
        }

        if (currentWorkoutStep.weight == null) {
            binding.stepWeightRow.setVisibility(View.GONE);
        }
        else {
            binding.stepWeightPrescribed.setText(String.valueOf(currentWorkoutStep.weight));
            binding.stepWeightPerformed.setText(String.valueOf(currentWorkoutStep.weight));
        }

        if (currentWorkoutStep.rpe == null) {
            binding.stepRpeRow.setVisibility(View.GONE);
        }
        else {
            binding.stepRpePrescribed.setText(String.valueOf(currentWorkoutStep.rpe));
            binding.stepRpePerformed.setText(String.valueOf(currentWorkoutStep.rpe));
        }

        if (currentWorkoutStep.durationSeconds == null) {
            binding.stepDurationRow.setVisibility(View.GONE);
        }
        else {
            binding.stepDurationPrescribed.setText(currentWorkoutStep.durationSeconds);
            binding.stepDurationPerformed.setText(currentWorkoutStep.durationSeconds);
        }

        if (currentWorkoutStep.restSeconds == null) {
            binding.stepRestRow.setVisibility(View.GONE);
        }
        else {
            binding.stepRestPrescribed.setText(String.valueOf(currentWorkoutStep.restSeconds));
            binding.stepRestPerformed.setText(String.valueOf(currentWorkoutStep.restSeconds));
        }
    }


    private WorkoutStep getCurrentWorkoutStep() {
        int workoutID = getIntent().getIntExtra("workoutID", 0);
        int stepNumber = getIntent().getIntExtra("nextStepNumber", 0);
        LiveData<WorkoutStep> step = database
                .workoutStepDAO()
                .getWorkoutStep(workoutID, stepNumber);
        return step.getValue();
    }
}