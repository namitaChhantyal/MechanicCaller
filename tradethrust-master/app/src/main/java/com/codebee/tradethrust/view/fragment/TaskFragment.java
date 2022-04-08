package com.codebee.tradethrust.view.fragment;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.codebee.tradethrust.R;
import com.codebee.tradethrust.model.Tab;
import com.codebee.tradethrust.utils.ThrustConstant;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by csangharsha on 5/17/18.
 */

public class TaskFragment extends Fragment {

    @BindView(R.id.viewpager)
    public ViewPager viewPager;

    @BindView(R.id.tabs)
    public TabLayout tabLayout;

    @BindView(R.id.filter_btn)
    public ImageButton filterBtn;

    @BindView(R.id.filter_type_text_view)
    public TextView filterTypeTextView;

    @BindView(R.id.task_group_btn_container)
    public LinearLayout taskGroupBtnContainer;

    private OnGroupBtnClickedListener onGroupBtnClickedListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.task_fragment,
                container, false);

        ButterKnife.bind(this, view);

        setupViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);

        return view;
    }

    public void setOnGroupBtnClickedListener(OnGroupBtnClickedListener onGroupBtnClickedListener) {
        this.onGroupBtnClickedListener = onGroupBtnClickedListener;
    }

    private void setupViewPager(ViewPager viewPager) {

        List<Tab> tabs = getTabs();

        if(tabs.size() > 5 ){
            tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        }else {
            tabLayout.setTabMode(TabLayout.MODE_FIXED);
        }

        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());

        for(int i = 0; i<tabs.size(); i++) {

            Tab tab = tabs.get(i);

            Bundle bundle=new Bundle();
            bundle.putString("taskTypeCode", tab.getCode());

            TaskTabFragment taskTabFragment = new TaskTabFragment();
            taskTabFragment.setArguments(bundle);
            adapter.addFragment(taskTabFragment, tab.getName());
        }
        viewPager.setAdapter(adapter);
    }

    @OnClick(R.id.filter_btn)
    public void filterDropdown(){
        PopupMenu popup = new PopupMenu(getContext(), filterBtn);
        //Inflating the Popup using xml file
        popup.getMenuInflater()
                .inflate(R.menu.task_filter_menu, popup.getMenu());

        //registering popup with OnMenuItemClickListener
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {

                Bundle bundle = new Bundle();

                switch (item.getItemId()) {
                    case R.id.group_task_by_pos:
                        bundle.putInt("groupType", ThrustConstant.GROUP_CATEGORY_TYPE_POS);
                        break;
                    case R.id.group_task_by_bit:
                        bundle.putInt("groupType", ThrustConstant.GROUP_CATEGORY_TYPE_BIT);
                        break;
                }

                onGroupBtnClickedListener.onGroupClicked(bundle);

                return true;

            }
        });

        popup.show(); //showing popup menu
    }

    private List<Tab> getTabs() {
        List<Tab> tabs = new ArrayList<>();
        tabs.add(new Tab(ThrustConstant.TASK_STATUS_NEW, "New"));
        tabs.add(new Tab(ThrustConstant.TASK_STATUS_IN_PROGRESS, "In Progress"));
        tabs.add(new Tab(ThrustConstant.TASK_STATUS_COMPLETED, "Completed"));
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

    public interface OnGroupBtnClickedListener {
        void onGroupClicked(Bundle bundle);
    }
}
