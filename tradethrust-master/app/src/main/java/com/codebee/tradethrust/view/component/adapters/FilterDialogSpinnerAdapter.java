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
import com.codebee.tradethrust.model.pos_filter.Data;
import com.codebee.tradethrust.model.pos_filter.FilterData;
import com.codebee.tradethrust.utils.ThrustConstant;

import java.util.List;

public class FilterDialogSpinnerAdapter extends ArrayAdapter {

    private final LayoutInflater mInflater;
    private final Context mContext;
    private final List<Data> items;
    private final int mResource;
    private int filterCategoryType;

    private Value selectedValue;


    public FilterDialogSpinnerAdapter(@NonNull Context context, @LayoutRes int resource,
                                      @NonNull List objects, int filterCategoryType) {
        super(context, resource, 0, objects);

        mContext = context;
        mInflater = LayoutInflater.from(context);
        mResource = resource;
        items = objects;
        this.filterCategoryType = filterCategoryType;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView,
                                @NonNull ViewGroup parent) {
        return createItemView(position, convertView, parent);
    }

    @Override
    public @NonNull View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View spView = mInflater.inflate(R.layout.filter_dialog_spinner_item_row, parent, false);
        TextView itemTextView = spView.findViewById(R.id.filter_text_word);

        Data value = items.get(position);
        if(filterCategoryType== ThrustConstant.FITLER_CATEGORY_TYPE_BIT) {
            itemTextView.setText(value.getBitName());
        }else if (filterCategoryType == ThrustConstant.FITLER_CATEGORY_TYPE_POS) {
            itemTextView.setText(value.getPosName());
        }
        return spView;
    }

    private View createItemView(int position, View convertView, ViewGroup parent){
        final View view = mInflater.inflate(R.layout.filter_dialog_spinner_item, parent, false);
        TextView itemTextView = view.findViewById(R.id.filter_text_word);

        Data value = items.get(position);
        if(filterCategoryType== ThrustConstant.FITLER_CATEGORY_TYPE_BIT) {
            itemTextView.setText(value.getBitName());
        }else if (filterCategoryType == ThrustConstant.FITLER_CATEGORY_TYPE_POS) {
            itemTextView.setText(value.getPosName());
        }

        return view;
    }

}
