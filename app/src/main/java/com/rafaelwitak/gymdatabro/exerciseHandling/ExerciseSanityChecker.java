/*
 * Copyright (c) 2020, Rafael Witak.
 */

package com.rafaelwitak.gymdatabro.exerciseHandling;

import com.rafaelwitak.gymdatabro.database.Exercise;
import com.rafaelwitak.gymdatabro.database.MasterDao;

import java.util.List;

public class ExerciseSanityChecker {

public static int getStatus(Exercise exercise, MasterDao dao) {
    if (exercise.getName() == null)
        return Status.NAME_MISSING;
    if (exercise.getName().length() <= 2)
        return Status.NAME_TOO_SHORT;
    if (isExistingExerciseName(exercise, dao))
        return Status.NAME_ALREADY_EXISTS;

    return Status.SAVABLE;
}

    @SuppressWarnings("RedundantIfStatement")
    private static boolean isExistingExerciseName(
            Exercise exercise,
            MasterDao dao) {

    List<Exercise> savedExercisesWithName =
            dao.getExercisesByName(exercise.getName());

        if (savedExercisesWithName.isEmpty()) {
            return false;
        } else if (savedExercisesWithName.size() == 1
                && savedExercisesWithName.get(0).getId() == exercise.getId())
            return false;

        return true;
    }


    public static class Status {
        public static int SAVABLE = 0;
        public static int NAME_MISSING = 1;
        public static int NAME_TOO_SHORT = 2;
        public static int NAME_ALREADY_EXISTS = 3;

        public static boolean isBadName(int sanityStatus) {
            return sanityStatus == NAME_TOO_SHORT
                    || sanityStatus == NAME_ALREADY_EXISTS
                    || sanityStatus == NAME_MISSING;
        }
    }
}
