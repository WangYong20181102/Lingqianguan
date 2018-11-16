package com.wtjr.lqg.activities;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.RotateAnimation;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.wtjr.lqg.R;
import com.wtjr.lqg.base.JSONBase;
import com.wtjr.lqg.base.TyjDetails;
import com.wtjr.lqg.basecommonly.BaseActivity;
import com.wtjr.lqg.constants.Constant;
import com.wtjr.lqg.enums.RefreshType;
import com.wtjr.lqg.utils.HttpUtil.HttpRequesListener;
import com.wtjr.lqg.widget.pullRefresh.PullToRefreshLayout;
import com.wtjr.lqg.widget.pullRefresh.PullToRefreshLayout.OnRefreshListener;
import com.wtjr.lqg.widget.pullRefresh.PullableExpandableListView;

import org.xutils.http.RequestParams;

import java.util.List;

/**
 * 体验金
 * 
 * @author Myf
 * 
 */
public class ExperienceMoneyActivity extends BaseActivity implements OnClickListener, HttpRequesListener, OnRefreshListener {
    /**
     * 标题名字
     */
    private TextView tvTitleName;
    /**
     * 返回的图片按钮
     */
    private ImageButton imgBtnBack;
    private RefreshType mRefreshType;

    private RadioGroup group1;
    private RadioGroup group2;
    /**
     * 体验金奖励
     */
    private RadioButton rdTyjAward;
    /**
     * 体验金投资
     */
    private RadioButton rdTyjInvestment;
    /**
     * 体验金到期
     */
    private RadioButton rdTyjExpire;
    /**
     * 全部
     */
    private RadioButton rbAll;
    /**
     * 标题菜单是否在显示
     */
    private boolean isTitleMenuShow = false;
    /**
     * 点击阴影，隐藏数据
     */
    private View viewHide;
    private PullToRefreshLayout pullToRefreshLayout;
    private PullableExpandableListView pullableExpandableListView;

    private LinearLayout ll_titleMenu;
    /**
     * 是否是第一次进入(为了避免第一次进入后箭头出现偏移旋转)
     */
    private boolean isFirst = true;

    /**
     * 标题图标
     */
    private ImageView imageTitle;

    @Override
    protected void onCreate(Bundle arg0) {
	super.onCreate(arg0);
	setContentView(R.layout.activity_experience_money);
	start();
    }

    @Override
    public void findViewById() {
	tvTitleName = (TextView) findViewById(R.id.tv_title_name);
	imgBtnBack = (ImageButton) findViewById(R.id.imgBtn_back);
	// 标题图标
	imageTitle = (ImageView) findViewById(R.id.image_title);

	viewHide = (View) findViewById(R.id.view_hide);

	ll_titleMenu = (LinearLayout) findViewById(R.id.ll_titleMenu);

	group1 = (RadioGroup) findViewById(R.id.radioGroup1);
	group2 = (RadioGroup) findViewById(R.id.radioGroup2);
	rdTyjAward = (RadioButton) findViewById(R.id.rd_tyj_award);
	rdTyjInvestment = (RadioButton) findViewById(R.id.rd_tyj_investment);
	rdTyjExpire = (RadioButton) findViewById(R.id.rd_tyj_expire);

	rbAll = (RadioButton) findViewById(R.id.rb_all);

	// 下拉刷新
	pullToRefreshLayout = (PullToRefreshLayout) findViewById(R.id.pullTo_refreshLayout);
	// 新建一个ExpandableListView
	pullableExpandableListView = (PullableExpandableListView) findViewById(R.id.pullable_Expandable_ListView);
    }

    @Override
    public void setListener() {
	// 返回按钮监听器
	imgBtnBack.setOnClickListener(this);
	setCheckedChangeListener();

	pullToRefreshLayout.setOnRefreshListener(this);
	tvTitleName.setOnClickListener(this);
	viewHide.setOnClickListener(this);
    }

    @Override
    public void setTitle() {
	// 设置名字为:体验金
	tvTitleName.setText(R.string.experience_money_name);
	httpUtil.setHttpRequesListener(this);
	// 一开始的时候是隐藏的，因为不知道要图片还是文字，所以现在显示出来
	imgBtnBack.setVisibility(View.VISIBLE);

	imageTitle.setVisibility(View.VISIBLE);
    }

    @Override
    public void initData() {
	sendHttpReques(RefreshType.RefreshNoPull, "");
	rbAll.setChecked(true);
    }

    @Override
    public void onClick(View v) {
	switch (v.getId()) {
	case R.id.imgBtn_back:// 返回按钮点击操作
	    if (isTitleMenuShow) {
		ll_titleMenu.setVisibility(View.GONE);
		isTitleMenuShow = false;
		showAnimation(imageTitle);
	    } else {
		finish();
	    }
	    break;
	case R.id.tv_title_name:// 点击标题文字
	case R.id.view_hide:// 点击标题后的箭头
	case R.id.image_title:// 点击标题后的箭头
	    showOrHideTitleMenu();
	    break;

	default:
	    break;
	}
    }

    /**
     * 显示或隐藏标题菜单
     */
    public void showOrHideTitleMenu() {
	showAnimation(imageTitle);
	if (isTitleMenuShow) {
	    ll_titleMenu.setVisibility(View.GONE);
	    isTitleMenuShow = false;

	} else {
	    ll_titleMenu.setVisibility(View.VISIBLE);
	    isTitleMenuShow = true;
	}
    }

    /**
     * 显示标题动画旋转
     * 
     * @param mView
     */
    public void showAnimation(View mView) {
	if (isFirst) {// 避免第一次进入后箭头出现偏移旋转
	    isFirst = false;
	    return;
	}

	final float centerX = mView.getWidth() / 2.0f;
	final float centerY = mView.getHeight() / 2.0f;
	// 这个是设置需要旋转的角度，我设置的是180度
	if (!isTitleMenuShow) {
	    RotateAnimation rotateAnimation = new RotateAnimation(0, 180, centerX, centerY);
	    rotateAnimation.setDuration(500);// 旋转时长
	    rotateAnimation.setFillAfter(true);
	    mView.startAnimation(rotateAnimation);
	} else {
	    RotateAnimation rotateAnimation = new RotateAnimation(180, 360, centerX, centerY);
	    rotateAnimation.setDuration(500);// 旋转时长
	    rotateAnimation.setFillAfter(true);
	    mView.startAnimation(rotateAnimation);
	}

    }

    private void setCheckedChangeListener() {
	group1.setOnCheckedChangeListener(new OnCheckedChangeListener() {

	    @Override
	    public void onCheckedChanged(RadioGroup group, int checkedId) {
		for (int i = 0; i < group.getChildCount(); i++) {
		    RadioButton childAt = (RadioButton) group.getChildAt(i);
		    if (childAt.isChecked()) {
			group2.clearCheck();
		    }
		}

		if (rdTyjAward.isChecked()) {// 体验金奖励
		    setSelect("1");
		} else if (rdTyjInvestment.isChecked()) {// 体验金投资
		    setSelect("2");
		} else if (rdTyjExpire.isChecked()) {// 体验金到期
		    setSelect("3");
		}
	    }
	});

	group2.setOnCheckedChangeListener(new OnCheckedChangeListener() {

	    @Override
	    public void onCheckedChanged(RadioGroup group, int checkedId) {
		for (int i = 0; i < group.getChildCount(); i++) {
		    RadioButton childAt = (RadioButton) group.getChildAt(i);
		    if (childAt.isChecked()) {
			group1.clearCheck();
		    }
		}
		if (rbAll.isChecked()) {// 全部
		    setSelect("");
		}
	    }
	});
    }

    /**
     * 设置选择项
     * 
     * @param dataType
     *            体验金奖励-->1,体验金投资-->2,体验金到期-->3
     * @param type
     */
    private void setSelect(String dataType) {
	if (TextUtils.isEmpty(dataType)) {
	    tvTitleName.setText("体验金");
	} else {
	    switch (dataType) {
	    case "1":
		tvTitleName.setText("体验金奖励");
		break;
	    case "2":
		tvTitleName.setText("体验金投资");
		break;
	    case "3":
		tvTitleName.setText("体验金到期");
		break;
	    }
	}
	showOrHideTitleMenu();
	sendHttpReques(RefreshType.RefreshNoPull, dataType);
    }

    @Override
    public void onRefresh() {
	// TODO Auto-generated method stub

    }

    @Override
    public void onLoadMore() {
	// TODO Auto-generated method stub

    }

    public void sendHttpReques(RefreshType refreshType, String dataType) {
	mRefreshType = refreshType;

	RequestParams params = new RequestParams(Constant.HOMEPAGE_EXPERIENCEGOLD);
	params.addBodyParameter("user_userunid", mUid);
	params.addBodyParameter("type", dataType);
	params.addBodyParameter("pageNum", "1");
	params.addBodyParameter("size", "10");
	httpUtil.sendRequest(params, refreshType, this);
    }

    @Override
    public void onFailure(String url, String errorContent) {
	// TODO Auto-generated method stub

    }

    @Override
    public void onSuccess(String url, JSONBase jsonBase) {
	JSONObject jsonObject = JSON.parseObject(jsonBase.getDisposeResult());
	String experienceMoneyLog = jsonObject.getString("experienceMoneyLog");
	List<TyjDetails> tyjDetails = JSON.parseArray(experienceMoneyLog, TyjDetails.class);

    }

}