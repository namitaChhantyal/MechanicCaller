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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.codebee.tradethrust.R;
import com.codebee.tradethrust.application.TradeThrustApplication;
import com.codebee.tradethrust.model.group_by_pos.DataItem;
import com.codebee.tradethrust.model.group_by_pos.POSGroupList;
import com.codebee.tradethrust.network.GroupByAPI;
import com.codebee.tradethrust.utils.ThrustConstant;
import com.codebee.tradethrust.view.acitivity.POSTaskDetailsActivity;
import com.codebee.tradethrust.view.adapter.GroupByPOSListAdapter;
import com.codebee.tradethrust.view.interfaces.OnCloseGroupByFragmentListener;
import com.codebee.tradethrust.view.interfaces.OnGroupPosClickedListener;

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

public class GroupByPOSFragment extends Fragment implements OnGroupPosClickedListener {

    private static final String TAG = "log";

    @BindView(R.id.pos_recycler_view)
    public RecyclerView recyclerView;

    @BindView(R.id.swiperefresh)
    public SwipeRefreshLayout swipeRefreshLayout;

    @BindView(R.id.pos_list_container)
    public LinearLayout posListContainer;

    @BindView(R.id.progressBar_container)
    public LinearLayout progressBarContainer;

    @BindView(R.id.no_item_found_text_view)
    public TextView noItemFoundTextView;

    @BindView(R.id.no_item_found_container)
    public LinearLayout noItemFoundContainer;

    @BindView(R.id.filter_type_text_view)
    public TextView filterTypeTextView;

    private SharedPreferences preferences;
    private Retrofit retrofit;
    private List<DataItem> POSList;
    private GroupByPOSListAdapter adapter;
    private OnCloseGroupByFragmentListener onCloseGroupByFragmentListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.group_by_fragment,
                container, false);

        ButterKnife.bind(this, view);

        TradeThrustApplication application = (TradeThrustApplication) getActivity().getApplicationContext();
        preferences = application.getPreferences();
        retrofit = application.getRetrofit();

        String groupByText;

        if(getArguments().getInt("groupType") == ThrustConstant.GROUP_CATEGORY_TYPE_POS) {
            groupByText = "Grouped By POS";
        }else {
            groupByText = "Grouped By Bit";
        }

        filterTypeTextView.setText(groupByText);

        hideAllView();

        setAdapter();

        getPOSList();

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // cancel the Visual indication of a refresh
                swipeRefreshLayout.setRefreshing(false);

                getPOSList();
            }
        });

        return view;
    }

    private void hideAllView() {
        recyclerView.setVisibility(View.GONE);
        progressBarContainer.setVisibility(View.GONE);
        noItemFoundTextView.setVisibility(View.GONE);
    }

    private void getPOSList() {

        hideAllView();

        progressBarContainer.setVisibility(View.VISIBLE);

        Map<String, String> headersInfo = getHeaderInfo();

        Map<String, String> queryParams = new HashMap<>();

        if(getArguments().getInt("groupType") == ThrustConstant.GROUP_CATEGORY_TYPE_POS) {
            queryParams.put("fos_id", preferences.getString(ThrustConstant.FOS_ID, "0"));
        }else if(getArguments().getInt("groupType") == ThrustConstant.GROUP_CATEGORY_TYPE_BIT){
            queryParams.put("bit_id", String.valueOf(getArguments().getLong("bitId")));
        }

        GroupByAPI groupByAPI = retrofit.create(GroupByAPI.class);
        Call<POSGroupList> posGroupListCall = groupByAPI.getGroupByPOSList(queryParams, headersInfo);

        posGroupListCall.enqueue(new Callback<POSGroupList>() {
            @Override
            public void onResponse(Call<POSGroupList> call, Response<POSGroupList> response) {
                POSGroupList posGroupList = response.body();
                if(posGroupList != null) {
                    POSList.clear();
                    POSList.addAll(posGroupList.getData());

                    hideAllView();

                    if (POSList.size() > 0) {
                        recyclerView.setVisibility(View.VISIBLE);
                        adapter.notifyDataSetChanged();
                    } else {
                        noItemFoundTextView.setVisibility(View.VISIBLE);
                    }

                    adapter.notifyDataSetChanged();

                }else {
                    Toast.makeText(getContext(), "Response not found.", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<POSGroupList> call, Throwable t) {
                Toast.makeText(getContext(), "Something went wrong.", Toast.LENGTH_SHORT).show();
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
        headersInfo.put("serializer", "Mobile::PosProfileSerializer");
        return headersInfo;
    }

    private void setAdapter() {
        POSList = new ArrayList<>();
        adapter = new GroupByPOSListAdapter(getContext(), POSList, this);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onGroupPOSClicked(DataItem item) {
        Intent intent = new Intent(getContext(), POSTaskDetailsActivity.class);
        intent.putExtra("posId", item.getId());
        intent.putExtra("pos_name", item.getName());
        intent.putExtra("status", item.getStatus());
        intent.putExtra("created_at", item.getCreatedBy());
        startActivity(intent);
    }

    public void setOnCloseGroupByFragmentListener(OnCloseGroupByFragmentListener onCloseGroupByFragmentListener) {
        this.onCloseGroupByFragmentListener = onCloseGroupByFragmentListener;
    }

    @OnClick(R.id.close_detail_btn)
    public void closebtn() {
        onCloseGroupByFragmentListener.onCloseFragment(this);
    }
}
