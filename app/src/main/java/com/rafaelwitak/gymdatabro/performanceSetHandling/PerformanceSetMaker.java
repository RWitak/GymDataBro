package com.rafaelwitak.gymdatabro.performanceSetHandling;

import android.util.Log;

import com.rafaelwitak.gymdatabro.database.PerformanceSet;

public class PerformanceSetMaker {

    public static PerformanceSet getPerformanceSet(PerformanceSetDataProviderHolder dpHolder) {
        PerformanceSet performanceSet = new PerformanceSet();

        for ( PerformanceSetDataProvider dp : dpHolder.getDataProviders() ) {
            performanceSet = dp.getUpdatedPerformanceSet(performanceSet);
        }

        Log.i("GymDataBro", "New PerformanceSet created by PerformanceSetMaker:\n"
                + performanceSet.toString()
                );
        return performanceSet;
    }
}
