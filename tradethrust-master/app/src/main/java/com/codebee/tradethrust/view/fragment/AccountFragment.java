package com.codebee.tradethrust.view.fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.codebee.tradethrust.R;
import com.codebee.tradethrust.application.TradeThrustApplication;
import com.codebee.tradethrust.model.profile.Data;
import com.codebee.tradethrust.model.profile.Profile;
import com.codebee.tradethrust.network.ProfileAPI;
import com.codebee.tradethrust.utils.ThrustConstant;
import com.codebee.tradethrust.view.acitivity.ChangePasswordActivity;
import com.codebee.tradethrust.view.acitivity.LoginActivity;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Created by csangharsha on 5/17/18.
 */

public class AccountFragment extends android.support.v4.app.Fragment {

    public static DateFormat dateformat = new SimpleDateFormat("MMMM, yyyy");

    private SharedPreferences preferences;

    private Retrofit retrofit;

    @BindView(R.id.mark)
    public TextView markTextView;

    @BindView(R.id.name_text_view)
    public TextView nameTextView;

    @BindView(R.id.email_textview)
    public TextView emailTextView;

    @BindView(R.id.phone_textview)
    public TextView phoneTextView;

    @BindView(R.id.company_name_text_view)
    public TextView companyNameTextView;

    @BindView(R.id.company_address_text_view)
    public TextView companyAddressTextView;

    @BindView(R.id.company_phone_text_view)
    public TextView companyPhoneTextView;

    @BindView(R.id.role_text_view)
    public TextView roleTextView;

    @BindView(R.id.role_since_text_view)
    public TextView roleSinceTextView;

    @BindView(R.id.progressBar_container)
    public LinearLayout progressBar;

    @BindView(R.id.account_main_container)
    public LinearLayout accountMainContainer;

    @BindView(R.id.refresh_btn_container)
    public LinearLayout refreshBtnContainer;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.account_fragment,
                container, false);

        ButterKnife.bind(this, view);

        TradeThrustApplication tradeThrustApplication = (TradeThrustApplication) getActivity().getApplicationContext();
        preferences = tradeThrustApplication.getPreferences();

        retrofit = tradeThrustApplication.getRetrofit();

        progressBar.setVisibility(View.GONE);
        accountMainContainer.setVisibility(View.VISIBLE);
        refreshBtnContainer.setVisibility(View.GONE);
        getProfileData();

        return view;
    }

    private void getProfileData() {
        progressBar.setVisibility(View.VISIBLE);
        accountMainContainer.setVisibility(View.GONE);
        refreshBtnContainer.setVisibility(View.GONE);

        ProfileAPI profileAPI = retrofit.create(ProfileAPI.class);
        Call<Profile> profileCall = profileAPI.getProfileData(preferences.getString(ThrustConstant.FOS_ID, "0"), getHeaderInfo());
        profileCall.enqueue(new Callback<Profile>() {
            @Override
            public void onResponse(Call<Profile> call, Response<Profile> response) {
                Profile profile = response.body();
                progressBar.setVisibility(View.GONE);
                if(profile != null) {
                    setUserInfo(profile);
                    accountMainContainer.setVisibility(View.VISIBLE);
                }else {
                    Toast.makeText(getContext(), response.message(), Toast.LENGTH_SHORT).show();
                    accountMainContainer.setVisibility(View.GONE);
                    refreshBtnContainer.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<Profile> call, Throwable t) {
                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
                accountMainContainer.setVisibility(View.GONE);
                refreshBtnContainer.setVisibility(View.VISIBLE);
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

    private void setUserInfo(Profile profile) {
        Data data = profile.getData();
        markTextView.setText(String.valueOf(data.getFosName().charAt(0)));
        nameTextView.setText(data.getFosName());
        emailTextView.setText(data.getEmail());
        phoneTextView.setText(data.getContactNumber());
//        companyNameTextView.setText(user.getCompanyName());
//        companyAddressTextView.setText(user.getCompanyAddress());
//        companyPhoneTextView.setText("Phone: " + user.getCompanyPhoneNumber());
//        roleTextView.setText(user.getCurrentRole());
//        roleSinceTextView.setText(dateformat.format(user.getCurrentRoleSince()));
    }

    @OnClick(R.id.change_password_textview)
    public void changePassword(View view) {
        Intent intent = new Intent(getActivity(), ChangePasswordActivity.class);
        getActivity().startActivity(intent);
    }

    @OnClick(R.id.phone_textview)
    public void changePhoneNumber(View view) {
//        Intent intent = new Intent(getActivity(), ChangePhoneNumberActivity.class);
//        getActivity().startActivity(intent);
    }

    @OnClick(R.id.logout_text_view)
    public void logOut(View view) {

        String companyName = preferences.getString(ThrustConstant.COMPANY_NAME, "");

        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.putString(ThrustConstant.COMPANY_NAME, companyName);
        editor.commit();

        Intent intent = new Intent(getActivity(), LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        getActivity().finish();

    }

}
