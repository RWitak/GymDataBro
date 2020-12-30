/*
 * Copyright (c) 2020, Rafael Witak.
 */

package com.rafaelwitak.gymdatabro.performanceSetHandling;

import com.rafaelwitak.gymdatabro.database.PerformanceSet;

public interface PerformanceSetDataProvider {
    abstract PerformanceSet getUpdatedPerformanceSet(PerformanceSet performanceSet);
}
