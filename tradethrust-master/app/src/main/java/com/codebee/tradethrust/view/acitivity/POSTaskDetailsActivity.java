package com.codebee.tradethrust.view.acitivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.codebee.tradethrust.R;
import com.codebee.tradethrust.application.TradeThrustApplication;
import com.codebee.tradethrust.model.task.details.Data;
import com.codebee.tradethrust.model.task.list.group_by_pos.POSFormDetails;
import com.codebee.tradethrust.network.POSFormDetailAPI;
import com.codebee.tradethrust.utils.BaseActivity;
import com.codebee.tradethrust.utils.ThrustConstant;
import com.codebee.tradethrust.view.component.TaskFilterDialog;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
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

public class POSTaskDetailsActivity extends BaseActivity {

    @BindView(R.id.activity_task_details_container)
    public LinearLayout activityTaskDetailsContainer;

    @BindView(R.id.pos_name_text_view)
    public TextView posNameTextView;

    @BindView(R.id.created_date_text_view)
    public TextView createdDateTextView;

    @BindView(R.id.status_text_view)
    public TextView statusTextView;

    @BindView(R.id.progressBar_container)
    public LinearLayout progressBarContainer;

    @BindView(R.id.utility_btn_group)
    public LinearLayout utilityBtnGroup;

    @BindView(R.id.main_container)
    public LinearLayout mainContainer;

    @BindView(R.id.pos_form_details_container)
    public LinearLayout posFormDetailsContainer;

    private POSFormDetails posFormDetails;

    private int posId;
    private String posName;
    private String status;
    private String createdBy;

    private SharedPreferences preferences;
    private Retrofit retrofit;
    private float mDensity;


    @Override
    protected void onViewReady(Bundle savedInstanceState, Intent intent) {
        ButterKnife.bind(this);

        posId = (int) intent.getLongExtra("posId", 0);
        posName = intent.getStringExtra("pos_name");
        status = intent.getStringExtra("status");
        createdBy = intent.getStringExtra("created_at");

        mDensity = getResources().getDisplayMetrics().density;

        hideAllViews();

        TradeThrustApplication application = (TradeThrustApplication) getApplicationContext();
        preferences = application.getPreferences();
        retrofit = application.getRetrofit();

        Map<String, String> headersInfo = new HashMap<>();
        headersInfo.put("access-token", preferences.getString(ThrustConstant.ACCESS_TOKEN, ""));
        headersInfo.put("client", preferences.getString(ThrustConstant.CLIENT, ""));
        headersInfo.put("uid", preferences.getString(ThrustConstant.UID, ""));
        headersInfo.put("Organization", preferences.getString(ThrustConstant.COMPANY_NAME, ""));

        final POSFormDetailAPI taskDetailAPI = retrofit.create(POSFormDetailAPI.class);
        Call<POSFormDetails> taskDetailsCall = taskDetailAPI.getPOSFormDetails(String.valueOf(posId), headersInfo);

        progressBarContainer.setVisibility(View.VISIBLE);
        taskDetailsCall.enqueue(new Callback<POSFormDetails>() {
            @Override
            public void onResponse(Call<POSFormDetails> call, Response<POSFormDetails> response) {
                posFormDetails = response.body();

                if(posFormDetails != null) {
                    populateViewValues(posFormDetails);
                    progressBarContainer.setVisibility(View.GONE);
                    mainContainer.setVisibility(View.VISIBLE);
                    utilityBtnGroup.setVisibility(View.GONE);
                }else {
                    Toast.makeText(POSTaskDetailsActivity.this, "Response is null.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<POSFormDetails> call, Throwable t) {
                hideAllViews();
                Toast.makeText(POSTaskDetailsActivity.this, "Something went wrong. Please contact Administrator", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void hideAllViews() {
        mainContainer.setVisibility(View.GONE);
        progressBarContainer.setVisibility(View.GONE);
        utilityBtnGroup.setVisibility(View.GONE);
    }

    private void populateViewValues(POSFormDetails taskDetails) {

        List<Data> data = taskDetails.getData();
        posNameTextView.setText(posName);
        createdDateTextView.setText(getFormattedDate(createdBy));
        statusTextView.setText(getFormattedDate(status));

        createdFormDetailsLayout(data);

    }

    private void createdFormDetailsLayout(List<Data> dataList) {
        for(Data data: dataList) {
            createView();
            createTextView(data);
        }
        createView();
    }

    private void createTextView(final Data data) {

        LinearLayout container = new LinearLayout(this);
        container.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        container.setOrientation(LinearLayout.HORIZONTAL);

        int[] attrs = new int[] { android.R.attr.selectableItemBackground /* index 0 */};
        TypedArray ta = obtainStyledAttributes(attrs);
        Drawable drawableFromTheme = ta.getDrawable(0 /* index */);
        ta.recycle();
        container.setBackground(drawableFromTheme);

        container.setClickable(true);

        TextView taskNameTextView = new TextView(this);
        LinearLayout.LayoutParams taskNameTextViewParams = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1);
        taskNameTextView.setLayoutParams(taskNameTextViewParams);
        taskNameTextView.setPadding((int) (15 * mDensity), (int) (10 * mDensity), (int) (10 * mDensity), (int) (10 * mDensity));
        taskNameTextView.setText(data.getTitle());
        taskNameTextView.setAllCaps(true);
        taskNameTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, this.getResources().getDimension(R.dimen.task_name_size));
        taskNameTextView.setTextColor(getResources().getColor(R.color.black));

        container.addView(taskNameTextView);

        TextView formNameTextView = new TextView(this);
        LinearLayout.LayoutParams formNameTextViewParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        formNameTextView.setLayoutParams(formNameTextViewParams);
        formNameTextView.setPadding((int) (15 * mDensity), (int) (10 * mDensity), (int) (10 * mDensity), (int) (10 * mDensity));
        formNameTextView.setText(data.getForm().getName());
        formNameTextView.setAllCaps(true);
        formNameTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, this.getResources().getDimension(R.dimen.task_name_size));
        formNameTextView.setTextColor(getResources().getColor(R.color.black));
        container.addView(formNameTextView);

        posFormDetailsContainer.addView(container);

        container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(data != null && data.getForm().getFormType() == null) {
                    Toast.makeText(POSTaskDetailsActivity.this, "ERROR: Form does not have type!!!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(data.getStatus().equalsIgnoreCase(ThrustConstant.TASK_STATUS_NEW)) {
                    Intent intent = new Intent(POSTaskDetailsActivity.this, StartThisTaskActivity.class);
                    intent.putExtra("taskId", data.getId());
                    intent.putExtra("formType", data.getForm().getFormType());
                    intent.putExtra("formId", data.getForm().getId());
                    intent.putExtra("posId", posId);
                    intent.putExtra("formList", posFormDetails.getData());
                    startActivity(intent);
                }else {
                    if (data != null && data.getForm().getFormType().equals(ThrustConstant.FORM_TYPE_NEW_FORM)) {
                        Intent intent = new Intent(POSTaskDetailsActivity.this, TaskFormActivity.class);
                        intent.putExtra("formId", data.getForm().getId());
                        intent.putExtra("taskId", data.getId().intValue());
                        intent.putExtra("formList", posFormDetails.getData());
                        startActivity(intent);
                    } else if (data != null && data.getForm().getFormType().equals(ThrustConstant.FORM_TYPE_REVISIT_FORM)) {
                        Intent intent = new Intent(POSTaskDetailsActivity.this, TaskFormActivity.class);
                        intent.putExtra("formId", data.getForm().getId());
                        intent.putExtra("taskId", data.getId().intValue());
                        intent.putExtra("posId", posId);
                        intent.putExtra("formType", data.getForm().getFormType());
                        intent.putExtra("formList", posFormDetails.getData());
                        startActivity(intent);
                    }
                }
            }
        });


    }

    private void createView() {
        View view = new View(this);
        view.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) (1 * mDensity)));
        view.setBackgroundColor(Color.parseColor("#CCCCCC"));
        posFormDetailsContainer.addView(view);
    }

    private String getFormattedDate(String startDate) {
        try {
            Date date = new SimpleDateFormat("yyyy-MM-dd").parse(startDate);
            return new SimpleDateFormat("MMM dd, yyyy").format(date);
        } catch (ParseException e) {

        }
        return startDate;
    }

    @OnClick(R.id.close_detail_btn)
    public void closeActivity(){
        super.onBackPressed();
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_pos_task_details;
    }

}
