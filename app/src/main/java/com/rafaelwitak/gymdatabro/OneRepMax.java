package com.rafaelwitak.gymdatabro;

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


    protected interface WeightCalculator {
        float getWeight(float reps, float orm);
    }
    protected interface RepsCalculator {
        int getReps(float weight, float orm);
    }
    protected interface OrmCalculator {
        float getOrm(float weight, float reps);
    }

    public abstract static class OrmFormula implements
            WeightCalculator,
            RepsCalculator,
            OrmCalculator
    {}


    private static class Lombardi extends OrmFormula {
        @Override
        public float getWeight(float reps, float orm) {
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
        public float getOrm(float weight, float reps) {
            return (float) (weight * Math.pow(reps, 0.1));
        }
    }

    private static class OConner extends OrmFormula {
        @Override
        public float getWeight(float reps, float orm) {
            if (orm == 0) {
                return 0;
            }
            return orm / (1 + (reps / 40));
        }

        @Override
        public int getReps(float weight, float orm) {
            if (weight == 0) {
                return 0;
            }
            return Math.round(40 * ((orm / weight) - 1));
        }

        @Override
        public float getOrm(float weight, float reps) {
            return weight * (1 + (reps / 40));
        }
    }
}
