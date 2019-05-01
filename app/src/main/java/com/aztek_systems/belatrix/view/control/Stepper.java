package com.aztek_systems.belatrix.view.control;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aztek_systems.belatrix.R;
import com.aztek_systems.belatrix.domain.model.Step;

import java.util.ArrayList;

public class Stepper extends ConstraintLayout {

    private int HORIZONTAL_ORIENTATION = 0;
    private int VERTICAL_ORIENTATION = 1;
    private int orientation = HORIZONTAL_ORIENTATION;
    private Context context;

    private int currentIndex = 0;
    ArrayList<Step> steps;

    public Stepper(Context context, AttributeSet attrs) {
        super(context, attrs);

        this.context = context;

        TypedArray attributes = context.obtainStyledAttributes(attrs,
                R.styleable.Stepper, 0, 0);

        CharSequence[] stepArray = attributes.getTextArray(R.styleable.Stepper_android_entries);
        CharSequence orientationText = attributes.getText(R.styleable.Stepper_orientation);

        if (orientationText != null && orientationText.equals("vertical")) {

            orientation = VERTICAL_ORIENTATION;
        }

        if (stepArray != null) {

            generateSteps(stepArray);
        }

        attributes.recycle();
    }

    public void generateSteps(CharSequence[] stepArray) {

        currentIndex = 0;

        steps = new ArrayList<>();

        for (CharSequence stepText : stepArray) {

            Step step = new Step();

            step.setText(stepText.toString());
            step.setCompleted(false);
            step.setCurrent(false);

            steps.add(step);
        }

        loadSteps();
    }

    private void loadSteps() {

        removeAllViews();

        for (Step step : steps) {

            step.setCurrent(false);
        }

        steps.get(currentIndex).setCurrent(true);

        for (Step step : steps) {

            addView(getStepView(step.getText(), step.isCompleted(), step.isCurrent(), steps.indexOf(step)));
            addView(getMark());
            addView(getSeparator());
        }

        if (orientation == HORIZONTAL_ORIENTATION) {

            generateHorizontalConstraints(steps);
        } else {

            generateVerticalConstraints(steps);
        }
    }

    private View getSeparator() {

        LinearLayout separator = new LinearLayout(context);

        if (orientation == HORIZONTAL_ORIENTATION) {

            separator.setLayoutParams(new LayoutParams(0,
                    (int) getResources().getDimension(R.dimen.separator_height)));
        } else {

            separator.setLayoutParams(new LayoutParams(
                    (int) getResources().getDimension(R.dimen.separator_height), 0));
        }

        separator.setBackgroundColor(ContextCompat.getColor(context, R.color.colorPrimary));

        separator.setId(View.generateViewId());
        return separator;
    }

    private View getStepView(String text, boolean checked, boolean current, int index) {

        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        View stepView;

        if (orientation == HORIZONTAL_ORIENTATION) {

            stepView = inflater.inflate(R.layout.horizontal_step, null);
        } else {

            stepView = inflater.inflate(R.layout.vertical_step, null);
        }

        CheckBox checkBox = stepView.findViewById(R.id.checkbox);
        TextView textView = stepView.findViewById(R.id.text);
        textView.setText(text);

        if (current) {

            checkBox.setText(String.valueOf(index + 1));
            checkBox.setBackground(getResources().getDrawable(R.drawable.current));
        } else {

            if (checked) {

                checkBox.setText("✓");
                checkBox.setBackground(getResources().getDrawable(R.drawable.checked));
            } else {

                checkBox.setText(String.valueOf(index + 1));
                checkBox.setBackground(getResources().getDrawable(R.drawable.unchecked));
            }
        }

        stepView.setId(View.generateViewId());

        return stepView;
    }

    private View getMark() {

        LinearLayout mark = new LinearLayout(context);
        mark.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        if (orientation == HORIZONTAL_ORIENTATION) {

            mark.setBackground(getResources().getDrawable(R.drawable.horizontal_mark_selector));
        } else {

            mark.setBackground(getResources().getDrawable(R.drawable.vertical_mark_selector));
        }

        mark.setId(View.generateViewId());

        return mark;
    }

    private void generateHorizontalConstraints(ArrayList<Step> steps) {

        ConstraintSet set = new ConstraintSet();
        set.clone(this);

        for (int i = 0; i < steps.size(); i++) {

            int currentStepViewIndex = i * 3;
            int currentMarkIndex = i * 3 + 1;
            int currentSeparatorIndex = i * 3 + 2;

            Integer stepViewId = getChildAt(currentStepViewIndex).getId();

            set.connect(stepViewId, ConstraintSet.TOP, getId(), ConstraintSet.TOP);
            set.connect(stepViewId, ConstraintSet.BOTTOM, getId(), ConstraintSet.BOTTOM);

            if (i == 0) {

                set.connect(stepViewId, ConstraintSet.START, getId(), ConstraintSet.START);
                set.connect(stepViewId, ConstraintSet.END, getChildAt(currentStepViewIndex + 3).getId(), ConstraintSet.START);
            } else if (i == steps.size() - 1) {

                set.connect(stepViewId, ConstraintSet.END, getId(), ConstraintSet.END);
                set.connect(stepViewId, ConstraintSet.START, getChildAt(currentStepViewIndex - 3).getId(),
                        ConstraintSet.END);
            } else {

                Integer pId = getChildAt(currentStepViewIndex - 3).getId();
                Integer nId = getChildAt(currentStepViewIndex + 3).getId();

                set.connect(stepViewId, ConstraintSet.START, pId, ConstraintSet.END);
                set.connect(stepViewId, ConstraintSet.END, nId, ConstraintSet.START);
            }

            Integer markId = getChildAt(currentMarkIndex).getId();

            set.connect(markId, ConstraintSet.START, stepViewId, ConstraintSet.END);
            set.connect(markId, ConstraintSet.END, stepViewId, ConstraintSet.START);

            if (i < steps.size() - 1) {

                Integer separatorId = getChildAt(currentSeparatorIndex).getId();
                Integer previousMarkId = getChildAt(currentMarkIndex).getId();
                Integer nextMarkId = getChildAt(currentMarkIndex + 3).getId();

                set.connect(separatorId, ConstraintSet.START, previousMarkId, ConstraintSet.END);
                set.connect(separatorId, ConstraintSet.END, nextMarkId, ConstraintSet.START);
                set.connect(separatorId, ConstraintSet.BOTTOM, previousMarkId, ConstraintSet.BOTTOM);
            }
        }

        set.applyTo(this);
    }

    private void generateVerticalConstraints(ArrayList<Step> steps) {

        ConstraintSet set = new ConstraintSet();
        set.clone(this);

        for (int i = 0; i < steps.size(); i++) {

            int currentStepViewIndex = i * 3;
            int currentMarkIndex = i * 3 + 1;
            int currentSeparatorIndex = i * 3 + 2;

            Integer stepViewId = getChildAt(currentStepViewIndex).getId();

            set.connect(stepViewId, ConstraintSet.START, getId(), ConstraintSet.START);

            if (i == 0) {

                set.connect(stepViewId, ConstraintSet.TOP, getId(), ConstraintSet.TOP);
                set.connect(stepViewId, ConstraintSet.BOTTOM, getChildAt(currentStepViewIndex + 3).getId(), ConstraintSet.TOP);
            } else if (i == steps.size() - 1) {

                set.connect(stepViewId, ConstraintSet.BOTTOM, getId(), ConstraintSet.BOTTOM);
                set.connect(stepViewId, ConstraintSet.TOP, getChildAt(currentStepViewIndex - 3).getId(),
                        ConstraintSet.BOTTOM);
            } else {

                Integer pId = getChildAt(currentStepViewIndex - 3).getId();
                Integer nId = getChildAt(currentStepViewIndex + 3).getId();

                set.connect(stepViewId, ConstraintSet.TOP, pId, ConstraintSet.BOTTOM);
                set.connect(stepViewId, ConstraintSet.BOTTOM, nId, ConstraintSet.TOP);
            }

            Integer markId = getChildAt(currentMarkIndex).getId();

            set.connect(markId, ConstraintSet.TOP, stepViewId, ConstraintSet.BOTTOM);
            set.connect(markId, ConstraintSet.BOTTOM, stepViewId, ConstraintSet.TOP);

            if (i < steps.size() - 1) {

                Integer separatorId = getChildAt(currentSeparatorIndex).getId();
                Integer previousMarkId = getChildAt(currentMarkIndex).getId();
                Integer nextMarkId = getChildAt(currentMarkIndex + 3).getId();

                set.connect(separatorId, ConstraintSet.TOP, previousMarkId, ConstraintSet.BOTTOM);
                set.connect(separatorId, ConstraintSet.BOTTOM, nextMarkId, ConstraintSet.TOP);
                set.connect(separatorId, ConstraintSet.END, previousMarkId, ConstraintSet.END);
            }
        }

        set.applyTo(this);
    }

    public int getCurrentIndex() {
        return currentIndex;
    }

    public void setCurrentIndex(int currentIndex) {

        if (currentIndex == -1) {

            currentIndex = steps.size() - 1;
        }

        if (currentIndex == steps.size()) {

            currentIndex = 0;
        }

        this.currentIndex = currentIndex;
        loadSteps();
    }

    public void completeStep(int index) {

        steps.get(index).setCompleted(true);
        setCurrentIndex(index + 1);
        loadSteps();
    }
}
