package com.rafaelwitak.gymdatabro.activities;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.rafaelwitak.gymdatabro.database.GymBroDatabase;
import com.rafaelwitak.gymdatabro.database.MasterDao;
import com.rafaelwitak.gymdatabro.database.PerformanceSet;
import com.rafaelwitak.gymdatabro.database.WorkoutInstance;
import com.rafaelwitak.gymdatabro.database.WorkoutStep;

public class IntentMaker {

    private final Context context;
    private final MasterDao dao;

    IntentMaker(Context context) {
        this.context = context;
        this.dao = GymBroDatabase.getDatabase(context).masterDao();
    }

    @NonNull
    public Intent getEditExerciseIntent() {
        return new Intent(context, EditExerciseActivity.class);
    }

    @NonNull
    public Intent getEditWorkoutStepIntent() {
        return new Intent(context, EditWorkoutStepActivity.class);
    }

    @NonNull
    public Intent getIntentToResumeWorkoutOrChooseProgram() {
        WorkoutInstance workoutInstance = getInstanceToResumeFromLastTime();
        if (workoutInstance == null) {
            Log.d("GDB", "workoutInstance == null");
            return getChooseProgramIntent();
        }
        Integer nextWorkoutStepNumber =
                getNextWorkoutStepNumber(workoutInstance.programId, workoutInstance);

        return createWorkoutStepIntentWithExtras(workoutInstance, nextWorkoutStepNumber);
    }

    @NonNull
    public Intent getChooseProgramIntent() {
        return new Intent(context, ChooseProgramActivity.class);
    }

    @NonNull
    private Intent createWorkoutStepIntentWithExtras(
            WorkoutInstance workoutInstance,
            Integer nextWorkoutStepNumber) {
        Intent intent = new Intent(context, WorkoutStepActivity.class);

        if (workoutInstance != null) {
            intent.putExtra("workoutInstanceId", workoutInstance.id);
            intent.putExtra("workoutID", workoutInstance.workoutId);
            intent.putExtra("nextStepNumber", nextWorkoutStepNumber);
        }

        return intent;
    }

    @Nullable
    public WorkoutInstance getInstanceToResumeFromLastTime() {
        WorkoutInstance latestInstance = dao.getLatestWorkoutInstance();
        if (latestInstance == null) {
            return null;
        }

        if (dao.isLastStepOfWorkout(
                latestInstance.workoutId,
                getLatestWorkoutStepNumber())) {
            if (dao.isLastInstanceOfProgram(latestInstance.id, latestInstance.programId)) {
                return null;
            }
            return dao.getNextWorkoutInstanceForProgram(
                    latestInstance.programId,
                    latestInstance.workoutNumber);
        }
        return latestInstance;
    }

    @NonNull
    private Integer getLatestWorkoutStepNumber() {
        return dao.getWorkoutStepById(
                dao.getLatestPerformanceSet().workoutStepId)
                .number;
    }

    @Nullable
    private Integer getNextWorkoutStepNumber(Integer programId, WorkoutInstance instance) {
        WorkoutStep nextStep = dao.getNextWorkoutStepForProgramId(programId, instance);
        if (nextStep == null) {
            Log.d("GDB", "nextStep == null");
            return null;
        }
        return nextStep.number;
    }

    @NonNull
    public Intent getIntentToResumeProgram(int programId) {
        PerformanceSet latestPerformanceSet =
                dao.getLatestPerformanceSetForProgramId(programId);
        WorkoutInstance instance =
                getWorkoutInstanceToResumeProgram(programId, latestPerformanceSet);

        return createWorkoutStepIntentWithExtras(
                instance,
                getNextWorkoutStepNumber(programId, instance));
    }

    @Nullable
    public WorkoutInstance getWorkoutInstanceToResumeProgram(
            int programId,
            PerformanceSet latestSetOfProgram) {
        if (latestSetOfProgram == null) {
            return dao.getFirstWorkoutInstanceForProgram(programId);
        }

        WorkoutInstance latestInstance =
                dao.getLatestWorkoutInstanceForProgram(programId);
        if (latestInstance == null
                || dao.isLastInstanceOfProgram(latestInstance.id, programId)) {
            return dao.getFirstWorkoutInstanceForProgram(programId);
        }


        WorkoutStep latestWorkoutStep =
                dao.getWorkoutStepById(latestSetOfProgram.workoutStepId);
        if (dao.isLastStepOfWorkout(
                latestWorkoutStep.workoutID,
                latestWorkoutStep.number)) {

            return dao.getNextWorkoutInstanceForProgram(
                    programId,
                    latestInstance.workoutNumber);
        }
        return latestInstance;
    }
}