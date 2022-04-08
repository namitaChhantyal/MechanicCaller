package com.codebee.tradethrust.view.component;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.codebee.tradethrust.R;
import com.codebee.tradethrust.application.TradeThrustApplication;
import com.codebee.tradethrust.model.form_details.list.Schema;
import com.codebee.tradethrust.model.form_details.list.Value;
import com.codebee.tradethrust.model.province.ListHolder;
import com.codebee.tradethrust.network.MunicipalityListAPI;
import com.codebee.tradethrust.view.component.spinner.FilterableSpinner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class TTFilterableSpinner extends LinearLayout {

    private Context context;

    private SharedPreferences preferences;
    private Retrofit retrofit;
    private TextView filterableLabelTextView;
    private FilterableSpinner spinner;
    private List<ListHolder> values;

    private float mDensity;

    public TTFilterableSpinner(Context context) {
        super(context);
        initLayout(context);
    }

    private void initLayout(Context context) {
        this.context = context;

        TradeThrustApplication application = (TradeThrustApplication) context.getApplicationContext();
        preferences = application.getPreferences();
        retrofit = application.getRetrofit();

        mDensity = context.getResources().getDisplayMetrics().density;

        LinearLayout.LayoutParams mainParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//        mainParams.bottomMargin = (int) (16 * mDensity);
        setLayoutParams(mainParams);
        setOrientation(LinearLayout.VERTICAL);

        LinearLayout filterableSpinnerContainer = new LinearLayout(context);
        LinearLayout.LayoutParams filterableSpinnerContainerParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        filterableSpinnerContainer.setLayoutParams(filterableSpinnerContainerParams);
        filterableSpinnerContainer.setOrientation(LinearLayout.VERTICAL);
        filterableSpinnerContainer.setPadding((int) (2 * mDensity), 0, (int) (2 * mDensity), 0);

        filterableLabelTextView = new TextView(context);
        LinearLayout.LayoutParams districtTextViewLayoutParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        districtTextViewLayoutParams.gravity = Gravity.CENTER_VERTICAL;
        filterableLabelTextView.setLayoutParams(districtTextViewLayoutParams);
        filterableLabelTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, context.getResources().getDimension(R.dimen.component_label_size));

        filterableSpinnerContainer.addView(filterableLabelTextView);

        addView(filterableSpinnerContainer);

    }

    public void setSchema(Schema schema) {

        filterableLabelTextView.setText(schema.getLabel());

        spinner = new FilterableSpinner(context);
        spinner.setList(new ArrayList<ListHolder>());

        this.addView(spinner);

    }

    public void setDistrict(String district) {
        MunicipalityListAPI municipalityListAPI = retrofit.create(MunicipalityListAPI.class);
        Map<String, String> map = new HashMap<>();
        map.put("keyword", "");
        map.put("district", district);

        Call<List<Value>> municipalityListCall = municipalityListAPI.getMunicipalityList(map);

        municipalityListCall.enqueue(new Callback<List<Value>>() {
            @Override
            public void onResponse(Call<List<Value>> call, Response<List<Value>> response) {
                List<Value> values = response.body();
                if (values != null) {
                    TTFilterableSpinner.this.values = getMunicipalityList(values);
                    spinner.setList(TTFilterableSpinner.this.values);

                } else {
                    Toast.makeText(context, "Values is null", Toast.LENGTH_SHORT).show();
                }
            }

            private List<ListHolder> getMunicipalityList(List<Value> values) {
                List<ListHolder> listHolders = new ArrayList<>();
                for(int i=0; i<values.size(); i++) {
                    ListHolder holder = new ListHolder();
                    holder.setId(i);
                    holder.setLabel(values.get(i).getLabel());
                    holder.setName(values.get(i).getLabel());
                    listHolders.add(holder);
                }
                return listHolders;
            }

            @Override
            public void onFailure(Call<List<Value>> call, Throwable t) {
                Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        values = new ArrayList<>();
    }

    public void setId(int id) {

    }

    public String getSelectedItemLabel(){
        return "";
    }

    public void setOnItemSelectedListener(FilterableSpinner.OnItemSelectedListener onItemSelectedListener) {
        spinner.setOnItemSelectedListener(onItemSelectedListener);
    }

    public int getSelectedItemPosition() {
        return spinner.getSelectedItemPosition();
    }

    public ListHolder getSelectedItem() {
        return spinner.getSelectedItem();
    }

    public void notifyDataSetChanged(List<ListHolder> holders){
        if(spinner != null) {
            spinner.setList(holders);
        }
    }

    public void clear() {
        if(spinner!=null) {
            spinner.setList(new ArrayList<ListHolder>());
        }
    }

    @Override
    public void setEnabled(boolean enabled) {
        if(spinner != null) {
            spinner.setEnabled(enabled);
        }
    }

}
