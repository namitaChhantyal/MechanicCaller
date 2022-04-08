package com.codebee.tradethrust.model.task.list.group_by_bit;

import com.codebee.tradethrust.model.task.details.Bit;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class BitFromDetails {

    @SerializedName("data")
    private List<Bit> bitList;

    public List<Bit> getBitList() {
        return bitList;
    }

    public void setBitList(List<Bit> bitList) {
        this.bitList = bitList;
    }
}
