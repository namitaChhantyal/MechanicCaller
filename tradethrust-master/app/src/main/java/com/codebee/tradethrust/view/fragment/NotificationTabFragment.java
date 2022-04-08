package com.codebee.tradethrust.view.fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.codebee.tradethrust.R;
import com.codebee.tradethrust.application.TradeThrustApplication;
import com.codebee.tradethrust.model.notification_details.DataItem;
import com.codebee.tradethrust.model.notification_details.Notification;
import com.codebee.tradethrust.network.NotificationListAPI;
import com.codebee.tradethrust.utils.ThrustConstant;
import com.codebee.tradethrust.view.adapter.FormRecordListAdapter;
import com.codebee.tradethrust.view.adapter.NotificationListAdapter;
import com.codebee.tradethrust.view.component.spinner.MyDividerItemDecoration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Created by csangharsha on 5/19/18.
 */

public class NotificationTabFragment extends Fragment {

    @BindView(R.id.no_item_found_text_view)
    public TextView noItemFoundTextView;

    @BindView(R.id.notification_recycler_view)
    public RecyclerView recyclerView;

    @BindView(R.id.recycler_view_container)
    public LinearLayout recyclerViewContainer;

    @BindView(R.id.progressBar_container)
    public LinearLayout progressBar;


    private NotificationListAdapter adapter;

    private SharedPreferences preferences;
    private Retrofit retrofit;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.notification_tab_fragment,
                container, false);

        ButterKnife.bind(this, view);

        TradeThrustApplication tradeThrustApplication = (TradeThrustApplication) getActivity().getApplicationContext();
        preferences = tradeThrustApplication.getPreferences();

        recyclerViewContainer.setVisibility(View.GONE);
        noItemFoundTextView.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);


        retrofit = tradeThrustApplication.getRetrofit();

        NotificationListAPI notificationListAPI = retrofit.create(NotificationListAPI.class);
        Call<Notification> notificationCall = notificationListAPI.getFormRecords(getHeaderInfo());

//        noItemFoundTextView.setVisibility(View.GONE);
//        recyclerViewContainer.setVisibility(View.VISIBLE);
//        progressBar.setVisibility(View.GONE);
//        setupView(mockData());

        notificationCall.enqueue(new Callback<Notification>() {
            @Override
            public void onResponse(Call<Notification> call, Response<Notification> response) {
                Notification notification = response.body();
                progressBar.setVisibility(View.GONE);
                if(notification != null) {
                    if(notification.getData().size() > 0) {
                        noItemFoundTextView.setVisibility(View.GONE);
                        recyclerViewContainer.setVisibility(View.VISIBLE);
                        setupView(notification);
                    }else {
                        noItemFoundTextView.setVisibility(View.VISIBLE);
                    }
                }else {

                }
            }

            @Override
            public void onFailure(Call<Notification> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
            }
        });

        return view;
    }


    private void setupView(Notification notification) {
        setAdapter(notification);
    }

    private Map<String, String> getHeaderInfo() {
        Map<String, String> headersInfo = new HashMap<>();
        headersInfo.put("access-token", preferences.getString(ThrustConstant.ACCESS_TOKEN, ""));
        headersInfo.put("client", preferences.getString(ThrustConstant.CLIENT, ""));
        headersInfo.put("uid", preferences.getString(ThrustConstant.UID, ""));
        headersInfo.put("Organization", preferences.getString(ThrustConstant.COMPANY_NAME, ""));
        return headersInfo;
    }

    public void setAdapter(Notification notification) {

        adapter = new NotificationListAdapter(getContext(), notification.getData());

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new MyDividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL, (int) (0 * getResources().getDisplayMetrics().density)));
        recyclerView.setAdapter(adapter);

    }

    public Notification mockData() {
        Notification notification = new Notification();

        List<DataItem> dataItemList = new ArrayList<>();

        DataItem dataItem = new DataItem();
        dataItem.setId(1);
        dataItem.setMessage("This is test Notification 1");
        dataItem.setCreatedAt("1 mins ago");
        dataItem.setRead(true);

        dataItemList.add(dataItem);

        dataItem = new DataItem();
        dataItem.setId(2);
        dataItem.setMessage("This is test Notification 2");
        dataItem.setCreatedAt("2 mins ago");
        dataItem.setRead(true);

        dataItemList.add(dataItem);

        dataItem = new DataItem();
        dataItem.setId(3);
        dataItem.setMessage("This is test Notification 3");
        dataItem.setCreatedAt("2 mins ago");
        dataItem.setRead(true);

        dataItemList.add(dataItem);

        dataItem = new DataItem();
        dataItem.setId(4);
        dataItem.setMessage("This is test Notification 4");
        dataItem.setCreatedAt("4 mins ago");
        dataItem.setRead(true);

        dataItemList.add(dataItem);

        notification.setData(dataItemList);
        return notification;
    }
}
