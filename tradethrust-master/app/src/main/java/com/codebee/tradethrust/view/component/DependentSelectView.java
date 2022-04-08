package com.codebee.tradethrust.view.component;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.codebee.tradethrust.model.form_details.list.Schema;
import com.codebee.tradethrust.model.form_details.list.ShowField;
import com.codebee.tradethrust.model.form_details.list.Value;
import com.codebee.tradethrust.view.interfaces.OnDependentItemSelectedListener;

/**
 * Created by csangharsha on 6/17/18.
 */

public class DependentSelectView implements OnDependentItemSelectedListener{

    private Context context;
    private Schema schema;
    private ViewGroup parentView;
    private int componentId;
    private String value;
    private ThrustSpinner spinner;
    private Value selectedValue;

    public DependentSelectView(Context context, Schema schema, ViewGroup parentView, int componentId, String value) {
        this.context = context;
        this.schema = schema;
        this.parentView = parentView;
        this.componentId = componentId;
        this.value = value;

        initComponent();
    }

    private void initComponent() {
        createSelectTypeView();
    }

    private void createSelectTypeView() {

        if(value == null) {
            spinner = new ThrustSpinner(context);
        }else {
            spinner = new ThrustSpinner(context, value);
        }

        spinner.setOnDependentItemSelectedListener(this);

        spinner.setSchema(schema);
        spinner.setId(componentId);
        spinner.setTag(schema.getName());

        parentView.addView(spinner);
    }

    @Override
    public void onDependentItemSelect(Value value) {
        manageDependentView();
        if(value.getShowFields() != null) {
            for (ShowField componentName : value.getShowFields()) {
                parentView.findViewWithTag(componentName.getName()).setVisibility(View.VISIBLE);
            }
        }

        selectedValue = value;
    }

    public void manageDependentView() {
        for (Value value : schema.getValues()) {
            if(value.getShowFields() != null) {
                for (ShowField componentName : value.getShowFields()) {
                    parentView.findViewWithTag(componentName.getName()).setVisibility(View.GONE);
                }
            }
        }

    }

    public Value getSelectedValue(){
        return selectedValue;
    }

}
