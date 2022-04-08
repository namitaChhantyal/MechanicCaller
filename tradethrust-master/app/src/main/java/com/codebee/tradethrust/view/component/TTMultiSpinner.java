package com.codebee.tradethrust.view.component;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
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

public class TTMultiSpinner extends LinearLayout {

    private boolean isRemoveNeeded = false;
    private TextView labelTextView;
    private Spinner spinner;
    private Context context;
    private CustomThrustSpinnerAdapter adapter;
    private ViewGroup parentView;

    private List<Value> valueList;

    private String selectedValue = null;
    private LinearLayout layout;
    private Button addButton;
    private Button removeButton;

    private OnDependentItemSelectedListener onDependentItemSelectedListener;
    private Schema schema;
    private TTMultiSpinner previousMultiSpinner;
    private LinearLayout buttonLayout;

    private float density;

    public TTMultiSpinner(Context context) {
        super(context);
        initLayout(context);
    }

    public TTMultiSpinner(Context context, boolean isRemoveNeeded) {
        super(context);
        this.isRemoveNeeded = isRemoveNeeded;
        initLayout(context);
    }

    public TTMultiSpinner(Context context, boolean isRemoveNeeded, TTMultiSpinner previousMultiSpinner) {
        super(context);
        this.isRemoveNeeded = isRemoveNeeded;
        this.previousMultiSpinner = previousMultiSpinner;
        initLayout(context);
    }

    public TTMultiSpinner(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
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
        mainParams.gravity = Gravity.CENTER_VERTICAL;
        setLayoutParams(mainParams);
        setOrientation(LinearLayout.HORIZONTAL);
        setGravity(Gravity.CENTER_VERTICAL);

        layout = new LinearLayout(context);
        LayoutParams layoutParams = new LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1);

        layout.setLayoutParams(layoutParams);
        layout.setOrientation(VERTICAL);

        addView(layout);

        setLabelTextView();
        setSpinner();
        createBottomView();

        buttonLayout = new LinearLayout(context);
        layoutParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);

        buttonLayout.setLayoutParams(layoutParams);
        buttonLayout.setOrientation(VERTICAL);

        addView(buttonLayout);

        createAddButton();

        if(isRemoveNeeded) {
            createRemoveButton();
        }
    }

    private void createAddButton() {
        addButton = new Button(context);
        LayoutParams layoutParams = new LayoutParams((int) (24 * density), (int) (24 * density));
        layoutParams.gravity = Gravity.CENTER;
        layoutParams.topMargin = (int) (2 * density);
        layoutParams.leftMargin = (int) (2 * density);
        addButton.setLayoutParams(layoutParams);
        addButton.setGravity(Gravity.CENTER_VERTICAL);
        addButton.setBackground(context.getResources().getDrawable(R.drawable.ic_add_box));
        addButton.setVisibility(View.GONE);

        addButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                Value value = (Value) spinner.getSelectedItem();

                schema.getValues().remove(value);

                TTMultiSpinner spinner = new TTMultiSpinner(context, true, TTMultiSpinner.this);
                spinner.setSchema(schema, parentView);
                spinner.setTag(schema.getName());

                // Manage the visibility and disability of the view
                TTMultiSpinner.this.addButton.setVisibility(View.GONE);
                if(TTMultiSpinner.this.removeButton != null) {
                    TTMultiSpinner.this.removeButton.setVisibility(View.GONE);
                }
                TTMultiSpinner.this.getSpinner().setEnabled(false);
                parentView.addView(spinner);

            }
        });

        buttonLayout.addView(addButton);
    }

    private void createRemoveButton() {
        removeButton = new Button(context);
        LayoutParams layoutParams = new LayoutParams((int) (24 * density), (int) (24 * density));
        layoutParams.gravity = Gravity.CENTER;
        layoutParams.topMargin = (int) (2 * density);
        layoutParams.leftMargin = (int) (2 * density);
        removeButton.setLayoutParams(layoutParams);
        removeButton.setGravity(Gravity.CENTER_VERTICAL);
        removeButton.setBackground(context.getResources().getDrawable(R.drawable.ic_minus_box));

        removeButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                // Manage the visibility and disability of the view
                if(previousMultiSpinner != null && previousMultiSpinner.getAddButton() != null ) {
                    previousMultiSpinner.getAddButton().setVisibility(View.VISIBLE);
                    if(previousMultiSpinner.getRemoveButton() != null) {
                        previousMultiSpinner.getRemoveButton().setVisibility(VISIBLE);
                    }
                    previousMultiSpinner.getSpinner().setEnabled(true);
                    schema.getValues().add(previousMultiSpinner.getSelectedItem());
                }
                parentView.removeView(TTMultiSpinner.this);

            }
        });

        buttonLayout.addView(removeButton);
    }

    public Button getAddButton() {
        return addButton;
    }

    public Button getRemoveButton() {
        return removeButton;
    }

    public Spinner getSpinner() {
        return spinner;
    }

    private void setLabelTextView() {
        labelTextView = new TextView(context);
        LinearLayout.LayoutParams fileNameTextViewLayoutParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        fileNameTextViewLayoutParams.gravity = Gravity.CENTER_VERTICAL;
        labelTextView.setLayoutParams(fileNameTextViewLayoutParams);
        labelTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, context.getResources().getDimension(R.dimen.component_label_size));
        labelTextView.setVisibility(View.GONE);
        layout.addView(labelTextView);
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

        layout.addView(spinner);
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
        View view = new View(context);
        view.setBackgroundColor(context.getResources().getColor(R.color.view_color));
        LinearLayout.LayoutParams viewLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, (int) context.getResources().getDimension(R.dimen.view_size));
        viewLayoutParams.topMargin = (int) (2 * density);
        view.setLayoutParams(viewLayoutParams);

        layout.addView(view);
    }

    public void setSchema(Schema schema, ViewGroup parentView) {
        this.parentView = parentView;
        setSchema(schema);
    }

    public void setSchema(Schema schema) {
        this.schema = schema;
        labelTextView.setHint(schema.getLabel());
        labelTextView.setVisibility(View.VISIBLE);
        if(schema.getValues() != null && schema.getValues().size() > 1) {
            addButton.setVisibility(View.VISIBLE);
        }

        if(isRemoveNeeded && removeButton != null) {
            removeButton.setVisibility(View.VISIBLE);
        }
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
    }

    private Value getSelectedItem(){
        return (Value)spinner.getSelectedItem();
    }

    public List<String> getAllSelectedValues(){
        List<String> selectedValues = new ArrayList<>();
        for(int i=0;i<this.getChildCount(); i++) {
            View view = this.getChildAt(i);
            if(view instanceof Spinner) {
                Spinner spinner = (Spinner) view;
                selectedValues.add( ((Value) spinner.getSelectedItem()).getValue());
            }
        }
        return selectedValues;
    }

}
