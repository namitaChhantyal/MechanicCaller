package com.codebee.tradethrust.view.component.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import com.codebee.tradethrust.R;
import com.codebee.tradethrust.model.form_details.list.Value;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by csangharsha on 6/7/18.
 */

public class CustomAutoCompleteTextViewAdapter extends ArrayAdapter<Value> {

    private List<Value> valueList;
    private Context mContext;
    private int itemLayout;
    private List<Value> allValueList;

    public CustomAutoCompleteTextViewAdapter(Context context, int resource, List<Value> storeDataLst) {
        super(context, resource, storeDataLst);
        valueList = storeDataLst;
        mContext = context;
        itemLayout = resource;
        allValueList = new ArrayList<>(storeDataLst);
    }

    @Override
    public int getCount() {
        return valueList.size();
    }

    @Override
    public Value getItem(int position) {
        return valueList.get(position);
    }

    @Override
    public View getView(int position, View view, @NonNull ViewGroup parent) {

        if (view == null) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(itemLayout, parent, false);
        }

        TextView strName = view.findViewById(R.id.textView);
        strName.setText(getItem(position).getLabel());
        return view;
    }

}
