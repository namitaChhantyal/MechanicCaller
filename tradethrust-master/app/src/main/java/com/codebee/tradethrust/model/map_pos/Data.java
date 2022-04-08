
package com.codebee.tradethrust.model.map_pos;

import com.google.gson.annotations.SerializedName;

public class Data {

    @SerializedName("created_at")
    private String CreatedAt;
    @SerializedName("latitude")
    private String Latitude;
    @SerializedName("longitude")
    private String Longitude;
    @SerializedName("pos_latitude")
    private String PosLatitude;
    @SerializedName("pos_longitude")
    private String PosLongitude;
    @SerializedName("owner_name")
    private String OwnerName;
    @SerializedName("photo")
    private String Photo;
    @SerializedName("second_photo")
    private String SecondPhoto;
    @SerializedName("uid")
    private String Uid;

    public String getCreatedAt() {
        return CreatedAt;
    }

    public void setCreatedAt(String createdAt) {
        CreatedAt = createdAt;
    }

    public String getLatitude() {
        return Latitude;
    }

    public void setLatitude(String latitude) {
        Latitude = latitude;
    }

    public String getLongitude() {
        return Longitude;
    }

    public void setLongitude(String longitude) {
        Longitude = longitude;
    }

    public String getPosLatitude() {
        return PosLatitude;
    }

    public void setPosLatitude(String posLatitude) {
        PosLatitude = posLatitude;
    }

    public String getPosLongitude() {
        return PosLongitude;
    }

    public void setPosLongitude(String posLongitude) {
        PosLongitude = posLongitude;
    }

    public String getOwnerName() {
        return OwnerName;
    }

    public void setOwnerName(String ownerName) {
        OwnerName = ownerName;
    }

    public String getPhoto() {
        return Photo;
    }

    public void setPhoto(String photo) {
        Photo = photo;
    }

    public String getSecondPhoto() {
        return SecondPhoto;
    }

    public void setSecondPhoto(String secondPhoto) {
        SecondPhoto = secondPhoto;
    }

    public String getUid() {
        return Uid;
    }

    public void setUid(String uid) {
        Uid = uid;
    }

}
