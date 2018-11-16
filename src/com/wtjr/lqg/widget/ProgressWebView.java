package com.wtjr.lqg.widget;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ClipDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.webkit.WebView;
import android.widget.ProgressBar;

import com.wtjr.lqg.R;

/**
 * 带进度条的WebView
 * 
 * @author 农民伯伯
 * @see http://www.cnblogs.com/over140/archive/2013/03/07/2947721.html
 * 
 */
public class ProgressWebView extends WebView {

    private OnScrollChangedCallback mOnScrollChangedCallback;

    private ProgressBar progressbar;

    int aaaa = 0;

    int lastSize = 0;

    public ProgressWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        if (isInEditMode()) {
            return;
        }
        int dimension = (int) (context.getApplicationContext().getResources().getDimension(R.dimen.d4));
        progressbar = new ProgressBar(context, null, android.R.attr.progressBarStyleHorizontal);
        ClipDrawable d = new ClipDrawable(new ColorDrawable(Color.parseColor("#77dd55")),// #FFE000
                Gravity.LEFT, ClipDrawable.HORIZONTAL);
        progressbar.setProgressDrawable(d);

        progressbar.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, dimension, 0, 0));
        progressbar.setMax(100);
        progressbar.setProgress(1);
        progressbar.setVisibility(View.GONE);
        addView(progressbar);
        // setWebViewClient(new WebViewClient(){});
        setWebChromeClient(new WebChromeClient());
    }

    public class WebChromeClient extends android.webkit.WebChromeClient {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            if (progressbar.getVisibility() == GONE) {
                progressbar.setVisibility(View.VISIBLE);
            }

            if (newProgress == 100) {
                newProgress = 0;
                replyImageUp(lastSize, 100);
                lastSize = 0;
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        progressbar.setVisibility(View.GONE);
                    }
                }, 500);
            } else {
                if (progressbar.getVisibility() == GONE) {
                    progressbar.setVisibility(View.VISIBLE);
                }
                replyImageUp(lastSize, newProgress);
                lastSize = newProgress;
            }
            super.onProgressChanged(view, newProgress);
        }

    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        LayoutParams lp = (LayoutParams) progressbar.getLayoutParams();
        lp.x = l;
        lp.y = t;
        progressbar.setLayoutParams(lp);
        super.onScrollChanged(l, t, oldl, oldt);

        if (mOnScrollChangedCallback != null) {
            mOnScrollChangedCallback.onScroll(l - oldl, t);
        }

    }

    /**
     * 
     * @param lastSize
     *            小到大
     * @param newCurrent
     */
    private void replyImageUp(final int lastSize, final int newCurrent) {
        // 设置动画
        ValueAnimator anim = ObjectAnimator.ofFloat(0.0F, 1.0F).setDuration(500);
        anim.addUpdateListener(new AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float cVal = (Float) animation.getAnimatedValue();
                int a = (int) (lastSize + (newCurrent - lastSize) * cVal);
                progressbar.setProgress(a);
            }
        });
        anim.start();
    }

    public static interface OnScrollChangedCallback {
        public void onScroll(int dx, int dy);
    }

    public OnScrollChangedCallback getOnScrollChangedCallback() {
        return mOnScrollChangedCallback;
    }

    public void setOnScrollChangedCallback(final OnScrollChangedCallback onScrollChangedCallback) {
        mOnScrollChangedCallback = onScrollChangedCallback;
    }
}
