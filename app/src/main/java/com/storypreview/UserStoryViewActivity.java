package com.storypreview;

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;

import com.storypreview.databinding.ActivityStoryViewBinding;

import java.util.ArrayList;

public class UserStoryViewActivity extends BaseActivity implements OnUserStoryFinish {

    private static final String TAG = "UserStoryViewActivity";
    public boolean isPlayStory = true;
    private ArrayList<Fragment> fragmentArrayList;
    private ActivityStoryViewBinding activityStoryPreviewBinding;
    private int currentPosition = 0;

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
            ProfileStoryPreviewFragment profileStoryPreviewFragment = new ProfileStoryPreviewFragment();
            profileStoryPreviewFragment.setInterFace(this);
            fragmentArrayList.add(profileStoryPreviewFragment);
        }

        int totalUser = fragmentArrayList.size();

        HomeViewPagerAdapter homeViewPagerAdapter = new HomeViewPagerAdapter(getSupportFragmentManager());

        for (int indexOfUser = 0; indexOfUser < totalUser; indexOfUser++) {
            homeViewPagerAdapter.addFragment(fragmentArrayList.get(indexOfUser), String.valueOf(indexOfUser));
        }

        activityStoryPreviewBinding.viewPager.setAdapter(homeViewPagerAdapter);
        activityStoryPreviewBinding.viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                currentPosition = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                switch (state) {
                    case 0:
                        isPlayStory = true;
                        break;
                    case 1:
                    case 2:
                        isPlayStory = false;
                        break;
                }
            }
        });
    }

    @Override
    public void finishAllStory(boolean finish) {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (currentPosition < fragmentArrayList.size() - 1) {
                    Log.d(TAG, "run: " + currentPosition);
                    activityStoryPreviewBinding.viewPager.setCurrentItem(currentPosition + 1, true);
                } else
                    onBackPressed();
            }
        });
    }

    @Override
    public void previousStory() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (currentPosition <= fragmentArrayList.size() - 1 && currentPosition != 0) {
                    if (currentPosition == 1) {
                        onBackPressed();
                    } else {
                        activityStoryPreviewBinding.viewPager.setCurrentItem(currentPosition - 1, true);
                    }
                } else {
                    onBackPressed();
                }
            }
        });
    }

    @Override
    public void swipeToFinishStory() {
        onBackPressed();
    }

}