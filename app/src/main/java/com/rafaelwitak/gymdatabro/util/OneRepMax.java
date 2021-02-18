/*
 * Copyright (c) 2021, Rafael Witak.
 */

package com.rafaelwitak.gymdatabro.util;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * Use this class to instantiate a formula (specific or default),
 * whose methods can then be used for OneRepMax calculations.
 */
public abstract class OneRepMax {
    private static final OrmFormula STANDARD_FORMULA = new OConner();
    public enum FORMULA {
        LOMBARDI,
        O_CONNER
    }

    public static OrmFormula getFormula() {
        return STANDARD_FORMULA;
    }

    public static OrmFormula getFormula(FORMULA formula) {
        switch (formula) {
            case LOMBARDI:
                return new Lombardi();
            case O_CONNER:
                return new OConner();
            default:
                return STANDARD_FORMULA;
        }
    }

    /**
     * The maximum number of possible reps,
     * found using inverted RPE, a kind of "Reps in Reverse" system
     * (10 RPE is failure, every step below is one more repetition "in the tank").
     * @param reps Repetitions
     * @param rpe Rate of Perceived Exertion
     */
    @NonNull
    public static Integer getMaxNumberOfReps(Integer reps, Float rpe) { // FIXME: Hotfix!
        if (rpe == null) {
            return reps;
        }
        if (reps == null) { // FIXME: This is a hotfix for having this called without reps
            return 0;
        }
        return (reps + Math.round(10 - rpe));
    }

    /**
     * Calculate the expected maximum weight
     * for the expected maximum reps.
     */
    @Nullable
    public static Float getWeightFromOrm(Float weight,
                                   Integer maxReps,
                                   Float orm,
                                   @NonNull OrmFormula formula) {
        if (orm == null || weight == null) {
            return null;
        }
        if (maxReps == null) {
            return weight;
        }
        if (maxReps == 0) { // FIXME: maxReps == 0 is hotfix!
            return null;
        }
        return formula.getWeight(maxReps, orm);
    }


    protected interface WeightCalculator {
        float getWeight(int reps, float orm);
    }
    protected interface RepsCalculator {
        int getReps(float weight, float orm);
    }
    protected interface OrmCalculator {
        float getOrm(float weight, int reps);
    }

    public abstract static class OrmFormula implements
            WeightCalculator,
            RepsCalculator,
            OrmCalculator
    {}


    private static class Lombardi extends OrmFormula {
        @Override
        public float getWeight(int reps, float orm) {
            if (orm == 0) {
                return 0;
            }
            return (float) (orm / Math.pow(reps, 0.1));
        }

        @Override
        public int getReps(float weight, float orm) {
            if (weight == 0) {
                return 0;
            }
            return (int) Math.round(Math.pow(orm / weight, 10));
        }

        @Override
        public float getOrm(float weight, int reps) {
            return (float) (weight * Math.pow(reps, 0.1));
        }
    }

    private static class OConner extends OrmFormula {
        @Override
        public float getWeight(int reps, float orm) {
            if (orm == 0) {
                return 0;
            }
            return orm / (1 + ((float) reps / 40));
        }

        @Override
        public int getReps(float weight, float orm) {
            if (weight == 0) {
                return 0;
            }
            return Math.round(40 * ((orm / weight) - 1));
        }

        @Override
        public float getOrm(float weight, int reps) {
            return weight * (1 + ((float) reps / 40));
        }
    }
}
