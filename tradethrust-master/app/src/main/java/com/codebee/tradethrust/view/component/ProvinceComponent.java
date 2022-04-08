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

public class ProvinceComponent implements OnDependentItemSelectedListener {

    private Context context;
    private Schema schema;
    private ViewGroup parentView;
    private int componentId;
    private String value;
    private ThrustSpinner provinceSpinner;
    private ZoneComponent zoneComponent;
    private Value selectedValue;

    public ProvinceComponent(Context context, Schema schema, ViewGroup parentView, int componentId, String value) {
        this.context = context;
        this.schema = schema;
        this.parentView = parentView;
        this.componentId = componentId;
        this.value = value;

        initComponent();
    }

    public void setDependentZone(ZoneComponent zoneComponent) {
        this.zoneComponent = zoneComponent;
    }

    private void initComponent() {
        createProvinceSelectTypeView();
    }

    private void createProvinceSelectTypeView() {

        if(value == null) {
            provinceSpinner = new ThrustSpinner(context);
        }else {
            provinceSpinner = new ThrustSpinner(context, value);
        }

        provinceSpinner.setOnDependentItemSelectedListener(this);

        ArrayList<Value> values = new ArrayList<>();
        for(String province: ProvinceMapping.provinceMap.keySet()) {
            Value  v = new Value();
            v.setValue(province);
            v.setLabel(province);
            values.add(v);
        }
        schema.setValues(values);

        provinceSpinner.setSchema(schema);
        provinceSpinner.setId(componentId);
        provinceSpinner.setTag(schema.getName());

        parentView.addView(provinceSpinner);
    }

    @Override
    public void onDependentItemSelect(Value value) {

        selectedValue = value;

        String selectedProvince = value.getValue();

        ArrayList<String> zoneList = new ArrayList<>(ProvinceMapping.provinceMap.get(selectedProvince));
        ArrayList<Value> valueList = new ArrayList<>();
        for(String zone: zoneList) {
            Value v = new Value();
            v.setLabel(zone);
            v.setValue(zone);
            valueList.add(v);
        }

        if(zoneComponent != null) {
            zoneComponent.setProvince(value);
            Schema schema = zoneComponent.getSchema();
            schema.setValues(valueList);
            zoneComponent.getSpinner().setSchema(schema);

        }
    }

    public Value getSelectedValue(){
        return selectedValue;
    }

    public void initializeDistrictComponent() {
        ArrayList<String> districtList = new ArrayList<>(ProvinceMapping.provinceMap.get("Province No. 1"));
        ArrayList<Value> valueList = new ArrayList<>();
        for(String district: districtList) {
            Value v = new Value();
            v.setLabel(district);
            v.setValue(district);
            valueList.add(v);
        }

        if(zoneComponent != null) {
            Schema schema = zoneComponent.getSchema();
            schema.setValues(valueList);
            zoneComponent.getSpinner().setSchema(schema);
        }
    }
}
