package com.codebee.tradethrust.view.component;

import android.content.Context;
import android.view.ViewGroup;

import com.codebee.tradethrust.model.form_details.list.Schema;
import com.codebee.tradethrust.model.form_details.list.Value;
import com.codebee.tradethrust.utils.ProvinceMapping;
import com.codebee.tradethrust.view.interfaces.OnDependentItemSelectedListener;

import java.util.ArrayList;


/**
 * Created by csangharsha on 6/18/18.
 */

public class ZoneComponent implements OnDependentItemSelectedListener {

    private Context context;
    private Schema schema;
    private ViewGroup parentView;
    private int componentId;
    private String value;
    private ThrustSpinner zoneSpinner;
    private Value selectedValue;
    private DistrictComponent districtComponent;
    private Value selectedProvince;

    public ZoneComponent(Context context, Schema schema, ViewGroup parentView, int componentId, String value) {
        this.context = context;
        this.schema = schema;
        this.parentView = parentView;
        this.componentId = componentId;
        this.value = value;

        initComponent();
    }

    private void initComponent() {
        createZoneSelectTypeView();
    }

    private void createZoneSelectTypeView() {

        if (value == null) {
            zoneSpinner = new ThrustSpinner(context);
        } else {
            zoneSpinner = new ThrustSpinner(context, value);
        }

        zoneSpinner.setOnDependentItemSelectedListener(this);

        zoneSpinner.setId(componentId);
        zoneSpinner.setTag(schema.getName());

        parentView.addView(zoneSpinner);
    }

    public void setDependentDistrict(DistrictComponent districtComponent) {
        this.districtComponent = districtComponent;
    }

    public void initializeDistrictComponent() {
        ArrayList<String> districtList = new ArrayList<>(ProvinceMapping.zoneMap.get(ProvinceMapping.getDistrictKey("Province No. 1", "Koshi")));
        ArrayList<Value> valueList = new ArrayList<>();
        for(String district: districtList) {
            Value v = new Value();
            v.setLabel(district);
            v.setValue(district);
            valueList.add(v);
        }

        if(districtComponent != null) {
            Schema schema = districtComponent.getSchema();
            schema.setValues(valueList);
            districtComponent.getSpinner().setSchema(schema);
        }
    }


    @Override
    public void onDependentItemSelect(Value value) {
        selectedValue = value;
        String selectedZone = value.getValue();

        String selectedProvince;
        if(this.selectedProvince != null) {
            selectedProvince = this.selectedProvince.getValue();
        } else {
            selectedProvince = "Province No. 1";
        }

        ArrayList<String> districtList = new ArrayList<>(ProvinceMapping.zoneMap.get(ProvinceMapping.getDistrictKey(selectedProvince, selectedZone)));
        ArrayList<Value> valueList = new ArrayList<>();
        for(String district: districtList) {
            Value v = new Value();
            v.setLabel(district);
            v.setValue(district);
            valueList.add(v);
        }

        if(districtComponent != null) {
            Schema schema = districtComponent.getSchema();
            schema.setValues(valueList);
            districtComponent.getSpinner().setSchema(schema);
        }
    }

    public Value getSelectedValue(){
        return selectedValue;
    }

    public ThrustSpinner getSpinner() {
        return zoneSpinner;
    }

    public Schema getSchema() {
        return schema;
    }

    public void setProvince(Value value) {
        selectedProvince = value;
    }

}