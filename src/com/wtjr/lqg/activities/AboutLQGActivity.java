package com.wtjr.lqg.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.wtjr.lqg.R;
import com.wtjr.lqg.basecommonly.BaseActivity;
import com.wtjr.lqg.constants.Constant;
import com.wtjr.lqg.sharedpreferences.SharedPreferencesUtil;
import com.wtjr.lqg.utils.RequestHttpPVPUStatisticsUtils;
import com.wtjr.lqg.utils.SystemUtil;
import com.wtjr.lqg.widget.AboutLQGLayoutItem;

/**
 * Created by WangYong on 2017/10/12.
 * 关于零钱罐
 */

public class AboutLQGActivity extends BaseActivity implements View.OnClickListener {
    /**
     * 标题名字
     */
    private TextView tvTitleName;
    /**
     * 返回的图片按钮
     */
    private ImageButton imgBtnBack;
    /**
     * 版本
     */
    private TextView tvVersionCode;
    /**
     * 官方介绍
     */
    private AboutLQGLayoutItem tvOfficialIntroduction;
    /**
     * 公司资质
     */
    private AboutLQGLayoutItem tvCompanyQualification;
    /**
     * 媒体报道
     */
    private AboutLQGLayoutItem tvMediaReports;
    /**
     * 零钱罐公益
     */
    private AboutLQGLayoutItem tvLqgGongYi;
    /**
     * 零钱罐故事
     */
    private AboutLQGLayoutItem tvLqgStory;
    /**
     * 零钱罐微信
     */
    private AboutLQGLayoutItem tvLqgWeChat;
    /**
     * 零钱罐微博
     */
    private AboutLQGLayoutItem tvLqgWeiBo;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_lqg);
        start();
    }

    @Override
    public void setTitle() {
        // 一开始的时候是隐藏的，因为不知道要图片还是文字，所以现在显示出来
        imgBtnBack.setVisibility(View.VISIBLE);
        // 设置名字为:零钱罐服务
        tvTitleName.setText(R.string.about_lqg);
    }

    @Override
    public void findViewById() {
        tvTitleName = (TextView) findViewById(R.id.tv_title_name);
        imgBtnBack = (ImageButton) findViewById(R.id.imgBtn_back);
        tvVersionCode = (TextView) findViewById(R.id.tv_version_code);
        tvOfficialIntroduction = (AboutLQGLayoutItem) findViewById(R.id.tv_official_introduction);
        tvCompanyQualification = (AboutLQGLayoutItem) findViewById(R.id.tv_company_qualification);
        tvMediaReports = (AboutLQGLayoutItem) findViewById(R.id.tv_media_reports);
        tvLqgGongYi = (AboutLQGLayoutItem) findViewById(R.id.tv_lqg_gongyi);
        tvLqgStory = (AboutLQGLayoutItem) findViewById(R.id.tv_lqg_story);
        tvLqgWeChat = (AboutLQGLayoutItem) findViewById(R.id.tv_lqg_wechat);
        tvLqgWeiBo = (AboutLQGLayoutItem) findViewById(R.id.tv_lqg_weibo);
    }

    @Override
    public void initData() {
        String oldVersion = SystemUtil.getAppVersionName(this);
        tvVersionCode.setText("当前版本：v" + oldVersion);
    }

    @Override
    public void setListener() {
        imgBtnBack.setOnClickListener(this);
        tvOfficialIntroduction.setOnClickListener(this);
        tvCompanyQualification.setOnClickListener(this);
        tvMediaReports.setOnClickListener(this);
        tvLqgGongYi.setOnClickListener(this);
        tvLqgStory.setOnClickListener(this);
        tvLqgWeChat.setOnClickListener(this);
        tvLqgWeiBo.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.imgBtn_back:  //返回
                finish();
                break;
            case R.id.tv_official_introduction: //官方介绍
                intent = new Intent(this, WebViewActivity.class);
                intent.putExtra("url", Constant.H5_WE);
                intent.putExtra("TitleName", "官方介绍");
                break;
            case R.id.tv_company_qualification: //公司资质
                intent = new Intent(this, WebViewActivity.class);
                intent.putExtra("url", Constant.H5_COMPANY);
                intent.putExtra("TitleName", "公司资质");
                break;
            case R.id.tv_media_reports: //媒体报道
                intent = new Intent(this, WebViewActivity.class);
                intent.putExtra("url", Constant.H5_MEDIAL);
                intent.putExtra("TitleName", "媒体报道");
                break;
            case R.id.tv_lqg_gongyi:    //零钱罐公益
                intent = new Intent(this, WebViewActivity.class);
                intent.putExtra("url", Constant.H5_ACCESSAPPINTERFACE_GOLQGWELFARE);
                intent.putExtra("TitleName", "零钱罐公益");
                break;
            case R.id.tv_lqg_story: //零钱罐故事
                intent = new Intent(this, WebViewActivity.class);
                intent.putExtra("url", Constant.H5_ACCESSAPPINTERFACE_GOLQGSTORY);
                intent.putExtra("TitleName", "零钱罐故事");
                break;
            case R.id.tv_lqg_wechat: //零钱罐微信
                intent = new Intent(AboutLQGActivity.this, WebViewActivity.class);
                intent.putExtra("url", Constant.H5_HFIVEPAGE + "?uid=" + mUid);
                intent.putExtra("TitleName", "零钱罐微信");
                break;
            case R.id.tv_lqg_weibo: //零钱罐微博
                String subLqgDomain = SharedPreferencesUtil.getPrefString(this, "subLqgDomain", "");
                RequestHttpPVPUStatisticsUtils.getInstance().requestType(this, mUid, 3, subLqgDomain);//统计次数
                intent = new Intent(AboutLQGActivity.this, WebViewActivity.class);
                intent.putExtra("url", Constant.H5_LQG_WEIBO);
                intent.putExtra("TitleName", "零钱罐微博");
                break;
        }
        if (intent != null) {
            startActivity(intent);
        }
    }
}
