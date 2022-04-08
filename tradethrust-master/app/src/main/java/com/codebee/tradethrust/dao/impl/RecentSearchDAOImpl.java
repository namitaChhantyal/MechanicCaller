package com.codebee.tradethrust.dao.impl;

import com.codebee.tradethrust.dao.RecentSearchDAO;
import com.codebee.tradethrust.model.RecentSearch;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by csangharsha on 5/18/18.
 */

public class RecentSearchDAOImpl implements RecentSearchDAO {

    private static List<RecentSearch> recentSearchList = new ArrayList<>();

    public RecentSearchDAOImpl() {
        setUpData();
    }

    private void setUpData() {
        recentSearchList.add(new RecentSearch(1, "Ramesh & Sons Traders"));
        recentSearchList.add(new RecentSearch(2, "Hanuman Traders"));
    }

    @Override
    public List<RecentSearch> getAllRecentSearch() {
        return recentSearchList;
    }
}
