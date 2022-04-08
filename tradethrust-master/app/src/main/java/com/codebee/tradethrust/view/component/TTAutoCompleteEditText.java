package com.codebee.tradethrust.view.component;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.codebee.tradethrust.R;
import com.codebee.tradethrust.application.TradeThrustApplication;
import com.codebee.tradethrust.model.form_details.list.Schema;
import com.codebee.tradethrust.model.form_details.list.Value;
import com.codebee.tradethrust.network.MunicipalityListAPI;
import com.codebee.tradethrust.utils.ThrustConstant;
import com.codebee.tradethrust.view.component.adapters.CustomAutoCompleteTextViewAdapter;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by csangharsha on 6/7/18.
 */
public class TTAutoCompleteEditText extends LinearLayout {

    private Context context;
    private CustomAutoCompleteView autoCompleteTextView;
    private CustomAutoCompleteTextViewAdapter adapter;

    private SharedPreferences preferences;
    private OkHttpClient okHttpClient;
    private Gson gson;

    private float mDensity;

    private List<Value> values;
    private String district;

    public TTAutoCompleteEditText(Context context) {
        super(context);
        initLayout(context);
    }

    private void initLayout(Context context) {
        this.context = context;

        TradeThrustApplication application = (TradeThrustApplication) context.getApplicationContext();
        preferences = application.getPreferences();
        okHttpClient = application.okHttpClient();
        gson = application.gson();

        mDensity = context.getResources().getDisplayMetrics().density;

        LinearLayout.LayoutParams mainParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mainParams.bottomMargin = (int) (16 * mDensity);
        setLayoutParams(mainParams);
        setOrientation(LinearLayout.VERTICAL);

    }

    private void setAdapter() {

        values = new ArrayList<>();

        adapter = new CustomAutoCompleteTextViewAdapter(context,
                R.layout.autocompleteitem, values);

        autoCompleteTextView.setAdapter(adapter);

    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public void setSchema(final Schema schema) {

        autoCompleteTextView = new CustomAutoCompleteView(context);
        autoCompleteTextView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        autoCompleteTextView.setTag(schema.getName());
        autoCompleteTextView.setHint(schema.getLabel());
        autoCompleteTextView.setThreshold(1);
        autoCompleteTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                adapter.notifyDataSetChanged();

                if(schema.getName().equalsIgnoreCase(ComponentGenerator.COMPONENT_TYPE_AUTOCOMPLETE_SUBTYPE_MUNICIPALITY)) {
                    if(editable.toString().length() >= 3) {
                        MunicipalityListAPI municipalityListAPI = getRetrofit().create(MunicipalityListAPI.class);
                        Map<String, String> map = new HashMap<>();
                        map.put("keyword", editable.toString());
                        map.put("district", district);

                        Call<List<Value>> municipalityListCall = municipalityListAPI.getMunicipalityList(map);

                        municipalityListCall.enqueue(new Callback<List<Value>>() {
                            @Override
                            public void onResponse(Call<List<Value>> call, Response<List<Value>> response) {
                                List<Value> values = response.body();
                                if (values != null) {
                                    adapter.notifyDataSetChanged();
                                    adapter = new CustomAutoCompleteTextViewAdapter(context,
                                            R.layout.autocompleteitem, values);

                                    autoCompleteTextView.setAdapter(adapter);
                                } else {
                                    Toast.makeText(context, "Values is null", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<List<Value>> call, Throwable t) {
                                Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }else {

                    List<Value> values = new ArrayList<>();

                    for (Value value : TTAutoCompleteEditText.this.values) {
                        if (value.getLabel().toLowerCase().contains(editable.toString().toLowerCase())) {
                            values.add(value);
                        }
                    }


                    adapter = new CustomAutoCompleteTextViewAdapter(context,
                            R.layout.autocompleteitem, values);

                    autoCompleteTextView.setAdapter(adapter);
                }
            }
        });

        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View arg1, int pos, long id) {

                LinearLayout rl = (LinearLayout) arg1;
                TextView tv = (TextView) rl.getChildAt(0);
                autoCompleteTextView.setText(tv.getText().toString());

            }

        });

        setAdapter();

        this.addView(autoCompleteTextView);
        if(schema.getValues() != null) {
            this.values = new ArrayList<>();
            this.values.addAll(schema.getValues());
            adapter.notifyDataSetChanged();

        }
    }

    public void setId(int id) {
        autoCompleteTextView.setId(id);
    }

    public void setText(String value) {
        autoCompleteTextView.setText(value);
    }

    private Retrofit getRetrofit() {

        String mBaseUrl = ThrustConstant.BASE_URL;

        return new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                .baseUrl(mBaseUrl)
                .client(okHttpClient)
                .build();
    }

    private Map<String, String> getHeaderInfo() {

        Map<String, String> headersInfo = new HashMap<>();
        headersInfo.put("access-token", preferences.getString(ThrustConstant.ACCESS_TOKEN, ""));
        headersInfo.put("client", preferences.getString(ThrustConstant.CLIENT, ""));
        headersInfo.put("uid", preferences.getString(ThrustConstant.UID, ""));
        headersInfo.put("Organization", preferences.getString(ThrustConstant.COMPANY_NAME, ""));
        headersInfo.put("Serializer", "Index::RecordSerializer");

        return headersInfo;
    }
}
