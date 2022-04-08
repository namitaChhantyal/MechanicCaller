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
import com.codebee.tradethrust.model.form_details.list.Value;
import com.codebee.tradethrust.view.component.adapters.CustomThrustSpinnerAdapter;
import com.codebee.tradethrust.view.component.multiselect.MultiSpinner;

import java.util.ArrayList;
import java.util.List;

public class TTMultiSelect extends LinearLayout implements MultiSpinner.MultiSpinnerListener {

    private TextView labelTextView;
    private MultiSpinner spinner;
    private Context context;
    private ViewGroup parentView;

    private List<Value> valueList;

    private String selectedValue = null;

    private Schema schema;

    private float density;
    private CustomThrustSpinnerAdapter adapter;

    public TTMultiSelect(Context context) {
        super(context);
        initLayout(context);
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
        spinner = new MultiSpinner(context);
        LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        spinner.setLayoutParams(params);

        valueList = new ArrayList<>();

        adapter = new CustomThrustSpinnerAdapter(context,
                R.layout.thrust_trade__item_layout, valueList);
        spinner.setAdapter(adapter, false, this);

        this.addView(spinner);
    }

    private void createBottomView(){
        View view = new View(context);
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
        }
    }

    public void setId(int id){
        spinner.setId(id);
    }

    public void setTag(String tag) {
        spinner.setTag(tag);
    }


    @Override
    public void onItemsSelected(boolean[] selected) {

    }
}
