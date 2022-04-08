package com.codebee.tradethrust.view.fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.codebee.tradethrust.R;
import com.codebee.tradethrust.application.TradeThrustApplication;
import com.codebee.tradethrust.dao.TaskTabDAO;
import com.codebee.tradethrust.dao.impl.TaskTabDAOImpl;
import com.codebee.tradethrust.model.FilterTaskType;
import com.codebee.tradethrust.model.pos_filter.FilterData;
import com.codebee.tradethrust.model.task.list.Datum;
import com.codebee.tradethrust.model.task.list.TaskList;
import com.codebee.tradethrust.network.TaskListApi;
import com.codebee.tradethrust.utils.ThrustConstant;
import com.codebee.tradethrust.view.acitivity.StartThisTaskActivity;
import com.codebee.tradethrust.view.acitivity.TaskDetailsActivity;
import com.codebee.tradethrust.view.adapter.TaskListAdapter;
import com.codebee.tradethrust.view.interfaces.OnTaskClickedListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by csangharsha on 5/17/18.
 */

public class TaskTabFragment extends Fragment implements OnTaskClickedListener {

    private static final String TAG = "log";

    @BindView(R.id.task_recycler_view)
    public RecyclerView recyclerView;

    @BindView(R.id.swiperefresh)
    public SwipeRefreshLayout swipeRefreshLayout;

    @BindView(R.id.task_list_container)
    public LinearLayout taskListContainer;

    @BindView(R.id.progressBar_container)
    public LinearLayout progressBarContainer;

    @BindView(R.id.no_item_found_text_view)
    public TextView noItemFoundTextView;

    @BindView(R.id.no_item_found_container)
    public LinearLayout noItemFoundContainer;

    private TaskListAdapter adapter;
    private List<Datum> taskList;

    private String taskTypeCode;
    private String token;
    private SharedPreferences preferences;
    private Retrofit retrofit;

    private static FilterTaskType filterTaskType;

    private boolean allowRefresh = false;

    private TaskTabDAO taskTabDAO = new TaskTabDAOImpl();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.task_tab_fragment,
                container, false);

        token = FirebaseInstanceId.getInstance().getToken();
        Log.d("TOKEN", "onViewReady: " + token);

        ButterKnife.bind(this, view);

        TradeThrustApplication application = (TradeThrustApplication) getActivity().getApplicationContext();
        preferences = application.getPreferences();
        retrofit = application.getRetrofit();

        taskTypeCode = getArguments().getString("taskTypeCode");

        hideAllView();

        setAdapter();

        getTaskList();

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // cancel the Visual indication of a refresh
                swipeRefreshLayout.setRefreshing(false);

                getTaskList();
            }
        });

        return view;
    }

    private void hideAllView() {
        recyclerView.setVisibility(View.GONE);
        progressBarContainer.setVisibility(View.GONE);
        noItemFoundTextView.setVisibility(View.GONE);
    }

    private void getTaskList() {

        hideAllView();

        progressBarContainer.setVisibility(View.VISIBLE);

        Map<String, String> headersInfo = getHeaderInfo();
        Map<String, String> body = new HashMap<>();
        body.put("device_id", token);

        Map<String, String> queryParams = new HashMap<>();
        queryParams.put("fos_id", preferences.getString(ThrustConstant.FOS_ID, "0"));

        if(filterTaskType != null && filterTaskType.getFilterType() == ThrustConstant.FITLER_CATEGORY_TYPE_BIT){
            queryParams.put("bit_id", String.valueOf(filterTaskType.getFilterId()));
        }else if(filterTaskType != null && filterTaskType.getFilterType() == ThrustConstant.FITLER_CATEGORY_TYPE_POS){
            queryParams.put("bit_pos_id", String.valueOf(filterTaskType.getFilterId()));
        }

        final TaskListApi taskListApi = retrofit.create(TaskListApi.class);
        Call<TaskList> task = taskListApi.getTask(queryParams, taskTypeCode, token, headersInfo);

        task.enqueue(new Callback<TaskList>() {
            @Override
            public void onResponse(Call<TaskList> call, Response<TaskList> response) {

                hideAllView();

                TaskList taskListTemp = response.body();

                if(taskListTemp != null) {

                    taskList.clear();

                    taskList.addAll(taskListTemp.getData());

                    if (taskList.size() > 0) {
                        recyclerView.setVisibility(View.VISIBLE);
                        adapter.notifyDataSetChanged();
                    } else {
                        noItemFoundTextView.setVisibility(View.VISIBLE);
                    }
                }else {
                    Toast.makeText(getContext(), "Something went wrong. Contact administrator.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<TaskList> call, Throwable t) {
                Toast.makeText(getActivity(), "Something went wrong. Contact Administrator.", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "onFailure: " + t.getMessage());
            }
        });

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

    private void setAdapter() {
        taskList = new ArrayList<>();
        adapter = new TaskListAdapter(getActivity(), taskList, this);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onTaskClicked(Datum task) {
        allowRefresh = true;
        if(task.getStatus().equalsIgnoreCase(ThrustConstant.TASK_STATUS_NEW)) {
            Intent intent = new Intent(getActivity(), StartThisTaskActivity.class);
            intent.putExtra("taskId", task.getId().longValue());
            getActivity().startActivity(intent);
        }else {
            Intent intent = new Intent(getActivity(), TaskDetailsActivity.class);
            intent.putExtra("taskId", task.getId().longValue());
            getActivity().startActivity(intent);
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        if(allowRefresh) {
            allowRefresh = false;
            getTaskList();
        }
    }

    public void refreshTaskList(FilterTaskType filterTaskType){
        TaskTabFragment.filterTaskType = filterTaskType;

        getTaskList();
    }
}
