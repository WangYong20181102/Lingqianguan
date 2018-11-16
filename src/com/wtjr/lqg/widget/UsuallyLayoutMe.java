package com.wtjr.lqg.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wtjr.lqg.R;

/**
 * Created by WangYong on 2017/11/2.
 */

public class UsuallyLayoutMe extends LinearLayout {

    /**
     * 图片资源
     */
    private Drawable image;
    /**
     * 文字内容
     */
    private String strContent;
    /**
     * 文字大小
     */
    private float fTextSize;
    /**
     * 文字颜色
     */
    private int textColor;

    /**
     * 图片
     */
    private ImageView reuseImage;
    /**
     * 内容
     */
    private TextView reuseTextView;
    private float textMarginTop;

    public UsuallyLayoutMe(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
        initAttr(context, attrs);
    }

    /**
     * 初始化控件
     */
    private void initView(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.widget_usually_layout_me, this, true);
        reuseImage = (ImageView) view.findViewById(R.id.reuse_image);
        reuseTextView = (TextView) view.findViewById(R.id.reuse_textview);
    }

    private void initAttr(Context context, AttributeSet attrs) {
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.UsuallyLayoutHome);
        image = array.getDrawable(R.styleable.UsuallyLayoutHome_reuse_icon);
        strContent = array.getString(R.styleable.UsuallyLayoutHome_reuse_context);
        fTextSize = array.getDimension(R.styleable.UsuallyLayoutHome_reuse_textSize, 28);
        textColor = array.getColor(R.styleable.UsuallyLayoutHome_reuse_textColor, R.color.FC_F5F5F5);
        textMarginTop = array.getDimension(R.styleable.UsuallyLayoutHome_reuse_text_margin_top, 11);

        reuseImage.setImageDrawable(image);
        reuseTextView.setText(strContent);
        reuseTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, fTextSize);
        reuseTextView.setTextColor(textColor);
        RelativeLayout.LayoutParams layoutParams1 = (RelativeLayout.LayoutParams) reuseTextView.getLayoutParams();
        layoutParams1.topMargin = (int) textMarginTop;


        array.recycle();
    }
}
