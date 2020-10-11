package com.rafaelwitak.gymdatabro.performanceSetHandling;

import android.util.Log;

import com.rafaelwitak.gymdatabro.database.PerformanceSet;

public class PerformanceSetMaker {

    public static PerformanceSet getPerformanceSet(PerformanceSetDataProviderHolder dpHolder) {
        PerformanceSet performanceSet = new PerformanceSet();

        for ( PerformanceSetDataProvider dp : dpHolder.getDataProviders() ) {
            performanceSet = dp.getUpdatedPerformanceSet(performanceSet);
        }

        //FIXME ID is always 0, not unique
        Log.i("GymDataBro", "New PerformanceSet created: " +
                "\nID=" + performanceSet.id +
                "\nExerciseId=" + performanceSet.exerciseID +
                "\nReps=" + performanceSet.reps +
                "\nWeight=" + performanceSet.weight +
                "\nDuration=" + performanceSet.secondsPerformed +
                "\nRest=" + performanceSet.secondsRested +
                "\nRPE=" + performanceSet.rpe +
                "\nPainLevel=" + performanceSet.painLevel +
                "\nNotes=" + performanceSet.notes
                );
        return performanceSet;
    }
}
