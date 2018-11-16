package com.wtjr.lqg.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wtjr.lqg.R;

/**
 * Created by WangYong on 2017/10/12.
 */

public class AboutLQGLayoutItem extends LinearLayout {

    /**
     * 文本内容
     */
    private TextView tvContent;

    public AboutLQGLayoutItem(Context context, AttributeSet attrs) {
        super(context, attrs);

        initView(context);
        initData(context, attrs);
    }

    /**
     * 初始化数据
     */
    private void initData(Context context, AttributeSet attrs) {
        if (isInEditMode()) {
            return;
        }
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.AboutLQGLayoutItem);
        String strContent = array.getString(R.styleable.AboutLQGLayoutItem_com_textContent);
        tvContent.setText(strContent);
        array.recycle();
    }

    /**
     * 初始化控件
     */
    private void initView(Context context) {
        if (isInEditMode()) {
            return;
        }
        View view = LayoutInflater.from(context).inflate(R.layout.activity_about_lqg_layout_item, this, true);
        tvContent = (TextView) view.findViewById(R.id.about_lqg_text_content);
    }

    /**
     * 设置文本
     */
    public void setTextContext(String str) {
        if (!TextUtils.isEmpty(str)) {
            tvContent.setText(str);
        }
    }
}
