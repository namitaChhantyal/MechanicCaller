package com.codebee.tradethrust.view.acitivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Toast;

import com.codebee.tradethrust.R;
import com.codebee.tradethrust.application.TradeThrustApplication;
import com.codebee.tradethrust.model.form_details.list.FormDetails;
import com.codebee.tradethrust.model.task.details.Data;
import com.codebee.tradethrust.network.StartThisActivityAPI;
import com.codebee.tradethrust.utils.BaseActivity;
import com.codebee.tradethrust.utils.ThrustConstant;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by csangharsha on 6/17/18.
 */

public class StartThisTaskActivity extends BaseActivity {

    private Long taskId;
    private Long formId;
    private int posId;
    private String formType;
    private SharedPreferences preferences;
    private OkHttpClient okHttpClient;
    private Gson gson;
    private ArrayList<Data> formList;

    @Override
    protected void onViewReady(Bundle savedInstanceState, Intent intent) {
        ButterKnife.bind(this);

//        intent.putExtra("taskId", data.getId());
//        intent.putExtra("formType", data.getForm().getFormType());
//        intent.putExtra("formId", data.getForm().getId());
//        intent.putExtra("posId", posId);

        taskId = intent.getLongExtra("taskId", 0);
        formId = intent.getLongExtra("formId", 0);
        formType = intent.getStringExtra("formType");
        posId = intent.getIntExtra("posId", 0);
        if(intent.hasExtra("formList")) {
            formList = (ArrayList<Data>) intent.getSerializableExtra("formList");
        }

        TradeThrustApplication application = (TradeThrustApplication) getApplicationContext();

        preferences = application.getPreferences();
        okHttpClient = application.okHttpClient();
        gson = application.gson();
    }

    @NonNull
    private RequestBody createPartFromString(String descriptionString) {
        return RequestBody.create(
                okhttp3.MultipartBody.FORM, descriptionString);
    }

    @NonNull
    private Map<String, String> getHeaderInfo() {
        Map<String, String> headersInfo = new HashMap<>();
        headersInfo.put("access-token", preferences.getString(ThrustConstant.ACCESS_TOKEN, ""));
        headersInfo.put("client", preferences.getString(ThrustConstant.CLIENT, ""));
        headersInfo.put("uid", preferences.getString(ThrustConstant.UID, ""));
        headersInfo.put("Organization", preferences.getString(ThrustConstant.COMPANY_NAME, ""));
        return headersInfo;
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_start_this_task;
    }

    @OnClick(R.id.start_this_task_btn)
    public void startThisTask(View view){

        showProgressDialog("Updating status...");

        Retrofit retrofit = getRetrofit();

        Map<String, String> requestBodyMap = new HashMap<>();

        Map<String, String> headersInfo = getHeaderInfo();

        StartThisActivityAPI startThisActivityAPI = retrofit.create(StartThisActivityAPI.class);

        final Call<FormDetails> formDetailsCall = startThisActivityAPI.updateTaskStatus(String.valueOf(taskId), headersInfo, requestBodyMap);

        formDetailsCall.enqueue(new Callback<FormDetails>() {
            @Override
            public void onResponse(Call<FormDetails> call, Response<FormDetails> response) {

                hideProgressDialog();

                if(response.body() != null) {
//                    if(formType == null) {
                        Intent intent = new Intent(StartThisTaskActivity.this, TaskDetailsActivity.class);
                        intent.putExtra("taskId", taskId);
                        intent.putExtra("formType", formType);
                        intent.putExtra("formList", formList);
                        startActivity(intent);
//                    }else {
//                        Intent intent = new Intent(StartThisTaskActivity.this, POSTaskDetailsActivity.class);
//                        intent.putExtra("taskId", taskId);
//                        intent.putExtra("formType", formType);
//                        intent.putExtra("formId", formId);
//                        intent.putExtra("posId", posId);
//                        startActivity(intent);
//                    }

                    finish();
                } else {
                    Toast.makeText(StartThisTaskActivity.this, "Could not update status! " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<FormDetails> call, Throwable t) {
                hideProgressDialog();
                Toast.makeText(StartThisTaskActivity.this, "Could not update status! ", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected boolean showActionBar() {
        return true;
    }

    private Retrofit getRetrofit() {

        String mBaseUrl = ThrustConstant.BASE_URL;

        return new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                .baseUrl(mBaseUrl)
                .client(okHttpClient)
                .build();
    }
}
