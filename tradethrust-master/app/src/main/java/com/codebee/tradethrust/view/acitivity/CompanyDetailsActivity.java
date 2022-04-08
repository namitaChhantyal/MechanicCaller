package com.codebee.tradethrust.view.acitivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.codebee.tradethrust.R;
import com.codebee.tradethrust.application.TradeThrustApplication;
import com.codebee.tradethrust.utils.BaseActivity;
import com.codebee.tradethrust.utils.ThrustConstant;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CompanyDetailsActivity extends BaseActivity {

    private SharedPreferences preferences;

    @BindView(R.id.company_name_text_view)
    public EditText companyNameTextView;

    @Override
    protected void onViewReady(Bundle savedInstanceState, Intent intent) {

        ButterKnife.bind(this);

        TradeThrustApplication application = (TradeThrustApplication) getApplicationContext();

        preferences = this.getSharedPreferences("PrefName", Context.MODE_PRIVATE);
        preferences.getString(ThrustConstant.COMPANY_NAME, "");
        if(preferences.contains(ThrustConstant.COMPANY_NAME)) {
            if( !preferences.contains(ThrustConstant.AUTO_LOGIN) || !preferences.getBoolean(ThrustConstant.AUTO_LOGIN, false) ) {
                intent = new Intent(CompanyDetailsActivity.this, LoginActivity.class);
            }else {
                intent = new Intent(CompanyDetailsActivity.this, DashboardActivity.class);
            }
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }

        companyNameTextView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // If the event is a key-down event on the "enter" button
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    onCompanyNameRegistered();
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_company_details;
    }

    @OnClick(R.id.next_btn)
    public void nextBtn(View view) {
        onCompanyNameRegistered();
    }

    private void onCompanyNameRegistered() {
        String companyName = companyNameTextView.getText().toString();
        if( companyName != null && !companyName.isEmpty() ) {

            SharedPreferences.Editor editor = preferences.edit();
            editor.putString(ThrustConstant.COMPANY_NAME, companyName);
            editor.commit();

            Intent intent = new Intent(CompanyDetailsActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }
    }
}
