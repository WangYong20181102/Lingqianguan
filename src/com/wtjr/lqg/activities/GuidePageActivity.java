package com.wtjr.lqg.activities;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.wtjr.lqg.MainActivity;
import com.wtjr.lqg.R;
import com.wtjr.lqg.basecommonly.BaseActivity;
import com.wtjr.lqg.sharedpreferences.SaveFirstInState;
import com.wtjr.lqg.sharedpreferences.SaveLoginState;
import com.wtjr.lqg.sharedpreferences.ToggleGesturePasswordState;
import com.wtjr.lqg.utils.DensityUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by WangXu on 2017/3/21.
 */

public class GuidePageActivity extends BaseActivity implements ViewPager.OnPageChangeListener {
    private ViewPager vp;
    private ImageView btnJump;
    private List<Integer> drawList;
    private LinearLayout llPoints;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_guide_page);
        start();

        vp.setAdapter(new MyVpAdapter());

        initDots();
    }

    /**
     * 初始化dot
     */
    private void initDots() {
        llPoints.removeAllViews();
        for (int i = 0; i < drawList.size(); i++) {
            ImageView imageView = new ImageView(this);
            // 设置小圆点imageview的参数
//            imageView.setLayoutParams(new  LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(DensityUtils.dp2px(this,9), DensityUtils.dp2px(this,4));
            layoutParams.rightMargin = DensityUtils.dp2px(this,5);
            layoutParams.leftMargin = DensityUtils.dp2px(this,5);
            imageView.setLayoutParams(layoutParams);

            imageView.setBackgroundResource(R.drawable.guide_page_dot_state);
            llPoints.addView(imageView);
        }
        updateDots();
    }

    /**
     * 更新点
     */
    private void updateDots() {
        if (drawList.size() == 0) {
            return;
        }
        int currentPage = vp.getCurrentItem() % drawList.size();
        for (int i = 0; i < llPoints.getChildCount(); i++) {
            llPoints.getChildAt(i).setEnabled(i == currentPage);// 设置setEnabled为true的话
            // 在选择器里面就会对应的使用黄色颜色
        }
    }

    @Override
    public void findViewById() {
        vp = (ViewPager) findViewById(R.id.vp_guide_page);
        btnJump = (ImageView) findViewById(R.id.btn_jump);
        llPoints = (LinearLayout) findViewById(R.id.ll_points_guide_page);
    }

    @Override
    public void initData() {
        /**
         * 添加引导页图片
         */
        drawList = new ArrayList<>();
        drawList.add(R.drawable.guide1);
        drawList.add(R.drawable.guide2);
        drawList.add(R.drawable.guide3);
        drawList.add(R.drawable.guide4);
    }

    @Override
    public void setListener() {

        vp.addOnPageChangeListener(this);

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        updateDots();
        btnJump.setVisibility(View.GONE);
        if (position == drawList.size() - 1) {
            btnJump.setVisibility(View.VISIBLE);
            btnJump.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    goActivity();

                }
            });
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    /**
     * 引导页适配器
     */
    private class MyVpAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return drawList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {

            ImageView imageView = new ImageView(getApplicationContext());

            imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
            imageView.setImageResource(drawList.get(position));
            container.addView(imageView);
            return imageView;

        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }

    @Override
    public void goActivity() {
        if (SaveFirstInState.getFirstInState(app)) {// 是第一次使用（一直都没有登录过）
            startActivity(new Intent(GuidePageActivity.this, MainActivity.class));
        } else {// 不是第一次使用（有登录过）
            if (SaveLoginState.getLoginState(app)) {// 账号处于登录的状态
                // 得到手势开个存储的状态
                boolean gesturePassword = ToggleGesturePasswordState.getGesturePassword(app);
                // 如果不等于null就跳到解锁
                if (gesturePassword) {
                    startActivity(new Intent(GuidePageActivity.this, GestureVerifyActivity.class));
                } else {
                    startActivity(new Intent(GuidePageActivity.this, MainActivity.class));
                }
            } else {// 账号没处于登录的状态
                startActivity(new Intent(GuidePageActivity.this, HaveAcountsLoginActivity.class));
            }
        }
        finish();
    }
}
