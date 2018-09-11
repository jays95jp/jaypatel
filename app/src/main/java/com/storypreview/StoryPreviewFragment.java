package com.storypreview;

import android.support.v4.app.Fragment;

public class StoryPreviewFragment extends Fragment  {

    /*private FragmentStoryViewBinding fragmentStoryViewBinding;
    private TimerTask childTaskProgress;
    private int childIndex, fragmentPosition;
    private boolean isDown = false, isImageLoad = false;
    private ArrayList<String> imageUrl;
    private boolean isSelect = false;
    private OnUserStoryFinish userStoryFinish;
    private long LIMIT_TIMER = 500L, downActionTime = 0L;

    public StoryPreviewFragment(OnUserStoryFinish userStoryFinish, int position) {
        this.userStoryFinish = userStoryFinish;
        fragmentPosition = position;
    }

    @Override
    public int getLayoutResId() {
        return R.layout.fragment_story_view;
    }

    @Override
    public void init() {

        fragmentStoryViewBinding = (FragmentStoryViewBinding) getBindingObj();
        fragmentStoryViewBinding.setStoryPreviewFragment(this);
        imageUrl = new ArrayList<>();

        imageUrl.add("http://simform.solutions/imagelisting/tmp/SethRogan.jpg");
        imageUrl.add("http://simform.solutions/imagelisting/tmp/Jose.jpg");
        imageUrl.add("http://simform.solutions/imagelisting/tmp/MorganFreeman.jpg");

        if (fragmentPosition == 0) {
            imageUrl.clear();
            imageUrl.add("https://s3-us-west-1.amazonaws.com/pro-manager/images/1510304421595.jpg");
            imageUrl.add("http://simform.solutions/imagelisting/tmp/Jose.jpg");
            imageUrl.add("http://simform.solutions/imagelisting/tmp/MorganFreeman.jpg");
        } else {
            imageUrl.clear();
            imageUrl.add("http://simform.solutions/imagelisting/tmp/Mary.jpg");
            imageUrl.add("http://simform.solutions/imagelisting/tmp/RainbowWeed.jpg");
            imageUrl.add("http://simform.solutions/imagelisting/tmp/KristinStewart.jpg");

        }

//        http://simform.solutions/imagelisting/tmp/Mary.jpg

//        imageUrl.add("https://s3-us-west-1.amazonaws.com/pro-manager/images/20171013191733718437585.jpg");
//        imageUrl.add("https://s3-us-west-1.amazonaws.com/pro-manager/images/20171013191733719729407.jpg");
//        imageUrl.add("http://simform.solutions/imagelisting/tmp/MorganFreeman.jpg");


        for (int i = 0; i < imageUrl.size(); i++) {
            Picasso.with(getActivity()).load(imageUrl.get(i)).fetch();
        }

        if (getArguments() != null && getArguments().containsKey("isSelect")) {
            isSelect = getArguments().getBoolean("isSelect");
        }
        if (!isSelect) {
            fragmentStoryViewBinding.progressBar.setVisibility(View.VISIBLE);
            Picasso.with(getActivity())
                    .load(imageUrl.get(0))
                    .into(fragmentStoryViewBinding.imgStory, new Callback() {
                        @Override
                        public void onSuccess() {
                            fragmentStoryViewBinding.progressBar.setVisibility(View.GONE);
                            isImageLoad = true;
                        }

                        @Override
                        public void onError() {
                            fragmentStoryViewBinding.progressBar.setVisibility(View.GONE);
                        }
                    });
        }
    }

    private void loadImage(int index) {

        if (index >= imageUrl.size())
            return;

        Handler uiHandler = new Handler(Looper.getMainLooper());
        uiHandler.post(new Runnable() {
            @Override
            public void run() {
                fragmentStoryViewBinding.progressBar.setVisibility(View.VISIBLE);
                Picasso.with(getActivity())
                        .load(imageUrl.get(index))
                        .into(fragmentStoryViewBinding.imgStory, new Callback() {
                            @Override
                            public void onSuccess() {
                                fragmentStoryViewBinding.progressBar.setVisibility(View.GONE);
                                isImageLoad = true;
                                new java.util.Timer().schedule(
                                        new java.util.TimerTask() {
                                            @Override
                                            public void run() {
                                                if (isSelect)
                                                    callnextImageViewCounter();
                                            }
                                        },
                                        500
                                );

                            }

                            @Override
                            public void onError() {
//                                fragmentStoryViewBinding.progressBar.setVisibility(View.GONE);

                                CommonUtils.toast(getActivity(), "Load story fail");
                                childIndex++;
                                loadImage(childIndex);

                            }
                        });
            }
        });
    }

    private void setListener() {
        fragmentStoryViewBinding.layoutConstrain.setOnTouchListener(this);
    }

    private void callnextImageViewCounter() {

        if (childIndex >= fragmentStoryViewBinding.lnyContainer.getChildCount())
            return;

        int[] progress = {0};

        stopStory();

        progress[0] = ((ProgressBar) fragmentStoryViewBinding.lnyContainer.getChildAt(childIndex)).getProgress();
        childTaskProgress = new TimerTask() {
            @Override
            public void run() {

                if (childIndex >= fragmentStoryViewBinding.lnyContainer.getChildCount())
                    return;
                if (getActivity() == null || progress[0] == 100) {
                    stopStory();
                    return;
                }
                progress[0]++;*//**//*

                ((ProgressBar) fragmentStoryViewBinding.lnyContainer.getChildAt(childIndex)).setProgress(progress[0]);
                if (progress[0] == 100) {
                    stopStory();
                    progress[0] = 0;

                    if (childIndex == imageUrl.size() - 1) {
                        userStoryFinish.finishAllStory(true);
                        ((ProgressBar) fragmentStoryViewBinding.lnyContainer.getChildAt(childIndex)).setProgress(0);

                       *//*
                       for (int i = 0; i < imageUrl.size(); i++) {
                            ((ProgressBar) fragmentStoryViewBinding.lnyContainer.getChildAt(i)).setProgress(0);
                            childIndex = 0;
                            Picasso.with(getActivity())
                                    .load(imageUrl.get(0))
                                    .into(fragmentStoryViewBinding.imgStory);
                        }
                        *//*

                        return;
                    }
                    childIndex++;
                    loadImage(childIndex);

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

    private void addProgressBar() {

        fragmentStoryViewBinding.lnyContainer.removeAllViews();
        for (int i = 0; i < imageUrl.size(); i++) {
            LinearLayout layout = new LinearLayout(getActivity());
            layout.setOrientation(LinearLayout.HORIZONTAL);

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    0, LinearLayout.LayoutParams.WRAP_CONTENT);

            if (i != imageUrl.size() - 1)
                params.setMargins(0, 0, 3, 0);
            params.weight = 1;
            ProgressBar progressBar = new ProgressBar(getActivity(), null, android.R.attr.progressBarStyleHorizontal);
            progressBar.setMax(100);
            progressBar.setProgressDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.drawable_progress_bar));

            fragmentStoryViewBinding.lnyContainer.addView(progressBar, params);
        }

    }

    private void nextPreviewsStory(float xPosition) {


        float screenWidth = CommonUtils.getDisplayWidth(getActivity());
//        float xPortion = (screenWidth * 0.4);
//        Right swipe
        if (xPosition > screenWidth / 2) {
            if (childIndex == imageUrl.size() - 1) {
                userStoryFinish.finishAllStory(true);
                return;
            }
            if (childIndex != imageUrl.size() - 1) {

                stopStory();
                ((ProgressBar) fragmentStoryViewBinding.lnyContainer.getChildAt(childIndex)).setProgress(100);
                childIndex++;
                loadImage(childIndex);
            }
        }
//        Left swipe
        else {
            if (childIndex != -1) {
                stopStory();
                ((ProgressBar) fragmentStoryViewBinding.lnyContainer.getChildAt(childIndex)).setProgress(0);
                childIndex--;
                if (childIndex != -1) {
                    ((ProgressBar) fragmentStoryViewBinding.lnyContainer.getChildAt(childIndex)).setProgress(0);
                } else {
                    childIndex++;
                }
                loadImage(childIndex);
            }
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        if (!isImageLoad) {
            return false;
        }

//   Condition for differentiate click and touch for pause or nex/previews
        float clickPosition = event.getX();

        switch (event.getAction()) {

            case MotionEvent.ACTION_DOWN:
                downActionTime = System.currentTimeMillis();

            case MotionEvent.ACTION_MOVE:

                isDown = true;
                stopStory();
                break;

            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:

                long upTime = System.currentTimeMillis();

                if (LIMIT_TIMER > (upTime - downActionTime)) {
                    isDown = false;
                    nextPreviewsStory(clickPosition);
                    return false;
                }

                if (isDown) {
                    isDown = false;
                    callnextImageViewCounter();
                }
                break;

            default:
//                Log.e("Action", "" + event.getAction());
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
        if (isSelect) {
            if (fragmentStoryViewBinding.lnyContainer.getChildCount() == 0) {

                setListener();
                loadImage(childIndex);
            } else
                callnextImageViewCounter();
        }
        if (fragmentStoryViewBinding.lnyContainer.getChildCount() == 0)
            addProgressBar();
    }

    @Override
    public void selectPagerFragment(boolean isSelect, boolean isUserChange) {

        *//*if (isUserChange && isSelect) {
            ((ProgressBar) fragmentStoryViewBinding.lnyContainer.getChildAt(childIndex)).setProgress(0);
        }*//*
        Log.e("IsSelect " + isSelect, "Position:" + fragmentPosition);
        Log.e("isUserChange " + isUserChange, "Position:" + fragmentPosition);

        this.isSelect = isSelect;
        isDown = isSelect;
        if (isSelect && isImageLoad) {
            stopStory();
//            childIndex = 0;
            if (fragmentStoryViewBinding.lnyContainer.getChildCount() == 0) {
                addProgressBar();
                setListener();
                loadImage(childIndex);
            } else
                callnextImageViewCounter();
            setListener();

        } else {
//            Log.e("childTaskProgress == null", "" + (childTaskProgress == null));
            if (childTaskProgress == null)
                return;
            stopStory();
        }
    }

    private void stopStory() {
        if (childTaskProgress != null)
            childTaskProgress.cancel();

//        timeCounter = null;
       *//*
       if (context instanceof UserStoryViewActivity) {
            ((UserStoryViewActivity) getActivity()).isPlayStory = false;
        }
        *//*
    }

    @Override
    public void selectPagerFragment(boolean isSelect, boolean isUserChange, int storyPosition) {

    }*/
}
