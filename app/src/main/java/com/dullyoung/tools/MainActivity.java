package com.dullyoung.tools;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.SeekBar;

import androidx.core.app.ActivityCompat;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity {

    @BindView(R.id.sb_light)
    SeekBar mSbLight;
    @BindView(R.id.ll_root)
    LinearLayout mLlRoot;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void init() {
        if (checkSelfPermission(Manifest.permission.WRITE_SETTINGS) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.WRITE_SETTINGS,
                    Manifest.permission.WRITE_SECURE_SETTINGS
            }, 1024);
            Log.i("aaaa", "requestPermissions: ");
        }
        int light = 0;
        try {
            light = Settings.System.getInt(getContentResolver(), Settings.System.SCREEN_BRIGHTNESS);
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }
        if (light != 0) {
            light = light / 255 * 100;
            mSbLight.setProgress(light);
        }

        mSbLight.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    saveBrightness(MainActivity.this, progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        RingView ringView = new RingView(this);
        ringView.setData(new int[]{0xffff9b27, 0xffff0025, 0xff000000, 0xff663000},
                new float[]{0.2f, 0.2f, 0.3f, 0.3f}, 40);
        ringView.setBackgroundColor(Color.TRANSPARENT);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(300, 300);
        ringView.setLayoutParams(layoutParams);
        mLlRoot.addView(ringView);
        ringView.startAnim(6000);
    }

    public void saveBrightness(Activity activity, int brightness) {
        brightness = brightness * 255 / 100;
        Uri uri = Settings.System.getUriFor(Settings.System.SCREEN_BRIGHTNESS);
        Log.i("aaaa", "saveBrightness: " + brightness);

        Settings.System.putInt(getContentResolver(), Settings.System.SCREEN_BRIGHTNESS, brightness);
        activity.getContentResolver().notifyChange(uri, null);
    }


}