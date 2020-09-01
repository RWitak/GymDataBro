package com.rafaelwitak.gymdatabro;

import android.os.Bundle;
import android.text.Editable;
import android.text.method.KeyListener;
import android.view.KeyEvent;
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

        // automatically bind all Views with IDs
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
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (painLevelInsideBounds(progress)) {
                    performedSet.painLevel = progress;
                }
                else {
                    throw new IndexOutOfBoundsException("Pain Level out of bounds");
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
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

    // Set visibility and/or data for the WorkoutStep's View's Rows
    private void setupWorkoutStepViewRows() {
        // TODO refactor
        if (currentWorkoutStep.reps == null) {
            binding.stepRepsRow.setVisibility(View.GONE);
        }
        else {
            binding.stepRepsPrescribed.setText(currentWorkoutStep.reps);
            binding.stepRepsPerformed.setText(currentWorkoutStep.reps);
            binding.stepRepsPerformed.setKeyListener(new KeyListener() {
                @Override
                public int getInputType() {
                    return 0;
                }

                @Override
                public boolean onKeyDown(View view, Editable editable, int i, KeyEvent keyEvent) {
                    return false;
                }

                @Override
                public boolean onKeyUp(View view, Editable editable, int i, KeyEvent keyEvent) {
                    performedSet.reps = editable.getChars();
                    return false;
                }

                @Override
                public boolean onKeyOther(View view, Editable editable, KeyEvent keyEvent) {
                    return false;
                }

                @Override
                public void clearMetaKeyState(View view, Editable editable, int i) {

                }
            });
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