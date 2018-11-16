package com.wtjr.lqg.widget.DatePicker;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.app.Dialog;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.wtjr.lqg.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;


/**
 * Created by liuwan on 2016/9/28.
 */
public class CustomDatePicker {

    private ResultHandler handler;

    /**
     * 定义结果回调接口
     */
    public interface ResultHandler {
        void handle(String time);

    }

    private Context context;
    private boolean canAccess = false;

    private Dialog datePickerDialog;
    private DatePickerView year_pv, month_pv;

    private static final int MAX_MONTH = 12;

    private ArrayList<String> year, month;
    private int startYear, startMonth, endYear, endMonth;
    private boolean spanYear, spanMon;
    private Calendar selectedCalender, startCalendar, endCalendar;
    private TextView tv_cancle, tv_select;

    private TextView tv_year_pv;
    private TextView tv_month_pv;
    /**
     * 最近三个月
     */
    private String lastThreeMonths;

    public CustomDatePicker(Context context, ResultHandler resultHandler, String startDate, String endDate) {
        if (isValidDate(startDate, "yyyy-MM") && isValidDate(endDate, "yyyy-MM")) {
            canAccess = true;
            this.context = context;
            this.handler = resultHandler;
            selectedCalender = Calendar.getInstance();
            startCalendar = Calendar.getInstance();
            endCalendar = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM", Locale.CHINA);
            try {
                startCalendar.setTime(sdf.parse(startDate));
                endCalendar.setTime(sdf.parse(endDate));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            initDialog();
            initView();
        }
    }

    /**
     * 初始化dialog
     */
    private void initDialog() {
        if (datePickerDialog == null) {
            datePickerDialog = new Dialog(context, R.style.time_dialog);
            datePickerDialog.setCancelable(true);
            datePickerDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            datePickerDialog.setContentView(R.layout.custom_date_picker);
            Window window = datePickerDialog.getWindow();
            window.setGravity(Gravity.BOTTOM);
            WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            DisplayMetrics dm = new DisplayMetrics();
            manager.getDefaultDisplay().getMetrics(dm);
            WindowManager.LayoutParams lp = window.getAttributes();
            lp.width = dm.widthPixels;
            window.setAttributes(lp);
        }
    }

    private void initView() {
        year_pv = (DatePickerView) datePickerDialog.findViewById(R.id.year_pv);
        month_pv = (DatePickerView) datePickerDialog.findViewById(R.id.month_pv);
        tv_cancle = (TextView) datePickerDialog.findViewById(R.id.tv_cancle);
        tv_select = (TextView) datePickerDialog.findViewById(R.id.tv_select);
        tv_year_pv = (TextView) datePickerDialog.findViewById(R.id.tv_year_pv);
        tv_month_pv = (TextView) datePickerDialog.findViewById(R.id.tv_month_pv);

        tv_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePickerDialog.dismiss();
            }
        });

        tv_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM", Locale.CHINA);
                handler.handle(sdf.format(selectedCalender.getTime()));
                datePickerDialog.dismiss();
            }
        });
    }

    private void initParameter() {
        startYear = startCalendar.get(Calendar.YEAR);
        startMonth = startCalendar.get(Calendar.MONTH) + 1;
        endYear = endCalendar.get(Calendar.YEAR);
        endMonth = endCalendar.get(Calendar.MONTH) + 1;
        if (endMonth <= 2) {
            lastThreeMonths = (endMonth - 2) + 12 + "-" + endMonth + "月";
        } else {
            lastThreeMonths = endMonth - 2 + "-" + endMonth + "月";
        }
        spanYear = startYear != endYear;
        spanMon = (!spanYear) && (startMonth != endMonth);
        selectedCalender.setTime(startCalendar.getTime());
    }

    private void initTimer() {
        initArrayList();
        if (spanYear) {
            for (int i = startYear; i <= endYear; i++) {
                year.add(String.valueOf(i));
            }
            for (int i = startMonth; i <= MAX_MONTH; i++) {
                month.add(formatTimeUnit(i));
            }
        } else if (spanMon) {
            year.add(String.valueOf(startYear));
            for (int i = startMonth; i <= endMonth; i++) {
                month.add(formatTimeUnit(i));
            }
        }
        loadComponent();
    }

    /**
     * 将“0-9”转换为“00-09”
     */
    private String formatTimeUnit(int unit) {
        return unit < 10 ? "0" + String.valueOf(unit) : String.valueOf(unit);
    }

    private void initArrayList() {
        if (year == null) year = new ArrayList<>();
        if (month == null) month = new ArrayList<>();
        year.clear();
        month.clear();
    }

    private void loadComponent() {

        year.add("最近三个月");
        year_pv.setData(year);
        month_pv.setData(month);
        year_pv.setSelected(0);
        month_pv.setSelected(0);
        executeScroll();
    }

    private void addListener() {
        year_pv.setOnSelectListener(new DatePickerView.onSelectListener() {
            @Override
            public void onSelect(String text) {
                if (text.equals("最近三个月")) {
                    selectedCalender.set(Calendar.YEAR, 000000);
                    //最近三个月
                    month.clear();
                    month.add(lastThreeMonths);
                    month_pv.setData(month);
                    tv_year_pv.setVisibility(View.INVISIBLE);
                    tv_month_pv.setVisibility(View.INVISIBLE);
                } else {
                    selectedCalender.set(Calendar.YEAR, Integer.parseInt(text));
                    tv_year_pv.setVisibility(View.VISIBLE);
                    tv_month_pv.setVisibility(View.VISIBLE);
                }
                monthChange();
            }
        });

        month_pv.setOnSelectListener(new DatePickerView.onSelectListener() {
            @Override
            public void onSelect(String text) {
                //最近三个月
                if (text.length() > 3) {
                    month.clear();
                    month.add(lastThreeMonths);
                    month_pv.setData(month);
                } else {
                    selectedCalender.set(Calendar.DAY_OF_MONTH, 1);
                    selectedCalender.set(Calendar.MONTH, Integer.parseInt(text) - 1);
                }
            }
        });

    }

    private void monthChange() {
        month.clear();
        int selectedYear = selectedCalender.get(Calendar.YEAR);
        if (selectedYear == startYear) {
            for (int i = startMonth; i <= MAX_MONTH; i++) {
                month.add(formatTimeUnit(i));
            }
            selectedCalender.set(Calendar.MONTH, Integer.parseInt(month.get(0)) - 1);
        } else if (selectedYear == endYear) {
            for (int i = 1; i <= endMonth; i++) {
                month.add(formatTimeUnit(i));
            }
            selectedCalender.set(Calendar.MONTH, Integer.parseInt(month.get(0)) - 1);
        } else if (selectedYear == 1) {//最近三个月
            month.add(lastThreeMonths);
        } else {
            for (int i = 1; i <= MAX_MONTH; i++) {
                month.add(formatTimeUnit(i));
            }
            selectedCalender.set(Calendar.MONTH, Integer.parseInt(month.get(0)) - 1);
        }

        month_pv.setData(month);
        month_pv.setSelected(0);
        executeAnimator(month_pv);
    }

    private void executeAnimator(View view) {
        PropertyValuesHolder pvhX = PropertyValuesHolder.ofFloat("alpha", 1f, 0f, 1f);
        PropertyValuesHolder pvhY = PropertyValuesHolder.ofFloat("scaleX", 1f, 1.3f, 1f);
        PropertyValuesHolder pvhZ = PropertyValuesHolder.ofFloat("scaleY", 1f, 1.3f, 1f);
        ObjectAnimator.ofPropertyValuesHolder(view, pvhX, pvhY, pvhZ).setDuration(200).start();
    }


    private void executeScroll() {
        year_pv.setCanScroll(year.size() > 1);
        month_pv.setCanScroll(month.size() > 0);
    }

    public void show(String time) {
        if (canAccess) {
            if (isValidDate(time, "yyyy-MM")) {
                if (startCalendar.getTime().getTime() < endCalendar.getTime().getTime()) {
                    canAccess = true;
                    initParameter();
                    initTimer();
                    addListener();
                    setSelectedTime(time);
                    datePickerDialog.show();
                }
            } else {
                canAccess = false;
            }
        }
    }


    /**
     * 设置日期控件是否可以循环滚动
     */
    public void setIsLoop(boolean isLoop) {
        if (canAccess) {
            this.year_pv.setIsLoop(isLoop);
            this.month_pv.setIsLoop(isLoop);
        }
    }

    /**
     * 设置日期控件默认选中的时间
     */
    public void setSelectedTime(String time) {
        if (canAccess) {
            String[] dateStr = time.split("-");

            year_pv.setSelected("最近三个月");
//            year_pv.setSelected(dateStr[0]);
//            selectedCalender.set(Calendar.YEAR, Integer.parseInt(dateStr[0]));
            selectedCalender.set(Calendar.YEAR, 0000);

            month.clear();
//            int selectedYear = selectedCalender.get(Calendar.YEAR);
//            if (selectedYear == startYear) {
//                for (int i = startMonth; i <= MAX_MONTH; i++) {
//                    month.add(formatTimeUnit(i));
//                }
//            } else if (selectedYear == endYear) {
//                for (int i = 1; i <= endMonth; i++) {
//                    month.add(formatTimeUnit(i));
//                }
//            } else {
//                for (int i = 1; i <= MAX_MONTH; i++) {
//                    month.add(formatTimeUnit(i));
//                }
//            }
            month.add(lastThreeMonths);
            month_pv.setData(month);
            month_pv.setSelected(lastThreeMonths);
            tv_year_pv.setVisibility(View.INVISIBLE);
            tv_month_pv.setVisibility(View.INVISIBLE);
//            month_pv.setSelected(dateStr[1]);
            selectedCalender.set(Calendar.MONTH, Integer.parseInt(dateStr[1]) - 1);
            executeAnimator(month_pv);

            executeScroll();
        }
    }

    /**
     * 验证字符串是否是一个合法的日期格式
     */
    private boolean isValidDate(String date, String template) {
        boolean convertSuccess = true;
        // 指定日期格式
        SimpleDateFormat format = new SimpleDateFormat(template, Locale.CHINA);
        try {
            // 设置lenient为false. 否则SimpleDateFormat会比较宽松地验证日期，比如2015/02/29会被接受，并转换成2015/03/01
            format.setLenient(false);
            format.parse(date);
        } catch (Exception e) {
            // 如果throw java.text.ParseException或者NullPointerException，就说明格式不对
            convertSuccess = false;
        }
        return convertSuccess;
    }

}
