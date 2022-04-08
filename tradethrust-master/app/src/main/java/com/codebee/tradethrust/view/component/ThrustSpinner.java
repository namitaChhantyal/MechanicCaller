package com.codebee.tradethrust.view.component;

import android.content.Context;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.codebee.tradethrust.R;
import com.codebee.tradethrust.model.form_details.list.Schema;
import com.codebee.tradethrust.model.form_details.list.ShowField;
import com.codebee.tradethrust.model.form_details.list.Value;
import com.codebee.tradethrust.view.component.adapters.CustomThrustSpinnerAdapter;
import com.codebee.tradethrust.view.interfaces.Dependable;
import com.codebee.tradethrust.view.interfaces.OnDependentItemSelectedListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by csangharsha on 6/3/18.
 */
public class ThrustSpinner extends LinearLayout implements Dependable {

    private TextView labelTextView;
    private Spinner spinner;
    private Context context;
    private CustomThrustSpinnerAdapter adapter;
    private ViewGroup parentView;

    private List<Value> valueList;

    private String selectedValue = null;

    private OnDependentItemSelectedListener onDependentItemSelectedListener;
    private Schema schema;
    private View view;

    private float density;

    public ThrustSpinner(Context context) {
        super(context);
        initLayout(context);
    }

    public ThrustSpinner(Context context, String value) {
        super(context);

        selectedValue = value;

        initLayout(context);
    }

    public void setOnDependentItemSelectedListener(OnDependentItemSelectedListener onDependentItemSelectedListener) {
        this.onDependentItemSelectedListener = onDependentItemSelectedListener;
    }

    private void initLayout(Context context) {
        this.context = context;

        density = context.getResources().getDisplayMetrics().density;

        LayoutParams mainParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mainParams.bottomMargin = (int) (16 * context.getResources().getDisplayMetrics().density);
        setLayoutParams(mainParams);
        setOrientation(LinearLayout.VERTICAL);

        setLabelTextView();
        setSpinner();
        createBottomView();
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

    private void setSpinner() {
        spinner = new Spinner(context);
        LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        spinner.setLayoutParams(params);

        valueList = new ArrayList<>();

        if(selectedValue == null) {
            adapter = new CustomThrustSpinnerAdapter(context,
                    R.layout.thrust_trade__item_layout, valueList);
        }else {
            adapter = new CustomThrustSpinnerAdapter(context,
                    R.layout.thrust_trade__item_layout, valueList, selectedValue);
        }
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(onDependentItemSelectedListener != null) {
                    onDependentItemSelectedListener.onDependentItemSelect(valueList.get(i));
                } else {
                    onDependentItemSelect(valueList.get(i));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        this.addView(spinner);
    }

    public void onDependentItemSelect(Value value) {
        manageDependentView();
        if(value.getShowFields() != null) {
            for (ShowField componentName : value.getShowFields()) {
                if(parentView.findViewWithTag(componentName.getName()) != null) {
                    parentView.findViewWithTag(componentName.getName()).setVisibility(View.VISIBLE);
                }
            }
        }
    }

    public void manageDependentView() {
        if(schema != null) {
            for (Value value : schema.getValues()) {
                if (value.getShowFields() != null) {
                    for (ShowField componentName : value.getShowFields()) {
                        if(parentView.findViewWithTag(componentName.getName()) != null) {
                            parentView.findViewWithTag(componentName.getName()).setVisibility(View.GONE);
                        }
                    }
                }
            }
        }

    }

    private void createBottomView(){
        view = new View(context);
        view.setBackgroundColor(context.getResources().getColor(R.color.view_color));
        LinearLayout.LayoutParams viewLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, (int) context.getResources().getDimension(R.dimen.view_size));
        viewLayoutParams.topMargin = (int) (2 * density);
        view.setLayoutParams(viewLayoutParams);

        this.addView(view);
    }

    public void setSchema(Schema schema, ViewGroup parentView) {
        this.parentView = parentView;
        setSchema(schema);
    }

    public void setSchema(Schema schema) {
        this.schema = schema;
        labelTextView.setHint(schema.getLabel());
        labelTextView.setVisibility(View.VISIBLE);

        if(schema.getValues() != null) {
            this.valueList.clear();
            this.valueList.addAll(schema.getValues());
            adapter.notifyDataSetChanged();
            for (Value value : schema.getValues()) {
                if (value.getValue().equals(selectedValue)) {
                    spinner.setSelection(schema.getValues().indexOf(value));
                }
            }
        }
    }

    public void setId(int id){
        spinner.setId(id);
    }

    public void setTag(String tag) {
        spinner.setTag(tag);
        spinner.setContentDescription(tag);
        labelTextView.setContentDescription(tag);
        view.setContentDescription(tag);

    }

    @Override
    public String getSelectedValues() {
        return ((Value)spinner.getSelectedItem()).getValue();
    }
}
