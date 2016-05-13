package com.android.olivia.chatwithus.app;

import android.content.Context;
import android.graphics.Rect;
import android.view.View;
import android.widget.RelativeLayout;

/**
 * Created by olivia on 5/4/16.
 */
public class SizeUpdateRelativeLayout extends RelativeLayout {

    public abstract interface SizeNotifierRelativeLayout {
        public abstract void onSizeChanged(int keyboardHeight);
    }

    public SizeUpdateRelativeLayout(Context context) {
        super(context);
    }

    public SizeUpdateRelativeLayout(android.content.Context context, android.util.AttributeSet attrs) {
        super(context, attrs);
    }

    public SizeUpdateRelativeLayout(android.content.Context context, android.util.AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    private Rect rect = new Rect();
    public SizeNotifierRelativeLayout notifier;

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if (notifier != null) {
            View rootView = this.getRootView();
            int usableViewHeight = rootView.getHeight() - Utils.getmStatusBarHeight() - Utils.getViewInset(rootView);
            this.getWindowVisibleDisplayFrame(rect);
            int keyboardHeight = usableViewHeight - (rect.bottom - rect.top);
            notifier.onSizeChanged(keyboardHeight);
        }
    }
}
