package com.codebee.tradethrust.view.acitivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.codebee.tradethrust.R;
import com.codebee.tradethrust.model.task.details.Bit;
import com.codebee.tradethrust.utils.BaseActivity;
import com.codebee.tradethrust.utils.BottomNavigationViewHelper;
import com.codebee.tradethrust.utils.ThrustConstant;
import com.codebee.tradethrust.utils.Utils;
import com.codebee.tradethrust.view.fragment.AccountFragment;
import com.codebee.tradethrust.view.fragment.GroupByBitFragment;
import com.codebee.tradethrust.view.fragment.GroupByPOSFragment;
import com.codebee.tradethrust.view.fragment.NotificationFragment;
import com.codebee.tradethrust.view.fragment.SearchFragment;
import com.codebee.tradethrust.view.fragment.TaskFragment;
import com.codebee.tradethrust.view.interfaces.OnBitRelatedPOSOpenedListener;
import com.codebee.tradethrust.view.interfaces.OnCloseGroupByFragmentListener;
import com.codebee.tradethrust.view.interfaces.OnGroupPosClickedListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DashboardActivity extends BaseActivity implements TaskFragment.OnGroupBtnClickedListener, OnBitRelatedPOSOpenedListener, OnCloseGroupByFragmentListener {

    public Fragment taskFragment;
    public Fragment searchFragment;
    public Fragment notificationFragment;
    public Fragment accountFragment;

    public Fragment groupByFragment;


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_task:
                    if(manager.getBackStackEntryCount() > 1) {
                        manager.popBackStack();
                    }
                    setFragment(taskFragment, true, "taskFragment");
                    return true;
                case R.id.create_task:
                    Intent intent = new Intent(DashboardActivity.this, TaskFormActivity.class);
                    startActivity(intent);
                    return true;
                case R.id.navigation_notification:
                    setFragment(notificationFragment, "notificationFragment");
                    return true;
                case R.id.navigation_account:
                    setFragment(accountFragment, "accountFragment");
                    return true;
            }
            return false;
        }

    };
    private FragmentManager manager;

    @Override
    protected int getContentView() {
        return R.layout.activity_dashboard;
    }

    @Override
    protected void onViewReady(Bundle savedInstanceState, Intent intent) {

        ButterKnife.bind(this);

        manager = getSupportFragmentManager();

        taskFragment = new TaskFragment();
        ((TaskFragment)taskFragment).setOnGroupBtnClickedListener(this);

        searchFragment = new SearchFragment();
        notificationFragment = new NotificationFragment();
        accountFragment = new AccountFragment();

        setFragment(taskFragment, true, "taskFragment");

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        BottomNavigationViewHelper.removeShiftMode(navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        Utils.statusCheck(this);

    }

    public void setFragment(Fragment fragment, boolean addToBackStack, String tag){
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.content, fragment, tag);
        if(addToBackStack) {
            transaction.addToBackStack(tag);
        }
        transaction.commit();
    }

    private void setFragment(Fragment fragment, String tag) {
        setFragment(fragment, false, tag);
    }

    @Override
    public void onGroupClicked(Bundle bundle) {

        String tag = "";
        Fragment groupByFragment = null;
        if(bundle.getInt("groupType") == ThrustConstant.GROUP_CATEGORY_TYPE_POS) {
            groupByFragment = new GroupByPOSFragment();
            ((GroupByPOSFragment) groupByFragment).setOnCloseGroupByFragmentListener(this);
            groupByFragment.setArguments(bundle);
            tag = "dashboardGroupByPOSFragment";

        }else if(bundle.getInt("groupType") == ThrustConstant.GROUP_CATEGORY_TYPE_BIT) {
            groupByFragment = new GroupByBitFragment();
            ((GroupByBitFragment) groupByFragment).setOnBitRelatedPOSOpenedListener(this);
            ((GroupByBitFragment) groupByFragment).setOnCloseGroupByFragmentListener(this);
            groupByFragment.setArguments(bundle);
            tag = "dashboardGroupByBITFragment";

        }
        if(groupByFragment != null) {
            setFragment(groupByFragment, true, tag);
        }
    }

    @Override
    public void onBitRelatedPOSOpened(Bit bit) {
        Fragment groupByFragment = new GroupByPOSFragment();
        ((GroupByPOSFragment) groupByFragment).setOnCloseGroupByFragmentListener(this);
        Bundle bundle = new Bundle();
        bundle.putInt("groupType", ThrustConstant.GROUP_CATEGORY_TYPE_BIT);
        bundle.putLong("bitId", bit.getId());
        groupByFragment.setArguments(bundle);
        setFragment(groupByFragment, true, "dashboardGroupByPOSFragment");
    }

    @Override
    public void onCloseFragment(Fragment fragment) {
        super.onBackPressed();
    }
}