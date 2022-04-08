
package com.codebee.tradethrust.model.login;

import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class LoginUserData {

    @SerializedName("data")
    private com.codebee.tradethrust.model.login.Data Data;

    public com.codebee.tradethrust.model.login.Data getData() {
        return Data;
    }

    public void setData(com.codebee.tradethrust.model.login.Data data) {
        Data = data;
    }

}
