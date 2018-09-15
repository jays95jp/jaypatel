package com.storypreview.base;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

public abstract class BaseActivity extends AppCompatActivity {

    private final String mTAG = BaseActivity.this.getClass().getName();
    private ViewDataBinding bindObject;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        bindObject = DataBindingUtil.setContentView(this, getLayoutResId());

        init();
    }


    public abstract int getLayoutResId();

    public abstract void init();

    public ViewDataBinding getBindObject() {
        return bindObject;
    }

}