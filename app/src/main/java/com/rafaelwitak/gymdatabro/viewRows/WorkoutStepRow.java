package com.rafaelwitak.gymdatabro.viewRows;

import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.rafaelwitak.gymdatabro.PerformanceSetDataHolder;
import com.rafaelwitak.gymdatabro.PerformanceSetUserInputProvider;
import com.rafaelwitak.gymdatabro.database.WorkoutStep;
import com.rafaelwitak.gymdatabro.databinding.ActivityWorkoutStepBinding;
public abstract class WorkoutStepRow implements PerformanceSetDataHolder {
    protected final ActivityWorkoutStepBinding binding;
    protected final WorkoutStep currentWorkoutStep;
    protected final View rowView;
    protected final TextView expectedValueView;
    protected final EditText actualValueView;

    @Override
    public Object getCurrentValue() {
        return this.getActualValue();
    }

    public WorkoutStepRow(
            ActivityWorkoutStepBinding binding,
            WorkoutStep workoutStep) {

        this.currentWorkoutStep = workoutStep;
        this.binding = binding;

        this.rowView = getRowViewFromBinding();
        this.expectedValueView = getExpectedValueView();
        this.actualValueView = getActualValueView();
    }

    protected abstract View getRowViewFromBinding();

    public void setup() {
        if (this.shouldBeVisible()) {
            setAllViewTexts();
        }
        else {
            this.makeInvisible();
        }
    }

    protected void setAllViewTexts() {
        expectedValueView.setText(this.getExpectedValue().toString());
        actualValueView.setHint(this.getActualValue().toString());
    }

    public void makeInvisible() {
        this.rowView.setVisibility(View.GONE);
    }

    protected abstract boolean shouldBeVisible();

    protected abstract Object getExpectedValue();

    public Object getActualValue() {
        return this.actualValueView.getText();
    }

    protected abstract TextView getExpectedValueView();

    protected abstract EditText getActualValueView();
}
