package com.wtjr.lqg.activities;

import java.util.ArrayList;
import java.util.List;

import org.xutils.http.RequestParams;

import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.wtjr.lqg.R;
import com.wtjr.lqg.base.EarningsDetails;
import com.wtjr.lqg.base.JSONBase;
import com.wtjr.lqg.basecommonly.BaseActivity;
import com.wtjr.lqg.constants.Constant;
import com.wtjr.lqg.enums.RefreshType;
import com.wtjr.lqg.utils.ArithUtil;
import com.wtjr.lqg.utils.DialogUtil;
import com.wtjr.lqg.utils.L;
import com.wtjr.lqg.utils.MoneyFormatUtil;
import com.wtjr.lqg.utils.TimeUtil;
import com.wtjr.lqg.utils.HttpUtil.HttpRequesListener;
import com.wtjr.lqg.widget.MonthDateView;
import com.wtjr.lqg.widget.MonthDateView.DateClick;

/**
 * 收益详情
 * 
 */
public class EarningsDetailsActivity extends BaseActivity implements HttpRequesListener,
		OnClickListener{

	/**
	 * 标题名字
	 */
	private TextView tvTitleName;
	/**
	 * 返回的图片按钮
	 */
	private ImageButton imgBtnBack;
	
	private TextView tvTitle;
    private ImageButton imgbtn_back;
    private ViewPager mViewPager;
    private CalendarViewAdapter<View> adapter;
    private int mCurrYear = 2016;
    private int mCurrMonth = 6;
    private int mCurrDay = 30;
    /**
     * 累计的总共收益
     */
    private String total;
    private LinearLayout llTitle;
//    private TextView tvDate;
    /**
     * 那一天的总收益标题
     */
    private TextView tvDayIncomeTitle;
    /**
     * 那一天的总收益值
     */
    private TextView tvDayIncomeValue;
    /**
     * 项目收益
     */
    private TextView tvProjectIncome;
    /**
     * 零钱宝收益
     */
    private TextView tvLqgIncome;
    /**
     * 累计总收益
     */
    private TextView tvTotalIncome;
    
    private boolean flag = true;
    
    /**
     * 是否初始化过日历
     */
    private boolean isInitCalendar = false;
    
    private String month;
    private MonthDateView monthDateView1;
    private MonthDateView monthDateView2;
    
    private  List<EarningsDetails> earningsDetails = new ArrayList<EarningsDetails>();

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.activity_earnings_details);
		start();
	}
	

	@Override
	public void findViewById() {
		tvTitleName = (TextView) findViewById(R.id.tv_title_name);
		imgBtnBack = (ImageButton) findViewById(R.id.imgBtn_back);
		
		mViewPager = (ViewPager) findViewById(R.id.vp_calendar);
//		tvDate = (TextView) findViewById(R.id.tv_date);
		
		tvTotalIncome = (TextView) findViewById(R.id.tv_totalIncome);
		
		tvDayIncomeTitle = (TextView) findViewById(R.id.tv_dayIncomeTitle);
		
		tvDayIncomeValue = (TextView) findViewById(R.id.tv_dayIncomeValue);
		tvProjectIncome = (TextView) findViewById(R.id.tv_projectIncome);
		tvLqgIncome = (TextView) findViewById(R.id.tv_lqgIncome);
	} 

	@Override
	public void setListener() {
		// 返回按钮监听器
		imgBtnBack.setOnClickListener(this);
		httpUtil.setHttpRequesListener(this);
	}

	@Override
	public void setTitle() {
		// 设置名字为:收益详情
		tvTitleName.setText(R.string.earnings_details_name);
		// 一开始的时候是隐藏的，因为不知道要图片还是文字，所以现在显示出来
        imgBtnBack.setVisibility(View.VISIBLE);
	}

	@Override
	public void initData() {
	    sendEarningsDetailsRequest("1");
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
        View[] views = new View[2];
        
        //本月的view视图
        View inflate1 = View.inflate(this, R.layout.calendar, null);
        monthDateView1= (MonthDateView) inflate1.findViewById(R.id.monthDateView);
        monthDateView1.setCurrDate(mCurrYear, mCurrMonth-1, mCurrDay);
        monthDateView1.setDateClick(new DateClick() {
            
            @Override
            public void onClickOnDate() {
                
                int getmSelYear = monthDateView1.getmSelYear();
                int getmSelMonth = monthDateView1.getmSelMonth();
                int getmSelDay = monthDateView1.getmSelDay();
                
//                tvDate.setText((getmSelMonth+1)+"月"+getmSelDay+"日");
                tvDayIncomeTitle.setText((getmSelMonth+1)+"月"+getmSelDay+"日获得总收益");
                
                String date = getmSelYear+"-"+(getmSelMonth+1)+"-"+getmSelDay;
                L.hintI(date);
                
                //将上个月份的选择状态取消
                monthDateView2.doClickAction(-1 , -1);
                
                showIncomeData(date);
            }

        });
        
        //上月的view视图
        View inflate2 = View.inflate(this, R.layout.calendar, null);
        monthDateView2 = (MonthDateView) inflate2.findViewById(R.id.monthDateView);
        monthDateView2.setCurrDate(mCurrYear, mCurrMonth-1, 32);
        monthDateView2.onLeftClick();
        monthDateView2.setDateClick(new DateClick() {
            
            @Override
            public void onClickOnDate() {
                int getmSelYear = monthDateView2.getmSelYear();
                int getmSelMonth = monthDateView2.getmSelMonth();
                int getmSelDay = monthDateView2.getmSelDay();
                
//                tvDate.setText((getmSelMonth+1)+"月"+getmSelDay+"日");
                tvDayIncomeTitle.setText((getmSelMonth+1)+"月"+getmSelDay+"日获得总收益");
                
                String date = getmSelYear+"-"+(getmSelMonth+1)+"-"+getmSelDay;
                L.hintD(date);
                
                //将本月份的选择状态取消
                monthDateView1.doClickAction(-1 , -1);
                    
                showIncomeData(date);
            }
        });
        
        views[1] = inflate1;
        views[0] = inflate2;
        
        adapter = new CalendarViewAdapter<View>(views);
        setViewPager();
    }
    
    /**
     * 显示用户选择的日期对应的收益数据
     * @param selectdate 用户选择的日期
     */
    private void showIncomeData(String selectdate) {
        EarningsDetails earning = null;
        for (EarningsDetails earnings : earningsDetails) {
            String date = TimeUtil.getFullTime(earnings.timeStamp, "yyyy-M-d");
            if(date.equals(selectdate)) {
                earning = earnings;
            }
        }
        
        
        if(earning == null) {
            tvProjectIncome.setText(MoneyFormatUtil.format(0));
            tvLqgIncome.setText(MoneyFormatUtil.format(0));
            tvDayIncomeValue.setText(MoneyFormatUtil.format(0));
        }else {
            tvProjectIncome.setText(MoneyFormatUtil.format(earning.investmentProfit));
            tvLqgIncome.setText(MoneyFormatUtil.format(earning.lpbProfit));
            double total = ArithUtil.add(earning.investmentProfit, earning.lpbProfit);
            tvDayIncomeValue.setText(MoneyFormatUtil.format(total));
        }
    }
    

    private void setViewPager() {
        mViewPager.setAdapter(adapter);
        mViewPager.setCurrentItem(1);
        mViewPager.setOnPageChangeListener(new OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                if(position == 0 && flag){//上个月
                    sendEarningsDetailsRequest("2");
                    flag = false;
                    return;
                }
                if(position == 1 && !flag){//本月
                    sendEarningsDetailsRequest("1");
                    flag = true;
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
	
	
	
	
	/**
	 * 发送收益详情请求
	 * @param dataType 1是本月，2是上月
	 */
	private void sendEarningsDetailsRequest(String dataType) {
	    RequestParams params=new RequestParams(Constant.HOMEPAGE_REVENUEDETAILS);
        params.addBodyParameter("user_userunid", mUid);
        params.addBodyParameter("type", dataType);
        httpUtil.sendRequest(params, RefreshType.RefreshNoPull, EarningsDetailsActivity.this);
    }

	@Override
	public void onFailure(String url, String errorContent) {
	    DialogUtil.promptDialog(this, DialogUtil.HEAD_SERVICE, errorContent);
	    
	    String currentTime = TimeUtil.getFullTime(System.currentTimeMillis(), "yyyy-MM-dd");
        
        String[] split = currentTime.split("-");
        mCurrYear = Integer.parseInt(split[0]);
        mCurrMonth = Integer.parseInt(split[1]);
        mCurrDay = Integer.parseInt(split[2]);
	    
	    if(!isInitCalendar) {//初始化一次日历
            initCalendar();
            isInitCalendar = true;
        }
	}

	@Override
	public void onSuccess(String url, JSONBase jsonBase) {
	    JSONObject jsonObject = JSON.parseObject(jsonBase.getDisposeResult());
	    String totalProfit = jsonObject.getString("totalProfit");
	    tvTotalIncome.setText(MoneyFormatUtil.format(totalProfit));
	    
        String revenueDetails = jsonObject.getString("revenueDetails");
        
        earningsDetails.clear();
        earningsDetails.addAll(JSON.parseArray(revenueDetails, EarningsDetails.class));
        

        String currentTime = "";
        if(earningsDetails.size() == 0) {//没获得到服务器的数据就根据本地时间显示日历
            currentTime = TimeUtil.getFullTime(System.currentTimeMillis(), "yyyy-M-d");
        }else {//得到服务器的数据中的第一条为当天显示日历
            currentTime = TimeUtil.getFullTime(earningsDetails.get(0).timeStamp, "yyyy-M-d");
        }
        
        String[] split = currentTime.split("-");
        mCurrYear = Integer.parseInt(split[0]);
        mCurrMonth = Integer.parseInt(split[1]);
        mCurrDay = Integer.parseInt(split[2]);
        
        if(!isInitCalendar) {//初始化一次日历
            initCalendar();
            isInitCalendar = true;
        }
        
//        tvDate.setText(mCurrMonth+"月"+mCurrDay+"日");
        tvDayIncomeTitle.setText(mCurrMonth+"月"+mCurrDay+"日获得总收益");
        showIncomeData(currentTime);
	}
}