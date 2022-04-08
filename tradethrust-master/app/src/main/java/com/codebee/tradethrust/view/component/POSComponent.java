package com.codebee.tradethrust.view.component;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.codebee.tradethrust.application.TradeThrustApplication;
import com.codebee.tradethrust.model.pos.DataItem;
import com.codebee.tradethrust.model.pos.POS;
import com.codebee.tradethrust.model.province.ListHolder;
import com.codebee.tradethrust.model.province.Province;
import com.codebee.tradethrust.network.POSListAPI;
import com.codebee.tradethrust.network.ProvinceListAPI;
import com.codebee.tradethrust.utils.ThrustConstant;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class POSComponent extends LinearLayout {

    private Context context;

    private float density;
    private Retrofit retrofit;
    private SharedPreferences preferences;
    private LinearLayout POSContainer;
    private TTFilterableSpinner POSSpinner;

    public POSComponent(Context context) {
        super(context);

        initLayout(context);
    }

    private void initLayout(final Context context) {
        this.context = context;

        density = context.getResources().getDisplayMetrics().density;

        TradeThrustApplication application = (TradeThrustApplication) context.getApplicationContext();
        retrofit = application.getRetrofit();
        preferences = application.getPreferences();

        LinearLayout.LayoutParams mainParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        setLayoutParams(mainParams);
        setOrientation(LinearLayout.VERTICAL);

        setPOSLayout();

        final POSListAPI posListAPI = retrofit.create(POSListAPI.class);
        Call<POS> posListHolder = posListAPI.getPOSList(getHeaderInfo());

        posListHolder.enqueue(new Callback<POS>() {
            @Override
            public void onResponse(Call<POS> call, Response<POS> response) {

                POS province = response.body();

                if(province != null) {
                    List<DataItem> provinceList = province.getData();

//                    POSSpinner.notifyDataSetChanged(provinceList);


                }else {
                    Toast.makeText(context, "Something went wrong. Contact Administrator.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<POS> call, Throwable t) {
                Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
            }

        });
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

    private void setPOSLayout() {

        POSContainer = new LinearLayout(context);
        LinearLayout.LayoutParams provinceContainerParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        provinceContainerParams.bottomMargin = (int) (16 * density);
        POSContainer.setLayoutParams(provinceContainerParams);
        POSContainer.setOrientation(LinearLayout.VERTICAL);
        POSContainer.setPadding((int) (2 * density), 0, (int) (2 * density), 0);

        POSSpinner = new TTFilterableSpinner(context);

        POSContainer.addView(POSSpinner);

        this.addView(POSContainer);
        POSContainer.setVisibility(View.GONE);

    }


}
