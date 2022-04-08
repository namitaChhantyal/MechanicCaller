package com.codebee.tradethrust.view.component;

import android.content.Context;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.codebee.tradethrust.R;
import com.codebee.tradethrust.model.form_details.list.Schema;
import com.codebee.tradethrust.model.form_details.list.ShowField;
import com.codebee.tradethrust.model.form_details.list.Value;
import com.codebee.tradethrust.view.component.adapters.CustomThrustSpinnerAdapter;
import com.codebee.tradethrust.view.interfaces.Dependable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by csangharsha on 6/6/18.
 */

public class TTCheckBox extends LinearLayout implements Dependable {

    private TextView labelTextView;
    private Context context;
    private LinearLayout checkBoxContainer;
    private Schema schema;

    private float density;
    private ViewGroup parentView;

    public TTCheckBox(Context context) {
        super(context);
        initLayout(context);
    }

    private void initLayout(Context context) {
        this.context = context;

        density = context.getResources().getDisplayMetrics().density;

        LinearLayout.LayoutParams mainParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mainParams.bottomMargin = (int) (16 * density);
        setLayoutParams(mainParams);
        setOrientation(LinearLayout.VERTICAL);

        setLabelTextView();
        setCheckBoxLayout();
    }

    private void setCheckBoxLayout() {
        checkBoxContainer = new LinearLayout(context);

        LinearLayout.LayoutParams mainParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mainParams.bottomMargin = (int) (5 * density);
        checkBoxContainer.setLayoutParams(mainParams);
        checkBoxContainer.setOrientation(LinearLayout.VERTICAL);
        checkBoxContainer.setPadding((int) (15 * density), (int) (5 * density), (int) (5 * density), (int) (5 * density));
        this.addView(checkBoxContainer);
    }

    public void setSchema(Schema schema, ViewGroup parentView) {
        this.parentView = parentView;
        setSchema(schema);
    }

    public void setSchema(final Schema schema) {
        this.schema = schema;
        List<Value> values = schema.getValues();

        if(values != null && values.size() != 0) {
            labelTextView.setVisibility(View.VISIBLE);
            labelTextView.setText(schema.getLabel());
            for(final Value value: values) {
                CheckBox checkBox = new CheckBox(context);
                LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                checkBox.setLayoutParams(params);
                checkBox.setText(value.getLabel());
                checkBox.setTag(value.getValue());
                checkBox.setChecked((value.getSelected() != null && value.getSelected()));
                checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if(schema.getDependable()) {
                            onDependentItemSelect(value, isChecked);
                        }
                    }
                });
                checkBoxContainer.addView(checkBox);
            }
        }
    }

    public void onDependentItemSelect(Value value, boolean isChecked) {
        if(value.getShowFields() != null) {
            for (ShowField componentName : value.getShowFields()) {
                int visibility;
                if(isChecked) {
                    visibility = View.VISIBLE;
                }else {
                    visibility = View.GONE;
                }
                parentView.findViewWithTag(componentName.getName()).setVisibility(visibility);
            }
        }
    }

    public void manageDependentView() {
        if(schema != null) {
            for (Value value : schema.getValues()) {
                if (value.getShowFields() != null) {
                    for (ShowField componentName : value.getShowFields()) {
                        parentView.findViewWithTag(componentName.getName()).setVisibility(View.GONE);
                    }
                }
            }
        }

    }

    private void setLabelTextView() {
        labelTextView = new TextView(context);
        LinearLayout.LayoutParams fileNameTextViewLayoutParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        fileNameTextViewLayoutParams.gravity = Gravity.CENTER_VERTICAL;
        labelTextView.setLayoutParams(fileNameTextViewLayoutParams);
        labelTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, context.getResources().getDimension(R.dimen.component_label_size));
        labelTextView.setVisibility(View.GONE);
        this.addView(labelTextView);
    }


    public List<String> getCheckedValue() {
        List<String> checkedValue = new ArrayList<>();

        for(int i=0; i < checkBoxContainer.getChildCount(); i++) {
            if(checkBoxContainer.getChildAt(i) instanceof CheckBox) {
                CheckBox checkBox = (CheckBox) checkBoxContainer.getChildAt(i);
                if(checkBox.isChecked()) {
                    checkedValue.add((String) checkBox.getTag());
                }
            }
        }
        return checkedValue;
    }


    public void setChecked(String value) {
        for(int i=0; i < checkBoxContainer.getChildCount(); i++) {
            if(checkBoxContainer.getChildAt(i) instanceof CheckBox) {
                CheckBox checkBox = (CheckBox) checkBoxContainer.getChildAt(i);
                if(checkBox.getTag().equals(value)) {
                    checkBox.setChecked(true);
                }
            }
        }
    }

    @Override
    public String getSelectedValues() {
        return null;
    }
}
