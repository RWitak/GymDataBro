package com.rafaelwitak.gymdatabro;

import org.junit.Test;

import static com.google.common.truth.Truth.assertThat;

public class OneRepMaxTest {

    @Test
    public void getFormula() {
        assertThat(OneRepMax.getFormula()).isInstanceOf(OneRepMax.OrmFormula.class);
    }

    @Test
    public void getWeightShouldReturnSensibleResult() {
        OneRepMax.OrmFormula formula = OneRepMax.getFormula();
        float orm = 50;
        int reps = 10;

        float calculatedWeight = formula.getWeight(reps, orm);
        assertThat(calculatedWeight).isGreaterThan(orm / reps);
        assertThat(calculatedWeight).isLessThan(orm);
        assertThat(calculatedWeight).isEqualTo(40); // for O'Conner


        formula = OneRepMax.getFormula(OneRepMax.FORMULA.LOMBARDI);
        orm = 50;
        reps = 12;

        calculatedWeight = formula.getWeight(reps, orm);
        assertThat(calculatedWeight).isGreaterThan(orm / reps);
        assertThat(calculatedWeight).isLessThan(orm);
        assertThat(Math.round(calculatedWeight)).isEqualTo(39); // for Lombardi
    }
}