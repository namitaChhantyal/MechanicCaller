package com.codebee.tradethrust.view.acitivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.codebee.tradethrust.R;
import com.codebee.tradethrust.application.TradeThrustApplication;
import com.codebee.tradethrust.model.login.LoginUserData;
import com.codebee.tradethrust.model.login.UserParam;
import com.codebee.tradethrust.network.LoginAPI;
import com.codebee.tradethrust.utils.BaseActivity;
import com.codebee.tradethrust.utils.ThrustConstant;
import com.codebee.tradethrust.utils.Utils;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends BaseActivity {

    private static final String TAG = "log";
    private SharedPreferences preferences;
    private OkHttpClient okHttpClient;
    private Gson gson;

    private boolean isPasswordShown = false;

    @BindView(R.id.email_textview)
    public EditText emailEditText;

    @BindView(R.id.password_textview)
    public EditText passwordEditText;

    @BindView(R.id.auto_login_btn)
    public CheckBox autoLoginBtn;

    @BindView(R.id.trade_thrust_login_logo)
    public TextView tradeThrustLoginLogo;

    @BindView(R.id.forget_password_text_view)
    public TextView forgetPasswordTextView;

    @Override
    protected void onViewReady(Bundle savedInstanceState, Intent intent) {
        ButterKnife.bind(this);

        TradeThrustApplication application = (TradeThrustApplication) getApplicationContext();

        preferences = application.getPreferences();
        okHttpClient = application.okHttpClient();
        gson = application.gson();

        String companyName = preferences.getString(ThrustConstant.COMPANY_NAME, "");

        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.putString(ThrustConstant.COMPANY_NAME, companyName);
        editor.commit();

        Utils.statusCheck(this);

        findViewById(R.id.login_container).getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {

                    @Override
                    public void onGlobalLayout() {

                        if (getIsKeyboardShown(findViewById(R.id.login_container).getRootView())) {
                            // keyboard visible
                            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) tradeThrustLoginLogo.getLayoutParams();
                            params.topMargin = (int) (80 * getResources().getDisplayMetrics().density);
                            forgetPasswordTextView.setVisibility(View.GONE);
                        } else {
                            // Keyboard not visible
                            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) tradeThrustLoginLogo.getLayoutParams();
                            params.topMargin = (int) (145 * getResources().getDisplayMetrics().density);
                            forgetPasswordTextView.setVisibility(View.VISIBLE);
                        }

                    }
                });

//        testCode();

    }

    private void testCode() {
        // For Sub-domain: codebee
//        emailEditText.setText("abcek.sth@gmail.com");
//        passwordEditText.setText("sekret@123");
//        String organizationName = "codebee";

        // For Sub-domain: beetle
//        emailEditText.setText("alex@beetle.com");
//        passwordEditText.setText("sekret@123");
//        String organizationName = "beetle";

        // For Sub-domain: samsung
//        emailEditText.setText("sushil.maharjan007@gmail.com");
//        passwordEditText.setText("12345678");
//        String organizationName = "samsung";

//        emailEditText.setText("niraj@tradethrust.com");
//        passwordEditText.setText("sekret@123");
//        String organizationName = "samsung";

        emailEditText.setText("avisek_sth@hotmail.com");
        passwordEditText.setText("12345678");
        String organizationName = "samsung";

        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(ThrustConstant.COMPANY_NAME, organizationName);
        editor.commit();
    }

    // CHECK IF THE KEYBOARD IS SHOWN
    private boolean getIsKeyboardShown(View rootView) {
        /*
         * 128dp = 32dp * 4, minimum button height 32dp and generic 4 rows soft
         * keyboard
         */
        final int SOFT_KEYBOARD_HEIGHT_DP_THRESHOLD = 128;

        Rect r = new Rect();
        rootView.getWindowVisibleDisplayFrame(r);
        DisplayMetrics dm = rootView.getResources().getDisplayMetrics();
        /*
         * heightDiff = rootView height - status bar height (r.top) - visible
         * frame height (r.bottom - r.top)
         */
        int heightDiff = rootView.getBottom() - r.bottom;
        /* Threshold size: dp to pixels, multiply with display density */
        boolean isKeyboardShown = heightDiff > SOFT_KEYBOARD_HEIGHT_DP_THRESHOLD
                * dm.density;

        return isKeyboardShown;
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_login;
    }

    @OnClick(R.id.signin_btn)
    public void signIn(View view) {

        userAuth();

    }

    private void userAuth() {
        Retrofit retrofit = getRetrofit();

        UserParam userParam = new UserParam();
        userParam.setEmail(emailEditText.getText().toString());
        userParam.setPassword(passwordEditText.getText().toString());

        if( (userParam.getEmail() == null || userParam.getPassword() == null) || (userParam.getEmail() != null && userParam.getEmail().isEmpty())
                || (userParam.getPassword() != null && userParam.getPassword().isEmpty()) ) {
            Toast.makeText(this, "Please Enter your Credentials", Toast.LENGTH_SHORT).show();
            return;
        }

        showProgressDialog("Authenticating...");

        Map<String, String> headerInfo = new HashMap<>();
        headerInfo.put("Organization", preferences.getString(ThrustConstant.COMPANY_NAME, ""));

        LoginAPI loginAPI = retrofit.create(LoginAPI.class);

        Call<LoginUserData> user = loginAPI.auth(userParam, headerInfo);

        user.enqueue(new Callback<LoginUserData>() {
            @Override
            public void onResponse(Call<LoginUserData> call, Response<LoginUserData> response) {

                LoginUserData loginUserData = response.body();

                if(loginUserData != null) {
                    Log.d(TAG, "onResponse: " + response.headers().get("access-token"));
                    Log.d(TAG, "onResponse: " + response.headers().get("uid"));
                    Log.d(TAG, "onResponse: " + response.headers().get("client"));

                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString(ThrustConstant.ACCESS_TOKEN, response.headers().get("access-token"));
                    editor.putString(ThrustConstant.UID, response.headers().get("uid"));
                    editor.putString(ThrustConstant.CLIENT, response.headers().get("client"));
                    editor.putString(ThrustConstant.FOS_ID, String.valueOf(loginUserData.getData().getFosId()));
                    editor.putString(ThrustConstant.USER_ID, String.valueOf(loginUserData.getData().getId()));

                    if (autoLoginBtn.isChecked()) {
                        editor.putBoolean(ThrustConstant.AUTO_LOGIN, true);
                    } else {
                        editor.putBoolean(ThrustConstant.AUTO_LOGIN, false);
                    }
                    editor.commit();

                    hideProgressDialog();

                    Intent intent = new Intent(LoginActivity.this, DashboardActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                }else {
                    hideProgressDialog();
                    Toast.makeText(LoginActivity.this, "Login Failed!!!", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<LoginUserData> call, Throwable t) {
                Toast.makeText(LoginActivity.this, "Something went wrong. Contact Administrator.", Toast.LENGTH_SHORT).show();
                hideProgressDialog();
            }
        });
    }

    private Retrofit getRetrofit() {

        String mBaseUrl = ThrustConstant.BASE_URL;

        return new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                .baseUrl(mBaseUrl)
                .client(okHttpClient)
                .build();
    }
}
