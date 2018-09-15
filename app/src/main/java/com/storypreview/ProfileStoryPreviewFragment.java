package com.storypreview;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.storypreview.databinding.FragmentProfilePreviewStoryBinding;

import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class ProfileStoryPreviewFragment extends BaseFragment implements View.OnTouchListener, PagerChangeInterface {
    private static final String TAG = "storyPreviewFragment";
    private FragmentProfilePreviewStoryBinding fragmentStoryViewBinding;
    private TimerTask childTaskProgress;
    private int childIndex = 0;
    private long downActionTime = 0L;
    private float yClickPositionDown;
    private float clickPosition;
    private ArrayList<SampleBean> beanArrayList;
    private OnUserStoryFinish userStoryFinish;
    private Boolean isSelect = false;
    private boolean isDown = false, isImageLoad = false;

    @Override
    public int getLayoutResId() {
        return R.layout.fragment_profile_preview_story;
    }

    public void setInterFace(OnUserStoryFinish onUserStoryFinish) {
        this.userStoryFinish = onUserStoryFinish;
    }

    @Override
    public void init() {
        fragmentStoryViewBinding = (FragmentProfilePreviewStoryBinding) getBindingObj();
        beanArrayList = new ArrayList<>();
        fetchSampleData();
        setListener();

        for (int i = 0; i < beanArrayList.size(); i++) {
            Picasso.with(getActivity()).load(beanArrayList.get(i).getUrl()).fetch();
        }

        if (getArguments() != null && getArguments().containsKey("select")) {
            isSelect = getArguments().getBoolean("select");
        }
        if (!isSelect) {
            Picasso.with(getActivity())
                    .load(R.mipmap.test_img)
                    .into(fragmentStoryViewBinding.imgStory, new Callback() {
                        @Override
                        public void onSuccess() {
                            isImageLoad = true;
                        }

                        @Override
                        public void onError() {
                        }
                    });
        }

    }

    private void fetchSampleData() {
/**
 *   sample.json file there for sample data
 */
        for (int i = 0; i < 3; i++) {
            SampleBean sampleBean = new SampleBean();
            sampleBean.setUrl("https://s3-us-west-2.amazonaws.com/spinach-cafe/thumbnails/Medium/main-image/20180725193344429.jpg");
            beanArrayList.add(sampleBean);
        }
    }

    private void loadStories(int index) {
        Observable.fromCallable(() -> true)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(aBoolean -> {
                    Picasso.with(activity)
//                            .load(beanArrayList.get(childIndex).getUrl())
                            .load(R.mipmap.test_img)
                            .into(fragmentStoryViewBinding.imgStory, new Callback() {
                                @Override
                                public void onSuccess() {
                                    isImageLoad = true;

                                    if (isSelect)
                                        Observable.just(true)
                                                .delay(500, TimeUnit.MILLISECONDS)
                                                .subscribe(bool -> callNextStoryViewCounter());

                                }

                                @Override
                                public void onError() {
                                    childIndex++;
                                    loadStories(childIndex);

                                }
                            });
                });
    }

    private void callNextStoryViewCounter() {
        int[] progress = {0};
        stopStory();
        if (activity == null)
            return;
        if (childIndex >= beanArrayList.size() || childIndex >= fragmentStoryViewBinding.lnyContainer.getChildCount()) {
            setProgressBar(fragmentStoryViewBinding.lnyContainer.getChildCount() - 1, 0);
            userStoryFinish.finishAllStory(true);
            /**
             *  here need to call next user story
             */
            return;
        }
        progress[0] = ((ProgressBar) fragmentStoryViewBinding.lnyContainer.getChildAt(childIndex)).getProgress();
        childTaskProgress = new TimerTask() {
            @Override
            public void run() {
                if (activity == null) {
                    stopStory();
                    return;
                }
                progress[0] = ((ProgressBar) fragmentStoryViewBinding.lnyContainer.getChildAt(childIndex)).getProgress() + 1;
                ((ProgressBar) fragmentStoryViewBinding.lnyContainer.getChildAt(childIndex)).setProgress(progress[0]);
                if (progress[0] == 100) {
                    stopStory();
                    progress[0] = 0;
                    if (childIndex < beanArrayList.size()) {
                        Observable.fromCallable(() -> true)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(aBoolean -> loadStories(childIndex));
                    } else if (childIndex == beanArrayList.size() - 1) {
                        userStoryFinish.finishAllStory(true);
                        setProgressBar(0, 0);
                        return;
                    }
                    childIndex++;
                }
            }
        };
        try {
            Timer timer = new Timer();
            timer.schedule(childTaskProgress, new Date(), 30);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    private void nextPreviousStory(float xPosition) {

        float screenWidth = 1000;
        if (xPosition > screenWidth / 2) {
            Log.e(TAG, "nextPreviousStory: 1 " + childIndex);
            if (childIndex == beanArrayList.size() - 1) {
                Log.e(TAG, "nextPreviousStory: 2 " + childIndex);
                setProgressBar(childIndex, 0);
                userStoryFinish.finishAllStory(true);
                return;
            }
            if (childIndex != beanArrayList.size() - 1) {
                Log.e(TAG, "nextPreviousStory: 3 " + childIndex);
                stopStory();
                setProgressBar(childIndex, 100);
                childIndex++;
                Log.e(TAG, "nextPreviousStory: 4 " + childIndex);
                loadStories(childIndex);
            }
        }
//        Left swipe
        else/* if (xPosition < screenWidth / 4)*/ {
            if (childIndex != -1) {
                Log.e(TAG, "nextPreviousStory: 5 " + childIndex);
                stopStory();

                if (childIndex == 0) {
                    setProgressBar(childIndex, 0);
                    userStoryFinish.previousStory();
                    return;
                }
                setProgressBar(childIndex, 0);
                childIndex--;
                Log.e(TAG, "nextPreviousStory: 6 " + childIndex);
                if (childIndex != -1) {
                    Log.e(TAG, "nextPreviousStory: 7 " + childIndex);
                    setProgressBar(childIndex, 0);
                } else {
                    childIndex++;
                    Log.e(TAG, "nextPreviousStory: 8 " + childIndex);
                }
                Log.e(TAG, "nextPreviousStory: 9 " + childIndex);
                loadStories(childIndex);
            }
        }

    }


    private void setProgressBar(int index, int progress) {
        ProgressBar progressBar = ((ProgressBar) fragmentStoryViewBinding.lnyContainer.getChildAt(index));
        if (progressBar != null)
            progressBar.setProgress(progress);
    }

    private void stopStory() {
        if (childTaskProgress != null)
            childTaskProgress.cancel();
    }

    private void setListener() {
        fragmentStoryViewBinding.imgStory.setOnTouchListener(this);
    }

    private void addProgressBar() {
        fragmentStoryViewBinding.lnyContainer.removeAllViews();
        LinearLayout layout = new LinearLayout(activity);
        layout.setOrientation(LinearLayout.HORIZONTAL);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        for (int i = 0; i < beanArrayList.size(); i++) {
            ProgressBar progressBar = new ProgressBar(activity, null, android.R.attr.progressBarStyleHorizontal);
            progressBar.setMax(100);
            progressBar.setProgressDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.progress_drawable));
            params.setMargins(0, 0, 3, 0);
            params.weight = 1;
            View view = inflater != null ? progressBar : progressBar;
            fragmentStoryViewBinding.lnyContainer.addView(view, params);
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
/**
 *  Add this condition for manage seek bar touch and display touch
 */
        if (Math.abs(event.getY() - fragmentStoryViewBinding.lnyContainer.getY()) < 200) {
            return true;
        }

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                onPause();
                yClickPositionDown = event.getY();
                clickPosition = event.getX();
                downActionTime = System.currentTimeMillis();
                break;

            case MotionEvent.ACTION_MOVE:
                isDown = true;
                stopStory();
                break;

            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                float yClickPositionUp = event.getY();
                long upTime = System.currentTimeMillis();
                if (Math.abs(yClickPositionUp) - Math.abs(yClickPositionDown) >= 2 * 100) {
                    stopStory();
                    activity.finish();
                    return false;
                }
                long LIMIT_TIMER = 500L;
                if (LIMIT_TIMER > (upTime - downActionTime)) {
                    isDown = false;
                    nextPreviousStory(clickPosition);
                    return false;
                }

                callNextStoryViewCounter();
                break;
            default:
                break;

        }
        return true;
    }

    @Override
    public void onDestroy() {
        stopStory();
        super.onDestroy();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (childTaskProgress == null)
            return;
        stopStory();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (fragmentStoryViewBinding.lnyContainer.getChildCount() == 0)
            addProgressBar();
        if (isSelect)
            loadStories(childIndex);

    }

    @Override
    public void selectPagerFragment(boolean isSelect, boolean isUserChange) {
        this.isSelect = isSelect;
        isDown = isSelect;
        if (isSelect && isImageLoad) {
            stopStory();
//            childIndex = 0;
            if (fragmentStoryViewBinding.lnyContainer.getChildCount() == 0) {
                addProgressBar();
                setListener();
                loadStories(childIndex);
            } else
                callNextStoryViewCounter();
            setListener();

        } else {
//            Log.e("childTaskProgress == null", "" + (childTaskProgress == null));
            if (childTaskProgress == null)
                return;
            stopStory();
        }
    }
}
