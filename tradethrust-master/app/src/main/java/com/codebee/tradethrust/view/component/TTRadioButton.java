package com.codebee.tradethrust.view.component;

import android.content.Context;
import android.os.Build;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.codebee.tradethrust.R;
import com.codebee.tradethrust.model.form_details.list.Schema;
import com.codebee.tradethrust.model.form_details.list.ShowField;
import com.codebee.tradethrust.model.form_details.list.Value;
import com.codebee.tradethrust.utils.Utils;
import com.codebee.tradethrust.view.interfaces.Dependable;

import java.util.List;

/**
 * Created by csangharsha on 6/6/18.
 */

public class TTRadioButton extends LinearLayout implements Dependable {

    private TextView labelTextView;
    private Context context;
    private RadioGroup rg;

    private float density;
    private Schema schema;
    private ViewGroup parentView;

    public TTRadioButton(Context context) {
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
        setRadioBtnGroup();
    }

    private void setRadioBtnGroup() {
        rg = new RadioGroup(context);
        rg.setOrientation(RadioGroup.VERTICAL);

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        lp.bottomMargin = (int) (5 * density);
        rg.setLayoutParams(lp);
        rg.setPadding((int) (15 * density), (int) (5 * density), (int) (5 * density), (int) (5 * density));

        this.addView(rg);

    }

    public void onDependentItemSelect(Value value) {
        manageDependentView();
        if(value.getShowFields() != null) {
            for (ShowField componentName : value.getShowFields()) {
                parentView.findViewWithTag(componentName.getName()).setVisibility(View.VISIBLE);
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

    public void setSchema(Schema schema, ViewGroup parentView) {
        this.parentView = parentView;
        setSchema(schema);
    }

    public void setSchema(final Schema schema) {
        List<Value> values = schema.getValues();

        this.schema = schema;

        if(values != null && values.size() != 0) {
            labelTextView.setVisibility(View.VISIBLE);
            labelTextView.setText(schema.getLabel());
            for(final Value value: values) {
                RadioButton radioButton = new RadioButton(context);
                LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                radioButton.setLayoutParams(params);
                radioButton.setText(value.getLabel());
                radioButton.setTag(value.getValue());
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1) {

                    radioButton.setId(Utils.generateViewId());

                } else {

                    radioButton.setId(View.generateViewId());

                }
//                radioButton.setSelected((value.getSelected() != null && value.getSelected()));
                if((value.getSelected() != null && value.getSelected())) {
                    rg.check(radioButton.getId());
                    radioButton.setChecked(true);
                }

                radioButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if(isChecked && schema.getDependable()) {
                            onDependentItemSelect(value);
                        }
                    }
                });
                rg.addView(radioButton);
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


    public LinearLayout getRadioButtonGroup(){
        return rg;
    }

    public String getSelectedValue(){
        RadioButton radioButton = rg.findViewById(rg.getCheckedRadioButtonId());
        if(radioButton != null) {
            return (String) radioButton.getTag();
        }
        return "";
    }

    public void setChecked(String value) {
        for(int i=0; i < rg.getChildCount(); i++) {
            if(rg.getChildAt(i) instanceof RadioButton) {
                RadioButton radioBtn = (RadioButton) rg.getChildAt(i);
                if(radioBtn.getTag().equals(value)) {
                    rg.check(radioBtn.getId());
                    radioBtn.setChecked(true);
                }
            }
        }
    }

    @Override
    public String getSelectedValues() {
        return null;
    }
}
