package com.codebee.tradethrust.view.component;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Filter;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.codebee.tradethrust.R;
import com.codebee.tradethrust.application.TradeThrustApplication;
import com.codebee.tradethrust.model.pos_filter.Data;
import com.codebee.tradethrust.model.pos_filter.FilterData;
import com.codebee.tradethrust.network.FOSBitsAPI;
import com.codebee.tradethrust.utils.ThrustConstant;
import com.codebee.tradethrust.view.component.adapters.FilterDialogSpinnerAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class TaskFilterDialog {

    private FilterDialogSpinnerAdapter bitAdapter;
    private FilterDialogSpinnerAdapter posAdapter;
    protected AlertDialog b;
    protected AlertDialog.Builder dialogBuilder;
    private Context context;

    private TextView titleTextView;
    private Spinner bitSelectSpinner;
    private Spinner posSelectSpinner;
    private LinearLayout bitSpinnerContainer;
    private LinearLayout posSpinnerContainer;

    private ProgressBar bitProgressbar;
    private ProgressBar posProgressbar;
    private List<Data> bitDataList = new ArrayList<>();
    private List<Data> posDataList = new ArrayList<>();

    private OnFilterBtnClickedListener onFilterBtnClickedListener;

    private SharedPreferences preferences;
    private Retrofit retrofit;

    private Data bitInfo;
    private Button filterBtn;
    private int filterType = ThrustConstant.FITLER_CATEGORY_TYPE_POS;


    public TaskFilterDialog(Context context, OnFilterBtnClickedListener onFilterBtnClickedListener) {
        initLayout(context, onFilterBtnClickedListener);
    }

    public TaskFilterDialog(Context context, Data bitInfo, OnFilterBtnClickedListener onFilterBtnClickedListener) {
        initLayout(context, onFilterBtnClickedListener);
        this.bitInfo = bitInfo;
        filterBtn.setText("Fill Form");
    }

    private void initLayout(Context context, final OnFilterBtnClickedListener onFilterBtnClickedListener) {
        this.context = context;
        this.onFilterBtnClickedListener = onFilterBtnClickedListener;

        TradeThrustApplication application = (TradeThrustApplication) context.getApplicationContext();
        preferences = application.getPreferences();
        retrofit = application.getRetrofit();

        dialogBuilder = new AlertDialog.Builder(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
        View dialogView = inflater.inflate(R.layout.fitler_dialog_layout, null);

        titleTextView = dialogView.findViewById(R.id.title_text_view);

        bitSpinnerContainer = dialogView.findViewById(R.id.bit_spinner_layout);
        posSpinnerContainer = dialogView.findViewById(R.id.pos_spinner_layout);
        bitSelectSpinner = dialogView.findViewById(R.id.bit_select_spinner);
        posSelectSpinner = dialogView.findViewById(R.id.pos_select_spinner);

        bitProgressbar = dialogView.findViewById(R.id.bit_progressbar);
        bitProgressbar.setVisibility(View.GONE);

        posProgressbar = dialogView.findViewById(R.id.pos_progressbar);
        posProgressbar.setVisibility(View.GONE);

        bitAdapter = new FilterDialogSpinnerAdapter(context, R.layout.filter_dialog_spinner_item, bitDataList, ThrustConstant.FITLER_CATEGORY_TYPE_BIT);
        bitSelectSpinner.setAdapter(bitAdapter);

        posAdapter = new FilterDialogSpinnerAdapter(context, R.layout.filter_dialog_spinner_item, posDataList, ThrustConstant.FITLER_CATEGORY_TYPE_POS);
        posSelectSpinner.setAdapter(posAdapter);

        dialogBuilder.setView(dialogView);
        dialogBuilder.setCancelable(true);
        b = dialogBuilder.create();

        filterBtn = dialogView.findViewById(R.id.filter_btn);
        filterBtn.setEnabled(false);

        filterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (filterType) {
                    case ThrustConstant.FITLER_CATEGORY_TYPE_BIT:
                        onFilterBtnClickedListener.onFilterBtnClicked((Data) bitSelectSpinner.getSelectedItem());
                        break;
                    case ThrustConstant.FITLER_CATEGORY_TYPE_POS:
                        onFilterBtnClickedListener.onFilterBtnClicked((Data) posSelectSpinner.getSelectedItem());
                        break;
                }
                hideDialog();
            }
        });

    }

    public int getFilteredPOSId() {
        return ((Data) (posSelectSpinner.getSelectedItem())).getId();
    }

    private void setBitData() {

        bitDataList.clear();
        if(bitInfo != null) {
            bitDataList.add(bitInfo);
            startBitDataUpdate();
            bitAdapter.setNotifyOnChange(true);
            bitAdapter.notifyDataSetChanged();
            endBitDataUpdate();
        }else {
            startBitDataUpdate();

            FOSBitsAPI fosBitsAPI = retrofit.create(FOSBitsAPI.class);
            Call<FilterData> filterDataCall = fosBitsAPI.getBitForFos(getHeaderInfo(), preferences.getString(ThrustConstant.FOS_ID, ""));

            filterDataCall.enqueue(new Callback<FilterData>() {
                @Override
                public void onResponse(Call<FilterData> call, Response<FilterData> response) {
                    FilterData filterData = response.body();
                    if(filterData != null) {
                        bitDataList.addAll(filterData.getData());
                        bitAdapter.setNotifyOnChange(true);
                        bitAdapter.notifyDataSetChanged();
                    }
                    endBitDataUpdate();
                }

                @Override
                public void onFailure(Call<FilterData> call, Throwable t) {
                    endBitDataUpdate();
                }
            });

        }

    }

    private void endBitDataUpdate() {
        bitSelectSpinner.setEnabled(true);
        posSelectSpinner.setEnabled(true);
        bitProgressbar.setVisibility(View.GONE);
        filterBtn.setEnabled(true);
    }

    private void startBitDataUpdate() {
        bitSelectSpinner.setEnabled(false);
        posSelectSpinner.setEnabled(false);
        bitProgressbar.setVisibility(View.VISIBLE);
    }

    private void endPOSDataUpdate() {
        posSelectSpinner.setEnabled(true);
        posProgressbar.setVisibility(View.GONE);
        filterBtn.setEnabled(true);
    }

    private void startPOSDataUpdate() {
        posSelectSpinner.setEnabled(false);
        posProgressbar.setVisibility(View.VISIBLE);
    }

    public void setTitle(String title) {
        titleTextView.setText(title);
    }

    public void showDialog( int filterType ) {
        this.filterType = filterType;
        switch (filterType) {
            case ThrustConstant.FITLER_CATEGORY_TYPE_BIT:
                posSpinnerContainer.setVisibility(View.GONE);
                bitSpinnerContainer.setVisibility(View.VISIBLE);
                setBitData();
                break;
            case ThrustConstant.FITLER_CATEGORY_TYPE_POS:
                bitSpinnerContainer.setVisibility(View.GONE);
                posSpinnerContainer.setVisibility(View.VISIBLE);
                setPosData();
                break;
        }
        b.show();
    }

    private void setPosData() {
        startPOSDataUpdate();

        FOSBitsAPI fosBitsAPI = retrofit.create(FOSBitsAPI.class);
        Call<FilterData> filterDataCall = fosBitsAPI.getPOSForFos(getHeaderInfo(), preferences.getString(ThrustConstant.FOS_ID, ""));

        filterDataCall.enqueue(new Callback<FilterData>() {
            @Override
            public void onResponse(Call<FilterData> call, Response<FilterData> response) {
                FilterData filterData = response.body();
                posDataList.clear();
                if(filterData != null) {
                    posDataList.addAll(filterData.getData());
                    posAdapter.setNotifyOnChange(true);
                    posAdapter.notifyDataSetChanged();
                }
                endPOSDataUpdate();
            }

            @Override
            public void onFailure(Call<FilterData> call, Throwable t) {
                endBitDataUpdate();
            }
        });

    }

    public Map<String, String> getHeaderInfo() {
        Map<String, String> headersInfo = new HashMap<>();
        headersInfo.put("access-token", preferences.getString(ThrustConstant.ACCESS_TOKEN, ""));
        headersInfo.put("client", preferences.getString(ThrustConstant.CLIENT, ""));
        headersInfo.put("uid", preferences.getString(ThrustConstant.UID, ""));
        headersInfo.put("Organization", preferences.getString(ThrustConstant.COMPANY_NAME, ""));
        return headersInfo;
    }

    public void hideDialog(){
        b.dismiss();
    }

    public interface OnFilterBtnClickedListener {
        void onFilterBtnClicked(Data filterData);
    }

}
