package com.wtjr.lqg.widget;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wtjr.lqg.R;

/**
 * 左侧图片大小为27*27的组合控件，不带背景的通用item控件(为了解决首页和有需要圆角的父布局使用不了圆角的问题)
 *
 * @author dell
 */
public class UsuallyLayoutItem3 extends RelativeLayout {
    /**
     * 右侧内容
     */
    private TextView tvContentR;
    /**
     * 右侧图标
     */
    private ImageView imageContentR;
    /**
     * 引导下一步箭头
     */
    private ImageView imageNext;
    /**
     * 左侧内容
     */
    private TextView tvContentL;
    /**
     * 左侧图片资源
     */
    private ImageView imageContentL;
    /**
     * 右侧红色提示小圆点
     */
    private TextView tvRedPromptDotR;
    /**
     * 右侧文字大小
     */
    private float TvSizeR;
    /**
     * 左侧文字大小
     */
    private float TvSizeL;
    private ImageView ivContentL;
    private RelativeLayout rlFu;
    private AnimationDrawable anim;
    /**
     * 首页隐藏资产的金币
     */
    private ImageView ivHomeUsuallyGold;


    public UsuallyLayoutItem3(Context context, AttributeSet attrs) {
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

        View view = LayoutInflater.from(context).inflate(R.layout.widget_usually_layout_item3, this, true);
        tvContentR = (TextView) view.findViewById(R.id.tv_content_r);
        imageContentR = (ImageView) view.findViewById(R.id.image_content_r);
        ivHomeUsuallyGold = (ImageView) view.findViewById(R.id.iv_home_usually_gold);
        tvRedPromptDotR = (TextView) view.findViewById(R.id.tv_red_prompt_dot_r);
        imageNext = (ImageView) view.findViewById(R.id.image_next);
        tvContentL = (TextView) view.findViewById(R.id.tv_content_l);
        imageContentL = (ImageView) view.findViewById(R.id.image_content_l);

        ivContentL = (ImageView) view.findViewById(R.id.iv_content_l);

        rlFu = (RelativeLayout) view.findViewById(R.id.rl_fu);
    }

    /**
     * 初始化数据
     *
     * @param context
     * @param attrs
     */
    private void initData(Context context, AttributeSet attrs) {
        if (isInEditMode()) {
            return;
        }
        Resources resources = context.getApplicationContext().getResources();
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.UsuallyLayoutItem);
        //右侧文字
        CharSequence textR = a.getText(R.styleable.UsuallyLayoutItem_com_textTvContentR);
        //右侧文字大小
        TvSizeR = a.getDimension(R.styleable.UsuallyLayoutItem_com_textTvSizeR, resources.getDimension(R.dimen.s15));
        setTextTvContentR(textR, 0);


        //左侧文字距离左侧的间距
        float textTvContentLMarginL = a.getDimension(R.styleable.UsuallyLayoutItem_com_textTvContentLMarginL, resources.getDimension(R.dimen.d0));
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) tvContentL.getLayoutParams();
        layoutParams.leftMargin = (int) textTvContentLMarginL;
        //右侧文字距离右侧的间距
        float textTvContentLMarginR = a.getDimension(R.styleable.UsuallyLayoutItem_com_textTvContentLMarginR, resources.getDimension(R.dimen.d7));
        LinearLayout.LayoutParams layoutParams1 = (LinearLayout.LayoutParams) tvContentR.getLayoutParams();
        layoutParams1.rightMargin = (int) textTvContentLMarginR;

        //设置xml图片
        Drawable drawableR = a.getDrawable(R.styleable.UsuallyLayoutItem_com_ImageContentrScrR);
        if (drawableR != null) {
            imageContentR.setVisibility(View.VISIBLE);
            imageContentR.setImageDrawable(drawableR);
        }
        //显示or隐藏下一步的图标
        boolean show = a.getBoolean(R.styleable.UsuallyLayoutItem_com_ImageNextVisibility, true);
        if (show) {
            imageNext.setVisibility(View.VISIBLE);
        } else {
            imageNext.setVisibility(View.GONE);
        }
        //左侧文字
        CharSequence textL = a.getText(R.styleable.UsuallyLayoutItem_com_textTvContentL);
        //左侧文字大小
        TvSizeL = a.getDimension(R.styleable.UsuallyLayoutItem_com_textTvSizeL, resources.getDimension(R.dimen.s15));
        setTextTvContentL(textL);

        //设置xml图片
        Drawable drawableL = a.getDrawable(R.styleable.UsuallyLayoutItem_com_ImageContentrScrL);
        if (drawableL != null) {
            imageContentL.setVisibility(View.VISIBLE);
            imageContentL.setImageDrawable(drawableL);
        }
        //左侧文字大小
        float leftMarginSize = a.getDimension(R.styleable.UsuallyLayoutItem_com_ImageLeftMarginL, resources.getDimension(R.dimen.d0));
        leftMargin(leftMarginSize);
        a.recycle();
    }

    /**
     * 设置右侧输入框输入的内容
     *
     * @param content
     */
    public void setTextTvContentR(CharSequence content, int rId) {
        if (rId != 0) {
            tvContentR.setTextColor(getResources().getColor(rId));
        }

        tvContentR.setTextSize(TypedValue.COMPLEX_UNIT_PX, TvSizeR);

        if (!TextUtils.isEmpty(content)) {
            tvContentR.setVisibility(View.VISIBLE);
            tvContentR.setText(content);
        } else {
            tvContentR.setVisibility(View.GONE);
        }


//		if(content != null && rId != 0) {
//		    try {
//		        double parseDouble = Double.parseDouble(content.toString().replace(",", ""));
//		        
//		        if(parseDouble > 0) {
//	                tvContentR.setTextColor(getResources().getColor(R.color.FC_FF8D6A));
//	            }else {
//	                tvContentR.setTextColor(getResources().getColor(R.color.FC_999999));
//	            }
//            } catch (Exception e) {//证明不是数字
//                tvContentR.setTextColor(getResources().getColor(R.color.FC_999999));
//            }
//		}
    }

    /**
     * 显示右侧指示箭头
     */
    public void setShowImageNext() {
        imageNext.setVisibility(View.VISIBLE);
    }

    /**
     * 设置显示隐藏资产
     *
     * @param showAsset true为显示资产 false 为不显示资产
     */
    public void setShowAsset(boolean showAsset) {
        if (showAsset) {
            ivHomeUsuallyGold.setVisibility(View.GONE);
            tvContentR.setVisibility(View.VISIBLE);
        } else {
            ivHomeUsuallyGold.setVisibility(View.VISIBLE);
            tvContentR.setVisibility(View.GONE);
        }
    }

    /**
     * 设置右侧图片资源
     *
     * @param resId
     */
    public void setImageContentrResourceR(int resId) {
        if (!TextUtils.isEmpty(resId + "")) {
            imageContentR.setVisibility(View.VISIBLE);
            imageContentR.setImageResource(resId);
        } else {
            imageContentR.setVisibility(View.GONE);
        }
    }

    /**
     * 设置右侧图片资源
     *
     * @param resId
     */
    public void setImageContentrMarginR(int resId) {
        if (!TextUtils.isEmpty(resId + "")) {

        }
    }

    /**
     * 设置左侧图片资源
     */
    public void setRedPromptDot(CharSequence content) {
        if (!TextUtils.isEmpty(content + "")) {
            tvRedPromptDotR.setVisibility(View.VISIBLE);
            tvRedPromptDotR.setText(content);

        } else {
            tvRedPromptDotR.setVisibility(View.GONE);
        }
    }


    /**
     * 设置左侧输入框输入的内容
     *
     * @param content
     */
    public void setTextTvContentL(CharSequence content) {
        if (!TextUtils.isEmpty(content)) {
            tvContentL.setVisibility(View.VISIBLE);
            tvContentL.setText(content);
        } else {
            tvContentL.setVisibility(View.GONE);
        }
        tvContentL.setTextSize(TypedValue.COMPLEX_UNIT_PX, TvSizeL);
    }

    /**
     * 设置左侧图片资源
     *
     * @param resId 0左边的图片GONE,-1左边的图片INVISIBLE,其他左边的图片VISIBLE
     */
    public void setImageContentrResourceL(int resId) {
        if (resId == 0) {
            imageContentL.setVisibility(View.GONE);
        } else if (resId == -1) {
            imageContentL.setVisibility(View.INVISIBLE);
        } else {
            imageContentL.setVisibility(View.VISIBLE);
            imageContentL.setImageResource(resId);
        }
    }

    /**
     * 设置左侧图片资源
     *
     * @param resId
     */
    public void setIvContentL(int resId) {
        if (resId == 0) {
            ivContentL.setVisibility(View.GONE);
        } else {
            ivContentL.setVisibility(View.VISIBLE);
            ivContentL.setImageResource(resId);
        }
    }


    /**
     * 设置左侧图片距离左侧的编剧
     */
    private void leftMargin(float leftMargin) {
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) imageContentL.getLayoutParams();
        layoutParams.leftMargin = (int) leftMargin;
        imageContentL.setLayoutParams(layoutParams);

    }

    /**
     * 设置下一步箭头旋转
     */
    public void setImageNextRotate(boolean isOpen) {
        final float centerX = imageNext.getWidth() / 2.0f;
        final float centerY = imageNext.getHeight() / 2.0f;
        RotateAnimation rotateAnimation;
        // 这个是设置需要旋转的角度，我设置的是180度
        if (isOpen) {
            rotateAnimation = new RotateAnimation(90, 0, centerX, centerY);
        } else {
            rotateAnimation = new RotateAnimation(0, 90, centerX, centerY);
        }
        rotateAnimation.setDuration(300);// 旋转时长
        rotateAnimation.setFillAfter(true);
        imageNext.startAnimation(rotateAnimation);
    }

    /**
     * 获得下一步图片左侧的图片控件
     *
     * @return
     */
    public ImageView getNextLeftImage() {
        return imageContentR;
    }

    public void setViewBackground(int resid) {
        rlFu.setBackgroundResource(resid);
    }
}
