package com.dullyoung.tools;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.nio.Buffer;

import butterknife.ButterKnife;

/*
 * Created by　Dullyoung on 2021/1/19
 */
public abstract class BaseActivity extends AppCompatActivity {
    protected abstract int getLayoutId();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        ButterKnife.bind(this);
        init();
    }

    protected abstract void init();
}
