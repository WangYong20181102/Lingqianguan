package com.wtjr.lqg;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewStub;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.umeng.socialize.UMShareAPI;
import com.wtjr.lqg.activities.HaveAcountsLoginActivity;
import com.wtjr.lqg.activities.LoginActivity;
import com.wtjr.lqg.activities.WebViewActivity;
import com.wtjr.lqg.base.AccountData;
import com.wtjr.lqg.base.JSONBase;
import com.wtjr.lqg.base.PopupPage;
import com.wtjr.lqg.basecommonly.BaseActivity;
import com.wtjr.lqg.basecommonly.BaseAppManager;
import com.wtjr.lqg.constants.Constant;
import com.wtjr.lqg.enums.RefreshType;
import com.wtjr.lqg.fragments.HomePageFragment;
import com.wtjr.lqg.fragments.InvestmentFragment;
import com.wtjr.lqg.fragments.LqbFragment;
import com.wtjr.lqg.fragments.MeFragment;
import com.wtjr.lqg.sharedpreferences.SaveCurrentUidUtil;
import com.wtjr.lqg.sharedpreferences.SaveFirstInState;
import com.wtjr.lqg.sharedpreferences.SharedPreferencesUtil;
import com.wtjr.lqg.utils.HttpUtil.HttpRequesListener;
import com.wtjr.lqg.utils.LocationUtil;
import com.wtjr.lqg.utils.LocationUtil.OnLocationListener;
import com.wtjr.lqg.utils.RSAUtils;
import com.wtjr.lqg.widget.NavigationBarItem;
import com.wtjr.lqg.widget.XCRoundRectImageView;
import com.wtjr.lqg.widget.wave.WaveHelper;
import com.wtjr.lqg.widget.wave.WaveView;

import org.xutils.http.RequestParams;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;


/**
 * 主Activity
 *
 * @author Myf
 */
public class MainActivity extends BaseActivity implements OnClickListener, HttpRequesListener, ViewPager.OnPageChangeListener {
    /**
     * 主页位置
     */
    private static final int HOME_PAGE_POSITION = 0;

    /**
     * 零钱宝位置
     */
    private static final int LQB_POSITION = 1;

    /**
     * 投资位置
     */
    private static final int INVESTMENT_POSITION = 2;

    /**
     * 我位置
     */
    private static final int ME_POSITION = 3;

    /**
     * 底部导航条主页
     */
    private NavigationBarItem nbiHomePage;

    /**
     * 底部导航条零钱宝
     */
    private NavigationBarItem nbiLqb;

    /**
     * 底部导航条投资
     */
    private NavigationBarItem nbiInvestment;

    /**
     * 底部导航条我
     */
    private NavigationBarItem nbiMe;

    /**
     * 底部导航条父布局
     */
    private static LinearLayout llFoot;

    /**
     * 声明一个List集合存放在Fragment(数据源)
     */
    private List<Fragment> fragmentList = new ArrayList<Fragment>();

    /**
     * 主页碎片
     */
    private HomePageFragment homePageFragment;

    /**
     * 零钱宝碎片
     */
    private LqbFragment lqbFragment;

    /**
     * 投资碎片
     */
    private InvestmentFragment investmentFragment;

    /**
     * 我碎片
     */
    private MeFragment meFragment;

    /**
     * 碎片管理者
     */
    private FragmentManager fragmentManager;

    /**
     * 碎片事务
     */
    private FragmentTransaction transaction;

    /**
     * 最后一次选中的碎片默认用首页碎片
     */
    private Fragment lastFragment;

    private LinearLayout llFragmentLayout;
    /**
     * 零钱宝最小利率
     */
    private String sLqbMinRote;
    /**
     * 零钱宝最大利率
     */
    private String sLqbMaxRote;


    /**
     * 放碎片(Fragment)布局的高度
     */
    public int mFragmentLayoutHeight;
    List<PopupPage> popupPageHome;

    /**
     * 定义一个数据更新广播接收类
     */
    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            final int location = intent.getIntExtra("ShowFragmentLocation", 0);

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (!action.equals(Constant.UPDATE_MEDATA)) {
                        showFragment(location);
                    }
                }
            }, 100);

            if (!TextUtils.isEmpty(action)) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        /**
                         * 调用对应碎片界面的下拉刷新以刷新数据
                         */
                        updateData(action, location);
                    }
                }, 200);
            }

        }
    };

    /**
     * 首页新手引导
     */
//    private ViewStub vsGuide1;

    private ViewStub vsGuide2;

    /**
     * 新手引导背景覆盖物
     */
    private RelativeLayout rlCoverBg;

    private ViewPager ivAd;
    private ViewPagerAdapter adapter;

    /**
     * 广告背景覆盖物
     */
    private RelativeLayout rlCoverAdBg;

    private WaveView waveCircle;

    private ViewStub vsGuide4;
    /**
     * 引导页年化收益
     */
    private TextView guidePageEarning;
    /**
     * 引导页最高利率
     */
    private TextView guidePageBigRate;

    private ViewStub vsGuide5;
    private ImageView btClose;
    private LinearLayout llHomePoints;

    /**
     * 显示哪个碎片界面
     *
     * @param location 第几个
     */
    private void showFragment(int location) {
        // 显示哪个碎片
        if (location == 0) {
            setNavigationStyle(location);
        } else if (location == 1) {
            setNavigationStyle(location);
        } else if (location == 2) {
            setNavigationStyle(location);
        }
    }

    /**
     * 调用对应碎片界面的下拉刷新以刷新数据
     *
     * @param action   刷新那个界面的广播意图
     * @param location 正在显示的是哪个页面(不显示的界面无法调用下拉刷新)
     */
    private void updateData(String action, int location) {
        if (action.equals(Constant.UPDATE_ACCOUNTDATA)) {// 账户数据更新广播的Action(显示的是首页界面)
            // 更新主页数据
            if (homePageFragment != null) {
                homePageFragment.refreshData(RefreshType.RefreshPull);
            }
            //零钱宝转入成功也需要刷新数据
            lqbFragment.refreshData(RefreshType.RefreshPull);// 刷新零钱宝
        } else if (action.equals(Constant.UPDATE_LQBDATA)) {// 零钱宝数据更新广播的Action(显示的是零钱宝界面)
            // 更新零钱宝数据
            if (lqbFragment != null) {
                lqbFragment.refreshData(RefreshType.RefreshPull);
            }
        } else if (action.equals(Constant.UPDATE_ACCOUNTDATA_LQBDATA)) {// 账户数据和零钱宝数据更新广播的Action
            switch (location) {
                case 0:// 显示的是首页
                    // 更新主页数据
                    if (homePageFragment != null) {
                        homePageFragment.refreshData(RefreshType.RefreshPull);
                    }

                    // 更新零钱宝数据
                    if (lqbFragment != null) {
                        lqbFragment.refreshData(RefreshType.RefreshNoPull);// 不显示的界面无法调用下拉刷新
                    }
                    break;
                case 1:// 显示的是零钱宝
                    // 更新主页数据
                    if (homePageFragment != null) {
                        homePageFragment.refreshData(RefreshType.RefreshNoPull);
                    }

                    // 更新零钱宝数据
                    if (lqbFragment != null) {
                        lqbFragment.refreshData(RefreshType.RefreshPull);
                    }
                    break;
            }

        } else if (action.equals(Constant.UPDATE_ALL)) {// 更新前3个主界面(显示的是投标界面)
            // 更新主页数据
            if (homePageFragment != null) {
                homePageFragment.refreshData(RefreshType.RefreshNoPull);
            }

            // 更新零钱宝数据
            if (lqbFragment != null) {
                lqbFragment.refreshData(RefreshType.RefreshNoPull);
            }

            // 更新投资数据
            if (investmentFragment != null) {
                investmentFragment.refreshData(RefreshType.RefreshPull);
            }
        } else if (action.equals(Constant.UPDATE_MEDATA)) {   //首页更新无下拉动作
            // 更新主页数据
            if (homePageFragment != null) {
                homePageFragment.refreshData(RefreshType.RefreshNoPull);
            }
        }
    }

    /**
     * 注册数据更新广播
     */
    public void registerBoradcastReceiver() {
        IntentFilter myIntentFilter = new IntentFilter();
        myIntentFilter.addAction(Constant.UPDATE_ACCOUNTDATA);
        myIntentFilter.addAction(Constant.UPDATE_LQBDATA);
        myIntentFilter.addAction(Constant.UPDATE_ACCOUNTDATA_LQBDATA);
        myIntentFilter.addAction(Constant.UPDATE_ALL);
        myIntentFilter.addAction(Constant.UPDATE_MEDATA);
        // 注册广播
        registerReceiver(mBroadcastReceiver, myIntentFilter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        addFragment();
        start();
        registerBoradcastReceiver();
        /**
         * 6.0以上高亮状态栏
         */
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            this.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        setStatusBarDarkMode(true, this);//小米手机状态栏字体变黑
        /**
         * 发送极光推送的RegistrationID给服务端用做tag推送，只发一次
         */

        new Thread() {
            public void run() {
                Looper.prepare();
                LocationUtil locationUtil = new LocationUtil();
                locationUtil.getAddress(MainActivity.this, new OnLocationListener() {
                    @Override
                    public void location(Location location) {
                        app.location = location;
                    }

                    @Override
                    public void address(String address) {

                    }
                });
                Looper.loop();
            }
        }.start();
    }
    /**
     * 小米手机状态栏字体变黑
     *
     * @param darkmode
     * @param activity
     */
    public void setStatusBarDarkMode(boolean darkmode, Activity activity) {
        Class<? extends Window> clazz = activity.getWindow().getClass();
        try {
            int darkModeFlag = 0;
            Class<?> layoutParams = Class.forName("android.view.MiuiWindowManager$LayoutParams");
            Field field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE");
            darkModeFlag = field.getInt(layoutParams);
            Method extraFlagField = clazz.getMethod("setExtraFlags", int.class, int.class);
            extraFlagField.invoke(activity.getWindow(), darkmode ? darkModeFlag : 0, darkModeFlag);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);// 信鸽必须要调用这句
    }

    @Override
    public void onStart() {
        super.onStart();
    }


    /**
     * 最先执行添加碎片
     */
    private void addFragment() {
        homePageFragment = new HomePageFragment();
        lqbFragment = new LqbFragment();
        investmentFragment = new InvestmentFragment();
        meFragment = new MeFragment();
        // 添加碎片到集合
        fragmentList.add(homePageFragment);
        fragmentList.add(lqbFragment);
        fragmentList.add(investmentFragment);
        fragmentList.add(meFragment);

        // 碎片管理者
        fragmentManager = this.getSupportFragmentManager();
    }

    @Override
    public void findViewById() {
        llFragmentLayout = (LinearLayout) findViewById(R.id.fragment_layout);
        llFoot = (LinearLayout) findViewById(R.id.ll_foot);
        nbiHomePage = (NavigationBarItem) findViewById(R.id.nbi_home_page);
        nbiLqb = (NavigationBarItem) findViewById(R.id.nbi_lqb);
        nbiInvestment = (NavigationBarItem) findViewById(R.id.nbi_investment);
        nbiMe = (NavigationBarItem) findViewById(R.id.nbi_me);


        // 广告
        ivAd = (ViewPager) findViewById(R.id.iv_ad);
        btClose = (ImageView) findViewById(R.id.bt_close);
        llHomePoints = (LinearLayout) findViewById(R.id.ll_home_points);

        llFragmentLayout.post(new Runnable() {

            @Override
            public void run() {
                mFragmentLayoutHeight = llFragmentLayout.getHeight();
            }
        });

        rlCoverBg = (RelativeLayout) findViewById(R.id.rl_cover_bg);

        rlCoverAdBg = (RelativeLayout) findViewById(R.id.rl_cover_ad_bg);

        vsGuide2 = (ViewStub) findViewById(R.id.vs_guide2);
        //零钱宝上半部分新手引导
        vsGuide4 = (ViewStub) findViewById(R.id.vs_guide4);
        vsGuide5 = (ViewStub) findViewById(R.id.vs_guide5);

    }



    /**
     * 零钱宝上部分的新手引导
     */
    public void noviceGuide4() {
        rlCoverBg.setVisibility(View.VISIBLE);
        rlCoverBg.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {

            }
        });

        waveCircle.setWaveColor(Color.parseColor("#FB9D52"), Color.parseColor("#fdcda9"));
        float density = getResources().getDisplayMetrics().density;
        waveCircle.setBorder(10 * density, Color.TRANSPARENT);
        waveCircle.setShapeType(WaveView.ShapeType.CIRCLE);
        WaveHelper mWaveHelpeCircler = new WaveHelper(waveCircle, 0.07f, 5000, 0.06f);
        waveCircle.setMaxCircleArcDegree(0.07f * 360f / 0.0988f);
        mWaveHelpeCircler.start();

        Button btGuide4 = (Button) findViewById(R.id.bt_guide4);
        btGuide4.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferencesUtil.setPrefBoolean(MainActivity.this, "NoviceGuide4", true);
                rlCoverBg.setVisibility(View.GONE);
                vsGuide4.setVisibility(View.GONE);
            }
        });
    }

    /**
     * 零钱宝下部分的新手引导
     */
    public void noviceGuide5(int height) {
        rlCoverBg.setVisibility(View.VISIBLE);
        rlCoverBg.setOnClickListener(null);
        vsGuide5.setVisibility(View.VISIBLE);

        Button btGuide5 = (Button) findViewById(R.id.bt_guide5);
        RelativeLayout rlGuide5 = (RelativeLayout) findViewById(R.id.rl_guide5);

        LayoutParams layoutParams = rlGuide5.getLayoutParams();
        layoutParams.height = (int) (height + getResources().getDimension(R.dimen.d55));

        btGuide5.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferencesUtil.setPrefBoolean(MainActivity.this, "NoviceGuide5", true);
                vsGuide5.setVisibility(View.GONE);
                rlCoverBg.setVisibility(View.GONE);
            }
        });
    }

    /**
     * 发送体验金转入请求
     */
    public void sendTyjRollInRequest(RefreshType refreshType, double money) {
        RequestParams params = new RequestParams(Constant.LQB_EXPMONEYINTOLQB);
        params.addBodyParameter("user_userunid", app.mAccountData.uId);
        params.addBodyParameter("expMoney2lqb", money + "");// 体验金转入
        httpUtil.sendRequest(params, refreshType, this);
    }

    @Override
    public void setListener() {
        // 主页监听器
        nbiHomePage.setOnClickListener(this);
        // 零钱宝监听器
        nbiLqb.setOnClickListener(this);
        // 投资监听器
        nbiInvestment.setOnClickListener(this);
        // 我监听器
        nbiMe.setOnClickListener(this);
        // 设置网络监听
        httpUtil.setHttpRequesListener(this);
        //关闭广告
        btClose.setOnClickListener(this);
        //首页弹窗广告监听
        ivAd.setOnPageChangeListener(this);
    }

    @Override
    public void setTitle() {
        sLqbMinRote = SharedPreferencesUtil.getPrefString(this, "minLqbRate", "");
        sLqbMaxRote = SharedPreferencesUtil.getPrefString(this, "maxLqbRate", "");
    }

    @Override
    public void initData() {

        fragmentMainPage();
        sendOpenAppRequest(RefreshType.RefreshNoPull);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.nbi_home_page:// 主页
                setNavigationStyle(HOME_PAGE_POSITION);
                return;
            case R.id.nbi_lqb:// 零钱宝
                setNavigationStyle(LQB_POSITION);

                boolean noviceGuide4 = SharedPreferencesUtil.getPrefBoolean(this, "NoviceGuide4", false);
                if (!noviceGuide4) {

                    if (vsGuide4.getParent() != null) {     //如果viewStub被inflate，getparent返回为null
                        vsGuide4.inflate();
                    }
                    guidePageEarning = (TextView) findViewById(R.id.guide_page_earning);
                    guidePageBigRate = (TextView) findViewById(R.id.guide_page_big_earning);
                    rlCoverBg.setVisibility(View.VISIBLE);
                    vsGuide4.setVisibility(View.VISIBLE);
                    if (!TextUtils.isEmpty(sLqbMaxRote) && !TextUtils.isEmpty(sLqbMinRote)) {
                        guidePageEarning.setText(sLqbMinRote);
                        guidePageBigRate.setText(sLqbMaxRote);
                    }
                    waveCircle = (WaveView) findViewById(R.id.wave_circle2);
                    waveCircle.post(new Runnable() {

                        @Override
                        public void run() {
                            /**
                             * 零钱宝上部分的新手引导
                             */
                            noviceGuide4();
                        }
                    });
                }
                return;
            case R.id.nbi_investment:// 投资
                setNavigationStyle(INVESTMENT_POSITION);
                return;
            case R.id.nbi_me:// 我
                setNavigationStyle(ME_POSITION);
                return;
        }

        if (!isLogin) {// 没账号登录
            if (!SaveFirstInState.getFirstInState(app)) {// 不是第一次使用（有账号登录过）
                startActivity(new Intent(this, HaveAcountsLoginActivity.class));
            } else {// 不是第一次使用（没有账号登录过）
                startActivity(new Intent(this, LoginActivity.class));
            }
            return;
        }

        switch (v.getId()) {
            case R.id.bt_close://关闭首页广告
                rlCoverAdBg.setVisibility(View.GONE);
                break;
        }
    }

    // TODO=====================================碎片模块=====================================

    /**
     * 初始化刚进来时候的首页碎片
     */
    private void fragmentMainPage() {
        // 事务每次做操作前都要重新调用一次
        transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.fragment_layout, homePageFragment);
        transaction.commit();
        // 将首页赋值给最后一次
        lastFragment = homePageFragment;
    }

    /**
     * 显示或者隐藏碎片
     *
     * @param position 位置
     */
    private void showOrHide(int position) {
        // 事务每次做操作前都要重新调用一次
        transaction = fragmentManager.beginTransaction();

        Fragment fragment = fragmentList.get(position);
        // 判断是否被添加到了activity里面，下次执行的时候直接调用onstart;
        if (fragment.isAdded()) {
            fragment.onStart();
            fragment.onResume();
        } else {
            transaction.add(R.id.fragment_layout, fragment);
        }

        showFragmnet(position);
        // 提交
        transaction.commit();
    }

    /**
     * 显示碎片
     *
     * @param position 位置
     */
    private void showFragmnet(int position) {
        for (int i = 0; i < fragmentList.size(); i++) {
            Fragment fragment = fragmentList.get(i);
            if (position == i) {
                // 记录最后一次显示的碎片
                lastFragment = fragment;
                transaction.show(fragment);
            } else {
                transaction.hide(fragment);
            }
        }
    }

    /**
     * 更新碎片数据
     */
    private void refreshFragmentData(Fragment currontFragment) {
        if (currontFragment == homePageFragment) {
            // 更新主页
            homePageFragment.refreshData(RefreshType.RefreshPull);

        } else if (currontFragment == lqbFragment) {
            // 更新零钱宝
            lqbFragment.refreshData(RefreshType.RefreshPull);

        } else if (currontFragment == investmentFragment) {
            // 更新投资
            investmentFragment.refreshData(RefreshType.RefreshPull);

        }
    }

    // TODO=====================================底部导航模块=====================================

    /**
     * 设置底部导航样式
     */
    public void setNavigationStyle(int position) {

        // 如果显示的最后一页和当前页是一样的，那么不用重新显示或者隐藏碎片
        if (fragmentList.get(position) == lastFragment) {
            refreshFragmentData(lastFragment);
            return;
        }

        //显示或者隐藏碎片
        showOrHide(position);

        // 子控件总数
        int childCount = llFoot.getChildCount();
        for (int i = 0; i < childCount; i++) {
            NavigationBarItem item = (NavigationBarItem) llFoot.getChildAt(i);
            if (i == position) {
                // 选中的
                selectItemStyle(i, item);
            } else {
                // 未选中的
                unSelectItemStyle(i, item);
            }
        }
    }

    /**
     * 底部控件距离底部距离
     *
     * @return
     */
    public static int footBottomDistance() {
        final int[] paddingBottom = new int[1];
        llFoot.post(new Runnable() {
            @Override
            public void run() {
                paddingBottom[0] = llFoot.getBottom();
            }
        });
        return paddingBottom[0];
    }

    /**
     * 选中的样式
     *
     * @param position 位置
     */
    private void selectItemStyle(int position, NavigationBarItem selectItem) {
        selectItem.setItemTextColor(getResources().getColor(R.color.lqb_head_bg));
        if (position == HOME_PAGE_POSITION) {// 主页
            selectItem.setItemImage(R.drawable.home_page_select);
        } else if (position == LQB_POSITION) {// 零钱宝
            selectItem.setItemImage(R.drawable.lqb_select);
        } else if (position == INVESTMENT_POSITION) {// 投资
            selectItem.setItemImage(R.drawable.investment_select);
        } else if (position == ME_POSITION) {// 我
            selectItem.setItemImage(R.drawable.me_select);
        }
    }

    /**
     * 未选中的样式
     *
     * @param position     位置
     * @param unSelectItem 当前的控件
     */
    private void unSelectItemStyle(int position, NavigationBarItem unSelectItem) {
        unSelectItem.setItemTextColor(Color.parseColor("#5A6166"));
        if (position == HOME_PAGE_POSITION) {// 主页
            unSelectItem.setItemImage(R.drawable.home_page_unselect);
        } else if (position == LQB_POSITION) {// 零钱宝
            unSelectItem.setItemImage(R.drawable.lqb_unselect);
        } else if (position == INVESTMENT_POSITION) {// 投资
            unSelectItem.setItemImage(R.drawable.investment_unselect);
        } else if (position == ME_POSITION) {// 我
            unSelectItem.setItemImage(R.drawable.me_unselect);
        }
    }

    // TODO=====================================Fragment和MainActivity同时销毁=====================================

    /**
     * 为了内存不足时候Fragment和MainActivity同时销毁.
     * <p>
     * 1.重写onSaveInstanceState
     * <p>
     * 2.注释super.onSaveInstanceState(outState)或删除。可阻止activity保存fragment的状态
     */
    public void onSaveInstanceState(Bundle outState) {
        // super.onSaveInstanceState(outState); //将这一行注释掉，
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        // ToastUtil.showToastLong(this, "系统内存不足");
    }

    /**
     * 发送打开APP请求
     */
    public void sendOpenAppRequest(RefreshType refreshType) {
        if (TextUtils.isEmpty(mUid)) {// 没账号登录
            RequestParams params = new RequestParams(Constant.ACCESSAPPINTERFACE_UNREGISTEREDOPENAPP);
            httpUtil.sendRequest(params, refreshType, this);
        } else {// 有账号登录
            RequestParams params = new RequestParams(Constant.ACCESSAPPINTERFACE_NOTTIEDUPCARDOPENAPP);
            params.addBodyParameter("user_userunid", mUid);
            httpUtil.sendRequest(params, refreshType, this);
        }
    }

    @Override
    public void onFailure(String url, String errorContent) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onSuccess(String url, JSONBase jsonBase) {
        // TODO Auto-generated method stub
        if (url.equals(Constant.ACCESSAPPINTERFACE_UNREGISTEREDOPENAPP)) {
            JSONObject jsonObject = JSON.parseObject(jsonBase.getDisposeResult());
            sLqbMinRote = jsonObject.getString("minLqbRate");
            sLqbMaxRote = jsonObject.getString("maxLqbRate");
            SharedPreferencesUtil.setPrefString(this, "minLqbRate", sLqbMinRote);
            SharedPreferencesUtil.setPrefString(this, "maxLqbRate", sLqbMaxRote);
            SharedPreferencesUtil.setPrefString(this, "minInvestRate", jsonObject.getString("minInvestRate"));
            SharedPreferencesUtil.setPrefString(this, "maxInvestRate", jsonObject.getString("maxInvestRate"));
            String homeBannerUrl = jsonObject.getString("AndroidBannerUrl");    //首页banner图
            if (!TextUtils.isEmpty(homeBannerUrl)) {
                SharedPreferencesUtil.setPrefString(this, "homeBannerUrl", homeBannerUrl);
//                homePageFragment.showHomeBanner(homeBannerUrl);
            }

            AccountData accountData = JSON.parseObject(jsonBase.getDisposeResult(), AccountData.class);
            homePageFragment.showAuctionFacility(accountData);

        }

    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            /**
             * 退出应用
             */
            BaseAppManager.getAppManager().exit(MainActivity.this);
        }
        return false;
    }

    @Override
    protected void onDestroy() {
        if (mBroadcastReceiver != null) {
            unregisterReceiver(mBroadcastReceiver);
        }
        super.onDestroy();
    }

    public LqbFragment getLqbFragment() {
        return lqbFragment;
    }

    /**
     * 友盟分享如果使用的是qq或者新浪精简版jar，需要在您使用分享或授权的Activity（fragment不行）中添加如下回调代码
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);

    }

    /**
     * 显示广告
     *
     * @param popupPage
     */
    public void showAd(List<PopupPage> popupPage) {
        popupPageHome = popupPage;
        if (popupPageHome.size() == 0){
            return;
        }
        initDots();
        rlCoverAdBg.setVisibility(View.VISIBLE);
        if (adapter == null) {
            adapter = new ViewPagerAdapter(popupPage);
            ivAd.setAdapter(adapter);
        } else {
            adapter.setActivityBases(popupPage);
            adapter.notifyDataSetChanged();
        }
    }

    /**
     * 初始化dot
     */
    private void initDots() {
        llHomePoints.removeAllViews();
        for (int i = 0; i < popupPageHome.size(); i++) {
            ImageView imageView = new ImageView(MainActivity.this);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(16, 16);
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
        if (popupPageHome.size() == 0) {
            return;
        }
        int currentPage = ivAd.getCurrentItem() % popupPageHome.size();
        for (int i = 0; i < llHomePoints.getChildCount(); i++) {
            llHomePoints.getChildAt(i).setEnabled(i == currentPage);// 设置setEnabled为true的话
            // 在选择器里面就会对应的使用黄色颜色
        }
    }

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

    private class ViewPagerAdapter extends PagerAdapter {

        private List<PopupPage> popupPagesDatas;

        public ViewPagerAdapter(List<PopupPage> popupPagesDatas) {
            this.popupPagesDatas = popupPagesDatas;
        }

        public void setActivityBases(List<PopupPage> popupPagesDatas) {
            this.popupPagesDatas = popupPagesDatas;
        }

        @Override
        public int getCount() {
            return popupPagesDatas.size();
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
            View view = View.inflate(container.getContext(), R.layout.main_popup_ad_layout, null);
            final PopupPage popupPages = popupPagesDatas.get(position);
            XCRoundRectImageView imageView = (XCRoundRectImageView) view.findViewById(R.id.image_home_popup_ad);

            app.setOptions(R.drawable.home_activity_load);
            String url = Constant.IMAGE_ADDRESS + popupPages.pageImage;
            app.setDisplayImage(url, imageView);
            container.addView(view);

            imageView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    rlCoverAdBg.setVisibility(View.GONE);
                    if (!TextUtils.isEmpty(popupPages.pageUrl)) {
                        if (popupPages.isTurntableActivity == 1) {
                            JSONObject json = new JSONObject();
                            json.put("ukey", SaveCurrentUidUtil.getCurrentUid(app));
                            json.put("code", Constant.ZP_CODE);
                            String sign = "";
                            try {
                                sign = RSAUtils.encrypt(json.toString(), Constant.ZP_ENCRYPT_KEY);
                            } catch (Exception e2) {
                                e2.printStackTrace();
                            }
                            popupPages.pageUrl = popupPages.pageUrl + "?ukey=" + mUid + "&code=" + Constant.ZP_CODE + "&sign=" + sign;
                        } else if (popupPages.isTurntableActivity == 2) {
                            if (!popupPages.pageUrl.contains("?uid=")){
                                popupPages.pageUrl = popupPages.pageUrl + "?uid=" + mUid;
                            }
                        } else if (popupPages.isTurntableActivity == 3) {

                        }
                        Intent intent = new Intent(MainActivity.this, WebViewActivity.class);
                        intent.putExtra(PopupPage.class.getName(), popupPages);
                        startActivity(intent);
                    }
                }
            });

            return view;

        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }


    }
}
