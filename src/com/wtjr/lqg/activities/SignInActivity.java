package com.wtjr.lqg.activities;

import java.util.List;

import org.xutils.http.RequestParams;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.wtjr.lqg.R;
import com.wtjr.lqg.base.JSONBase;
import com.wtjr.lqg.base.SignIn;
import com.wtjr.lqg.basecommonly.BaseActivity;
import com.wtjr.lqg.constants.Constant;
import com.wtjr.lqg.enums.RefreshType;
import com.wtjr.lqg.utils.DialogUtil;
import com.wtjr.lqg.utils.HttpUtil;
import com.wtjr.lqg.utils.L;
import com.wtjr.lqg.utils.TimeUtil;
import com.wtjr.lqg.utils.DialogUtil.OnClickYesListener;
import com.wtjr.lqg.utils.HttpUtil.HttpRequesListener;
import com.wtjr.lqg.widget.SignInMonthDateView;
import com.wtjr.lqg.widget.UpDownTextView;
import com.wtjr.lqg.widget.SignInMonthDateView.DateClick;
import com.wtjr.lqg.widget.pullRefresh.PullToRefreshLayout.OnRefreshListener;

/**
 * 签到页面
 * 
 * @author JoLie
 * 
 */
public class SignInActivity extends BaseActivity implements OnClickListener, HttpRequesListener, OnRefreshListener {
    /**
     * 标题名字
     */
    private TextView tvTitleName;

    /**
     * 返回的图片按钮
     */
    private ImageButton imgBtnBack;

    // private PullToRefreshLayout pullToRefreshLayout;
    // private PullableListView pullableListView;

    private int mCurrYear = 2016;

    private int mCurrMonth = 7;

    private int mCurrDay = 7;

    /**
     * 日历视图
     */
    private View calendarView;

    private LinearLayout llSignIn;

    private CalendarViewAdapter<View> adapter;

    private ViewPager mViewPager;

    private boolean flag = true;

    private String month;

    private SignInMonthDateView monthDateView1;

    /**
     * 标题栏
     */
    private RelativeLayout rlTitle;

    public HttpUtil mHttpUtil;

    private boolean isInitCalendar = false;

    /**
     * 显示签到状态（已签到，未签到）
     */
    private TextView tvSignInState;

    /**
     * 累计签到
     */
    private UpDownTextView udtvTotalSignIn;

    /**
     * 连续签到
     */
    private UpDownTextView udtvContinuousSignIn;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_sign_in);
        start();
    }

    @Override
    public void findViewById() {
        rlTitle = (RelativeLayout) findViewById(R.id.rl_title);
        tvTitleName = (TextView) findViewById(R.id.tv_title_name);
        imgBtnBack = (ImageButton) findViewById(R.id.imgBtn_back);

        llSignIn = (LinearLayout) findViewById(R.id.ll_sign_in);
        mViewPager = (ViewPager) findViewById(R.id.vp_calendar);

        // 显示签到状态（已签到，未签到）
        tvSignInState = (TextView) findViewById(R.id.tv_signInState);

        // 累计签到
        udtvTotalSignIn = (UpDownTextView) findViewById(R.id.udtv_totalSignIn);
        // 连续签到
        udtvContinuousSignIn = (UpDownTextView) findViewById(R.id.udtv_continuousSignIn);

        // // 下拉刷新
        // pullToRefreshLayout = (PullToRefreshLayout)
        // findViewById(R.id.pullTo_refreshLayout);
        // //ListView列表
        // pullableListView = (PullableListView)
        // findViewById(R.id.pullable_listView);
    }

    @Override
    public void setListener() {
        // 返回按钮监听器
        imgBtnBack.setOnClickListener(this);
        httpUtil.setHttpRequesListener(this);
        // pullToRefreshLayout.setOnRefreshListener(this);
    }

    @Override
    public void setTitle() {
        // 一开始的时候是隐藏的，因为不知道要图片还是文字，所以现在显示出来
        imgBtnBack.setVisibility(View.VISIBLE);
        imgBtnBack.setImageResource(R.drawable.back2);
        // 设置名字为:签到
        tvTitleName.setText(R.string.sign_in_name);
        tvTitleName.setTextColor(Color.BLACK);

        rlTitle.setBackgroundColor(Color.WHITE);
    }

    @Override
    public void initData() {
        sendSignInRequest();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.imgBtn_back:// 返回按钮点击操作
            // 结束当前的Activity
            finish();
            break;

        default:
            break;
        }
    }

    /**
     * 初始化日历视图
     */
    private void initCalendar() {
        View[] views = new View[1];

        // 本月的view视图
        View inflate1 = View.inflate(this, R.layout.sign_in_calendar, null);

        // WeekDayView weekDayView= (WeekDayView)
        // inflate1.findViewById(R.id.weekDayView);
        // String[] ss = {""};
        // weekDayView.setWeekString(ss);

        monthDateView1 = (SignInMonthDateView) inflate1.findViewById(R.id.monthDateView);
        monthDateView1.setCurrDate(mCurrYear, mCurrMonth - 1, mCurrDay);
        monthDateView1.setDateClick(new DateClick() {

            @Override
            public void onClickOnDate() {

                int getmSelYear = monthDateView1.getmSelYear();
                int getmSelMonth = monthDateView1.getmSelMonth();
                int getmSelDay = monthDateView1.getmSelDay();

                L.hintI(getmSelYear + "--" + (getmSelMonth + 1) + "--" + getmSelDay);

                String date = getmSelYear + "-" + (getmSelMonth + 1) + "-" + getmSelDay;

                // 将上个月份的选择状态取消
                // monthDateView2.doClickAction(-1 , -1);

                // showIncomeData(date);
            }

        });

        // //上月的view视图
        // View inflate2 = View.inflate(this, R.layout.calendar, null);
        // monthDateView2 = (MonthDateView)
        // inflate2.findViewById(R.id.monthDateView);
        // monthDateView2.setCurrDate(mCurrYear, mCurrMonth-1, 32);
        // monthDateView2.onLeftClick();
        // monthDateView2.setDateClick(new DateClick() {
        //
        // @Override
        // public void onClickOnDate() {
        // int getmSelYear = monthDateView2.getmSelYear();
        // int getmSelMonth = monthDateView2.getmSelMonth();
        // int getmSelDay = monthDateView2.getmSelDay();
        //
        // L.hintD(getmSelYear+"--"+(getmSelMonth+1)+"--"+getmSelDay);
        //
        // String date = getmSelYear+"-"+(getmSelMonth+1)+"-"+getmSelDay;
        //
        // //将本月份的选择状态取消
        // monthDateView1.doClickAction(-1 , -1);
        //
        // // showIncomeData(date);
        // }
        // });

        views[0] = inflate1;
        // views[0] = inflate2;

        adapter = new CalendarViewAdapter<View>(views);
        setViewPager();
    }

    private void setViewPager() {
        mViewPager.setAdapter(adapter);
        mViewPager.setCurrentItem(0);
        mViewPager.setOnPageChangeListener(new OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                if (position == 0 && flag) {
                    // sendHttpReques("up");
                }
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int arg0) {

            }
        });
    }

    class CalendarViewAdapter<V extends View> extends PagerAdapter {
        private V[] views;

        public CalendarViewAdapter(V[] views) {
            super();
            this.views = views;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {

            if (((ViewPager) container).getChildCount() == views.length) {
                ((ViewPager) container).removeView(views[position % views.length]);
            }

            ((ViewPager) container).addView(views[position % views.length], 0);
            return views[position % views.length];
        }

        @Override
        public int getCount() {
            return views.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == ((View) object);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            ((ViewPager) container).removeView((View) container);
        }

        public V[] getAllItems() {
            return views;
        }
    }

    @Override
    public void onRefresh() {
        // pullToRefreshLayout.rerefreshFinish(pullToRefreshLayout.FAIL);
    }

    @Override
    public void onLoadMore() {
        // pullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);
    }

    /**
     * 发送签到请求
     */
    private void sendSignInRequest() {
        RequestParams params = new RequestParams(Constant.H5_SING_IN);
        params.addBodyParameter("user_userunid", mUid);
        httpUtil.sendRequest(params, RefreshType.RefreshNoPull, this);
    }

    @Override
    public void onFailure(String url, String errorContent) {
        DialogUtil.promptDialog(this, DialogUtil.HEAD_SERVICE, errorContent, new OnClickYesListener() {

            @Override
            public void onClickYes() {
                finish();
            }
        });
    }

    @Override
    public void onSuccess(String url, JSONBase jsonBase) {
        tvSignInState.setText("已签到");

        JSONObject jsonObject = JSON.parseObject(jsonBase.getDisposeResult());
        long sysDate = jsonObject.getLong("sysDate");// 服务器时间
        int total = jsonObject.getInteger("total");// 累计签到天数
        int checkInDays = jsonObject.getInteger("checkInDays");// 连续签到天数
        String signLogList = jsonObject.getString("signLogList");// 签到过的记录

        udtvTotalSignIn.setUpTextContent(total + "天");
        udtvContinuousSignIn.setUpTextContent(checkInDays + "天");

        String currentTime = "";
        if (sysDate == 0) {// 没获得到服务器的数据就根据本地时间显示日历
            currentTime = TimeUtil.getFullTime(System.currentTimeMillis(), "yyyy-M-d");
        } else {// 得到服务器的数据中的第一条为当天显示日历
            currentTime = TimeUtil.getFullTime(sysDate, "yyyy-M-d");
        }

        String[] split = currentTime.split("-");
        mCurrYear = Integer.parseInt(split[0]);
        mCurrMonth = Integer.parseInt(split[1]);
        mCurrDay = Integer.parseInt(split[2]);

        if (!isInitCalendar) {// 初始化一次日历
            initCalendar();
            isInitCalendar = true;
        }

        List<SignIn> signIns = JSON.parseArray(signLogList, SignIn.class);
        String[] days = new String[signIns.size()];
        for (int i = 0; i < signIns.size(); i++) {
            days[i] = TimeUtil.getFullTime(signIns.get(i).time, "d");
        }
        monthDateView1.setSignInDate(days);
    }
}
