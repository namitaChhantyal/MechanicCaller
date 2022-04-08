package com.codebee.tradethrust.view.component.spinner;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.codebee.tradethrust.R;
import com.codebee.tradethrust.model.province.ListHolder;

import java.util.ArrayList;
import java.util.List;

public class FilterableSpinner extends LinearLayout implements FilterableSpinnerDropdownDialog.OnProvinceItemSelectedListener {

    public static final String FRAGMENT_DROPDOWN_TAG = "fragment_dropdown_filterable_spinner";
    private Context context;
    private float mDensity;
    private TextView spinnerTextView;
    private ArrayList<ListHolder> list;
    private OnItemSelectedListener onItemSelectedListener;
    private ListHolder selectedItem;

    public FilterableSpinner(Context context) {
        super(context);

        init(context);
    }

    private void init(Context context) {
        this.context = context;

        this.mDensity = context.getResources().getDisplayMetrics().density;

        list = new ArrayList<>();

        setUpSpinner();

        this.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                openDropdown();
            }

        });

    }

    private void setUpSpinner() {
        LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.bottomMargin = 20;
        setLayoutParams(params);
        setOrientation(VERTICAL);

        setUpTextView();

        setUpUnderLineView();
    }

    private void setUpTextView() {
        spinnerTextView = new TextView(context);

        spinnerTextView.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        spinnerTextView.setPadding((int)(5 * mDensity), (int)(5 * mDensity), (int)(5 * mDensity), (int)(5 * mDensity));
        spinnerTextView.setGravity(Gravity.CENTER_VERTICAL);
        spinnerTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, 20 * mDensity);
        spinnerTextView.setTextColor(Color.parseColor("#333333"));
        spinnerTextView.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_dropdown, 0);

        this.addView(spinnerTextView);
    }

    private void setUpUnderLineView() {
        View view = new View(context);
        view.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) (2 * mDensity)));
        view.setBackgroundColor(Color.parseColor("#CCCCCC"));

        this.addView(view);
    }

    private void openDropdown() {
        FragmentManager manager = ((AppCompatActivity) context).getSupportFragmentManager();
        Fragment frag = manager.findFragmentByTag(FRAGMENT_DROPDOWN_TAG);
        if (frag != null) {
            manager.beginTransaction().remove(frag).commit();
        }

        FilterableSpinnerDropdownDialog filterableSpinnerDropdownDialog = new FilterableSpinnerDropdownDialog();
        Bundle bundle = new Bundle();
        bundle.putSerializable(FilterableSpinnerDropdownDialog._LIST_KEY, list);
        filterableSpinnerDropdownDialog.setOnProvinceItemSelectedListener(this);
        filterableSpinnerDropdownDialog.setArguments(bundle);
        filterableSpinnerDropdownDialog.show(manager, FRAGMENT_DROPDOWN_TAG);

    }

    public void setTextColor(int color) {
        spinnerTextView.setTextColor(color);
    }

    public void setTextSize(float size) {
        spinnerTextView.setTextSize(size);
    }

    public void setTextSize(int unit, float size) {
        spinnerTextView.setTextSize(unit, size);
    }


    public void setList(List<ListHolder> listHolders) {
        spinnerTextView.setText("");
        list.clear();
        list.addAll(listHolders);
    }

    public int getSelectedItemPosition(){
        return list.indexOf(selectedItem);
    }

    public ListHolder getSelectedItem() {
        return selectedItem;
    }

    @Override
    public void onProvinceItemSelected(ListHolder listHolder) {
        this.selectedItem = listHolder;
        spinnerTextView.setText(listHolder.getLabel());
        if(onItemSelectedListener != null) {
            onItemSelectedListener.onItemSelected(listHolder);
        }
    }

    public void setOnItemSelectedListener(OnItemSelectedListener onItemSelectedListener) {
        this.onItemSelectedListener = onItemSelectedListener;
    }

    public interface OnItemSelectedListener {
        void onItemSelected(ListHolder holder);
    }
}
