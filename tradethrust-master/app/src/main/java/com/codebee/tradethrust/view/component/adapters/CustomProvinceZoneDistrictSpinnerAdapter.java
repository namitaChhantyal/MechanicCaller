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

import java.util.List;

public class CustomProvinceZoneDistrictSpinnerAdapter extends ArrayAdapter {
    private final LayoutInflater mInflater;
    private final Context mContext;
    private List<String> items;
    private final int mResource;

    public CustomProvinceZoneDistrictSpinnerAdapter(@NonNull Context context, @LayoutRes int resource,
                                      @NonNull List objects) {
        super(context, resource, 0, objects);

        mContext = context;
        mInflater = LayoutInflater.from(context);
        mResource = resource;
        items = objects;
    }

    public CustomProvinceZoneDistrictSpinnerAdapter(@NonNull Context context, @LayoutRes int resource) {
        super(context, resource, 0);

        mContext = context;
        mInflater = LayoutInflater.from(context);
        mResource = resource;
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

    private View createItemView(int position, View convertView, ViewGroup parent) {
        final View view = mInflater.inflate(mResource, parent, false);
        TextView itemTextView = view.findViewById(R.id.item_text_view);

        String value = items.get(position);
        itemTextView.setText(value);

        return view;
    }
}
