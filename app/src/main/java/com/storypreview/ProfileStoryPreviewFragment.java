package com.storypreview;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.storypreview.databinding.FragmentProfilePreviewStoryBinding;

import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class ProfileStoryPreviewFragment extends BaseFragment implements View.OnTouchListener {
    private static final String TAG = "storyPreviewFragment";
    private FragmentProfilePreviewStoryBinding fragmentStoryViewBinding;
    private TimerTask childTaskProgress;
    private int childIndex = 0;
    private long downActionTime = 0L;
    private float yClickPositionDown;
    private float clickPosition;
    private Disposable subscribeLoadStory;
    private int period;


    @Override
    public int getLayoutResId() {
        return R.layout.fragment_profile_preview_story;
    }

    @Override
    public void init() {
        fragmentStoryViewBinding = (FragmentProfilePreviewStoryBinding) getBindingObj();
        setListener();
    }

    private void loadStories(int index) {
        subscribeLoadStory = Observable.fromCallable(() -> true)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(aBoolean -> {
                    Picasso.with(activity)
                            .load("https://s3-us-west-2.amazonaws.com/freewire-stage-images/Images/BodyPart_b94b0a77-799d-44a1-9a22-cbd94697ceb5.jpg")
                            .into(fragmentStoryViewBinding.imgStory, new Callback() {
                                @Override
                                public void onSuccess() {

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
        progress[0] = ((ProgressBar) fragmentStoryViewBinding.lnyContainer.getChildAt(0)).getProgress();
        childTaskProgress = new TimerTask() {
            @Override
            public void run() {
                if (activity == null) {
                    stopStory();
                    return;
                }
                setSeekBarEnable(true);
                progress[0] = ((ProgressBar) fragmentStoryViewBinding.lnyContainer.getChildAt(0)).getProgress() + 1;
                ((ProgressBar) fragmentStoryViewBinding.lnyContainer.getChildAt(0)).setProgress(progress[0]);
                if (progress[0] == 100) {
                    ((ProgressBar) fragmentStoryViewBinding.lnyContainer.getChildAt(0)).setProgress(0);
                    stopStory();
                    progress[0] = 0;
                    if (childIndex == 4 - 1) {

                        ((ProgressBar) fragmentStoryViewBinding.lnyContainer.getChildAt(0)).setProgress(0);
                        Observable.fromCallable(() -> true)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(aBoolean -> {
                                });
                        return;
                    }
                    childIndex++;
                }
            }
        };

        if (getActivity() != null) {
            period = 5000;
        }

    }


    private void nextPreviousStory(float xPosition) {

        //applyAnimation();
        float screenWidth = 3000;
        if (xPosition > screenWidth / 2) {
            Log.e(TAG, "nextPreviousStory: 1 " + childIndex);
            if (childIndex == 4) {
                Log.e(TAG, "nextPreviousStory: 2 " + childIndex);
                ((ProgressBar) fragmentStoryViewBinding.lnyContainer.getChildAt(0)).setProgress(0);
                return;
            }
            if (childIndex != 4 - 1) {
                Log.e(TAG, "nextPreviousStory: 3 " + childIndex);
                stopStory();

                ((ProgressBar) fragmentStoryViewBinding.lnyContainer.getChildAt(0)).setProgress(0);
                childIndex++;
                Log.e(TAG, "nextPreviousStory: 4 " + childIndex);
            }
        }
//        Left swipe
        else/* if (xPosition < screenWidth / 4)*/ {
            if (childIndex != -1) {
                Log.e(TAG, "nextPreviousStory: 5 " + childIndex);
                stopStory();

                if (childIndex == 0) {
                    ((ProgressBar) fragmentStoryViewBinding.lnyContainer.getChildAt(0)).setProgress(0);
                    return;
                }

                ((ProgressBar) fragmentStoryViewBinding.lnyContainer.getChildAt(0)).setProgress(0);
                childIndex--;
                Log.e(TAG, "nextPreviousStory: 6 " + childIndex);
                if (childIndex != -1) {
                    Log.e(TAG, "nextPreviousStory: 7 " + childIndex);
                    ((ProgressBar) fragmentStoryViewBinding.lnyContainer.getChildAt(0)).setProgress(0);
                } else {
                    childIndex++;
                    Log.e(TAG, "nextPreviousStory: 8 " + childIndex);
                }
                Log.e(TAG, "nextPreviousStory: 9 " + childIndex);

            }
        }


//        setBottomSheet();
    }

    private void stopStory() {
        if (childTaskProgress != null)
            childTaskProgress.cancel();
    }

    private void setListener() {
    }

    private void addProgressBar() {

        fragmentStoryViewBinding.lnyContainer.removeAllViews();
        LinearLayout layout = new LinearLayout(activity);
        layout.setOrientation(LinearLayout.HORIZONTAL);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ProgressBar seekBar = new ProgressBar(activity);
        View view = inflater != null ? seekBar : seekBar;
        params.weight = 1;
        seekBar = (ProgressBar) view;
        seekBar.setMax(100);
        fragmentStoryViewBinding.lnyContainer.addView(view, params);
        setSeekBarEnable(false);
    }

    private void setSeekBarEnable(boolean isEnable) {
        Observable.fromCallable(() -> isEnable)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(aBoolean -> fragmentStoryViewBinding.lnyContainer.getChildAt(0).setEnabled(aBoolean), Throwable::printStackTrace);
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

            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                float yClickPositionUp = event.getY();
                long upTime = System.currentTimeMillis();
                if (Math.abs(yClickPositionUp) - Math.abs(yClickPositionDown) >= 2 * 100) {

                    stopStory();
                    activity.finish();
                    return false;
                }
                long LIMIT_TIMER = 200L;
                if (LIMIT_TIMER > (upTime - downActionTime)) {
                    nextPreviousStory(clickPosition);
                    return false;
                }
                onResume();
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

        loadStories(childIndex);

    }
}
