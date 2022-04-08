package com.codebee.tradethrust.view.acitivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.codebee.tradethrust.R;
import com.codebee.tradethrust.application.TradeThrustApplication;
import com.codebee.tradethrust.model.ChangePasswordResponse;
import com.codebee.tradethrust.network.ChangePasswordAPI;
import com.codebee.tradethrust.utils.BaseActivity;
import com.codebee.tradethrust.utils.ThrustConstant;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ChangePasswordActivity extends BaseActivity {

    @BindView(R.id.password_textview )
    public EditText passwordTextView;

    @BindView(R.id.confirm_password_textview)
    public EditText confirmPasswordTextView;

    private SharedPreferences preferences;

    private Retrofit retrofit;


    @Override
    protected void onViewReady(Bundle savedInstanceState, Intent intent) {
        ButterKnife.bind(this);

        TradeThrustApplication tradeThrustApplication = (TradeThrustApplication) getApplicationContext();
        preferences = tradeThrustApplication.getPreferences();

        retrofit = tradeThrustApplication.getRetrofit();

    }

    @Override
    protected int getContentView() {
        return R.layout.activity_change_password;
    }

    @Override
    protected boolean showActionBar() {
        return true;
    }

    @OnClick(R.id.udpate_btn)
    public void updateBtn(View view) {
        showProgressDialog("Updating password...");

        String password = passwordTextView.getText().toString();
        String confirmPassword = confirmPasswordTextView.getText().toString();

        if(password.isEmpty() || confirmPassword.isEmpty()) {
            Toast.makeText(this, "Please fill all fields!!!", Toast.LENGTH_SHORT).show();
            return;
        }

        Map<String, String> body = new HashMap<>();
        body.put("password", password);
        body.put("password_confirmation",confirmPassword);

        ChangePasswordAPI changePasswordAPI = retrofit.create(ChangePasswordAPI.class);

        Call<ChangePasswordResponse> changePasswordResponseCall = changePasswordAPI.changePassword(preferences.getString(ThrustConstant.USER_ID, "0"), body, getHeaderInfo());

        changePasswordResponseCall.enqueue(new Callback<ChangePasswordResponse>() {
            @Override
            public void onResponse(Call<ChangePasswordResponse> call, Response<ChangePasswordResponse> response) {
                hideProgressDialog();
                ChangePasswordResponse changePasswordResponse = response.body();
                if(changePasswordResponse != null && changePasswordResponse.getMessage().equalsIgnoreCase("Operation Successfull")){
                    Toast.makeText(ChangePasswordActivity.this, "Password updated successfully!!!", Toast.LENGTH_SHORT).show();
                    logOut();
                }else {
                    Toast.makeText(ChangePasswordActivity.this, "Unable to update Password!!!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ChangePasswordResponse> call, Throwable t) {
                hideProgressDialog();
                Toast.makeText(ChangePasswordActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private Map<String, String> getHeaderInfo() {

        Map<String, String> headersInfo = new HashMap<>();
        headersInfo.put("access-token", preferences.getString(ThrustConstant.ACCESS_TOKEN, ""));
        headersInfo.put("client", preferences.getString(ThrustConstant.CLIENT, ""));
        headersInfo.put("uid", preferences.getString(ThrustConstant.UID, ""));
        headersInfo.put("Organization", preferences.getString(ThrustConstant.COMPANY_NAME, ""));
        headersInfo.put("Serializer", "Index::RecordSerializer");

        return headersInfo;
    }

    public void logOut() {

        String companyName = preferences.getString(ThrustConstant.COMPANY_NAME, "");

        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.putString(ThrustConstant.COMPANY_NAME, companyName);
        editor.commit();

        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();

    }
}
