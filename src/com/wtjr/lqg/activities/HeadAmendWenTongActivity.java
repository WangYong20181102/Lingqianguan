package com.wtjr.lqg.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wtjr.lqg.R;
import com.wtjr.lqg.basecommonly.BaseActivity;
import com.wtjr.lqg.constants.Constant;
import com.wtjr.lqg.fragments.HeadChangeGirlFragment;
import com.wtjr.lqg.fragments.HeadChangeManFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Myf on 2016/12/6.
 */

public class HeadAmendWenTongActivity extends BaseActivity implements ViewPager.OnPageChangeListener, View.OnClickListener {
    /**
     * 返回按钮
     *
     * @param savedInstanceState
     */
    private ImageView imageBack;
    /**
     * 设置标题栏背景色
     */
    private RelativeLayout rlTitle;
    /**
     * 标题
     *
     * @param savedInstanceState
     */
    private TextView textTittle;

    /**
     * 底部小圆点（left）
     *
     * @param savedInstanceState
     */
    private RadioButton imageBottomPointLeft;
    /**
     * 底部确认按钮
     */
    private static Button btnSure;
    private static MyPagerAdapter adapter;

    private ArrayList<Fragment> baseActivityList = new ArrayList<Fragment>();
    /**
     * 底部小圆点（右）
     *
     * @param savedInstanceState
     */
    private RadioButton imageBottomPointRight;
    private ViewPager viewPager;
    private HeadChangeGirlFragment changeGirlFragment;
    private HeadChangeManFragment changeManFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.head_wentong_activity_layout);
        start();
    }

    @Override
    public void setTitle() {
        imageBack.setVisibility(View.VISIBLE);
        imageBack.setImageResource(R.drawable.back2);
        textTittle.setText("更换头像");
        textTittle.setTextColor(Color.BLACK);
        rlTitle.setBackgroundColor(Color.WHITE);
    }


    @Override
    public void initData() {
        btnSure.setEnabled(false);
        changeGirlFragment = new HeadChangeGirlFragment();
        changeManFragment = new HeadChangeManFragment();
        baseActivityList.add(changeGirlFragment);
        baseActivityList.add(changeManFragment);
        adapter = new MyPagerAdapter(getSupportFragmentManager(), baseActivityList);
        viewPager.setAdapter(adapter);

    }


    @Override
    public void findViewById() {
        imageBack = (ImageView) findViewById(R.id.imgBtn_back);
        textTittle = (TextView) findViewById(R.id.tv_title_name);
        imageBottomPointLeft = (RadioButton) findViewById(R.id.image_point_state_left);
        imageBottomPointRight = (RadioButton) findViewById(R.id.image_point_state_right);
        rlTitle = (RelativeLayout) findViewById(R.id.rl_title);
        viewPager = (ViewPager) findViewById(R.id.change_head_viewpager);
        btnSure = (Button) findViewById(R.id.btn_head_bottom);
    }

    @Override
    public void setListener() {
        imageBack.setOnClickListener(this);
        viewPager.setOnPageChangeListener(this);
        btnSure.setOnClickListener(this);
    }

    /**
     * 广播更新ui
     */
    public static void updateData() {
        if (app.mAccountData.headPortraitUrl != null && app.mAccountData.headPortraitUrl.length() <= 2) {
            if (Constant.SELECT == Integer.parseInt(app.mAccountData.headPortraitUrl)) {
                btnSure.setEnabled(false);
            } else {
                btnSure.setEnabled(true);
            }
        } else {
            btnSure.setEnabled(true);
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onPageScrolled(int i, float v, int i1) {

    }

    @Override
    public void onPageSelected(int i) {
        if (i == 0) {
            imageBottomPointLeft.setChecked(true);
            imageBottomPointRight.setChecked(false);
        } else {
            imageBottomPointLeft.setChecked(false);
            imageBottomPointRight.setChecked(true);
        }
    }

    @Override
    public void onPageScrollStateChanged(int i) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imgBtn_back:  //返回按钮
                finish();
                break;
            case R.id.btn_head_bottom:      //底部确认按钮
//                if(Constant.SELECT != -1){//此时证明已经选择图片了
                Intent intent = new Intent();
                setResult(RESULT_OK, intent);
                finish();
//                }else{
//                    DialogUtil.promptDialog(this, DialogUtil.HEAD_BAND_BANK, getString(R.string.please_select_head_portrait));
//                }
                break;
        }
    }

    class MyPagerAdapter extends FragmentPagerAdapter {

        private List<Fragment> baseActivityList;

        public MyPagerAdapter(FragmentManager fragmentManager, ArrayList<Fragment> baseActivityList) {
            super(fragmentManager);
            this.baseActivityList = baseActivityList;
        }


        @Override
        public Fragment getItem(int i) {
            return baseActivityList.get(i);
        }

        @Override
        public int getCount() {
            return baseActivityList.size();
        }
    }
}
