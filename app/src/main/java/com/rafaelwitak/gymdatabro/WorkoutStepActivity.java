package com.rafaelwitak.gymdatabro;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.SeekBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.rafaelwitak.gymdatabro.database.GymBroDatabase;
import com.rafaelwitak.gymdatabro.database.PerformanceSet;
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

    private ActivityWorkoutStepBinding binding;
    private ArrayList <WorkoutStepRow> rows = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        database = MainActivity.database;
        currentWorkoutStep = getCurrentWorkoutStep();
        currentWorkout = getCurrentWorkout();
        performedSet = new PerformanceSet();

        // automatically bind all Views with IDs
        binding = ActivityWorkoutStepBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        populateRows();
        setUpToolbar(binding);
        setUpPainSlider(binding);

        setUpWorkoutStepViewRows();

        binding.stepBtnNext.setOnClickListener(getViewOnClickListener());
    }

    private void setUpPainSlider(com.rafaelwitak.gymdatabro.databinding.ActivityWorkoutStepBinding binding) {
        SeekBar painSlider = binding.stepPainSlider;
        painSlider.setOnSeekBarChangeListener(getSeekBarChangeListener());
    }

    private void setUpToolbar(com.rafaelwitak.gymdatabro.databinding.ActivityWorkoutStepBinding binding) {
        Toolbar toolbar = binding.toolbar.getRoot();
        setSupportActionBar(toolbar);
        toolbar.setTitle(currentWorkout.name);
        toolbar.setSubtitle(getCurrentExerciseName());
    }

    private String getCurrentExerciseName() {
       String currentExerciseName = database
               .exerciseNameDAO()
               .getMainNameByID(currentWorkoutStep.exerciseID);

        if (currentExerciseName == null) {
            return "Unnamed Exercise";
        }

        return currentExerciseName;
    }

    private Workout getCurrentWorkout() {
        Workout currentWorkout = database.workoutDAO().getWorkoutByID(currentWorkoutStep.workoutID);

        if (currentWorkout == null) {
            return new Workout();
        }

        return currentWorkout;
    }

    private View.OnClickListener getViewOnClickListener() {
        //noinspection Convert2Lambda
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
        // currently, WorkoutSteps are always savable per definition
        return true;
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

    private void populateRows() {
        rows.addAll(Arrays.asList(
                new RepsRow(binding, currentWorkoutStep),
                new WeightRow(binding, currentWorkoutStep),
                new RPERow(binding, currentWorkoutStep),
                new DurationRow(binding, currentWorkoutStep),
                new RestRow(binding, currentWorkoutStep)
                )
        );
    }

    private void saveCurrentWorkoutStep() {
        database.workoutStepDAO().insertWorkoutStep(currentWorkoutStep);
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

        final List<WorkoutStep> workoutSteps = database.workoutStepDAO().getAllStepsForWorkoutAsLiveData(currentWorkout.id).getValue();
        final int numberOfStepsInWorkout = Objects.requireNonNull(workoutSteps).size();

        return (currentWorkoutStep.number + 1 == numberOfStepsInWorkout);
    }

    private boolean painLevelInsideBounds(Integer painLevel) {
        return (0 <= painLevel && painLevel <= getResources().getInteger(R.integer.pain_max));
    }

    // Set visibility and/or data for the WorkoutStep's View's Rows
    private void setUpWorkoutStepViewRows() {

        for ( WorkoutStepRow row : rows ) {
            row.setup();
        }
    }

    private WorkoutStep getCurrentWorkoutStep() {
        int workoutID = getIntent().getIntExtra("workoutID", 0);
        int stepNumber = getIntent().getIntExtra("nextStepNumber", 0);

        WorkoutStep step = database
                .workoutStepDAO()
                .getWorkoutStepSynchronously(workoutID, stepNumber);

        if (step == null) {
            return new WorkoutStep();
        }

        return step;
    }
}