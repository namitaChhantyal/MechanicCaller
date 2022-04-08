package com.codebee.tradethrust.view.acitivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.codebee.tradethrust.R;
import com.codebee.tradethrust.dao.AccountDAO;
import com.codebee.tradethrust.dao.impl.AccountDAOImpl;
import com.codebee.tradethrust.utils.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ChangePhoneNumberActivity extends BaseActivity {

    @BindView(R.id.phone_number_edit_text)
    public EditText phoneNumberEditText;

    private AccountDAO accountDAO = new AccountDAOImpl();

    @Override
    protected int getContentView() {
        return R.layout.activity_change_phone_number;
    }

    @Override
    protected void onViewReady(Bundle savedInstanceState, Intent intent) {
        ButterKnife.bind(this);
    }

    @Override
    protected boolean showActionBar() {
        return true;
    }

    @OnClick(R.id.save_phone_btn)
    public void savePhoneNumber(View view) {
        if( accountDAO.savePhoneNumber(phoneNumberEditText.getText().toString()) ){
            super.onBackPressed();
        }
    }
}
