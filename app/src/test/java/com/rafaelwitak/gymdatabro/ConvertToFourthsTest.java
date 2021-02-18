/*
 * Copyright (c) 2020, Rafael Witak.
 */

package com.rafaelwitak.gymdatabro;

import org.junit.Test;

import static com.google.common.truth.Truth.assertThat;
import static com.rafaelwitak.gymdatabro.util.ConvertToFourths.convertToFourthsPrecision;

public class ConvertToFourthsTest {

    @Test
    public void convertToFourthsPrecisionTest() {
        assertThat(convertToFourthsPrecision(23.4567)).isEqualTo(23.5f);
        assertThat(convertToFourthsPrecision(24.79)).isEqualTo(24.75f);
    }
}