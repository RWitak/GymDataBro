package com.rafaelwitak.gymdatabro.workoutStepHandling;

import android.widget.EditText;
import android.widget.TextView;

import com.rafaelwitak.gymdatabro.database.WorkoutStep;
import com.rafaelwitak.gymdatabro.databinding.ActivityWorkoutStepBinding;

public abstract class WorkoutStepTextInputRow extends WorkoutStepRow {
    protected final TextView expectedValueTextView;
    protected final EditText actualValueEditText;

    public WorkoutStepTextInputRow(ActivityWorkoutStepBinding binding,
                                   WorkoutStep workoutStep) {
        super(binding, workoutStep);
        this.expectedValueTextView = getExpectedValueTextView();
        this.actualValueEditText = getActualValueEditText();
    }


    protected abstract TextView getExpectedValueTextView();

    protected abstract EditText getActualValueEditText();


    public void setup() {
        if (this.shouldBeVisible()) {
            setAllViewTexts();
        }
        else {
            this.makeInvisible();
        }
    }

    protected void setAllViewTexts() {
        expectedValueTextView.setText(this.getExpectedValue().toString());
        actualValueEditText.setHint(this.getActualValueHint().toString());
    }

    public Object getActualValueHint() {
        //TODO get a better hint
        return this.actualValueEditText.getText();
    }

    protected abstract Object getExpectedValue();
}
