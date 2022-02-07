/*
 * Copyright (c) 2022, Rafael Witak.
 */

package com.rafaelwitak.gymdatabro.util;

import com.rafaelwitak.gymdatabro.database.WorkoutStep;
import org.junit.Test;

import static com.google.common.truth.Truth.assertThat;
import static com.rafaelwitak.gymdatabro.util.WeightProvider.getRecommendationOrNull;

public class WeightProviderTest {

    @Test
    public void ignoreNonRepBasedSteps() {
        WorkoutStep ws = new WorkoutStep();
        ws.setWeight(0.0F);
        ws.setDurationSeconds(20);
        assertThat(getRecommendationOrNull(ws, null, null)).isNull();
    }
}