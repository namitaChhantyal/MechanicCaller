package com.codebee.tradethrust.view.acitivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.codebee.tradethrust.R;
import com.codebee.tradethrust.application.TradeThrustApplication;
import com.codebee.tradethrust.model.form_details.list.Schema;
import com.codebee.tradethrust.model.records_details.DataItem;
import com.codebee.tradethrust.model.records_details.FormRecord;
import com.codebee.tradethrust.model.task.list.Datum;
import com.codebee.tradethrust.network.TaskFormListAPI;
import com.codebee.tradethrust.utils.BaseActivity;
import com.codebee.tradethrust.utils.ThrustConstant;
import com.codebee.tradethrust.view.adapter.FormRecordListAdapter;
import com.codebee.tradethrust.view.adapter.TaskListAdapter;
import com.codebee.tradethrust.view.component.spinner.MyDividerItemDecoration;
import com.codebee.tradethrust.view.interfaces.OnFormRecordClickedListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class FormRecordListActivity extends BaseActivity implements OnFormRecordClickedListener {

    private SharedPreferences preferences;
    private Retrofit retrofit;

    private FormRecordListAdapter adapter;

    private long formId;
    private int taskId;

    @BindView(R.id.task_title_text_view)
    public TextView taskTitleTextView;

    @BindView(R.id.progressBar_container)
    public LinearLayout progressBar;

    @BindView(R.id.main_container)
    public LinearLayout mainContainer;

    @BindView(R.id.recycler_view)
    public RecyclerView recyclerView;

    @Override
    protected void onViewReady(Bundle savedInstanceState, Intent intent) {
        ButterKnife.bind(this);

        TradeThrustApplication tradeThrustApplication = (TradeThrustApplication) getApplicationContext();
        preferences = tradeThrustApplication.getPreferences();

        retrofit = tradeThrustApplication.getRetrofit();

        taskTitleTextView.setText(intent.getStringExtra("taskTitle"));

        formId = intent.getLongExtra("formId", 0L);
        taskId = intent.getIntExtra("taskId", 0);

        progressBar.setVisibility(View.VISIBLE);
        mainContainer.setVisibility(View.GONE);

        Map<String, String> headersInfo = getHeaderInfo();

        TaskFormListAPI taskFormListAPICall = retrofit.create(TaskFormListAPI.class);
        Call<FormRecord> formRecordCall = taskFormListAPICall.getFormRecords(headersInfo, taskId, (int) formId);
        formRecordCall.enqueue(new Callback<FormRecord>() {
            @Override
            public void onResponse(Call<FormRecord> call, Response<FormRecord> response) {
                FormRecord formRecord = response.body();
                if(formRecord != null) {
                    progressBar.setVisibility(View.GONE);
                    mainContainer.setVisibility(View.VISIBLE);
                    setupView(formRecord);
                }else {
                    Toast.makeText(FormRecordListActivity.this, "Something went wrong. Please contact Administrator", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<FormRecord> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                mainContainer.setVisibility(View.GONE);
            }
        });

    }

    private void setupView(FormRecord formRecord) {
        setAdapter(formRecord);
    }

    private void setAdapter(FormRecord formRecord) {

        adapter = new FormRecordListAdapter(this, formRecord.getData(), this);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new MyDividerItemDecoration(this, DividerItemDecoration.VERTICAL, (int) (0 * getResources().getDisplayMetrics().density)));
        recyclerView.setAdapter(adapter);
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
        return R.layout.activity_form_record_list;
    }

    @OnClick(R.id.close_detail_btn)
    public void closeDetails(View view){
        super.onBackPressed();
    }

    @OnClick(R.id.info_detail_btn)
    public void openTaskDetails(View view){
        super.onBackPressed();
    }

    @Override
    public void onFormRecordClickedListener(DataItem record) {
        Intent intent = new Intent(this, ShowTaskFormDetailsActivity.class);
        intent.putExtra("formValueMap", record.getData());
        intent.putExtra("schemaList", (ArrayList<Schema>) record.getForm().getSchema());
        intent.putExtra("formId", formId);
        intent.putExtra("taskId", taskId);
        startActivity(intent);
    }
}
