package com.test.wllnon.sirenxinyi.fragment;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.test.wllnon.sirenxinyi.R;
import com.test.wllnon.sirenxinyi.adapter.TabFragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/2/22.
 */
public class FriendFragment extends Fragment{
    private TabLayout tabLayout;

    private ViewPager viewPager;
    private TabFragmentPagerAdapter viewPagerAdapter;

    private List<BaseTabFragment> tabFragmentList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_friend, container, false);

        // Initial the tab fragments
        tabFragmentList = new ArrayList<>();
        tabFragmentList.add(new FriendFellowFragment());
        tabFragmentList.add(new FriendDoctorFragment());

        viewPager = (ViewPager) rootView.findViewById(R.id.viewpager_friend);
        viewPagerAdapter = new TabFragmentPagerAdapter(getChildFragmentManager(), tabFragmentList);
        viewPager.setAdapter(viewPagerAdapter);

        if (tabLayout != null) {
            tabLayout.setVisibility(View.VISIBLE);
            tabLayout.setupWithViewPager(viewPager);
        }
        return rootView;
    }

    public void setTabLayout(TabLayout tabLayout) {
        this.tabLayout = tabLayout;
    }
}
