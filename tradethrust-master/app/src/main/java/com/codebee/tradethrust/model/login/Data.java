
package com.codebee.tradethrust.model.login;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class Data {

    @SerializedName("email")
    private String Email;
    @SerializedName("id")
    private Long Id;
    @SerializedName("name")
    private String Name;
    @SerializedName("nickname")
    private String Nickname;
    @SerializedName("roles")
    private List<Role> Roles;
    @SerializedName("sign_in_count")
    private Long SignInCount;
    @SerializedName("fos_id")
    private Long FosId;

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
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

    public String getNickname() {
        return Nickname;
    }

    public void setNickname(String nickname) {
        Nickname = nickname;
    }

    public List<Role> getRoles() {
        return Roles;
    }

    public void setRoles(List<Role> roles) {
        Roles = roles;
    }

    public Long getSignInCount() {
        return SignInCount;
    }

    public void setSignInCount(Long signInCount) {
        SignInCount = signInCount;
    }

    public Long getFosId() {
        return FosId;
    }

    public void setFosId(Long fosId) {
        FosId = fosId;
    }
}
