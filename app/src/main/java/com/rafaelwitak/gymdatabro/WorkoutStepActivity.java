package com.rafaelwitak.gymdatabro;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.SeekBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.LiveData;

import com.rafaelwitak.gymdatabro.database.PerformanceSet;
import com.rafaelwitak.gymdatabro.databinding.ActivityWorkoutStepBinding;

import com.rafaelwitak.gymdatabro.database.GymBroDatabase;
import com.rafaelwitak.gymdatabro.database.Workout;
import com.rafaelwitak.gymdatabro.database.WorkoutStep;
import com.rafaelwitak.gymdatabro.databinding.ActivityWorkoutStepBinding;
import com.rafaelwitak.gymdatabro.workoutStepRows.DurationRow;
import com.rafaelwitak.gymdatabro.workoutStepRows.RPERow;
import com.rafaelwitak.gymdatabro.workoutStepRows.RepsRow;
import com.rafaelwitak.gymdatabro.workoutStepRows.RestRow;
import com.rafaelwitak.gymdatabro.workoutStepRows.WeightRow;
import com.rafaelwitak.gymdatabro.workoutStepRows.WorkoutStepRow;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class WorkoutStepActivity extends AppCompatActivity {

    private GymBroDatabase database;
    private Workout currentWorkout;
    private WorkoutStep currentWorkoutStep;
    private PerformanceSet performedSet;

    private ArrayList <WorkoutStepRow> rows;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        database = MainActivity.database;
        currentWorkout = getCurrentWorkout();
        currentWorkoutStep = getCurrentWorkoutStep();
        performedSet = new PerformanceSet();

        // automatically bind all Views with IDs
        setContentView(R.layout.activity_workout_step);
        com.rafaelwitak.gymdatabro.databinding.ActivityWorkoutStepBinding binding =
                ActivityWorkoutStepBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        populateRows(binding);

        // set up Toolbar
        Toolbar toolbar = binding.toolbar.getRoot();
        setSupportActionBar(toolbar);
        toolbar.setTitle(currentWorkout.name);
        toolbar.setSubtitle(getCurrentExerciseName());

        // set up painSlider SeekBar
        SeekBar painSlider = binding.stepPainSlider;
        painSlider.setOnSeekBarChangeListener(getSeekBarChangeListener());

        setupWorkoutStepViewRows();

        binding.stepBtnNext.setOnClickListener(getViewOnClickListener());
    }

    private String getCurrentExerciseName() {
        return database.exerciseNameDAO().getMainNameByID(currentWorkoutStep.exerciseID);
    }

    private Workout getCurrentWorkout() {
        return database.workoutDAO().getWorkoutByID(currentWorkoutStep.workoutID);
    }

    private View.OnClickListener getViewOnClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentWorkoutStepSavable()) {
                    saveCurrentWorkoutStep();

                    if (isLastWorkoutStep(currentWorkoutStep)){
                        finish();
                    }
                    else {
                        startNextWorkoutStep();
                    }
                }
            }
        };
    }

    private boolean currentWorkoutStepSavable() {
        // TODO implement
        return false;
    }

    private SeekBar.OnSeekBarChangeListener getSeekBarChangeListener() {

        return new SeekBar.OnSeekBarChangeListener() {
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
        };
    }

    private void populateRows(com.rafaelwitak.gymdatabro.databinding.ActivityWorkoutStepBinding binding) {
        rows.addAll(Arrays.asList(
                new RepsRow(this, binding, currentWorkoutStep),
                new WeightRow(this, binding, currentWorkoutStep),
                new RPERow(this, binding, currentWorkoutStep),
                new DurationRow(this, binding, currentWorkoutStep),
                new RestRow(this, binding, currentWorkoutStep)
                )
        );
    }

    private void saveCurrentWorkoutStep() {
        // TODO implement

    }

    private void startNextWorkoutStep() {
        Intent intent = getIntentWithExtras();

        startActivity(intent);
    }

    private Intent getIntentWithExtras() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("workoutID", currentWorkoutStep.workoutID);
        intent.putExtra("nextStepNumber", currentWorkoutStep.number + 1);

        return intent;
    }

    public boolean isLastWorkoutStep(WorkoutStep currentWorkoutStep) {

        final Workout currentWorkout = database.workoutDAO().getWorkoutByID(currentWorkoutStep.workoutID);
        final List<WorkoutStep> workoutSteps = database.workoutStepDAO().getAllStepsForWorkout(currentWorkout.id).getValue();
        final int numberOfStepsInWorkout = Objects.requireNonNull(workoutSteps).size();

        return (currentWorkoutStep.number + 1 == numberOfStepsInWorkout);
    }

    private boolean painLevelInsideBounds(Integer painLevel) {
        return (0 <= painLevel && painLevel <= getResources().getInteger(R.integer.pain_max));
    }

    // Set visibility and/or data for the WorkoutStep's View's Rows
    private void setupWorkoutStepViewRows() {

        for ( WorkoutStepRow row : rows ) {
            row.setup();
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