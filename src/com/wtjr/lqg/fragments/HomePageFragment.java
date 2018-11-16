package com.wtjr.lqg.fragments;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.umeng.analytics.MobclickAgent;
import com.wtjr.lqg.MainActivity;
import com.wtjr.lqg.R;
import com.wtjr.lqg.activities.ActivityWebViewActivity;
import com.wtjr.lqg.activities.EarningsDetailsActivity;
import com.wtjr.lqg.activities.HaveAcountsLoginActivity;
import com.wtjr.lqg.activities.LiCaiPlanInvestmentDetailActivity;
import com.wtjr.lqg.activities.LoginActivity;
import com.wtjr.lqg.activities.RechargeActivity;
import com.wtjr.lqg.activities.RegisterActivity;
import com.wtjr.lqg.activities.WebViewActivity;
import com.wtjr.lqg.activities.WithdrawalActivity;
import com.wtjr.lqg.base.AccountData;
import com.wtjr.lqg.base.ActivityData;
import com.wtjr.lqg.base.JSONBase;
import com.wtjr.lqg.base.PlanMasData;
import com.wtjr.lqg.base.PopupPage;
import com.wtjr.lqg.base.SevenDayEarnings;
import com.wtjr.lqg.basecommonly.BaseFragment;
import com.wtjr.lqg.constants.Constant;
import com.wtjr.lqg.enums.InvestmentDetailsType;
import com.wtjr.lqg.enums.RechargeType;
import com.wtjr.lqg.enums.RefreshType;
import com.wtjr.lqg.sharedpreferences.SaveCurrentUidUtil;
import com.wtjr.lqg.sharedpreferences.SharedPreferencesUtil;
import com.wtjr.lqg.sharedpreferences.ToggleVoiceState;
import com.wtjr.lqg.utils.DialogUtil;
import com.wtjr.lqg.utils.HttpUtil;
import com.wtjr.lqg.utils.JointUtils;
import com.wtjr.lqg.utils.L;
import com.wtjr.lqg.utils.MoneyFormatUtil;
import com.wtjr.lqg.utils.NetworkUtil;
import com.wtjr.lqg.utils.PermissionUtils;
import com.wtjr.lqg.utils.RSAUtils;
import com.wtjr.lqg.utils.SystemUtil;
import com.wtjr.lqg.utils.TimeUtil;
import com.wtjr.lqg.utils.ToastUtil;
import com.wtjr.lqg.widget.NavigationBarItem;
import com.wtjr.lqg.widget.ObservableScrollView;
import com.wtjr.lqg.widget.ScalePagerView;
import com.wtjr.lqg.widget.UsuallyLayoutHome;
import com.wtjr.lqg.widget.pullRefresh.PullToRefreshLayout;

import org.xutils.http.RequestParams;

import java.util.ArrayList;
import java.util.List;

import pl.droidsonroids.gif.GifImageView;

import static com.wtjr.lqg.enums.RollInType.TYJ;


/**
 * Created by WangXu on 2017/11/6.
 */

public class HomePageFragment extends BaseFragment implements HttpUtil.HttpRequesListener, View.OnClickListener, PullToRefreshLayout
        .OnRefreshListener, ViewPager.OnPageChangeListener, ObservableScrollView.ScrollViewListener{

    private MainActivity mActivity;
    private RefreshType mRefreshType;

    /**
     * 是否显示资产的状态 默认为true为显示
     */
    private boolean showAssetStatus = true;
    /**
     * 首页客服
     */
    private ImageView ivHomeService;
    /**
     * 昨日收益
     */
    private TextView tvYesterdayIncome;

    /**
     * 未登录之前的登录/注册
     */
    private RelativeLayout llHomeUnlogin;
    /**
     * 登录之后显示昨日收益
     */
    private RelativeLayout rlHomelogin;
    /**
     * 显示隐藏资产
     */
    private ImageView ivHomeShowAsset;

    /**
     * 预计明日收益父布局
     */
    private LinearLayout llTtomorrowIncome;
    /**
     * 预计明日收益
     */
    private TextView tvTomorrowIncome;
    /**
     * 累计收益的父控件
     */
    private LinearLayout llTotalincome;
    /**
     * 累计收益
     */
    private TextView tvTotalincome;
    /**
     * 首页轮播图
     */
    private ScalePagerView homeVp;
    /**
     * 未登录时的banner图
     */
    private ImageView ivBannerBg;
    /**
     * 轮播图加下面的点的布局
     */
    private LinearLayout llHomePoints;
    /**
     * 轮播图的父控件
     */
    private LinearLayout llHomevp;
    /**
     * 首页上半部分的父布局
     */
    private LinearLayout llHomeTop;
    /**
     * 滚动监听控件
     */
    private ObservableScrollView osvHome;
    /**
     * 下拉刷新最外层
     */
    private PullToRefreshLayout pullToRefreshLayout;
    /**
     * 记录第一次点击的时间
     */
    private long clickTime = 0;
    /**
     * 播放音频视频
     */
    private MediaPlayer mediaPlayer;
    /**
     * 我的界面小红点
     */
    private NavigationBarItem nbiMe;
    /**
     * 刷新首页返回数据
     */
    private AccountData accountData;
    /**
     * 信息披露
     */
    private UsuallyLayoutHome ulhInformation;
    /**
     * 精彩活动
     */
    private UsuallyLayoutHome ulhActiv;
    /**
     * 签到奖励
     */
    private UsuallyLayoutHome ulhSignin;
    /**
     * 邀请有奖
     */
    private UsuallyLayoutHome ulhInvitation;
    /**
     * 轮播图定时轮播的识别msg
     */
    private static final int HANDLERID = 1;

    private int START = 0;
    private float meTittleHeight;
    /**
     * 首页活动轮播图中的数据集合
     */
    private List<ActivityData> activityDatas = new ArrayList<ActivityData>();
    private ViewPagerAdapter adapter;
    /**
     * 轮播消息handler
     */
    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            if (msg.what == HANDLERID) {

                homeVp.setCurrentItem((homeVp.getCurrentItem() + 1));

                sendEmptyMessageDelayed(HANDLERID, 3000);
            }
        }
    };
    /**
     * 首页一键转入体验金/零钱宝gif
     */
    private GifImageView homeGif;
    private ArrayList<PopupPage> popupPageList = new ArrayList<>();
    /**
     * 标年化收益率
     */
    private TextView tvYearRate;
    /**
     * 最低起投金额
     */
    private TextView tvMiniMoney;
    /**
     * 已投标人数
     */
    private TextView tvHomeBorrownum;
    /**
     * 立即抢购
     */
    private ImageView ivSnapUp;
    /**
     * 大满贯计划
     */
    private LinearLayout llImageGrandSlamPlan;
    /**
     * 提现
     */
    private LinearLayout llHomeWithdraw;
    /**
     * 充值
     */
    private LinearLayout llhomerecharge;
    private TextView tvTextYesterdayIncome;
    private GifImageView gifHomeRecharge;
    private int CLICKRECHARGE = 0;
    /**
     * 周周生数据
     */
    private PlanMasData planMasData;
    /**
     * 可收回充值气泡
     */
    private boolean PACKUP = true;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_page, container, false);
        mActivity = (MainActivity) getActivity();

        String currentUid = SaveCurrentUidUtil.getCurrentUid(getActivity());
        L.hintI("currentUid=" + currentUid);

        start(view);
        return view;
    }

    @Override
    public void findViewById(View view) {
        // 下拉刷新
        pullToRefreshLayout = (PullToRefreshLayout) view.findViewById(R.id.pullTo_refreshLayout);
        osvHome = (ObservableScrollView) view.findViewById(R.id.osv_home);
        llHomeTop = (LinearLayout) view.findViewById(R.id.ll_home_top);
        //充值气球
        gifHomeRecharge = (GifImageView) view.findViewById(R.id.gif_home_recharge);
        //首页客服
        ivHomeService = (ImageView) view.findViewById(R.id.iv_home_service);
        //未登录之前的登录/注册
        llHomeUnlogin = (RelativeLayout) view.findViewById(R.id.rl_home_unlogin);
        //昨日收益字
        tvTextYesterdayIncome = (TextView) view.findViewById(R.id.tv_text_yesterdayIncome);
        //提现
        llHomeWithdraw = (LinearLayout) view.findViewById(R.id.ll_home_withdraw);
        //充值
        llhomerecharge = (LinearLayout) view.findViewById(R.id.ll_home_recharge);
        //登录之后显示昨日收益
        rlHomelogin = (RelativeLayout) view.findViewById(R.id.rl_home_login);
        //显示隐藏资产
        ivHomeShowAsset = (ImageView) view.findViewById(R.id.iv_home_show_asset);

        //轮播图父布局
        llHomevp = (LinearLayout) view.findViewById(R.id.ll_homevp);
        //轮播图
        homeVp = (ScalePagerView) view.findViewById(R.id.vp_home_spv);
//        homeVp.bindScrollView(osvHome);
        //未登录时的banner图
        ivBannerBg = (ImageView) view.findViewById(R.id.iv_banner_bg);
        //轮播图的点的布局
        llHomePoints = (LinearLayout) view.findViewById(R.id.ll_home_points);

        //昨日收益
        tvYesterdayIncome = (TextView) view.findViewById(R.id.tv_yesterdayIncome);
        //预计明日收益父布局
        llTtomorrowIncome = (LinearLayout) view.findViewById(R.id.ll_tomorrow_income);
        //预计明日收益
        tvTomorrowIncome = (TextView) view.findViewById(R.id.tv_tomorrow_income);
        //累计收益父布局
        llTotalincome = (LinearLayout) view.findViewById(R.id.ll_totalincome);
        //累计收益
        tvTotalincome = (TextView) view.findViewById(R.id.tv_totalincome);
        //信息披露
        ulhInformation = (UsuallyLayoutHome) view.findViewById(R.id.ulh_information);
        //精彩活动
        ulhActiv = (UsuallyLayoutHome) view.findViewById(R.id.ulh_activ);
        //签到奖励
        ulhSignin = (UsuallyLayoutHome) view.findViewById(R.id.ulh_sign_in);
        //邀请有奖
        ulhInvitation = (UsuallyLayoutHome) view.findViewById(R.id.ulh_invitation);

        //标年化收益率
        tvYearRate = (TextView) view.findViewById(R.id.tv_year_rate);
        //最低起投金额
        tvMiniMoney = (TextView) view.findViewById(R.id.tv_mini_money);
        //已投标人数
        tvHomeBorrownum = (TextView) view.findViewById(R.id.tv_home_borrownum);
        //立即抢购
        ivSnapUp = (ImageView) view.findViewById(R.id.iv_home_snap_up);

        // 首页一键转入体验金/零钱宝
        homeGif = (GifImageView) view.findViewById(R.id.home_gif);
        //大满贯计划
        llImageGrandSlamPlan = view.findViewById(R.id.ll_image_grand_slam_plan);
    }


    @Override
    public void setListener() {
        //网络请求
        mHttpUtil.setHttpRequesListener(this);
        //下拉刷新最外层
        pullToRefreshLayout.setOnRefreshListener(this);
        //首页ScrollView滑动监听
        osvHome.setScrollViewListener(this);
        //未登录时点击登录注册
        llHomeUnlogin.setOnClickListener(this);
        //未登录时点击登录注册
        llHomeWithdraw.setOnClickListener(this);
        //未登录时点击登录注册
        llhomerecharge.setOnClickListener(this);
        //未登录时点击默认广告图
        ivBannerBg.setOnClickListener(this);
        //显示隐藏资产
        ivHomeShowAsset.setOnClickListener(this);
        //首页客服监听器
        ivHomeService.setOnClickListener(this);
        //充值气球
        gifHomeRecharge.setOnClickListener(this);
        //监听轮播图变化
        homeVp.addOnPageChangeListener(this);
        //总资产监听器
        llTtomorrowIncome.setOnClickListener(this);
        //累计收益监听器
        llTotalincome.setOnClickListener(this);
        //品牌故事
        ulhInformation.setOnClickListener(this);
        //安全保障
        ulhActiv.setOnClickListener(this);
        //签到奖励监听
        ulhSignin.setOnClickListener(this);
        //邀请有奖
        ulhInvitation.setOnClickListener(this);
        //立即抢购
        ivSnapUp.setOnClickListener(this);
        llImageGrandSlamPlan.setOnClickListener(this);

    }

    @Override
    public void initData() {
        MainActivity.footBottomDistance();
        //改变webview对话框显示状态，防止分享URL注册成功跳转app无法点击邀请分享按钮
        WebViewActivity.MARKDIALOH = 1;

        //当前版本号存入本地，在启动页判断是否需要引导页
        SharedPreferencesUtil.setPrefString(mActivity, "oldVersion", SystemUtil.getAppVersionName(mActivity));

        CLICKRECHARGE = SharedPreferencesUtil.getPrefInt(mActivity, "clickRecharge", 0);
        if (CLICKRECHARGE > 5) {//5从之后不弹出
            gifHomeRecharge.setVisibility(View.GONE);
        }

        if (isLogin) {
            showLocalData();

            llHomeUnlogin.setVisibility(View.GONE);
            tvTextYesterdayIncome.setVisibility(View.VISIBLE);
            rlHomelogin.setVisibility(View.VISIBLE);
            if (app.mAccountData == null) {
                return;
            }

        } else {

            llHomeUnlogin.setVisibility(View.VISIBLE);
            tvTextYesterdayIncome.setVisibility(View.INVISIBLE);
            rlHomelogin.setVisibility(View.GONE);
        }

        /**
         * 动态获取6.0版本以上手机存储权限
         */
        PermissionUtils.writerReadSDcard(mActivity);


    }


    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {

            case R.id.iv_banner_bg://未登录下默认banner图显示图片的点击
                intent = new Intent(mActivity, RegisterActivity.class);
                startActivity(intent);

                return;

            case R.id.gif_home_recharge:

                if (PACKUP) {
                    PACKUP = false;
                    packUpRechargeAnimation();
                }

                return;
        }


        if (!isLogin) {// 没账号登录0
            if (!mFirstInState) {// 不是第一次使用（有账号登录过）
                startActivity(new Intent(getActivity(), HaveAcountsLoginActivity.class));
            } else {// 不是第一次使用（没有账号登录过）跳转到注册
                startActivity(new Intent(getActivity(), LoginActivity.class));
            }
            return;
        }
        switch (v.getId()) {
//            case R.id.iv_home_activ:    //活动
////                intent = new Intent(mActivity, ActivityWebViewActivity.class);
////                startActivity(intent);
//                sendHomePageADRequest();
//                return;
            case R.id.ll_home_withdraw://提现
                intent = new Intent(mActivity, WithdrawalActivity.class);

                startActivity(intent);
                return;
            case R.id.ll_home_recharge://充值
                intent = new Intent(mActivity, RechargeActivity.class);
                intent.putExtra(RechargeType.class.getName(), RechargeType.Balance);
                startActivity(intent);
                return;

            case R.id.iv_home_service:// 我的客服
                //点击通知栏消掉我的客服的new
//                MeFragment.dissNewTab();
                intent = new Intent(mActivity, WebViewActivity.class);
                intent.putExtra("url", Constant.H5_LQG_MYKEFU);
                intent.putExtra("showMyKeFu", "showMyKeFu");
                intent.putExtra("TitleName", "我的客服");
                startActivity(intent);
                return;

            case R.id.ulh_information://信息披露
                intent = new Intent(mActivity, WebViewActivity.class);
                intent.putExtra("url", Constant.H5_WE);
                intent.putExtra("TitleName", "信息披露");
                startActivity(intent);
                return;

            case R.id.ulh_activ://精彩活动
                intent = new Intent(getActivity(), ActivityWebViewActivity.class);
                intent.putExtra("bDailActivityOrNewActiity", true);
                startActivity(intent);
                return;

            case R.id.ulh_sign_in://签到奖励
                intent = new Intent(mActivity, WebViewActivity.class);
                String url = JointUtils.httpJointUid(Constant.H5_SING_IN, mUid);
                intent.putExtra("url", url);
                intent.putExtra("TitleName", "签到");
                startActivity(intent);
                return;

            case R.id.ulh_invitation://邀请有礼
                String invitationUrl = SharedPreferencesUtil.getPrefString(getActivity(), "InvitationUrl", "") + "?uid=" + mUid;
                intent = new Intent(mActivity, WebViewActivity.class);
                intent.putExtra("url", invitationUrl);
                intent.putExtra("TitleName", "邀请好友");
                startActivity(intent);
                return;
            case R.id.ll_image_grand_slam_plan: //大满贯计划
                DialogUtil.liCaiPlanSubscribeDialog(getActivity(), accountData.content, accountData.tyj, new DialogUtil.OnClickYesListener() {
                    @Override
                    public void onClickYes() {
                        sendBookRequest(RefreshType.RefreshNoPull, "1");
                    }
                }, new DialogUtil.OnClickNoListener() {
                    @Override
                    public void onClickNo() {
                        sendBookRequest(RefreshType.RefreshNoPull, "0");
                    }
                });
                break;

            case R.id.rl_home_unlogin://登录/注册   未登录时上面直接判断跳登录注册了
                break;
            case R.id.ll_tomorrow_income://预计明日收益

                // 获得昨日收益
//                String yesterdayIncome = tvYesterdayIncome.getText().toString();
//                intent = new Intent(mActivity, TotalAssetsActivity.class);
//                intent.putExtra("YesterdayIncome", yesterdayIncome);
//                startActivity(intent);
//
                break;

            case R.id.ll_totalincome://累计收益
//                intent = new Intent(mActivity, LqbDetailsActivity.class);
//                intent.putExtra(LqbDetailsType.class.getName(), LqbDetailsType.Earnings);
//                startActivity(intent);
                startActivity(new Intent(getActivity(), EarningsDetailsActivity.class));
                break;
            case R.id.iv_home_show_asset://显示隐藏资产
                showAssetStatus = SharedPreferencesUtil.getPrefBoolean(mActivity, "showAssetStatus", true);

                if (showAssetStatus) {
                    //设置下次状态
                    SharedPreferencesUtil.setPrefBoolean(mActivity, "showAssetStatus", false);
                } else {
                    //设置下次状态
                    SharedPreferencesUtil.setPrefBoolean(mActivity, "showAssetStatus", true);
                }
                setShowAssetStatus(SharedPreferencesUtil.getPrefBoolean(mActivity, "showAssetStatus", true));
                break;

            case R.id.iv_home_snap_up://立即抢购
                if (!NetworkUtil.isNetworkAvailable(mActivity)) { // 判断网络是否链接
                    ToastUtil.showToastShort(mActivity, "无法连接网络，请检查网络配置");
                } else {

                    if (planMasData != null) {
                        intent = new Intent(getActivity(), LiCaiPlanInvestmentDetailActivity.class);
                        intent.putExtra(PlanMasData.class.getName(), planMasData);
                        intent.putExtra(InvestmentDetailsType.class.getName(), InvestmentDetailsType.Investment);
                        startActivity(intent);
                    }

                }
                break;
            default:
                break;
        }
    }

    /**
     * 收起充值气球
     */
    private void packUpRechargeAnimation() {
        CLICKRECHARGE++;
        SharedPreferencesUtil.setPrefInt(mActivity, "clickRecharge", CLICKRECHARGE);
        /** 设置缩放动画 */
        final ScaleAnimation animation = new ScaleAnimation(1.0f, 0.0f, 1.0f, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                0.5f);
        animation.setDuration(300);//设置动画持续时间
        /** 常用方法 */
        //animation.setRepeatCount(int repeatCount);//设置重复次数
        animation.setFillAfter(true);//动画执行完后是否停留在执行完的状态
        //animation.setStartOffset(long startOffset);//执行前的等待时间
        gifHomeRecharge.startAnimation(animation);
    }

    /**
     * 设置显示隐藏资产
     *
     * @param showAssetStatus
     */
    private void setShowAssetStatus(boolean showAssetStatus) {
        if (showAssetStatus) {//显示资产

            ivHomeShowAsset.setImageResource(R.drawable.home_show_asset);
            // 预计明日收益
            tvTomorrowIncome.setText(MoneyFormatUtil.format(accountData.tomorrowInterest));
            // 累计收益
            tvTotalincome.setText(MoneyFormatUtil.format(accountData.total_income));
            // 昨日收益
            tvYesterdayIncome.setText(MoneyFormatUtil.format(accountData.recent_income.get(0).dayProfit));
        } else {//隐藏资产

            ivHomeShowAsset.setImageResource(R.drawable.home_unshow_asset);
            tvTomorrowIncome.setText("******");
            tvTotalincome.setText("******");
            tvYesterdayIncome.setText("******");
        }

    }

    /**
     * 显示本地缓存数据
     */
    private void showLocalData() {
        // 取本地数据进行显示
        AccountData mAccountData = app.mAccountData;

        if (mAccountData != null) {
            // 7日收益字符串值
            String sevenDayEarnings = mAccountData.sevenDayEarnings;
            // 没有七日数据
            if (TextUtils.isEmpty(sevenDayEarnings)) {
                // 伪造无收益的七日数据均为0
                List<SevenDayEarnings> earnings = new ArrayList<SevenDayEarnings>();
                for (int i = 0; i < 7; i++) {
                    SevenDayEarnings dayEarnings = new SevenDayEarnings();
                    dayEarnings.dayProfit = 0;
                    earnings.add(dayEarnings);
                }
                mAccountData.recent_income = earnings;
            } else {// 有七日收益
                String[] split = sevenDayEarnings.split(",");

                List<SevenDayEarnings> earnings = new ArrayList<SevenDayEarnings>();
                for (int i = 0; i < 7; i++) {
                    SevenDayEarnings dayEarnings = new SevenDayEarnings();
                    dayEarnings.dayProfit = Double.parseDouble(split[i]);
                    earnings.add(dayEarnings);
                }
                mAccountData.recent_income = earnings;
            }
            updateData(mAccountData);
        }
    }

    /**
     * 初始化首页轮播图
     * ScalePagerView
     */
    private void initViewPager() {

        adapter = new ViewPagerAdapter(activityDatas);
        homeVp.setOffscreenPageLimit(3);
        homeVp.setPageMargin(5);
        homeVp.setAdapter(adapter);
        /**
         * 无限轮播
         */
        //TODO
        homeVp.setCurrentItem(1000 / 2 - ((1000 / 2) % activityDatas.size()));
//        homeVp.setCurrentItem(Integer.MAX_VALUE / 2 - ((Integer.MAX_VALUE / 2) % activityDatas.size()));
        homeVp.bindScrollView(osvHome);
        homeVp.setActivityDatas(activityDatas);
        homeVp.setHandler(handler);

    }

    /**
     * 初始化dot
     */
    private void initDots() {
        llHomePoints.removeAllViews();
        for (int i = 0; i < activityDatas.size(); i++) {
            ImageView imageView = new ImageView(mActivity);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, 16);
            layoutParams.rightMargin = 7;
            layoutParams.leftMargin = 7;
            imageView.setLayoutParams(layoutParams);

            imageView.setBackgroundResource(R.drawable.home_vp_dot_bg);
            llHomePoints.addView(imageView);
        }
        updateDots();
    }

    /**
     * 更新点
     */
    private void updateDots() {
        if (activityDatas.size() == 0) {
            return;
        }
        int currentPage = homeVp.getCurrentItem() % activityDatas.size();
        for (int i = 0; i < llHomePoints.getChildCount(); i++) {
            llHomePoints.getChildAt(i).setEnabled(i == currentPage);// 设置setEnabled为true的话
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, 16);
            layoutParams.rightMargin = 7;
            layoutParams.leftMargin = 7;
            llHomePoints.getChildAt(i).setLayoutParams(layoutParams);
        }
    }

    /**
     * 调用该方法实现下拉刷新
     */
    @Override
    public void refreshData(RefreshType refreshType) {
        // 先判断控件是否有null
        if (getActivity() == null || osvHome == null) {
            return;
        }

        switch (refreshType) {
            case RefreshPull:
                // 那就自动滚动到顶部
                osvHome.fullScroll(ScrollView.FOCUS_UP);

                // 如果没有手触摸，并且距离顶部的位置是0那么就可以下拉刷新
                if (PullToRefreshLayout.TRIGGER && pullToRefreshLayout.getHeadHeight() == 0) {

                    if ((System.currentTimeMillis() - clickTime) > 2000) {

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                pullToRefreshLayout.autoRefresh();
                            }
                        }, 500);

                        clickTime = System.currentTimeMillis();
                    }
                }
                break;

            case RefreshNoPull:
                if (isLogin) {
                    sendAccountDataRequest(RefreshType.RefreshNoPull);
                    sendActivityReques(RefreshType.RefreshPull);
                }
                break;
        }
    }

    /**
     * 首页ScrollView滑动的回调方法
     */
    @Override
    public void onScrollChanged(ObservableScrollView scrollView, int x, int y, int oldx, int oldy) {
//        meTittleHeight = getResources().getDimension(R.dimen.d40);

//        if (osvHome.canPullDown()) {
//            ivHomeService.setImageResource(R.drawable.home_service);
//        } else {
//            ivHomeService.setImageResource(R.drawable.home_service2);
//        }
        // 设置标题栏逐渐透明化
//        setTitleBgAlpha(y);
    }


//    /**
//     * 设置标题背景透明化
//     */
//    private void setTitleBgAlpha(float oldy) {
//
//        if (oldy >= meTittleHeight) {
//            oldy = (int) meTittleHeight;
//        }
//        float alpha = (float) oldy / (float) meTittleHeight;
//        int a = (int) (255 * alpha);
//        int color = Color.argb(a, 255, 255, 255);
//        if (alpha < 0.1) {
//            alpha = 0;
//        }
//        ivTitleLqg.setVisibility(View.VISIBLE);
//        rlTitleBg.setBackgroundColor(color); //动态改变标题栏背景
//        ivTitleLqg.setAlpha(alpha);
//    }

    /**
     * 发送账户数据请求
     *
     * @param refreshType
     */
    public void sendAccountDataRequest(RefreshType refreshType) {
        mRefreshType = refreshType;
        RequestParams params = new RequestParams(Constant.ACCESSAPPINTERFACE_INDEXREFRESH);
        params.addBodyParameter("user_userunid", mUid);
        mHttpUtil.sendRequest(params, refreshType, getActivity());

        //首页弹窗广告
        String day = TimeUtil.getDay(System.currentTimeMillis());
        String lastDay = SharedPreferencesUtil.getPrefString(getActivity(), "AD_Date", "0");
        if (!lastDay.equals(day) && app.mAccountData != null) {
            sendHomePageADRequest();
        }
    }

    /**
     * 发送首页广告请求
     */
    public void sendHomePageADRequest() {
        mRefreshType = RefreshType.RefreshPull;
        RequestParams params = new RequestParams(Constant.HOMEPAGE_TOPPOPUPPAGES);
        params.addBodyParameter("user_userunid", mUid);
        mHttpUtil.sendRequest(params, RefreshType.RefreshPull, getActivity());
    }

    /**
     * 发送首页banner图活动请求
     *
     * @param refreshType
     */
    public void sendActivityReques(RefreshType refreshType) {
        mRefreshType = refreshType;

        RequestParams params = new RequestParams(Constant.SETTINGUP_ACTIVITY);
        params.addBodyParameter("user_userunid", mUid);
        params.addBodyParameter("activeType", "3");
        mHttpUtil.sendRequest(params, refreshType, getActivity());
    }

    @Override
    public void onFailure(String url, String errorContent) {
        DialogUtil.promptDialog(getActivity(), DialogUtil.HEAD_SERVICE, errorContent);

        if (mRefreshType == RefreshType.RefreshPull) {
            pullToRefreshLayout.refreshFinish(PullToRefreshLayout.FAIL);
        }
    }

    @Override
    public void onSuccess(String url, JSONBase jsonBase) {
        if (mRefreshType == RefreshType.RefreshPull) {
            pullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
        }

        JSONObject jsonObject = JSON.parseObject(jsonBase.getDisposeResult());
        if (url.equals(Constant.ACCESSAPPINTERFACE_INDEXREFRESH)) {//首页刷新
            if (mRefreshType == RefreshType.RefreshPull) {
                initMediaPlayer(R.raw.coin);
                startMediaPlayer();

                if (isLogin) {
                    AccountData data = JSON.parseObject(jsonBase.getDisposeResult(), AccountData.class);

                    SharedPreferencesUtil.setPrefString(getActivity(), "noviceUrl", data.noviceUrl);  //新手活动url
                    SharedPreferencesUtil.setPrefString(getActivity(), "dailyUrl", data.dailyUrl);    //日常活动url
                    SharedPreferencesUtil.setPrefString(getActivity(), "InvitationUrl", data.InvitationUrl);  //邀请有礼url

                    if (data.isMsgRead != null) {
                        boolean isMsg = data.isMsgRead.equals("1");
                        // 新消息或活动不为0时显示红点，否则不显示红点
                        nbiMe = (NavigationBarItem) getActivity().findViewById(R.id.nbi_me);
                        if (isMsg) {
                            // 显示红点
                            nbiMe.showRedDot();
                        } else {
                            // 不显示红点
                            nbiMe.cancelRedDot();
                        }
                    }
                }
            }

            accountData = JSON.parseObject(jsonBase.getDisposeResult(), AccountData.class);
            app.updateData(accountData);
            updateData(accountData);

            showAssetStatus = SharedPreferencesUtil.getPrefBoolean(mActivity, "showAssetStatus", true);
            //显示隐藏资产
            setShowAssetStatus(showAssetStatus);

            SharedPreferencesUtil.setPrefString(mActivity, "shareJson", jsonBase.getDisposeResult());


        } else if (url.equals(Constant.HOMEPAGE_TOPPOPUPPAGES)) {// 广告
            String day = TimeUtil.getDay(System.currentTimeMillis());
            SharedPreferencesUtil.setPrefString(getActivity(), "AD_Date", day);

            String disposeResult = jsonBase.getDisposeResult();
            if (TextUtils.isEmpty(disposeResult)) {
                return;
            }

            if (jsonObject == null) {
                return;
            }
            String popupPage = jsonObject.getString("popupPage");
            popupPageList.clear();
            popupPageList.addAll(JSON.parseArray(popupPage, PopupPage.class));

            // 显示广告
            mActivity.showAd(popupPageList);
        } else if (url.equals(Constant.SETTINGUP_ACTIVITY)) {//广告轮播图
            String activity = jsonObject.getString("activity");
            activityDatas.clear();
            activityDatas.addAll(JSON.parseArray(activity, ActivityData.class));

            for (ActivityData activityData : activityDatas) {
                if (activityData.activeTtype == 1) {// 转盘活动
                    JSONObject json = new JSONObject();
                    json.put("ukey", SaveCurrentUidUtil.getCurrentUid(app));
                    json.put("code", Constant.ZP_CODE);

                    String sign = "";
                    try {
                        sign = RSAUtils.encrypt(json.toString(), Constant.ZP_ENCRYPT_KEY);
                    } catch (Exception e2) {
                        e2.printStackTrace();
                    }

                    activityData.activeUrl = activityData.activeUrl + "?ukey=" + mUid + "&code=" + Constant.ZP_CODE + "&sign=" + sign;

                } else if (activityData.activeTtype == 3) {
                    activityData.activeUrl = activityData.activeUrl + "?uid=" + mUid;
                } else {
                    if (TextUtils.isEmpty(activityData.activeUrl)) {// 判断活动地址是否为空
                        // 启用备用地址
                        activityData.activeUrl = Constant.IMAGE_ADDRESS + activityData.activeImageUrl;
                    }
                }
            }

            if (adapter == null) {
                initViewPager();
            } else {
                adapter.setActivityBases(activityDatas);
                adapter.notifyDataSetChanged();
            }

            if (activityDatas.size() > 1) {
                handler.removeMessages(HANDLERID);
                handler.sendEmptyMessageDelayed(HANDLERID, 3000);
            } else {// 停止轮播图滚动
                handler.removeMessages(HANDLERID);
            }
            //传入handler在点击或者滑动中viewpager不自动滑
            homeVp.setHandler(handler);

            initDots();

        } else if (url.equals(Constant.LQB_EXPMONEYINTOLQB)) {//体验金转入零钱宝
            this.refreshData(RefreshType.RefreshNoPull);
            double usableExpMoney = app.mAccountData.usableExpMoney;
            DialogUtil.rollInLqbDialog(mActivity, TYJ, usableExpMoney);

        } else if (url.equals(Constant.PLANBOOK_STARTBOOK)) {
            if (jsonObject.getString("status").equals("0")) {
                DialogUtil.liCaiPlanSubscribeDialog(getActivity(), accountData.tyj + "元体验金已到账！", new DialogUtil.OnClickYesListener() {
                    @Override
                    public void onClickYes() {
                        sendAccountDataRequest(RefreshType.RefreshNoPull);
                    }
                });
            }
        }
    }


    /**
     * 更新页面数据
     *
     * @param accountData
     */
    private void updateData(final AccountData accountData) {
        if (accountData == null) {
            return;
        }
        if (SharedPreferencesUtil.getPrefBoolean(mActivity, "showAssetStatus", true)) {//判断是显示资产是才给资产赋值

            // 预计明日收益
            tvTomorrowIncome.setText(MoneyFormatUtil.format(accountData.tomorrowInterest));
            // 累计收益
            tvTotalincome.setText(MoneyFormatUtil.format(accountData.total_income));
            // 昨日收益
            tvYesterdayIncome.setText(MoneyFormatUtil.format(accountData.recent_income.get(0).dayProfit));
        }
        /**
         * 显示首页标的状态数据
         */
        showAuctionFacility(accountData);
        if (!TextUtils.isEmpty(accountData.isBook)) {
            if (accountData.isBook.equals("0")) {    //周周升是否开启预约
                llImageGrandSlamPlan.setVisibility(View.VISIBLE);
            } else {
                llImageGrandSlamPlan.setVisibility(View.GONE);
            }
        } else {
            llImageGrandSlamPlan.setVisibility(View.GONE);
        }
        if (!TextUtils.isEmpty(accountData.bookDialog)) {
            if (accountData.bookDialog.equals("1")) {

                DialogUtil.liCaiPlanSubscribeDialog(getActivity(), accountData.content, accountData.tyj, new DialogUtil.OnClickYesListener() {
                    @Override
                    public void onClickYes() {
                        sendBookRequest(RefreshType.RefreshNoPull, "1");
                    }
                }, new DialogUtil.OnClickNoListener() {
                    @Override
                    public void onClickNo() {
                        sendBookRequest(RefreshType.RefreshNoPull, "0");
                    }
                });
            }
        }

        if (accountData.usableExpMoney > 0) {
            homeGif.setVisibility(View.VISIBLE);
            homeGif.setImageResource(R.drawable.gif_usableexpmoney);
            homeGif.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sendTyjRollInRequest(RefreshType.RefreshNoPull);
                }
            });
        } else if (accountData.usableExpMoney <= 0) {
            homeGif.setVisibility(View.GONE);
        }

    }

    /**
     * 预约开启
     *
     * @param refreshNoPull
     */
    private void sendBookRequest(RefreshType refreshNoPull, String isBook) {
        RequestParams params = new RequestParams(Constant.PLANBOOK_STARTBOOK);
        params.addBodyParameter("user_userunid", mUid);
        params.addBodyParameter("isBook", isBook);
        mHttpUtil.sendRequest(params, refreshNoPull, getActivity());
    }

    /**
     * 显示首页标的状态数据
     *
     * @param accountData
     */
    public void showAuctionFacility(AccountData accountData) {

        planMasData = JSON.parseObject(accountData.planMas, PlanMasData.class);
        if (planMasData != null) {

            tvYearRate.setText(planMasData.baseRate + "-" + planMasData.maxRate);
            tvMiniMoney.setText(MoneyFormatUtil.format2(planMasData.investMin) + " 元起购");
            tvHomeBorrownum.setText("已加入" + planMasData.investUserCount + "人");

        }
    }


    /**
     * 发送体验金转入请求
     */
    public void sendTyjRollInRequest(RefreshType refreshType) {
        RequestParams params = new RequestParams(Constant.LQB_EXPMONEYINTOLQB);
        params.addBodyParameter("user_userunid", app.mAccountData.uId);
        params.addBodyParameter("expMoney2lqb", app.mAccountData.usableExpMoney + "");// 体验金转入
        mHttpUtil.sendRequest(params, refreshType, mActivity);
    }

    @Override
    public void onRefresh() {
        START++;
        if (!NetworkUtil.isNetworkAvailable(mActivity)) { // 判断网络是否链接
            tvHomeBorrownum.setVisibility(View.GONE);
            homeVp.setVisibility(View.INVISIBLE);
        } else {
            tvHomeBorrownum.setVisibility(View.VISIBLE);
            homeVp.setVisibility(View.VISIBLE);
        }

        if (isLogin) {
            llHomevp.setVisibility(View.VISIBLE);
            ivBannerBg.setVisibility(View.GONE);
            sendAccountDataRequest(RefreshType.RefreshPull);
            sendActivityReques(RefreshType.RefreshPull);
        } else {
            pullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
            llHomevp.setVisibility(View.GONE);
            ivBannerBg.setVisibility(View.VISIBLE);
            homeGif.setVisibility(View.GONE);
        }
    }

    @Override
    public void onLoadMore() {

    }

    @Override
    public void onResume() {
        super.onResume();
        // 友盟统计
        MobclickAgent.onPageStart("HomePageFragment");
    }

    @Override
    public void onPause() {
        super.onPause();
        // 友盟统计
        MobclickAgent.onPageEnd("HomePageFragment");
    }

    /**
     * 视频音频播放器
     *
     * @param resid 音频资源
     */
    private void initMediaPlayer(int resid) {
        if (getActivity() != null && ToggleVoiceState.getVoice(getActivity())) {
            if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
                mediaPlayer.release();
                mediaPlayer = null;
            }
            mediaPlayer = MediaPlayer.create(getActivity(), resid);
        }
    }

    /**
     * 启动声音播放
     */
    private void startMediaPlayer() {
        if (getActivity() != null && mediaPlayer != null && ToggleVoiceState.getVoice(getActivity())) {
            mediaPlayer.start();
        }
    }

    /**
     * 当ViewPager中页面的状态发生改变时调用
     */
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        updateDots();
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    /**
     * 首页轮播图适配器
     */
    private class ViewPagerAdapter extends PagerAdapter {

        private List<ActivityData> activityDatas;

        public ViewPagerAdapter(List<ActivityData> activityDatas) {
            this.activityDatas = activityDatas;
        }

        public void setActivityBases(List<ActivityData> activityDatas) {
            this.activityDatas = activityDatas;
        }

        @Override
        public int getCount() {
            return Integer.MAX_VALUE;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public int getItemPosition(Object object) {
            return super.getItemPosition(object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            int size = activityDatas.size();
            View view = View.inflate(container.getContext(), R.layout.investment_view_pager_item, null);
//            ImageView imageView = new ImageView(container.getContext());
            // 如果小于1表示没有数据直接返回
            if (size < 1) {
                return view;
            }
            // 有数据
            final ActivityData activityData = activityDatas.get(position % size);

            ImageView imageView = (ImageView) view.findViewById(R.id.view_image);

            app.setOptions(R.drawable.home_activity_load);
            String url = Constant.IMAGE_ADDRESS + activityData.activeImage;
            app.setDisplayImage(url, imageView);

            container.addView(view);

            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (TextUtils.isEmpty(activityData.activeUrl) && TextUtils.isEmpty(activityData.activeImageUrl)) {
                        return;
                    }

                    Intent intent2 = new Intent(mActivity, WebViewActivity.class);
                    intent2.putExtra(ActivityData.class.getName(), activityData);
                    mActivity.startActivity(intent2);
                }
            });
            return view;

        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        handler.removeMessages(HANDLERID);
    }
}
