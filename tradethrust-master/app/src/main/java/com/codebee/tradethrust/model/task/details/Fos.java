
package com.codebee.tradethrust.model.task.details;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Fos implements Serializable {

    @SerializedName("age")
    private Long Age;
    @SerializedName("contact_number")
    private String ContactNumber;
    @SerializedName("contact_person_name")
    private String ContactPersonName;
    @SerializedName("email")
    private String Email;
    @SerializedName("fos_name")
    private String FosName;
    @SerializedName("id")
    private Long Id;

    public Long getAge() {
        return Age;
    }

    public void setAge(Long age) {
        Age = age;
    }

    public String getContactNumber() {
        return ContactNumber;
    }

    public void setContactNumber(String contactNumber) {
        ContactNumber = contactNumber;
    }

    public String getContactPersonName() {
        return ContactPersonName;
    }

    public void setContactPersonName(String contactPersonName) {
        ContactPersonName = contactPersonName;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getFosName() {
        return FosName;
    }

    public void setFosName(String fosName) {
        FosName = fosName;
    }

    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }

}
