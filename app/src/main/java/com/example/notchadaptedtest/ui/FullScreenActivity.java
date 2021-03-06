package com.example.notchadaptedtest.ui;

import android.annotation.TargetApi;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.DisplayCutout;
import android.view.View;
import android.view.WindowInsets;
import android.view.WindowManager;

import com.example.notchadaptedtest.utils.HwNotchUtils;
import com.example.notchadaptedtest.utils.RomUtils;
import com.example.notchadaptedtest.utils.XiaomiNotchUtils;

import java.util.List;

/**
 * @description: 全屏显示页面
 * @author: zhukai
 * @date: 2019/3/9 15:58
 */
public class FullScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            // Android P利用官方提供的API适配
            WindowManager.LayoutParams lp = getWindow().getAttributes();
            // 始终允许窗口延伸到屏幕短边上的缺口区域
            lp.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
            getWindow().setAttributes(lp);
        } else {
            // Android P以下根据手机厂商的适配方案进行适配
            if (RomUtils.isHuawei() && HwNotchUtils.hasNotch(this)) {
                HwNotchUtils.setFullScreenWindowLayoutInDisplayCutout(getWindow());
            } else if (RomUtils.isXiaomi() && XiaomiNotchUtils.hasNotch(this)) {
                XiaomiNotchUtils.setFullScreenWindowLayoutInDisplayCutout(getWindow());
            }
        }
    }

    /**
     * 获得刘海区域信息
     */
    @TargetApi(28)
    public void getNotchParams() {
        final View decorView = getWindow().getDecorView();
        if (decorView != null) {
            decorView.post(new Runnable() {
                @Override
                public void run() {
                    WindowInsets windowInsets = decorView.getRootWindowInsets();
                    if (windowInsets != null) {
                        // 当全屏顶部显示黑边时，getDisplayCutout()返回为null
                        DisplayCutout displayCutout = windowInsets.getDisplayCutout();
                        Log.e("TAG", "安全区域距离屏幕左边的距离 SafeInsetLeft:" + displayCutout.getSafeInsetLeft());
                        Log.e("TAG", "安全区域距离屏幕右部的距离 SafeInsetRight:" + displayCutout.getSafeInsetRight());
                        Log.e("TAG", "安全区域距离屏幕顶部的距离 SafeInsetTop:" + displayCutout.getSafeInsetTop());
                        Log.e("TAG", "安全区域距离屏幕底部的距离 SafeInsetBottom:" + displayCutout.getSafeInsetBottom());
                        // 获得刘海区域
                        List<Rect> rects = displayCutout.getBoundingRects();
                        if (rects == null || rects.size() == 0) {
                            Log.e("TAG", "不是刘海屏");
                        } else {
                            Log.e("TAG", "刘海屏数量:" + rects.size());
                            for (Rect rect : rects) {
                                Log.e("TAG", "刘海屏区域：" + rect);
                            }
                        }
                    }
                }
            });
        }
    }
}
