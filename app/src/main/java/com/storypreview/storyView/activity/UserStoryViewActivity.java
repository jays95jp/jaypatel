package com.storypreview.storyView.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;

import com.storypreview.R;
import com.storypreview.adapter.HomeViewPagerAdapter;
import com.storypreview.base.BaseActivity;
import com.storypreview.databinding.ActivityStoryViewBinding;
import com.storypreview.interfaceStory.OnUserStoryFinish;
import com.storypreview.interfaceStory.PagerChangeInterface;
import com.storypreview.storyView.fragment.ProfileStoryPreviewFragment;

import java.util.ArrayList;

public class UserStoryViewActivity extends BaseActivity implements OnUserStoryFinish {

    private static final String TAG = "UserStoryViewActivity";
    public boolean isPlayStory = true;
    int position = 0;
    private ArrayList<Fragment> fragmentArrayList;
    private ActivityStoryViewBinding activityStoryPreviewBinding;
    private int currentPosition = 0;
    private PagerChangeInterface pagerChangeInterface;

    @Override
    public int getLayoutResId() {
        return R.layout.activity_story_view;
    }

    @Override
    public void init() {
        activityStoryPreviewBinding = (ActivityStoryViewBinding) getBindObject();
        fragmentArrayList = new ArrayList<>();
        setStoryPager();
    }

    private void setStoryPager() {


        for (int storiesResultBean = 0; storiesResultBean < 4; storiesResultBean++) {
            Bundle bundle = new Bundle();
            bundle.putBoolean("select", storiesResultBean == 0);

            ProfileStoryPreviewFragment profileStoryPreviewFragment = new ProfileStoryPreviewFragment();
            profileStoryPreviewFragment.setArguments(bundle);
            profileStoryPreviewFragment.setInterFace(this);
            fragmentArrayList.add(profileStoryPreviewFragment);
        }

        int totalUser = fragmentArrayList.size();

        HomeViewPagerAdapter homeViewPagerAdapter = new HomeViewPagerAdapter(getSupportFragmentManager());
        for (int indexOfUser = 0; indexOfUser < totalUser; indexOfUser++) {
            homeViewPagerAdapter.addFragment(fragmentArrayList.get(indexOfUser), String.valueOf(indexOfUser));
        }

        activityStoryPreviewBinding.viewPager.setAdapter(homeViewPagerAdapter);
        pagerChangeInterface = (PagerChangeInterface) fragmentArrayList.get(0);

        activityStoryPreviewBinding.viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                currentPosition = position;
                pagerChangeInterface = (PagerChangeInterface) fragmentArrayList.get(position);
            }

            @SuppressLint("LongLogTag")
            @Override
            public void onPageScrollStateChanged(int state) {

                switch (state) {
                    case 0:
                        isPlayStory = true;
                        pagerChangeInterface.selectPagerFragment(true, false);
                        break;
                    case 1:
                    case 2:
                        position = currentPosition;
                        isPlayStory = false;
                        pagerChangeInterface.selectPagerFragment(false, false);
                        break;

                }
            }

        });
    }

    @Override
    public void finishAllStory(boolean finish) {

        runOnUiThread(() -> {
            if (currentPosition < fragmentArrayList.size() - 1) {
                Log.d(TAG, "run: " + currentPosition);
                activityStoryPreviewBinding.viewPager.setCurrentItem(currentPosition + 1, true);
            } else
                onBackPressed();
        });
    }

    @Override
    public void previousStory() {
        runOnUiThread(() -> {
            if (currentPosition <= fragmentArrayList.size() - 1 && currentPosition != 0) {
                if (currentPosition == 1) {
                    onBackPressed();
                } else {
                    activityStoryPreviewBinding.viewPager.setCurrentItem(currentPosition - 1, true);
                }
            } else {
                onBackPressed();
            }
        });
    }

    @Override
    public void swipeToFinishStory() {
        onBackPressed();
    }

}