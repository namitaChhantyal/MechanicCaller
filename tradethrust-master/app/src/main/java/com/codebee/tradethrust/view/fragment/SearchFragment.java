package com.codebee.tradethrust.view.fragment;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codebee.tradethrust.R;
import com.codebee.tradethrust.dao.RecentSearchDAO;
import com.codebee.tradethrust.dao.impl.RecentSearchDAOImpl;
import com.codebee.tradethrust.model.RecentSearch;
import com.codebee.tradethrust.view.adapter.RecentSearchListAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by csangharsha on 5/17/18.
 */

public class SearchFragment extends android.support.v4.app.Fragment {

    @BindView(R.id.recent_search_recycler_view)
    public RecyclerView recentSearchRecyclerView;

    @BindView(R.id.search_result_recycler_view)
    public RecyclerView searchResultRecyclerView;

    private RecentSearchListAdapter recentSearchListAdapter;
    private List<RecentSearch> recentSearchList;

    private RecentSearchDAO recentSearchDAO = new RecentSearchDAOImpl();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.search_fragment,
                container, false);

        ButterKnife.bind(this, view);

        setAdapter();

        getAllRecentSearch();

        return view;
    }

    private void getAllRecentSearch() {
        List<RecentSearch> recentSearchListTemp = recentSearchDAO.getAllRecentSearch();
        recentSearchList.clear();
        recentSearchList.addAll(recentSearchListTemp);
        recentSearchListAdapter.notifyDataSetChanged();
    }

    private void setAdapter() {
        recentSearchList = new ArrayList<>();
        recentSearchListAdapter = new RecentSearchListAdapter(getActivity(), recentSearchList);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recentSearchRecyclerView.setLayoutManager(mLayoutManager);
        recentSearchRecyclerView.setItemAnimator(new DefaultItemAnimator());
        recentSearchRecyclerView.setAdapter(recentSearchListAdapter);
    }

}
