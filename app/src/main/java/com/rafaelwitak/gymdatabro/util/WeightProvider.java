/*
 * Copyright (c) 2021, Rafael Witak.
 */

package com.rafaelwitak.gymdatabro.util;

import androidx.annotation.Nullable;

import com.rafaelwitak.gymdatabro.database.MasterDao;
import com.rafaelwitak.gymdatabro.database.WorkoutStep;

import static com.rafaelwitak.gymdatabro.util.OneRepMax.getMaxNumberOfReps;
import static com.rafaelwitak.gymdatabro.util.OneRepMax.getWeightFromOrm;

public class WeightProvider {
    @Nullable
    public static Float getRecentStrengthBasedWeight(
            MasterDao.WeightRepsRpe recentPerformance,
            WorkoutStep currentWorkoutStep) {

        if (recentPerformance == null || recentPerformance.weight == null) {
            return null;
        }
        if (recentPerformance.reps == null) {
            if (currentWorkoutStep.getReps() == null) {
                // for exercises not based on reps, the previous weight may be used...
                // TODO: ...for now! But that's not representative,
                //  eg. a duration based exercise requires different weight
                //  for different durations to be considered equally fatiguing.
                return recentPerformance.weight;
            } else {
                return null;
            }
        }

        Integer maxReps = getMaxNumberOfReps( recentPerformance.reps, recentPerformance.rpe);

        // Previous set failed? Try again with less weight.
        if (recentPerformance.reps == 0) {
            return recentPerformance.weight * .75f;
        }

        float recentOrm = OneRepMax.getFormula().getOrm(recentPerformance.weight, maxReps);

        return getWeightFromOrm(
                currentWorkoutStep.getWeight(),
                getMaxNumberOfReps(currentWorkoutStep.getReps(), currentWorkoutStep.getRpe()),
                recentOrm,
                OneRepMax.getFormula()
        );
    }

}
