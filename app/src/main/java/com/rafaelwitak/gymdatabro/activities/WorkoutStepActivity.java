package com.rafaelwitak.gymdatabro.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.rafaelwitak.gymdatabro.OneRepMax;
import com.rafaelwitak.gymdatabro.database.Exercise;
import com.rafaelwitak.gymdatabro.database.GymBroDatabase;
import com.rafaelwitak.gymdatabro.database.PerformanceSet;
import com.rafaelwitak.gymdatabro.database.Workout;
import com.rafaelwitak.gymdatabro.database.WorkoutStep;
import com.rafaelwitak.gymdatabro.databinding.ActivityWorkoutStepBinding;
import com.rafaelwitak.gymdatabro.performanceSetHandling.PerformanceSetDataProviderHolder;
import com.rafaelwitak.gymdatabro.performanceSetHandling.PerformanceSetMaker;
import com.rafaelwitak.gymdatabro.workoutStepHandling.WorkoutStepRowHolder;

public class WorkoutStepActivity extends AppCompatActivity {
    private GymBroDatabase database;

    private Workout currentWorkout;
    private WorkoutStep currentWorkoutStep;
    private ActivityWorkoutStepBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.database = GymBroDatabase.getDatabase(this);
        this.currentWorkoutStep = getCurrentWorkoutStep();
        if (currentWorkoutStep == null) {
            Toast.makeText(this,
                    "Please create a WorkoutStep first!",
                    Toast.LENGTH_SHORT).show();
            finish();
            return; // DO NOT DELETE: Method will try to continue without a proper WorkoutStep!
        }
        updateCurrentWorkoutStepWeight();
        this.currentWorkout = getCurrentWorkout();

        // automatically bind all Views with IDs
        this.binding = ActivityWorkoutStepBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setUpViews();
    }

    private WorkoutStep getCurrentWorkoutStep() {
        int workoutID = getWorkoutIdFromIntent();
        int stepNumber = getStepNumberFromIntent();

        Log.d("GymDataBro", "Fetching Workout " +
                + workoutID
                + ", Step "
                + stepNumber);

        return database
                .workoutStepDAO()
                .getWorkoutStep(workoutID, stepNumber);
    }

    private int getWorkoutIdFromIntent() {
        return getIntent().getIntExtra("workoutID", 1);
    }

    private int getStepNumberFromIntent() {
        return getIntent().getIntExtra("nextStepNumber", 0);
    }

    private void updateCurrentWorkoutStepWeight() {
        currentWorkoutStep.weight =
                getWeightFromOrm(
                        currentWorkoutStep.weight,
                        getCurrentExercise(),
                        OneRepMax.getFormula());
    }

    @Nullable
    private Float getWeightFromOrm(Float weight,
                                   @NonNull Exercise exercise,
                                   @NonNull OneRepMax.OrmFormula formula) {
        if (weight != null && currentWorkoutStep.reps != null) {
            float orm = (exercise.pr != null)
                    ? exercise.pr
                    : 0;
            weight = formula.getWeight(getMaxNumberOfReps(), orm);
        }
        return weight;
    }

    /** The maximum number of possible reps, found using RPE (10 RPE is failure).*/
    private Integer getMaxNumberOfReps() {
        if (currentWorkoutStep.rpe == null) {
            return currentWorkoutStep.reps;
        }
        return (currentWorkoutStep.reps + Math.round(10 - currentWorkoutStep.rpe));
    }

    private Exercise getCurrentExercise() {
        return database.exerciseDAO().getExerciseByID(currentWorkoutStep.exerciseID);
    }

    @NonNull
    private Workout getCurrentWorkout() {
        Workout currentWorkout = database.workoutDAO().getWorkoutByID(currentWorkoutStep.workoutID);

        if (currentWorkout == null) {
            return new Workout();
        }

        return currentWorkout;
    }


    private void setUpViews() {
        setUpToolbar();
        setUpWorkoutStepViewRows();
        setUpButton();
    }
    private void setUpToolbar() {
        Toolbar toolbar = binding.toolbar.getRoot();
        setSupportActionBar(toolbar);
        this.setTitle(getToolbarTitle()); // otherwise toolbar always just displays app name

        String programName = getCurrentProgramName();
        if (programName != null) {
            toolbar.setSubtitle(programName);
        }
    }

    private String getToolbarTitle() {
        String workoutInstanceName = getWorkoutInstanceName(getWorkoutInstanceId());
        if (workoutInstanceName == null || workoutInstanceName.isEmpty()) {
            if (currentWorkout.name.isEmpty()) {
                return "Unnamed Workout";
            }
            return currentWorkout.name;
        }
        return workoutInstanceName;
    }

    private String getWorkoutInstanceName(Integer workoutInstanceId) {
        return database.workoutInstanceDAO().getNameByInstanceId(workoutInstanceId);
    }

    @Nullable
    private String getCurrentProgramName() {
        Integer id = currentWorkout.programID;
        if (id != null) {
            return database.programDAO().getProgramByID(id).name;
        }
        return null;
    }

    private void setUpButton() {
        binding.stepBtnNext.setOnClickListener(getViewOnClickListener());
    }

    @NonNull
    private View.OnClickListener getViewOnClickListener() {
        return v -> {
            savePerformanceSet(getPerformedSet());

            if (isLastWorkoutStep(currentWorkoutStep)){
                congratulateAndFinish();
            }
            else {
                startNextWorkoutStep();
            }
        };
    }

    private void savePerformanceSet(PerformanceSet performanceSet) {
        long savedSetRowId = database.performanceSetDAO().insertSet(performanceSet);

        Log.d("GymDataBro", "PerformanceSet successfully saved to database by "
                + "WorkoutStepActivity.savePerformanceSet():"
                + "\n"
                + database.performanceSetDAO().getSetByRowId(savedSetRowId).toString());
    }

    @NonNull
    private PerformanceSet getPerformedSet() {
        return PerformanceSetMaker.getPerformanceSet(getPerformanceSetDataProviderHolder());
    }

    private boolean isLastWorkoutStep(@NonNull WorkoutStep currentWorkoutStep) {
        return database.masterDao()
                .isLastStepOfWorkout(
                        currentWorkoutStep.workoutID,
                        currentWorkoutStep.number);
    }

    @NonNull
    private PerformanceSetDataProviderHolder getPerformanceSetDataProviderHolder() {
        return new PerformanceSetDataProviderHolder(
                binding,
                currentWorkoutStep,
                getWorkoutInstanceId());
    }

    public void congratulateAndFinish() {
        Toast.makeText(this, "Congrats, you are done for today! \n" +
                "Have a good rest!", Toast.LENGTH_LONG).show();
        finish();
    }

    private void startNextWorkoutStep() {
        Intent intent = getNewIntentWithExtras();

        startActivity(intent);
        finish();
    }

    @NonNull
    private Intent getNewIntentWithExtras() {
        Intent intent = new Intent(this, WorkoutStepActivity.class);
        Integer nextWorkoutStepNumber = getNextWorkoutStepNumber();
        intent.putExtra("workoutID", currentWorkoutStep.workoutID);
        intent.putExtra("nextStepNumber", nextWorkoutStepNumber);
        intent.putExtra("workoutInstanceId", getWorkoutInstanceId());

        return intent;
    }

    @Nullable
    private Integer getNextWorkoutStepNumber() {
        WorkoutStep nextStep = database.masterDao()
                .getNextStepInWorkout(currentWorkoutStep.id, currentWorkout.id);
        if (nextStep == null) {
            return null;
        }
        return nextStep.number;
    }

    @Nullable
    private Integer getWorkoutInstanceId() {
        int currentWorkoutNumber = getWorkoutInstanceIdFromIntent();
        return currentWorkoutNumber == -1
                ? null
                : currentWorkoutNumber;
    }

    private int getWorkoutInstanceIdFromIntent() {
        return getIntent().getIntExtra("workoutInstanceId", -1);
    }


    /** Set visibility and/or data for the WorkoutStep's View's Rows */
    private void setUpWorkoutStepViewRows() {
        WorkoutStepRowHolder rowHolder = new WorkoutStepRowHolder(
                binding,
                currentWorkoutStep,
                this);
        rowHolder.setupAllRows();
    }
}