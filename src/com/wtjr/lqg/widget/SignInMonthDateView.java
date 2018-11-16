package com.wtjr.lqg.widget;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.wtjr.lqg.utils.DateUtils;
import com.wtjr.lqg.utils.L;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

public class SignInMonthDateView extends View {
    private static final int NUM_COLUMNS = 7;

    private static final int NUM_ROWS = 6;

    private Paint mPaint;

//    private int mDayColor = Color.parseColor("#ffffff");// 数字颜色
//    private int mSelectDayColor = Color.parseColor("#ffffff");// 选中文字颜色
    private int mSelectBGColor = Color.parseColor("#FF6660");// 选中背景颜色
//    private int mCurrentColor = Color.parseColor("#ff0000");
    
    private int mDayColor = Color.parseColor("#000000");//数字颜色
    private int mSelectDayColor = Color.parseColor("#ffffff");//选中文字颜色
    private int mCurrentColor = Color.parseColor("#FA8F21");
    

    private int mCurrYear, mCurrMonth, mCurrDay;

    private int mSelYear, mSelMonth, mSelDay;

    private int mColumnSize, mRowSize;

    private DisplayMetrics mDisplayMetrics;

    private int mDaySize = 15;

    private TextView tv_date, tv_week;

    private int weekRow;

    private int[][] daysString;

    private int mCircleRadius = 6;

    private DateClick dateClick;

    private int mCircleColor = Color.TRANSPARENT;

    private List<Integer> daysHasThingList;

    private String dateee;

    public SignInMonthDateView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mDisplayMetrics = getResources().getDisplayMetrics();
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

        // Calendar calendar = Calendar.getInstance();
        // mCurrYear = calendar.get(Calendar.YEAR);
        // mCurrMonth = calendar.get(Calendar.MONTH);
        // mCurrDay = calendar.get(Calendar.DATE);
        //
        // setSelectYearMonth(mCurrYear,mCurrMonth,mCurrDay);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    public void setCurrDate(int mCurrYear, int mCurrMonth, int mCurrDay) {
        this.mCurrYear = mCurrYear;
        this.mCurrMonth = mCurrMonth;
        this.mCurrDay = mCurrDay;

        dateee = mCurrYear + "-" + (mCurrMonth + 1) + "-" + mCurrDay;
        setSelectYearMonth(mCurrYear, mCurrMonth, mCurrDay);
    }

    @Override
	protected void onDraw(Canvas canvas) { 
		initSize();
		
		daysString = new int[6][7];
		mPaint.setTextSize(mDaySize*mDisplayMetrics.scaledDensity);
		String dayString = null;
		int mMonthDays = DateUtils.getMonthDays(mSelYear, mSelMonth);
		int weekNumber = DateUtils.getFirstDayWeek(mSelYear, mSelMonth);
		
		
		for(int day = 0;day < mMonthDays;day++){
			dayString = (day + 1) + "";
			int column = (day+weekNumber - 1) % 7;
			int row = (day+weekNumber - 1) / 7;
			daysString[row][column]=day + 1;
			int startX = (int) (mColumnSize * column + (mColumnSize - mPaint.measureText(dayString))/2);
			int startY = (int) (mRowSize * row + mRowSize/2 - (mPaint.ascent() + mPaint.descent())/2);
			int startRecX = mColumnSize * column;
			int startRecY = mRowSize * row;
			int endRecX = startRecX + mColumnSize;
			int endRecY = startRecY + mRowSize;
			
			if(dayString.equals(mSelDay+"")){//绘制选择的日子
				//绘制背景色
				mPaint.setColor(mSelectBGColor);
//				(startRecX, startRecY, endRecX, endRecY, mPaint);
				canvas.drawCircle(startRecX+(endRecX-startRecX)/2, startRecY+(endRecY-startRecY)/2, (endRecY-startRecY)/3, mPaint);
				//记录第几行，即第几周
				weekRow = row + 1;
			}
			
			//绘制事务圆形标志
//			drawCircle(row,column,day + 1,canvas);
			
			String date = mSelYear+"-"+(mSelMonth+1)+"-"+dayString;
			int compare_date = compare_date(date, dateee);	
			
			if(dayString.equals(mCurrDay+"") && mCurrMonth == mSelMonth){//当天
				//正常月，选中其他日期，则今日为红色
				mPaint.setColor(Color.parseColor("#EAEAEA"));
				canvas.drawCircle(startRecX+(endRecX-startRecX)/2, startRecY+(endRecY-startRecY)/2, (endRecY-startRecY)/3, mPaint);
				mPaint.setColor(mCurrentColor);
				
			}else if(dayString.equals(mSelDay+"")){
				
				mPaint.setColor(mSelectDayColor);
				
			}else{
				if(compare_date <= 0){//在当前日子的前面的号
					
					mPaint.setColor(Color.parseColor("#e6e6e6"));
					canvas.drawCircle(startRecX+(endRecX-startRecX)/2, startRecY+(endRecY-startRecY)/2, (endRecY-startRecY)/3, mPaint);
		
					mPaint.setColor(mDayColor);
				
				}else{//在当前日子的后面的号
					mPaint.setColor(Color.parseColor("#99eaeaea"));
					canvas.drawCircle(startRecX+(endRecX-startRecX)/2, startRecY+(endRecY-startRecY)/2, (endRecY-startRecY)/3, mPaint);
			
					mPaint.setColor(Color.WHITE);
				}
			}
			
		
			
			//判断哪天签到了
            if (mDays != null && mDays.length > 0) {
                for (int i = 0; i < mDays.length; i++) {
                    if(mDays[i].equals(dayString)) {
                        mPaint.setColor(Color.parseColor("#ffc65c"));
                        canvas.drawCircle(startRecX+(endRecX-startRecX)/2, startRecY+(endRecY-startRecY)/2, (endRecY-startRecY)/3, mPaint);
                        mPaint.setColor(mSelectDayColor);
                    }
                }
            }
            
            //当天红色日子
            if(mCurrYear == mSelYear && mCurrMonth == mSelMonth && mCurrDay == mSelDay && dayString.equals(mSelDay+"")){
                //绘制背景色
                mPaint.setColor(mSelectBGColor);
                canvas.drawCircle(startRecX+(endRecX-startRecX)/2, startRecY+(endRecY-startRecY)/2, (endRecY-startRecY)/3, mPaint);
                mPaint.setColor(mSelectDayColor);
            }
			
//			if(compare_date <= 0){//在当前日子的前面的号
//				canvas.drawText(dayString, startX, startY, mPaint);
//			}else{//在当前日子的后面的号
//				mPaint.setColor(Color.WHITE);
				canvas.drawText(dayString, startX, startY, mPaint);
//			}
			
			if(tv_date != null){
				tv_date.setText(mSelYear + "年" + (mSelMonth + 1) + "月");
			}
			
			if(tv_week != null){
				tv_week.setText("第" + weekRow  +"周");
			}
			
		
		}
	}

    public int compare_date(String DATE1, String DATE2) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date dt1 = df.parse(DATE1);
            Date dt2 = df.parse(DATE2);
            if (dt1.getTime() >= dt2.getTime()) {
                return 1;
            } else {
                return -1;
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }

        return 0;
    }

    private void drawCircle(int row, int column, int day, Canvas canvas) {
        if (daysHasThingList != null && daysHasThingList.size() > 0) {
            if (!daysHasThingList.contains(day))
                return;
            mPaint.setColor(mCircleColor);
            float circleX = (float) (mColumnSize * column + mColumnSize * 0.8);
            float circley = (float) (mRowSize * row + mRowSize * 0.2);
            canvas.drawCircle(circleX, circley, mCircleRadius
                    * mDisplayMetrics.scaledDensity, mPaint);
        }
    }

    @Override
    public boolean performClick() {
        return super.performClick();
    }

    private int downX = 0, downY = 0;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // int eventCode= event.getAction();
        // switch(eventCode){
        // case MotionEvent.ACTION_DOWN:
        // downX = (int) event.getX();
        // downY = (int) event.getY();
        // break;
        // case MotionEvent.ACTION_MOVE:
        // break;
        // case MotionEvent.ACTION_UP:
        // int upX = (int) event.getX();
        // int upY = (int) event.getY();
        // if(Math.abs(upX-downX) < 10 && Math.abs(upY - downY) < 10){//点击事件
        // performClick();
        // doClickAction((upX + downX)/2,(upY + downY)/2);
        // }
        // break;
        // }
        return true;
    }

    /**
     * 初始化列宽行高
     */
    private void initSize() {
        mColumnSize = getWidth() / NUM_COLUMNS;
        mRowSize = getHeight() / NUM_ROWS;
    }

    /**
     * 设置年月
     * 
     * @param year
     * @param month
     */
    public void setSelectYearMonth(int year, int month, int day) {

        mSelYear = year;
        mSelMonth = month;
        mSelDay = day;
    }

    // /**
    // * 设置年月
    // * @param year
    // * @param month
    // */
    // public void setCurrDate(int mCurrYear,int mCurrMonth,int mCurrDay){
    // mSelYear = mCurrYear;
    // mSelMonth = mCurrMonth;
    // mSelDay = mCurrDay;
    //
    // dateee = mCurrYear+"-"+(mCurrMonth+1)+"-"+mCurrDay;
    // }

    /**
     * 执行点击事件
     * 
     * @param x
     * @param y
     */
    public void doClickAction(int x, int y) {
        if (mRowSize == 0 || mColumnSize == 0) {
            return;
        }

        int row = y / mRowSize;
        int column = x / mColumnSize;

        // 获得点击的日（号）
        int day = daysString[row][column];

        if (x == -1 && y == -1) {// 此时是取消另一个月份的选择状态，所以将日改为32
            setSelectYearMonth(mSelYear, mSelMonth, 32);
            invalidate();
            return;
        }

        if (day != 0) {// 点击的是非日期的数字(点击外部)
            setSelectYearMonth(mSelYear, mSelMonth, day);
            invalidate();
            // 执行activity发送过来的点击处理事件
            if (dateClick != null) {
                dateClick.onClickOnDate();
            }
        }
    }

    /**
     * 左点击，日历向后翻页
     */
    public void onLeftClick() {
        int year = mSelYear;
        int month = mSelMonth;
        int day = mSelDay;
        if (month == 0) {// 若果是1月份，则变成12月份
            year = mSelYear - 1;
            month = 11;
        } else if (DateUtils.getMonthDays(year, month) == day) {
            // 如果当前日期为该月最后一点，当向前推的时候，就需要改变选中的日期
            month = month - 1;
            day = DateUtils.getMonthDays(year, month);
        } else {
            month = month - 1;
        }
        setSelectYearMonth(year, month, day);
        invalidate();
    }

    /**
     * 右点击，日历向前翻页
     */
    public void onRightClick() {
        int year = mSelYear;
        int month = mSelMonth;
        int day = mSelDay;
        if (month == 11) {// 若果是12月份，则变成1月份
            year = mSelYear + 1;
            month = 0;
        } else if (DateUtils.getMonthDays(year, month) == day) {
            // 如果当前日期为该月最后一点，当向前推的时候，就需要改变选中的日期
            month = month + 1;
            day = DateUtils.getMonthDays(year, month);
        } else {
            month = month + 1;
        }
        setSelectYearMonth(year, month, day);
        invalidate();
    }

    /**
     * 获取选择的年份
     * 
     * @return
     */
    public int getmSelYear() {
        return mSelYear;
    }

    /**
     * 获取选择的月份
     * 
     * @return
     */
    public int getmSelMonth() {
        return mSelMonth;
    }

    /**
     * 获取选择的日期
     * 
     * @param mSelDay
     */
    public int getmSelDay() {
        return this.mSelDay;
    }

    /**
     * 普通日期的字体颜色，默认黑色
     * 
     * @param mDayColor
     */
    public void setmDayColor(int mDayColor) {
        this.mDayColor = mDayColor;
    }

    /**
     * 选择日期的颜色，默认为白色
     * 
     * @param mSelectDayColor
     */
    public void setmSelectDayColor(int mSelectDayColor) {
        this.mSelectDayColor = mSelectDayColor;
    }

    /**
     * 选中日期的背景颜色，默认蓝色
     * 
     * @param mSelectBGColor
     */
    public void setmSelectBGColor(int mSelectBGColor) {
        this.mSelectBGColor = mSelectBGColor;
    }

    /**
     * 当前日期不是选中的颜色，默认红色
     * 
     * @param mCurrentColor
     */
    public void setmCurrentColor(int mCurrentColor) {
        this.mCurrentColor = mCurrentColor;
    }

    /**
     * 日期的大小，默认18sp
     * 
     * @param mDaySize
     */
    public void setmDaySize(int mDaySize) {
        this.mDaySize = mDaySize;
    }

    /**
     * 设置显示当前日期的控件
     * 
     * @param tv_date
     *            显示日期
     * @param tv_week
     *            显示周
     */
    public void setTextView(TextView tv_date, TextView tv_week) {
        this.tv_date = tv_date;
        this.tv_week = tv_week;
        invalidate();
    }

    /**
     * 设置事务天数
     * 
     * @param daysHasThingList
     */
    public void setDaysHasThingList(List<Integer> daysHasThingList) {
        this.daysHasThingList = daysHasThingList;
    }

    /***
     * 设置圆圈的半径，默认为6
     * 
     * @param mCircleRadius
     */
    public void setmCircleRadius(int mCircleRadius) {
        this.mCircleRadius = mCircleRadius;
    }

    /**
     * 设置圆圈的半径
     * 
     * @param mCircleColor
     */
    public void setmCircleColor(int mCircleColor) {
        this.mCircleColor = mCircleColor;
    }

    /**
     * 设置日期的点击回调事件
     * 
     * @author shiwei.deng
     * 
     */
    public interface DateClick {
        public void onClickOnDate();
    }

    /**
     * 设置日期点击事件
     * 
     * @param dateClick
     */
    public void setDateClick(DateClick dateClick) {
        this.dateClick = dateClick;
    }

    /**
     * 跳转至今天
     */
    public void setTodayToView() {
        setSelectYearMonth(mCurrYear, mCurrMonth, mCurrDay);
        invalidate();
    }

    /**
     * 签到的日子
     */
    private String[] mDays;

    /**
     * 设置签到日期，比如4号，10号，11号签到  days =  {"4","10","11"};
     */
    public void setSignInDate(String[] days) {
        mDays = days;
    }
}
