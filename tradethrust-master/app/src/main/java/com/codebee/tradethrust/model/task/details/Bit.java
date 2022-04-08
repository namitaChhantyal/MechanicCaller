
package com.codebee.tradethrust.model.task.details;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class Bit implements Serializable{

    @SerializedName("area")
    private String Area;
    @SerializedName("center")
    private List<Double> Center;
    @SerializedName("geo_type")
    private String GeoType;
    @SerializedName("id")
    private Long Id;
    @SerializedName("name")
    private String Name;
    @SerializedName("pos_profiles_count")
    private Integer posProfileCount;

    public String getArea() {
        return Area;
    }

    public void setArea(String area) {
        Area = area;
    }

    public List<Double> getCenter() {
        return Center;
    }

    public void setCenter(List<Double> center) {
        Center = center;
    }

    public String getGeoType() {
        return GeoType;
    }

    public void setGeoType(String geoType) {
        GeoType = geoType;
    }

    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public Integer getPosProfileCount() {
        return posProfileCount;
    }

    public void setPosProfileCount(Integer posProfileCount) {
        this.posProfileCount = posProfileCount;
    }
}
