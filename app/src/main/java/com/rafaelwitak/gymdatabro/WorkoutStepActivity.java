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

import com.rafaelwitak.gymdatabro.workoutStepRows.WorkoutStepRow;
import com.rafaelwitak.gymdatabro.workoutStepRows.WorkoutStepRowHolder;

import java.util.List;
import java.util.Objects;

public class WorkoutStepActivity extends AppCompatActivity {

    private GymBroDatabase database;
    private Workout currentWorkout;
    private WorkoutStep currentWorkoutStep;
    private PerformanceSet performedSet;
    private ActivityWorkoutStepBinding binding;

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

        setUpViews();
    }


    private void setUpViews() {
        setUpToolbar();
        setUpWorkoutStepViewRows();
        setUpPainSlider();
        setUpButton();
    }

    private void setUpToolbar() {
        Toolbar toolbar = binding.toolbar.getRoot();
        setSupportActionBar(toolbar);
        toolbar.setTitle(getCurrentWorkoutName());
        toolbar.setSubtitle(getCurrentExerciseName());
    }

    // Set visibility and/or data for the WorkoutStep's View's Rows
    private void setUpWorkoutStepViewRows() {
        for ( WorkoutStepRow row : WorkoutStepRowHolder.getRows(binding, currentWorkoutStep) ) {
            row.setup();
        }
    }

    private void setUpPainSlider() {
        SeekBar painSlider = binding.stepPainSlider;
        painSlider.setOnSeekBarChangeListener(getSeekBarChangeListener());
    }

    private void setUpButton() {
        binding.stepBtnNext.setOnClickListener(getViewOnClickListener());
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

    public boolean isLastWorkoutStep(WorkoutStep currentWorkoutStep) {
        final List<WorkoutStep> workoutSteps =
                database
                        .workoutStepDAO()
                        .getAllStepsForWorkoutAsLiveData(currentWorkout.id)
                        .getValue();

        final int numberOfStepsInWorkout = Objects.requireNonNull(workoutSteps).size();

        return (currentWorkoutStep.number + 1 == numberOfStepsInWorkout);
    }

    private boolean painLevelInsideBounds(Integer painLevel) {
        return (0 <= painLevel && painLevel <= getResources().getInteger(R.integer.pain_max));
    }



    private void saveCurrentWorkoutStep() {
        database.workoutStepDAO().insertWorkoutStep(currentWorkoutStep);
    }

    private boolean currentWorkoutStepSavable() {
        // currently, WorkoutSteps are always savable per definition
        return true;
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

    private Workout getCurrentWorkout() {
        Workout currentWorkout = database.workoutDAO().getWorkoutByID(currentWorkoutStep.workoutID);

        if (currentWorkout == null) {
            return new Workout();
        }

        return currentWorkout;
    }


    private String getCurrentWorkoutName() {
        return currentWorkout.name;
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
}