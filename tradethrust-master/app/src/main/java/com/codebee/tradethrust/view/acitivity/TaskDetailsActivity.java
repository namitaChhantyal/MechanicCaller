package com.codebee.tradethrust.view.acitivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.codebee.tradethrust.R;
import com.codebee.tradethrust.application.TradeThrustApplication;
import com.codebee.tradethrust.model.pos_filter.FilterData;
import com.codebee.tradethrust.model.task.details.Data;
import com.codebee.tradethrust.model.task.details.TaskDetails;
import com.codebee.tradethrust.network.TaskDetailAPI;
import com.codebee.tradethrust.utils.BaseActivity;
import com.codebee.tradethrust.utils.ThrustConstant;
import com.codebee.tradethrust.view.component.TaskFilterDialog;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class TaskDetailsActivity extends BaseActivity implements TaskFilterDialog.OnFilterBtnClickedListener{

    @BindView(R.id.activity_task_details_container)
    public LinearLayout activityTaskDetailsContainer;

    @BindView(R.id.description_container)
    public LinearLayout descriptionContainer;

    @BindView(R.id.task_title_text_view)
    public TextView taskTitleTextView;

    @BindView(R.id.description_text_view)
    public TextView descriptionTextView;

    @BindView(R.id.created_date_text_view)
    public TextView createdDateTextView;

    @BindView(R.id.deadline_text_view)
    public TextView deadlineTextView;

    @BindView(R.id.task_type_text_view)
    public TextView taskTypeTextView;

    @BindView(R.id.total_pos_text_view)
    public TextView totalPOSTextView;

    @BindView(R.id.form_name_textView)
    public TextView formNameTextView;

    @BindView(R.id.progressBar_container)
    public LinearLayout progressBarContainer;

    @BindView(R.id.utility_btn_group)
    public LinearLayout utilityBtnGroup;

    @BindView(R.id.main_container)
    public LinearLayout mainContainer;

    private int taskId;
    private SharedPreferences preferences;
    private Retrofit retrofit;

    private TaskDetails taskDetails;
    private Long formId;

    @Override
    protected void onViewReady(Bundle savedInstanceState, Intent intent) {

        ButterKnife.bind(this);

        taskId = (int) intent.getLongExtra("taskId", 0);

        hideAllViews();

        TradeThrustApplication application = (TradeThrustApplication) getApplicationContext();
        preferences = application.getPreferences();
        retrofit = application.getRetrofit();

        Map<String, String> headersInfo = new HashMap<>();
        headersInfo.put("access-token", preferences.getString(ThrustConstant.ACCESS_TOKEN, ""));
        headersInfo.put("client", preferences.getString(ThrustConstant.CLIENT, ""));
        headersInfo.put("uid", preferences.getString(ThrustConstant.UID, ""));
        headersInfo.put("Organization", preferences.getString(ThrustConstant.COMPANY_NAME, ""));

        final TaskDetailAPI taskDetailAPI = retrofit.create(TaskDetailAPI.class);
        Call<TaskDetails> taskDetailsCall = taskDetailAPI.getTaskDetails(String.valueOf(taskId), headersInfo);

        progressBarContainer.setVisibility(View.VISIBLE);
        taskDetailsCall.enqueue(new Callback<TaskDetails>() {
            @Override
            public void onResponse(Call<TaskDetails> call, Response<TaskDetails> response) {
                taskDetails = response.body();
                formId = taskDetails.getData().getForm().getId();

                populateViewValues(taskDetails);
                progressBarContainer.setVisibility(View.GONE);
                mainContainer.setVisibility(View.VISIBLE);
                utilityBtnGroup.setVisibility(View.VISIBLE);
            }

            @Override
            public void onFailure(Call<TaskDetails> call, Throwable t) {
                hideAllViews();
                Toast.makeText(TaskDetailsActivity.this, "Something went wrong. Please contact Administrator", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void hideAllViews() {
        mainContainer.setVisibility(View.GONE);
        progressBarContainer.setVisibility(View.GONE);
        utilityBtnGroup.setVisibility(View.GONE);
    }

    private void populateViewValues(TaskDetails taskDetails) {

        Data data = taskDetails.getData();
        taskTitleTextView.setText(data.getTitle());
        createdDateTextView.setText(getFormattedDate(data.getStartDate()));
        deadlineTextView.setText(getFormattedDate(data.getEndsDate()));

        if(data.getDescription() == null) {
            descriptionContainer.setVisibility(View.GONE);
        }else {
            descriptionContainer.setVisibility(View.VISIBLE);
            descriptionTextView.setText(String.valueOf(data.getDescription()));
        }

        if(data.getForm().getFormType() == null) {
            taskTypeTextView.setText("");
        }else {
            taskTypeTextView.setText(data.getForm().getFormType());
        }

        int totalPOS = 0;

        if(data.getPos() != null) {
            totalPOS = data.getPos().size();
        }
        totalPOSTextView.setText(String.valueOf(totalPOS));

        formNameTextView.setText(data.getForm().getName());
    }

    private String getFormattedDate(String startDate) {
        try {
            Date date = new SimpleDateFormat("yyyy-MM-dd").parse(startDate);
            return new SimpleDateFormat("MMM dd, yyyy").format(date);
        } catch (ParseException e) {

        }
        return startDate;
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_task_details;
    }

    @OnClick(R.id.form_name_textView)
    public void openFormActivity(View view){
        if(taskDetails != null && taskDetails.getData().getForm().getFormType() == null) {
            Toast.makeText(this, "ERROR: Form does not have type!!!", Toast.LENGTH_SHORT).show();
            return;
        }

        if(taskDetails != null && taskDetails.getData().getForm().getFormType().equals(ThrustConstant.FORM_TYPE_NEW_FORM)) {
            Intent intent = new Intent(TaskDetailsActivity.this, TaskFormActivity.class);
            intent.putExtra("formId", formId);
            intent.putExtra("taskId", taskId);
            startActivity(intent);
        }else if(taskDetails != null && taskDetails.getData().getForm().getFormType().equals(ThrustConstant.FORM_TYPE_REVISIT_FORM)){
            TaskFilterDialog filterDialog = new TaskFilterDialog(this, new com.codebee.tradethrust.model.pos_filter.Data(taskDetails.getData().getBit().getId().intValue(), taskDetails.getData().getBit().getName()),this);
            filterDialog.setTitle("Select POS");
            filterDialog.showDialog(ThrustConstant.FITLER_CATEGORY_TYPE_POS);
        }
    }

    @OnClick(R.id.close_detail_btn)
    public void closeDetails(View view){
        super.onBackPressed();
    }

    @OnClick(R.id.map_detail_btn)
    public void openMapView(View view) {
        if(taskDetails != null){
            Intent intent = new Intent(TaskDetailsActivity.this, MapViewActivity.class);
            intent.putExtra("formId", formId);
            intent.putExtra("taskId", taskId);
            intent.putExtra("formType", taskDetails.getData().getForm().getFormType());
            intent.putExtra("wkt", taskDetails.getData().getBit().getArea());

            if(taskDetails.getData().getBit().getCenter().size() > 0) {
                intent.putExtra("centerLatOfBit", taskDetails.getData().getBit().getCenter().get(0));
                intent.putExtra("centerLongOfBit", taskDetails.getData().getBit().getCenter().get(0));
            }

            startActivity(intent);
        }else {
            Toast.makeText(this, "Task was not loaded", Toast.LENGTH_SHORT).show();
        }
    }

    @OnClick(R.id.list_detail_btn)
    public void openFormRecords() {
        if(taskDetails != null) {
            Intent intent = new Intent(TaskDetailsActivity.this, FormRecordListActivity.class);
            intent.putExtra("formId", formId);
            intent.putExtra("taskId", taskId);
            intent.putExtra("taskTitle", taskDetails.getData().getTitle());

            startActivity(intent);
        }else {
            Toast.makeText(this, "Task was not loaded", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onFilterBtnClicked(com.codebee.tradethrust.model.pos_filter.Data filterData) {
        Intent intent = new Intent(TaskDetailsActivity.this, TaskFormActivity.class);
        intent.putExtra("formId", formId);
        intent.putExtra("taskId", taskId);
        intent.putExtra("posId", filterData.getId());
        intent.putExtra("formType", taskDetails.getData().getForm().getFormType());
        startActivity(intent);
    }
}
