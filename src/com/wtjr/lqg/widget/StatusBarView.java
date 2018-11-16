package com.wtjr.lqg.widget;

import org.xutils.x;

import com.wtjr.lqg.activities.LqgApplication;
import com.wtjr.lqg.utils.ScreenUtils;

import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * 状态栏
 * 
 * @author Myf
 * 
 */
public class StatusBarView extends View {

    private LqgApplication app;
    int mScreenWidth;
    int mStatusHeight;
    
    public StatusBarView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    public StatusBarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        if(isInEditMode()) {
            return;
        }
        
        app = (LqgApplication) x.app();
        mScreenWidth = app.mScreenWidth;
        mStatusHeight = app.mStatusHeight;
        
        if (!isInEditMode()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                setVisibility(View.VISIBLE);
            } else {
                setVisibility(View.GONE);
            }
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(mScreenWidth, mStatusHeight);
    }

}
