package com.codebee.tradethrust.view.component.adapters;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.codebee.tradethrust.R;
import com.codebee.tradethrust.model.form_details.list.Value;

import java.util.List;

/**
 * Created by csangharsha on 6/3/18.
 */

public class CustomThrustSpinnerAdapter extends ArrayAdapter {
    private final LayoutInflater mInflater;
    private final Context mContext;
    private final List<Value> items;
    private final int mResource;

    private Value selectedValue;
    private String selectedValueEditMode;

    public CustomThrustSpinnerAdapter(@NonNull Context context, @LayoutRes int resource,
                              @NonNull List objects) {
        super(context, resource, 0, objects);

        mContext = context;
        mInflater = LayoutInflater.from(context);
        mResource = resource;
        items = objects;
    }

    public CustomThrustSpinnerAdapter(Context context, int resource, List<Value> objects, String selectedValue) {
        super(context, resource, objects);

        mContext = context;
        mInflater = LayoutInflater.from(context);
        mResource = resource;
        items = objects;
        selectedValueEditMode = selectedValue;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView,
                                @NonNull ViewGroup parent) {
        return createItemView(position, convertView, parent);
    }

    @Override
    public @NonNull View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return createItemView(position, convertView, parent);
    }

    private View createItemView(int position, View convertView, ViewGroup parent){
        final View view = mInflater.inflate(mResource, parent, false);
        TextView itemTextView = view.findViewById(R.id.item_text_view);

        Value value = items.get(position);
        itemTextView.setText(value.getLabel());
        if( (selectedValueEditMode != null && value.getValue().equals(selectedValueEditMode)) ) {
            selectedValue = value;
        } else if(selectedValueEditMode == null && value.getSelected() != null && value.getSelected()){
            selectedValue = value;
        }

        return view;
    }

    public Value getSelectedValue() {
        return selectedValue;
    }

    public void setSelectedValue(Value selectedValue) {
        this.selectedValue = selectedValue;
    }
}
