package com.test.wllnon.sirenxinyi.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.test.wllnon.sirenxinyi.fragment.BaseTabFragment;

import java.util.List;

/**
 * Created by Administrator on 2016/2/21.
 */
public class TabFragmentPagerAdapter extends FragmentPagerAdapter {
    private List<BaseTabFragment> tabFragmentList;
    public TabFragmentPagerAdapter(FragmentManager fragmentManager,
                                   List<BaseTabFragment> fragmentList) {
        super(fragmentManager);
        tabFragmentList = fragmentList;
    }

    @Override
    public Fragment getItem(int position) {
        if (tabFragmentList != null)
            return tabFragmentList.get(position);
        return null;
    }

    @Override
    public int getCount() {
        if (tabFragmentList != null)
            return tabFragmentList.size();
        return 0;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        BaseTabFragment fragment = tabFragmentList.get(position);
        if (fragment != null) {
            return fragment.getTitle();
        }
        return "";
    }
}
