package com.wtjr.lqg.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wtjr.lqg.R;


/**
 * 通用样式
 *
 * @author Myf
 */
public class UsuallyLayoutStyle extends RelativeLayout {
    /**
     * 图标
     */
    private ImageView imageView;
    /**
     * 上面的文字
     */
    private TextView tvUp;
    /**
     * 下面的文字
     */
    private TextView tvDown;
    /**
     * 打开或者关闭，默认是打开iade
     */
    private boolean isCloseAndOpen = true;
    private int color;
    /**
     * 首页隐藏资产的金币
     */
    private ImageView ivHomeUsuallyGold;

    public UsuallyLayoutStyle(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
        initData(context, attrs);
    }

    /**
     * 初始化控件
     *
     * @param context
     */
    private void initView(Context context) {
        if (isInEditMode()) {
            return;
        }

        View view = LayoutInflater.from(context).inflate(R.layout.widget_usually_layout_style, this, true);
        imageView = (ImageView) view.findViewById(R.id.iamge);
        ivHomeUsuallyGold = (ImageView) view.findViewById(R.id.iv_home_usually_gold);
        tvUp = (TextView) view.findViewById(R.id.tv_up);
        tvDown = (TextView) view.findViewById(R.id.tv_down);
    }

    private void initData(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.UsuallyLayoutStyle);
        // 上面文字
        CharSequence strTvUp = a.getText(R.styleable.UsuallyLayoutStyle_com_tv_up);
        // 下面文字
        CharSequence strTvDown = a.getText(R.styleable.UsuallyLayoutStyle_com_tv_down);
        //下面文字颜色
        color = a.getColor(R.styleable.UsuallyLayoutStyle_com_tv_down_color, Color.parseColor("#fa8f21"));
        // 图片
        Drawable image = a.getDrawable(R.styleable.UsuallyLayoutStyle_com_image);

        setTextUpContent(strTvUp);
        setTextDownContent(strTvDown);
        setImageDrawable(image);
    }
    /**
     * 设置显示隐藏资产
     *
     * @param showAsset true为显示资产 false 为不显示资产
     */
    public void setShowAsset(boolean showAsset) {
        if (showAsset) {
            ivHomeUsuallyGold.setVisibility(View.GONE);
            tvDown.setVisibility(View.VISIBLE);
        } else {
            ivHomeUsuallyGold.setVisibility(View.VISIBLE);
            tvDown.setVisibility(View.GONE);
        }
    }
    /**
     * 设置文字上面内容
     */
    public void setTextUpContent(CharSequence strTvUp) {
        if (isInEditMode()) {
            return;
        }
        tvUp.setText(strTvUp + "");
    }

    /**
     * 设置文字下面内容
     */
    public void setTextDownContent(CharSequence strTvDown) {
        if (isInEditMode()) {
            return;
        }
        if (isCloseAndOpen) {
            tvDown.setText(strTvDown + "");
            tvDown.setTextColor(color);
        } else {
            tvDown.setText("****");
        }

    }

    /**
     * 设置图片
     */
    public void setImageDrawable(Drawable drawable) {
        if (isInEditMode()) {
            return;
        }
        imageView.setImageDrawable(drawable);
    }

    /**
     * 设置文字下面内容闭眼
     */
    public void closeDownContent() {
        isCloseAndOpen = false;
    }

    /**
     * 设置文字下面内容开眼
     */
    public void openDownContent() {
        isCloseAndOpen = true;
    }
}
