package com.rafaelwitak.gymdatabro.workoutStepRows;

import android.content.Context;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.rafaelwitak.gymdatabro.database.WorkoutStep;
import com.rafaelwitak.gymdatabro.databinding.ActivityWorkoutStepBinding;

// FIXME extending View unnecessary!
public abstract class WorkoutStepRow extends View {
    protected final ActivityWorkoutStepBinding binding;
    protected final Context context;
    protected final WorkoutStep currentWorkoutStep;
    protected final View rowView;
    protected final TextView expectedValueView;
    protected final EditText actualValueView;

    public WorkoutStepRow(
            Context context,
            ActivityWorkoutStepBinding binding,
            WorkoutStep workoutStep) {

        super(context);

        this.context = context;
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
