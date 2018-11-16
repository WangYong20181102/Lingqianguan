package com.wtjr.lqg.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wtjr.lqg.R;

/**
 * 底部导航条item
 *
 * @author Myf
 */
public class NavigationBarItem extends RelativeLayout {
    /**
     * 导航条文字item
     */
    private TextView tvItem;
    /**
     * 导航条图片
     */
    private ImageView imageItem;
    /**
     * 首页小红点
     */
    private View vHomeDot;


    public NavigationBarItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
        if (!isInEditMode()) {
            initData(context, attrs);
        }
    }

    private void initView(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.widget_foot_item, this, true);
        tvItem = (TextView) view.findViewById(R.id.tv_item);
        vHomeDot = (View) view.findViewById(R.id.v_home_dot);
        imageItem = (ImageView) view.findViewById(R.id.image_item);
    }

    /**
     * 初始化数据
     *
     * @param context
     */
    private void initData(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.NavigationBarItem);
        //图片
        int itemImageId = a.getResourceId(R.styleable.NavigationBarItem_nbi_image, R.drawable.home_page_select);
        setItemImage(itemImageId);

        //文字内容
        String itemContent = a.getString(R.styleable.NavigationBarItem_nbi_content);
        setItemContent(itemContent);

        //文字颜色
        int itemTextColor = a.getColor(R.styleable.NavigationBarItem_nbi_textColor, Color.BLACK);
        setItemTextColor(itemTextColor);


        a.recycle();
    }

    /**
     * 设置图片
     *
     * @param resId 传入图片的id
     */
    public void setItemImage(int resId) {
        if (!TextUtils.isEmpty(resId + "")) {
            imageItem.setImageResource(resId);
        }

    }

    /**
     * 设置item的内容
     *
     * @param content 内容
     */
    public void setItemContent(String content) {
        if (!TextUtils.isEmpty(content)) {
            tvItem.setText(content);
        }

    }

    /**
     * 设置字体颜色
     *
     * @param color 颜色
     */
    public void setItemTextColor(int color) {
        if (!TextUtils.isEmpty(color + "")) {
            tvItem.setTextColor(color);
        }
    }

    /**
     * 显示红点
     *
     * @param isShow 是显示的
     */
    public void showRedDot() {
        vHomeDot.setVisibility(View.VISIBLE);

    }

    /**
     * 取消红点
     */
    public void cancelRedDot() {
        vHomeDot.setVisibility(View.GONE);
    }

}
