package com.wtjr.lqg.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import android.annotation.SuppressLint;
import android.content.Context;

import com.wtjr.lqg.sharedpreferences.SharedPreferencesUtil;

import static android.R.attr.format;

/**
 * 时间格式工具类
 *
 * @author Myf
 */
public class TimeUtil {
    private TimeUtil() {
        /* cannot be instantiated */
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    /**
     * 获得年
     */
    @SuppressLint("SimpleDateFormat")
    public static String getYear(long format) {
        return new SimpleDateFormat("yyyy年").format(new Date(format));
    }

    /**
     * 获得月
     */
    @SuppressLint("SimpleDateFormat")
    public static String getMonth(long format) {
        return new SimpleDateFormat("MM月").format(new Date(format));
    }

    /**
     * 获得日
     */
    @SuppressLint("SimpleDateFormat")
    public static String getDay(long format) {
        return new SimpleDateFormat("dd日").format(new Date(format));
    }

    /**
     * 获得时
     */
    @SuppressLint("SimpleDateFormat")
    public static String getHours(long format) {
        return new SimpleDateFormat("HH时").format(new Date(format));
    }

    /**
     * 获得分
     */
    @SuppressLint("SimpleDateFormat")
    public static String getMinutes(long format) {
        return new SimpleDateFormat("mm分").format(new Date(format));
    }

    /**
     * 获得秒
     */
    @SuppressLint("SimpleDateFormat")
    public static String getSeconds(long format) {
        return new SimpleDateFormat("ss秒").format(new Date(format));
    }

    /**
     * 格式化时间，获得年月格式为（YYYY年MM月）
     *
     * @param format
     * @return
     */
    @SuppressLint("SimpleDateFormat")
    public static String formatYYYYMM(String format) {
        StringBuffer time = new StringBuffer();
        time.append(format.substring(0, 4)).append("年").append(format.substring(4)).append("月");
        return time.toString();
    }

    /**
     * 获得年月日格式为（yyyy/MM/dd）
     */
    @SuppressLint("SimpleDateFormat")
    public static String getY_M_D_Type1(long format) {
        return new SimpleDateFormat("yyyy/MM/dd").format(new Date(format));
    }

    /**
     * 获得年月日格式为（yyyy年MM月dd日）
     */
    @SuppressLint("SimpleDateFormat")
    public static String getY_M_D_Type2(long format) {
        return new SimpleDateFormat("yyyy年MM月dd日").format(new Date(format));
    }
    /**
     * 获得年月日格式为（yyyy-MM-dd）
     */
    @SuppressLint("SimpleDateFormat")
    public static String getY_M_D_Type3(long format) {
        return new SimpleDateFormat("yyyy-MM-dd").format(new Date(format));
    }

    /**
     * 获得时分秒格式为（HH:mm:ss）
     */
    @SuppressLint("SimpleDateFormat")
    public static String getH_M_S(long format) {
        return new SimpleDateFormat("HH:mm:ss").format(new Date(format));
    }

    /**
     * 获得时分格式为（HH:mm）
     */
    @SuppressLint("SimpleDateFormat")
    public static String getH_M(long format) {
        return new SimpleDateFormat("HH:mm").format(new Date(format));
    }

    /**
     * 获得月日时分格式为（MM/dd HH:mm）
     */
    @SuppressLint("SimpleDateFormat")
    public static String getM_D_H_M(long format) {
        return new SimpleDateFormat("MM/dd HH:mm").format(new Date(format));
    }

    /**
     * 获得月日时分格式为（MM-dd  HH:mm）
     */
    @SuppressLint("SimpleDateFormat")
    public static String getM_D__H_M(long format) {
        return new SimpleDateFormat("MM-dd  HH:mm").format(new Date(format));
    }

    /**
     * 获得年月日时分秒（yyyy/MM/dd HH:mm:ss）
     */
    @SuppressLint("SimpleDateFormat")
    public static String getTimeType(long format) {
        return new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date(format));
    }

    /**
     * 获得年月日时分秒（yyyy-MM-dd HH:mm:ss）
     */
    @SuppressLint("SimpleDateFormat")
    public static String getTimeType2(long format) {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(format));
    }
    /**
     * 获得年月日时分（yyyy-MM-dd HH:mm）
     */
    @SuppressLint("SimpleDateFormat")
    public static String getTimeType3(long format) {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date(format));
    }

    /**
     * 自定义时间格式
     *
     * @param format
     * @param format "yyyy-MM-dd HH:mm:ss"当前系统时间的时间格式
     * @return
     */
    public static String getFullTime(String time, String format) {
        long mTime = 0;
        try {
            mTime = Long.parseLong(time);
        } catch (NumberFormatException e) {
            throw new NumberFormatException("数字格式异常:" + e.getMessage());
        }
        int length = (mTime + "").length();
        switch (length) {
            case 10:
                return new SimpleDateFormat(format).format(new Date(mTime * 1000));
            case 13:
                return new SimpleDateFormat(format).format(new Date(mTime));
            default:
                return "1970-01-01 08:00:00";
        }
    }

    /**
     * 自定义时间格式
     *
     * @param format
     * @param format "yyyy-MM-dd HH:mm:ss"当前系统时间的时间格式
     * @return
     */
    public static String getFullTime(long time, String format) {
        int length = (time + "").length();
        switch (length) {
            case 10:
                return new SimpleDateFormat(format).format(new Date(time * 1000));
            case 13:
                return new SimpleDateFormat(format).format(new Date(time));
            default:
                return new SimpleDateFormat(format).format(new Date(time));
        }
    }

    /**
     * 将字符串时间转化成时间戳(time的格式与format的格式一样)
     *
     * @param time   1970/01/01 08:00:00
     * @param format yyyy/MM/dd HH:mm:ss
     * @return
     */
    public static long getTimeTransformation(String time, String format) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        Date date = null;
        try {
            date = simpleDateFormat.parse(time);
        } catch (ParseException e) {
            throw new UnsupportedOperationException("解析异常" + e.getMessage());
        }
        return (date.getTime());
    }


    /**
     * 存储倒计时计时器的时间
     */
    public static int setCountdownTimerState(Context context, String key, String phone) {
        //第一次拿号码为0
        String number = SharedPreferencesUtil.getPrefString(context, "number", "0");

        int currentCountdown = 180;

        long currentTimeMillis = System.currentTimeMillis();
        //倒计时取出
        long registerCountdown = SharedPreferencesUtil.getPrefLong(context, key, 0);
        //如果当前的时间小于等于存储的时间，就要重新180秒倒计时
        if (currentTimeMillis <= registerCountdown) {
            //文件存储的和查找的不是一样的就取180秒，并且重新开始计算，如果一样的就取记录的时间
            if (number.equals(phone)) {
                //一样
                currentCountdown = (int) ((registerCountdown - currentTimeMillis) / 1000);

            } else {
                //不一样
                currentCountdown = 180;
            }
        } else {
            currentCountdown = 180;
        }
        SharedPreferencesUtil.setPrefString(context, "number", phone);
        //存储的时间
        SharedPreferencesUtil.setSettingLong(context, key, currentTimeMillis + currentCountdown * 1000);
        return currentCountdown;
    }

    /**
     * 是否重新获取短信验证码，返回true,需要请求网络重新发送，false反之
     *
     * @param context
     * @param key
     * @param phone
     * @return
     */
    public static boolean getCountdownTimerState(Context context, String key, String phone) {
        //倒计时取出
        long timeRemaining = SharedPreferencesUtil.getPrefLong(context, key, 0);
        String number = SharedPreferencesUtil.getPrefString(context, "number", "0");

        if (System.currentTimeMillis() <= timeRemaining && phone.equals(number)) {
            return false;//不发送
        } else {
            return true;//发送
        }
    }

    /**
     * 获得剩余时间
     *
     * @param context
     * @param key
     * @param phone
     * @return
     */
    public static Long getTimeRemaining(Context context, String key, String phone) {
        long timeRemaining = 0;
        //false表示还不需要发生，
        boolean countdownTimerState = getCountdownTimerState(context, key, phone);
        if (!countdownTimerState) {
            //倒计时取出
            long saveTimeRemaining = SharedPreferencesUtil.getPrefLong(context, key, 0);
            timeRemaining = saveTimeRemaining - System.currentTimeMillis();
        }
        return timeRemaining;
    }

    /**
     * 清理倒计时
     *
     * @param context
     * @param key
     * @param phone
     */
    public static void cleanTimeRemaining(Context context, String key, String phone) {
        //存储的时间
        SharedPreferencesUtil.setSettingLong(context, key, System.currentTimeMillis());
    }


    /**
     * 将秒转换成几时几分几秒
     *
     * @param <T>
     * @param second 秒
     * @return
     */
    public static <T> String formatSecond(T second) {
        String html = "0s";
        if (second != null) {
            Double s = Double.parseDouble(second + "");
            String format;
            Object[] array;
            Integer hours = (int) (s / (60 * 60));
            Integer minutes = (int) (s / 60 - hours * 60);
            Integer seconds = (int) (s - minutes * 60 - hours * 60 * 60);
            if (hours > 0) {
                format = "%1$,02d:%2$,02d:%3$,02d";
                array = new Object[]{hours, minutes, seconds};
            } else if (minutes > 0) {
                format = "%1$,02d:%2$,02d";
                array = new Object[]{minutes, seconds};
            } else {
                format = "%1$,ds";
                array = new Object[]{seconds};
            }
            html = String.format(format, array);
        }
        return html;
    }

    /**
     * 计算两个日期之间相差的天数
     *
     * @param smdate 较小的时间
     * @param bdate  较大的时间
     * @return 相差天数
     * @throws ParseException
     */
    public static int daysBetween(Date smdate, Date bdate) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        smdate = sdf.parse(sdf.format(smdate));
        bdate = sdf.parse(sdf.format(bdate));
        Calendar cal = Calendar.getInstance();
        cal.setTime(smdate);
        long time1 = cal.getTimeInMillis();
        cal.setTime(bdate);
        long time2 = cal.getTimeInMillis();
        long between_days = (time2 - time1) / (1000 * 3600 * 24);

        return Integer.parseInt(String.valueOf(between_days));
    }

    /**
     * 计算开始时间与系统当前时间之间相差的分钟数
     *
     * @param smdate 开始时间
     * @return 相差分钟数
     * @throws ParseException
     */
    public static int minuteBetween(long smdate) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
        Date endtime = format.parse(getH_M_S(smdate));  //开始抢标时间
        Date starttime = format.parse(format.format(new Date()));       //系统当前时间
        long seconds = endtime.getTime() - starttime.getTime();
        return (int) seconds / (1000 * 60);
    }

    /**
     * 字符串的日期格式的计算
     */
    public static int daysBetween(String smdate, String bdate) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        cal.setTime(sdf.parse(smdate));
        long time1 = cal.getTimeInMillis();
        cal.setTime(sdf.parse(bdate));
        long time2 = cal.getTimeInMillis();
        long between_days = (time2 - time1) / (1000 * 3600 * 24);

        return Integer.parseInt(String.valueOf(between_days));
    }
}
