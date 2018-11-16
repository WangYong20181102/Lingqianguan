package com.wtjr.lqg.fragments;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.umeng.analytics.MobclickAgent;
import com.wtjr.lqg.Manifest;
import com.wtjr.lqg.R;
import com.wtjr.lqg.activities.HaveAcountsLoginActivity;
import com.wtjr.lqg.activities.LoginActivity;
import com.wtjr.lqg.activities.LqbDetailsActivity;
import com.wtjr.lqg.activities.LqbInvestmentActivity;
import com.wtjr.lqg.activities.RechargeActivity;
import com.wtjr.lqg.activities.RollInActivity;
import com.wtjr.lqg.activities.RollOutActivity;
import com.wtjr.lqg.activities.StartInvestmentActivity;
import com.wtjr.lqg.activities.WebViewActivity;
import com.wtjr.lqg.adapters.SimpleAdatper;
import com.wtjr.lqg.base.JSONBase;
import com.wtjr.lqg.base.LqbData;
import com.wtjr.lqg.basecommonly.BaseFragment;
import com.wtjr.lqg.constants.Constant;
import com.wtjr.lqg.enums.LqbDetailsType;
import com.wtjr.lqg.enums.RechargeType;
import com.wtjr.lqg.enums.RefreshType;
import com.wtjr.lqg.enums.RollInType;
import com.wtjr.lqg.sharedpreferences.SharedPreferencesUtil;
import com.wtjr.lqg.utils.ArithUtil;
import com.wtjr.lqg.utils.DialogUtil;
import com.wtjr.lqg.utils.HttpUtil.HttpRequesListener;
import com.wtjr.lqg.utils.MoneyFormatUtil;
import com.wtjr.lqg.utils.ToastUtil;
import com.wtjr.lqg.widget.F_ScrollViewContainer;
import com.wtjr.lqg.widget.F_ScrollViewContainer.OnPageListener;
import com.wtjr.lqg.widget.LqbUpDownTextView;
import com.wtjr.lqg.widget.UpDownTextView;
import com.wtjr.lqg.widget.XCRecyclerView;
import com.wtjr.lqg.widget.XCRecyclerView.OnSlidingStatelistener;
import com.wtjr.lqg.widget.periscope.PeriscopeLayout;
import com.wtjr.lqg.widget.pullRefresh.PullToRefreshLayout;
import com.wtjr.lqg.widget.pullRefresh.PullToRefreshLayout.OnRefreshListener;
import com.wtjr.lqg.widget.wave.WaveHelper;
import com.wtjr.lqg.widget.wave.WaveView;

import org.xutils.http.RequestParams;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;

/**
 * 零钱宝碎片
 *
 * @author Myf
 */
public class LqbFragment extends BaseFragment implements OnClickListener, OnSlidingStatelistener, OnRefreshListener, HttpRequesListener,
        OnPageListener {

    // 上部分----------------------------------------------------
    /**
     * 水波圆
     */
    private WaveView waveCircle;
    /**
     * 水波矩形
     */
    private WaveView waveRectangle;
    /**
     * 水波圆帮助类
     */
    private WaveHelper mWaveHelpeCircler;
    /**
     * 水波矩形帮助类
     */
    private WaveHelper mWaveHelperRectangle;
    /**
     * 冒泡控件
     */
    private PeriscopeLayout periscopeLayout;
    /**
     * 图片帮助按钮
     */
    private ImageButton imgBtnHelp;
    /**
     * 转入
     */
    private LinearLayout btnInto;
    private Button btnInto1;
    /**
     * 转出
     */
    private Button btnRollOut;
    /**
     * 年化收益
     */
    private TextView lqbrate;
    /**
     * 零钱宝资金
     */
    private LqbUpDownTextView udtvTotalAssets;
    /**
     * 体验金
     */
    private LqbUpDownTextView udtvExperienceMoney;
    /**
     * 累计收益
     */
    private LqbUpDownTextView udtvTotalEarnings;
    /**
     * 投资天数
     */
    private LqbUpDownTextView udtvInvestmentDay;
    /**
     * 昨日收益
     */
    private UpDownTextView udtvYesterDayIncome;
    /**
     * 下一页
     */
    private TextView tvToDown;
    /**
     * 利率
     */
    private float mLv = 0.07f;
    /**
     * 下一页图标
     */
    private ImageView ivToDown;
    /**
     * 上下ScrollView的控件
     */
    private F_ScrollViewContainer fsvContainer;

    /**
     * 刻度尺利率
     */
    private XCRecyclerView rvLv;
    /**
     * 刻度尺数据
     */
    private ArrayList<String> mDatas;
    /**
     * 刻度尺数据
     */
    private SimpleAdatper mSimpleAdatper;
    /**
     * 获取屏幕一半的宽度
     */
    private int screenWidthHalf;
    /**
     * 得到手机像素密度
     */
    private float mDensity;
    /**
     * 显示刻度值的TextView
     */
    private EditText tvScaleValue;
    /**
     * 下拉刷新最外层布局
     */
    private PullToRefreshLayout pullToRefreshLayout;
    /**
     * 上部分
     */
    private ScrollView svUp;

    /**
     * 刻度尺最大金额100000
     */
    private final float MAX_MONEY_100000 = 200000.00f;
    /**
     * 零钱宝定期每元钱的高度
     */
    private double perH;
    /**
     * 银行，宝宝类，零钱宝，零钱宝定期各自的年化收益率
     */
    private double l1 = 0.0035, l2 = 0.0243, l3 = 0.07, l4 = 0.1488;
    private TextView tvInvestAndEarnings;
    private RelativeLayout rlTitle;
    private View vLqbTitleHeight;
    private TextView tvTitleName;
    private LinearLayout llLqb;
    /**
     * 零钱宝最小利率
     */
    private String minLqbRate;
    /**
     * 零钱宝最大利率
     */
    private String maxLqbRate;
    /**
     * 初始化利率值
     */
    private int intNum = 100000;
    /**
     * 顶部通知
     */
    private TextView tvActionContext1;
    private TextView tvActionContext2;
    private ImageButton imageActionClick;
    private RelativeLayout rlLqbAction;
    /**
     * 判断上拉或者下拉
     */
    private boolean bCanDown = true;
    /**
     * 四个收益柱形图的所在父布局
     */
    private LinearLayout llPillarView;
    // 屏幕中心位置
    private int screenCentre;
    /**
     * 判断是手动输入还是滑动
     */
    private boolean bInputOrSlide = false;
    private View tittleBottomLine;
    /**
     * 零钱宝水波圆下方文字
     */
    private TextView textView2;
    /**
     * 投资最大利率
     */
    private String maxInvestRate;

    /**
     * 转到周周升对话框
     */
    private Dialog dialog;


    // 下部分----------------------------------------------------
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lqb, container, false);
        start(view);
        return view;
    }

    @Override
    public void findViewById(View view) {
        // 下拉刷新
        pullToRefreshLayout = (PullToRefreshLayout) view.findViewById(R.id.pullTo_refreshLayout);
        fsvContainer = (F_ScrollViewContainer) view.findViewById(R.id.scrollView_container);
        tittleBottomLine = view.findViewById(R.id.title_bottom_line);
        //顶部通知
        tvActionContext1 = (TextView) view.findViewById(R.id.tv_action_context1);
        tvActionContext2 = (TextView) view.findViewById(R.id.tv_action_context2);
        imageActionClick = (ImageButton) view.findViewById(R.id.image_down);
        rlLqbAction = (RelativeLayout) view.findViewById(R.id.rl_lqb_action);
        initViewUp(view);
        initViewDown(view);
    }

    /**
     * 初始化上部分布局
     *
     * @param view
     */
    private void initViewUp(View view) {
        textView2 = (TextView) view.findViewById(R.id.textView2);

        svUp = (ScrollView) view.findViewById(R.id.sv_up);
        // 水波圆
        waveCircle = (WaveView) view.findViewById(R.id.wave_circle);
        // 水波矩形
        waveRectangle = (WaveView) view.findViewById(R.id.wave_rectangle);
        // 冒泡控件
        periscopeLayout = (PeriscopeLayout) view.findViewById(R.id.periscope);
        // 帮助图标按钮
        imgBtnHelp = (ImageButton) view.findViewById(R.id.imgBtn_help);
        // 年化收益
        lqbrate = (TextView) view.findViewById(R.id.textView1);
        // 转入
        btnInto = (LinearLayout) view.findViewById(R.id.btn_into);
        btnInto1 = (Button) view.findViewById(R.id.btn_into1);
        // 转出
        btnRollOut = (Button) view.findViewById(R.id.btn_roll_out);
        // 零钱宝资金
        udtvTotalAssets = (LqbUpDownTextView) view.findViewById(R.id.udtv_total_assets);
        // 体验金
        udtvExperienceMoney = (LqbUpDownTextView) view.findViewById(R.id.udtv_experience_money);
        // 累计收益
        udtvTotalEarnings = (LqbUpDownTextView) view.findViewById(R.id.udtv_total_earnings);
        // 投资天数
        udtvInvestmentDay = (LqbUpDownTextView) view.findViewById(R.id.udtv_investment_day);
        // 昨日收益
        udtvYesterDayIncome = (UpDownTextView) view.findViewById(R.id.lqb_yesterday_income);
        // 下一页
        tvToDown = (TextView) view.findViewById(R.id.tv_to_down);
        // 下一页图标
        ivToDown = (ImageView) view.findViewById(R.id.iv_to_down);


        llLqb = (LinearLayout) view.findViewById(R.id.ll_lqb);

        fsvContainer.setTvToDown(tvToDown, ivToDown);

        if (!TextUtils.isEmpty(minLqbRate) && !TextUtils.isEmpty(maxLqbRate)) {
            Spanned fromHtml = Html.fromHtml("<font color=#ffffff>" + "历史年化收益" + minLqbRate + "%起，每周涨0.5%，可达" + "</font>");
            Spanned fromHtml2 = Html.fromHtml("<font color=#fffa88>" + maxLqbRate + "%" + "</font>");
            textView2.setText(fromHtml);
            textView2.append(fromHtml2);
            // 年化利率
            NumberFormat nf = new DecimalFormat("0.0#");
            String testStr = nf.format(Double.parseDouble(minLqbRate));
            lqbrate.setText(testStr);
        } else {
            Spanned fromHtml = Html.fromHtml("<font color=#ffffff>" + "历史年化收益7.0%起，每周涨0.5%，可达" + "</font>");
            Spanned fromHtml2 = Html.fromHtml("<font color=#fffa88>" + "9.88%" + "</font>");
            textView2.setText(fromHtml);
            textView2.append(fromHtml2);
        }

    }

    @Override
    public void initIntent() {
        minLqbRate = SharedPreferencesUtil.getPrefString(getActivity(), "minLqbRate", "");
        maxLqbRate = SharedPreferencesUtil.getPrefString(getActivity(), "maxLqbRate", "");
        maxInvestRate = SharedPreferencesUtil.getPrefString(getActivity(), "maxInvestRate", "");
    }

    /**
     * 初始化下部分布局
     *
     * @param view
     */
    private void initViewDown(View view) {
        rvLv = (XCRecyclerView) view.findViewById(R.id.rv_lv);
        tvScaleValue = (EditText) view.findViewById(R.id.tv_scale_value);

        llPillarView = (LinearLayout) view.findViewById(R.id.ll_pillarView);

        rlTitle = (RelativeLayout) view.findViewById(R.id.rl_title);
        tvTitleName = (TextView) view.findViewById(R.id.tv_title_name);
        vLqbTitleHeight = view.findViewById(R.id.v_lqb_title_height);

        tvInvestAndEarnings = (TextView) view.findViewById(R.id.tv_investAndEarnings);

        // 以下是防止上部分无法填满一屏
        final RelativeLayout rlLqgUp = (RelativeLayout) view.findViewById(R.id.rl_lqg_up);
        rlLqgUp.post(new Runnable() {
            @Override
            public void run() {
                int height = rlLqgUp.getHeight();

                if (height < mMainActivity.mFragmentLayoutHeight) {
                    LayoutParams params = (LayoutParams) rlLqgUp.getLayoutParams();
                    params.height = mMainActivity.mFragmentLayoutHeight;
                }
            }
        });

        rvLv.post(new Runnable() {
            @Override
            public void run() {
                // 刻度尺中一个Item的长度
                float widthItem = app.getResources().getDimension(R.dimen.d100);
                // 刻度尺的总长度
                // float totalWidth = widthItem * MAX_MONEY_100000/100 + 1;
                // 定义刻度尺的初始位置
                // rvLv.scrollBy((int)totalWidth, 0);

                // 定义刻度尺的初始位置 (中间)
                float totalWidth = widthItem * MAX_MONEY_100000 / 5000 / 2;
                rvLv.scrollBy((int) totalWidth, 0);
            }
        });
    }

    @Override
    public void setListener() {
        // 上部分----------------------------------------------------
        // 帮助图标点击监听
        imgBtnHelp.setOnClickListener(this);
        // 转入
        btnInto.setOnClickListener(this);
        btnInto1.setOnClickListener(this);
        // 转出
        btnRollOut.setOnClickListener(this);
        // 总资产
        udtvTotalAssets.setOnClickListener(this);
        // 体验金
        udtvExperienceMoney.setOnClickListener(this);
        // 累计收益
        udtvTotalEarnings.setOnClickListener(this);
        // 投资天数
        udtvInvestmentDay.setOnClickListener(this);
        // 昨日收益
        udtvYesterDayIncome.setOnClickListener(this);
        // 下一页
        tvToDown.setOnClickListener(this);

        mHttpUtil.setHttpRequesListener(this);

        rvLv.setOnSlidingStatelistener(this);
        // 下拉刷新监听器
        pullToRefreshLayout.setOnRefreshListener(this);
        // 页数监听
        fsvContainer.setOnPageListener(this);

        imageActionClick.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_earningsView:      //点击空白区域进行计算
                setBottomKaChi();
                break;
            case R.id.image_down:   //顶部通知下拉
                if (bCanDown) {
                    bCanDown = false;
                    imageActionClick.setImageResource(R.drawable.lqb_action_up_icon);
                    tvActionContext1.setVisibility(View.GONE);
                    tvActionContext2.setVisibility(View.VISIBLE);
                    tvActionContext2.setText(app.mLqbData.lqbConfig.notice);
                } else {
                    bCanDown = true;
                    imageActionClick.setImageResource(R.drawable.lqb_action_down_icon);
                    tvActionContext2.setVisibility(View.GONE);
                    tvActionContext1.setVisibility(View.VISIBLE);
                    tvActionContext1.setText(app.mLqbData.lqbConfig.notice);
                }
                break;
        }
        if (!isLogin) {// 没账号登录
            if (!mFirstInState) {// 不是第一次使用（有账号登录过）
                startActivity(new Intent(getActivity(), HaveAcountsLoginActivity.class));
            } else {// 不是第一次使用（没有账号登录过）
                startActivity(new Intent(getActivity(), LoginActivity.class));
            }
            return;
        }

        Intent intent = null;
        switch (v.getId()) {
            case R.id.imgBtn_help:// 帮助
                intent = new Intent(getActivity(), WebViewActivity.class);
                intent.putExtra("url", Constant.H5_LQB_HELP);
                intent.putExtra("TitleName", "零钱宝帮助");
                break;
            case R.id.btn_into1:// 转入
                RollInDialog();
                break;
            case R.id.btn_into: //转入周周升
                if (app.mLqbData.planStatus == 1) {
                    dialog = DialogUtil.rollInLiCaiPlanDialog(getActivity(), app.mAccountData.lqb,  new DialogUtil.OnSetMoneyListener() {
                        @Override
                        public void getMoney(String money) {
                            if (Double.parseDouble(money) < app.mLqbData.investMin && app.mLqbData.investMin > 0) {
                                ToastUtil.showToastLong(getActivity(), "起投金额不足" + MoneyFormatUtil.format2(app.mLqbData.investMin) + "元");
                            } else if (Double.parseDouble(money) > app.mLqbData.investMax && app.mLqbData.investMax > 0) {
                                ToastUtil.showToastLong(getActivity(), "单笔投资金额不得超过" + MoneyFormatUtil.format2(app.mLqbData.investMax) + "元");
                            } else {
                                sendInvestmentRequest(RefreshType.RefreshNoPull, money);
                                if (dialog.isShowing()) {
                                    dialog.dismiss();
                                }
                            }
                        }
                    });
                } else {
                    ToastUtil.showToastLong(getActivity(), "计划已满");
                }

                break;
            case R.id.btn_roll_out:// 转出
                startActivity(new Intent(getActivity(), RollOutActivity.class));
                break;
            case R.id.udtv_total_assets:// 零钱宝资金
                intent = new Intent(getActivity(), LqbDetailsActivity.class);
                intent.putExtra(LqbDetailsType.class.getName(), LqbDetailsType.All);
                break;
            case R.id.udtv_experience_money:// 体验金
                intent = new Intent(getActivity(), LqbDetailsActivity.class);
                intent.putExtra(LqbDetailsType.class.getName(), LqbDetailsType.TYJ);
                break;
            case R.id.udtv_total_earnings:// 累计收益
                intent = new Intent(getActivity(), LqbDetailsActivity.class);
                intent.putExtra(LqbDetailsType.class.getName(), LqbDetailsType.Earnings);
                break;
            case R.id.udtv_investment_day:// 投资天数
                startActivity(new Intent(getActivity(), LqbInvestmentActivity.class));
                break;
            case R.id.lqb_yesterday_income:// 昨日收益
                break;
            case R.id.tv_to_down:// 下一页
                fsvContainer.autoDown();
                break;
        }

        if (intent != null) {
            startActivity(intent);
        }
    }

    /**
     * 发送投资请求
     */
    public void sendInvestmentRequest(RefreshType refreshType, String mInvestMoney) {

        RequestParams params = new RequestParams(Constant.PLANINVEST_IMMEDIATEINVESTMENT);
        params.addBodyParameter("user_userunid", mUid);
        params.addBodyParameter("invest_money", mInvestMoney);
        params.addBodyParameter("invest_way", "3");
        params.addBodyParameter("pay_password", "");
        params.addBodyParameter("planId", "");

        mHttpUtil.sendRequest(params, refreshType, getActivity());
    }

    /**
     * 设置底部卡尺数据
     */
    private void setBottomKaChi() {
        bInputOrSlide = true;
        String strInputNum = tvScaleValue.getText().toString();
        int numMoney;
        if (TextUtils.isEmpty(strInputNum)) {
            numMoney = 100;
        } else {
            numMoney = Integer.parseInt(strInputNum);
            if (numMoney < 100) {
                numMoney = 100;
            }
        }
        tvScaleValue.setText(numMoney + "");
        intNum = numMoney;
        earningsShow(numMoney);
        final int finalDNum = numMoney;
        rvLv.post(new Runnable() {
            @Override
            public void run() {
                // 刻度尺中一个Item的长度
                float widthItem = app.getResources().getDimension(R.dimen.d100);
                float totalWidth = widthItem * finalDNum / 5000;
                totalWidth = totalWidth - (screenCentre / 50);
                rvLv.scrollBy((int) totalWidth, 0);
            }
        });
    }

    /**
     * 转入方式选择的对话框
     */
    public void RollInDialog() {
        final Dialog dialog = new Dialog(getActivity(), R.style.dialog);
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_roll_in, null);
        dialog.setContentView(view);
        dialog.setCanceledOnTouchOutside(false);

        ImageView closeIV = (ImageView) view.findViewById(R.id.iv_close);
        // 体验金
        LinearLayout tyjShiftToLL = (LinearLayout) view.findViewById(R.id.ll_balance_shiftTo2);
        TextView tyjBalanceTV = (TextView) view.findViewById(R.id.tv_shiftToBalance2);
        // 余额
        RelativeLayout balanceShiftToLL = (RelativeLayout) view.findViewById(R.id.ll_balance_shiftTo);
        TextView balanceTV = (TextView) view.findViewById(R.id.tv_shiftToBalance);

        // 银行卡
        LinearLayout llBank = (LinearLayout) view.findViewById(R.id.ll_bank);
        // 体验金的
        tyjBalanceTV.setText("余额:" + MoneyFormatUtil.format(app.mAccountData.usableExpMoney));
        // 余额
        balanceTV.setText("余额:" + MoneyFormatUtil.format(app.mAccountData.available_money));

        llBank.setVisibility(View.VISIBLE);

        if (app.mAccountData.usableExpMoney == 0) {
            tyjShiftToLL.setVisibility(View.GONE);
        }

        if (app.mAccountData.available_money == 0) {
            balanceShiftToLL.setVisibility(View.GONE);
        }

        if (app.mAccountData.usableExpMoney == 0 && app.mAccountData.available_money == 0) {
            Intent intent = new Intent(getActivity(), RechargeActivity.class);
            intent.putExtra(RechargeType.class.getName(), RechargeType.Lqb);
            startActivity(intent);
            return;
        }

        // 点击关闭Dialog
        closeIV.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        // 点击体验金转入的监听
        tyjShiftToLL.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                sendRollInRequest(RefreshType.RefreshNoPull);
                dialog.dismiss();
            }
        });

        // 点击余额转入的监听
        balanceShiftToLL.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                Intent intentShiftTo = new Intent(getActivity(), RollInActivity.class);
                // 转入形式(use_money-->余额转入,tyj-->体验金转入)
                intentShiftTo.putExtra("RollInType", RollInType.Balance);
                startActivity(intentShiftTo);

                dialog.dismiss();
            }
        });
        // 银行卡
        llBank.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), RechargeActivity.class);
                intent.putExtra(RechargeType.class.getName(), RechargeType.Lqb);
                startActivity(intent);

                dialog.dismiss();
            }
        });

        dialog.show();
    }

    /**
     * 发送转入请求
     */
    public void sendRollInRequest(RefreshType refreshType) {
        RequestParams params = new RequestParams(Constant.LQB_EXPMONEYINTOLQB);
        params.addBodyParameter("user_userunid", app.mAccountData.uId);
        params.addBodyParameter("expMoney2lqb", app.mAccountData.usableExpMoney + "");// 体验金转入
        mHttpUtil.sendRequest(params, refreshType, getActivity());
    }

    @Override
    public void setTitle() {
        rlTitle.setBackgroundColor(getResources().getColor(R.color.lqb_head_bg1));
        tvTitleName.setText("零钱宝");
        tvTitleName.setTextColor(getResources().getColor(R.color.white));
        tittleBottomLine.setVisibility(View.GONE);
    }

    /**
     * 记录第一次点击的时间
     */
    private long clickTime = 0;

    @Override
    public void refreshData(RefreshType refreshType) {
        // //先判断
        if (getActivity() == null || fsvContainer == null || pullToRefreshLayout == null) {
            return;
        }

        boolean noviceGuide4 = SharedPreferencesUtil.getPrefBoolean(getActivity(), "NoviceGuide4", false);
        if (!noviceGuide4) {
            refreshType = RefreshType.RefreshNoPull;
        }
        switch (refreshType) {
            case RefreshPull:
                // 如果是在第一页跳到顶部，在刷新
                if (fsvContainer.getCurrentViewIndex() == 0) {
                    svUp.fullScroll(ScrollView.FOCUS_UP);
                } else {
                    // 先返回上一页
                    fsvContainer.autoUp();
                    return;
                }

                // 界面停止操作，用户已抬起手指(ACTION_UP-----TRIGGER是true),并且头部刷新的高度是0(RefreshHeadHeight==0)，证明刷新完成
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
                    sendLQBRequest(RefreshType.RefreshNoPull);
                }
                break;
        }
    }

    @Override
    public void initData() {
        initDataUp();
        addBlisters();
        initScaleData();
        updateData(app.mLqbData);
    }

    /**
     * 初始化上部分
     */
    private void initDataUp() {
        setWaveColor();
        if (mLv > 1) {
            mLv = 1;
        }

        float density = getResources().getDisplayMetrics().density;
        waveCircle.setBorder(6 * density, Color.TRANSPARENT);
        waveCircle.setShapeType(WaveView.ShapeType.CIRCLE);
        mWaveHelpeCircler = new WaveHelper(waveCircle, mLv, 5000, 0.03f);

        waveRectangle.setShapeType(WaveView.ShapeType.SQUARE);
        waveRectangle.setBorder(0, Color.TRANSPARENT);
        mWaveHelperRectangle = new WaveHelper(waveRectangle, 0.05f, 5000, 0.03f);
        float fWaterWaveMax = 0.0988f;
        if (!TextUtils.isEmpty(maxLqbRate)) {
            fWaterWaveMax = Float.parseFloat(maxLqbRate) / 100f;
        }
        waveCircle.setMaxCircleArcDegree(mLv * 360f / fWaterWaveMax);

        mWaveHelpeCircler.start();
        mWaveHelperRectangle.start();
    }

    /**
     * 初始化下部分数据
     */
    private void initDataDown() {

        // 计算10万预计30天收益
        String sMoney = MoneyFormatUtil.sEarnings(intNum, mLv, 365, 30);

        Spanned fromHtml = Html.fromHtml("投资" + "<font color=#fa8c1e>" + intNum + "</font>" + "元，");
        Spanned fromHtml2 = Html.fromHtml("<font color=#fa8c1e>" + 30 + "</font>" + "天最多可赚" + "<font color=#fa8c1e>" + sMoney + "</font>" + "元");
        tvInvestAndEarnings.setText(fromHtml);
        tvInvestAndEarnings.append(fromHtml2);
    }

    /**
     * 初始化刻度尺数据
     */
    private void initScaleData() {
        if (!TextUtils.isEmpty(maxInvestRate)) {
            l4 = Double.parseDouble(maxInvestRate) / 100;
        }
        // 得到像素密度
        mDensity = app.mDensity;
        // 得到屏幕宽的一半
        screenWidthHalf = app.mScreenWidth / 2;

        mDatas = new ArrayList<String>();
        for (int i = 0; i < MAX_MONEY_100000 / 5000 + 1; i++) {
            if (i == 0) {
                mDatas.add("100");
            } else {
                mDatas.add("" + i * 5000);
            }
        }

        mSimpleAdatper = new SimpleAdatper(getActivity(), mDatas);

        rvLv.setLayoutManager(new LinearLayoutManager(getActivity()));

        View mHeaderView = LayoutInflater.from(getActivity()).inflate(R.layout.head_foot_xc_recycler, rvLv, false);
        View mFooterView = LayoutInflater.from(getActivity()).inflate(R.layout.head_foot_xc_recycler, rvLv, false);
        rvLv.addHeaderView(mHeaderView);
        rvLv.addFooterView(mFooterView);

        rvLv.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.HORIZONTAL));
        rvLv.setAdapter(mSimpleAdatper);

        rvLv.setOnScrollListener(new RecyclerView.OnScrollListener() {
            // 屏幕中心位置值
            private int screenCentreValue;

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                // 获得中心红线的位置
                screenCentre += 50 * dx;
                // 获得中心红线的位置值
                screenCentreValue = (int) (screenCentre / mDensity);

                if (screenCentreValue < 100) {
                    screenCentreValue = 100;
                }
                if (bInputOrSlide) {
                    bInputOrSlide = false;
                } else {
                    intNum = screenCentreValue;
                    tvScaleValue.setText(screenCentreValue + "");
                    earningsShow(screenCentreValue);
                }
            }
        });
    }

    /**
     * 用于计算收益柱状图的数据
     *
     * @param newCurrent
     */
    private void earningsShow(int newCurrent) {

        Spanned fromHtml = Html.fromHtml("投资" + "<font color=#fa8c1e>" + newCurrent + "</font>" + "元，");
        Spanned fromHtml2 = Html.fromHtml("<font color=#fa8c1e>" + 30 + "</font>" + "天最多可赚" + "<font color=#fa8c1e>" + MoneyFormatUtil.sEarnings2
                (newCurrent, l3, 365, 30) +
                "</font>" + "元");
        tvInvestAndEarnings.setText(fromHtml);
        tvInvestAndEarnings.append(fromHtml2);
    }

    /**
     * 添加水泡
     */
    private void addBlisters() {
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                periscopeLayout.addHeart();
                addBlisters();
            }
        }, 1000);
    }

    /**
     * 设置水波的颜色
     */
    private void setWaveColor() {
        waveCircle.post(new Runnable() {
            @Override
            public void run() {
                waveCircle.setWaveColor(Color.parseColor("#FFE773"), Color.parseColor("#FFF5C7"));
//                waveCircle.setWaveColor(Color.parseColor("#FB9D52"), Color.parseColor("#fdcda9"));
            }
        });

        waveRectangle.post(new Runnable() {
            @Override
            public void run() {
//                waveRectangle.setWaveColor(Color.parseColor("#faa741"), Color.parseColor("#fcc670"));
                waveRectangle.setWaveColor(Color.parseColor("#FCC729"), Color.parseColor("#FDDC78"));
            }
        });
    }

    @Override
    public void slidingState(boolean state) {
        if (fsvContainer.canPullDown) {
            if (state) {
                fsvContainer.canPullDown = false;
            } else {
                fsvContainer.canPullDown = true;
            }
        }
    }

    private RefreshType mRefreshType;

    @Override
    public void onRefresh() {
        if (isLogin) {
            sendLQBRequest(RefreshType.RefreshPull);
        } else {
            pullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
        }
    }

    @Override
    public void onLoadMore() {

    }

    /**
     * 发送零钱宝数据请求
     *
     * @param refreshType
     */
    public void sendLQBRequest(RefreshType refreshType) {
        mRefreshType = refreshType;

        RequestParams params = new RequestParams(Constant.LQB_QUERYLQBINFO);
        params.addBodyParameter("user_userunid", mUid);
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

        if (url.equals(Constant.LQB_QUERYLQBINFO)) {// 零钱宝数据请求
            LqbData lqbData = JSON.parseObject(jsonBase.getDisposeResult(), LqbData.class);
            app.updateData(lqbData);
            updateData(lqbData);
        } else if (url.equals(Constant.LQB_EXPMONEYINTOLQB)) {// 体验金转入
            // 发送账户数据和零钱宝数据更新广播
            Intent mIntent = new Intent(Constant.UPDATE_ACCOUNTDATA_LQBDATA);
            mIntent.putExtra("ShowFragmentLocation", 1);
            getActivity().sendBroadcast(mIntent, Manifest.permission.receiver_permission);
        } else if (url.equals(Constant.PLANINVEST_IMMEDIATEINVESTMENT)) {   //周周升投资

            DialogUtil.promptDialog(getActivity(), DialogUtil.HEAD_BAND_BANK, jsonBase.getMessage(), new
                    DialogUtil.OnClickYesListener() {
                        @Override
                        public void onClickYes() {
                            // 发送账户数据和零钱宝数据更新广播
                            Intent mIntent = new Intent(Constant.UPDATE_ACCOUNTDATA_LQBDATA);
                            mIntent.putExtra("ShowFragmentLocation", 1);
                            getActivity().sendBroadcast(mIntent, Manifest.permission.receiver_permission);
                            sendLQBRequest(RefreshType.RefreshNoPull);
                        }
                    });
        }

    }

    /**
     * 网络请求成功后更新页面数据
     */
    private void updateData(LqbData lqbData) {
        if (!isLogin || lqbData == null) {
            udtvYesterDayIncome.setUpTextContent("预计明日收益(元)");
            return;
        }

        String totalAssets = udtvTotalAssets.getDownTextContent();
        String experienceMoney = udtvExperienceMoney.getDownTextContent();
        String totalEarnings = udtvTotalEarnings.getDownTextContent();

        // 总资产
        udtvTotalAssets.withNumber(totalAssets, lqbData.lqb + "");
        // 体验金
        udtvExperienceMoney.withNumber(experienceMoney, lqbData.tyjinlqb + "");
        // 累计收益
        udtvTotalEarnings.withNumber(totalEarnings, lqbData.lqbTotalIncome + "");
        // 投资天数
        udtvInvestmentDay.setDownTextContent(lqbData.investmentDays + "");
        //是否显示通知
        if (app.mLqbData.lqbConfig != null) {
            tvActionContext1.setText(app.mLqbData.lqbConfig.notice);
            if (app.mLqbData.lqbConfig.isOpenNotice == 1) {
                rlLqbAction.setVisibility(View.VISIBLE);
            } else {
                rlLqbAction.setVisibility(View.GONE);
            }
            //显示转入还是转到周周升
            if (app.mLqbData.lqbConfig.isOpenTransferinto == 1) {
                btnInto.setVisibility(View.VISIBLE);
                btnInto1.setVisibility(View.GONE);
            } else {
                btnInto.setVisibility(View.GONE);
                btnInto1.setVisibility(View.VISIBLE);
            }
        }


        Spanned fromHtml = Html.fromHtml("<font color=#3f1e00>" + "历史年化收益" + lqbData.minLqbRate + "%起，每周涨0.5%，可达" + "</font>");
        Spanned fromHtml2 = Html.fromHtml("<font color=#ff7c03>" + lqbData.maxLqbRate + "%" + "</font>");
        textView2.setText(fromHtml);
        textView2.append(fromHtml2);

        if (lqbData.lqb == 0) {
            btnRollOut.setTextColor(getResources().getColor(R.color.FC_999999));
            btnRollOut.setEnabled(false);
        } else {
            btnRollOut.setTextColor(getResources().getColor(R.color.FC_FF9900));
            btnRollOut.setEnabled(true);
        }

        // 年化利率
        NumberFormat nf = new DecimalFormat("0.0#");
        String testStr = nf.format(lqbData.lqbRate);
        lqbrate.setText(testStr);

        mLv = (float) lqbData.lqbRate / 100;
        l3 = mLv;

        initDataUp();// 刷新水球的数据
        waveCircle.isUpOrDown(false);// 水球外边圆的回退
        initDataDown();
        // 昨日收益
        if (lqbData.lqbYesterdayIncome == 0) {
            udtvYesterDayIncome.setUpTextContent("预计明日收益(元)");

            float dimension1 = getActivity().getResources().getDimension(R.dimen.s16);
            float dimension2 = getActivity().getResources().getDimension(R.dimen.s36);

            if (lqbData.lqb < 100 && lqbData.tyjinlqb < 100) {
                udtvYesterDayIncome.setDownTextSize(dimension1);
                udtvYesterDayIncome.setDownTextContent("零钱宝余额不足100元，没有收益");
            } else {
                double lqbIncome = ArithUtil.round(lqbData.lqb * (lqbData.lqbRate / 100d) / 365d, 2);
                double tyjIncome = ArithUtil.round(lqbData.tyjinlqb * (lqbData.lqbRate / 100d) / 365d, 2);
                udtvYesterDayIncome.setDownTextSize(dimension2);
                udtvYesterDayIncome.setDownTextContent(MoneyFormatUtil.format(lqbIncome + tyjIncome));
            }
        } else {
            udtvYesterDayIncome.setUpTextContent("昨日收益(元)");
            udtvYesterDayIncome.setDownTextContent(MoneyFormatUtil.format(lqbData.lqbYesterdayIncome));
        }
    }

    /**
     * 当前滑动的是哪一页
     */
    @Override
    public void page(int p) {
        switch (p) {
            case 0:
                rlTitle.setVisibility(View.GONE);
                vLqbTitleHeight.setVisibility(View.GONE);
                llLqb.setBackgroundColor(Color.parseColor("#ffdc36"));
                break;
            case 1:
                rlTitle.setVisibility(View.VISIBLE);
                vLqbTitleHeight.setVisibility(View.VISIBLE);
                llLqb.setBackgroundColor(getResources().getColor(R.color.lqb_head_bg1));

                boolean noviceGuide5 = SharedPreferencesUtil.getPrefBoolean(getActivity(), "NoviceGuide5", false);
                if (!noviceGuide5) {// 没执行过该新手引导
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mMainActivity.noviceGuide5(llPillarView.getHeight());
                        }
                    }, 100);
                }
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        // 友盟统计
        MobclickAgent.onPageStart("LqbFragment");
    }

    @Override
    public void onPause() {
        super.onPause();
        // 友盟统计
        MobclickAgent.onPageEnd("LqbFragment");
    }

}
