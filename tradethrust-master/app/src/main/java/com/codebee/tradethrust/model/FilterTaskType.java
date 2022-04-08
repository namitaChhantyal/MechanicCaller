package com.codebee.tradethrust.model;

import java.io.Serializable;

public class FilterTaskType implements Serializable {

    private int filterType;
    private int filterId;

    public FilterTaskType() {
    }

    public int getFilterType() {
        return filterType;
    }

    public void setFilterType(int filterType) {
        this.filterType = filterType;
    }

    public int getFilterId() {
        return filterId;
    }

    public void setFilterId(int filterId) {
        this.filterId = filterId;
    }
}
