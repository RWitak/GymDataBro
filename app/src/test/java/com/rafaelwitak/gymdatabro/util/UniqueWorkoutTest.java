/*
 * Copyright (c) 2021, Rafael Witak.
 */

package com.rafaelwitak.gymdatabro.util;

import org.junit.Test;

import static com.google.common.truth.Truth.assertThat;

public class UniqueWorkoutTest {
    @Test
    public void comparingSameShouldReturnZero() {
        UniqueWorkout u1 = new UniqueWorkout(420, 69, 42, 17, "someName", null, null);
        UniqueWorkout u1copy = new UniqueWorkout(420, 69, 42, 17, "someName", null, null);
        assertThat(u1).isEquivalentAccordingToCompareTo(u1copy);
    }

    @Test
    public void workoutIdShouldPrecedeWorkoutNumberInComparison() {
        UniqueWorkout higherNumberSameWorkoutId = new UniqueWorkout(10, 4, 300, 15, null, null, null);
        UniqueWorkout lowNumberHighWorkoutId = new UniqueWorkout(10, 3, 300, 15, null, null, null);
        assertThat(lowNumberHighWorkoutId).isLessThan(higherNumberSameWorkoutId);

        UniqueWorkout highNumberLowWorkoutId = new UniqueWorkout(10, 300, 3, 15, null, null, null);
        assertThat(highNumberLowWorkoutId).isLessThan(lowNumberHighWorkoutId);
    }
}