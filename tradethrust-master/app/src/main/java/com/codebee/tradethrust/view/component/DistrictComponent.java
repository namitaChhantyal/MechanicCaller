package com.codebee.tradethrust.view.component;

import android.content.Context;
import android.view.ViewGroup;

import com.codebee.tradethrust.model.form_details.list.Schema;
import com.codebee.tradethrust.model.form_details.list.Value;
import com.codebee.tradethrust.view.interfaces.OnDependentItemSelectedListener;

class DistrictComponent implements OnDependentItemSelectedListener {

    private Context context;
    private Schema schema;
    private ViewGroup parentView;
    private int componentId;
    private String value;
    private ThrustSpinner districtSpinner;
    private Value selectedValue;
    private ZoneComponent zoneComponent;

    public DistrictComponent(Context context, Schema schema, ViewGroup parentView, int componentId, String value) {
        this.context = context;
        this.schema = schema;
        this.parentView = parentView;
        this.componentId = componentId;
        this.value = value;

        initComponent();
    }

    private void initComponent() {
        createDistrictSelectTypeView();
    }

    private void createDistrictSelectTypeView() {

        if (value == null) {
            districtSpinner = new ThrustSpinner(context);
        } else {
            districtSpinner = new ThrustSpinner(context, value);
        }

        districtSpinner.setOnDependentItemSelectedListener(this);

        districtSpinner.setId(componentId);
        districtSpinner.setTag(schema.getName());

        parentView.addView(districtSpinner);
    }

    @Override
    public void onDependentItemSelect(Value value) {
        selectedValue = value;
    }

    public Value getSelectedValue(){
        return selectedValue;
    }

    public ThrustSpinner getSpinner() {
        return districtSpinner;
    }

    public Schema getSchema() {
        return schema;
    }
}