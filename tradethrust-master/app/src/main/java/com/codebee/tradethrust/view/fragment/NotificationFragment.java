package com.codebee.tradethrust.view.fragment;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codebee.tradethrust.R;
import com.codebee.tradethrust.model.Tab;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by csangharsha on 5/17/18.
 */

public class NotificationFragment extends android.support.v4.app.Fragment {

    @BindView(R.id.viewpager)
    public ViewPager viewPager;

    @BindView(R.id.tabs)
    public TabLayout tabLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.notification_fragment,
                container, false);

        ButterKnife.bind(this, view);

        setupViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);

        return view;
    }

    private void setupViewPager(ViewPager viewPager) {

        List<String> tabs = getTabs();

        if(tabs.size() > 5 ){
            tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        }else {
            tabLayout.setTabMode(TabLayout.MODE_FIXED);
        }

        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());
        for(String tab:tabs) {
            Bundle bundle=new Bundle();

            NotificationTabFragment notificationTabFragment = new NotificationTabFragment();
            notificationTabFragment.setArguments(bundle);
            adapter.addFragment(notificationTabFragment, tab);
        }
        viewPager.setAdapter(adapter);
    }

    private List<String> getTabs() {
        List<String> tabs = new ArrayList<>();
        tabs.add("All");
//        tabs.add("Comment");
        return tabs;
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

}
