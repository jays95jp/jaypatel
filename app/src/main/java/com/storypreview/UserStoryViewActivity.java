package com.storypreview;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;

import com.storypreview.databinding.ActivityStoryViewBinding;

import java.util.ArrayList;

public class UserStoryViewActivity extends BaseActivity implements OnUserStoryFinish {

    public static final String ARG_BUNDLE_KEY_SCREEN_TAG = "screen_tag";
    private static final String TAG = "UserStoryViewActivity";
    public boolean isPlayStory = true;
    private int storyPosition = 0;

    private ArrayList<Fragment> fragmentArrayList;
    private ActivityStoryViewBinding activityStoryPreviewBinding;
    //    private PagerChangeInterface pagerChangeInterface;
    private int currentPosition = 0;
    private int currentClickPositionFromIntent = 0;
    private boolean isCallFromDashUserstory = false;

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
            fragmentArrayList.add(new ProfileStoryPreviewFragment());
        }

        int totalUser = fragmentArrayList.size();

        HomeViewPagerAdapter homeViewPagerAdapter = new HomeViewPagerAdapter(getSupportFragmentManager());

        for (int indexOfUser = 0; indexOfUser < totalUser; indexOfUser++) {
            homeViewPagerAdapter.addFragment(fragmentArrayList.get(indexOfUser), String.valueOf(indexOfUser));
        }

        activityStoryPreviewBinding.viewPager.setAdapter(homeViewPagerAdapter);
        activityStoryPreviewBinding.viewPager.setCurrentItem(currentClickPositionFromIntent, true);
//        pagerChangeInterface = (PagerChangeInterface) fragmentArrayList.get(currentClickPositionFromIntent);


        activityStoryPreviewBinding.viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                Log.d(TAG, "onPageScrolled: " + position);
            }

            @Override
            public void onPageSelected(int position) {

                currentPosition = position;
//                pagerChangeInterface = (PagerChangeInterface) fragmentArrayList.get(currentPosition);
                Log.d(TAG, "onPageSelected: " + currentPosition);

            }

            @Override
            public void onPageScrollStateChanged(int state) {
                switch (state) {
                    case 0:
                        isPlayStory = true;
//                        pagerChangeInterface.selectPagerFragment(true, false, UserStoryViewActivity.this.storyPosition);
                        break;
                    case 1:
                    case 2:
                        isPlayStory = false;
//                        pagerChangeInterface.selectPagerFragment(false, false, UserStoryViewActivity.this.storyPosition);
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
                    if (currentPosition == 1 && isCallFromDashUserstory) {
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

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra(ARG_BUNDLE_KEY_SCREEN_TAG, UserStoryViewActivity.class.getSimpleName());
        setResult(RESULT_OK, intent);
        super.onBackPressed();
    }

}