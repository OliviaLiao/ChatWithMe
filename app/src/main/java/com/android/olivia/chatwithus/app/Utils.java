package com.android.olivia.chatwithus.app;

import android.graphics.Rect;
import android.os.Build;
import android.view.View;

import java.lang.reflect.Field;

/**
 * Created by olivia on 5/4/16.
 */
public class Utils {
    private static int mStatusBarHeight = 0;

    public static void setStatusBarHeight(int height) {
        mStatusBarHeight = height;
    }

    public static int getmStatusBarHeight() {
        return mStatusBarHeight;
    }

    public static int getViewInset(View view) {
        if (view == null || Build.VERSION.SDK_INT < 21) {
            return 0;
        }
        try {
            Field mAttachInfoField = View.class.getDeclaredField("mAttachInfo");
            mAttachInfoField.setAccessible(true);
            Object mAttachInfo = mAttachInfoField.get(view);
            if (mAttachInfo != null) {
                Field mStableInsetsField = mAttachInfo.getClass().getDeclaredField("mStableInsets");
                mStableInsetsField.setAccessible(true);
                Rect insets = (Rect) mStableInsetsField.get(mAttachInfo);
                return insets.bottom;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
}
