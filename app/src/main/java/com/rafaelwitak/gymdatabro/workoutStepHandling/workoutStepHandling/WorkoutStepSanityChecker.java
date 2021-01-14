/*
 * Copyright (c) 2020, Rafael Witak.
 */

package com.rafaelwitak.gymdatabro.workoutStepHandling.workoutStepHandling;

import com.rafaelwitak.gymdatabro.database.ExerciseDAO;
import com.rafaelwitak.gymdatabro.database.WorkoutStep;
import com.rafaelwitak.gymdatabro.database.WorkoutStepDAO;

public class WorkoutStepSanityChecker {
    public static int getStatus(WorkoutStep workoutStep,
                                WorkoutStepDAO workoutStepDAO,
                                ExerciseDAO exerciseDAO,
                                boolean isExistingWorkoutStep) {
        if (workoutStep.getNumber() < 0) {
            return Status.NUMBER_MISSING;
        }
        if (!isExistingWorkoutStep && isAlreadyInDatabase(workoutStep, workoutStepDAO)) {
            return Status.NUMBER_DUPLICATE;
        }
        if (workoutStep.getNumber() < 0) {
            return Status.NUMBER_OUT_OF_RANGE;
        }
        if (workoutStep.getName() != null) {
            if (workoutStep.getName().length() <= 2) {
                return Status.NAME_TOO_SHORT;
            }
        }
        if (workoutStep.getExerciseID() < 0) {
            return Status.EXERCISE_ID_MISSING;
        }
        if (isNonExistingExerciseId(workoutStep.getExerciseID(), exerciseDAO)) {
            return Status.EXERCISE_NON_EXISTENT;
        }
        if (workoutStep.getRpe() != null) {
            if (10 < workoutStep.getRpe() || workoutStep.getRpe() <= 0) {
                return Status.RPE_OUT_OF_RANGE;
            }
        }

        return Status.SAVABLE;
    }

    private static boolean isNonExistingExerciseId(Integer exerciseID, ExerciseDAO exerciseDAO) {
        return exerciseDAO.getExerciseByID(exerciseID) == null;
    }

    private static boolean isAlreadyInDatabase(
            WorkoutStep workoutStep,
            WorkoutStepDAO workoutStepDAO) {
        return workoutStepDAO.
                getWorkoutStep(
                        workoutStep.getWorkoutID(),
                        workoutStep.getNumber())
                != null;
    }

    public static class Status {
        public static final int SAVABLE = 0;
        public static final int NUMBER_MISSING = 10;
        public static final int NUMBER_DUPLICATE = 11;
        public static final int NUMBER_OUT_OF_RANGE = 12;
        public static final int NAME_TOO_SHORT = 20;
        public static final int EXERCISE_ID_MISSING = 30;
        public static final int EXERCISE_NON_EXISTENT = 31;
        public static final int RPE_OUT_OF_RANGE = 40;

        public static boolean isBadNumber(int sanityStatus) {
            return (10 <= sanityStatus && sanityStatus < 20);
        }

        public static boolean isBadName(int sanityStatus) {
            return (20 <= sanityStatus && sanityStatus < 30);
        }

        public static boolean isBadExerciseId(int sanityStatus) {
            return (30 <= sanityStatus && sanityStatus < 40);
        }

        public static boolean isBadRpe(int sanityStatus) {
            return (40 <= sanityStatus && sanityStatus <50);
        }
    }
}
