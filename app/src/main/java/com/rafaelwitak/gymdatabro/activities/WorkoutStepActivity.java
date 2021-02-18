/*
 * Copyright (c) 2020, Rafael Witak.
 */

package com.rafaelwitak.gymdatabro.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.rafaelwitak.gymdatabro.database.Exercise;
import com.rafaelwitak.gymdatabro.database.GymBroDatabase;
import com.rafaelwitak.gymdatabro.database.PerformanceSet;
import com.rafaelwitak.gymdatabro.database.Workout;
import com.rafaelwitak.gymdatabro.database.WorkoutStep;
import com.rafaelwitak.gymdatabro.databinding.ActivityWorkoutStepBinding;
import com.rafaelwitak.gymdatabro.performanceSetHandling.PerformanceSetDataProviderHolder;
import com.rafaelwitak.gymdatabro.performanceSetHandling.PerformanceSetMaker;
import com.rafaelwitak.gymdatabro.util.OneRepMax;
import com.rafaelwitak.gymdatabro.util.WeightProvider;
import com.rafaelwitak.gymdatabro.workoutStepHandling.WorkoutStepRowHolder;

import java.util.Locale;

import static com.rafaelwitak.gymdatabro.util.OneRepMax.getMaxNumberOfReps;
import static com.rafaelwitak.gymdatabro.util.OneRepMax.getWeightFromOrm;

public class WorkoutStepActivity extends AppCompatActivity {
    private GymBroDatabase database;

    private Workout currentWorkout;
    private WorkoutStep currentWorkoutStep;
    private ActivityWorkoutStepBinding binding;
    private Exercise currentExercise;

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
        this.currentExercise = getCurrentExercise();
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

        WorkoutStep step =
                database.workoutStepDAO()
                        .getWorkoutStep(workoutID, stepNumber);

        if (step == null) {
            Log.d("GymDataBro", "This WorkoutStep doesn't exist.");
        }
        return step;
    }

    private int getWorkoutIdFromIntent() {
        return getIntent().getIntExtra("workoutID", 1);
    }

    private int getStepNumberFromIntent() {
        return getIntent().getIntExtra("nextStepNumber", 0);
    }

    private void updateCurrentWorkoutStepWeight() {
        if (currentWorkoutStep.getWeight() == null) {
            return;
        }

        Float recentWeight = WeightProvider.getRecentStrengthBasedWeight(
                database.masterDao()
                        .getLatestWeightRepsRpeForExercise(currentExercise.getId()),
                currentWorkoutStep);
        if (recentWeight != null) {
            currentWorkoutStep.setWeight(recentWeight);
            return;
        }

        Float ormBasedWeight =
                getWeightFromOrm(
                        currentWorkoutStep.getWeight(),
                        getMaxNumberOfReps(
                                currentWorkoutStep.getReps(),
                                currentWorkoutStep.getRpe()
                        ),
                        currentExercise.getPr(),
                        OneRepMax.getFormula());
        if (ormBasedWeight != null) {
            currentWorkoutStep.setWeight(ormBasedWeight);
        }
    }


    @NonNull
    private Workout getCurrentWorkout() {
        Workout currentWorkout =
                database.workoutDAO().getWorkoutByID(currentWorkoutStep.getWorkoutID());

        if (currentWorkout == null) {
            return new Workout();
        }

        return currentWorkout;
    }

    @NonNull
    private Exercise getCurrentExercise() {
        return database.exerciseDAO().getExerciseByID(currentWorkoutStep.getExerciseID());
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
            if (currentWorkout.getName().isEmpty()) {
                return "Unnamed Workout";
            }
            return currentWorkout.getName();
        }
        return workoutInstanceName;
    }

    private String getWorkoutInstanceName(Integer workoutInstanceId) {
        return database.workoutInstanceDAO().getNameByInstanceId(workoutInstanceId);
    }

    @Nullable
    private String getCurrentProgramName() {
        Integer id = currentWorkout.getProgramID();
        if (id != null) {
            return database.programDAO().getProgramByID(id).getName();
        }
        return null;
    }

    private void setUpButton() {
        binding.stepBtnNext.setOnClickListener(v ->
            wrapUpCurrentPerformanceSet()
        );
    }

    private void wrapUpCurrentPerformanceSet() {
        PerformanceSet performedSet = getPerformedSet();
        savePerformanceSet(performedSet);
        handleNewRecords(performedSet);

        if (isLastWorkoutStep(currentWorkoutStep)) {
            congratulateAndFinish();
        } else {
            startNextWorkoutStep();
        }
    }

    @NonNull
    private PerformanceSet getPerformedSet() {
        return PerformanceSetMaker.getPerformanceSet(getPerformanceSetDataProviderHolder());
    }

    private void savePerformanceSet(PerformanceSet performanceSet) {
        long savedSetRowId = database.performanceSetDAO().insertSet(performanceSet);

        Log.d("GymDataBro", "PerformanceSet successfully saved to database by "
                + "WorkoutStepActivity.savePerformanceSet():"
                + "\n"
                + database.performanceSetDAO().getSetByRowId(savedSetRowId).toString());
    }

    private void handleNewRecords(@NonNull PerformanceSet performedSet) {
        @Nullable Float previousOrm = currentExercise.getPr();
        @Nullable Float currentOrm = getOneRepMaxValue(
                performedSet.getReps(),
                performedSet.getWeight(),
                performedSet.getRpe());

        if (isNewPr(previousOrm, currentOrm)) {
            currentExercise.setPr(currentOrm);
            database.exerciseDAO().updateExercise(currentExercise);

            showNewOrmMessage(performedSet, currentOrm);
        }
    }

    private Float getOneRepMaxValue(Integer reps, Float weight, Float rpe) {
        if (reps == null || weight == null) {
            return null;
        }
        if (rpe == null) {
            rpe = 10f; // If no RPE is given, maximum exertion is assumed.
        }
        return OneRepMax.getFormula().getOrm(weight, getMaxNumberOfReps(reps, rpe));
    }

    private void showNewOrmMessage(@NonNull PerformanceSet performedSet, Float currentOrm) {
        Toast.makeText(
                this,
                String.format(
                        Locale.ENGLISH,
                        "Congrats! That was a new personal best: \n" +
                                "%dx%.2fkg " +
                                "(ORM %.2fkg)",
                        performedSet.getReps(),
                        performedSet.getWeight(),
                        currentOrm),
                Toast.LENGTH_LONG).show();
    }

    private boolean isNewPr(Float previousOrm, Float currentOrm) {
        if (previousOrm == null && currentOrm != null) {
            return true;
        }
        if (currentOrm == null) {
            return false;
        }
        return previousOrm < currentOrm;
    }

    private boolean isLastWorkoutStep(@NonNull WorkoutStep currentWorkoutStep) {
        return database.masterDao()
                .isLastStepOfWorkout(
                        currentWorkoutStep.getWorkoutID(),
                        currentWorkoutStep.getNumber());
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
        intent.putExtra("workoutID", currentWorkoutStep.getWorkoutID());
        intent.putExtra("nextStepNumber", nextWorkoutStepNumber);
        intent.putExtra("workoutInstanceId", getWorkoutInstanceId());

        return intent;
    }

    @Nullable
    private Integer getNextWorkoutStepNumber() {
        WorkoutStep nextStep = database.masterDao()
                .getNextStepInWorkout(currentWorkoutStep.getId(), currentWorkout.getId());
        if (nextStep == null) {
            return null;
        }
        return nextStep.getNumber();
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


    /**
     * Set visibility and/or data for the WorkoutStep's View's Rows
     */
    private void setUpWorkoutStepViewRows() {
        WorkoutStepRowHolder rowHolder = new WorkoutStepRowHolder(
                binding,
                currentWorkoutStep,
                this);
        rowHolder.setupAllRows();
    }
}