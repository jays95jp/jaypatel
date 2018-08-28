package com.storypreview;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class HomeViewPagerAdapter extends FragmentStatePagerAdapter {
    private List<Fragment> mFragmentList = new ArrayList<>();
    private List<String> mTitleList = new ArrayList<>();
    private Fragment primaryItem;
    private Boolean visible = null;

    public HomeViewPagerAdapter(FragmentManager manager) {
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
        mTitleList.add(title);
    }

    public void removeFragment(int position) {
        mFragmentList.remove(getItem(position));
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTitleList.get(position);
    }

    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        super.setPrimaryItem(container, position, object);
        if (object instanceof Fragment) primaryItem = (Fragment) object;
        if (primaryItem != null) {
            if (visible != null) {
                primaryItem.setUserVisibleHint(visible);
                visible = null;
            }
        }
    }

    public void setUserVisibleHint(boolean visible) {
        if (primaryItem != null) {
            primaryItem.setUserVisibleHint(visible);
            this.visible = null;
        } else {
            this.visible = visible;
        }
    }

    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

}