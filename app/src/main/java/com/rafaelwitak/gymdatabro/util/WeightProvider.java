/*
 * Copyright (c) 2021, Rafael Witak.
 */

package com.rafaelwitak.gymdatabro.util;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.rafaelwitak.gymdatabro.database.MasterDao.WeightRepsRpe;
import com.rafaelwitak.gymdatabro.database.WorkoutStep;

import static com.rafaelwitak.gymdatabro.util.OneRepMax.getMaxNumberOfReps;
import static com.rafaelwitak.gymdatabro.util.OneRepMax.getWeightRecommendationFromOrm;

public class WeightProvider {

    public static final float REDUCING_WEIGHT_FACTOR = .75f;

    @Nullable
    public static Float getRecommendationOrNull(
            WorkoutStep currentWorkoutStep,
            WeightRepsRpe recentPerformance,
            Float pr) {


        Float recommendation = null;

        if (recentPerformance != null) {
            recommendation =
                    tryGettingRecentStrengthBasedRecommendation(
                            currentWorkoutStep,
                            recentPerformance);
        }

        if (recommendation == null) {
            recommendation =
                    tryGettingOrmBasedRecommendation(
                            currentWorkoutStep,
                            pr);
        }
        return recommendation;
    }

    private static Float tryGettingOrmBasedRecommendation(
            @NonNull WorkoutStep currentWorkoutStep,
            Float pr) {

        if (currentWorkoutStep.getReps() != null
                && currentWorkoutStep.getWeight() != null) {

            int maxReps = getMaxNumberOfReps(
                    currentWorkoutStep.getReps(),
                    currentWorkoutStep.getRpe());

            return getWeightRecommendationFromOrm(
                    currentWorkoutStep.getWeight(),
                    maxReps,
                    pr,
                    OneRepMax.getFormula());
        }
        return null;
    }

    @Nullable
    private static Float tryGettingRecentStrengthBasedRecommendation(
            WorkoutStep currentWorkoutStep,
            @NonNull WeightRepsRpe recentPerformance) {

        if (recentPerformance.weight == null) {
            return null;
        }

        if (!currentWorkoutStep.usesReps()) {
            return recentPerformance.weight;
            // for exercises not based on reps, the previous weight may be used...
            // ...for now! But that's not representative,
            // eg. a duration based exercise requires different weight
            // for different durations to be considered equally fatiguing.
        }

        // If the exercise has since been altered to include reps:
        if (recentPerformance.reps == null) {
            return null;
        }

        // Previous set failed? Try again with less weight.
        if (recentPerformance.reps == 0) {
            return getReducedWeight(recentPerformance.weight);
        }

        return getRecentStrengthBasedRecommendation(currentWorkoutStep, recentPerformance);
    }

    private static float getReducedWeight(float weight) {
        return weight * REDUCING_WEIGHT_FACTOR;
    }

    @Nullable
    private static Float getRecentStrengthBasedRecommendation(
            WorkoutStep currentWorkoutStep,
            @NonNull WeightRepsRpe recentPerformance) {

        if (recentPerformance.reps == null || currentWorkoutStep.getReps() == null) {
            return null;
        }

        Integer recentMaxReps =
                getMaxNumberOfReps(
                        recentPerformance.reps,
                        recentPerformance.rpe);

        Integer currentMaxReps =
                getMaxNumberOfReps(
                        currentWorkoutStep.getReps(),
                        currentWorkoutStep.getRpe());

        float recentOrm =
                getRecentOrm(recentPerformance, recentMaxReps);

        return getWeightRecommendationFromOrm(
                currentWorkoutStep.getWeight(),
                currentMaxReps,
                recentOrm,
                OneRepMax.getFormula()
        );
    }

    private static float getRecentOrm(@NonNull WeightRepsRpe recentPerformance, Integer maxReps) {
        return OneRepMax.getFormula().getOrm(recentPerformance.weight, maxReps);
    }

}
