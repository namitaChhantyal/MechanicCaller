package com.codebee.tradethrust.model;

/**
 * Created by csangharsha on 5/18/18.
 */

public class RecentSearch {

    private int id;
    private String keyword;

    public RecentSearch(int id, String keyword) {
        this.id = id;
        this.keyword = keyword;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }
}
